/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

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
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaPrevisioneModelDetail;

/**
 * The Class AggiornaCapitoloDiUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiUscitaPrevisioneService extends CrudCapitoloBaseService<AggiornaCapitoloDiUscitaPrevisione, AggiornaCapitoloDiUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
		
	@Override
	protected void init() {
		ente = req.getCapitoloUscitaPrevisione().getEnte();
		bilancio = req.getCapitoloUscitaPrevisione().getBilancio();
		
		capitoloUscitaPrevisioneDad.setBilancio(bilancio);
		capitoloUscitaPrevisioneDad.setEnte(ente);
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);
		
		capitoloUscitaGestioneDad.setEnte(ente);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloUscitaPrevisione(), "capitoloUscitaPrevisione");
		checkEntita(req.getCapitoloUscitaPrevisione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloUscitaPrevisione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloUscitaPrevisione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
		
		checkEntita(req.getCapitoloUscitaPrevisione().getStrutturaAmministrativoContabile(), "strutturaAmministrativoContabile", false);
		
		// CR 2204
//		checkEntita(req.getCapitoloUscitaPrevisione().getElementoPianoDeiConti(), "elementoPianoDeiConti", false);
//		checkNotNull(req.getCapitoloUscitaPrevisione().getMacroaggregato(), "macroaggregato", false);
//		checkNotNull(req.getCapitoloUscitaPrevisione().getProgramma(), "programma", false);
	}

	@Transactional
	public AggiornaCapitoloDiUscitaPrevisioneResponse executeService(AggiornaCapitoloDiUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//SIAC-5003 Il capitolo e' modificabile solo se lo stato operativo e' di tipo valido o provvisorio
		
		checkCapitoloModificabilePerAggiornamento(req.getCapitoloUscitaPrevisione());
		
		//SIAC-5007
		checkCapitoloUscitaPrevisioneSeFPV(req.getCapitoloUscitaPrevisione());
		checkCapitoloUscitaPrevisioneSeStandard(req.getCapitoloUscitaPrevisione());

		CapitoloUscitaPrevisione cupAttuale = cercaCapitoloUscitaPrevisioneAttuale();
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		// SIAC-6881
		// Ricarico gli importi originali: la variazione dei dati e' gestita lato componente
		loadImportiOriginali(cupAttuale);
		
		if(isValorizzatoExCapitolo()){
			CapitoloUscitaGestione exCap = ricercaExCapitoloUscitaGestione();
			req.getCapitoloUscitaPrevisione().setUidExCapitolo(exCap.getUid());
		}
		
		CapitoloUscitaPrevisione cup = capitoloUscitaPrevisioneDad.update(req.getCapitoloUscitaPrevisione());
		res.setCapitoloUscitaPrevisione(cup);
	}
	
	/**
	 * Caricamento degli importi originali del capitolo
	 * @param cupAttuale
	 */
	private void loadImportiOriginali(CapitoloUscitaPrevisione cupAttuale) {
		List<ImportiCapitoloUP> listaImportiCapitolo = new ArrayList<ImportiCapitoloUP>();
		listaImportiCapitolo.add(cupAttuale.getImportiCapitolo());
		listaImportiCapitolo.add(importiCapitoloDad.findImportiCapitolo(cupAttuale.getUid(), cupAttuale.getAnnoCapitolo().intValue() + 1, ImportiCapitoloUP.class, EnumSet.noneOf(ImportiCapitoloEnum.class)));
		listaImportiCapitolo.add(importiCapitoloDad.findImportiCapitolo(cupAttuale.getUid(), cupAttuale.getAnnoCapitolo().intValue() + 2, ImportiCapitoloUP.class, EnumSet.noneOf(ImportiCapitoloEnum.class)));
		
		req.getCapitoloUscitaPrevisione().setListaImportiCapitoloUP(listaImportiCapitolo);
		req.getCapitoloUscitaPrevisione().setImportiCapitoloUP(listaImportiCapitolo.get(0));
	}

	/**
	 * Cerca capitolo uscita previsione attuale.
	 * @return the capitolo uscita previsione
	 */
	private CapitoloUscitaPrevisione cercaCapitoloUscitaPrevisioneAttuale() {
		RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();

		criteriRicerca.setAnnoEsercizio(bilancio.getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaPrevisione().getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaPrevisione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaPrevisione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaPrevisione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaPrevisione().getStatoOperativoElementoDiBilancio()); 
		//se si vuole fare in modo che l'aggiornamento aggiorni anche lo stato la ricerca puntuale non deve avere come parametro obbligatorio lo stato! 
		//togliendo questa obbligatorietà il servizio aggiorna correttamente anche lo stato
		
		CapitoloUscitaPrevisione cup = capitoloUscitaPrevisioneDad.ricercaPuntualeModulareCapitoloUscitaPrevisione(criteriRicerca, CapitoloUscitaPrevisioneModelDetail.Stato);
		if(cup == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("aggiornamento", String.format("Capitolo: %s ",
				enteGestisceUEB() ? req.getCapitoloUscitaPrevisione().getDescBilancioAnnoNumeroArticoloUEB() : req.getCapitoloUscitaPrevisione().getDescBilancioAnnoNumeroArticolo())));
		}
		if(req.getCapitoloUscitaPrevisione().getUid() != cup.getUid()){
			throw new IllegalArgumentException("Trovato capitolo con Uid incongruente. " +String.format("Capitolo: %s/%s/%s/%s/%s/%s",
					bilancio.getAnno(),
					req.getCapitoloUscitaPrevisione().getAnnoCapitolo(),
					req.getCapitoloUscitaPrevisione().getNumeroCapitolo(),
					req.getCapitoloUscitaPrevisione().getNumeroArticolo(),
					req.getCapitoloUscitaPrevisione().getNumeroUEB(),
					req.getCapitoloUscitaPrevisione().getStatoOperativoElementoDiBilancio().toString()));
			
		}
		// Calcolo gli importi
		ImportiCapitoloUP importiCapitoloUP = importiCapitoloDad.findImportiCapitolo(cup, bilancio.getAnno(), ImportiCapitoloUP.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		cup.setImportiCapitolo(importiCapitoloUP);
		
		return cup;
	}
	
	/**
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkClassificatoriModificabili() {
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getCapitoloUscitaPrevisione().getClassificatoriGenerici(),
				req.getCapitoloUscitaPrevisione().getProgramma(), req.getCapitoloUscitaPrevisione().getClassificazioneCofogProgramma(),
				req.getCapitoloUscitaPrevisione().getMacroaggregato(), req.getCapitoloUscitaPrevisione().getElementoPianoDeiConti(), req.getCapitoloUscitaPrevisione().getSiopeSpesa(),
				req.getCapitoloUscitaPrevisione().getStrutturaAmministrativoContabile(),
				req.getCapitoloUscitaPrevisione().getTipoFinanziamento(), req.getCapitoloUscitaPrevisione().getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloUscitaPrevisione().getRicorrenteSpesa(), req.getCapitoloUscitaPrevisione().getPerimetroSanitarioSpesa(),
				req.getCapitoloUscitaPrevisione().getTransazioneUnioneEuropeaSpesa(), req.getCapitoloUscitaPrevisione().getPoliticheRegionaliUnitarie());
		
		checkClassificatoriModificabiliAggiornamento(classificatoriDaInserire, req.getCapitoloUscitaPrevisione());
	}
	
	/**
	 * Controlla se gli attributi che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	protected void checkAttributiModificabili() {
		checkAttributiModificabiliAggiornamento(req.getCapitoloUscitaPrevisione());
	}
	
	/**
	 * Checks if is valorizzato ex capitolo.
	 *
	 * @return true se è sono stati valorizzati i campi dell'ex capitolo
	 */
	private boolean isValorizzatoExCapitolo(){
		return req.getCapitoloUscitaPrevisione().getExAnnoCapitolo()!=null 
				&& req.getCapitoloUscitaPrevisione().getExCapitolo() != null
				&& req.getCapitoloUscitaPrevisione().getExArticolo() != null;
	}
	
	/**
	 * Ricerca l'ex capitolo.
	 *
	 * @return capitolo
	 */
	private CapitoloUscitaGestione ricercaExCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUGest criteriRicerca = new RicercaPuntualeCapitoloUGest();

		criteriRicerca.setAnnoEsercizio(req.getCapitoloUscitaPrevisione().getExAnnoCapitolo());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloUscitaPrevisione().getExAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(req.getCapitoloUscitaPrevisione().getExCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloUscitaPrevisione().getExArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloUscitaPrevisione().getExUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloUscitaPrevisione().getStatoOperativoElementoDiBilancio());
		
		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.ricercaPuntualeModulareCapitoloUscitaGestione(criteriRicerca);
		if(cug == null) {
			String msg = String.format("Capitolo: %s ", 
					enteGestisceUEB() ? req.getCapitoloUscitaPrevisione().getDescBilancioAnnoNumeroArticoloUEBExCapitolo() : req.getCapitoloUscitaPrevisione().getDescBilancioAnnoNumeroArticoloExCapitolo());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ex Capitolo", msg));
		}
		return cug;
	}

}
