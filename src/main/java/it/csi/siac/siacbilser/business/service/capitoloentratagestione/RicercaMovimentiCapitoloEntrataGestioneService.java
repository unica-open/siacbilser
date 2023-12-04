/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAccertamentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAccertamentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.DocumentoDiEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaMovimentiCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentiCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<RicercaMovimentiCapitoloEntrataGestione, RicercaMovimentiCapitoloEntrataGestioneResponse> {
	
	/** The ricerca variazioni capitolo entrata gestione service. */
	@Autowired
	private RicercaVariazioniCapitoloEntrataGestioneService ricercaVariazioniCapitoloEntrataGestioneService;
	
	/** The ricerca vincolo service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloService;
	
	/** The ricerca accertamenti capitolo entrata gestione service. */
	@Autowired
	private RicercaAccertamentiCapitoloEntrataGestioneService ricercaAccertamentiCapitoloEntrataGestioneService;
	
	/** The ricerca documenti capitolo entrata gestione service. */
	@Autowired
	private RicercaDocumentiCapitoloEntrataGestioneService ricercaDocumentiCapitoloEntrataGestioneService;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"),true);
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"),false);
		checkNotNull(req.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataGestione"));
		checkCondition(req.getCapitoloEntrataGestione().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaMovimentiCapitoloEntrataGestioneResponse executeService(RicercaMovimentiCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// RICERCA VARIAZIONI CAPITOLO
		RicercaVariazioniCapitoloEntrataGestioneResponse variazioni = ricercaVariazioni();
		List<VariazioneImportoCapitolo>  listaVariazioneImporti   = variazioni.getListaVariazioniImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodifiche = variazioni.getListaVariazioniCodifiche();
				
		// RICERCA VINCOLI CAPITOLO
		RicercaVincoloResponse vincoli = ricercaVincoli();
		List<VincoloCapitoli>  listaVincoli = vincoli.getVincoloCapitoli();//.getListaVincoliCapitoloUEGest();
		
		// RICERCA IMPEGNI
		RicercaAccertamentiCapitoloEntrataGestioneResponse accertamenti = ricercaAccertamenti();
		List<Accertamento> listaAccertamenti = accertamenti.getListaAccertamento(); 
		
		// RICERCA DOCUMENTI
		RicercaDocumentiCapitoloEntrataGestioneResponse documenti = ricercaDocumenti();
		List<DocumentoDiEntrata> listaDocumentiEntrata = documenti.getListaDocumentiDiEntrata();
		
		// RISPOSTA
		res.setListaVariazioneImporti(listaVariazioneImporti);
		res.setListaVariazioneCodifiche(listaVariazioneCodifiche);
		res.setListaVincoli(listaVincoli);
		res.setListaAccertamenti(listaAccertamenti);
		res.setDocumentiEntrata(listaDocumentiEntrata);
	}
	
	/**
	 * Ricerca variazioni.
	 *
	 * @return the ricerca variazioni capitolo entrata gestione response
	 */
	private RicercaVariazioniCapitoloEntrataGestioneResponse ricercaVariazioni() {
		CapitoloEntrataGestione capitoloEntrataGestione = req.getCapitoloEntrataGestione();
		capitoloEntrataGestione.setEnte(req.getEnte());		
		capitoloEntrataGestione.setBilancio(req.getBilancio());		
		
		RicercaVariazioniCapitoloEntrataGestione ricercaVariazioni = new RicercaVariazioniCapitoloEntrataGestione();
		ricercaVariazioni.setRichiedente(req.getRichiedente());
		ricercaVariazioni.setCapitoloEntrataGest(capitoloEntrataGestione);
		ricercaVariazioni.setCapitoloEntrataGest(req.getCapitoloEntrataGestione());
		ricercaVariazioni.setDataOra(new Date());
		
		return executeExternalService(ricercaVariazioniCapitoloEntrataGestioneService,ricercaVariazioni);
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
		rv.setCapitolo(req.getCapitoloEntrataGestione());
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getEnte());
		rv.setVincolo(vincolo);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(100);
		pp.setNumeroPagina(0);
		rv.setParametriPaginazione(pp);
		
		return executeExternalService(ricercaVincoloService,rv);
	}
	
	/**
	 * Ricerca accertamenti.
	 *
	 * @return the ricerca accertamenti capitolo entrata gestione response
	 */
	private RicercaAccertamentiCapitoloEntrataGestioneResponse ricercaAccertamenti() {
		RicercaAccertamentiCapitoloEntrataGestione ricercaAccertamentiCapitoloEntrataGestione = new RicercaAccertamentiCapitoloEntrataGestione ();
		ricercaAccertamentiCapitoloEntrataGestione.setRichiedente(req.getRichiedente());
		ricercaAccertamentiCapitoloEntrataGestione.setEnte(req.getEnte());
		ricercaAccertamentiCapitoloEntrataGestione.setBilancio(req.getBilancio());
		ricercaAccertamentiCapitoloEntrataGestione.setCapitoloEGest(req.getCapitoloEntrataGestione());
		ricercaAccertamentiCapitoloEntrataGestione.setDataOra(new Date());
		
		return executeExternalService(ricercaAccertamentiCapitoloEntrataGestioneService,ricercaAccertamentiCapitoloEntrataGestione);
	}
	
	/**
	 * Ricerca documenti.
	 *
	 * @return the ricerca documenti capitolo entrata gestione response
	 */
	private RicercaDocumentiCapitoloEntrataGestioneResponse ricercaDocumenti() {
		RicercaDocumentiCapitoloEntrataGestione ricercaDocumentiCapitoloEntrataGestione = new RicercaDocumentiCapitoloEntrataGestione();
		ricercaDocumentiCapitoloEntrataGestione.setRichiedente(req.getRichiedente());
		ricercaDocumentiCapitoloEntrataGestione.setEnte(req.getEnte());
		ricercaDocumentiCapitoloEntrataGestione.setBilancio(req.getBilancio());
		ricercaDocumentiCapitoloEntrataGestione.setCapitoloEntrataGestione(req.getCapitoloEntrataGestione());
		ricercaDocumentiCapitoloEntrataGestione.setDataOra(new Date());		
		
		return executeExternalService(ricercaDocumentiCapitoloEntrataGestioneService,ricercaDocumentiCapitoloEntrataGestione);
	}	
}