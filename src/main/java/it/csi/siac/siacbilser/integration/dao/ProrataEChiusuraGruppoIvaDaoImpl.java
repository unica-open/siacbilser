/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoProrata;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaProrata;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ProrataEChiusuraGruppoIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProrataEChiusuraGruppoIvaDaoImpl extends JpaDao<SiacTIvaProrata, Integer> implements ProrataEChiusuraGruppoIvaDao {
	
	public SiacTIvaProrata create(SiacTIvaProrata p){
		
		Date now = new Date();
		p.setDataModificaInserimento(now);
		
		
		if(p.getSiacRIvaGruppoProratas()!=null){
			for(SiacRIvaGruppoProrata gruppo : p.getSiacRIvaGruppoProratas()){
				gruppo.setDataModificaInserimento(now);
			}
		}
		
		p.setUid(null);	
		super.save(p);
		return p;
		
	}
	
	public SiacTIvaProrata update(SiacTIvaProrata p){
		Integer anno = null;
		for(SiacRIvaGruppoProrata srigp : p.getSiacRIvaGruppoProratas()) {
			anno = srigp.getIvagruproAnno();
		}
		
		SiacTIvaProrata pAttuale = this.findById(p.getUid());
		
		Date now = new Date();
		p.setDataModificaInserimento(now);
		
		if(pAttuale.getSiacRIvaGruppoProratas()!=null){
			for(SiacRIvaGruppoProrata gruppo : pAttuale.getSiacRIvaGruppoProratas()){
				if(anno == null || anno.equals(gruppo.getIvagruproAnno())) {
					gruppo.setDataCancellazioneIfNotSet(now);
				}
			}
		}
		
		if(p.getSiacRIvaGruppoProratas()!=null){
			for(SiacRIvaGruppoProrata gruppo : p.getSiacRIvaGruppoProratas()){
				gruppo.setDataModificaInserimento(now);
			}
		}
			
		super.update(p);
		return p;
		
	}
	

}
