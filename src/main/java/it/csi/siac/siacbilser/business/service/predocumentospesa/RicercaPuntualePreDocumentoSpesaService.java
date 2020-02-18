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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualePreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualePreDocumentoSpesaService extends CheckedAccountBaseService<RicercaPuntualePreDocumentoSpesa, RicercaPuntualePreDocumentoSpesaResponse> {
	
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
		
		checkNotNull(preDoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(preDoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkCondition(preDoc.getNumero()!=null && !preDoc.getNumero().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero predocumento"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaPuntualePreDocumentoSpesaResponse executeService(RicercaPuntualePreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		PreDocumentoSpesa preDocumentoSpesa = preDocumentoSpesaDad.ricercaPuntualePreDocumento(preDoc);
		if(preDocumentoSpesa==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("PreDocumentoSpesa","numero: "+ preDoc.getNumero()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setPreDocumentoSpesa(preDocumentoSpesa);
	}

}
