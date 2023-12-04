/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.integration.dao.TipoProvvedimentoRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;



@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProvvedimentoFinDad extends BaseDadImpl {

	
	@Autowired
	TipoProvvedimentoRepository tipoProvvedimentoRepository;
	

	public TipoAtto getTipoAttoByCodice(Ente ente, String codice){
		
		TipoAtto tipoAtto = new TipoAtto();
		
		if(!StringUtils.isEmpty(codice)){
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = tipoProvvedimentoRepository.findTipoProvvedimentoByCodiceAndIdEnte(ente.getUid(), codice, now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				tipoAtto.setCodice(siacDAttoAmmTipoList.get(0).getAttoammTipoCode());
				tipoAtto.setUid(siacDAttoAmmTipoList.get(0).getAttoammTipoId());
			}else tipoAtto = null;
		}
	
	    return tipoAtto;
	}
}