/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoDocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoDocumentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoDocumentoService extends CheckedAccountBaseService<RicercaTipoDocumento, RicercaTipoDocumentoResponse>{
	
	/** The tipo documento dad. */
	@Autowired
	private TipoDocumentoDad tipoDocumentoDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		//tipoFamDoc ora è facoltativo!
//		checkNotNull(req.getTipoFamDoc(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo famiglia"));
//		checkNotNull(req.getTipoFamDoc().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo famiglia"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoDocumentoDad.setEnte(req.getEnte());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional (readOnly=true)
	public RicercaTipoDocumentoResponse executeService(RicercaTipoDocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<TipoDocumento> tipiDocumento = tipoDocumentoDad.ricercaTipoDocumento(req.getTipoFamDoc(), req.getFlagSubordinato(), req.getFlagRegolarizzazione());	
		
		res.setElencoTipiDocumento(tipiDocumento);
		
	}
}
