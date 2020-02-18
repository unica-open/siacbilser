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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaPreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaPreDocumentoSpesaService extends CheckedAccountBaseService<AnnullaPreDocumentoSpesa, AnnullaPreDocumentoSpesaResponse> {
	
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
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento spesa"));
		checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento spesa"));
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
		preDocumentoSpesaDad.setEnte(preDoc.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaPreDocumentoSpesaResponse executeService(AnnullaPreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		StatoOperativoPreDocumento statoOperativoDocumento = StatoOperativoPreDocumento.ANNULLATO;
		preDocumentoSpesaDad.aggiornaStatoPreDocumento(preDoc.getUid(), statoOperativoDocumento);
		preDoc.setStatoOperativoPreDocumento(statoOperativoDocumento);
		res.setPreDocumentoSpesa(preDoc);

	}

}
