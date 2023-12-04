/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimentoResponse;
import it.csi.siac.siacfinser.integration.dad.ProvvedimentoFinDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoProvvedimentoService extends AbstractBaseService<RicercaTipoProvvedimento, RicercaTipoProvvedimentoResponse> {

	@Autowired
	ProvvedimentoFinDad attoAmministrativoDad;
	
	@Override
	protected void init() {
				
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaTipoProvvedimentoResponse executeService(RicercaTipoProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		
		TipoAtto tipoAtto = attoAmministrativoDad.getTipoAttoByCodice(req.getEnte(), req.getCodice());
		res.setTipoAtto(tipoAtto);
	}



}

