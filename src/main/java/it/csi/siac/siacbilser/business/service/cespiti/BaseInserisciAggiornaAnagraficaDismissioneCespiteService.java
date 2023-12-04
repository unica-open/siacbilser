/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.AttoAmministrativoModelDetail;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 *
 * @author elisa
 * @version 1.0.0 - 20-08-2018
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class BaseInserisciAggiornaAnagraficaDismissioneCespiteService<REQ extends ServiceRequest, RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {

	// DAD
	@Autowired
	protected DismissioneCespiteDad dismissioneCespiteDad;
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired
	private CausaleEPDad causaleEpDad;
	
	protected DismissioneCespite dismissioneCespite;
	

	/**
	 * @throws ServiceParamError
	 */
	protected void checkCampiDismissioneCespite() throws ServiceParamError {
		checkEntita(dismissioneCespite.getAttoAmministrativo(), "atto amministrativo");
		//		checkNotNull(dismissioneCespite.getAttoAmministrativo(), "atto amministrativo");
//		checkCondition(dismissioneCespite.getAttoAmministrativo().getUid() != 0  
//				^ (dismissioneCespite.getAttoAmministrativo().getAnno() != 0 && dismissioneCespite.getAttoAmministrativo().getNumero() != 0 && dismissioneCespite.getAttoAmministrativo().getTipoAtto() != null && dismissioneCespite.getAttoAmministrativo().getTipoAtto().getUid() != 0),
//				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(" non sono stati forniti i dati necessari ad indentificare correttamente l'atto amministrativo."));
		checkNotBlank(dismissioneCespite.getDescrizione(), "descrizione");
		checkEntita(dismissioneCespite.getCausaleEP(), "causale dismissione");
		checkNotNull(dismissioneCespite.getDataCessazione(), "data cessazione");
	}

	@Override
	protected void init() {
		super.init();
		dismissioneCespiteDad.setEnte(ente);
		dismissioneCespiteDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(ente);
	}

	/**
	 * Carica l'atto amministrativo.
	 * @throws BusinessException se l'atto non esiste
	 */
	protected void caricaAttoAmministrativo() {
		AttoAmministrativo aa = attoAmministrativoDad.findProvvedimentoByIdAndModelDetail(dismissioneCespite.getAttoAmministrativo().getUid(), new AttoAmministrativoModelDetail[]{});
		if(aa == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("atto amministrativo"));
		}
		dismissioneCespite.setAttoAmministrativo(aa);
	}

	/**
	 * Controlla la causale.
	 * @throws BusinessException se la causale non supera i controlli.
	 */
	protected void checkCausale() {
		if(!TipoCausale.Libera.equals(dismissioneCespite.getCausaleEP().getTipoCausale())) {
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la causale non e' libera."));
		}
		
		if(!isCausaleCorrettaPerEventoIndicato()) {
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la causale e l'evento indicati non sono compatibili. "));
		}
	}

	/**
	 * Checks if is causale corretta per evento indicato.
	 *
	 * @return true, if is causale corretta per evento indicato
	 */
	private boolean isCausaleCorrettaPerEventoIndicato() {
		if(dismissioneCespite.getEvento() == null) {
			return true;
		}
		List<Evento> eventi = dismissioneCespite.getCausaleEP().getEventi();
		for (Evento evento : eventi) {
			if(dismissioneCespite.getEvento().getUid() == evento.getUid()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Carica la causale 
	 * @throws BusinessException se la causale  non esiste
	 */
	protected void caricaCausale() {
		dismissioneCespite.getCausaleEP().setDataInizioValiditaFiltro(Utility.primoGiornoDellAnno(req.getAnnoBilancio()));
		CausaleEP cep = causaleEpDad.findCausaleEPByIdModelDetail(dismissioneCespite.getCausaleEP(),new CausaleEPModelDetail[]{CausaleEPModelDetail.Tipo, CausaleEPModelDetail.Evento});
		if(cep == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("causale dismissione"));
		}
		dismissioneCespite.setCausaleEP(cep);
	}

	/**
	 * Controlla la correttezza dell'evento passato in input
	 */
	protected void checkEvento() {
		Evento evento = dismissioneCespite.getEvento();
		if(evento == null || evento.getUid() == 0) {
			dismissioneCespite.setEvento(null);
			return;
		}
		boolean isEventoLibero = dismissioneCespiteDad.isEventoLibero(evento);
		if(!isEventoLibero){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'evento selezionato non risulta essere associato alla causale libera "));
		}
		
	}

}


