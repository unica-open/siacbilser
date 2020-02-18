/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCronopElemBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElemDet;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


// TODO: Auto-generated Javadoc
/**
 * The Class DettaglioCronoprogrammaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioCronoprogrammaDaoImpl extends JpaDao<SiacTCronopElem, Integer> implements DettaglioCronoprogrammaDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DettaglioCronoprogrammaDao#create(it.csi.siac.siacbilser.integration.entity.SiacTCronopElem)
	 */
	public SiacTCronopElem create(SiacTCronopElem c) {
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		if(c.getSiacRCronopElemClasses()!=null){
			for(SiacRCronopElemClass classif : c.getSiacRCronopElemClasses()){
				classif.setDataModificaInserimento(now);
			}
		}

		if(c.getSiacRCronopElemBilElems()!=null){
			for(SiacRCronopElemBilElem rbilelem : c.getSiacRCronopElemBilElems()){
				rbilelem.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacTCronopElemDets()!=null){
			for(SiacTCronopElemDet elemDet : c.getSiacTCronopElemDets()){
				elemDet.setDataModificaInserimento(now);
			}
		}
		
		c.setUid(null);		
		super.save(c);
		return c;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTCronopElem update(SiacTCronopElem c) {
		
		SiacTCronopElem cAttuale = this.findById(c.getUid());
		
		Date now = new Date();
		c.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(cAttuale.getSiacRCronopElemClasses()!=null){
			for(SiacRCronopElemClass classif : cAttuale.getSiacRCronopElemClasses()){
				classif.setDataCancellazioneIfNotSet(now);
			}
		}

		if(cAttuale.getSiacRCronopElemBilElems()!=null){
			for(SiacRCronopElemBilElem rbilelem : cAttuale.getSiacRCronopElemBilElems()){
				rbilelem.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(cAttuale.getSiacTCronopElemDets()!=null){
			for(SiacTCronopElemDet elemDet : cAttuale.getSiacTCronopElemDets()){
				elemDet.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi
		if(c.getSiacRCronopElemClasses()!=null){
			for(SiacRCronopElemClass classif : c.getSiacRCronopElemClasses()){
				classif.setDataModificaInserimento(now);
			}
		}

		if(c.getSiacRCronopElemBilElems()!=null){
			for(SiacRCronopElemBilElem rbilelem : c.getSiacRCronopElemBilElems()){
				rbilelem.setDataModificaInserimento(now);
			}
		}
		
		if(c.getSiacTCronopElemDets()!=null){
			for(SiacTCronopElemDet elemDet : c.getSiacTCronopElemDets()){
				elemDet.setDataModificaInserimento(now);
			}
		}
		
		super.update(c);
		return c;
		
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	public void delete(SiacTCronopElem c) {
			
		Date now = new Date();
		c.setDataCancellazioneIfNotSet(now);		
		
		//cancellazione elementi collegati
		if(c.getSiacRCronopElemClasses()!=null){
			for(SiacRCronopElemClass classif : c.getSiacRCronopElemClasses()){
				classif.setDataCancellazioneIfNotSet(now);
			}
		}

		if(c.getSiacRCronopElemBilElems()!=null){
			for(SiacRCronopElemBilElem rbilelem : c.getSiacRCronopElemBilElems()){
				rbilelem.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(c.getSiacTCronopElemDets()!=null){
			for(SiacTCronopElemDet elemDet : c.getSiacTCronopElemDets()){
				elemDet.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		
		super.update(c);		
	}

	


}
