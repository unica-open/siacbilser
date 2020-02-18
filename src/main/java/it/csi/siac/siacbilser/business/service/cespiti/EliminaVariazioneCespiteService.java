/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.integration.dad.VariazioneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Eliminazione della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaVariazioneCespiteService extends CheckedAccountBaseService<EliminaVariazioneCespite, EliminaVariazioneCespiteResponse> {

	// DAD
	@Autowired
	private VariazioneCespiteDad variazioneCespiteDad;
	@Autowired
	private CespiteDad cespiteDad;
	
	// Fields
	private Cespite cespite;
	private VariazioneCespite variazioneCespiteOld;
	@Autowired
	@Inventario
	PrimaNotaInvDad primaNotaInvDad;
	private List<PrimaNota> primeNoteDaAnnullare;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getVariazioneCespite(), "variazione cespite");
	}

	@Override
	protected void init() {
		super.init();
		variazioneCespiteDad.setEnte(ente);
		variazioneCespiteDad.setLoginOperazione(loginOperazione);
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}

	@Transactional
	@Override
	public EliminaVariazioneCespiteResponse executeService(EliminaVariazioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		caricaEControllaStatoPrimeNote();
		retrieveCespite();
		retrieveOldVariazione();
		
		aggiornaImportoCespite();
		VariazioneCespite variazioneCespite = variazioneCespiteDad.eliminaVariazioneCespite(req.getVariazioneCespite());
		// Imposto il cespite si' da avere l'importo attuale aggiornato
		annullaPrimeNoteCollegate();
		
		variazioneCespite.setCespite(cespite);
		res.setVariazioneCespite(variazioneCespite);
	}
	
	/**
	 * Ottiene il cespite
	 */
	private void retrieveCespite() {
		cespite = variazioneCespiteDad.findCespiteByVariazione(req.getVariazioneCespite(), new CespiteModelDetail[] {});
		if(cespite == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Cespite", "collegato alla variazione di uid " + req.getVariazioneCespite().getUid()));
		}
	}
	
	/**
	 * Ottiene la precedente variazione
	 */
	private void retrieveOldVariazione() {
		variazioneCespiteOld = variazioneCespiteDad.findVariazioneCespiteById(req.getVariazioneCespite());
		if(variazioneCespiteOld == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Variazione cespite", "uid " + req.getVariazioneCespite().getUid()));
		}
	}
	
	
	private void aggiornaImportoCespite() {
		BigDecimal importoOldVariazione = variazioneCespiteOld.getImporto();
		// Devo togliere il vecchio importo
		BigDecimal importoToSubtract = Boolean.TRUE.equals(variazioneCespiteOld.getFlagTipoVariazioneIncremento()) ? importoOldVariazione : importoOldVariazione.negate();
		
		BigDecimal newImporto = cespite.getValoreAttuale().subtract(importoToSubtract);
		
		if(newImporto.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'importo del cespite non puo' diventare negativo"));
		}
		cespite.setValoreAttuale(newImporto);
		cespiteDad.aggiornaImportoCespite(cespite);
	}
	
	/**
	 * Check prime note collegate.
	 */
	private void caricaEControllaStatoPrimeNote() {
		List<PrimaNota> primeNote = primaNotaInvDad.ricercaScrittureInventarioByEntita(req.getVariazioneCespite(), null,null, Boolean.TRUE, new PrimaNotaModelDetail[] {PrimaNotaModelDetail.StatoOperativo}, Ambito.AMBITO_INV);
		if(primeNote == null || primeNote.isEmpty()) {
			return;
		}
		List<PrimaNota> pns = new ArrayList<PrimaNota>();
		for (PrimaNota primaNota : primeNote) {
			if(StatoOperativoPrimaNota.DEFINITIVO.equals(primaNota.getStatoOperativoPrimaNota())) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile eliminare la variazione, sono presenti prime note definitive collegate."));
			}
			pns.add(primaNota);
		}
		
		primeNoteDaAnnullare = pns;
		
	}
	
	private void annullaPrimaNotaPrecedente(PrimaNota primaNota) {
		final String methodName ="annullaPrimaNotaPrecedente";
		if(primaNota == null || primaNota.getUid() == 0){
			log.debug(methodName, "Non sono presenti prime note precedenti, non devo annullare.");
			return;
		}
		log.debug(methodName, "Annullo la prima Nota precedente [uid: " + primaNota.getUid()  + " ].");
		primaNotaInvDad.aggiornaStatoPrimaNota(primaNota.getUid(), StatoOperativoPrimaNota.ANNULLATO);
	}
	
	
	private void annullaPrimeNoteCollegate() {
		if(primeNoteDaAnnullare == null || primeNoteDaAnnullare.isEmpty()){
			return;
		}
		for (PrimaNota p : primeNoteDaAnnullare) {
			annullaPrimaNotaPrecedente(p);
		}
		
	}
	
}
