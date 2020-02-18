/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumentoResponse;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * Ricerca degli ordini legati ad un documento.
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class RicercaOrdiniDocumentoService extends CheckedAccountBaseService<RicercaOrdiniDocumento, RicercaOrdiniDocumentoResponse> {
	
	@Autowired 
	private OrdineDad ordineDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumento(), "documento spesa");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		ordineDad.setEnte(ente);
		ordineDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaOrdiniDocumentoResponse executeService(RicercaOrdiniDocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<Ordine> ordini = ordineDad.ricercaOrdiniDocumento(req.getDocumento());
		res.setOrdini(ordini);
	}

	
}
