/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.DocumentoDiSpesa;
import it.csi.siac.siacfinser.model.Impegno;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaMovimentiCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentiCapitoloUscitaGestioneService 
	extends CheckedAccountBaseService<RicercaMovimentiCapitoloUscitaGestione, RicercaMovimentiCapitoloUscitaGestioneResponse> {
	
	/** The ricerca variazioni capitolo uscita gestione service. */
	@Autowired
	private RicercaVariazioniCapitoloUscitaGestioneService ricercaVariazioniCapitoloUscitaGestioneService;
	
	/** The ricerca vincolo capitolo gestione service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloCapitoloGestioneService;
	
	/** The ricerca impegni capitolo uscita gestione service. */
	@Autowired
	private RicercaImpegniCapitoloUscitaGestioneService ricercaImpegniCapitoloUscitaGestioneService;
	
	/** The ricerca documenti capitolo uscita gestione service. */
	@Autowired
	private RicercaDocumentiCapitoloUscitaGestioneService ricercaDocumentiCapitoloUscitaGestioneService;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), true);
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(req.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaGestione"));
		checkCondition(req.getCapitoloUscitaGestione().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaMovimentiCapitoloUscitaGestioneResponse executeService(RicercaMovimentiCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// RICERCA VARIAZIONI CAPITOLO
		RicercaVariazioniCapitoloUscitaGestioneResponse variazioni = ricercaVariazioni();
		List<VariazioneImportoCapitolo>  listaVariazioneImporti   = variazioni.getListaVariazioniImporti();
		List<VariazioneCodificaCapitolo> listaVariazioneCodifiche = variazioni.getListaVariazioniCodifiche();
				
		// RICERCA VINCOLI CAPITOLO
		RicercaVincoloResponse vincoli = ricercaVincoli();
		List<VincoloCapitoli>  listaVincoli = vincoli.getVincoloCapitoli();//.getListaVincoliCapitoloUEGest();
		
		// RICERCA IMPEGNI
		RicercaImpegniCapitoloUscitaGestioneResponse impegni = ricercaImpegni();
		List<Impegno> listaImpegni = impegni.getListaImpegni(); 
		
		// RICERCA DOCUMENTI
		RicercaDocumentiCapitoloUscitaGestioneResponse documenti = ricercaDocumenti();
		List<DocumentoDiSpesa> listaDocumentiSpesa = documenti.getListaDocumenti();
		
		// RISPOSTA
		res.setListaVariazioneImporti(listaVariazioneImporti);
		res.setListaVariazioneCodifiche(listaVariazioneCodifiche);
		res.setListaVincoli(listaVincoli);
		res.setListaImpegni(listaImpegni);
		res.setListaDocumentoSpesa(listaDocumentiSpesa);
	}
	
	/**
	 * Ricerca variazioni.
	 *
	 * @return the ricerca variazioni capitolo uscita gestione response
	 */
	private RicercaVariazioniCapitoloUscitaGestioneResponse ricercaVariazioni() {
		CapitoloUscitaGestione capitoloUscitaGestione = req.getCapitoloUscitaGestione();
		capitoloUscitaGestione.setEnte(req.getEnte());		
		capitoloUscitaGestione.setBilancio(req.getBilancio());
		
		RicercaVariazioniCapitoloUscitaGestione ricercaVariazioni = new RicercaVariazioniCapitoloUscitaGestione();
		ricercaVariazioni.setRichiedente(req.getRichiedente());
		ricercaVariazioni.setCapitoloUscitaGest(capitoloUscitaGestione);
		ricercaVariazioni.setDataOra(new Date());
		
		return executeExternalService(ricercaVariazioniCapitoloUscitaGestioneService,ricercaVariazioni);
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
		rv.setCapitolo(req.getCapitoloUscitaGestione());
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getEnte());
		rv.setVincolo(vincolo);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(100);
		pp.setNumeroPagina(0);
		rv.setParametriPaginazione(pp);
		
		return executeExternalService(ricercaVincoloCapitoloGestioneService,rv);
		
	}
	
	/**
	 * Ricerca impegni.
	 *
	 * @return the ricerca impegni capitolo uscita gestione response
	 */
	private RicercaImpegniCapitoloUscitaGestioneResponse ricercaImpegni() {
		RicercaImpegniCapitoloUscitaGestione ricercaImpegniCapitoloUscitaGestione = new RicercaImpegniCapitoloUscitaGestione();
		ricercaImpegniCapitoloUscitaGestione.setRichiedente(req.getRichiedente());
		ricercaImpegniCapitoloUscitaGestione.setEnte(req.getEnte());
		ricercaImpegniCapitoloUscitaGestione.setBilancio(req.getBilancio());
		ricercaImpegniCapitoloUscitaGestione.setCapitoloUscitaGestione(req.getCapitoloUscitaGestione());
		ricercaImpegniCapitoloUscitaGestione.setDataOra(new Date());
		
		return executeExternalService(ricercaImpegniCapitoloUscitaGestioneService,ricercaImpegniCapitoloUscitaGestione);
	}
	
	/**
	 * Ricerca documenti.
	 *
	 * @return the ricerca documenti capitolo uscita gestione response
	 */
	private RicercaDocumentiCapitoloUscitaGestioneResponse ricercaDocumenti() {
		RicercaDocumentiCapitoloUscitaGestione ricercaDocumentiCapitoloUscitaGestione = new RicercaDocumentiCapitoloUscitaGestione();
		ricercaDocumentiCapitoloUscitaGestione.setRichiedente(req.getRichiedente());
		ricercaDocumentiCapitoloUscitaGestione.setEnte(req.getEnte());
		ricercaDocumentiCapitoloUscitaGestione.setBilancio(req.getBilancio());
		ricercaDocumentiCapitoloUscitaGestione.setCapitoloUscitaGestione(req.getCapitoloUscitaGestione());
		ricercaDocumentiCapitoloUscitaGestione.setDataOra(new Date());		
		
		return executeExternalService(ricercaDocumentiCapitoloUscitaGestioneService,ricercaDocumentiCapitoloUscitaGestione);
	}	
}