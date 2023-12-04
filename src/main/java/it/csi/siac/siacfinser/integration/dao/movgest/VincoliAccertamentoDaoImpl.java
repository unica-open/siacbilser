/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
@Transactional
public class VincoliAccertamentoDaoImpl extends JpaDao<VincoloAccertamento, SiacRMovgestTsFin> implements VincoliAccertamentiDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacRMovgestTsFin> findSiacRMovgestTsA(Integer uidAccertamento, Integer uidEnte) {
		
		StringBuilder jpql = new StringBuilder();
		List<SiacRMovgestTsFin> listaVincoli = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT srmt ")
			.append(" FROM SiacRMovgestTsFin srmt ")
			.append(" JOIN srmt.siacTEnteProprietario step ")
			.append(" JOIN srmt.siacTMovgestTsB impegnoTs ")
			.append(" JOIN impegnoTs.siacTMovgest impegno ")
//			.append(" JOIN impegnoTs.siacTMovgestTsDets impDet ")
//			.append(" JOIN impDet.siacDMovgestTsDetTipo impDetTipo ")
			.append(" JOIN impegno.siacTBil stb ")
			.append(" JOIN stb.siacTPeriodo stp ")
			.append(" JOIN srmt.siacTMovgestTsA accertamentoTs ")
			.append(" JOIN accertamentoTs.siacTMovgest accertamento ")
			.append(" WHERE step.enteProprietarioId = :uidEnte ")
			.append(" AND accertamento.movgestId = :uidAccertamento")
			.append(" AND impegnoTs.movgestTsIdPadre IS NULL ") // PER I VINCOLI CONSIDERO LA TESTATA
//			.append(" AND impDetTipo.movgestTsDetTipoCode = 'A' ") // IMPORTO ATTUALE
			.append(" AND srmt.dataCancellazione IS NULL ")
			.append(" AND ( srmt.dataFineValidita IS NULL OR srmt.dataFineValidita > :now ) ")
//			.append(" AND impDet.dataCancellazione IS NULL ")
//			.append(" AND ( impDet.dataFineValidita IS NULL OR impDet.dataFineValidita > :now ) ")
			;
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("uidEnte", uidEnte);
		param.put("now", new Date());
		
		Query query = createQuery(jpql.toString(), param);
		
		listaVincoli = (List<SiacRMovgestTsFin>) query.getResultList();
		
