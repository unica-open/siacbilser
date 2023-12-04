/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CassaEconomaleDaoImpl extends JpaDao<SiacTCassaEcon, Integer> implements CassaEconomaleDao {
	
	public SiacTCassaEcon update(SiacTCassaEcon c){
		
		//SiacTCassaEcon cAttuale = this.findById(c.getUid());
		
		Date now = new Date();
		c.setDataModificaAggiornamento(now);	

		
//		//cancellazione elementi collegati		
//		if(cAttuale.getSiacRAccountRuoloOpCassaEcons()!=null){
//			for(SiacRAccountCassaEcon acc: cAttuale.getSiacRAccountRuoloOpCassaEcons()){
//				acc.setDataCancellazioneIfNotSet(now);
//			}
//		}
//		
//		//cancellazione elementi collegati		
//		if(cAttuale.getSiacRGruppoRuoloOpCassaEcons()!=null){
//			for(SiacRGruppoRuoloOpCassaEcon gr: cAttuale.getSiacRGruppoRuoloOpCassaEcons()){
//				gr.setDataCancellazioneIfNotSet(now);
//			}
//		}
//		
//		//inserimento elementi nuovi		
//		if(c.getSiacRAccountRuoloOpCassaEcons()!=null){
//			for(SiacRAccountCassaEcon acc : c.getSiacRAccountRuoloOpCassaEcons()){
//				acc.setDataModificaInserimento(now);
//			}
//		}
//		
//		//inserimento elementi nuovi		
//		if(c.getSiacRGruppoRuoloOpCassaEcons()!=null){
//			for(SiacRGruppoRuoloOpCassaEcon gr : c.getSiacRGruppoRuoloOpCassaEcons()){
//				gr.setDataModificaInserimento(now);
//			}
//		}
//		
		super.update(c);
		return c;
	}



	@Override
	public BigDecimal findImportoDerivato(Integer cassaeconId, Integer annoBilancio, String functionName) {
		final String methodName = "findImportoDerivato";
		if(annoBilancio == null){
			throw new IllegalArgumentException("Parametro anno obbligatorio per la function: "+functionName);
		}
		
		Query query = entityManager.createNativeQuery("SELECT "+ functionName + "(:cassaeconId, :annoBilancio)");
		query.setParameter("cassaeconId", cassaeconId);
		query.setParameter("annoBilancio", annoBilancio.toString()); //anno nella function e' varchar!	
		BigDecimal result = (BigDecimal) query.getSingleResult();
		log.debug(methodName, "Returning result: " + result + " for cassaeconId: " + cassaeconId + " annoBilancio: " + annoBilancio
				+ " and functionName: " + functionName);
		return result;
	}
	
	

}
