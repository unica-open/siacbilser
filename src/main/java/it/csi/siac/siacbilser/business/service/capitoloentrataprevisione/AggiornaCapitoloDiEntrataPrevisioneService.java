/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CalcoloDisponibilitaDiUnCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * The Class AggiornaCapitoloDiEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiEntrataPrevisioneService extends CrudCapitoloBaseService<AggiornaCapitoloDiEntrataPrevisione, AggiornaCapitoloDiEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The calcolo disponibilita di un capitolo service. */
	@Autowired
	private CalcoloDisponibilitaDiUnCapitoloService calcoloDisponibilitaDiUnCapitoloService;
	
	@Override
	protected void init() {
		ente = req.getCapitoloEntrataPrevisione().getEnte();
		bilancio = req.getCapitoloEntrataPrevisione().getBilancio();
		
		capitoloEntrataPrevisioneDad.setBilancio(bilancio);
		capitoloEntrataPrevisioneDad.setEnte(ente);
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);
		
		capitoloEntrataGestioneDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataPrevisione(), "capitoloEntrataPrevisione");
		checkEntita(req.getCapitoloEntrataPrevisione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloEntrataPrevisione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloEntrataPrevisione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
		
		checkEntita(req.getCapitoloEntrataPrevisione().getStrutturaAmministrativoContabile(), "strutturaAmministrativoContabile", false);
		
		// CR 2204
//		checkEntita(req.getCapitoloEntrataPrevisione().getElementoPianoDeiConti(), "ElementoPianoDeiConti", false);
//		checkEntita(req.getCapitoloEntrataPrevisione().getCategoriaTipologiaTitolo(), "CategoriaTipologiaTitolo", false);
	}

	@Transactional
	public AggiornaCapitoloDiEntrataPrevisioneResponse executeService(AggiornaCapitoloDiEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//SIAC-7722: Pulisco le descrizioni da eventuali "a capo"
		req.setCapitoloEntrataPrevisione(pulisciDescrizioni(req.getCapitoloEntrataPrevisione()));
		
		checkClassificazioneBilancioSeStandard(req.getCapitoloEntrataPrevisione());
		
		CapitoloEntrataPrevisione cepAttuale = cercaCapitoloEntrataPrevisioneAttuale();
		
		//SIAC-5003 Il capitolo e' modificabile solo se lo stato operativo e' di tipo valido o provvisorio
		checkCapitoloModificabilePerAggiornamento(cepAttuale);
				
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		checkVariazioneImporti(cepAttuale);

		if(isValorizzatoExCapitolo()){
			CapitoloEntrataGestione exCap = ricercaExCapitoloEntrataGestione();
			req.getCapitoloEntrataPrevisione().setUidExCapitolo(exCap.getUid());
		}
		
		CapitoloEntrataPrevisione cep = capitoloEntrataPrevisioneDad.update(req.getCapitoloEntrataPrevisione());
		res.setCapitoloEntrataPrevisione(cep);
	}
	
	/**
	 * Cerca capitolo entrata previsione attuale.
	 *
	 * @return the capitolo entrata previsione
	 */
	private CapitoloEntrataPrevisione cercaCapitoloEntrataPrevisioneAttuale() {
		RicercaPuntualeCapitoloEPrev criteriRicerca = new RicercaPuntualeCapitoloEPrev();

		criteriRicerca.setAnnoEsercizio(bilancio.getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataPrevisione().getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataPrevisione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataPrevisione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataPrevisione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataPrevisione().getStatoOperativoElementoDiBilancio());
		
		CapitoloEntrataPrevisione cep = capitoloEntrataPrevisioneDad.ricercaPuntualeModulareCapitoloEntrataPrevisione(criteriRicerca, CapitoloEntrataPrevisioneModelDetail.Stato);
		if(cep == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("aggiornamento", String.format("Capitolo: %s ",
				enteGestisceUEB() ? req.getCapitoloEntrataPrevisione().getDescBilancioAnnoNumeroArticoloUEB() : req.getCapitoloEntrataPrevisione().getDescBilancioAnnoNumeroArticolo())));
		}
		if(req.getCapitoloEntrataPrevisione().getUid() != cep.getUid()){
			throw new IllegalArgumentException("Trovato capitolo con Uid incongruente. " +String.format("Capitolo: %s/%s/%s/%s/%s/%s",
					bilancio.getAnno(),
					req.getCapitoloEntrataPrevisione().getAnnoCapitolo(),
					req.getCapitoloEntrataPrevisione().getNumeroCapitolo(),
					req.getCapitoloEntrataPrevisione().getNumeroArticolo(),
					req.getCapitoloEntrataPrevisione().getNumeroUEB(),
					req.getCapitoloEntrataPrevisione().getStatoOperativoElementoDiBilancio().toString()));
			
		}
		// Calcolo gli importi
		ImportiCapitoloEP importiCapitoloEP = importiCapitoloDad.findImportiCapitolo(cep, bilancio.getAnno(), ImportiCapitoloEP.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		cep.setImportiCapitolo(importiCapitoloEP);
		
		return cep;
	}
	
	/**
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkClassificatoriModificabili() {
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getCapitoloEntrataPrevisione().getClassificatoriGenerici(),
				req.getCapitoloEntrataPrevisione().getCategoriaTipologiaTitolo(), req.getCapitoloEntrataPrevisione().getElementoPianoDeiConti(), req.getCapitoloEntrataPrevisione().getSiopeEntrata(),
				req.getCapitoloEntrataPrevisione().getStrutturaAmministrativoContabile(),
				req.getCapitoloEntrataPrevisione().getTipoFinanziamento(), req.getCapitoloEntrataPrevisione().getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloEntrataPrevisione().getRicorrenteEntrata(), req.getCapitoloEntrataPrevisione().getPerimetroSanitarioEntrata(),
				req.getCapitoloEntrataPrevisione().getTransazioneUnioneEuropeaEntrata());
		
		checkClassificatoriModificabiliAggiornamento(classificatoriDaInserire, req.getCapitoloEntrataPrevisione());
	}
	
	/**
	 *  
	 * Controlla se gli attributi che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkAttributiModificabili() {
		checkAttributiModificabiliAggiornamento(req.getCapitoloEntrataPrevisione());
	}
	
	/**
	 * Controlla la variazione degli importi Stanziamento e Stanziamento cassa
	 * Restituisce true se sono invariati o se la variazione &eacute; tale che:
	 * <pre>(disp + importoAttuale) &leq; importoNuovo</pre>
	 *
	 * @param cepAttuale the cep attuale
	 */
	private void checkVariazioneImporti(CapitoloEntrataPrevisione cepAttuale) {
		ImportiCapitoloEP importiDaAggiornare = req.getCapitoloEntrataPrevisione().getImportiCapitoloEP();
		ImportiCapitoloEP importiAttuali = cepAttuale.getImportiCapitoloEP();
		
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
	private boolean checkVariazioneStanziamento(ImportiCapitoloEP importiDaAggiornare, ImportiCapitoloEP importiAttuali) {
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
	private boolean stanziamentoInvariato(ImportiCapitoloEP importiDaAggiornare, ImportiCapitoloEP importiAttuali) {
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
		serviceRequest.setAnnoCapitolo(req.getCapitoloEntrataPrevisione().getAnnoCapitolo());
		serviceRequest.setNumroCapitolo(req.getCapitoloEntrataPrevisione().getNumeroCapitolo());
		serviceRequest.setFase(new FaseEStatoAttualeBilancio());
		serviceRequest.setTipoDisponibilitaRichiesta(tipoDisponibilita);
		CalcoloDisponibilitaDiUnCapitoloResponse response = executeExternalService(calcoloDisponibilitaDiUnCapitoloService,serviceRequest);
		return response.getDisponibilitaRichiesta();
	}
	
	/**
	 * Checkvariazione stanziamento cassa.
	 *
	 * @param importiDaAggiornare the importi da aggiornare
	 * @param importiAttuali the importi attuali
	 * @return the boolean
	 */
	private boolean checkVariazioneStanziamentoCassa(ImportiCapitoloEP importiDaAggiornare, ImportiCapitoloEP importiAttuali) {
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
	private boolean stanziamentoCassaInvariato(ImportiCapitoloEP importiDaAggiornare, ImportiCapitoloEP importiAttuali) {
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
		return req.getCapitoloEntrataPrevisione().getExAnnoCapitolo()!=null
				&& req.getCapitoloEntrataPrevisione().getExCapitolo() != null
				&& req.getCapitoloEntrataPrevisione().getExArticolo() != null;
	}
	
	/**
	 * Ricerca l'ex capitolo .
	 *
	 * @return capitolo
	 */
	private CapitoloEntrataGestione ricercaExCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEGest criteriRicerca = new RicercaPuntualeCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(req.getCapitoloEntrataPrevisione().getExAnnoCapitolo());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataPrevisione().getExAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataPrevisione().getExCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataPrevisione().getExArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataPrevisione().getExUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataPrevisione().getStatoOperativoElementoDiBilancio());
		
		CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.ricercaPuntualeModulareCapitoloEntrataGestione(criteriRicerca);
		if(ceg == null) {
			String msg = String.format("Capitolo: %s ", 
					enteGestisceUEB() ? req.getCapitoloEntrataPrevisione().getDescBilancioAnnoNumeroArticoloUEBExCapitolo() : req.getCapitoloEntrataPrevisione().getDescBilancioAnnoNumeroArticoloExCapitolo());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ex Capitolo", msg));
		}
		return ceg;
	}

}
