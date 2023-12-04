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
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornaCapitoloDiUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornamentoMassivoCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AnnullaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.EliminaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.InserisceCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioMassivaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioModulareCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaMovimentiCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaPuntualeCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaPuntualeMovimentiCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaSinteticaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaSinteticaMassivaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaVariazioniCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.VerificaAnnullabilitaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.VerificaEliminabilitaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisioneResponse;

/**
 * Implementazione del web service CapitoloUscitaPrevisioneService.
 *
 * @author Domenico Lisi
 */
@WebService(serviceName = "CapitoloUscitaPrevisioneService", portName = "CapitoloUscitaPrevisioneServicePort", 
				targetNamespace = BILSvcDictionary.NAMESPACE, 
				endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService")
public class CapitoloUscitaPrevisioneServiceImpl implements CapitoloUscitaPrevisioneService {

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
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#inserisceCapitoloDiUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public InserisceCapitoloDiUscitaPrevisioneResponse inserisceCapitoloDiUscitaPrevisione(InserisceCapitoloDiUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, InserisceCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#aggiornaCapitoloDiUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaCapitoloDiUscitaPrevisioneResponse aggiornaCapitoloDiUscitaPrevisione(AggiornaCapitoloDiUscitaPrevisione arg) {
		return appCtx.getBean("aggiornaCapitoloDiUscitaPrevisioneService", AggiornaCapitoloDiUscitaPrevisioneService.class).executeService(arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaDettaglioCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioCapitoloUscitaPrevisione(RicercaDettaglioCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaMovimentiCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaMovimentiCapitoloUscitaPrevisioneResponse ricercaMovimentiCapitoloUscitaPrevisione(RicercaMovimentiCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaMovimentiCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaPuntualeCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntualeCapitoloUscitaPrevisione(RicercaPuntualeCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaSinteticaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaSinteticaCapitoloUscitaPrevisione(RicercaSinteticaCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCapitoloUscitaPrevisioneService.class, arg);

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#annullaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AnnullaCapitoloUscitaPrevisioneResponse annullaCapitoloUscitaPrevisione(@WebParam AnnullaCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#eliminaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public EliminaCapitoloUscitaPrevisioneResponse eliminaCapitoloUscitaPrevisione(@WebParam EliminaCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, EliminaCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaPuntualeMovimentiCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse ricercaPuntualeMovimentiCapitoloUscitaPrevisione(
			@WebParam RicercaPuntualeMovimentiCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeMovimentiCapitoloUscitaPrevisioneService.class, arg);

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaVariazioniCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaVariazioniCapitoloUscitaPrevisioneResponse ricercaVariazioniCapitoloUscitaPrevisione(
			@WebParam RicercaVariazioniCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloUscitaPrevisioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#verificaAnnullabilitaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse verificaAnnullabilitaCapitoloUscitaPrevisione(
			@WebParam VerificaAnnullabilitaCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaCapitoloUscitaPrevisioneService.class, arg);

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#verificaEliminabilitaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaEliminabilitaCapitoloUscitaPrevisioneResponse verificaEliminabilitaCapitoloUscitaPrevisione(
			@WebParam VerificaEliminabilitaCapitoloUscitaPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaEliminabilitaCapitoloUscitaPrevisioneService.class, arg);

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#aggiornaMassivoCapitoloDiUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaMassivoCapitoloDiUscitaPrevisioneResponse aggiornaMassivoCapitoloDiUscitaPrevisione(
			@WebParam AggiornaMassivoCapitoloDiUscitaPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornamentoMassivoCapitoloUscitaPrevisioneService.class, parameters);

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaSinteticaMassivaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse ricercaSinteticaMassivaCapitoloUscitaPrevisione(
			@WebParam RicercaSinteticaMassivaCapitoloUscitaPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMassivaCapitoloUscitaPrevisioneService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService#ricercaDettaglioMassivaCapitoloUscitaPrevisione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ricercaDettaglioMassivaCapitoloUscitaPrevisione(
			@WebParam RicercaDettaglioMassivaCapitoloUscitaPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioMassivaCapitoloUscitaPrevisioneService.class, parameters);
	}

	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse ricercaDettaglioModulareCapitoloUscitaPrevisione(RicercaDettaglioModulareCapitoloUscitaPrevisione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioModulareCapitoloUscitaPrevisioneService.class, parameters);
	}

}
