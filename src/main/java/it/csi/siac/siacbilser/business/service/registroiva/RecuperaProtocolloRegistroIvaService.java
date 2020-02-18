/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * The Class RecuperaProtocolloRegistroIvaService.
 * <br/>
 * 
 * Il sistema presenta la videata attuale di aggiorna registro con il pi&uacute; il <strong>NUMERO DI PROTOCOLLO PROVVISORIO-DEFINITIVO DA RECUPERARE</strong>
 * (Titolo campo 'Num. Prot. Def. da recuperare' per il definitivo, 'Num. Prot. Prov.  da recuperare' per il provvisorio) in base al registro selezionato
 * (se si tratta di Iva differita compare anche il campo per il protocollo provvisorio).
 * <br/>
 * Il numero indicato <strong>DEVE</strong> essere gi&agrave; utilizzato (non si pu&oacute; associare un nuovo numero,
 * in questo caso deve essere segnalato un messaggio di errore), quando si preme il pulsante 'recupera' tutti i documenti IVA
 * con i numeri di protocollo successivi a quello indicato per quel registro verranno riassegnati ed il progressivo verr&agrave; reso coerente, non ci saranno controlli.
 * <br/>
 * Dopo questa operazione lo stesso operatore di Backoffice potr&agrave; cos&iacute; andare sul documento IVA ad aggiornare e utilizzare questo numero,
 * assegnandolo assieme al registro al documento IVA desiderato, anche se dovesse appartenere ad un registro differente rispetto a quello precedente.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RecuperaProtocolloRegistroIvaService extends CheckedAccountBaseService<RecuperaProtocolloRegistroIva, RecuperaProtocolloRegistroIvaResponse> {


	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	
	/** The registro iva. */
	private RegistroIva registroIva;
	private Bilancio bilancio;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistroIva(), "registro iva");
		registroIva = req.getRegistroIva();
		
		checkEntita(req.getBilancio(), "bilancio");
		
		checkNotBlank(registroIva.getCodice(), "codice registro iva", false);
		checkNotBlank(registroIva.getDescrizione(), "codice registro iva", false);
		
		checkEntita(registroIva.getGruppoAttivitaIva(), "gruppo attivita' iva", false);
		checkNotNull(registroIva.getTipoRegistroIva(), "tipo registro Iva", false);
		
		checkCondition(req.getNumeroProtocolloProvvisorio() != null || req.getNumeroProtocolloDefinitivo() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero protocollo da recuperare"), false);
	}
	
	@Override
	protected void init() {
		registroIvaDad.setLoginOperazione(loginOperazione);
		registroIvaDad.setEnte(ente);
		contatoreRegistroIvaDad.setEnte(ente);
	}

	@Transactional
	@Override
	public RecuperaProtocolloRegistroIvaResponse executeService(RecuperaProtocolloRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setRegistroIva(registroIva);
		caricaBilancio();
		businessChecks();
//		registroIvaDad.aggiornaRegistroIva(registroIva); //a cosa serve questo!?!?
		recuperoProtocolloProvvisorio();
		recuperoProtocolloDefinitivo();
	}

	private void caricaBilancio() {
		this.bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
	}

	/**
	 * Controlli di business:
	 * <ul>
	 *     <li>il registro deve esistere</li>
	 *     <li>il registro deve essere bloccato</li>
	 *     <li>il numero di protocollo il cui recupero &eacute; richiesto deve essere gi&agrave; utilizzato</li>
	 * </ul>
	 */
	private void businessChecks() {
		final String methodName = "businessChecks";
		RegistroIva ri = registroIvaDad.findRegistroIvaByIdMinimal(registroIva.getUid());
		if(ri == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro IVA", "uid " + registroIva.getUid()));
		}
		if(!Boolean.TRUE.equals(ri.getFlagBloccato())) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il Registro IVA non risulta essere bloccato"));
		}
		if(req.getNumeroProtocolloDefinitivo() != null && registroIvaDad.findSubdocumentoIvaMinimalByRegistroAndNumeroProtocolloDef(registroIva, req.getNumeroProtocolloDefinitivo(), Integer.valueOf(bilancio.getAnno())) == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento IVA", "registro IVA " + ri.getCodice()
				+ ", numero registrazione definitivo " + req.getNumeroProtocolloDefinitivo()));
		}
		if(req.getNumeroProtocolloProvvisorio() != null && registroIvaDad.findSubdocumentoIvaMinimalByRegistroAndNumeroProtocolloProv(registroIva, req.getNumeroProtocolloProvvisorio(), Integer.valueOf(bilancio.getAnno())) == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento IVA", "registro IVA " + ri.getCodice()
				+ ", numero protocollo provvisorio " + req.getNumeroProtocolloProvvisorio()));
		}
		Integer[] ultimiNumeriStampati = registroIvaDad.ultimoNumeroProtocolloDefinitivoProvvisorioStampatoDefinitivo(registroIva, bilancio.getAnno());
		Integer ultimoNumeroProtocolloDefinitivoStampato = ultimiNumeriStampati[0];
		Integer ultimoNumeroProtocolloProvvisorioStampato = ultimiNumeriStampati[1];
		log.debug(methodName, "Ultimi numeri stampati in definitivo: " + ultimoNumeroProtocolloDefinitivoStampato + " (definitivo), "
				+ ultimoNumeroProtocolloProvvisorioStampato + " (provvisorio)");
		
		if(req.getNumeroProtocolloDefinitivo() != null && ultimoNumeroProtocolloDefinitivoStampato != null && req.getNumeroProtocolloDefinitivo().compareTo(ultimoNumeroProtocolloDefinitivoStampato) <= 0) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il Registro IVA risulta essere stato stampato in definitivo per il protocollo definitivo "
				+ req.getNumeroProtocolloDefinitivo()));
		}
		if(req.getNumeroProtocolloProvvisorio() != null && ultimoNumeroProtocolloProvvisorioStampato != null && req.getNumeroProtocolloProvvisorio().compareTo(ultimoNumeroProtocolloProvvisorioStampato) <= 0) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il Registro IVA risulta essere stato stampato in definitivo per il protocollo provvisorio "
				+ req.getNumeroProtocolloProvvisorio()));
		}
	}

	/**
	 * Recupera il numero di protocollo provvisorio spostando in avanti tutti i subdocumenti con numero pari o maggiore a quello da recuperare
	 */
	private void recuperoProtocolloProvvisorio() {
		if(req.getNumeroProtocolloProvvisorio() == null) {
			return;
		}
		Integer nuovoUltimoProtProv = registroIvaDad.incrementaProtocolloProvvisorio(registroIva, req.getNumeroProtocolloProvvisorio(), Integer.valueOf(bilancio.getAnno()));
		if(nuovoUltimoProtProv != null){
			contatoreRegistroIvaDad.aggiornaProtProv(registroIva, nuovoUltimoProtProv, bilancio);
		}
	}

	/**
	 * Recupera il numero di protocollo definitivo spostando in avanti tutti i subdocumenti con numero pari o maggiore a quello da recuperare
	 */
	private void recuperoProtocolloDefinitivo() {
		if(req.getNumeroProtocolloDefinitivo() == null) {
			return;
		}
		Integer nuovoUltimoProtDef = registroIvaDad.incrementaProtocolloDefinitivo(registroIva, req.getNumeroProtocolloDefinitivo(), Integer.valueOf(bilancio.getAnno()));
		if(nuovoUltimoProtDef != null){
			contatoreRegistroIvaDad.aggiornaProtDef(registroIva, nuovoUltimoProtDef, bilancio);
		}
	}
	

}
