/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;

/**
 * The Class AggiornaCapitoloDiUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiUscitaGestioneService extends CrudCapitoloBaseService<AggiornaCapitoloDiUscitaGestione, AggiornaCapitoloDiUscitaGestioneResponse> {
	
	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
//	/** The calcolo disponibilita di un capitolo service. */
//	@Autowired
//	private CalcoloDisponibilitaDiUnCapitoloService calcoloDisponibilitaDiUnCapitoloService;
	
	@Override
	protected void init() {
		ente = req.getCapitoloUscitaGestione().getEnte();
		bilancio = req.getCapitoloUscitaGestione().getBilancio();
		
		capitoloUscitaGestioneDad.setBilancio(bilancio);
		capitoloUscitaGestioneDad.setEnte(ente);
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloUscitaGestione(), "capitoloUscitaGestione");
		checkEntita(req.getCapitoloUscitaGestione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloUscitaGestione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloUscitaGestione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
		
		checkEntita(req.getCapitoloUscitaGestione().getStrutturaAmministrativoContabile(), "strutturaAmministrativoContabile", false);
		
		// CR 2204
//		checkEntita(req.getCapitoloUscitaGestione().getElementoPianoDeiConti(), "elementoPianoDeiConti", false);
//		checkNotNull(req.getCapitoloUscitaGestione().getMacroaggregato(), "macroaggregato", false);
//		checkNotNull(req.getCapitoloUscitaGestione().getProgramma(), "programma", false);
	}

	@Transactional
	public AggiornaCapitoloDiUscitaGestioneResponse executeService(AggiornaCapitoloDiUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		//SIAC-5003 Il capitolo e' modificabile solo se lo stato operativo e' di tipo valido o provvisorio
		checkCapitoloModificabilePerAggiornamento(req.getCapitoloUscitaGestione());
		//SIAC-5007
		checkCapitoloUscitaGestioneSeFPV(req.getCapitoloUscitaGestione());
		checkCapitoloUscitaGestioneSeStandard(req.getCapitoloUscitaGestione());
		
		CapitoloUscitaGestione cugAttuale = cercaCapitoloUscitaGestioneAttuale();
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		// SIAC-6881
		// Ricarico gli importi originali: la variazione dei dati e' gestita lato componente
		loadImportiOriginali(cugAttuale);
//		checkVariazioneImporti(cugAttuale);
		
		if(isValorizzatoExCapitolo()){
			CapitoloUscitaGestione exCap = ricercaExCapitoloUscitaGestione();
			req.getCapitoloUscitaGestione().setUidExCapitolo(exCap.getUid());
		}
		
		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.update(req.getCapitoloUscitaGestione());
		res.setCapitoloUscitaGestione(cug);
	}
	
	/**
	 * Caricamento degli importi originali del capitolo
	 * @param cugAttuale
	 */
	private void loadImportiOriginali(CapitoloUscitaGestione cugAttuale) {
		List<ImportiCapitoloUG> listaImportiCapitolo = new ArrayList<ImportiCapitoloUG>();
		listaImportiCapitolo.add(cugAttuale.getImportiCapitolo());
		listaImportiCapitolo.add(importiCapitoloDad.findImportiCapitolo(cugAttuale.getUid(), cugAttuale.getAnnoCapitolo().intValue() + 1, ImportiCapitoloUG.class, EnumSet.noneOf(ImportiCapitoloEnum.class)));
		listaImportiCapitolo.add(importiCapitoloDad.findImportiCapitolo(cugAttuale.getUid(), cugAttuale.getAnnoCapitolo().intValue() + 2, ImportiCapitoloUG.class, EnumSet.noneOf(ImportiCapitoloEnum.class)));
		
		req.getCapitoloUscitaGestione().setListaImportiCapitoloUG(listaImportiCapitolo);
		req.getCapitoloUscitaGestione().setImportiCapitoloUG(listaImportiCapitolo.get(0));
	}
	
	/**
	 * Cerca capitolo uscita gestione attuale.
	 *
	 * @return the capitolo uscita gestione
	 */
	private CapitoloUscitaGestione cercaCapitoloUscitaGestioneAttuale() {
		RicercaPuntualeCapitoloUGest criteriRicerca = new RicercaPuntualeCapitoloUGest();

		criteriRicerca.setAnnoEsercizio(bilancio.getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaGestione().getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaGestione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaGestione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaGestione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaGestione().getStatoOperativoElementoDiBilancio()); 
		//se si vuole fare in modo che l'aggiornamento aggiorni anche lo stato la ricerca puntuale non deve avere come parametro obbligatorio lo stato! 
		//togliendo questa obbligatorietà il servizio aggiorna correttamente anche lo stato
		
		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.ricercaPuntualeModulareCapitoloUscitaGestione(criteriRicerca, CapitoloUscitaGestioneModelDetail.Stato);
		if(cug == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("aggiornamento", String.format("Capitolo: %s ",
				enteGestisceUEB() ? req.getCapitoloUscitaGestione().getDescBilancioAnnoNumeroArticoloUEB() : req.getCapitoloUscitaGestione().getDescBilancioAnnoNumeroArticolo())));
		}
		if(req.getCapitoloUscitaGestione().getUid() != cug.getUid()){
			throw new IllegalArgumentException("Trovato capitolo con Uid incongruente. " +String.format("Capitolo: %s/%s/%s/%s/%s/%s",
					bilancio.getAnno(),
					req.getCapitoloUscitaGestione().getAnnoCapitolo(),
					req.getCapitoloUscitaGestione().getNumeroCapitolo(),
					req.getCapitoloUscitaGestione().getNumeroArticolo(),
					req.getCapitoloUscitaGestione().getNumeroUEB(),
					req.getCapitoloUscitaGestione().getStatoOperativoElementoDiBilancio().toString()));
		}
		// Importi capitolo
		ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(cug, bilancio.getAnno(), ImportiCapitoloUG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		cug.setImportiCapitolo(importiCapitoloUG);
		
		return cug;
	}
	
	/**
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkClassificatoriModificabili() {
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getCapitoloUscitaGestione().getClassificatoriGenerici(),
				req.getCapitoloUscitaGestione().getProgramma(), req.getCapitoloUscitaGestione().getClassificazioneCofogProgramma(),
				req.getCapitoloUscitaGestione().getMacroaggregato(), req.getCapitoloUscitaGestione().getElementoPianoDeiConti(), req.getCapitoloUscitaGestione().getSiopeSpesa(),
				req.getCapitoloUscitaGestione().getStrutturaAmministrativoContabile(),
				req.getCapitoloUscitaGestione().getTipoFinanziamento(), req.getCapitoloUscitaGestione().getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloUscitaGestione().getRicorrenteSpesa(), req.getCapitoloUscitaGestione().getPerimetroSanitarioSpesa(),
				req.getCapitoloUscitaGestione().getTransazioneUnioneEuropeaSpesa(), req.getCapitoloUscitaGestione().getPoliticheRegionaliUnitarie());
		
		checkClassificatoriModificabiliAggiornamento(classificatoriDaInserire, req.getCapitoloUscitaGestione());
	}
	
	/**
	 * Controlla se gli attributi che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkAttributiModificabili() {
		checkAttributiModificabiliAggiornamento(req.getCapitoloUscitaGestione());
	}
	
//	/**
//	 * Controlla la variazione degli importi Stanziamento e Stanziamento cassa
//	 * Restituisce true se sono invariati o se la variazione &eacute; tale che: 
//	 * <pre>(disp + importoAttuale) &leq; importoNuovo</pre>
//	 *
//	 * @param cugAttuale the cup attuale
//	 */
//	private void checkVariazioneImporti(CapitoloUscitaGestione cugAttuale) {
//		ImportiCapitoloUG importiDaAggiornare = req.getCapitoloUscitaGestione().getImportiCapitoloUG();
//		ImportiCapitoloUG importiAttuali = cugAttuale.getImportiCapitoloUG();
//		
//		boolean stanziamentoOk = checkVariazioneStanziamento(importiDaAggiornare, importiAttuali);
//		boolean stanziamentoCassaOk = checkVariazioneStanziamentoCassa(importiDaAggiornare, importiAttuali);
//		
//		if(!stanziamentoOk || !stanziamentoCassaOk) {
//			throw new BusinessException(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
//		}
//	}
	
