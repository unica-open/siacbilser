/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.AggiornaElementoPianoDeiContiRegistrazioneMovFinService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.AssegnaContoEPRegistrazioneMovFinService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.CalcolaImportoMovimentoCollegatoService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.RicercaDettaglioRegistrazioneMovFinService;
import it.csi.siac.siacbilser.business.service.registrazionemovfin.RicercaSinteticaRegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegatoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;

@WebService(serviceName = "RegistrazioneMovFinService", portName = "RegistrazioneMovFinServicePort", 
targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService")
public class RegistrazioneMovFinServiceImpl implements RegistrazioneMovFinService {

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
	public RicercaSinteticaRegistrazioneMovFinResponse ricercaSinteticaRegistrazioneMovFin(RicercaSinteticaRegistrazioneMovFin parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaRegistrazioneMovFinService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioRegistrazioneMovFinResponse ricercaDettaglioRegistrazioneMovFin(RicercaDettaglioRegistrazioneMovFin parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioRegistrazioneMovFinService.class, parameters);
	}


	@Override
	public AssegnaContoEPRegistrazioneMovFinResponse assegnaContoEPRegistrazioneMovFin(AssegnaContoEPRegistrazioneMovFin parameters) {
		return BaseServiceExecutor.execute(appCtx, AssegnaContoEPRegistrazioneMovFinService.class, parameters);
	}
	
	

	@Override
	public AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse aggiornaElementoPianoDeiContiRegistrazioneMovFin(@WebParam AggiornaElementoPianoDeiContiRegistrazioneMovFin parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaElementoPianoDeiContiRegistrazioneMovFinService.class, parameters);
	}


	@Override
	public CalcolaImportoMovimentoCollegatoResponse calcolaImportoMovimentoCollegato(CalcolaImportoMovimentoCollegato parameters) {
		return BaseServiceExecutor.execute(appCtx, CalcolaImportoMovimentoCollegatoService.class, parameters);
	}
	
}
