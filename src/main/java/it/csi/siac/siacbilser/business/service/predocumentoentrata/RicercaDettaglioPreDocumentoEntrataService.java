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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioPreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioPreDocumentoEntrataService extends CheckedAccountBaseService<RicercaDettaglioPreDocumentoEntrata, RicercaDettaglioPreDocumentoEntrataResponse> {
	
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
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));		
		checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioPreDocumentoEntrataResponse executeService(RicercaDettaglioPreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		PreDocumentoEntrata preDocumentoEntrata = preDocumentoEntrataDad.findPreDocumentoById(preDoc.getUid());	
		if(preDocumentoEntrata==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("PreDocumentoEntrata","uid: "+ preDoc.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setPreDocumentoEntrata(preDocumentoEntrata);	
	}

}
