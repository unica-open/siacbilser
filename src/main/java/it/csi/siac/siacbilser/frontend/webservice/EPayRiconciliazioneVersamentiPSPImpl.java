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

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.EPayRiconciliazioneVersamentiPSP;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.EsitoFlussiPagoPAResponse;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPARequest;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPAResponse;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TestataTrasmissioneFlussiType;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TrasmettiFlussiInErrorePagoPARequest;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TrasmettiFlussiPagoPARequest;
import it.csi.epay.epaywso.types.ResponseType;
import it.csi.siac.siacbilser.business.service.pagopa.LeggiEsitoFlussiService;
import it.csi.siac.siacbilser.business.service.pagopa.RicercaProvvisoriService;
import it.csi.siac.siacbilser.business.service.pagopa.TrasmettiFlussiService;
import it.csi.siac.siacbilser.business.service.pagopa.msg.LeggiEsitoFlussi;
import it.csi.siac.siacbilser.business.service.pagopa.msg.LeggiEsitoFlussiResponse;
import it.csi.siac.siacbilser.business.service.pagopa.msg.RicercaProvvisori;
import it.csi.siac.siacbilser.business.service.pagopa.msg.RicercaProvvisoriResponse;
import it.csi.siac.siacbilser.business.service.pagopa.msg.TrasmettiFlussi;
import it.csi.siac.siacbilser.business.service.pagopa.msg.TrasmettiFlussiResponse;
import it.csi.siac.siacbilser.business.service.pagopa.util.ResultTypeEnum;
import it.csi.siac.siacbilser.business.utility.Utility;

/**
 * @author AR
 */
@WebService(
        serviceName = "EPayRiconciliazioneVersamentiPSP",
        portName = "EPayRiconciliazioneVersamentiPSPSOAP",
        targetNamespace = "http://www.csi.it/epay/epaywso/riconciliazione-versamenti-psp",
      //  wsdlLocation = "EPayRiconciliazioneVersamentiPSP.wsdl",
        endpointInterface = "it.csi.epay.epaywso.riconciliazione_versamenti_psp.EPayRiconciliazioneVersamentiPSP")
        
public class EPayRiconciliazioneVersamentiPSPImpl implements EPayRiconciliazioneVersamentiPSP {

	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public EsitoFlussiPagoPAResponse esitoFlussiPagoPA(TestataTrasmissioneFlussiType param) {
		LeggiEsitoFlussi req = new LeggiEsitoFlussi(param);

        try{
        	LeggiEsitoFlussiResponse res = Utility.getBeanViaDefaultName(appCtx, LeggiEsitoFlussiService.class).executeService(req);
	        return res.getPagoPAResponse();
        } catch(Throwable e) {
    		EsitoFlussiPagoPAResponse rt = new EsitoFlussiPagoPAResponse();
        	rt.setResult(ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType(e.getMessage()));
        	return rt;
        }
	}

	@Override
	public ResponseType trasmettiFlussiPagoPA(TrasmettiFlussiPagoPARequest param) {
        TrasmettiFlussi req = new TrasmettiFlussi(param);

        try{
        	TrasmettiFlussiResponse res = Utility.getBeanViaDefaultName(appCtx, TrasmettiFlussiService.class).executeService(req);
	        return res.getPagoPAResponse();
        } catch(Throwable e) {
        	ResponseType rt = new ResponseType();
        	rt.setResult(ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType(e.getMessage()));
        	return rt;
        }
	}

	@Override
	public RicercaProvvisoriPagoPAResponse ricercaProvvisoriPagoPA(RicercaProvvisoriPagoPARequest param) {
        RicercaProvvisori req = new RicercaProvvisori(param);

        try{
        	RicercaProvvisoriResponse res = Utility.getBeanViaDefaultName(appCtx, RicercaProvvisoriService.class).executeService(req);
	        return res.getPagoPAResponse();
        } catch(Throwable e) {
        	RicercaProvvisoriPagoPAResponse rt = new RicercaProvvisoriPagoPAResponse();
        	rt.setResult(ResultTypeEnum.DEFAULT_RT200_ERRORI_DI_SISTEMA.getResultType(e.getMessage()));
        	return rt;
        }
	}

	@Override
	public ResponseType trasmettiFlussiInErrorePagoPA(TrasmettiFlussiInErrorePagoPARequest parameters) {
    	ResponseType rt = new ResponseType();
    	rt.setResult(ResultTypeEnum.DEFAULT_RT099_NON_IMPLEMENTATO.getResultType());
    	return rt;
	}
}
