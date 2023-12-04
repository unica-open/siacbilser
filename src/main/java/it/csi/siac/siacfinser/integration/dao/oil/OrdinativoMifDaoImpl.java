/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.oil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfin2ser.model.TipoOrdinativo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrdinativoMifDaoImpl extends NamedParameterJdbcDaoSupport implements OrdinativoMifDao
{
	@Autowired
	public OrdinativoMifDaoImpl(DataSource dataSource)
	{
		super();
		setDataSource(dataSource);
	}

	@Override
	public TipoOrdinativo getCodiceTipo(int idElaborazione)
	{
		String sql = "select * from mif_d_flusso_elaborato_tipo d, mif_t_flusso_elaborato t " +
				" where d.flusso_elab_mif_tipo_id=t.flusso_elab_mif_tipo_id "
				+ " and t.flusso_elab_mif_id=:flusso_elab_mif_id " 
				+ " and t.data_cancellazione is null and (t.validita_fine > now() or t.validita_fine is null)";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("flusso_elab_mif_id", idElaborazione);
				
		Map<String, Object> map = getNamedParameterJdbcTemplate().queryForMap(sql, params);
		
		String codiceTipo = (String) map.get("flusso_elab_mif_tipo_code");

		if ("REVMIF".equals(codiceTipo) || "REVMIF_SPLUS".equals(codiceTipo))
			return TipoOrdinativo.INCASSO;

		if ("MANDMIF".equals(codiceTipo) || "MANDMIF_SPLUS".equals(codiceTipo))
			return TipoOrdinativo.PAGAMENTO;
		
		throw new IllegalArgumentException("idTipo non valido " + codiceTipo);
	}

	@Override
	public List<Integer> getAnniEsercizioOrdinativiEntrata(int idElaborazione)
	{
		return getAnniEsercizio(idElaborazione, "mif_t_ordinativo_entrata");
	}

	@Override
	public List<Integer> getAnniEsercizioOrdinativiSpesa(int idElaborazione)
	{
		return getAnniEsercizio(idElaborazione, "mif_t_ordinativo_spesa");
	}

	@Override
	public List<String> getCodiciIstatAnnoEsercizioOrdinativiEntrata(int idElaborazione, Integer anno) {
		return getCodiciIstatAnnoEsercizio(idElaborazione, anno, "mif_t_ordinativo_entrata");
	}

	@Override
	public List<String> getCodiciIstatAnnoEsercizioOrdinativiSpesa(int idElaborazione, Integer anno) {
		return getCodiciIstatAnnoEsercizio(idElaborazione, anno, "mif_t_ordinativo_spesa");
	}
	
	@Override
	public int countOrdinativiEntrata(int idElaborazione)
	{
		return countOrdinativi(idElaborazione, "mif_t_ordinativo_entrata");
	}

	@Override
	public int countOrdinativiSpesa(int idElaborazione)
	{
		return countOrdinativi(idElaborazione, "mif_t_ordinativo_spesa");
	}
	
	@Override
	public int countOrdinativiAnnoEntrata(int idElaborazione, Integer anno)
	{
		return countOrdinativiAnno(idElaborazione, anno, "mif_t_ordinativo_entrata");
	}

	@Override
	public int countOrdinativiAnnoSpesa(int idElaborazione, Integer anno)
	{
		return countOrdinativiAnno(idElaborazione, anno, "mif_t_ordinativo_spesa");
	}
	
	@Override
	public int countOrdinativiAnnoCodiceIstatEntrata(int idElaborazione, Integer anno, String codiceIstat)
	{
		return countOrdinativiAnnoCodiceIstat(idElaborazione, anno, codiceIstat, "mif_t_ordinativo_entrata");
	}

	@Override
	public int countOrdinativiAnnoCodiceIstatSpesa(int idElaborazione, Integer anno, String codiceIstat)
	{
		return countOrdinativiAnnoCodiceIstat(idElaborazione, anno, codiceIstat, "mif_t_ordinativo_spesa");
	}
	
	private List<Integer> getAnniEsercizio(int idElaborazione, String tabella) 
	{
		String sql = "select distinct mif_ord_anno_esercizio from " + tabella 
				+ " where mif_ord_flusso_elab_mif_id=:mif_ord_flusso_elab_mif_id and "
				+ " data_cancellazione is null and validita_fine is null";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mif_ord_flusso_elab_mif_id", idElaborazione);
				
		return getNamedParameterJdbcTemplate().queryForList(sql, params, Integer.class);
	}
		
	private List<String> getCodiciIstatAnnoEsercizio(int idElaborazione, Integer anno, String tabella) 
	{
		String sql = "select distinct mif_ord_codice_ente_istat from " + tabella 
				+ " where mif_ord_flusso_elab_mif_id=:mif_ord_flusso_elab_mif_id and "
				+ " mif_ord_anno_esercizio=:mif_ord_anno_esercizio and "
				+ " data_cancellazione is null and validita_fine is null";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mif_ord_flusso_elab_mif_id", idElaborazione);
		params.put("mif_ord_anno_esercizio", anno);
				
		return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
	}
		
	private int countOrdinativi(int idElaborazione, String tabella) 
	{
		String sql = "select count(*) from " + tabella 
				+ " where mif_ord_flusso_elab_mif_id=:mif_ord_flusso_elab_mif_id and "
				+ " data_cancellazione is null and validita_fine is null";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mif_ord_flusso_elab_mif_id", idElaborazione);
				
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
	}
		
	private int countOrdinativiAnno(int idElaborazione, Integer anno, String tabella) 
	{
		String sql = "select count(*) from " + tabella 
				+ " where mif_ord_flusso_elab_mif_id=:mif_ord_flusso_elab_mif_id and "
				+ " mif_ord_anno_esercizio=:mif_ord_anno_esercizio and "
				+ " data_cancellazione is null and validita_fine is null";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mif_ord_flusso_elab_mif_id", idElaborazione);
		params.put("mif_ord_anno_esercizio", anno);
				
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
	}
		
	private int countOrdinativiAnnoCodiceIstat(int idElaborazione, Integer anno, String codiceIstat, String tabella) 
	{
		String sql = "select count(*) from " + tabella 
				+ " where mif_ord_flusso_elab_mif_id=:mif_ord_flusso_elab_mif_id and "
				+ " mif_ord_anno_esercizio=:mif_ord_anno_esercizio and "
				+ " mif_ord_codice_ente_istat=:mif_ord_codice_ente_istat and "
				+ " data_cancellazione is null and validita_fine is null";
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("mif_ord_flusso_elab_mif_id", idElaborazione);
		params.put("mif_ord_anno_esercizio", anno);
		params.put("mif_ord_codice_ente_istat", codiceIstat);
				
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
	}
		
	public List<Map<String, Object>> leggiStrutturaXml(int idElaborazione)
	{
		String sql = "select d.* from mif_d_flusso_elaborato d, mif_t_flusso_elaborato t "
				+ " where d.flusso_elab_mif_tipo_id=t.flusso_elab_mif_tipo_id " 
				+ " and t.flusso_elab_mif_id=:flusso_elab_mif_id "
				+ " and d.flusso_elab_mif_attivo " 
				+ " and t.data_cancellazione is null and (t.validita_fine > now() or t.validita_fine is null) "
				+ " order by d.flusso_elab_mif_ordine";

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("flusso_elab_mif_id", idElaborazione);

		return queryForList(sql, params);    
	}

	public List<Map<String, Object>> queryForList(String sql, Map<String, Object> params)
	{
		return getNamedParameterJdbcTemplate().queryForList(sql, params);
	}
}
