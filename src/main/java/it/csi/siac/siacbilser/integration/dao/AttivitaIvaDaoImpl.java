/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttivitaIvaDaoImpl extends JpaDao<SiacTIvaAttivita,Integer> implements AttivitaIvaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.GruppoAttivitaIvaDao#ricercaSinteticaGruppoAttivitaIva(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTIvaAttivita> ricercaAttivitaIva(
			Integer enteProprietarioId, String ivaattCode, String ivaattDesc) {
		
		final String methodName = "ricercaSinteticaGruppoAttivitaIva";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaAttivitaIva( jpql, param, enteProprietarioId, ivaattCode, ivaattDesc);
		
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		
		Query query = createQuery(jpql.toString(), param);
		
		return query.getResultList();
				
	}
	
	/**
	 * Componi query ricerca sintetica gruppo attivita iva.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivagruproAnno the ivagrupro anno
	 * @param ivagruCode the ivagru code
	 * @param ivagruDesc the ivagru desc
	 * @param ivagruTipoCode the ivagru tipo code
	 */
	private void componiQueryRicercaAttivitaIva(StringBuilder jpql,
			Map<String, Object> param, Integer enteProprietarioId,
			String ivaattCode, String ivaattDesc) {	
		
		jpql.append("FROM SiacTIvaAttivita a ");
		jpql.append(" WHERE ");
		jpql.append(" a.dataCancellazione IS NULL ");
		jpql.append(" AND a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if( ivaattCode!=null && !StringUtils.isEmpty(ivaattCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("a.ivaattCode", "CONCAT('%', :ivaattCode, '%')") + " ");
			param.put("ivaattCode", ivaattCode);			
		}
		
		if(ivaattDesc!=null && !StringUtils.isEmpty(ivaattDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("a.ivaattDesc", "CONCAT('%', :ivaattDesc, '%')") + " ");
			param.put("ivaattDesc", ivaattDesc);			
		}
	
		jpql.append(" ORDER BY a.ivaattCode ");

	}	
	
	
	
}