//	/**
//	 * Check variazione stanziamento.
//	 *
//	 * @param importiDaAggiornare the importi da aggiornare
//	 * @param importiAttuali the importi attuali
//	 * @return the boolean
//	 */
//	private boolean checkVariazioneStanziamento(ImportiCapitoloUG importiDaAggiornare, ImportiCapitoloUG importiAttuali) {
//		boolean stanziamentoInvariato = stanziamentoInvariato(importiDaAggiornare, importiAttuali);
//		if(stanziamentoInvariato){
//			return true;
//		}
//		
//		BigDecimal diponibilitaStanziamento = calcolaDisponibilitaRichiesta("MioTipoDiponibilita sara' stanziamento, stanziamentoCassa ecc ?????????");
//		return (diponibilitaStanziamento.add(importiAttuali.getStanziamento())).compareTo(importiDaAggiornare.getStanziamento()) <= 0;
//	}
	
//	/**
//	 * Stanziamento invariato.
//	 *
//	 * @param importiDaAggiornare the importi da aggiornare
//	 * @param importiAttuali the importi attuali
//	 * @return the boolean
//	 */
//	private boolean stanziamentoInvariato(ImportiCapitoloUG importiDaAggiornare, ImportiCapitoloUG importiAttuali) {
//		try{
//			return importiAttuali.getStanziamento().compareTo(importiDaAggiornare.getStanziamento()) == 0;
//		} catch(NullPointerException npe){
//			boolean stanziamentoNonValorizzato = importiAttuali == null || importiAttuali.getStanziamento() == null; 
//			boolean stanziamentoDaAggiornareNonValorizzato = importiDaAggiornare == null || importiDaAggiornare.getStanziamento() == null; 
//			
//			if(stanziamentoNonValorizzato && stanziamentoDaAggiornareNonValorizzato){
//				return true;
//			}
//			
//			return !stanziamentoNonValorizzato && stanziamentoDaAggiornareNonValorizzato;
//		}
//	}
	
