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

import it.csi.siac.pagopa.frontend.webservice.PagoPAService;
import it.csi.siac.pagopa.frontend.webservice.PagoPASvcDictionary;
import it.csi.siac.pagopa.frontend.webservice.msg.AggiornaAccertamentoModaleResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazione;
import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazioneResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaAccertamentoResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioniResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDocResponse;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniResponse;
import it.csi.siac.siacbilser.business.service.pagopa.AggiornaAccertamentoRiconciliazioneService;
import it.csi.siac.siacbilser.business.service.pagopa.ElaboraFlussoRiconciliazioneService;
import it.csi.siac.siacbilser.business.service.pagopa.RicercaAccertamentoRiconciliazioneService;
import it.csi.siac.siacbilser.business.service.pagopa.RicercaElaborazioniService;
import it.csi.siac.siacbilser.business.service.pagopa.RicercaRiconciliazioniDocService;
import it.csi.siac.siacbilser.business.service.pagopa.RicercaRiconciliazioniService;
import it.csi.siac.siacbilser.business.utility.Utility;

@WebService(serviceName = "PagoPAService", 
			portName = "PagoPAServicePort", 
			targetNamespace = PagoPASvcDictionary.NAMESPACE,
			endpointInterface = "it.csi.siac.pagopa.frontend.webservice.PagoPAService")

public class PagoPAServiceImpl implements PagoPAService {
    
	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public ElaboraFlussoRiconciliazioneResponse elaboraFlussoRiconciliazione(ElaboraFlussoRiconciliazione req) {
		return Utility.getBeanViaDefaultName(appCtx, ElaboraFlussoRiconciliazioneService.class).executeService(req);
	}

	@Override
	public RicercaElaborazioniResponse ricercaElaborazioni(RicercaElaborazioni req) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaElaborazioniService.class).executeService(req);
	}
	
	
	@Override
	public RicercaRiconciliazioniResponse ricercaRiconciliazioni(RicercaRiconciliazioni req) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaRiconciliazioniService.class).executeService(req);
	}
	
	@Override
	public RicercaRiconciliazioniDocResponse ricercaRiconciliazioniDoc(RicercaRiconciliazioniDoc req) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaRiconciliazioniDocService.class).executeService(req);
	}
	
	//SIAC-8046 Task 2.2 CM 31/03/2021 Inizio
	@Override
	public RicercaAccertamentoResponse ricercaAccertamento(RicercaRiconciliazioniDoc req) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaAccertamentoRiconciliazioneService.class).executeService(req);
	}
	//SIAC-8046 Task 2.2 CM 31/03/2021 Fine
	
	//SIAC-8046 Task 2.2-2.3 CM 13/04/2021 Inizio
	@Override
	public AggiornaAccertamentoModaleResponse aggiornaAccertamento(RicercaRiconciliazioniDoc req) {
		return Utility.getBeanViaDefaultName(appCtx, AggiornaAccertamentoRiconciliazioneService.class).executeService(req);
	}
	//SIAC-8046 Task 2.2-2.3 CM 13/04/2021 Fine
}
