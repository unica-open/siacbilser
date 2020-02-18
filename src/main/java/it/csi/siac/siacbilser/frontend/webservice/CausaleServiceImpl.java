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
import it.csi.siac.siacbilser.business.service.causale.AggiornaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.AnnullaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.EliminaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.InserisceCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaClassificatoriEPService;
import it.csi.siac.siacbilser.business.service.causale.RicercaDettaglioCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaDettaglioModulareCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaEventiPerTipoService;
import it.csi.siac.siacbilser.business.service.causale.RicercaMinimaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaSinteticaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaSinteticaModulareCausaleService;
import it.csi.siac.siacbilser.business.service.causale.ValidaCausaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEP;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEPResponse;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausaleResponse;

@WebService(serviceName = "CausaleService", portName = "CausaleServicePort", 
targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.CausaleService")
public class CausaleServiceImpl implements CausaleService {

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
	public InserisceCausaleResponse inserisceCausale(InserisceCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceCausaleService.class, parameters);
	}

	@Override
	public AggiornaCausaleResponse aggiornaCausale(AggiornaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCausaleService.class, parameters);
	}

	@Override
	public AnnullaCausaleResponse annullaCausale(AnnullaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaCausaleService.class, parameters);
	}

	@Override
	public RicercaSinteticaCausaleResponse ricercaSinteticaCausale(RicercaSinteticaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCausaleService.class, parameters);
	}

	@Override
	public RicercaDettaglioCausaleResponse ricercaDettaglioCausale(RicercaDettaglioCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCausaleService.class, parameters);
	}

	@Override
	public EliminaCausaleResponse eliminaCausale(EliminaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaCausaleService.class, parameters);
	}

	@Override
	public ValidaCausaleResponse validaCausale(ValidaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidaCausaleService.class, parameters);
	}

	@Override
	public RicercaEventiPerTipoResponse ricercaEventiPerTipo(RicercaEventiPerTipo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaEventiPerTipoService.class, parameters);
	}

	@Override
	public RicercaClassificatoriEPResponse ricercaClassificatoriEP(RicercaClassificatoriEP parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaClassificatoriEPService.class, parameters);
	}

	// Lotto O
	@Override
	public RicercaMinimaCausaleResponse ricercaMinimaCausale(RicercaMinimaCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaMinimaCausaleService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareCausaleResponse ricercaSinteticaModulareCausale(RicercaSinteticaModulareCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareCausaleService.class, parameters);
	}

	@Override
	public RicercaDettaglioModulareCausaleResponse ricercaDettaglioModulareCausale(RicercaDettaglioModulareCausale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioModulareCausaleService.class, parameters);
	}

}
