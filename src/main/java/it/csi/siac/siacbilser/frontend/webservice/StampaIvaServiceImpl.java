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
import it.csi.siac.siacbilser.business.service.registroiva.RicercaSinteticaStampaIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaStampaIvaService;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.StampaLiquidazioneIvaService;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.StampaRegistriIvaService;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.StampaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.StampaRiepilogoAnnualeIvaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIvaResponse;

/**
 * Implementazione del servizio StampaIvaService.
 *
 */
@WebService(serviceName = "StampaIvaService", portName = "StampaIvaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService")
public class StampaIvaServiceImpl implements StampaIvaService {
	
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
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService#stampaRegistroIva(it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva)
	 */
	@Override
	public StampaRegistroIvaResponse stampaRegistroIva(StampaRegistroIva parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRegistroIvaService.class), StampaRegistroIvaService.class).executeService(parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService#stampaRegistriIva(it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIva)
	 */
	@Override
	public StampaRegistriIvaResponse stampaRegistriIva(StampaRegistriIva parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRegistriIvaService.class), StampaRegistriIvaService.class).executeService(parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService#stampaLiquidazioneIva(it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva)
	 */
	@Override
	public StampaLiquidazioneIvaResponse stampaLiquidazioneIva(StampaLiquidazioneIva parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaLiquidazioneIvaService.class), StampaLiquidazioneIvaService.class).executeService(parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService#stampaRiepilogoAnnualeIva(it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva)
	 */
	@Override
	public StampaRiepilogoAnnualeIvaResponse stampaRiepilogoAnnualeIva(StampaRiepilogoAnnualeIva parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRiepilogoAnnualeIvaService.class), StampaRiepilogoAnnualeIvaService.class).executeService(parameters);
	}

	@Override
	public RicercaStampaIvaResponse ricercaStampaIva(RicercaStampaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaStampaIvaService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaStampaIvaResponse ricercaSinteticaStampaIva(RicercaSinteticaStampaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaStampaIvaService.class, parameters);
	}

}
