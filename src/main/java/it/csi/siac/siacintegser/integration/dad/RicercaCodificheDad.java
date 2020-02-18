/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.integration.dad;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacintegser.integration.dao.StrutturaAmministrativaIntegRepository;
import it.csi.siac.siacintegser.integration.dao.TipoProvvedimentoIntegRepository;


@Deprecated // FIXME classi da includere nelle funzionalita' di BIL/FIN gia' esistenti, non devono fare parte di INTEG
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RicercaCodificheDad extends IntegBaseDad {


	
	@Autowired
	TipoProvvedimentoIntegRepository tipoProvvedimentoRepository;
	
	@Autowired
	StrutturaAmministrativaIntegRepository strutturaAmministrativaRepository;
	


	/**
	 * Metodo che si occupa di ricerca impegni e subimpegni
	 * @param ente
	 * @param codice tipo atto 
	 * @param now
	 * @return
	 */
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
	
	
	/**
	 * Metodo che si occupa di ricerca impegni e subimpegni
	 * @param ente
	 * @param codice sac
	 * @param now
	 * @return
	 */
	public StrutturaAmministrativoContabile getStrutturaByCodice(Ente ente, String codice, String codiceTipo){
		
		StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
	
		if(!StringUtils.isEmpty(codice)){
			Timestamp now = new Timestamp(System.currentTimeMillis());
			SiacTClassFin tClass = strutturaAmministrativaRepository.findStrutturaByCodiceAndIdEnte(ente.getUid(), codice, codiceTipo, now); 
			if(null!=tClass ){
				sac.setCodice(tClass.getClassifCode());
				sac.setDescrizione(tClass.getClassifDesc());
				sac.setUid(tClass.getUid());
			}
		}
	
        return sac;
	}


	
}