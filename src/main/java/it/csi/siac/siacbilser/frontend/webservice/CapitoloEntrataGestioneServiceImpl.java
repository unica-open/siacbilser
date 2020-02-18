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
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.AggiornaCapitoloDiEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.AggiornamentoMassivoCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.AnnullaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.EliminaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.InserisceCapitoloDiEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaAccertamentiCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaDettaglioCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaDettaglioMassivaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaDisponibilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaDocumentiCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaMovimentiCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaPuntualeCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaMassivaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaVariazioniCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaAnnullabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaEliminabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioModulareCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAccertamentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAccertamentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestioneResponse;

/**
 * Implementazione del servizio CapitoloEntrataGestioneService.
 *
 * @author VM
 * @version $Id: $
 */
@WebService(serviceName = "CapitoloEntrataGestioneService", portName = "CapitoloEntrataGestioneServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService")
public class CapitoloEntrataGestioneServiceImpl implements CapitoloEntrataGestioneService {

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
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#inserisceCapitoloDiEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public InserisceCapitoloDiEntrataGestioneResponse inserisceCapitoloDiEntrataGestione(
			InserisceCapitoloDiEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, InserisceCapitoloDiEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaDettaglioCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioCapitoloEntrataGestione(
			RicercaDettaglioCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaMovimentiCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaMovimentiCapitoloEntrataGestioneResponse ricercaMovimentiCapitoloEntrataGestione(
			RicercaMovimentiCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaMovimentiCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaPuntualeCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntualeCapitoloEntrataGestione(
			RicercaPuntualeCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaSinteticaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaCapitoloEntrataGestioneResponse ricercaSinteticaCapitoloEntrataGestione(
			RicercaSinteticaCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaAccertamentiCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAccertamentiCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaAccertamentiCapitoloEntrataGestioneResponse ricercaAccertamentiCapitoloEntrataGestione(
			RicercaAccertamentiCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaAccertamentiCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaDocumentiCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDocumentiCapitoloEntrataGestioneResponse ricercaDocumentiCapitoloEntrataGestione(
			RicercaDocumentiCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDocumentiCapitoloEntrataGestioneService.class, arg);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#aggiornaCapitoloDiEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaCapitoloDiEntrataGestioneResponse aggiornaCapitoloDiEntrataGestione(
			@WebParam AggiornaCapitoloDiEntrataGestione arg) {
		return appCtx.getBean("aggiornaCapitoloDiEntrataGestioneService",AggiornaCapitoloDiEntrataGestioneService.class).executeService(arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#verificaAnnullabilitaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaAnnullabilitaCapitoloEntrataGestioneResponse verificaAnnullabilitaCapitoloEntrataGestione(
			@WebParam VerificaAnnullabilitaCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#annullaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AnnullaCapitoloEntrataGestioneResponse annullaCapitoloEntrataGestione(
			@WebParam AnnullaCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#verificaEliminabilitaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaEliminabilitaCapitoloEntrataGestioneResponse verificaEliminabilitaCapitoloEntrataGestione(
			@WebParam VerificaEliminabilitaCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaEliminabilitaCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#eliminaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public EliminaCapitoloEntrataGestioneResponse eliminaCapitoloEntrataGestione(
			@WebParam EliminaCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, EliminaCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaVariazioniCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaVariazioniCapitoloEntrataGestioneResponse ricercaVariazioniCapitoloEntrataGestione(
			@WebParam RicercaVariazioniCapitoloEntrataGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloEntrataGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#aggiornaMassivoCapitoloDiEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaMassivoCapitoloDiEntrataGestioneResponse aggiornaMassivoCapitoloDiEntrataGestione(
			@WebParam AggiornaMassivoCapitoloDiEntrataGestione parameters) {
		
		return BaseServiceExecutor.execute(appCtx, AggiornamentoMassivoCapitoloEntrataGestioneService.class, parameters);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaSinteticaMassivaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaMassivaCapitoloEntrataGestioneResponse ricercaSinteticaMassivaCapitoloEntrataGestione(
			@WebParam RicercaSinteticaMassivaCapitoloEntrataGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMassivaCapitoloEntrataGestioneService.class, parameters);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService#ricercaDettaglioMassivaCapitoloEntrataGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioMassivaCapitoloEntrataGestioneResponse ricercaDettaglioMassivaCapitoloEntrataGestione(
			@WebParam RicercaDettaglioMassivaCapitoloEntrataGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioMassivaCapitoloEntrataGestioneService.class, parameters);
	}

	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioModulareCapitoloEntrataGestioneResponse ricercaDettaglioModulareCapitoloEntrataGestione(RicercaDettaglioModulareCapitoloEntrataGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioModulareCapitoloEntrataGestioneService.class, parameters);
	}

	@Override
	public RicercaDisponibilitaCapitoloEntrataGestioneResponse ricercaDisponibilitaCapitoloEntrataGestione(RicercaDisponibilitaCapitoloEntrataGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDisponibilitaCapitoloEntrataGestioneService.class, parameters);
	}

}
