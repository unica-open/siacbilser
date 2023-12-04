/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CalcoloDisponibilitaDiUnCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * The Class AggiornaCapitoloDiEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiEntrataGestioneService extends CrudCapitoloBaseService<AggiornaCapitoloDiEntrataGestione, AggiornaCapitoloDiEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The calcolo disponibilita di un capitolo service. */
	@Autowired
	private CalcoloDisponibilitaDiUnCapitoloService calcoloDisponibilitaDiUnCapitoloService;
	
	@Override
	protected void init() {
		ente = req.getCapitoloEntrataGestione().getEnte();
		bilancio = req.getCapitoloEntrataGestione().getBilancio();
		
		capitoloEntrataGestioneDad.setBilancio(bilancio);
		capitoloEntrataGestioneDad.setEnte(ente);
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataGestione(), "capitoloEntrataGestione");
		checkEntita(req.getCapitoloEntrataGestione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloEntrataGestione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloEntrataGestione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
		
		checkEntita(req.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile(), "strutturaAmministrativoContabile", false);
		
		// CR 2204
//		checkEntita(req.getCapitoloEntrataGestione().getElementoPianoDeiConti(), "ElementoPianoDeiConti", false);
//		checkEntita(req.getCapitoloEntrataGestione().getCategoriaTipologiaTitolo(), "CategoriaTipologiaTitolo", false);
	}

	@Transactional
	public AggiornaCapitoloDiEntrataGestioneResponse executeService(AggiornaCapitoloDiEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//SIAC-7722: Pulisco le descrizioni da eventuali "a capo"
		req.setCapitoloEntrataGestione(pulisciDescrizioni(req.getCapitoloEntrataGestione()));
		
		checkClassificazioneBilancioSeStandard(req.getCapitoloEntrataGestione());		
		CapitoloEntrataGestione cegAttuale = cercaCapitoloEntrataGestioneAttuale();		
		
		//SIAC-5003 Il capitolo e' modificabile solo se lo stato operativo e' di tipo valido o provvisorio
		checkCapitoloModificabilePerAggiornamento(cegAttuale);
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		checkVariazioneImporti(cegAttuale);
		
		if(isValorizzatoExCapitolo()){
			CapitoloEntrataGestione exCap = ricercaExCapitoloEntrataGestione();
			req.getCapitoloEntrataGestione().setUidExCapitolo(exCap.getUid());
		}
		//SIAC-7507
		caricaImportiCapitolo(cegAttuale);
		
		CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.update(req.getCapitoloEntrataGestione());
		res.setCapitoloEntrataGestione(ceg);
	}
	
	
	
	/**
	 * Carica importi capitolo. Gli importi del capitolo non sono modificabili, quindi li ricarico dalla base dati.
	 *
	 * @param cegAttuale the ceg attuale
	 */
	private void caricaImportiCapitolo(CapitoloEntrataGestione cegAttuale) {
		
		List<ImportiCapitoloEG> listaImporti = new ArrayList<ImportiCapitoloEG>();
		
		//anno di bilancio
		listaImporti.add(cegAttuale.getImportiCapitoloEG());

		//anno di bilancio +1
		ImportiCapitoloEG importiAnno1 = importiCapitoloDad.findImportiCapitolo(cegAttuale, bilancio.getAnno() + 1, ImportiCapitoloEG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		listaImporti.add(importiAnno1);
		
		
		//anno di bilancio +2
		ImportiCapitoloEG importiAnno2 = importiCapitoloDad.findImportiCapitolo(cegAttuale, bilancio.getAnno() + 2, ImportiCapitoloEG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		listaImporti.add(importiAnno2);
		
		req.getCapitoloEntrataGestione().setListaImportiCapitoloEG(listaImporti);
		
	}

	/**
	 * Cerca capitolo entrata gestione attuale.
	 *
	 * @return the capitolo entrata gestione
	 */
	private CapitoloEntrataGestione cercaCapitoloEntrataGestioneAttuale() {
		RicercaPuntualeCapitoloEGest criteriRicerca = new RicercaPuntualeCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(bilancio.getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataGestione().getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataGestione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataGestione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataGestione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataGestione().getStatoOperativoElementoDiBilancio());
		
		CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.ricercaPuntualeModulareCapitoloEntrataGestione(criteriRicerca, CapitoloEntrataGestioneModelDetail.Stato);
		if(ceg == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("aggiornamento", String.format("Capitolo: %s ",
				enteGestisceUEB() ? req.getCapitoloEntrataGestione().getDescBilancioAnnoNumeroArticoloUEB() : req.getCapitoloEntrataGestione().getDescBilancioAnnoNumeroArticolo())));
		}
		if(req.getCapitoloEntrataGestione().getUid() != ceg.getUid()){
			throw new IllegalArgumentException("Trovato capitolo con Uid incongruente. " +String.format("Capitolo: %s/%s/%s/%s/%s/%s",
					bilancio.getAnno(),
					req.getCapitoloEntrataGestione().getAnnoCapitolo(),
					req.getCapitoloEntrataGestione().getNumeroCapitolo(),
					req.getCapitoloEntrataGestione().getNumeroArticolo(),
					req.getCapitoloEntrataGestione().getNumeroUEB(),
					req.getCapitoloEntrataGestione().getStatoOperativoElementoDiBilancio().toString()));
		}
		// Importi capitolo
		ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(ceg, bilancio.getAnno(), ImportiCapitoloEG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		ceg.setImportiCapitolo(importiCapitoloEG);
		
		return ceg;
	}
	
	/**
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkClassificatoriModificabili() {
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getCapitoloEntrataGestione().getClassificatoriGenerici(),
				req.getCapitoloEntrataGestione().getCategoriaTipologiaTitolo(), req.getCapitoloEntrataGestione().getElementoPianoDeiConti(), req.getCapitoloEntrataGestione().getSiopeEntrata(),
				req.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile(),
				req.getCapitoloEntrataGestione().getTipoFinanziamento(), req.getCapitoloEntrataGestione().getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloEntrataGestione().getRicorrenteEntrata(), req.getCapitoloEntrataGestione().getPerimetroSanitarioEntrata(),
				req.getCapitoloEntrataGestione().getTransazioneUnioneEuropeaEntrata());
		
		checkClassificatoriModificabiliAggiornamento(classificatoriDaInserire, req.getCapitoloEntrataGestione());
	}
	
	/**
	 * Controlla se gli attributi che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkAttributiModificabili() {
		checkAttributiModificabiliAggiornamento(req.getCapitoloEntrataGestione());
	}
	
	/**
	 * Controlla la variazione degli importi Stanziamento e Stanziamento cassa
	 * Restituisce true se sono invariati o se la variazione &eacute; tale che: 
	 * <pre>(disp + importoAttuale) &leq; importoNuovo</pre>
	 *  
	 *
	 * @param capAttuale the cap attuale
	 */
	private void checkVariazioneImporti(CapitoloEntrataGestione capAttuale) {
		ImportiCapitoloEG importiDaAggiornare = req.getCapitoloEntrataGestione().getImportiCapitoloEG();
		ImportiCapitoloEG importiAttuali = capAttuale.getImportiCapitoloEG();
		
		boolean stanziamentoOk = checkVariazioneStanziamento(importiDaAggiornare, importiAttuali);
		boolean stanziamentoCassaOk = checkVariazioneStanziamentoCassa(importiDaAggiornare, importiAttuali);
		
		if(!stanziamentoOk || !stanziamentoCassaOk) {
			throw new BusinessException(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
		}
	}
	
	/**
	 * Check variazione stanziamento.
	 *
	 * @param importiDaAggiornare the importi da aggiornare
	 * @param importiAttuali the importi attuali
	 * @return the boolean
	 */
	private boolean checkVariazioneStanziamento(ImportiCapitoloEG importiDaAggiornare, ImportiCapitoloEG importiAttuali) {
		boolean stanziamentoInvariato = stanziamentoInvariato(importiDaAggiornare, importiAttuali);
		if(stanziamentoInvariato){
			return true;
		}
		
		BigDecimal diponibilitaStanziamento = calcolaDisponibilitaRichiesta("MioTipoDiponibilita sara' stanziamento, stanziamentoCassa ecc ?????????");
		return (diponibilitaStanziamento.add(importiAttuali.getStanziamento())).compareTo(importiDaAggiornare.getStanziamento()) <= 0;
	}
	
	/**
	 * Stanziamento invariato.
	 *
	 * @param importiDaAggiornare the importi da aggiornare
	 * @param importiAttuali the importi attuali
	 * @return the boolean
	 */
	private boolean stanziamentoInvariato(ImportiCapitoloEG importiDaAggiornare, ImportiCapitoloEG importiAttuali) {		
		try{
			return importiAttuali.getStanziamento().compareTo(importiDaAggiornare.getStanziamento()) == 0;
		} catch(NullPointerException npe){
			boolean stanziamentoNonValorizzato = importiAttuali == null || importiAttuali.getStanziamento() == null; 
			boolean stanziamentoDaAggiornareNonValorizzato = importiDaAggiornare == null || importiDaAggiornare.getStanziamento() == null; 
			
			if(stanziamentoNonValorizzato && stanziamentoDaAggiornareNonValorizzato){
				return true;
			}
			
			return !stanziamentoNonValorizzato && stanziamentoDaAggiornareNonValorizzato;
		}
	}

	/**
	 * Calcola disponibilita richiesta.
	 *
	 * @param tipoDisponibilita the tipo disponibilita
	 * @return the big decimal
	 */
	private BigDecimal calcolaDisponibilitaRichiesta(String tipoDisponibilita) {
		CalcoloDisponibilitaDiUnCapitolo serviceRequest = new CalcoloDisponibilitaDiUnCapitolo();
		serviceRequest.setRichiedente(req.getRichiedente());
		serviceRequest.setEnte(ente);
		serviceRequest.setAnnoCapitolo(req.getCapitoloEntrataGestione().getAnnoCapitolo());
		serviceRequest.setNumroCapitolo(req.getCapitoloEntrataGestione().getNumeroCapitolo());
		serviceRequest.setFase(new FaseEStatoAttualeBilancio());
		serviceRequest.setTipoDisponibilitaRichiesta(tipoDisponibilita);
		CalcoloDisponibilitaDiUnCapitoloResponse response = executeExternalService(calcoloDisponibilitaDiUnCapitoloService, serviceRequest);
		
		return response.getDisponibilitaRichiesta();
	}


	/**
	 * Check variazione stanziamento cassa.
	 *
	 * @param importiDaAggiornare the importi da aggiornare
	 * @param importiAttuali the importi attuali
	 * @return the boolean
	 */
	private boolean checkVariazioneStanziamentoCassa(ImportiCapitoloEG importiDaAggiornare, ImportiCapitoloEG importiAttuali) {
		boolean stanziamentoCassaInvariato = stanziamentoCassaInvariato(importiDaAggiornare, importiAttuali);	
		if(stanziamentoCassaInvariato){
			return true;
		}

		BigDecimal diponibilitaStanziamentoCassa = calcolaDisponibilitaRichiesta("MioTipoDiponibilita sara' stanziamento, stanziamentoCassa ecc ?????????");
		return (diponibilitaStanziamentoCassa.add(importiAttuali.getStanziamentoCassa())).compareTo(importiDaAggiornare.getStanziamentoCassa()) <= 0;
	}

	/**
	 * Stanziamento cassa invariato.
	 *
	 * @param importiDaAggiornare the importi da aggiornare
	 * @param importiAttuali the importi attuali
	 * @return the boolean
	 */
	private boolean stanziamentoCassaInvariato(ImportiCapitoloEG importiDaAggiornare, ImportiCapitoloEG importiAttuali) {
		try{
			return importiAttuali.getStanziamentoCassa().compareTo(importiDaAggiornare.getStanziamentoCassa()) == 0;
		} catch(NullPointerException npe){
			boolean stanziamentoCassaNonValorizzato = importiAttuali == null || importiAttuali.getStanziamentoCassa() == null; 
			boolean stanziamentoCassaDaAggiornareNonValorizzato = importiDaAggiornare == null || importiDaAggiornare.getStanziamentoCassa() == null; 
			
			if(stanziamentoCassaNonValorizzato && stanziamentoCassaDaAggiornareNonValorizzato){
				return true;
			}
			
			return !stanziamentoCassaNonValorizzato && stanziamentoCassaDaAggiornareNonValorizzato;
		}
	}

	/**
	 * Checks if is valorizzato ex capitolo.
	 *
	 * @return true se è sono stati valorizzati i campi dell'ex capitolo
	 */
	private boolean isValorizzatoExCapitolo(){
		return req.getCapitoloEntrataGestione().getExAnnoCapitolo()!=null 
				&& req.getCapitoloEntrataGestione().getExCapitolo() != null
				&& req.getCapitoloEntrataGestione().getExArticolo() != null;
	}
	
	/**
	 * Ricerca l'ex capitolo .
	 *
	 * @return capitolo
	 */
	private CapitoloEntrataGestione ricercaExCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEGest criteriRicerca = new RicercaPuntualeCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(req.getCapitoloEntrataGestione().getExAnnoCapitolo());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataGestione().getExAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataGestione().getExCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataGestione().getExArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataGestione().getExUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataGestione().getStatoOperativoElementoDiBilancio());
		
		CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.ricercaPuntualeModulareCapitoloEntrataGestione(criteriRicerca);
		if(ceg == null) {
			String msg = String.format("Capitolo: %s ", 
					enteGestisceUEB() ? req.getCapitoloEntrataGestione().getDescBilancioAnnoNumeroArticoloUEBExCapitolo() : req.getCapitoloEntrataGestione().getDescBilancioAnnoNumeroArticoloExCapitolo());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ex Capitolo", msg));
		}
		return ceg;
	}
	
}
