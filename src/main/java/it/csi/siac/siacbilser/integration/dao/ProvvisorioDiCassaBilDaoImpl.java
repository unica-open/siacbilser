/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProvvisorioDiCassaBilDaoImpl extends JpaDao<SiacTProvCassa, Integer> implements ProvvisorioDiCassaBilDao {
	
	@Override
	public SiacTProvCassa findById(Integer id) {
		SiacTProvCassa siacTProvCassa = super.findById(id);
		return siacTProvCassa;
	}

	
	/**
	 * calcola l'importo da regolarizzare per un provvisorio di cassa
	 * tramite richiamo a function su database
	 * @param provcId id del provvisorio
	 * 
	 * @return result importo da regolarizzare del provvisorio
	 */
	public BigDecimal calcolaImportoDaRegolarizzare(Integer provcId){
		BigDecimal result  = BigDecimal.ZERO;
		String methodName = "calcolaImportoDaRegolarizzare";
		String functionName= "fnc_siac_daregolarizzareprovvisorio ";
		Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:provcId)");
		query.setParameter("provcId", provcId);		
		result = (BigDecimal) query.getSingleResult();
		log.debug(methodName, "Returning result: "+ result + " for provcId: "+ provcId + " and functionName: "+ functionName);
		return result;
	}

}
