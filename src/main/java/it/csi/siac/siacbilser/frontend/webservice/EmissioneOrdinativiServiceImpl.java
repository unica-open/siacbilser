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
import it.csi.siac.siacbilser.business.service.capitolo.ControllaDisponibilitaCassaCapitoloByMovimentoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoAsyncService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoAsyncService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimentoResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.EmissioneOrdinativiService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;


/**
 * The Class EmissioneOrdinativiServiceImpl.
 */
@WebService(serviceName = "EmissioneOrdinativiService", portName = "EmissioneOrdinativiServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.EmissioneOrdinativiService")
public class EmissioneOrdinativiServiceImpl implements EmissioneOrdinativiService {
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	/**
	 * Inits the bean.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public AsyncServiceResponse emetteOrdinativiDiPagamentoDaElenco(AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> parameters) {
//		AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> wrapper = AsyncRequestMapper.toAsyncServiceRequestWrapper(parameters);
		return BaseServiceExecutor.execute(appCtx, EmetteOrdinativiDiPagamentoDaElencoAsyncService.class, parameters);
	}

	@Override
	public AsyncServiceResponse emetteOrdinativiDiIncassoDaElenco(AsyncServiceRequestWrapper<EmetteOrdinativiDiIncassoDaElenco> parameters) {
//		AsyncServiceRequestWrapper<EmetteOrdinativiDiIncassoDaElenco> wrapper = AsyncRequestMapper.toAsyncServiceRequestWrapper(parameters);
		return BaseServiceExecutor.execute(appCtx, EmetteOrdinativiDiIncassoDaElencoAsyncService.class, parameters);
	}

	@Override
	public EmetteOrdinativiDiPagamentoDaElencoResponse emetteOrdinativiDiPagamentoDaElencoSync(EmetteOrdinativiDiPagamentoDaElenco request) {
		return BaseServiceExecutor.execute(appCtx, EmetteOrdinativiDiPagamentoDaElencoService.class, request);
	}

	@Override
	public EmetteOrdinativiDiIncassoDaElencoResponse emetteOrdinativiDiIncassoDaElencoSync(EmetteOrdinativiDiIncassoDaElenco request) {
		return BaseServiceExecutor.execute(appCtx, EmetteOrdinativiDiIncassoDaElencoService.class, request);
	}

	@Override
	public ControllaDisponibilitaCassaCapitoloByMovimentoResponse controllaDisponibilitaCassaCapitoloByMovimento(
			ControllaDisponibilitaCassaCapitoloByMovimento request) {
		// TODO Auto-generated method stub
		return BaseServiceExecutor.execute(appCtx, ControllaDisponibilitaCassaCapitoloByMovimentoService.class, request);
	}
	
}
