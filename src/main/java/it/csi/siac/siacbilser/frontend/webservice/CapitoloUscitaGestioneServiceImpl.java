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
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.AggiornaCapitoloDiUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.AggiornamentoMassivoCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.AnnullaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.EliminaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.InserisceCapitoloDiUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDettaglioCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDettaglioMassivaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDettaglioModulareCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDisponibilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDocumentiCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaImpegniCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaMovimentiCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaPuntualeCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaSinteticaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaSinteticaMassivaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaVariazioniCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaAnnullabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaEliminabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;

/**
 * Implementazione del servizio CapitoloUscitaGestioneService.
 *
 * @author VM
 * @version $Id: $
 */
@WebService(serviceName = "CapitoloUscitaGestioneService", portName = "CapitoloUscitaGestioneServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService")
public class CapitoloUscitaGestioneServiceImpl implements CapitoloUscitaGestioneService {

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
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#inserisceCapitoloDiUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public InserisceCapitoloDiUscitaGestioneResponse inserisceCapitoloDiUscitaGestione(
			InserisceCapitoloDiUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, InserisceCapitoloDiUscitaGestioneService.class, arg);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaDettaglioCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestione(
			RicercaDettaglioCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaMovimentiCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaMovimentiCapitoloUscitaGestioneResponse ricercaMovimentiCapitoloUscitaGestione(
			RicercaMovimentiCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaMovimentiCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaPuntualeCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaPuntualeCapitoloUscitaGestioneResponse ricercaPuntualeCapitoloUscitaGestione(
			RicercaPuntualeCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaSinteticaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaCapitoloUscitaGestioneResponse ricercaSinteticaCapitoloUscitaGestione(
			RicercaSinteticaCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaDocumentiCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDocumentiCapitoloUscitaGestioneResponse ricercaDocumentiCapitoloUscitaGestione(
			RicercaDocumentiCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaDocumentiCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaImpegniCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaImpegniCapitoloUscitaGestioneResponse ricercaImpegniCapitoloUscitaGestione(
			RicercaImpegniCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaImpegniCapitoloUscitaGestioneService.class, arg);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#aggiornaCapitoloDiUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaCapitoloDiUscitaGestioneResponse aggiornaCapitoloDiUscitaGestione(
			@WebParam AggiornaCapitoloDiUscitaGestione arg) {
		return appCtx.getBean("aggiornaCapitoloDiUscitaGestioneService",AggiornaCapitoloDiUscitaGestioneService.class).executeService(arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#verificaAnnullabilitaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaAnnullabilitaCapitoloUscitaGestioneResponse verificaAnnullabilitaCapitoloUscitaGestione(
			@WebParam VerificaAnnullabilitaCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#annullaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AnnullaCapitoloUscitaGestioneResponse annullaCapitoloUscitaGestione(
			@WebParam AnnullaCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#verificaEliminabilitaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public VerificaEliminabilitaCapitoloUscitaGestioneResponse verificaEliminabilitaCapitoloUscitaGestione(
			@WebParam VerificaEliminabilitaCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, VerificaEliminabilitaCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#eliminaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public EliminaCapitoloUscitaGestioneResponse eliminaCapitoloUscitaGestione(
			@WebParam EliminaCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, EliminaCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaVariazioniCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaVariazioniCapitoloUscitaGestioneResponse ricercaVariazioniCapitoloUscitaGestione(
			@WebParam RicercaVariazioniCapitoloUscitaGestione arg) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloUscitaGestioneService.class, arg);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#aggiornaMassivoCapitoloDiUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public AggiornaMassivoCapitoloDiUscitaGestioneResponse aggiornaMassivoCapitoloDiUscitaGestione(
			@WebParam AggiornaMassivoCapitoloDiUscitaGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornamentoMassivoCapitoloUscitaGestioneService.class, parameters);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaSinteticaMassivaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaSinteticaMassivaCapitoloUscitaGestioneResponse ricercaSinteticaMassivaCapitoloUscitaGestione(
			@WebParam RicercaSinteticaMassivaCapitoloUscitaGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMassivaCapitoloUscitaGestioneService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService#ricercaDettaglioMassivaCapitoloUscitaGestione(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioMassivaCapitoloUscitaGestioneResponse ricercaDettaglioMassivaCapitoloUscitaGestione(
			@WebParam RicercaDettaglioMassivaCapitoloUscitaGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioMassivaCapitoloUscitaGestioneService.class, parameters);
	}

	@Override
	@WebMethod
	@WebResult
	public RicercaDettaglioModulareCapitoloUscitaGestioneResponse ricercaDettaglioModulareCapitoloUscitaGestione(
			RicercaDettaglioModulareCapitoloUscitaGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioModulareCapitoloUscitaGestioneService.class, parameters);
	}

	@Override
	public RicercaDisponibilitaCapitoloUscitaGestioneResponse ricercaDisponibilitaCapitoloUscitaGestione(RicercaDisponibilitaCapitoloUscitaGestione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDisponibilitaCapitoloUscitaGestioneService.class, parameters);
	}

}
