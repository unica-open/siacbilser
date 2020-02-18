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
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FindComunePerNomeService extends AbstractBaseService<ListaComunePerNome, ListaComunePerNomeResponse>{

	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		
		
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public ListaComunePerNomeResponse executeService(ListaComunePerNome serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "FindComunePerNomeService - execute()";
		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		/*
		 *  utilizzato per ajax
		 *  e' possibile ricerca per like o per nome puntuale andando a settare l'opportuno parametro di request : ricerca puntuale
		 */
		
		res.setListaComuni(commonDad.findComuneByDescrizione(req.getNomeComune(), req.getCodiceNazione(), req.isRicercaPuntuale(), ente.getUid()));
	}

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		
	}
}
