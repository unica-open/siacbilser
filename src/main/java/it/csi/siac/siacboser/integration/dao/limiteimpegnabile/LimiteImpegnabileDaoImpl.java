/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.integration.dao.limiteimpegnabile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LimiteImpegnabileDaoImpl extends NamedParameterJdbcDaoSupport implements LimiteImpegnabileDao
{
	@Autowired
	public LimiteImpegnabileDaoImpl(DataSource dataSource)
	{
		super();
		setDataSource(dataSource);
	}

	@Override
	public Integer leggiCapitolo(String numeroCapitolo, String numeroArticolo, String numeroUeb, String anno,
			Integer idEnte)
	{
		String sql = "SELECT elem_id FROM siac_t_bil_elem "
				+ " WHERE elem_code=:elem_code "
				+ " AND elem_code2=:elem_code2 "
				+ " AND elem_code3=COALESCE(:elem_code3, '1') "
				+ " AND ente_proprietario_id=:ente_proprietario_id "
				+ " AND elem_tipo_id= "
				+ "     (SELECT elem_tipo_id FROM siac_d_bil_elem_tipo "
				+ "	     WHERE ente_proprietario_id=:ente_proprietario_id "
				+ "          AND elem_tipo_code='CAP-UG' "
				+ "          AND data_cancellazione IS NULL "
				+ "          AND validita_inizio < NOW() "
				+ "          AND (validita_fine IS NULL OR validita_fine > NOW())) "
				+ " AND bil_id= "
				+ "    (SELECT b.bil_id FROM siac_t_bil b, siac_t_periodo p, siac_d_periodo_tipo pt "
				+ "    	WHERE b.ente_proprietario_id=:ente_proprietario_id "
				+ "        AND p.ente_proprietario_id=b.ente_proprietario_id "
				+ "        AND pt.ente_proprietario_id=p.ente_proprietario_id "
				+ "        AND pt.periodo_tipo_code='SY' "
				+ "        AND b.periodo_id=p.periodo_id "
				+ "        AND p.periodo_tipo_id=pt.periodo_tipo_id "
				+ "        AND p.anno=:anno "
				+ "        AND b.data_cancellazione IS NULL "
				+ "        AND b.validita_inizio < NOW() "
				+ "        AND (b.validita_fine IS NULL OR b.validita_fine > NOW()) "
				+ "        AND p.data_cancellazione IS NULL "
				+ "        AND p.validita_inizio < NOW() "
				+ "        AND (p.validita_fine IS NULL OR p.validita_fine > NOW()) "
				+ "        AND pt.data_cancellazione IS NULL "
				+ "        AND pt.validita_inizio < NOW() "
				+ "        AND (pt.validita_fine IS NULL OR pt.validita_fine > NOW())) "
				+ " AND data_cancellazione IS NULL "
				+ " AND validita_inizio < NOW() "
				+ " AND (validita_fine IS NULL OR validita_fine > NOW()) ";
		
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("elem_code", numeroCapitolo);
		paramMap.put("elem_code2", numeroArticolo);
		paramMap.put("elem_code3", numeroUeb);
		paramMap.put("anno", anno);
		paramMap.put("ente_proprietario_id", idEnte);
				
		try
		{
			return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

//	@Override
//	public Integer inserisciCapitolo(String numeroCapitolo, String numeroArticolo, String numeroUeb, String anno,
//			Integer idEnte, String loginOperazione)
//	{
//		String sql = "INSERT INTO siac_t_bil_elem "
//				+ "( elem_code, "
//				+ "  elem_code2, "
//				+ "  elem_code3, "
//				+ "  elem_desc, "
//				+ "  elem_tipo_id, "
//				+ "  bil_id, "
//				+ "  ordine, "
//				+ "  livello, "
//				+ "  ente_proprietario_id, "
//				+ "  login_operazione, "
//				+ "  validita_inizio) "
//				+ "  VALUES "
//				+ "  ( "
//				+ "    :elem_code, "
//				+ "    :elem_code2, "
//				+ "    COALESCE(:elem_code3, 1), "
//				+ "    '', "
//				+ "    (SELECT x.elem_tipo_id FROM siac_d_bil_elem_tipo x "
//				+ "    	WHERE x.ente_proprietario_id=:ente_proprietario_id "
//				+ "        AND x.elem_tipo_code='CAP-UG' "
//				+ "        AND x.data_cancellazione IS NULL "
//				+ "        AND x.validita_inizio < NOW() "
//				+ "        AND (x.validita_fine IS NULL OR x.validita_fine > NOW())), "
//				+ "    (SELECT b.bil_id FROM siac_t_bil b, siac_t_periodo p, siac_d_periodo_tipo pt "
//				+ "    	WHERE b.ente_proprietario_id=:ente_proprietario_id "
//				+ "        AND p.ente_proprietario_id=b.ente_proprietario_id "
//				+ "        AND pt.ente_proprietario_id=p.ente_proprietario_id "
//				+ "        AND pt.periodo_tipo_code='SY' "
//				+ "        AND b.periodo_id=p.periodo_id "
//				+ "        AND p.periodo_tipo_id=pt.periodo_tipo_id "
//				+ "        AND p.anno=:anno "
//				+ "        AND b.data_cancellazione IS NULL "
//				+ "        AND b.validita_inizio < NOW() "
//				+ "        AND (b.validita_fine IS NULL OR b.validita_fine > NOW()) "
//				+ "        AND p.data_cancellazione IS NULL "
//				+ "        AND p.validita_inizio < NOW() "
//				+ "        AND (p.validita_fine IS NULL OR p.validita_fine > NOW()) "
//				+ "        AND pt.data_cancellazione IS NULL "
//				+ "        AND pt.validita_inizio < NOW() "
//				+ "        AND (pt.validita_fine IS NULL OR pt.validita_fine > NOW())), "
//				+ "        '', "
//				+ "        0, "
//				+ "        :ente_proprietario_id, "
//				+ "        :login_operazione, "
//				+ "        NOW() "
//				+ "      );";
//		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//
//		paramMap.put("elem_code", numeroCapitolo);
//		paramMap.put("elem_code2", numeroArticolo);
//		paramMap.put("elem_code3", numeroUeb);
//		paramMap.put("anno", anno);
//		paramMap.put("ente_proprietario_id", idEnte);
//		paramMap.put("login_operazione", loginOperazione);
//				
//		getNamedParameterJdbcTemplate().update(sql, paramMap);
//		
//		return getNamedParameterJdbcTemplate().queryForInt("SELECT lastval()", (SqlParameterSource) null);
//	}

	@Override
	public Map<String, Object> leggiImportoCapitolo(Integer idCapitolo, String anno, String codiceTipoImporto, Integer idEnte)
	{
		String sql = "SELECT * FROM siac_t_bil_elem_det "
				+ " WHERE elem_id=:elem_id "
				+ " AND data_cancellazione IS NULL "
				+ " AND validita_inizio < NOW() "
				+ " AND (validita_fine IS NULL OR validita_fine > NOW()) "
				+ " AND periodo_id="
				+ "    (SELECT p.periodo_id FROM siac_t_periodo p, siac_d_periodo_tipo pt "
				+ "      WHERE p.ente_proprietario_id=:ente_proprietario_id "
				+ "      AND pt.ente_proprietario_id=p.ente_proprietario_id "
				+ "      AND p.periodo_tipo_id=pt.periodo_tipo_id "
				+ "      AND pt.periodo_tipo_code='SY' "
				+ "      AND p.anno=:anno "
				+ "      AND p.data_cancellazione IS NULL "
				+ "      AND p.validita_inizio < NOW() "
				+ "      AND (p.validita_fine IS NULL OR p.validita_fine > NOW()) "
				+ "      AND pt.data_cancellazione IS NULL "
				+ "      AND pt.validita_inizio < NOW() "
				+ "      AND (pt.validita_fine IS NULL OR pt.validita_fine > NOW())) "
			    + " AND elem_det_tipo_id="
				+ "    (SELECT x.elem_det_tipo_id FROM siac_d_bil_elem_det_tipo x "
				+ "      WHERE x.ente_proprietario_id=:ente_proprietario_id "
				+ "      AND x.elem_det_tipo_code=:codiceTipoImporto "
				+ "      AND x.data_cancellazione IS NULL "
				+ "      AND x.validita_inizio < NOW() "
				+ "      AND (x.validita_fine IS NULL OR x.validita_fine > NOW())) ";	
		
	
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("elem_id", idCapitolo);
		paramMap.put("anno", anno);
		paramMap.put("codiceTipoImporto", codiceTipoImporto);
		paramMap.put("ente_proprietario_id", idEnte);
		
		try
		{
			return getNamedParameterJdbcTemplate().queryForMap(sql, paramMap);
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}
	
	
	
	@Override
	public void inserisciImportoCapitolo(Integer idCapitolo, BigDecimal importo, String anno, Integer idEnte,
			String loginOperazione)
	{
		String sql = "INSERT INTO siac_t_bil_elem_det "
				+ "  ( "
				+ "    elem_id, "
				+ "    elem_det_importo , "
				+ "    elem_det_tipo_id, "
				+ "    periodo_id, "
				+ "    validita_inizio, "
				+ "    ente_proprietario_id, "
				+ "    login_operazione "
				+ "  ) "
				+ "  VALUES "
				+ "  ( "
				+ "    :elem_id, "
				+ "    :elem_det_importo, "
				+ "    (SELECT x.elem_det_tipo_id FROM siac_d_bil_elem_det_tipo x "
				+ "      WHERE x.ente_proprietario_id=:ente_proprietario_id "
				+ "      AND x.elem_det_tipo_code='MI' "
				+ "      AND x.data_cancellazione IS NULL "
				+ "      AND x.validita_inizio < NOW() "
				+ "      AND (x.validita_fine IS NULL OR x.validita_fine > NOW())), "
				+ "    (SELECT p.periodo_id FROM siac_t_periodo p, siac_d_periodo_tipo pt "
				+ "      WHERE p.ente_proprietario_id=:ente_proprietario_id "
				+ "      AND pt.ente_proprietario_id=p.ente_proprietario_id "
				+ "      AND p.periodo_tipo_id=pt.periodo_tipo_id "
				+ "      AND pt.periodo_tipo_code='SY' "
				+ "      AND p.anno=:anno "
				+ "      AND p.data_cancellazione IS NULL "
				+ "      AND p.validita_inizio < NOW() "
				+ "      AND (p.validita_fine IS NULL OR p.validita_fine > NOW()) "
				+ "      AND pt.data_cancellazione IS NULL "
				+ "      AND pt.validita_inizio < NOW() "
				+ "      AND (pt.validita_fine IS NULL OR pt.validita_fine > NOW())), "
				+ "	NOW(), "
				+ "    :ente_proprietario_id, "
				+ "    :login_operazione "
				+ "  );";	
		
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("elem_id", idCapitolo);
		paramMap.put("elem_det_importo", importo);
		paramMap.put("anno", anno);
		paramMap.put("ente_proprietario_id", idEnte);
		paramMap.put("login_operazione", loginOperazione);

		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void aggiornaImportoCapitolo(Integer idImportoCapitolo, BigDecimal importo, String loginOperazione)
	{
		String sql = "UPDATE siac_t_bil_elem_det "
				+ "    SET elem_det_importo=:elem_det_importo, "
				+ "    data_modifica=NOW(), "
				+ "    login_operazione=:login_operazione "
				+ " WHERE elem_det_id=:elem_det_id ";	
		
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("elem_det_id", idImportoCapitolo);
		paramMap.put("elem_det_importo", importo);
		paramMap.put("login_operazione", loginOperazione);
				
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
}
