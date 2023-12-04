/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Aggiornamento della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaVariazioneCespiteService extends BaseInserisciAggiornaVariazioneCespiteService<AggiornaVariazioneCespite, AggiornaVariazioneCespiteResponse> {

	private VariazioneCespite variazioneCespiteOld;
	
	@Autowired
	@Inventario
	PrimaNotaInvDad primaNotaInvDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getVariazioneCespite(), "variazione cespite");

		variazioneCespite = req.getVariazioneCespite();
		checkCampiVariazione();
	}

	@Override
	protected void init() {
		super.init();
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}

	@Transactional
	@Override
	public AggiornaVariazioneCespiteResponse executeService(AggiornaVariazioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		retrieveCespite();
		
		caricaTipoBene();
		checkTipoBene();
		
		retrieveOldVariazione();
		
		aggiornaImportoCespite();
		
		VariazioneCespite variazioneCespiteInserita = variazioneCespiteDad.aggiornaVariazioneCespite(variazioneCespite);
		// Imposto il cespite si' da avere l'importo attuale aggiornato
		variazioneCespite.setCespite(cespite);
		
		gestisciPrimeNote(variazioneCespiteInserita);
		
		res.setVariazioneCespite(variazioneCespiteInserita);
	}
	

	/**
	 * Ottiene la precedente variazione
	 */
	private void retrieveOldVariazione() {
		variazioneCespiteOld = variazioneCespiteDad.findVariazioneCespiteById(variazioneCespite, VariazioneCespiteModelDetail.StatoVariazioneCespite);
		if(variazioneCespiteOld == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Variazione cespite", "uid " + variazioneCespite.getUid()));
		}
		//SIAC-6556
		if(variazioneCespiteOld.getStatoVariazioneCespite() != null && StatoVariazioneCespite.DEFINITIVO.equals(variazioneCespiteOld.getStatoVariazioneCespite())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile aggiornare la variazione, lo stato risulta essere definitivo."));
		}
		// Uso il flag di quella gia' salvata: non permetto il cambio del tipo
		variazioneCespite.setFlagTipoVariazioneIncremento(variazioneCespiteOld.getFlagTipoVariazioneIncremento());
		// TODO: mantengo vecchio stato?
		variazioneCespite.setStatoVariazioneCespite(variazioneCespiteOld.getStatoVariazioneCespite());
	}
	
	
	private void aggiornaImportoCespite() {
		BigDecimal importoVariazione = variazioneCespite.getImporto();
		BigDecimal importoOldVariazione = variazioneCespiteOld.getImporto();
		// Devo togliere il vecchio importo
		BigDecimal importoToAdd = importoVariazione.subtract(importoOldVariazione);
		importoToAdd = Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento()) ? importoToAdd : importoToAdd.negate();
		
		BigDecimal newImporto = cespite.getValoreAttuale().add(importoToAdd);
		
		if(newImporto.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'importo del cespite non puo' diventare negativo"));
		}
		cespite.setValoreAttuale(newImporto);
		cespiteDad.aggiornaImportoCespite(cespite);
	}

	@Override
	public void gestisciPrimeNote(VariazioneCespite var) {
		final String methodName ="gestisciPrimeNote";
		if(variazioneCespite.getImporto().compareTo(variazioneCespiteOld.getImporto()) == 0) {
			log.debug(methodName, "non vi e' un aggiornamentoDiImporto. Non devo effettuare operazioni sulle prime note");
			return;
		}
		
		PrimaNota primaNota =  caricaPrimaNotaInventario();
		annullaPrimaNotaPrecedente(primaNota);
		inserisciEAssociaPrimaNota(var);
		
	}
	
	
	private PrimaNota caricaPrimaNotaInventario(){ 
		List<PrimaNota> primeNoteCollegate = primaNotaInvDad.ricercaScrittureInventarioByEntita(variazioneCespite,null, null, null, null,null);
		if(primeNoteCollegate == null || primeNoteCollegate.isEmpty()) {
			return null;
		}
		return primeNoteCollegate.get(0);
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

}
