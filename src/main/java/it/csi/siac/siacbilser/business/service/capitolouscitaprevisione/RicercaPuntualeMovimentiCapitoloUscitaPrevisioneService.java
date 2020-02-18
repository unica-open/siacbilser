/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeMovimentiCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeMovimentiCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaPuntualeMovimentiCapitoloUscitaPrevisione, RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The ricerca variazioni capitolo uscita previsione service. */
	@Autowired
	private RicercaVariazioniCapitoloUscitaPrevisioneService ricercaVariazioniCapitoloUscitaPrevisioneService;
	
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
		checkNotNull(req.getCapitoloUscitaPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));	
		checkCondition(req.getCapitoloUscitaPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione anno capitolo"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloUscitaPrevisioneDad.setBilancio(req.getBilancio());
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse executeService(RicercaPuntualeMovimentiCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		popolaVariazioniCapitoloUscitaPrevisione();
		popolaVincoloCapitoloPrevisione();
	}

	/**
	 * Popola variazioni capitolo uscita previsione.
	 */
	private void popolaVariazioniCapitoloUscitaPrevisione() {
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = req.getCapitoloUscitaPrev();
		capitoloUscitaPrevisione.setEnte(req.getEnte());
		capitoloUscitaPrevisione.setBilancio(req.getBilancio());		
		
		RicercaVariazioniCapitoloUscitaPrevisione ricercaVariazioniCapitoloUscitaPrevisione = new RicercaVariazioniCapitoloUscitaPrevisione();
		ricercaVariazioniCapitoloUscitaPrevisione.setRichiedente(req.getRichiedente());
		ricercaVariazioniCapitoloUscitaPrevisione.setCapitoloUscitaPrev(capitoloUscitaPrevisione);
		ricercaVariazioniCapitoloUscitaPrevisione.setDataOra(new Date());
		
		RicercaVariazioniCapitoloUscitaPrevisioneResponse ricercaVariazioniCapitoloUscitaPrevisioneResponse = executeExternalService(ricercaVariazioniCapitoloUscitaPrevisioneService,ricercaVariazioniCapitoloUscitaPrevisione);
		res.setListaVariazioneImporti(ricercaVariazioniCapitoloUscitaPrevisioneResponse.getListaVariazioniImporti());
		res.setListaVariazioneCodifiche(ricercaVariazioniCapitoloUscitaPrevisioneResponse.getListaVariazioniCodifiche());
	}
	
	/**
	 * Popola vincolo capitolo previsione.
	 */
	private void popolaVincoloCapitoloPrevisione() {
		
		RicercaVincolo rv = new RicercaVincolo();
		rv.setRichiedente(req.getRichiedente());
		rv.setDataOra(new Date());
		rv.setCapitolo(req.getCapitoloUscitaPrev());
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getEnte());
		rv.setVincolo(vincolo);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(100);
		pp.setNumeroPagina(0);
		rv.setParametriPaginazione(pp);
		
		RicercaVincoloResponse ricercaVincoloCapitoloPrevisioneResponse = executeExternalService(ricercaVincoloCapitoloPrevisioneService,rv);
		res.setListaVincoli(ricercaVincoloCapitoloPrevisioneResponse.getVincoloCapitoli()/*.getListaVincoliCapitoloUEPrev()*/);
		
		
//		RicercaVincoloCapitoloPrev ricercaVincoloCapitoloPrev = new RicercaVincoloCapitoloPrev();
//		ricercaVincoloCapitoloPrev.setAnnoEsercizio(req.getCapitoloUscitaPrev().getAnnoCapitolo());
//		ricercaVincoloCapitoloPrev.setTipoVincolo("P");
		
//		RicercaVincolo ricercaVincoloCapitoloPrevisione = new RicercaVincolo();
////		ricercaVincoloCapitoloPrevisione.setEnte(req.getEnte());
//		ricercaVincoloCapitoloPrevisione.setRichiedente(req.getRichiedente());
////		ricercaVincoloCapitoloPrevisione.setRicercaVincoloCapitoloPrev(ricercaVincoloCapitoloPrev);
//		ricercaVincoloCapitoloPrevisione.setDataOra(new Date());
//		
//		RicercaVincoloResponse ricercaVincoloCapitoloPrevisioneResponse = executeExternalService(ricercaVincoloCapitoloPrevisioneService,ricercaVincoloCapitoloPrevisione);
//		res.setListaVincoli(ricercaVincoloCapitoloPrevisioneResponse.getVincoloCapitoli()/*.getListaVincoliCapitoloUEPrev()*/);
//		
	}
}
