/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifica;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificaResponse;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.model.TipoCodifica;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Servizio generico di ricerca di una sola codifiche.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCodificaService extends CheckedAccountBaseService<RicercaCodifica, RicercaCodificaResponse> {

	@Autowired
	private CodificaDad codificaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotBlank(req.getCodice(), "codice", false);
		checkNotNull(req.getTipoCodifica()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipi codifiche"));
		checkCondition(req.getTipoCodifica().getNomeCodifica()!=null || req.getTipoCodifica().getTipoCodifica()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("nome o tipo tipa codifica"));
	}
	
	@Override
	protected void init() {
		super.init();
		codificaDad.setEnte(ente);
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public RicercaCodificaResponse executeService(RicercaCodifica serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		TipoCodifica tipoCodifica = req.getTipoCodifica();
		Codifica codifica = codificaDad.ricercaCodifica(tipoCodifica, req.getCodice());
		res.setCodifica(codifica);
		
	}

}
