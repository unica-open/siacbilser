/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AnteprimaAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAnteprimaAmmortamentoAnnuoCespiteService extends CheckedAccountBaseService<InserisciAnteprimaAmmortamentoAnnuoCespite, InserisciAnteprimaAmmortamentoAnnuoCespiteResponse> {

	//DAD
	@Autowired
	private AnteprimaAmmortamentoAnnuoCespiteDad anteprimaAmmortamentoAnnuoCespiteDad;
	@Autowired
	private PrimaNotaInvDad primaNotaInvDad;
	
	//questa potrebbe essere mappa uid - object[]
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno per ammortamento annuo"));
	}

	@Override
	protected void init() {
		super.init();
		anteprimaAmmortamentoAnnuoCespiteDad.setEnte(ente);
		anteprimaAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}

	@Transactional
	@Override
	public InserisciAnteprimaAmmortamentoAnnuoCespiteResponse executeService(InserisciAnteprimaAmmortamentoAnnuoCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		annullaPrimeNoteAnteprimaPrecedente();
		
		Integer cespitiInseriti = anteprimaAmmortamentoAnnuoCespiteDad.inserisciNuovaAnteprimaAmmortamentoAnnuoCespite(req.getAnno());
		if(cespitiInseriti == null || cespitiInseriti.intValue() == 0) {
			//throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile inserire un'anteprima con i parametri indicati."));
			throw new BusinessException(ErroreBil.ANTEPRIMA_NON_CONSENTITA.getErrore("cespiti non trovati in base al parametro Anno digitato."));	
		}
		res.setCespitiCoinvolti(cespitiInseriti);
	}

	private void annullaPrimeNoteAnteprimaPrecedente() {
		final String methodName="annullaPrimeNoteAnteprimaPrecedente";
		AnteprimaAmmortamentoAnnuoCespite anteprimaPrecedente = anteprimaAmmortamentoAnnuoCespiteDad.caricaAnteprimaAmmprtamentoAnnuo(req.getAnno());
		if(anteprimaPrecedente == null || anteprimaPrecedente.getUid() == 0) {
			return;
		}
		List<PrimaNota> primeNote = primaNotaInvDad.ricercaScrittureInventarioByEntita(anteprimaPrecedente, null, null, Boolean.TRUE, null, null);
		if(primeNote == null || primeNote.isEmpty()) {
			log.debug(methodName, "Non sono presenti prime note per l'anteprima.");
			return;
		}
		for (PrimaNota primaNota : primeNote) {
			annullaPrimaNotaPrecedente(primaNota);
		}
	}
	
	/**
	 * Annulla prima nota precedente.
	 *
	 * @param primaNota the prima nota
	 */
	private void annullaPrimaNotaPrecedente(PrimaNota primaNota) {
		final String methodName ="annullaPrimaNotaPrecedente";
		if(primaNota == null || primaNota.getUid() == 0){
			log.debug(methodName, "Prima nota da annullare null o con uid non valorizzato.Esco.");
			return;
		}
		
		log.debug(methodName, "Annullo la prima Nota precedente [uid: " + primaNota.getUid()  + " ].");
//		if(StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota()) && !req.isAnnullaPrimeNoteDefinitivePrecedenti()) {
//			log.warn(methodName, "Non posso annullare la prima nota poiche' lo stato e' definitivo ed isAnnullaPrimeNoteDefinitivePrecedenti = " + req.isAnnullaPrimeNoteDefinitivePrecedenti());
//			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("esistono delle prime note definitive ma i parametri impostati non consentono il loro annullamento."));
//		}
		
		primaNotaInvDad.aggiornaStatoPrimaNota(primaNota.getUid(), StatoOperativoPrimaNota.ANNULLATO);
		log.debug(methodName, "Prima Nota correttamente annullata.");
	}

}


