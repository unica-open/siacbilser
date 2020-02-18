/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeMovimentiCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeMovimentiCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<RicercaPuntualeMovimentiCapitoloEntrataPrevisione, RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The ricerca variazioni capitolo entrata previsione service. */
	@Autowired
	private RicercaVariazioniCapitoloEntrataPrevisioneService ricercaVariazioniCapitoloEntrataPrevisioneService;
	
	/** The ricerca vincolo capitolo previsione service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloCapitoloPrevisioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(req.getCapitoloEntrataPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione"), true);
		checkCondition(req.getCapitoloEntrataPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione anno capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataPrevisioneDad.setBilancio(req.getBilancio());
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse executeService(RicercaPuntualeMovimentiCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		popolaVariazioniCapitoloEntrataPrevisione();
		popolaVincoloCapitoloPrevisione();
	}

	/**
	 * Popola variazioni capitolo entrata previsione.
	 */
	private void popolaVariazioniCapitoloEntrataPrevisione() {
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = req.getCapitoloEntrataPrev();
		capitoloEntrataPrevisione.setEnte(req.getEnte());
		capitoloEntrataPrevisione.setBilancio(req.getBilancio());		
		
		RicercaVariazioniCapitoloEntrataPrevisione ricercaVariazioniCapitoloEntrataPrevisione = new RicercaVariazioniCapitoloEntrataPrevisione();
		ricercaVariazioniCapitoloEntrataPrevisione.setRichiedente(req.getRichiedente());
		ricercaVariazioniCapitoloEntrataPrevisione.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		ricercaVariazioniCapitoloEntrataPrevisione.setDataOra(new Date());
		
		RicercaVariazioniCapitoloEntrataPrevisioneResponse ricercaVariazioniCapitoloEntrataPrevisioneResponse = executeExternalService(ricercaVariazioniCapitoloEntrataPrevisioneService,ricercaVariazioniCapitoloEntrataPrevisione);
		res.setListaVariazioneImporti(ricercaVariazioniCapitoloEntrataPrevisioneResponse.getListaVariazioniImporti());
		res.setListaVariazioneCodifiche(ricercaVariazioniCapitoloEntrataPrevisioneResponse.getListaVariazioniCodifiche());
	}
	
	/**
	 * Popola vincolo capitolo previsione.
	 */
	private void popolaVincoloCapitoloPrevisione() {
		
		
		RicercaVincolo rv = new RicercaVincolo();
		rv.setRichiedente(req.getRichiedente());
		rv.setDataOra(new Date());
		rv.setCapitolo(req.getCapitoloEntrataPrev());
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getEnte());
		rv.setVincolo(vincolo);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(100);
		pp.setNumeroPagina(0);
		rv.setParametriPaginazione(pp);
		
		RicercaVincoloResponse ricercaVincoloCapitoloPrevisioneResponse = executeExternalService(ricercaVincoloCapitoloPrevisioneService,rv);
		res.setListaVincoli(ricercaVincoloCapitoloPrevisioneResponse.getVincoloCapitoli()/*.getListaVincoliCapitoloUEPrev()*/);
		
	}
}