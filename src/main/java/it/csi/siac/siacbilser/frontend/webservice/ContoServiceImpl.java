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
import it.csi.siac.siacbilser.business.service.conto.AggiornaContoService;
import it.csi.siac.siacbilser.business.service.conto.AnnullaContoService;
import it.csi.siac.siacbilser.business.service.conto.EliminaContoService;
import it.csi.siac.siacbilser.business.service.conto.InserisceContoService;
import it.csi.siac.siacbilser.business.service.conto.LeggiTreeCodiceBilancioService;
import it.csi.siac.siacbilser.business.service.conto.RicercaClassiPianoAmmortamentoService;
import it.csi.siac.siacbilser.business.service.conto.RicercaDettaglioContoService;
import it.csi.siac.siacbilser.business.service.conto.RicercaSinteticaContoFigliService;
import it.csi.siac.siacbilser.business.service.conto.RicercaSinteticaContoService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamentoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;

@WebService(serviceName = "ContoService", portName = "ContoServicePort", 
targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.ContoService")

public class ContoServiceImpl implements ContoService {

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
	public InserisceContoResponse inserisceConto(InserisceConto parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceContoService.class, parameters);
	}

	@Override
	public AggiornaContoResponse aggiornaConto(AggiornaConto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaContoService.class, parameters);
	}

	@Override
	public AnnullaContoResponse annullaConto(AnnullaConto parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaContoService.class, parameters);
	}
	
	@Override
	public EliminaContoResponse eliminaConto(EliminaConto parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaContoService.class, parameters);
	}

	@Override
	public RicercaSinteticaContoResponse ricercaSinteticaConto(RicercaSinteticaConto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaContoService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaContoFigliResponse ricercaSinteticaContoFigli(RicercaSinteticaContoFigli parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaContoFigliService.class, parameters);
	}

	@Override
	public RicercaDettaglioContoResponse ricercaDettaglioConto(RicercaDettaglioConto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioContoService.class, parameters);
	}

	@Override
	public LeggiTreeCodiceBilancioResponse leggiTreeCodiceBilancio(LeggiTreeCodiceBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiTreeCodiceBilancioService.class, parameters);
	}

	@Override
	public RicercaClassiPianoAmmortamentoResponse ricercaClassiPianoAmmortamento(RicercaClassiPianoAmmortamento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaClassiPianoAmmortamentoService.class, parameters);
	}

}
