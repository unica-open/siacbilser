/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


/**
 * The Interface MovimentoGestioneDao.
 */
@Component
@Transactional
public class MovimentoGestioneDaoImpl extends JpaDao<SiacTMovgest, Integer> implements MovimentoGestioneDao {
	
	@Override
	public BigDecimal calcolaDisponibilita(Integer uid, String functionName) {
		final String methodName = "calcolaDisponibilita";
		Query query = entityManager.createNativeQuery("SELECT " + functionName + "(:uid)");
		query.setParameter("uid", uid);
		
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.debug(methodName, "Returning result: "+ result + " for uid: "+ uid + " and functionName: "+ functionName);
		return result;
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRES_NEW)
	public BigDecimal calcolaDisponibilitaAttuale(Integer uid, String functionName) {
		final String methodName = "calcolaDisponibilita";
		Query query = entityManager.createNativeQuery("SELECT " + functionName + "(:uid)");
		query.setParameter("uid", uid);
		
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.debug(methodName, "Returning result: "+ result + " for uid: "+ uid + " and functionName: "+ functionName);
		return result;
	}
	
	@Override
	public BigDecimal calcolaTotaleImportoSubOrdinativiByMovgestTsId(Integer movgestTsId) {
		final String methodName = "calcolaTotaleImportoSubOrdinativiByMovgestTsId";
		StringBuilder sql = new StringBuilder()
			.append(" SELECT COALESCE(SUM(c.ord_ts_det_importo), 0) ")
			.append(" FROM ")
			.append("     siac_t_ordinativo a, ")
			.append("     siac_t_ordinativo_ts b, ")
			.append("     siac_t_ordinativo_ts_det c, ")
			.append("     siac_r_ordinativo_stato d, ")
			.append("     siac_d_ordinativo_stato e, ")
			.append("     siac_r_liquidazione_ord f, ")
			.append("     siac_r_liquidazione_stato g, ")
			.append("     siac_d_liquidazione_stato h, ")
			.append("     siac_r_liquidazione_movgest i, ")
			.append("     siac_d_ordinativo_ts_det_tipo l ")
			.append(" WHERE a.ord_id = b.ord_id ")
			.append(" AND c.ord_ts_id = b.ord_ts_id ")
			.append(" AND d.ord_id = a.ord_id ")
			.append(" AND d.ord_stato_id = e.ord_stato_id ")
			.append(" AND now() BETWEEN d.validita_inizio AND COALESCE (d.validita_fine, now()) ")
			.append(" AND e.ord_stato_code <> 'A' ")
			.append(" AND f.sord_id = b.ord_ts_id ")
			.append(" AND now() BETWEEN f.validita_inizio AND COALESCE (f.validita_fine, now()) ")
			.append(" AND g.liq_id = f.liq_id ")
			.append(" AND h.liq_stato_id = g.liq_stato_id ")
			.append(" AND now() BETWEEN g.validita_inizio AND COALESCE (g.validita_fine, now()) ")
			.append(" AND h.liq_stato_code = 'V' ")
			.append(" AND i.liq_id = f.liq_id ")
			.append(" AND now() BETWEEN i.validita_inizio AND COALESCE (i.validita_fine, now()) ")
			.append(" AND i.movgest_ts_id = :movgestTsId ")
			.append(" AND l.ord_ts_det_tipo_id = c.ord_ts_det_tipo_id ")
			.append(" AND l.ord_ts_det_tipo_code = 'A' ")
			.append(" AND a.data_cancellazione IS NULL ")
			.append(" AND b.data_cancellazione IS NULL ")
			.append(" AND c.data_cancellazione IS NULL ")
			.append(" AND d.data_cancellazione IS NULL ")
			.append(" AND e.data_cancellazione IS NULL ")
			.append(" AND f.data_cancellazione IS NULL ")
			.append(" AND g.data_cancellazione IS NULL ")
			.append(" AND h.data_cancellazione IS NULL ")
			.append(" AND i.data_cancellazione IS NULL ")
			.append(" AND l.data_cancellazione IS NULL ");
			
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("movgestTsId", movgestTsId);
		
		BigDecimal importoTotaleSubordinativi = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "importoTotaleSubordinativi [" + importoTotaleSubordinativi + "]");
		return importoTotaleSubordinativi;
	}
	
}
