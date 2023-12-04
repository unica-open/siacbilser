/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;

/**
 * Elaborazione di un file in modalit&agrave; sincrona.
 * Attiva dinamicamente il servizio sottostante a partire dal codice tipo file sulla base di quanto
 * descritto in {@link ElaboraFileServicesEnum}.
 * 
 * @author Domenico Lisi
 * @see ElaboraFileService
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileAsyncService extends ExtendedBaseService<AsyncServiceRequestWrapper<ElaboraFile>, AsyncServiceResponse> {
	
	@Autowired
	private ApplicationContext appCtx;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("request"));
		checkNotNull(req.getRequest().getFile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("file"));
		checkNotNull(req.getRequest().getFile().getTipo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo file"));
		checkNotNull(req.getRequest().getFile().getTipo().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo file"));
	}
	
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<ElaboraFile> serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		String methodName = "execute";
		
		String codiceTipoFile = req.getRequest().getFile().getTipo().getCodice();

		ElaboraFileAsyncServiceInfo elaboraFileAsyncServiceInfo = getElaboraFileAsyncServiceInfo(codiceTipoFile);

		ElaboraFileAsyncBaseService<?, ?> serviceBean;
		
		try {
			serviceBean = appCtx.getBean(elaboraFileAsyncServiceInfo.getAsyncServiceName(), elaboraFileAsyncServiceInfo.getAsyncServiceClass());
		} catch (BeansException be) {
			String msg = "Impossibile ottenere il bean " +elaboraFileAsyncServiceInfo.getAsyncServiceName()+ " legato al codice tipo file: "+ codiceTipoFile;
			log.error(methodName, msg, be);
			throw new BusinessException(msg, Esito.FALLIMENTO);
		}
		
		AsyncServiceResponse asr = serviceBean.executeService(req);
		res = asr;
		
		
	}

	protected ElaboraFileAsyncServiceInfo getElaboraFileAsyncServiceInfo(String codiceTipoFile) {
		return ElaboraFileServicesEnum.byCodice(codiceTipoFile);
	}
}
