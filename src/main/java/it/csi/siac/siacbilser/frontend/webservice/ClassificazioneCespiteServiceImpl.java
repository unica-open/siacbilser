/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AggiornaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AggiornaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AnnullaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AnnullaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.EliminaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.EliminaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.InserisciCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.InserisciTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaSinteticaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaSinteticaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.VerificaAnnullabilitaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.VerificaAnnullabilitaTipoBeneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.CESPSvcDictionary;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespiteResponse;

@WebService(serviceName = "ClassificazioneCespiteService", portName = "ClassificazioneCespiteServicePort",
targetNamespace = CESPSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService")
public class ClassificazioneCespiteServiceImpl implements ClassificazioneCespiteService {

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

	@Override
	public InserisciCategoriaCespitiResponse inserisciCategoriaCespiti(InserisciCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciCategoriaCespitiService.class, parameters);
	}

	@Override
	public AggiornaCategoriaCespitiResponse aggiornaCategoriaCespiti(AggiornaCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCategoriaCespitiService.class, parameters);
	}

	@Override
	public RicercaDettaglioCategoriaCespitiResponse ricercaDettaglioCategoriaCespiti(RicercaDettaglioCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCategoriaCespitiService.class, parameters);
	}

	@Override
	public RicercaSinteticaCategoriaCespitiResponse ricercaSinteticaCategoriaCespiti(RicercaSinteticaCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCategoriaCespitiService.class, parameters);
	}

	@Override
	public EliminaCategoriaCespitiResponse eliminaCategoriaCespiti(EliminaCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaCategoriaCespitiService.class, parameters);
	}
	
	@Override
	public AnnullaCategoriaCespitiResponse annullaCategoriaCespiti(AnnullaCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCategoriaCespitiService.class, parameters);
	}
	
	@Override
	public VerificaAnnullabilitaCategoriaCespitiResponse verificaAnnullabilitaCategoriaCespiti(VerificaAnnullabilitaCategoriaCespiti parameters) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaCategoriaCespitiService.class, parameters);
	}

	@Override
	public InserisciTipoBeneCespiteResponse inserisciTipoBeneCespite(InserisciTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciTipoBeneCespiteService.class, parameters);
	}

	@Override
	public AggiornaTipoBeneCespiteResponse aggiornaTipoBeneCespite(AggiornaTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoBeneCespiteService.class, parameters);
	}

	@Override
	public RicercaDettaglioTipoBeneCespiteResponse ricercaDettaglioTipoBeneCespite(RicercaDettaglioTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoBeneCespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaTipoBeneCespiteResponse ricercaSinteticaTipoBeneCespite(RicercaSinteticaTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoBeneCespiteService.class, parameters);
	}

	@Override
	public EliminaTipoBeneCespiteResponse eliminaTipoBeneCespite(EliminaTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaTipoBeneCespiteService.class, parameters);
	}

	@Override
	public AnnullaTipoBeneCespiteResponse annullaTipoBeneCespite(AnnullaTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaTipoBeneCespiteService.class, parameters);
	}

	@Override
	public VerificaAnnullabilitaTipoBeneCespiteResponse verificaAnnullabilitaTipoBeneCespite(VerificaAnnullabilitaTipoBeneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaTipoBeneCespiteService.class, parameters);
	}

}



