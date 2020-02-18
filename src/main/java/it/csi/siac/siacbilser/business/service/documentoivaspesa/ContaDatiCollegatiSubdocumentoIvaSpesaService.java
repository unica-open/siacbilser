/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesaResponse;

/**
 * Conta dei dati collegati al subdocumento iva di spesa
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContaDatiCollegatiSubdocumentoIvaSpesaService extends CheckedAccountBaseService<ContaDatiCollegatiSubdocumentoIvaSpesa, ContaDatiCollegatiSubdocumentoIvaSpesaResponse> {

	@Autowired
	private SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdocumentoIvaSpesa(), "subdocumento iva spesa");
	}
	
	@Override
	@Transactional(readOnly=true)
	public ContaDatiCollegatiSubdocumentoIvaSpesaResponse executeService(ContaDatiCollegatiSubdocumentoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Long ordinativiCollegati = subdocumentoIvaSpesaDad.countOrdinativiAssociatiAQuoteDocumentoCollegato(req.getSubdocumentoIvaSpesa());
		Long liquidazioniCollegate = subdocumentoIvaSpesaDad.countLiquidazioniAssociateAQuoteDocumentoCollegato(req.getSubdocumentoIvaSpesa());
		
		res.setOrdinativiCollegati(ordinativiCollegati);
		res.setLiquidazioniCollegate(liquidazioniCollegate);
	}

}
