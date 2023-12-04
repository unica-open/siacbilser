/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceCapitoloDiEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCapitoloDiEntrataPrevisioneService 
	extends CrudCapitoloBaseService<InserisceCapitoloDiEntrataPrevisione, InserisceCapitoloDiEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The ricerca puntuale capitolo entrata previsione service. */
	@Autowired
	private RicercaPuntualeCapitoloEntrataPrevisioneService ricercaPuntualeCapitoloEntrataPrevisioneService;
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataPrevisioneDad.setBilancio(req.getBilancio());
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());		
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);			
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		super.ente = req.getEnte();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		this.bilancio = req.getBilancio();
		
		checkNotNull(req.getCapitoloEntrataPrevisione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"),true);
		checkNotNull(req.getCapitoloEntrataPrevisione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo anno"), false);
		checkNotNull(req.getCapitoloEntrataPrevisione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero"), false);
		checkNotNull(req.getCapitoloEntrataPrevisione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataPrevisione().getNumeroUEB(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo numero ueb"), false);
		
		checkNotNull(req.getStruttAmmContabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("strutturaAmministrativoContabile"));
		checkCondition(req.getStruttAmmContabile().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("strutturaAmministrativoContabile uid"),false);
		
		checkEntita(req.getCapitoloEntrataPrevisione().getCategoriaCapitolo(), "categoria capitolo");
		
		// CR 2204
//		checkNotNull(req.getElementoPianoDeiConti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO	.getErrore("ElementoPianoDeiConti"));
//		checkCondition(req.getElementoPianoDeiConti().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ElementoPianoDeiConti uid"),false);
		
//		checkNotNull(req.getCategoriaTipologiaTitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CategoriaTipologiaTitolo"));
//		checkCondition(req.getCategoriaTipologiaTitolo().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CategoriaTipologiaTitolo uid"),false);
		
		//res.addErrore(errore) per casi particolari
				
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public InserisceCapitoloDiEntrataPrevisioneResponse executeService(InserisceCapitoloDiEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		//SIAC-7722: Pulisco le descrizioni da eventuali "a capo"
		req.setCapitoloEntrataPrevisione(pulisciDescrizioni(req.getCapitoloEntrataPrevisione()));
		
		checkClassificazioneBilancioSeStandard(req.getCapitoloEntrataPrevisione());
		
		if (esisteCapitoloEntrataPrevisione()) {
			//Se l'entità esiste viene ritornato codice di errore ENTITA_PRESENTE
			
			CapitoloEntrataPrevisione cap = req.getCapitoloEntrataPrevisione();			
			
			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Capitolo: %s", 
					enteGestisceUEB()? cap.getDescBilancioAnnoNumeroArticoloUEB(): cap.getDescBilancioAnnoNumeroArticolo()					
					//,cup.getStatoOperativoElementoDiBilancio().toString()
					)));
			res.setEsito(Esito.FALLIMENTO);
			return;
			
		}
		
		checkClassificatoriModificabili();
		checkAttributiModificabili();
		
		CapitoloEntrataPrevisione cap = capitoloEntrataPrevisioneDad.create(req.getCapitoloEntrataPrevisione()/*,
				req.getImportiCapitoloEP(), req.getTipoFondo(), req.getTipoFinanziamento(), req.getElementoPianoDeiConti(),
				 req.getClassificatoriGenerici(), req.getStruttAmmContabile(), 
				req.getCategoriaTipologiaTitolo()*/);

		res.setCapitoloEntrataPrevisione(cap);
		
	
	
		res.setEsito(Esito.SUCCESSO); //di default è già SUCCESSO!
	}

	
	
	/**
	 * Esiste capitolo entrata previsione.
	 *
	 * @return true se esiste
	 */
	private boolean esisteCapitoloEntrataPrevisione() {

		RicercaPuntualeCapitoloEPrev criteriRicerca = new RicercaPuntualeCapitoloEPrev();

		criteriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(req.getCapitoloEntrataPrevisione().getAnnoCapitolo());		
		criteriRicerca.setNumeroCapitolo(req.getCapitoloEntrataPrevisione().getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(req.getCapitoloEntrataPrevisione().getNumeroArticolo());
		criteriRicerca.setNumeroUEB(req.getCapitoloEntrataPrevisione().getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(req.getCapitoloEntrataPrevisione().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloEntrataPrevisione rp = new RicercaPuntualeCapitoloEntrataPrevisione();
		rp.setRichiedente(req.getRichiedente());
		rp.setEnte(req.getEnte());
		rp.setRicercaPuntualeCapitoloEPrev(criteriRicerca);
		
		
		RicercaPuntualeCapitoloEntrataPrevisioneResponse resRic = executeExternalService(ricercaPuntualeCapitoloEntrataPrevisioneService,rp);	

		return resRic.getCapitoloEntrataPrevisione() != null;
	}
	
	/**
	 *  
	 * Controlla se i classificatori che si stanno aggiornando sono compatibili con quanto previsto in configurazione.
	 */
	private void checkClassificatoriModificabili() {		
		Map<TipologiaClassificatore, Integer> classificatoriDaInserire = caricaClassificatoriDaInserire(req.getClassificatoriGenerici(),
				req.getCategoriaTipologiaTitolo(), req.getElementoPianoDeiConti(), req.getCapitoloEntrataPrevisione().getSiopeEntrata(),
				req.getStruttAmmContabile(),
				req.getTipoFinanziamento(), req.getTipoFondo(),
				// Aggiunti fase 4
				req.getCapitoloEntrataPrevisione().getRicorrenteEntrata(), req.getCapitoloEntrataPrevisione().getPerimetroSanitarioEntrata(),
				req.getCapitoloEntrataPrevisione().getTransazioneUnioneEuropeaEntrata());
		
		checkClassificatoriModificabiliInserimento(classificatoriDaInserire, req.getCapitoloEntrataPrevisione());		
	}
	

	/**
	 *  
	 * Controlla se i classificatori che si stanno inserendo sono compatibili con quanto previsto in configurazione.
	 */
	private void checkAttributiModificabili() {			
		checkAttributiModificabiliInserimento(req.getCapitoloEntrataPrevisione());		
	}
}