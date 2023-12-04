/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Ricerca sintetica di una PrimaNota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/12/2017
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaPrimaNotaIntegrataManualeService extends CheckedAccountBaseService<RicercaSinteticaPrimaNotaIntegrataManuale, RicercaSinteticaPrimaNotaIntegrataManualeResponse> {
	
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired 
	private PrimaNotaDad primaNotaDad;
	
	private PrimaNota primaNota;
	
	private TipoEvento tipoEvento;
	
	private List<Evento> eventi;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione());
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
		checkNotNull(primaNota.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione"));
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaPrimaNotaIntegrataManualeResponse executeService(RicercaSinteticaPrimaNotaIntegrataManuale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		primaNotaDad.setEnte(ente);
		codificaDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		handleEvento();
		handleAnnoNumeroSub();
		ListaPaginata<PrimaNota> list;
		try {
			list = primaNotaDad.ricercaSinteticaPrimeNoteIntegrate(//importoDocumentoDa, importoDocumentoA, capitolo, parametriPaginazione)(
				req.getBilancio(),
				primaNota,
				req.getConto(),
				null,
				tipoEvento,
				eventi,
				annoMovimento,
				numeroMovimento,
				numeroSubmovimento,
				null,
				req.getCausaleEP(),
				req.getDataRegistrazioneDa(),
				req.getDataRegistrazioneA(),
				req.getDataRegistrazioneProvvisoriaDa(),
				req.getDataRegistrazioneProvvisoriaA(),
				null,
				null,
				null,
				null,
				null,
				null,
				req.getImporto(),
				req.getImporto(),
				null,
				req.getParametriPaginazione());
		} catch(DadException de) {
			// Ignoro l'errore
			// TODO: loggare qualcosa?
			res.addErrore(ErroreBil.ERRORE_GENERICO.getErrore(de.getMessage()));
			return;
		}
		
		res.setCardinalitaComplessiva(list.size());
		res.setPrimeNote(list);
		
	}

	/**
	 * Gestione dell'evento.
	 * <br/>
	 * L'evento pu&ograve; essere fornito dal chiamante come EXTR. Questo non &eacute; accettabile (cfr. inserimento prima nota integrata manuale).
	 * Pertanto se l'evento ha codice EXTR viene ricercato l'evento corrispondente al tipo di entit&agrave; fornita in ingresso.
	 * <br/>
	 * Nel caso in cui <strong>non</strong> sia stata fornita un'entit&agrave; in ingresso, &eacute; impossibile determinare l'evento corretto.
	 * In tal caso si sceglie di effettuare un fallback sul tipo di evento (fornito o meno dal chiamante)
	 */
	private void handleEvento() {
		final String methodName = "handleEvento";
		// Calcolo il tipo evento
		tipoEvento = codificaDad.ricercaCodifica(TipoEvento.class, "EXTR");
		
		eventi = new ArrayList<Evento>();
		
		for(Evento e : req.getEventi()) {
			Evento evt = e != null && e.getUid() != 0 ? codificaDad.ricercaCodifica(Evento.class, e.getUid()) : null;
			if(evt != null && evt.getCodice() != null && evt.getCodice().startsWith("EXTR-")) {
				eventi.add(evt);
			}
		}
		
		if(eventi.size() == req.getEventi().size() || !eventi.isEmpty()) {
			// Ho mappato tutti gli eventi possibili
			return;
		}
		
		// Ho solo l'evento EXTR nella lista fornita dal chiamante. Devo filtrare per entita
		// Devo calcolare l'evento corretto generando il codice via il tipo di entita' se presente
		if(req.getEntita() == null || req.getEntita().getUid() == 0) {
			log.debug(methodName, "Entita' non valorizzata. Impossibile determinare l'evento corretto");
			return;
		}
		Class<?> clazz = req.getEntita().getClass();
		log.debug(methodName, "Evento con codice \"EXTR\" non valido: calcolo dell'evento corrispondente per entita' collegata di tipo " + clazz.getSimpleName());
		String newCode = "EXTR-" + clazz.getSimpleName().replaceAll("[a-z]", "");
		Evento tmp = codificaDad.ricercaCodifica(Evento.class, newCode);
		if(tmp  == null || tmp.getCodice() == null) {
			throw new BusinessException("Impossibile reperire l'evento con codice " + newCode);
		}
		eventi.add(tmp);
	}


	private void handleAnnoNumeroSub() {
		final String methodName = "handleAnnoNumeroSub";
		if(eventi == null || eventi.isEmpty()) {
			// Non ho un evento. Non avrei modo di determinare i dati con anno, numero e sub. Esco
			log.debug(methodName, "Dati anno/numero/sub non determinabili");
			return;
		}
		Entita entita = req.getEntita();
		if(entita == null || entita.getUid() == 0) {
			// Entita non presente
			log.debug(methodName, "Entita non fornita");
			return;
		}
		if(entita instanceof SubImpegno) {
			SubImpegno subImpegno = impegnoBilDad.findSubImpegnoByUid(entita.getUid(), SubImpegnoModelDetail.Padre);
			annoMovimento = Integer.valueOf(subImpegno.getAnnoImpegnoPadre());
			numeroMovimento = subImpegno.getNumeroImpegnoPadre().toPlainString();
			numeroSubmovimento = Integer.valueOf(subImpegno.getNumeroBigDecimal().intValue());
			return;
		}
		if(entita instanceof Impegno) {
			Impegno impegno = impegnoBilDad.findImpegnoByUid(entita.getUid());
			annoMovimento = Integer.valueOf(impegno.getAnnoMovimento());
			numeroMovimento = impegno.getNumeroBigDecimal().toPlainString();
			return;
		}
		if(entita instanceof SubAccertamento) {
			SubAccertamento subAccertamento = accertamentoBilDad.findSubAccertamentoByUid(entita.getUid(), SubAccertamentoModelDetail.Padre);
			annoMovimento = Integer.valueOf(subAccertamento.getAnnoAccertamentoPadre());
			numeroMovimento = subAccertamento.getNumeroAccertamentoPadre().toPlainString();
			numeroSubmovimento = Integer.valueOf(subAccertamento.getNumeroBigDecimal().intValue());
			return;
		}
		if(entita instanceof Accertamento) {
			Accertamento accertamento = accertamentoBilDad.findAccertamentoByUid(entita.getUid());
			annoMovimento = Integer.valueOf(accertamento.getAnnoMovimento());
			numeroMovimento = accertamento.getNumeroBigDecimal().toPlainString();
			return;
		}
		throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("l'entita' in ricerca di tipo " + entita.getClass().getSimpleName() + " non e' accettabile per la ricerca"));
	}
	

}
