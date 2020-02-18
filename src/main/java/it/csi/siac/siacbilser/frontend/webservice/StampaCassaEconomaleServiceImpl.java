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
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaStampeCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaUltimaStampaDefinitivaGiornaleCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaUltimoRendicontoCassaStampatoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaUltimoRendicontoRichiestaEconomaleStampatoService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.StampaGiornaleCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.RicercaSinteticaRendicontoCassaDaStampareService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.StampaRendicontoCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.VerificaStampaRendicontoCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.StampaRicevutaRendicontoRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.StampaRicevutaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccecser.frontend.webservice.CECSvcDictionary;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampareResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampatoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoRichiestaEconomaleStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoRichiestaEconomaleStampatoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassaResponse;


/**
 * The Class CassaEconomaleServiceImpl.
 */
@WebService(serviceName = "StampaCassaEconomaleService", portName = "StampaCassaEconomaleService", 
targetNamespace = CECSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService")
public class StampaCassaEconomaleServiceImpl implements StampaCassaEconomaleService {

	
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
	public StampaRicevutaRichiestaEconomaleResponse stampaRicevutaRichiestaEconomale(StampaRicevutaRichiestaEconomale parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRicevutaRichiestaEconomaleService.class), StampaRicevutaRichiestaEconomaleService.class).executeService(parameters);
	}
	
	@Override
	public StampaRicevutaRendicontoRichiestaEconomaleResponse stampaRendicontoRicevutaRichiestaEconomale(StampaRicevutaRendicontoRichiestaEconomale parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRicevutaRendicontoRichiestaEconomaleService.class), StampaRicevutaRendicontoRichiestaEconomaleService.class).executeService(parameters);
	}

	@Override
	public StampaRendicontoCassaResponse stampaRendicontoCassa(StampaRendicontoCassa parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaRendicontoCassaService.class), StampaRendicontoCassaService.class).executeService(parameters);
	}

	@Override
	public StampaGiornaleCassaResponse stampaGiornaleCassa(StampaGiornaleCassa parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaGiornaleCassaService.class), StampaGiornaleCassaService.class).executeService(parameters);
	}
	
	@Override
	public RicercaUltimoRendicontoCassaStampatoResponse ricercaUltimoRendicontoCassaStampato(RicercaUltimoRendicontoCassaStampato parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaUltimoRendicontoCassaStampatoService.class, parameters);
	}
	
	@Override
	public RicercaUltimaStampaDefinitivaGiornaleCassaResponse ricercaDatiUltimaStampaDefinitivaGiornaleCassa(RicercaUltimaStampaDefinitivaGiornaleCassa parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaUltimaStampaDefinitivaGiornaleCassaService.class, parameters);
	}
	
	@Override
	public RicercaStampeCassaEconomaleResponse ricercaStampeCassaEconomale(RicercaStampeCassaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaStampeCassaEconomaleService.class, parameters);
	}

	@Override
	public RicercaUltimoRendicontoRichiestaEconomaleStampatoResponse ricercaUltimoRendicontoRichiestaEconomaleStampato(RicercaUltimoRendicontoRichiestaEconomaleStampato parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaUltimoRendicontoRichiestaEconomaleStampatoService.class, parameters);
	}

	@Override
	public VerificaStampaRendicontoCassaResponse verificaStampaRendicontoCassa(VerificaStampaRendicontoCassa parameters) {
		return BaseServiceExecutor.execute(appCtx, VerificaStampaRendicontoCassaService.class, parameters);
	}

	@Override
	public RicercaSinteticaRendicontoCassaDaStampareResponse ricercaSinteticaRendicontoCassaDaStampare(RicercaSinteticaRendicontoCassaDaStampare parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaRendicontoCassaDaStampareService.class, parameters);
	}

	
	
}
