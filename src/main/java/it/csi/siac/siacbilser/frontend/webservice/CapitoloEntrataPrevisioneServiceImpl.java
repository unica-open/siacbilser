/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.AggiornaCapitoloDiEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.AggiornamentoMassivoCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.AnnullaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.EliminaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.InserisceCapitoloDiEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioMassivaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioModulareCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaMovimentiCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaPuntualeCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaPuntualeMovimentiCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaSinteticaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaSinteticaMassivaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaVariazioniCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.VerificaAnnullabilitaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.VerificaEliminabilitaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisioneResponse;

/**
 * Implementazione del servizio CapitoloEntrataPrevisioneService.
 *
 * @author VM
 * @version $Id: $
 */
@WebService(serviceName = "CapitoloEntrataPrevisioneService", portName = "CapitoloEntrataPrevisioneServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService")
public class CapitoloEntrataPrevisioneServiceImpl implements CapitoloEntrataPrevisioneService {

	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#inserisceCapitoloDiEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public InserisceCapitoloDiEntrataPrevisioneResponse inserisceCapitoloDiEntrataPrevisione(
			InserisceCapitoloDiEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, InserisceCapitoloDiEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#aggiornaCapitoloDiEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaCapitoloDiEntrataPrevisioneResponse aggiornaCapitoloDiEntrataPrevisione(
			AggiornaCapitoloDiEntrataPrevisione arg) {
		return appCtx.getBean("aggiornaCapitoloDiEntrataPrevisioneService",AggiornaCapitoloDiEntrataPrevisioneService.class).executeService(arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaDettaglioCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioCapitoloEntrataPrevisioneResponse ricercaDettaglioCapitoloEntrataPrevisione(
			RicercaDettaglioCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaMovimentiCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaMovimentiCapitoloEntrataPrevisioneResponse ricercaMovimentiCapitoloEntrataPrevisione(
			RicercaMovimentiCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaMovimentiCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaPuntualeCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeCapitoloEntrataPrevisioneResponse ricercaPuntualeCapitoloEntrataPrevisione(
			RicercaPuntualeCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaSinteticaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaSinteticaCapitoloEntrataPrevisione(
			RicercaSinteticaCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCapitoloEntrataPrevisioneService.class, arg);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#annullaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AnnullaCapitoloEntrataPrevisioneResponse annullaCapitoloEntrataPrevisione(
			@WebParam AnnullaCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#eliminaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public EliminaCapitoloEntrataPrevisioneResponse eliminaCapitoloEntrataPrevisione(
			@WebParam EliminaCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, EliminaCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaPuntualeMovimentiCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse ricercaPuntualeMovimentiCapitoloEntrataPrevisione(
			@WebParam RicercaPuntualeMovimentiCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeMovimentiCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#verificaAnnullabilitaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse verificaAnnullabilitaCapitoloEntrataPrevisione(
			@WebParam VerificaAnnullabilitaCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#verificaEliminabilitaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaEliminabilitaCapitoloEntrataPrevisioneResponse verificaEliminabilitaCapitoloEntrataPrevisione(
			@WebParam VerificaEliminabilitaCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaEliminabilitaCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaVariazioniCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaVariazioniCapitoloEntrataPrevisioneResponse ricercaVariazioniCapitoloEntrataPrevisione(
			@WebParam RicercaVariazioniCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloEntrataPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#aggiornaMassivoCapitoloDiEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaMassivoCapitoloDiEntrataPrevisioneResponse aggiornaMassivoCapitoloDiEntrataPrevisione(
			@WebParam AggiornaMassivoCapitoloDiEntrataPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornamentoMassivoCapitoloEntrataPrevisioneService.class, parameters);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaSinteticaMassivaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse ricercaSinteticaMassivaCapitoloEntrataPrevisione(
			@WebParam RicercaSinteticaMassivaCapitoloEntrataPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMassivaCapitoloEntrataPrevisioneService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaDettaglioMassivaCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse ricercaDettaglioMassivaCapitoloEntrataPrevisione(
			@WebParam RicercaDettaglioMassivaCapitoloEntrataPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioMassivaCapitoloEntrataPrevisioneService.class, parameters);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService#ricercaDettaglioCapitoloEntrataPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse ricercaDettaglioModulareCapitoloEntrataPrevisione(RicercaDettaglioModulareCapitoloEntrataPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioModulareCapitoloEntrataPrevisioneService.class, arg);
	}

}
