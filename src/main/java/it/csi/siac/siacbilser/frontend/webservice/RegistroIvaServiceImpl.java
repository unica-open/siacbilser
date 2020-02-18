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
import it.csi.siac.siacbilser.business.service.registroiva.AggiornaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.AllineaProtocolloRegistroIvaAsyncService;
import it.csi.siac.siacbilser.business.service.registroiva.AllineaProtocolloRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.BloccaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.EliminaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.InserisceRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RecuperaProtocolloRegistroIvaAsyncService;
import it.csi.siac.siacbilser.business.service.registroiva.RecuperaProtocolloRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaDettaglioRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaSinteticaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.SbloccaRegistroIvaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIvaResponse;

/**
 * The Class RegistroIvaServiceImpl.
 */
@WebService(serviceName = "RegistroIvaService", portName = "RegistroIvaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService")
public class RegistroIvaServiceImpl implements RegistroIvaService {

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
	public InserisceRegistroIvaResponse inserisceRegistroIva(InserisceRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceRegistroIvaService.class, parameters);
	}

	@Override
	public AggiornaRegistroIvaResponse aggiornaRegistroIva(AggiornaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRegistroIvaService.class, parameters);
	}

	@Override
	public RicercaRegistroIvaResponse ricercaRegistroIva(RicercaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaRegistroIvaService.class, parameters);
	}

	@Override
	public EliminaRegistroIvaResponse eliminaRegistroIva(EliminaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaRegistroIvaService.class, parameters);
	}

	@Override
	public RicercaDettaglioRegistroIvaResponse ricercaDettaglioRegistroIva(RicercaDettaglioRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioRegistroIvaService.class, parameters);
	}

	@Override
	public RicercaSinteticaRegistroIvaResponse ricercaSinteticaRegistroIva(RicercaSinteticaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaRegistroIvaService.class, parameters);
	}

	// CR-3791
	@Override
	public BloccaRegistroIvaResponse bloccaRegistroIva(BloccaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, BloccaRegistroIvaService.class, parameters);
	}

	@Override
	public SbloccaRegistroIvaResponse sbloccaRegistroIva(SbloccaRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, SbloccaRegistroIvaService.class, parameters);
	}

	@Override
	public AllineaProtocolloRegistroIvaResponse allineaProtocolloRegistroIva(AllineaProtocolloRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, AllineaProtocolloRegistroIvaService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse allineaProtocolloRegistroIvaAsync(AsyncServiceRequestWrapper<AllineaProtocolloRegistroIva> parameters) {
		return BaseServiceExecutor.execute(appCtx, AllineaProtocolloRegistroIvaAsyncService.class, parameters);
	}

	@Override
	public RecuperaProtocolloRegistroIvaResponse recuperaProtocolloRegistroIva(RecuperaProtocolloRegistroIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RecuperaProtocolloRegistroIvaService.class, parameters);
	}

	@Override
	public AsyncServiceResponse recuperaProtocolloRegistroIvaAsync(AsyncServiceRequestWrapper<RecuperaProtocolloRegistroIva> parameters) {
		return BaseServiceExecutor.execute(appCtx, RecuperaProtocolloRegistroIvaAsyncService.class, parameters);
	}

	

}
