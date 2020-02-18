/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaEntrataResponse;

/**
 * Conta dei dati collegati al subdocumento iva di spesa
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContaDatiCollegatiSubdocumentoIvaEntrataService extends CheckedAccountBaseService<ContaDatiCollegatiSubdocumentoIvaEntrata, ContaDatiCollegatiSubdocumentoIvaEntrataResponse> {

	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdocumentoIvaEntrata(), "subdocumento iva spesa");
	}
	
	@Override
	@Transactional(readOnly=true)
	public ContaDatiCollegatiSubdocumentoIvaEntrataResponse executeService(ContaDatiCollegatiSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Long ordinativiCollegati = subdocumentoIvaEntrataDad.countOrdinativiAssociatiAQuoteDocumentoCollegato(req.getSubdocumentoIvaEntrata());
		
		res.setOrdinativiCollegati(ordinativiCollegati);
	}

}
