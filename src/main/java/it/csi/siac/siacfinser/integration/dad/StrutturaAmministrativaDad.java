/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.integration.dao.StrutturaAmministrativaRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;



@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class StrutturaAmministrativaDad extends BaseDadImpl {

	
	@Autowired
	StrutturaAmministrativaRepository strutturaAmministrativaRepository;
	

	/**
	 * Metodo che si occupa di ricerca impegni e subimpegni
	 * @param ente
	 * @param codice sac
	 * @param now
	 * @return
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativaByCodice(Ente ente, String codice, String codiceTipo){
		
		StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
	
		if(!StringUtils.isEmpty(codice)){
			Timestamp now = new Timestamp(System.currentTimeMillis());
			SiacTClassFin tClass = strutturaAmministrativaRepository.findStrutturaByCodiceAndIdEnte(ente.getUid(), codice, codiceTipo, now); 
			if(null!=tClass ){
				sac.setCodice(tClass.getClassifCode());
				sac.setDescrizione(tClass.getClassifDesc());
				sac.setUid(tClass.getUid());
			}else return null;
		}
	
        return sac;
	}
	
	/**
	 * FIXME: per ora la commentiamo, sembra non esserci il requisito per il fruitore
	 * Metodo che ricerca il CDR di una sac di tipo CDC
	 * @param ente
	 * @param codice sac
	 * @param now
	 * @return
	 */
	/**
	public StrutturaAmministrativoContabile getStrutturaAmministrativaPadreByIdFiglio(Ente ente, String codiceCdc, String codiceTipoCdc){
		
		StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
	
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		SiacTClass tClass = strutturaAmministrativaRepository.findStrutturaAmministrativaPadreByIdFiglio(ente.getUid(), codiceCdc, codiceTipoCdc, now) ;
		if(null!=tClass ){
			sac.setCodice(tClass.getClassifCode());
			sac.setDescrizione(tClass.getClassifDesc());
			sac.setUid(tClass.getUid());
			
			TipoClassificatore tipoClassificatore = new TipoClassificatore();
			tipoClassificatore.setCodice(tClass.getSiacDClassTipo().getClassifTipoCode());
			sac.setTipoClassificatore(tipoClassificatore);
		}
		
	
        return sac;
	}*/
}