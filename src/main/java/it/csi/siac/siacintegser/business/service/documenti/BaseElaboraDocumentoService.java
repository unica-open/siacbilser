/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.helper.BilancioServiceHelper;
import it.csi.siac.siacintegser.business.service.helper.FileServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseRequest;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoRequestInterface;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoResponseInterface;

public abstract class BaseElaboraDocumentoService<EDREQ extends BaseRequest & ElaboraDocumentoRequestInterface, 
															 EDRES extends BaseResponse & ElaboraDocumentoResponseInterface> 
															 extends IntegBaseService<EDREQ, EDRES> {
	
	@Autowired private BilancioServiceHelper bilancioServiceHelper;
	@Autowired private FileServiceHelper fileServiceHelper;
	
	@Override
	protected void checkServiceBaseParameters(EDREQ edReq) throws ServiceParamError {

		super.checkServiceBaseParameters(edReq);
		
		assertParamNotNull(edReq.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		assertParamNotNull(edReq.getContenutoDocumento(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("contenuto documento"));
	}

	protected ElaboraFile buildElaboraFileRequest(EDREQ edReq) {
		ElaboraFile req = map(edReq, ElaboraFile.class, getRequestMapId());

		req.setBilancio(bilancioServiceHelper.findBilancioByAnno(ente, richiedente, edReq.getAnnoBilancio()));
		
		String codiceTipoDocumento = getCodiceTipoDocumento(edReq);
		req.getFile().setTipo(fileServiceHelper.findTipoFileByCodice(ente, richiedente, codiceTipoDocumento));

		req.setEnte(ente);

		return req;
	}

	protected abstract String getCodiceTipoDocumento(EDREQ edReq);

	protected EDRES syncExecute(EDREQ edReq) {
		ElaboraFile req = buildElaboraFileRequest(edReq);

		Class<? extends ElaboraFileService> elaboraFileServiceClass = getElaboraFileServiceClass();
		
		ElaboraFileResponse res = appCtx.getBean(Utility.toDefaultBeanName(elaboraFileServiceClass), elaboraFileServiceClass).executeService(req);

		EDRES ires = map(res, getIntegResponseClass(), getResponseMapId());

		for (it.csi.siac.siaccorser.model.Messaggio m : res.getMessaggi()) {
			addMessaggio(m);
		}

		return ires;
	}

	protected Class<? extends ElaboraFileService> getElaboraFileServiceClass() { 
		return ElaboraFileService.class;
	}
	
	protected EDRES asyncExecute(EDREQ edReq) {
		ElaboraFile req = buildElaboraFileRequest(edReq);

		AsyncServiceRequestWrapper<ElaboraFile> reqw = new AsyncServiceRequestWrapper<ElaboraFile>();
		
		reqw.setRequest(req);
		reqw.setEnte(ente);
		reqw.setRichiedente(richiedente);
		reqw.setAccount(richiedente.getAccount());
		reqw.setAzioneRichiesta(new AzioneRichiesta(req.getFile().getTipo().getAzioneServizio()));
		
		
		Class<? extends ElaboraFileAsyncService> elaboraFileAsyncServiceClass = getElaboraFileAsyncServiceClass();
		
		AsyncServiceResponse res = appCtx.getBean(Utility.toDefaultBeanName(elaboraFileAsyncServiceClass), elaboraFileAsyncServiceClass).executeService(reqw);

		return map(res, getIntegResponseClass(), getResponseMapId());
	}

	protected Class<? extends ElaboraFileAsyncService> getElaboraFileAsyncServiceClass() { 
		return ElaboraFileAsyncService.class;
	}
	
	protected abstract IntegMapId getRequestMapId();
	protected abstract IntegMapId getResponseMapId();
	
}