		return CoreUtil.checkList(listaVincoli);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacRMovgestTsFin> findSiacRMovgestTsAByAnno(Integer uidAccertamento, Integer annoBilancio, Integer uidEnte) {
		StringBuilder jpql = new StringBuilder();
		List<SiacRMovgestTsFin> listaVincoli = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT srmt ")
			.append(" FROM SiacRMovgestTsFin srmt ")
			.append(" JOIN srmt.siacTEnteProprietario step ")
			.append(" JOIN srmt.siacTMovgestTsB impegnoTs ")
			.append(" JOIN impegnoTs.siacTMovgest impegno ")
			.append(" JOIN impegno.siacTBil stb ")
			.append(" JOIN stb.siacTPeriodo stp ")
			.append(" JOIN srmt.siacTMovgestTsA accertamentoTs ")
			.append(" JOIN accertamentoTs.siacTMovgest accertamento ")
			.append(" WHERE step.enteProprietarioId = :uidEnte ")
			.append(" AND accertamento.movgestId = :uidAccertamento")
			.append(" AND stp.anno < :annoBilancio ")
			.append(" AND srmt.dataCancellazione IS NULL ")
			;
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("uidEnte", uidEnte);
		param.put("annoBilancio", annoBilancio.toString());
		
		Query query = createQuery(jpql.toString(), param);
		
		listaVincoli = (List<SiacRMovgestTsFin>) query.getResultList();
		
		return CoreUtil.checkList(listaVincoli);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacRMovgestTsFin> findSiacRMovgestTsAByAttr(Integer uidAccertamento, Integer uidEnte) {
		StringBuilder jpql = new StringBuilder();
		List<SiacRMovgestTsFin> listaVincoli = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT srmt ")
		.append(" FROM SiacRMovgestTsFin srmt ")
		.append(" JOIN srmt.siacTEnteProprietario step ")
		.append(" JOIN srmt.siacTMovgestTsB impegnoTs ")
		.append(" JOIN impegnoTs.siacTMovgest impegno ")
		.append(" JOIN impegno.siacTBil stb ")
		.append(" JOIN stb.siacTPeriodo stp ")
		.append(" JOIN srmt.siacTMovgestTsA accertamentoTs ")
		.append(" JOIN accertamentoTs.siacTMovgest accertamento ")
		.append(" WHERE step.enteProprietarioId = :uidEnte ")
		.append(" AND accertamento.movgestId = ( ")
			//TODO aggiungere subquery
		.append(" )	")
		.append(" AND srmt.dataCancellazione IS NULL ")
		;
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("uidEnte", uidEnte);
		
		Query query = createQuery(jpql.toString(), param);
		
		listaVincoli = (List<SiacRMovgestTsFin>) query.getResultList();
		
		return CoreUtil.checkList(listaVincoli);
	}

	@Override
	public BigDecimal sumImportoDeltaSiacRModificaVincoloByUidAccertamento(Integer uidAccertamento, Integer uidEnte) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT ABS(COALESCE(SUM(srmv.importoDelta), 0)) ")
			.append(" FROM SiacRModificaVincolo srmv ")
			.append(" JOIN srmv.siacRMovgestTs srmt ")
			.append(" JOIN srmt.siacTMovgestTsA accertamentoTs ")
			.append(" JOIN accertamentoTs.siacTMovgest accertamento ")
			.append(" JOIN srmv.siacTEnteProprietario step ")
			.append(" JOIN srmv.siacTmodifica stm ")
			.append(" JOIN stm.siacRModificaStatos srms ")
			.append(" JOIN srms.siacDModificaStato sdms ")
			.append(" JOIN stm.siacDModificaTipo sdmt ")
//			.append(" JOIN srms.siacTMovgestTsDetMods stmtdm ")
			.append(" WHERE step.enteProprietarioId = :uidEnte ")
			.append(" AND accertamento.movgestId = :uidAccertamento ")
			.append(" AND srmv.modvincTipoOperazione = 'INSERIMENTO' ")
			.append(" AND sdmt.modTipoCode IN ('REIMP', 'REANNO') ")
			.append(" AND sdms.modStatoCode <> 'A' ")
//			.append(" AND stmtdm.mtdmReimputazioneFlag = FALSE ")
//			.append(" AND stmtdm.mtdmReimputazioneAnno IS NULL ")
			.append(" AND srmv.dataCancellazione IS NULL ")
			.append(" AND ( srmv.dataFineValidita IS NULL OR srmv.dataFineValidita > :now ) ")
			.append(" AND srmt.dataCancellazione IS NULL ")
			.append(" AND ( srmt.dataFineValidita IS NULL OR srmt.dataFineValidita > :now ) ")
			.append(" AND srms.dataCancellazione IS NULL ")
			.append(" AND ( srms.dataFineValidita IS NULL OR srms.dataFineValidita > :now ) ")
			;
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("uidEnte", uidEnte);
		param.put("now", new Date());
		
		Query query = createQuery(jpql.toString(), param);
		
		return (BigDecimal) query.getSingleResult();
	}
	
