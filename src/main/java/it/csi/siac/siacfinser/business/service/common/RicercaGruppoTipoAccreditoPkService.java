/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaGruppoTipoAccreditoPkService extends AbstractBaseService<RicercaGruppoTipoAccreditoPerChiave, RicercaGruppoTipoAccreditoPerChiaveResponse>{


	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {}

	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaGruppoTipoAccreditoPerChiaveResponse executeService(RicercaGruppoTipoAccreditoPerChiave serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "RicercaGruppoTipoAccreditoPkService - execute()";
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer tipoUid = req.getTipoId();
		
		/*
		 *  utilizzato per la ricerca del gruppo tipo accredito, utilizzato ad es nelle 
		 *  modalita' di pagamento 
		 */
		
		res.setGruppoTipoAccredito(commonDad.findGruppoAccreditoByTipoId(ente.getUid(), tipoUid));
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		
	}
	
}
