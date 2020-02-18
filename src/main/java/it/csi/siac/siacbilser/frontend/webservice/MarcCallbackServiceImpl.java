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

import it.csi.siac.pcc.marc.schema.callbackservicetypes_1.AggiornamentoStatoRichiestaRequest;
import it.csi.siac.pcc.marc.schema.marccallbackservice_1_0.MarcCallbackService;
import it.csi.siac.pcc.marc.schema.marccommontypes_1.ResponseType;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.AggiornaStatoRichiesta;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.AggiornaStatoRichiestaResponse;
import it.csi.siac.siacbilser.business.service.pcc.AggiornaStatoRichiestaService.ResultTypeEnum;
import it.csi.siac.siacbilser.business.utility.Utility;

/**
 * Implementazione del Servizio di callback invocato da MARC. 
 * 
 * @author Domenico
 */
@WebService(serviceName = "MarcCallbackService",
            portName = "MarcCallbackServiceSOAP",
            targetNamespace = "http://www.csi.it/marc/schema/MarcCallbackService-1.0",
            //wsdlLocation = "file:/D:/workarea/csi/siac/fase19/documentazione/INTEGRAZIONE%20PCC%2021102015/Interfacce%20WS/MarcCallbackService.wsdl",
            endpointInterface = "it.csi.siac.pcc.marc.schema.marccallbackservice_1_0.MarcCallbackService")
                      
public class MarcCallbackServiceImpl implements MarcCallbackService {
    
	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
    public ResponseType aggiornamentoStatoRichiesta(AggiornamentoStatoRichiestaRequest param) { 

        AggiornaStatoRichiesta req = new AggiornaStatoRichiesta();
        req.setAggiornamentoStatoRichiestaRequest(param);
        try{
        	AggiornaStatoRichiestaResponse res = Utility.getBeanViaDefaultName(appCtx, AggiornaStatoRichiestaService.class).executeService(req);
	        return res.getResponseType();
        } catch(Exception e){
        	ResponseType rt = new ResponseType();
        	rt.setResult(ResultTypeEnum.RT210.getResultType());
        	return rt;
        }
        
        
    }

}
