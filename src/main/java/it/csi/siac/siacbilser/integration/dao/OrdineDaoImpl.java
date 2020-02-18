/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOrdine;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrdineDaoImpl extends JpaDao<SiacTOrdine, Integer> implements OrdineDao {
	
	public SiacTOrdine create(SiacTOrdine o){
		
		Date now = new Date();
		o.setDataModificaInserimento(now);
		
		if(o.getSiacRDocOrdines()!=null){
			for(SiacRDocOrdine doc : o.getSiacRDocOrdines()){
				doc.setDataModificaInserimento(now);
			}
		}

		o.setUid(null);		
		super.save(o);
		return o;
		
	}

	public SiacTOrdine update(SiacTOrdine o){
		
		SiacTOrdine oAttuale = this.findById(o.getUid());
				
		Date now = new Date();
		o.setDataModificaAggiornamento(now);		
		
		if(oAttuale.getSiacRDocOrdines()!=null){
			for(SiacRDocOrdine doc : oAttuale.getSiacRDocOrdines()){
				doc.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(o.getSiacRDocOrdines()!=null){
			for(SiacRDocOrdine doc : o.getSiacRDocOrdines()){
				doc.setDataModificaAggiornamento(now);
			}
		}
		
		super.update(o);
		return o;
	}
	
}
