/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Ricerca sintatica del registro A per il cespite.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/10/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaRegistroACespiteService extends CheckedAccountBaseService<RicercaSinteticaRegistroACespite, RicercaSinteticaRegistroACespiteResponse> {

	private static final String[] eventoTipoCodesDefault = new String[] {"DE", "DS", "L"};
	
	@Autowired
	private PrimaNotaInvDad primaNotaInvDad;
	
	private List<StatoOperativoPrimaNota>              stati    = new ArrayList<StatoOperativoPrimaNota>();
    private List<StatoAccettazionePrimaNotaDefinitiva> statiInv = new ArrayList<StatoAccettazionePrimaNotaDefinitiva>();
    		
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio", false);
		checkParametriPaginazione(req.getParametriPaginazione(), false);
		checkNotNull(req.getPrimaNota(), "prima nota");
		checkNotNull(req.getPrimaNota().getTipoCausale(), "Tipo causale", false);
	}
	
	@Override
	protected void init() {
		primaNotaInvDad.setEnte(ente);
		Utility.MDTL.addModelDetails(req.getModelDetails());
	}

	@Override
	@Transactional(readOnly=true, timeout=120)
	public RicercaSinteticaRegistroACespiteResponse executeService(RicercaSinteticaRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		initStati();
		if(TipoCausale.Libera.equals(req.getPrimaNota().getTipoCausale())) {
			ricercaLibera();
			return;
		}
		ricercaIntegrata();
	}
	
	/**
	 * Nel caso in cui lo stato non sia fornito, il default deve essere D
	 */
	private void initStati() {
		stati = new ArrayList<StatoOperativoPrimaNota>();
		statiInv = new ArrayList<StatoAccettazionePrimaNotaDefinitiva>();
		
		
		if(req.getPrimaNota().getStatoOperativoPrimaNota() != null) {
			stati.add(req.getPrimaNota().getStatoOperativoPrimaNota());
		} else {
			stati.add(StatoOperativoPrimaNota.ANNULLATO);
			stati.add(StatoOperativoPrimaNota.DEFINITIVO);
		}
		
		if(req.getPrimaNota().getStatoAccettazionePrimaNotaDefinitiva() != null) {
			statiInv.add(req.getPrimaNota().getStatoAccettazionePrimaNotaDefinitiva());
		}
	}

	private void ricercaLibera() {
		ListaPaginata<PrimaNota> primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteRegistroA(
				req.getBilancio(),
				req.getPrimaNota(),
				req.getEventi(),
				req.getCausaleEP(),
				req.getConto(),
				req.getDataRegistrazioneDa(),
				req.getDataRegistrazioneA(),
				req.getDataRegistrazioneProvvisoriaDa(),
				req.getDataRegistrazioneProvvisoriaA(),
				stati,
				statiInv,
				req.getParametriPaginazione(),
				Utility.MDTL.byModelDetailClass(PrimaNotaModelDetail.class));
		res.setPrimeNote(primeNote);
	}
	
	@SuppressWarnings("unchecked")
	private void ricercaIntegrata() {
		ListaPaginata<PrimaNota> primeNote;
		List<TipoEvento> listaTipoEvento = ottieniListaTipoEvento();
		try {
			primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteIntegrateRegistroA(
					req.getBilancio(),
					req.getPrimaNota(),
					req.getEventi(),
					req.getCausaleEP(),
					req.getConto(),
					req.getDataRegistrazioneDa(),
					req.getDataRegistrazioneA(),
					req.getDataRegistrazioneProvvisoriaDa(),
					req.getDataRegistrazioneProvvisoriaA(),
					stati,
					statiInv,
					req.getTipoElenco(),
					listaTipoEvento,
					req.getAnnoMovimento(),
					req.getNumeroMovimento(),
					req.getNumeroSubmovimento(),
					req.getRegistrazioneMovFin(),
					req.getImportoDocumentoDa(),
					req.getImportoDocumentoA(),
					req.getSoggetto(),
					req.getAttoAmministrativo(),
					req.getDocumento() != null && req.getDocumento().getUid() != 0? Arrays.asList(Integer.valueOf(req.getDocumento().getUid())) : null,
					req.getCapitolo(),
					req.getMovimentoGestione(),
					req.getSubMovimentoGestione(),
					req.getStrutturaAmministrativoContabile(),
					req.getParametriPaginazione(),
					Utility.MDTL.byModelDetailClass(PrimaNotaModelDetail.class));
		} catch(DadException de) {
			res.addErrore(ErroreBil.ERRORE_GENERICO.getErrore(de.getMessage()));
			return;
		}
		res.setPrimeNote(primeNote);
	}

	private List<TipoEvento> ottieniListaTipoEvento() {
		if(req.getTipoEvento() != null && req.getTipoEvento().getUid() != 0) {
			TipoEvento tipoEventoCaricato = primaNotaInvDad.popolaTipoEventoByUid(req.getTipoEvento());
			if(tipoEventoCaricato == null) {
				throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("ricerca prima nota", "Il tipo evento selezionato"));
			}
			return Arrays.asList(tipoEventoCaricato);
		}
		return primaNotaInvDad.popolaTipiEventoByCodici(eventoTipoCodesDefault);
	}
	
}
