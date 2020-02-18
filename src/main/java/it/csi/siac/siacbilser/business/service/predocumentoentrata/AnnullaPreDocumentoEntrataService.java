/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaPreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaPreDocumentoEntrataService extends CheckedAccountBaseService<AnnullaPreDocumentoEntrata, AnnullaPreDocumentoEntrataResponse> {
	
	/** The pre documento entrata dad. */
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	/** The pre doc. */
	private PreDocumentoEntrata preDoc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoEntrata();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento Entrata"));
		checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento Entrata"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		preDocumentoEntrataDad.setEnte(preDoc.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaPreDocumentoEntrataResponse executeService(AnnullaPreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		StatoOperativoPreDocumento statoOperativoDocumento = StatoOperativoPreDocumento.ANNULLATO;
		preDocumentoEntrataDad.aggiornaStatoPreDocumento(preDoc.getUid(), statoOperativoDocumento);
		preDoc.setStatoOperativoPreDocumento(statoOperativoDocumento);
		res.setPreDocumentoEntrata(preDoc);

	}

}
