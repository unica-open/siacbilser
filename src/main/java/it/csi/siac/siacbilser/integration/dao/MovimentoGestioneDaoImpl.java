/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siaccommon.util.number.NumberUtil;
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
	
	//SIAC-7349 GM 13/07/2020
	@Override
	public BigDecimal calcolaTotaleImportoDeltaByModificaId(Integer modificaId) {
		final String methodName = "calcolaTotaleImportoDeltaByModificaId";
		//select  COALESCE(sum(abs(rvinc.importo_delta)), 0)
        //from siac_r_modifica_vincolo rvinc
        //where rvinc.mod_id=68711 
        //and   rvinc.modvinc_tipo_operazione='INSERIMENTO'
        //and   rvinc.data_cancellazione is null
        //and   rvinc.validita_fine is null;
		
		StringBuilder sql = new StringBuilder()
			.append(" SELECT COALESCE(SUM(abs(rvinc.importo_delta)), 0) ")
			.append(" FROM ")
			.append("     siac_r_modifica_vincolo rvinc ")
			.append(" WHERE rvinc.modvinc_tipo_operazione='INSERIMENTO' ")
			.append(" AND rvinc.data_cancellazione is null ")
			.append(" and rvinc.validita_fine is null ")
			.append(" and rvinc.mod_id = :modificaId ");
			
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("modificaId", modificaId);
		
		BigDecimal importoDeltaVincolo = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "importoDeltaVincolo [" + importoDeltaVincolo + "]");
		return importoDeltaVincolo;
	}

	@Override
	public Page<SiacTMovgest> ricercaMovimentiGestioneMutuo(Integer enteProprietarioId, Integer mutuoId,
			Integer movgestAnno, Integer movgestNumero, SiacDMovgestTipoEnum siacDMovgestTipoEnum, Integer annoBilancio,
			Integer elemId, Integer attoammId, Integer attoammAnno, Integer attoammNumero, Integer attoammTipoId,
			Integer attoammSacId, Integer soggettoId, Integer idTipoComponente, Pageable pageable) {
		
		final String methodName = "ricercaMovimentiGestioneMutuo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTMovgest p WHERE ");

		jpql.append(" p.siacDMovgestTipo.movgestTipoCode = :movgestTipoCode");
		param.put("movgestTipoCode", siacDMovgestTipoEnum.getCodice());
		
		jpql.append(" AND CAST(p.siacTBil.siacTPeriodo.anno AS integer) = :annoBilancio");
		param.put("annoBilancio", annoBilancio);
		
		jpql.append(getDateValiditaCancellazioneClauses("p")); 
		
		jpql.append(getEnteClause("p"));
		param.put("enteProprietarioId", enteProprietarioId);
		
		if (movgestAnno != null) {
			jpql.append(" AND p.movgestAnno = :movgestAnno");
			param.put("movgestAnno", movgestAnno);
		}
		
		if (movgestNumero != null) {
			jpql.append(" AND p.movgestNumero = :movgestNumero");
			param.put("movgestNumero", new BigDecimal(movgestNumero));
		}
		
		if (NumberUtil.isValidAndGreaterThanZero(elemId)) {
			jpql.append(" AND EXISTS (FROM SiacRMovgestBilElem rBilElem WHERE rBilElem.siacTMovgest.movgestId = p.movgestId AND rBilElem.siacTBilElem.elemId=:elemId ");
			jpql.append(getDateValiditaCancellazioneClauses("rBilElem"));
			jpql.append(" )");
			param.put("elemId", elemId);
		}
		
		if (NumberUtil.isValidAndGreaterThanZero(idTipoComponente)) {
			jpql.append(" AND EXISTS ( FROM SiacRMovgestBilElem rBilElem ");
			jpql.append(" WHERE rBilElem.siacTMovgest.movgestId = p.movgestId ");
			jpql.append(" AND rBilElem.siacDBilElemDetCompTipo.elemDetCompTipoId=:idTipoComponente ");
			jpql.append(getDateValiditaCancellazioneClauses("rBilElem"));
			jpql.append(" )");
			param.put("idTipoComponente", idTipoComponente);
		}
		
		jpql.append(" AND EXISTS (");
		jpql.append(" FROM p.siacTMovgestTs t JOIN t.siacRMovgestTsStatos r WHERE t.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ");
		jpql.append(" AND r.siacDMovgestStato.movgestStatoCode='D' " );
		jpql.append(getDateValiditaCancellazioneClauses("t"));
		jpql.append(getDateValiditaCancellazioneClauses("r"));

		if (NumberUtil.isValidAndGreaterThanZero(attoammId) ||
				NumberUtil.isValidAndGreaterThanZero(attoammAnno) ||
				NumberUtil.isValidAndGreaterThanZero(attoammNumero) ||
				NumberUtil.isValidAndGreaterThanZero(attoammTipoId) ||
				NumberUtil.isValidAndGreaterThanZero(attoammSacId)) {
			jpql.append(" AND EXISTS (FROM t.siacRMovgestTsAttoAmms rAtto WHERE 1=1 " );

			if (NumberUtil.isValidAndGreaterThanZero(attoammId)) {
				jpql.append(" AND rAtto.siacTAttoAmm.attoammId=:attoammId " );
				param.put("attoammId", attoammId);
			}			
			
			if (NumberUtil.isValidAndGreaterThanZero(attoammAnno)) {
				jpql.append(" AND rAtto.siacTAttoAmm.attoammAnno= CAST(:attoammAnno AS string) " );
				param.put("attoammAnno", attoammAnno);
			}
			
			if (NumberUtil.isValidAndGreaterThanZero(attoammNumero)) {
				jpql.append(" AND rAtto.siacTAttoAmm.attoammNumero=:attoammNumero " );
				param.put("attoammNumero", attoammNumero);
			}

			if (NumberUtil.isValidAndGreaterThanZero(attoammTipoId)) {
				jpql.append(" AND rAtto.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId " );
				param.put("attoammTipoId", attoammTipoId);
			}

			if (NumberUtil.isValidAndGreaterThanZero(attoammSacId)) {
				jpql.append(" AND EXISTS (FROM rAtto.siacTAttoAmm.siacRAttoAmmClasses raac WHERE raac.siacTClass.classifId =:attoammSacId " );
				jpql.append(getDateValiditaCancellazioneClauses("raac"));
				jpql.append(" )");
				param.put("attoammSacId", attoammSacId);
			}
			
			jpql.append(getDateValiditaCancellazioneClauses("rAtto"));
			jpql.append(" )");
		}
		
		if (NumberUtil.isValidAndGreaterThanZero(soggettoId)) {
			jpql.append(" AND EXISTS (FROM t.siacRMovgestTsSogs rSoggetto WHERE rSoggetto.siacTSoggetto.soggettoId=:soggettoId " );
			jpql.append(getDateValiditaCancellazioneClauses("rSoggetto"));
			jpql.append(" )");
			param.put("soggettoId", soggettoId);
		}
		
		jpql.append(" )");

		jpql.append(" ORDER BY p.movgestAnno, p.movgestNumero "); 
		
		String jpqlString = jpql.toString();
		log.debug(methodName, jpqlString);
		
		return getPagedList(jpqlString, param, pageable);		
		
	}
}
