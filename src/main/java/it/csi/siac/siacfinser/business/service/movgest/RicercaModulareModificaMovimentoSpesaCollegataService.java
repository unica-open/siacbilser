/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModulareModificaMovimentoSpesaCollegata;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModulareModificaMovimentoSpesaCollegataResponse;
import it.csi.siac.siacfinser.integration.dad.ModificaMovimentoGestioneSpesaCollegataDad;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegataFinModelDetail;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModulareModificaMovimentoSpesaCollegataService extends ExtendedBaseService<RicercaModulareModificaMovimentoSpesaCollegata, RicercaModulareModificaMovimentoSpesaCollegataResponse> {
	
	
	@Autowired
	private ModificaMovimentoGestioneSpesaCollegataDad modificaMovimentoGestioneSpesaCollegataDad;
	
	private Accertamento acc;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		acc = req.getAccertamento();
		checkNotNull(acc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento"));
		checkCondition(acc.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid accertamento"));
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaModulareModificaMovimentoSpesaCollegataResponse executeService(RicercaModulareModificaMovimentoSpesaCollegata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzo a default il model detail
		if(req.getModelDetails() == null) {
			req.setModelDetails(getAllModelDetailFromEnum());
		}
		
		//TODO utile in un servizio di sola lettura?
		modificaMovimentoGestioneSpesaCollegataDad.setLoginOperazione(req.getRichiedente().getAccount().getLoginOperazione());
	}
	
	@Override
	public void execute() {
		
		List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpesaCollegate = modificaMovimentoGestioneSpesaCollegataDad
				.ricercaModulareModificaMovimentoGestioneSpesaCollegata(req.getAccertamento(), req.getModifica(), false, req.getModelDetails());
		
		res.setListaModificheMovimentoGestioneSpesaCollegata(CollectionUtils.isNotEmpty(listaModificheSpesaCollegate) ? listaModificheSpesaCollegate : new ArrayList<ModificaMovimentoGestioneSpesaCollegata>());
	}
	
	private ModelDetailEnum[] getAllModelDetailFromEnum() {
		List<ModificaMovimentoGestioneSpesaCollegataFinModelDetail> list = new ArrayList<ModificaMovimentoGestioneSpesaCollegataFinModelDetail>(
			EnumSet.allOf(ModificaMovimentoGestioneSpesaCollegataFinModelDetail.class)
		);
		return list.toArray(new ModificaMovimentoGestioneSpesaCollegataFinModelDetail[list.size()]);
	}
}
