/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControlloImportiImpegniVincolatiDaoImpl extends JpaDao<String, Integer> implements ControlloImportiImpegniVincolatiDao {


	

	public List<Object[]> controlloImportiImpegniVincolati(String listAttoAllId) {
		final String methodName = "controlloImportiImpegniVincolati";		
		log.debug(methodName, "Calling functionName:  fnc_siac_controllo_importo_impegni_vincolati  for uidCronoprogramma: "+ listAttoAllId );
		String sql = "SELECT * FROM fnc_siac_controllo_importo_impegni_vincolati (:listAttoAllId)";		
		Query query = entityManager.createNativeQuery(sql);		
		query.setParameter("listAttoAllId", listAttoAllId);		
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();
		return result;
	}

}
