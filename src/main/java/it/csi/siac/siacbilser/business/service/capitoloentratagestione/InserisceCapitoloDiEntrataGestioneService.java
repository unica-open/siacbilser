/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceCapitoloDiEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCapitoloDiEntrataGestioneService 
	extends CrudCapitoloBaseService<InserisceCapitoloDiEntrataGestione, InserisceCapitoloDiEntrataGestioneResponse> {


	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The ricerca puntuale capitolo entrata gestione service. */
	@Autowired
	private RicercaPuntualeCapitoloEntrataGestioneService ricercaPuntualeCapitoloEntrataGestioneService;
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setBilancio(req.getBilancio());
		capitoloEntrataGestioneDad.setEnte(req.getEnte());		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);			
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		this.ente = req.getEnte();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		this.bilancio = req.getBilancio();
		
		checkNotNull(req.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"),true);
		checkNotNull(req.getCapitoloEntrataGestione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo anno"), false);
		checkNotNull(req.getCapitoloEntrataGestione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero"), false);
		checkNotNull(req.getCapitoloEntrataGestione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataGestione().getNumeroUEB(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero ueb"), false);
		
		checkNotNull(req.getStruttAmmContabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("strutturaAmministrativoContabile"));
		checkCondition(req.getStruttAmmContabile().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("strutturaAmministrativoContabile uid"),false);
		
		checkEntita(req.getCapitoloEntrataGestione().getCategoriaCapitolo(), "categoria capitolo");
		
		// CR 2204
//		checkNotNull(req.getElementoPianoDeiConti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti"));
//		checkCondition(req.getElementoPianoDeiConti().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti uid"),false);
		
//		checkNotNull(req.getCategoriaTipologiaTitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CategoriaTipologiaTitolo"));
//		checkCondition(req.getCategoriaTipologiaTitolo().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CategoriaTipologiaTitolo uid"),false);
				
		//res.addErrore(errore) per casi particolari
				
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public InserisceCapitoloDiEntrataGestioneResponse executeService(InserisceCapitoloDiEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkClassificazioneBilancioSeStandard(req.getCapitoloEntrataGestione());
		
		if (esisteCapitoloEntrataGestione()) {
			//Se l'entità esiste viene ritornato codice di errore ENTITA_PRESENTE
			
			CapitoloEntrataGestione cap = req.getCapitoloEntrataGestione();			
			
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Capitolo: %s", 
					enteGestisceUEB()? cap.getDescBilancioAnnoNumeroArticoloUEB(): cap.getDescBilancioAnnoNumeroArticolo()					
					//,cup.getStatoOperativoElementoDiBilancio().toString()
					)));
			res.setEsito(Esito.FALLIMENTO);
			return;
			
		}
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		
		
		CapitoloEntrataGestione cap = capitoloEntrataGestioneDad.create(req.getCapitoloEntrataGestione()/*,
				req.getImportiCapitoloEG(), req.getTipoFondo(), req.getTipoFinanziamento(), req.getElementoPianoDeiConti(),
				req.getClassificatoriGenerici(), req.getStruttAmmContabile(), 
				req.getCategoriaTipologiaTitolo()*/);

		res.setCapitoloEntrataGestione(cap);
		
	
	
		res.setEsito(Esito.SUCCESSO); //di default è già SUCCESSO!
	}

	
	
	/**
	 * Esiste capitolo entrata gestione.
	 *
	 * @return true se esiste
	 */
	private boolean esisteCapitoloEntrataGestione() {

		RicercaPuntualeCapitoloEGest criteriRicerca = new RicercaPuntualeCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataGestione().getAnnoCapitolo());		
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataGestione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataGestione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataGestione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataGestione().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloEntrataGestione rp = new RicercaPuntualeCapitoloEntrataGestione();
		rp.setRichiedente(req.getRichiedente());
		rp.setEnte(req.getEnte());
		rp.setRicercaPuntualeCapitoloEGest(criteriRicerca);
		
		
		RicercaPuntualeCapitoloEntrataGestioneResponse resRic = executeExternalService(ricercaPuntualeCapitoloEntrataGestioneService,rp);	

		return resRic.getCapitoloEntrataGestione() != null;
	}
	

	/**
	 *  
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	private void checkClassificatoriModificabili() {		
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getClassificatoriGenerici(),
				req.getCategoriaTipologiaTitolo(), req.getElementoPianoDeiConti(), req.getCapitoloEntrataGestione().getSiopeEntrata(),
				req.getStruttAmmContabile(),
				req.getTipoFinanziamento(), req.getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloEntrataGestione().getRicorrenteEntrata(), req.getCapitoloEntrataGestione().getPerimetroSanitarioEntrata(),
				req.getCapitoloEntrataGestione().getTransazioneUnioneEuropeaEntrata());
		
		checkClassificatoriModificabiliInserimento(classificatoriDaInserire, req.getCapitoloEntrataGestione());		
	}
	

	/**
	 *  
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkAttributiModificabili() {			
		checkAttributiModificabiliInserimento(req.getCapitoloEntrataGestione());		
	}

}
