/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDoc;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class RegistroUnicoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistroUnicoDaoImpl extends JpaDao<SiacTRegistrounicoDoc, Integer> implements RegistroUnicoDao {
	
	public SiacTRegistrounicoDoc create(SiacTRegistrounicoDoc ru){
		Date now = new Date();
		ru.setDataModificaInserimento(now);
		
	
//		if(c.getSiacRDocStatos()!=null){
//			for(SiacRDocStato stato : c.getSiacRDocStatos()){
//				stato.setDataModificaInserimento(now);
//			}
//		}
		
		
		ru.setUid(null);		
		super.save(ru);
		return ru;
		
	}

	public SiacTRegistrounicoDoc update(SiacTRegistrounicoDoc ru){
//		SiacTRegistrounicoDoc ruAttuale = this.findById(ru.getUid());
		
		Date now = new Date();
		ru.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati		
//		if(ruAttuale.getSiacRDocAttrs()!=null){
//			for(SiacRDocAttr att : ruAttuale.getSiacRDocAttrs()){
//				att.setDataCancellazioneIfNotSet(now);
//			}
//		}

		
		//inserimento elementi nuovi		
//		if(ru.getSiacRDocAttrs()!=null){
//			for(SiacRDocAttr att : ru.getSiacRDocAttrs()){
//				att.setDataModificaInserimento(now);
//			}
//		}
		
		super.update(ru);
		return ru;
	}

	
	

}