	@Override
	public BigDecimal extractImportoModificheReimpReanno(Integer uidAccertamento, Integer uidEnte) {
		final String methodName = "extractImportoModificheReimpReanno";
		
		log.info(methodName, "Calling functionName: siac.fnc_siac_somma_importo_modifiche_reimp_reanno for"
				+ " uidAccertamento: " + uidAccertamento 
				+ ", uidEnte: " + uidEnte
				+ ", tipoMovimentoCode: " + "A"
				);
		
		String sql = "SELECT * FROM fnc_siac_somma_importo_modifiche_reimp_reanno(:uidAccertamento, :uidEnte, :tipoMovimento )";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidAccertamento", uidAccertamento);
		query.setParameter("uidEnte", uidEnte);
		query.setParameter("tipoMovimentoCode", "A");
		
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		if(result != null) {
			log.debug(methodName, "Somma importo modifiche di tipo [REIMP] e [REANNO]: " + result 
					+ " per Accertamento con uid: " + uidAccertamento);
		} else {
			log.debug(methodName, "Nessun importo modifiche [REIMP] o [REANNO] trovato per Accertamento"
					+ " con uid: " + uidAccertamento);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] extractEstremiRiaccertamento(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento) {
		
		//task-110
		String sql = " SELECT "
				+" sta.attr_code, "
				+" coalesce(srmta.\"boolean\",'N') AS attrBoolean, "  //-- Sofia
				+" ( case when coalesce(rattr_anno.testo,'') in ('0','','null') or rattr_anno.testo IS NULL then cast(stm.movgest_anno as varchar) else rattr_anno.testo end ) AS anno, "
				+" ( case when coalesce(rattr_num.testo,'') in ('0','','null') or rattr_num.testo IS NULL then cast(stm.movgest_numero as varchar) else rattr_num.testo end ) AS numero, "
				+" stp.anno AS bilancio "
				+" FROM siac_t_movgest stm "
				+" JOIN siac_t_ente_proprietario step ON stm.ente_proprietario_id = step.ente_proprietario_id "
				+" JOIN siac_d_movgest_tipo sdmt ON stm.movgest_tipo_id = sdmt.movgest_tipo_id "
				+" JOIN siac_t_bil stb ON stm.bil_id = stb.bil_id "
				+" JOIN siac_t_periodo stp ON stb.periodo_id = stp.periodo_id "
				+" JOIN siac_t_movgest_ts stmt ON stm.movgest_id = stmt.movgest_id "
				+" LEFT JOIN siac_r_movgest_ts_attr srmta " // -- Sofia - inizio 
				+" JOIN siac_t_attr sta ON (srmta.attr_id = sta.attr_id AND sta.attr_code IN ('flagDaReanno', 'flagDaRiaccertamento')) "
				+" ON ( srmta.movgest_ts_id = stmt.movgest_ts_id  AND srmta.data_cancellazione IS NULL ) "
				+" LEFT JOIN siac_r_movgest_ts_attr rattr_num " 
				+" JOIN siac_t_attr attr_num ON (attr_num.attr_id = rattr_num.attr_id AND attr_num.attr_code IN ('numeroRiaccertato')) "
				+" ON (rattr_num.movgest_ts_id = stmt.movgest_ts_id and rattr_num.data_cancellazione IS NULL) "
				+" LEFT JOIN siac_r_movgest_ts_attr rattr_anno "
				+" JOIN siac_t_attr attr_anno ON (attr_anno.attr_id = rattr_anno.attr_id AND attr_anno.attr_code IN ('annoRiaccertato')) "
				+" ON (rattr_anno.movgest_ts_id = stmt.movgest_ts_id and rattr_anno.data_cancellazione IS NULL) " //-- Sofia - fine 
				+" WHERE step.ente_proprietario_id = :uidEnte "
				+" AND cast(stp.anno as integer) <= :annoEsercizio "
				+" AND stm.movgest_anno = :annoMovimento " 
				+" AND stm.movgest_numero = :numeroMovimento " 
				+" AND sdmt.movgest_tipo_code = :tipoMovimento " 
				+" order by cast(stp.anno as integer) , coalesce(srmta.\"boolean\",'N') desc " //-- Sofia 
				+" limit 1 "
				;
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidEnte", uidEnte);
		query.setParameter("annoEsercizio", annoEsercizio);
		query.setParameter("annoMovimento", annoMovimento);
//		query.setParameter("annoEsercizioMenoUno", (annoEsercizio--).toString()); // PROBLEMA ESTRAZIONE REANNO-1
		query.setParameter("numeroMovimento", numeroMovimento);
		query.setParameter("tipoMovimento", tipoMovimento);
		
		List<Object[]> objs = query.getResultList();
		
		return objs == null || (objs != null && objs.size() == 0) ? new Object[0] : objs.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] extractEstremiMovimentoPadre(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento, String elemCode, String elemCode2) {
		
		String sql = " SELECT stbe.elem_code, stbe.elem_code2, stp.anno, stm.movgest_anno, stm.movgest_numero, sdmt.movgest_tipo_desc "
				+ " FROM siac_t_movgest stm  "
				+ " JOIN siac_t_bil stb ON stm.bil_id = stb.bil_id  "
				+ " JOIN siac_t_periodo stp ON stb.periodo_id = stp.periodo_id  "
				+ " JOIN siac_t_ente_proprietario step ON step.ente_proprietario_id = stm.ente_proprietario_id "
				+ " JOIN siac_d_movgest_tipo sdmt ON stm.movgest_tipo_id = sdmt.movgest_tipo_id  "
				+ " JOIN siac_r_movgest_bil_elem srmbe ON stm.movgest_id = srmbe.movgest_id "
				+ " JOIN siac_t_bil_elem stbe ON srmbe.elem_id = stbe.elem_id  "
				+ " WHERE step.ente_proprietario_id = :uidEnte "
				+ " AND stm.movgest_anno = :annoMovimento "
				+ " AND stm.movgest_numero = :numeroMovimento "
				+ " AND sdmt.movgest_tipo_code = :tipoMovimento "
				+ " AND stp.anno = :annoEsercizio "
				+ " AND stbe.elem_code = :elemCode "
				+ " AND stbe.elem_code2 = :elemCode2 "
				+ " AND srmbe.data_cancellazione IS NULL "
				+ " AND (srmbe.validita_fine IS NULL OR srmbe.validita_fine > :now ) "
				;
						
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("uidEnte", uidEnte);
		query.setParameter("annoEsercizio", annoEsercizio.toString());
		query.setParameter("annoMovimento", annoMovimento);
		query.setParameter("numeroMovimento", numeroMovimento);
		query.setParameter("tipoMovimento", tipoMovimento);
		query.setParameter("elemCode", elemCode);
		query.setParameter("elemCode2", elemCode2);
		query.setParameter("now", new Date());
		
		List<Object[]> objs = query.getResultList();
		
		return objs == null || (objs != null && objs.size() == 0) ? new Object[0] : objs.get(0);
	}

}
