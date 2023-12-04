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

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiacDCassaEconOperazTipoImpl extends JpaDao<SiacDCassaEconOperazTipo, Integer> implements SiacDCassaEconOperazTipoDao {

	public List<SiacDCassaEconOperazTipo> findByCodiceEEnteECassaETipo(String cassaeconopTipoCode,
			Integer enteProprietarioId, Integer anno, Integer cassaeconId, String cassaeconopTipoEntrataspesa) {
		
		Query query = entityManager.createQuery(" FROM SiacDCassaEconOperazTipo toc "
				+ " WHERE toc.cassaeconopTipoCode = :cassaeconopTipoCode "
				+ " AND toc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND toc.dataCancellazione IS NULL "
				//" AND (toc.dataFineValidita IS NULL OR toc.dataFineValidita > CURRENT_TIMESTAMP) "
				+ getSiacTClassDataValiditaSql("toc", "anno") 
				// Lotto M
				+ "AND EXISTS ( "
				+ "     FROM toc.siacRCassaEconOperazTipoCassas rceotc "
				+ "     WHERE rceotc.dataCancellazione IS NULL "
				+ "     AND rceotc.siacTCassaEcon.cassaeconId = :cassaeconId "
				+ " ) "
				+ " AND toc.cassaeconopTipoEntrataspesa = :cassaeconopTipoEntrataspesa ");
		
		query.setParameter("cassaeconopTipoCode", cassaeconopTipoCode);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("cassaeconId", cassaeconId);
		query.setParameter("cassaeconopTipoEntrataspesa", cassaeconopTipoEntrataspesa);
		query.setParameter("anno", anno);
		
		@SuppressWarnings("unchecked")
		List<SiacDCassaEconOperazTipo> list = query.getResultList();
		
		return list;
	}
}
