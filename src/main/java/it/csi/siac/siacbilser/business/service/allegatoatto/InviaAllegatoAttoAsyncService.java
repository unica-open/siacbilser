/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAttoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InviaAllegatoAttoAsyncService extends AsyncBaseService<InviaAllegatoAtto, 
																					  InviaAllegatoAttoResponse, 
																					  AsyncServiceRequestWrapper<InviaAllegatoAtto>, 
																					  InviaAllegatoAttoAsyncResponseHandler, 
																					  InviaAllegatoAttoService> {

	//DADs
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	
	@Override
	protected void preStartService() {
		checkAlmenoUnaQuotaSpesaPresente();
	}

	@Override
	protected void postStartService() {
		// Nessuna post-elaborazione
	}
	//CR-2996: STAMPA e INVIO a FLUX: solo per Atti che abbiano almeno una spesa collegata 
	private void checkAlmenoUnaQuotaSpesaPresente() {
		final String methodName = "checkAlmenoUnaQuotaSpesaPresente";
		if(req.getRequest()!=null && req.getRequest().getAllegatoAtto()!= null){
			Boolean hasQuoteSpesa = Boolean.valueOf(allegatoAttoDad.countQuoteSpesaAssociateAdAllegato(req.getRequest().getAllegatoAtto()) >=1);
			if(!hasQuoteSpesa){
				log.debug(methodName, "L'allegato [uid:" + req.getRequest().getAllegatoAtto().getUid() + "] non risulta collegato a nessuna quota spesa. ");
				throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("invia", "allegato atto non collegato a nessuna quota spesa"));
			}
		}
	}
}
