/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.model.TipoCodifica;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Servizio generico di ricerca delle codifiche.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCodificheService extends CheckedAccountBaseService<RicercaCodifiche, RicercaCodificheResponse> {

	@Autowired
	private CodificaDad codificaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getLitaTipoCodifica()!=null && !req.getLitaTipoCodifica().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipi codifiche"));
		for(TipoCodifica tipoCodifica : req.getLitaTipoCodifica()){
			checkCondition(tipoCodifica.getNomeCodifica()!=null || tipoCodifica.getTipoCodifica()!=null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("nome o tipo tipi codifiche"));
			
		}
	}
	
	@Override
	protected void init() {
		super.init();
		codificaDad.setEnte(ente);
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public RicercaCodificheResponse executeService(RicercaCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		for(TipoCodifica tipoCodifica : req.getLitaTipoCodifica()) {
			List<? extends Codifica> codifiche = codificaDad.ricercaCodifiche(tipoCodifica);
			res.addCodifiche(codifiche);
		}
		
		res.setCardinalitaComplessiva(res.getCodifiche().size());

	}

}
