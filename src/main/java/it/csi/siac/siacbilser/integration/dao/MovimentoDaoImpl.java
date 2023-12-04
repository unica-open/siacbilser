/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;

/**
 * The Class MovimentoDaoImpl.
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MovimentoDaoImpl extends ExtendedJpaDao<SiacTMovimento, Integer> implements MovimentoDao {

	@Override
	public Page<SiacTMovimento> ricercaSinteticaMovimentoStampa(
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId,
			Pageable pageable) {
		final String methodName = "ricercaSinteticaMovimento";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		componiQueryNativaRicercaSinteticaMovimentoStampa(jpql, param, movtDataInizio, movtDataFine, cassaeconId, enteProprietarioId, bilId);
		
		jpql.append(" ORDER BY elem_code, elem_code2, elem_code3, movgest_anno, movgest_numero, movgest_ts_code, movt_numero ");
		
		log.info(methodName, "SQL to execute: " + jpql.toString());


		log.info(methodName,  cassaeconId);
		log.info(methodName,  enteProprietarioId);
		log.info(methodName,  bilId);

		
		// Ricavo gli uid, e poi le entities
		Page<Object[]> pagedIds = getNativePagedList(jpql.toString(), param, pageable, true);
		List<Integer> uids = new ArrayList<Integer>();
		for(Object[] objs : pagedIds) {
			uids.add((Integer)objs[0]);
		}
		return getPageByPks(uids, pageable, pagedIds.getTotalElements());
	}

	/**
	 * Composizione della query nativa per la ricerca sintetica del movimento per la stampa
	 * @param jpql il jpql da costruire
	 * @param param i parametri da injettare
	 * @param movtDataInizio la data di inizio dei movimenti
	 * @param movtDataFine la data di fine dei movimenti
	 * @param cassaeconId l'id della cassa economale
	 * @param enteProprietarioId l'id dell'ente
	 * @param bilId l'id del bilancio
	 */
	private void componiQueryNativaRicercaSinteticaMovimentoStampa(
			StringBuilder jpql,
			Map<String, Object> param,
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId) {
		// Compongo la query in nativo in quanto ci sono due rami distinti di cui tener traccia
		
		// Blocco giustificativo
		jpql.append(" SELECT tmo.movt_id, tmo.movt_numero, CAST(tbe.elem_code AS int), CAST(tbe.elem_code2 AS int), CAST(tbe.elem_code3 AS int), CAST(tm.movgest_anno AS int), CAST(tm.movgest_numero AS int), CAST(tmt.movgest_ts_code AS int) ");
		jpql.append(" FROM siac_t_movimento tmo ");
		jpql.append(" JOIN siac_r_richiesta_econ_movgest rrem ON (rrem.ricecon_id = tmo.ricecon_id AND rrem.data_cancellazione IS NULL) ");
		jpql.append(" JOIN siac_t_movgest_ts tmt ON tmt.movgest_ts_id = rrem.movgest_ts_id ");
		jpql.append(" JOIN siac_t_movgest tm ON tm.movgest_id = tmt.movgest_id ");
		jpql.append(" JOIN siac_r_movgest_bil_elem rmbe ON (tm.movgest_id = rmbe.movgest_id AND rmbe.data_cancellazione IS NULL) ");
		jpql.append(" JOIN siac_t_bil_elem tbe ON (rmbe.elem_id = tbe.elem_id AND tbe.data_cancellazione IS NULL) ");
		jpql.append(" JOIN siac_t_richiesta_econ tre ON tre.ricecon_id = tmo.ricecon_id ");
		jpql.append(" JOIN siac_t_cassa_econ tce ON tce.cassaecon_id = tre.cassaecon_id ");
		jpql.append(" WHERE tbe.ente_proprietario_id = :enteProprietarioId ");
		jpql.append(" AND date_trunc('day', cast(tmo.movt_data as date)) >= date_trunc('day', cast(:movtDataInizio as date)) ");
		jpql.append(" AND date_trunc('day', cast(tmo.movt_data as date)) <= date_trunc('day', cast(:movtDataFine as date)) ");
		jpql.append(" AND tmo.data_cancellazione is null ");
		jpql.append(" AND tre.bil_id = :bilId ");
		jpql.append(" AND tce.cassaecon_id = :cassaeconId ");
		
		jpql.append(" UNION ");
		
		// Blocco richiesta economale
		jpql.append(" SELECT tmo.movt_id, tmo.movt_numero, CAST(tbe.elem_code AS int), CAST(tbe.elem_code2 AS int), CAST(tbe.elem_code3 AS int), CAST(tm.movgest_anno AS int), CAST(tm.movgest_numero AS int), CAST(tmt.movgest_ts_code AS int) ");
		jpql.append(" FROM siac_t_movimento tmo ");
		jpql.append(" JOIN siac_r_giustificativo_movgest rgm ON rgm.gst_id = tmo.gst_id ");
		jpql.append(" JOIN siac_t_movgest_ts tmt ON tmt.movgest_ts_id = rgm.movgest_ts_id ");
		jpql.append(" JOIN siac_t_movgest tm ON tm.movgest_id=tmt.movgest_id ");
		jpql.append(" JOIN siac_r_movgest_bil_elem rmbe ON tm.movgest_id=rmbe.movgest_id ");
		jpql.append(" JOIN siac_t_bil_elem tbe ON rmbe.elem_id=tbe.elem_id ");
		jpql.append(" JOIN siac_t_giustificativo tg ON tg.gst_id=tmo.gst_id ");
		jpql.append(" JOIN siac_t_richiesta_econ tre ON tg.ricecon_id=tre.ricecon_id ");
		jpql.append(" JOIN siac_t_cassa_econ tce ON tce.cassaecon_id=tre.cassaecon_id ");
		jpql.append(" WHERE rmbe.data_cancellazione is null ");
		jpql.append(" AND tbe.data_cancellazione is null ");
		jpql.append(" AND tbe.ente_proprietario_id = :enteProprietarioId ");
		jpql.append(" AND rgm.data_cancellazione is null ");
		jpql.append(" AND date_trunc('day', cast(tmo.movt_data as date))>=date_trunc('day', cast(:movtDataInizio as date)) ");
		jpql.append(" AND date_trunc('day', cast(tmo.movt_data as date))<=date_trunc('day', cast(:movtDataFine as date)) ");
		jpql.append(" AND tmo.data_cancellazione is null ");
		jpql.append(" AND tre.bil_id = :bilId ");
		jpql.append(" AND tce.cassaecon_id = :cassaeconId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("movtDataInizio", movtDataInizio);
		param.put("movtDataFine", movtDataFine);
		param.put("cassaeconId", cassaeconId);
		param.put("bilId", bilId);
	}
	
	@Override
	public BigDecimal totaleRicercaSinteticaMovimentoStampa(
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId,
			List<String> codiciStatoDaEscludere) {
		final String methodName = "totaleRicercaSinteticaMovimento";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		componiQueryTotaleRicercaSinteticaMovimentoStampa(jpql, param, movtDataInizio, movtDataFine, cassaeconId, enteProprietarioId, bilId,codiciStatoDaEscludere);
		
		log.debug(methodName, "SQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		return (BigDecimal) query.getSingleResult();
	}
	
	private void componiQueryTotaleRicercaSinteticaMovimentoStampa(
			StringBuilder jpql,
			Map<String, Object> param,
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId,
			List<String> codiciStatoDaEscludere) {
		
		/*
		 * La colonna siac_t_movimento.ricecon_id e' sempre valorizzata, siac_t_movimento.gst_id puo' non esserlo.
		 * Effettuo pertanto una outer join per ottenere il valore del giustificativo qualora presente.
		 * Questo mi obbliga ad effettuare DUE coalesce: la prima per discriminare tra importo del giustificativo e importo della richiesta economale;
		 * la seconda serve per coprire il caso limite in cui non vi siano record.
		 */
		jpql.append(" SELECT COALESCE(SUM(COALESCE(tg.rendImportoIntegrato - tg.rendImportoRestituito, tmo.siacTRichiestaEcon.riceconImporto)), 0) ");
		jpql.append(" FROM SiacTMovimento tmo "); 
		jpql.append(" LEFT OUTER JOIN tmo.siacTGiustificativo tg ");
		jpql.append(" WHERE tmo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "); 
		jpql.append(" AND DATE_TRUNC('day', CAST(tmo.movtData AS date)) >= DATE_TRUNC('day', CAST(:movtDataInizio AS date)) "); 
		jpql.append(" AND DATE_TRUNC('day', CAST(tmo.movtData AS date)) <= DATE_TRUNC('day', CAST(:movtDataFine AS date)) "); 
		jpql.append(" AND tmo.dataCancellazione IS NULL "); 
		jpql.append(" AND tmo.siacTRichiestaEcon.siacTBil.bilId = :bilId "); 
		jpql.append(" AND tmo.siacTRichiestaEcon.siacTCassaEcon.cassaeconId = :cassaeconId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("movtDataInizio", movtDataInizio);
		param.put("movtDataFine", movtDataFine);
		param.put("cassaeconId", cassaeconId);
		param.put("bilId", bilId);
		
		//SIAC-6450
		if(codiciStatoDaEscludere != null && !codiciStatoDaEscludere.isEmpty()) {
			jpql.append(" AND NOT EXISTS( ");
			jpql.append(" FROM SiacRRichiestaEconStato rres ");
			jpql.append(" WHERE rres.dataCancellazione IS NULL ");
			jpql.append(" AND rres.siacTRichiestaEcon = tmo.siacTRichiestaEcon ");
			jpql.append(" AND rres.siacDRichiestaEconStato.riceconStatoCode IN ( :riceconStatoCodes ) ");
			jpql.append(" ) ");
			param.put("riceconStatoCodes", codiciStatoDaEscludere);
		}
	}
	
	
}
