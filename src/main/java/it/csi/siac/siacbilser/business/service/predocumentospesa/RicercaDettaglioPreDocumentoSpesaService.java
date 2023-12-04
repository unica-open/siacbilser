/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioPreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioPreDocumentoSpesaService extends CheckedAccountBaseService<RicercaDettaglioPreDocumentoSpesa, RicercaDettaglioPreDocumentoSpesaResponse> {
	
	/** The pre documento spesa dad. */
	@Autowired 
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	/** The pre doc. */
	private PreDocumentoSpesa preDoc;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));		
		checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaDettaglioPreDocumentoSpesaResponse executeService(RicercaDettaglioPreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		PreDocumentoSpesa preDocumentoSpesa = preDocumentoSpesaDad.findPreDocumentoById(preDoc.getUid());
		if(preDocumentoSpesa==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("PreDocumentoSpesa","uid: "+ preDoc.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setPreDocumentoSpesa(preDocumentoSpesa);	
	}

}