//	/**
//	 * Calcola disponibilita richiesta.
//	 *
//	 * @param tipoDisponibilita the tipo disponibilita
//	 * @return the big decimal
//	 */
//	private BigDecimal calcolaDisponibilitaRichiesta(String tipoDisponibilita) {
//		CalcoloDisponibilitaDiUnCapitolo serviceRequest = new CalcoloDisponibilitaDiUnCapitolo();
//		serviceRequest.setRichiedente(req.getRichiedente());
//		serviceRequest.setEnte(req.getCapitoloUscitaGestione().getEnte());
//		serviceRequest.setAnnoCapitolo(req.getCapitoloUscitaGestione().getAnnoCapitolo());
//		serviceRequest.setNumroCapitolo(req.getCapitoloUscitaGestione().getNumeroCapitolo());
//		serviceRequest.setFase(new FaseEStatoAttualeBilancio());
//		serviceRequest.setTipoDisponibilitaRichiesta(tipoDisponibilita);
//		CalcoloDisponibilitaDiUnCapitoloResponse response = executeExternalService(calcoloDisponibilitaDiUnCapitoloService,serviceRequest);
//		return response.getDisponibilitaRichiesta();
//	}

//	/**
//	 * Check variazione stanziamento cassa.
//	 *
//	 * @param importiDaAggiornare the importi da aggiornare
//	 * @param importiAttuali the importi attuali
//	 * @return the boolean
//	 */
//	private boolean checkVariazioneStanziamentoCassa(ImportiCapitoloUG importiDaAggiornare, ImportiCapitoloUG importiAttuali) {
//		boolean stanziamentoCassaInvariato = stanziamentoCassaInvariato(importiDaAggiornare, importiAttuali);
//		if(stanziamentoCassaInvariato){
//			return true;
//		}
//		BigDecimal diponibilitaStanziamentoCassa = calcolaDisponibilitaRichiesta("MioTipoDiponibilita sara' stanziamento, stanziamentoCassa ecc ?????????");
//		return  (diponibilitaStanziamentoCassa.add(importiAttuali.getStanziamentoCassa())).compareTo(importiDaAggiornare.getStanziamentoCassa()) <= 0;
//	}

//	/**
//	 * Stanziamento cassa invariato.
//	 *
//	 * @param importiDaAggiornare the importi da aggiornare
//	 * @param importiAttuali the importi attuali
//	 * @return the boolean
//	 */
//	private boolean stanziamentoCassaInvariato(ImportiCapitoloUG importiDaAggiornare, ImportiCapitoloUG importiAttuali) {
//		try{
//			return importiAttuali.getStanziamentoCassa().compareTo(importiDaAggiornare.getStanziamentoCassa()) == 0;
//		} catch(NullPointerException npe){
//			boolean stanziamentoCassaNonValorizzato = importiAttuali == null || importiAttuali.getStanziamentoCassa() == null; 
//			boolean stanziamentoCassaDaAggiornareNonValorizzato = importiDaAggiornare == null || importiDaAggiornare.getStanziamentoCassa() == null; 
//			
//			if(stanziamentoCassaNonValorizzato && stanziamentoCassaDaAggiornareNonValorizzato){
//				return true;
//			}
//			
//			return !stanziamentoCassaNonValorizzato && stanziamentoCassaDaAggiornareNonValorizzato;
//		}
//	}
	
	/**
	 * Checks if is valorizzato ex capitolo.
	 *
	 * @return true se è sono stati valorizzati i campi dell'ex capitolo
	 */
	private boolean isValorizzatoExCapitolo(){
		return req.getCapitoloUscitaGestione().getExAnnoCapitolo()!=null 
				&& req.getCapitoloUscitaGestione().getExCapitolo() != null
				&& req.getCapitoloUscitaGestione().getExArticolo() != null;
	}
	
	/**
	 * Ricerca l'ex capitolo .
	 *
	 * @return capitolo
	 */
	private CapitoloUscitaGestione ricercaExCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUGest criteriRicerca = new RicercaPuntualeCapitoloUGest();

		criteriRicerca.setAnnoEsercizio(req.getCapitoloUscitaGestione().getExAnnoCapitolo());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaGestione().getExAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaGestione().getExCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaGestione().getExArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaGestione().getExUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaGestione().getStatoOperativoElementoDiBilancio());

		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.ricercaPuntualeModulareCapitoloUscitaGestione(criteriRicerca);
		if(cug == null) {
			String msg = String.format("Capitolo: %s ", 
					enteGestisceUEB() ? req.getCapitoloUscitaGestione().getDescBilancioAnnoNumeroArticoloUEBExCapitolo() : req.getCapitoloUscitaGestione().getDescBilancioAnnoNumeroArticoloExCapitolo());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ex Capitolo", msg));
		}
		return cug;
	}

}
