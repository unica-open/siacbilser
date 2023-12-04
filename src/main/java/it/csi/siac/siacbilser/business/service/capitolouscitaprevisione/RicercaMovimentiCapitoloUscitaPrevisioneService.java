/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// TODO: Auto-generated Javadoc

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaMovimentiCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaPuntualeCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.DocumentoDiSpesa;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class RicercaMovimentiCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentiCapitoloUscitaPrevisioneService extends
		ExtendedBaseService<RicercaMovimentiCapitoloUscitaPrevisione, RicercaMovimentiCapitoloUscitaPrevisioneResponse> {
	
	/** The ricerca variazioni capitolo uscita previsione service. */
	@Autowired
	private RicercaVariazioniCapitoloUscitaPrevisioneService ricercaVariazioniCapitoloUscitaPrevisioneService;
	
	/** The ricerca vincolo capitolo previsione service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloCapitoloPrevisioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	/** The ricerca puntuale capitolo uscita gestione service. */
	@Autowired
	private RicercaPuntualeCapitoloUscitaGestioneService ricercaPuntualeCapitoloUscitaGestioneService;
	
	/** The ricerca movimenti capitolo uscita gestione service. */
	@Autowired
	private RicercaMovimentiCapitoloUscitaGestioneService ricercaMovimentiCapitoloUscitaGestioneService;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), true);
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(req.getCapitoloUPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));
		checkCondition(req.getCapitoloUPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaMovimentiCapitoloUscitaPrevisioneResponse executeService(RicercaMovimentiCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// RICERCA VARIAZIONI CAPITOLO
		RicercaVariazioniCapitoloUscitaPrevisioneResponse variazioni = ricercaVariazioni();
		List<VariazioneImportoCapitolo>  listaVariazioneImporti   = variazioni.getListaVariazioniImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodifiche = variazioni.getListaVariazioniCodifiche();
				
		// RICERCA VINCOLI CAPITOLO
		RicercaVincoloResponse vincoli = ricercaVincoli();
		List<VincoloCapitoli>  listaVincoli = vincoli.getVincoloCapitoli();//.getListaVincoliCapitoloUEPrev();
		
		// FASE IN ESERCIZIO PROVVISORIO?
		RicercaMovimentiCapitoloUscitaGestioneResponse movimenti = new RicercaMovimentiCapitoloUscitaGestioneResponse();
		if (isBilancioInFaseEsercizioProvvisorio()) {
			// RICERCA MOVIMENTI CAPITOLO GESTIONE EQUIVALENTE
			movimenti = ricercaMovimenti(ricercaCapitoloUscitaGestione());
		}
		List<VariazioneImportoCapitolo>  listaVariazioneImportiUG   = movimenti.getListaVariazioneImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodificheUG = movimenti.getListaVariazioneCodifiche();
		List<VincoloCapitoli>			 listaVincoliUG             = movimenti.getListaVincoli();
		List<Impegno>                    listaImpegni               = movimenti.getListaImpegni();
		List<DocumentoDiSpesa>           listaDocumentoDiSpesa      = movimenti.getListaDocumentoSpesa();
		
		// RISPOSTA
		res.setListaVariazioneImporti(listaVariazioneImporti);
		res.setListaVariazioneCodifiche(listaVariazioneCodifiche);
		res.setListaVincoli(listaVincoli);
		res.setListaVariazioneImportiCapUGest(listaVariazioneImportiUG);
		res.setListaVariazioneCodificheCapUGest(listaVariazioneCodificheUG);
		res.setListaVincoliCapUGest(listaVincoliUG);
		res.setListaImpegni(listaImpegni);
		res.setListaDocumentoSpesa(listaDocumentoDiSpesa);
	}
	
	/**
	 * Ricerca variazioni.
	 *
	 * @return the ricerca variazioni capitolo uscita previsione response
	 */
	private RicercaVariazioniCapitoloUscitaPrevisioneResponse ricercaVariazioni() {
		CapitoloUscitaPrevisione capitoloUPrev = req.getCapitoloUPrev();
		capitoloUPrev.setEnte(req.getEnte());		
		capitoloUPrev.setBilancio(req.getBilancio());	
		
		RicercaVariazioniCapitoloUscitaPrevisione ricercaVariazioni = new RicercaVariazioniCapitoloUscitaPrevisione();
		ricercaVariazioni.setRichiedente(req.getRichiedente());		
		ricercaVariazioni.setCapitoloUscitaPrev(capitoloUPrev);
		ricercaVariazioni.setDataOra(new Date());
		
		return executeExternalService(ricercaVariazioniCapitoloUscitaPrevisioneService,ricercaVariazioni);
	}
	
	/**
	 * Ricerca vincoli.
	 *
	 * @return the ricerca vincolo response
	 */
	private RicercaVincoloResponse ricercaVincoli() {
//		RicercaVincoloCapitoloPrev ricercaVincoloCapitoloPrev = new RicercaVincoloCapitoloPrev();
//		ricercaVincoloCapitoloPrev.setAnnoEsercizio(req.getBilancio().getAnno());
//		ricercaVincoloCapitoloPrev.setTipoVincolo("P"); //TODO sarà corretto?
//		ricercaVincoloCapitoloPrev.setNumeroCapitoloUPrev(req.getCapitoloUPrev().getNumeroCapitolo()); //TODO sarà corretto?

		RicercaVincolo rv = new RicercaVincolo();
		rv.setRichiedente(req.getRichiedente());
		rv.setDataOra(new Date());
		rv.setCapitolo(req.getCapitoloUPrev());
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
	 * Ricerca capitolo uscita gestione.
	 *
	 * @return the capitolo uscita gestione
	 */
	private CapitoloUscitaGestione ricercaCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUGest ricercaPuntualeCapitoloUGest = new RicercaPuntualeCapitoloUGest();
		ricercaPuntualeCapitoloUGest.setAnnoEsercizio(req.getCapitoloUPrev().getAnnoCapitolo());
		ricercaPuntualeCapitoloUGest.setAnnoCapitolo(req.getCapitoloUPrev().getAnnoCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroCapitolo(req.getCapitoloUPrev().getNumeroCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroArticolo(req.getCapitoloUPrev().getNumeroArticolo());
		ricercaPuntualeCapitoloUGest.setNumeroUEB(req.getCapitoloUPrev().getNumeroUEB());
		ricercaPuntualeCapitoloUGest.setStatoOperativoElementoDiBilancio(req.getCapitoloUPrev().getStatoOperativoElementoDiBilancio());

		RicercaPuntualeCapitoloUscitaGestione ricercaPuntualeCapitoloUscitaGestione = new RicercaPuntualeCapitoloUscitaGestione();
		ricercaPuntualeCapitoloUscitaGestione.setEnte(req.getEnte());
		ricercaPuntualeCapitoloUscitaGestione.setRichiedente(req.getRichiedente());
		ricercaPuntualeCapitoloUscitaGestione.setRicercaPuntualeCapitoloUGest(ricercaPuntualeCapitoloUGest);
		ricercaPuntualeCapitoloUscitaGestione.setDataOra(new Date());
					
		RicercaPuntualeCapitoloUscitaGestioneResponse ricercaPuntualeCapitoloUscitaGestioneResponse = executeExternalService(ricercaPuntualeCapitoloUscitaGestioneService,ricercaPuntualeCapitoloUscitaGestione);
		return ricercaPuntualeCapitoloUscitaGestioneResponse.getCapitoloUscitaGestione();
	}
	
	/**
	 * Ricerca movimenti.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the ricerca movimenti capitolo uscita gestione response
	 */
	private RicercaMovimentiCapitoloUscitaGestioneResponse ricercaMovimenti(CapitoloUscitaGestione capitoloUscitaGestione) {
		if (capitoloUscitaGestione == null){
			return new RicercaMovimentiCapitoloUscitaGestioneResponse();
		} 
		
		RicercaMovimentiCapitoloUscitaGestione ricercaMovimentiCapitoloUscitaGestione = new RicercaMovimentiCapitoloUscitaGestione();
		ricercaMovimentiCapitoloUscitaGestione.setEnte(req.getEnte());
		ricercaMovimentiCapitoloUscitaGestione.setRichiedente(req.getRichiedente());
		ricercaMovimentiCapitoloUscitaGestione.setBilancio(req.getBilancio());
		ricercaMovimentiCapitoloUscitaGestione.setCapitoloUscitaGestione(capitoloUscitaGestione);
		ricercaMovimentiCapitoloUscitaGestione.setDataOra(new Date());
		
		return executeExternalService(ricercaMovimentiCapitoloUscitaGestioneService,ricercaMovimentiCapitoloUscitaGestione);
	}
}
