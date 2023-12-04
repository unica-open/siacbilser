/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaMovimentiCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaPuntualeCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.DocumentoDiEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaMovimentiCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentiCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<RicercaMovimentiCapitoloEntrataPrevisione, RicercaMovimentiCapitoloEntrataPrevisioneResponse> {
	
	/** The ricerca variazioni capitolo entrata previsione service. */
	@Autowired
	private RicercaVariazioniCapitoloEntrataPrevisioneService ricercaVariazioniCapitoloEntrataPrevisioneService;
	
	/** The ricerca vincolo capitolo previsione service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloCapitoloPrevisioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	/** The ricerca puntuale capitolo entrata gestione service. */
	@Autowired
	private RicercaPuntualeCapitoloEntrataGestioneService ricercaPuntualeCapitoloEntrataGestioneService;
	
	/** The ricerca movimenti capitolo entrata gestione service. */
	@Autowired
	private RicercaMovimentiCapitoloEntrataGestioneService ricercaMovimentiCapitoloEntrataGestioneService;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), true);
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(req.getCapitoloEPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione"));
		checkCondition(req.getCapitoloEPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaMovimentiCapitoloEntrataPrevisioneResponse executeService(RicercaMovimentiCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// RICERCA VARIAZIONI CAPITOLO
		RicercaVariazioniCapitoloEntrataPrevisioneResponse variazioni = ricercaVariazioni();
		List<VariazioneImportoCapitolo>  listaVariazioneImporti   = variazioni.getListaVariazioniImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodifiche = variazioni.getListaVariazioniCodifiche();
				
		// RICERCA VINCOLI CAPITOLO
		RicercaVincoloResponse vincoli = ricercaVincoli();
		List<VincoloCapitoli>  listaVincoli = vincoli.getVincoloCapitoli();//.getListaVincoliCapitoloUEPrev();
		
		// FASE IN ESERCIZIO PROVVISORIO?
		RicercaMovimentiCapitoloEntrataGestioneResponse movimenti = new RicercaMovimentiCapitoloEntrataGestioneResponse();
		if (isBilancioInFaseEsercizioProvvisorio()) {
			// RICERCA MOVIMENTI CAPITOLO GESTIONE EQUIVALENTE
			movimenti = ricercaMovimenti(ricercaCapitoloEntrataGestione());
		}
		List<VariazioneImportoCapitolo>  listaVariazioneImportiUG   = movimenti.getListaVariazioneImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodificheUG = movimenti.getListaVariazioneCodifiche();
		List<VincoloCapitoli>		     listaVincoliEG             = movimenti.getListaVincoli();
		List<Accertamento>               listaAccertamento          = movimenti.getListaAccertamenti();
		List<DocumentoDiEntrata>         documentoDiEntrata         = movimenti.getDocumentiEntrata();
		
		// RISPOSTA
		res.setListaVariazioneImporti(listaVariazioneImporti);
		res.setListaVariazioneCodifiche(listaVariazioneCodifiche);
		res.setListaVincoli(listaVincoli);
		res.setListaVariazioneImportiCapEGest(listaVariazioneImportiUG);
		res.setListaVariazioneCodificheCapEGest(listaVariazioneCodificheUG);
		res.setListaVincoliCapEGest(listaVincoliEG);
		res.setListaAccertamenti(listaAccertamento);
		res.setDocumentiEntrata(documentoDiEntrata);
	}
	
	/**
	 * Ricerca variazioni.
	 *
	 * @return the ricerca variazioni capitolo entrata previsione response
	 */
	private RicercaVariazioniCapitoloEntrataPrevisioneResponse ricercaVariazioni() {
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = req.getCapitoloEPrev();
		capitoloEntrataPrevisione.setEnte(req.getEnte());		
		capitoloEntrataPrevisione.setBilancio(req.getBilancio());
		
		RicercaVariazioniCapitoloEntrataPrevisione ricercaVariazioni = new RicercaVariazioniCapitoloEntrataPrevisione();
		ricercaVariazioni.setRichiedente(req.getRichiedente());
		ricercaVariazioni.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		ricercaVariazioni.setDataOra(new Date());
		
		return executeExternalService(ricercaVariazioniCapitoloEntrataPrevisioneService,ricercaVariazioni);
	}
	
	/**
	 * Ricerca vincoli.
	 *
	 * @return the ricerca vincolo response
	 */
	private RicercaVincoloResponse ricercaVincoli() {
		
		RicercaVincolo rv = new RicercaVincolo();
		rv.setRichiedente(req.getRichiedente());
		rv.setDataOra(new Date());
		rv.setCapitolo(req.getCapitoloEPrev());
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getEnte());
		rv.setVincolo(vincolo);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(100);
		pp.setNumeroPagina(0);
		rv.setParametriPaginazione(pp);
		
		return executeExternalService(ricercaVincoloCapitoloPrevisioneService,rv);
	}
	
	/**
	 * Checks if is bilancio in fase esercizio provvisorio.
	 *
	 * @return true, if is bilancio in fase esercizio provvisorio
	 */
	private boolean isBilancioInFaseEsercizioProvvisorio() {
		bilancioDad.setEnteEntity(req.getEnte());
		return bilancioDad.isFaseEsercizioProvvisiorio(req.getBilancio().getAnno());
	}

	/**
	 * Ricerca capitolo entrata gestione.
	 *
	 * @return the capitolo entrata gestione
	 */
	private CapitoloEntrataGestione ricercaCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEGest ricercaPuntualeCapitoloEGest = new RicercaPuntualeCapitoloEGest();
		ricercaPuntualeCapitoloEGest.setAnnoEsercizio(req.getCapitoloEPrev().getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setAnnoCapitolo(req.getCapitoloEPrev().getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroCapitolo(req.getCapitoloEPrev().getNumeroCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroArticolo(req.getCapitoloEPrev().getNumeroArticolo());
		ricercaPuntualeCapitoloEGest.setNumeroUEB(req.getCapitoloEPrev().getNumeroUEB());
		ricercaPuntualeCapitoloEGest.setStatoOperativoElementoDiBilancio(req.getCapitoloEPrev().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloEntrataGestione ricercaPuntualeCapitoloEntrataGestione = new RicercaPuntualeCapitoloEntrataGestione();
		ricercaPuntualeCapitoloEntrataGestione.setEnte(req.getEnte());
		ricercaPuntualeCapitoloEntrataGestione.setRichiedente(req.getRichiedente());
		ricercaPuntualeCapitoloEntrataGestione.setRicercaPuntualeCapitoloEGest(ricercaPuntualeCapitoloEGest);
		ricercaPuntualeCapitoloEntrataGestione.setDataOra(new Date());
					
		RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntualeCapitoloEntrataGestioneResponse = executeExternalService(ricercaPuntualeCapitoloEntrataGestioneService,ricercaPuntualeCapitoloEntrataGestione);
		return ricercaPuntualeCapitoloEntrataGestioneResponse.getCapitoloEntrataGestione();
	}
	
	/**
	 * Ricerca movimenti.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the ricerca movimenti capitolo entrata gestione response
	 */
	private RicercaMovimentiCapitoloEntrataGestioneResponse ricercaMovimenti(CapitoloEntrataGestione capitoloEntrataGestione) {
		if (capitoloEntrataGestione == null){
			return new RicercaMovimentiCapitoloEntrataGestioneResponse();
		} 
		
		RicercaMovimentiCapitoloEntrataGestione ricercaMovimentiCapitoloEntrataGestione = new RicercaMovimentiCapitoloEntrataGestione();
		ricercaMovimentiCapitoloEntrataGestione.setEnte(req.getEnte());
		ricercaMovimentiCapitoloEntrataGestione.setRichiedente(req.getRichiedente());
		ricercaMovimentiCapitoloEntrataGestione.setBilancio(req.getBilancio());
		ricercaMovimentiCapitoloEntrataGestione.setCapitoloEntrataGestione(capitoloEntrataGestione);
		ricercaMovimentiCapitoloEntrataGestione.setDataOra(new Date());
		
		return executeExternalService(ricercaMovimentiCapitoloEntrataGestioneService,ricercaMovimentiCapitoloEntrataGestione);
	}
}
