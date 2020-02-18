/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.saldo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoFin;

@Component
@Transactional
public class SaldoDaoImpl extends JpaDao<SiacTCbpiSaldoFin, Integer> implements SaldoDao
{
	@Override
	public BigDecimal leggiSommaImportiPredocumentiEntrataDataCompetenza(Integer idClassifConto, Date data, Integer anno, Integer idEnte)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ente_proprietario_id", idEnte);
		params.put("predoc_data_competenza", data);
		params.put("anno_competenza", anno);
		params.put("classif_id", idClassifConto);
		
		Query q = createNativeQuery(
					"SELECT COALESCE(SUM(p.predoc_importo), 0) "
						+ "FROM siac_t_predoc p, siac_r_predoc_class pc "
						+ "WHERE p.doc_fam_tipo_id = (SELECT d.doc_fam_tipo_id FROM siac_d_doc_fam_tipo d "
						+ "								WHERE d.doc_fam_tipo_code='E' "
						+ "                                AND d.ente_proprietario_id=:ente_proprietario_id "
						+ "                                AND d.data_cancellazione IS NULL "
						+ "                                AND d.validita_inizio <= now() "
						+ "                                AND (d.validita_fine IS NULL OR d.validita_fine > NOW())) "
						+ "AND p.predoc_data_competenza<=:predoc_data_competenza "
						+ "AND extract(year from p.predoc_data_competenza)>=:anno_competenza "
						+ "AND p.predoc_id=pc.predoc_id "
						+ "AND pc.classif_id=:classif_id "
						+ "AND p.ente_proprietario_id=:ente_proprietario_id "
						+ "AND p.data_cancellazione IS NULL "
						+ "AND p.validita_inizio <= now() "
						+ "AND (p.validita_fine IS NULL OR p.validita_fine > NOW())"				
				, params);
		
		return (BigDecimal) q.getSingleResult();
	}



	@Override
	public BigDecimal leggiSaldoSpesePrelievi(Integer idClassifConto, Date data, Integer anno, Integer idEnte)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ente_proprietario_id", idEnte);
		params.put("cbpisaldoa_data_addebito", data);
		params.put("anno", anno);
		params.put("classif_id", idClassifConto);
		
		Query q = createNativeQuery(
				"SELECT s.cbpisaldo_importo_iniziale "
						+ "	   - COALESCE(SUM(sa.cbpisaldoa_importo_spesa), 0) "
						+ "	   - COALESCE(SUM(sa.cbpisaldoa_importo_prelievo), 0) "
						+ "	FROM siac_t_cbpi_saldo s "
						+ " LEFT OUTER JOIN siac_t_cbpi_saldo_addebito sa "
						+ " ON sa.cbpisaldo_id=s.cbpisaldo_id "
						+ " AND sa.cbpisaldoa_data_addebito<=:cbpisaldoa_data_addebito "
						+ " AND extract(year from sa.cbpisaldoa_data_addebito)>=:anno "
						+ " WHERE s.classif_id=:classif_id "
						+ "	AND s.ente_proprietario_id=:ente_proprietario_id "
						+ "	AND s.data_cancellazione IS NULL "
						+ "	AND s.validita_inizio <= NOW() "
						+ "	AND (s.validita_fine IS NULL OR s.validita_fine > NOW()) "
						+ "    GROUP BY s.cbpisaldo_importo_iniziale"				
				, params);
		
		try
		{
			return (BigDecimal) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			return BigDecimal.ZERO;
		}
	}

}