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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * Elaborazione di un file in modalit&agrave; sincrona.
 * Attiva dinamicamente il servizio sottostante a partire dal codiceTipoFile sulla base di quanto
 * descritto in {@link ElaboraFileServicesEnum}.
 * 
 * Per utilizzare la modalit&agrave; asincrona utilizzare {@link ElaboraFileAsyncService}
 * 
 * 
 * @author Domenico Lisi
 * @see ElaboraFileAsyncService
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileService extends ExtendedBaseService<ElaboraFile, ElaboraFileResponse> {
	
	@Autowired
	private ApplicationContext appCtx;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getFile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("file"));
		checkNotNull(req.getFile().getTipo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo file"));
		checkNotNull(req.getFile().getTipo().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo file"));
	}
	
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public ElaboraFileResponse executeService(ElaboraFile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		String codiceTipoFile = req.getFile().getTipo().getCodice();
		
		ElaboraFileServiceInfo elaboraFileServiceInfo = getElaboraFileServiceInfo(codiceTipoFile);
		
		ElaboraFileBaseService serviceBean;
		
		try {
			serviceBean = appCtx.getBean(elaboraFileServiceInfo.getServiceName(), elaboraFileServiceInfo.getServiceClass());
		} catch (BeansException be) {
			String msg = "Impossibile ottenere il bean " +elaboraFileServiceInfo.getServiceName()+ " legato al codice tipo file: "+ codiceTipoFile;
			log.error(methodName, msg, be);
			throw new BusinessException(msg, Esito.FALLIMENTO);
		}
		
		ElaboraFileResponse efr = serviceBean.executeServiceTxRequiresNew(req); //serviceExecutor.executeServiceTxRequiresNew(serviceBean, req); 
		res = efr;
		
	}

	protected ElaboraFileServiceInfo getElaboraFileServiceInfo(String codiceTipoFile) {
		return ElaboraFileServicesEnum.byCodice(codiceTipoFile);
	}

}
