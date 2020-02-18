/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeDocumentoEntrataService extends CheckedAccountBaseService<RicercaPuntualeDocumentoEntrata, RicercaPuntualeDocumentoEntrataResponse> {

	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	/** The doc. */
	private DocumentoEntrata doc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		doc = req.getDocumentoEntrata();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));		

		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		
		checkCondition(doc.getStatoOperativoDocumento()!=null || req.getStatoOperativoDocumentoDaEscludere()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaPuntualeDocumentoEntrataResponse executeService(RicercaPuntualeDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoEntrata documentoEntrata = documentoEntrataDad.ricercaPuntualeDocumentoEntrata(doc, req.getStatoOperativoDocumentoDaEscludere());
		res.setDocumentoEntrata(documentoEntrata);
	}

}
