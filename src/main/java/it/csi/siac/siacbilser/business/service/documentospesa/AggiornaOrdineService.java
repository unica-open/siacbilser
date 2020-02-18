/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdineResponse;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * Aggiornamento dell'ordine.
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class AggiornaOrdineService extends CheckedAccountBaseService<AggiornaOrdine, AggiornaOrdineResponse> {
	
	@Autowired 
	private OrdineDad ordineDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getOrdine(), "Ordine");
		checkCondition(StringUtils.isNotBlank(req.getOrdine().getNumeroOrdine()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero ordine"));

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
	public AggiornaOrdineResponse executeService(AggiornaOrdine serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		Ordine ordine = ordineDad.aggiornaOrdine(req.getOrdine());
		res.setOrdine(ordine);
	}

	
}
