/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromAttoAmmJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromBilElemBySacJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromBilElemJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromMovgestJpqlEnum;
import it.csi.siac.siacbilser.integration.dao.jpql.MovimentiFromMovgestTsJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPnDefAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacDPnProvAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovEpDetClass;
import it.csi.siac.siacbilser.integration.entity.SiacRPnDefAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPnProvAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnDefAccettazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnProvAccettazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * The Class PrimaNotaDaoImpl.
 * 
 * @author Valentina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PrimaNotaDaoImpl extends ExtendedJpaDao<SiacTPrimaNota, Integer> implements PrimaNotaDao {
	
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	@Autowired
	private EnumEntityFactory eef;
	
	@Override
	public SiacTPrimaNota create(SiacTPrimaNota e){
		Date now = new Date();
		
		e.setDataModificaInserimento(now);
		
		setDataModificaInserimento(e.getSiacRPrimaNotaStatos(), now);
		setDataModificaInserimento(e.getSiacRPrimaNotaFiglio(), now);
		setDataModificaInserimento(e.getSiacRPrimaNotaPadre(), now);
		setDataModificaInserimento(e.getSiacRGsaClassifPrimaNotas(), now);
		setDataModificaInserimento(e.getSiacRPnDefAccettazioneStatos(), now);
		setDataModificaInserimento(e.getSiacRPnProvAccettazioneStatos(), now);
		
		if(e.getSiacTMovEps() != null){
			for(SiacTMovEp mov : e.getSiacTMovEps()){
				mov.setDataModificaInserimento(now);
				mov.setUid(null);
				if(mov.getSiacTMovEpDets() != null){
					for(SiacTMovEpDet det : mov.getSiacTMovEpDets()){
						det.setDataModificaInserimento(now);
						det.setUid(null);
						
						// Annidamento incredibile!
						if(det.getSiacRMovEpDetClasses() != null) {
							for(SiacRMovEpDetClass classif : det.getSiacRMovEpDetClasses()) {
								classif.setDataModificaInserimento(now);
								classif.setUid(null);
							}
						}
					}
				}
			}
		}

		
		e.setUid(null);
		super.save(e);
		return e;
	}

	@Override
	public SiacTPrimaNota update(SiacTPrimaNota e){
		
		SiacTPrimaNota eAttuale = this.findById(e.getUid());
		Date now = new Date();
		e.setDataModificaAggiornamento(now);
		
		setDataCancellazione(eAttuale.getSiacRPrimaNotaStatos(), now);
		setDataCancellazione(eAttuale.getSiacRPrimaNotaFiglio(), now);
		setDataCancellazione(eAttuale.getSiacRPrimaNotaPadre(), now);
		setDataCancellazione(eAttuale.getSiacRGsaClassifPrimaNotas(), now);
		setDataCancellazione(e.getSiacRPnDefAccettazioneStatos(), now);
		setDataCancellazione(e.getSiacRPnProvAccettazioneStatos(), now);
		
		if(eAttuale.getSiacTMovEps() != null){
			for(SiacTMovEp mov : eAttuale.getSiacTMovEps()){
				mov.setDataCancellazioneIfNotSet(now);
				if(mov.getSiacTMovEpDets() != null){
					for(SiacTMovEpDet det : mov.getSiacTMovEpDets()){
						det.setDataCancellazioneIfNotSet(now);
						
						// Annidamento incredibile!
						if(det.getSiacRMovEpDetClasses() != null) {
							for(SiacRMovEpDetClass classif : det.getSiacRMovEpDetClasses()) {
								classif.setDataCancellazioneIfNotSet(now);
							}
						}
					}
				}
			}
		}
		
		setDataModificaInserimento(e.getSiacRPrimaNotaStatos(), now);
		setDataModificaInserimento(e.getSiacRPrimaNotaFiglio(), now);
		setDataModificaInserimento(e.getSiacRPrimaNotaPadre(), now);
		setDataModificaInserimento(e.getSiacRGsaClassifPrimaNotas(), now);
		setDataModificaInserimento(e.getSiacRPnDefAccettazioneStatos(), now);
		setDataModificaInserimento(e.getSiacRPnProvAccettazioneStatos(), now);
		
		if(e.getSiacTMovEps() != null){
			for(SiacTMovEp mov : e.getSiacTMovEps()){
				mov.setDataModificaInserimento(now);
				mov.setUid(null);
				if(mov.getSiacTMovEpDets() != null){
					for(SiacTMovEpDet det : mov.getSiacTMovEpDets()){
						det.setDataModificaInserimento(now);
						det.setUid(null);
						
						// Annidamento incredibile!
						if(det.getSiacRMovEpDetClasses() != null) {
							for(SiacRMovEpDetClass classif : det.getSiacRMovEpDetClasses()) {
								classif.setDataModificaInserimento(now);
								classif.setUid(null);
							}
						}
					}
				}
			}
		}
		
		super.update(e);
		return e;
	}

	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaPrimaNota(Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer pnotaNumero, 
			Integer pnotaProgressivogiornale, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, String pnotaDesc, Collection<Integer> eventoIds, Integer causaleEpId, Integer pdceContoId, Date dataRegistrazioneDa,
			Date dataRegistrazioneA, Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA,BigDecimal importo, Integer missioneId, Integer programmaId,
			Integer gsaClassifId, Integer cespiteId,Integer tipoEventoId,Pageable pageable) {
		
		final String methodName = "ricercaSinteticaPrimaNota";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaPrimaNota(jpql, param, enteProprietarioId, bilId, siacDAmbitoEnum, siacDCausaleEpTipoEnum, pnotaNumero, pnotaProgressivogiornale, siacDPrimaNotaStatoEnum, pnotaDesc,
				 eventoIds, causaleEpId, pdceContoId, null /*soggettoId*/, dataRegistrazioneDa, dataRegistrazioneA, importo, missioneId, programmaId, gsaClassifId,cespiteId, tipoEventoId);
		
		appendFilterDataRegistrazioneProvvisoriaDa(jpql, param, dataRegistrazioneProvvisoriaDa);
		appendFilterDataRegistrazioneProvvisoriaA(jpql, param, dataRegistrazioneProvvisoriaA);
		
		jpql.append(" ORDER BY p.pnotaNumero");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		log.debug(methodName, "PARAMS: " + param);
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	private void componiQueryRicercaSinteticaPrimaNotaBase(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer pnotaNumero, Integer pnotaProgressivogiornale, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, String pnotaDesc,
			Integer pdceContoId, Integer causaleEpId, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA, Integer gsaClassifId,Integer cespiteId,Integer tipoEventoId) {
		
		//log.info("componiQueryRicercaSinteticaPrimaNotaBase", "cespite Id " + cespiteId );
		
		jpql.append(" FROM SiacTPrimaNota p ");
		jpql.append(" WHERE p.dataCancellazione IS NULL ");
		jpql.append(" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND p.siacTBil.bilId = :bilId ");
		
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("bilId", bilId); //siamo sicuri!?!?
		
		// Aggiunta dei filtri
		appendFilterSiacDAmbitoEnum(jpql, param, siacDAmbitoEnum);
		appendFilterSiacDCausaleEpTipoEnum(jpql, param, siacDCausaleEpTipoEnum);
		appendFilterPnotaNumero(jpql, param, pnotaNumero);
		appendFilterPnotaNumeroRegistrazione(jpql, param, pnotaProgressivogiornale);
		appendFilterSiacDPrimaNotaStatoEnum(jpql, param, siacDPrimaNotaStatoEnum);
		appendFilterPnotaDesc(jpql, param, pnotaDesc);
		appendFilterSiacTPdceConto(jpql, param, pdceContoId);
		appendFilterSiacTCausaleEp(jpql, param, causaleEpId);
		appendFilterSiacTSoggetto(jpql, param, soggettoId);
		appendFilterDataRegistrazioneDa(jpql, param, dataRegistrazioneDa);
		appendFilterDataRegistrazioneA(jpql, param, dataRegistrazioneA);
		appendFilterSiacTGsaClassif(jpql, param, gsaClassifId);
		appendFilterSiacTCespite(jpql, param, cespiteId);
		//SIAC-6617
		appendFilterSiacDEventoTipo(jpql, param, tipoEventoId);
		
		
	}
	
	private void componiNativeQueryRicercaSinteticaPrimaNotaBase(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer pnotaNumero, Integer pnotaProgressivogiornale, Integer pnotaAnno, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, String pnotaDesc,
			Integer pdceContoId, Integer causaleEpId, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA) {
		
		jpql.append(" SELECT ");
		jpql.append("     DISTINCT p.* ");
		jpql.append(" FROM ");
		jpql.append("     siac_t_prima_nota p, ");
		jpql.append("     siac_d_ambito da, ");
		jpql.append("     siac_d_causale_ep_tipo cet, ");
		jpql.append("     siac_r_prima_nota_stato rpns, ");
		jpql.append("     siac_d_prima_nota_stato pns, ");
		jpql.append("     siac_t_mov_ep tme, ");
		jpql.append("     siac_r_evento_reg_movfin rerm, ");
		jpql.append("     siac_d_evento de, ");
		jpql.append("     siac_d_collegamento_tipo dct, ");
		jpql.append("	  siac_t_bil bil, ");//SIAC-8595
		jpql.append("	  siac_t_periodo periodo ");//SIAC-8595
		jpql.append(" WHERE p.data_cancellazione IS NULL ");
		jpql.append(" AND da.data_cancellazione IS NULL ");
		jpql.append(" AND cet.data_cancellazione IS NULL ");
		jpql.append(" AND rpns.data_cancellazione IS NULL ");
		jpql.append(" AND pns.data_cancellazione IS NULL ");
		jpql.append(" AND tme.data_cancellazione IS NULL ");
		jpql.append(" AND rerm.data_cancellazione IS NULL ");
		jpql.append(" AND de.data_cancellazione IS NULL ");
		jpql.append(" AND dct.data_cancellazione IS NULL ");
		
		
		//Join
		jpql.append(" AND p.ambito_id = da.ambito_id ");
		jpql.append(" AND p.causale_ep_tipo_id = cet.causale_ep_tipo_id ");
		jpql.append(" AND rpns.pnota_id = p.pnota_id ");
		jpql.append(" AND rpns.pnota_stato_id = pns.pnota_stato_id ");
		jpql.append(" AND p.pnota_id = tme.regep_id ");
		jpql.append(" AND rerm.regmovfin_id = tme.regmovfin_id ");
		jpql.append(" AND rerm.evento_id = de.evento_id ");
		jpql.append(" AND de.collegamento_tipo_id = dct.collegamento_tipo_id ");
		jpql.append(" AND bil.bil_id = p.bil_id ");//SIAC-8595
		jpql.append(" AND periodo.periodo_id = bil.periodo_id ");//SIAC-8595
		
		//Base Filter
		jpql.append(" AND p.ente_proprietario_id = :enteProprietarioId ");
		jpql.append(" AND p.bil_id = :bilId ");
		
		
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("bilId", bilId);
		
		//Aggiunta dei filtri opzionali
		appendNativeFilterSiacDAmbitoEnum(jpql, param, siacDAmbitoEnum);
		appendNativeFilterSiacDCausaleEpTipoEnum(jpql, param, siacDCausaleEpTipoEnum);
		appendNativeFilterPnotaNumero(jpql, param, pnotaNumero);
		appendNativeFilterPnotaNumeroRegistrazione(jpql, param, pnotaProgressivogiornale);
		appendNativeFilterPnotaAnno(jpql, param, pnotaAnno);
		appendNativeFilterSiacDPrimaNotaStatoEnum(jpql, param, siacDPrimaNotaStatoEnum);
		appendNativeFilterPnotaDesc(jpql, param, pnotaDesc);
		appendNativeFilterSiacTPdceConto(jpql, param, pdceContoId);
		appendNativeFilterSiacTCausaleEp(jpql, param, causaleEpId);
		appendNativeFilterSiacTSoggetto(jpql, param, soggettoId);
		appendNativeFilterDataRegistrazioneDa(jpql, param, dataRegistrazioneDa);
		appendNativeFilterDataRegistrazioneA(jpql, param, dataRegistrazioneA);
		
		
	}
	
	private void appendFilterSiacDAmbitoEnum(StringBuilder jpql, Map<String, Object> param, SiacDAmbitoEnum siacDAmbitoEnum) {
		if(siacDAmbitoEnum != null){
			jpql.append(" AND p.siacDAmbito.ambitoCode = :ambitoCode ");
			param.put("ambitoCode", siacDAmbitoEnum.getCodice());
		}
	}
	
	private void appendNativeFilterSiacDAmbitoEnum(StringBuilder sql, Map<String, Object> param, SiacDAmbitoEnum siacDAmbitoEnum) {
		if(siacDAmbitoEnum != null){
			sql.append(" AND da.ambito_code = :ambitoCode ");
			param.put("ambitoCode", siacDAmbitoEnum.getCodice());
		}
	}

	private void appendFilterDataRegistrazioneA(StringBuilder jpql,Map<String, Object> param, Date dataRegistrazioneA) {
		if(dataRegistrazioneA != null){
			jpql.append(" AND DATE_TRUNC('day', CAST(p.pnotaDataregistrazionegiornale AS date)) <= DATE_TRUNC('day', CAST(:dataRegistrazioneA AS date)) ");
			param.put("dataRegistrazioneA", dataRegistrazioneA);
		}
	}
	
	private void appendNativeFilterDataRegistrazioneA(StringBuilder sql, Map<String, Object> param, Date dataRegistrazioneA) {
		if(dataRegistrazioneA != null){
			sql.append(" AND DATE_TRUNC('day', CAST(p.pnota_dataregistrazionegiornale AS date)) <= DATE_TRUNC('day', CAST(:dataRegistrazioneA AS date)) ");
			param.put("dataRegistrazioneA", dataRegistrazioneA);
		}
	}

	private void appendFilterDataRegistrazioneDa(StringBuilder jpql,Map<String, Object> param, Date dataRegistrazioneDa) {
		if(dataRegistrazioneDa != null){
			jpql.append(" AND DATE_TRUNC('day', CAST(p.pnotaDataregistrazionegiornale AS date)) >= DATE_TRUNC('day', CAST(:dataRegistrazioneDa AS date)) ");
			param.put("dataRegistrazioneDa", dataRegistrazioneDa);
		}
	}
	
	private void appendNativeFilterDataRegistrazioneDa(StringBuilder sql, Map<String, Object> param, Date dataRegistrazioneDa) {
		if(dataRegistrazioneDa != null){
			sql.append(" AND DATE_TRUNC('day', CAST(p.pnota_dataregistrazionegiornale AS date)) >= DATE_TRUNC('day', CAST(:dataRegistrazioneDa AS date)) ");
			param.put("dataRegistrazioneDa", dataRegistrazioneDa);
		}
	}
	
	private void appendFilterDataRegistrazioneProvvisoriaA(StringBuilder jpql,Map<String, Object> param, Date dataRegistrazioneProvvisoriaA) {
		if(dataRegistrazioneProvvisoriaA != null){
			jpql.append(" AND DATE_TRUNC('day', CAST(p.pnotaData AS date)) <= DATE_TRUNC('day', CAST(:pnotaDataA AS date)) ");
			param.put("pnotaDataA", dataRegistrazioneProvvisoriaA);
		}
	}
	
	private void appendNativeFilterDataRegistrazioneProvvisoriaA(StringBuilder sql, Map<String, Object> param, Date dataRegistrazioneProvvisoriaA) {
		if(dataRegistrazioneProvvisoriaA != null){
			sql.append(" AND DATE_TRUNC('day', CAST(p.pnota_data AS date)) <= DATE_TRUNC('day', CAST(:pnotaDataA AS date)) ");
			param.put("pnotaDataA", dataRegistrazioneProvvisoriaA);
		}
	}

	private void appendFilterDataRegistrazioneProvvisoriaDa(StringBuilder jpql,Map<String, Object> param, Date dataRegistrazioneProvvisoriaDa) {
		if(dataRegistrazioneProvvisoriaDa != null){
			jpql.append(" AND DATE_TRUNC('day', CAST(p.pnotaData AS date)) >= DATE_TRUNC('day', CAST(:pnotaDataDa AS date)) ");
			param.put("pnotaDataDa", dataRegistrazioneProvvisoriaDa);
		}
	}
	
	private void appendNativeFilterDataRegistrazioneProvvisoriaDa(StringBuilder sql,Map<String, Object> param, Date dataRegistrazioneProvvisoriaDa) {
		if(dataRegistrazioneProvvisoriaDa != null){
			sql.append(" AND DATE_TRUNC('day', CAST(p.pnota_data AS date)) >= DATE_TRUNC('day', CAST(:pnotaDataDa AS date)) ");
			param.put("pnotaDataDa", dataRegistrazioneProvvisoriaDa);
		}
	}

	private void appendFilterSiacTPdceConto(StringBuilder jpql, Map<String, Object> param, Integer pdceContoId) {
		if(pdceContoId != null && pdceContoId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTMovEpDets det ");
			jpql.append("         WHERE det.dataCancellazione IS NULL ");
			jpql.append("         AND det.siacTPdceConto.pdceContoId = :pdceContoId ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("pdceContoId", pdceContoId);
		}
	}
	
	private void appendNativeFilterSiacTPdceConto(StringBuilder sql, Map<String, Object> param, Integer pdceContoId) {
		if(pdceContoId != null && pdceContoId != 0){
			sql.append(" AND EXISTS ( ");
			sql.append("     SELECT 1 FROM siac_t_mov_ep_det det ");
			sql.append("     WHERE  ");
			sql.append("     det.data_cancellazione IS NULL ");
			sql.append("     AND det.movep_id = tme.movep_id ");
			sql.append("     AND det.pdce_conto_Id = :pdceContoId ");
			sql.append(" ) ");
			
			param.put("pdceContoId", pdceContoId);
		}
	}

	private void appendFilterPnotaDesc(StringBuilder jpql, Map<String, Object> param, String pnotaDesc) {
		if(StringUtils.isNotBlank(pnotaDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("p.pnotaDesc ", "CONCAT('%', :pnotaDesc, '%')") + " ");
			param.put("pnotaDesc", pnotaDesc);
		}
	}
	
	private void appendNativeFilterPnotaDesc(StringBuilder sql, Map<String, Object> param, String pnotaDesc) {
		if(StringUtils.isNotBlank(pnotaDesc)){
			sql.append(" AND " + Utility.toJpqlSearchLike("p.pnota_desc ", "CONCAT('%', :pnotaDesc, '%')") + " ");
			param.put("pnotaDesc", pnotaDesc);
		}
	}

	private void appendFilterSiacDPrimaNotaStatoEnum(StringBuilder jpql, Map<String, Object> param, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum) {
		if(siacDPrimaNotaStatoEnum != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacRPrimaNotaStatos ps ");
			jpql.append("     WHERE ps.dataCancellazione IS NULL ");
			jpql.append("     AND ps.siacDPrimaNotaStato.pnotaStatoCode = :pnotaStatoCode ");
			jpql.append(" ) ");
			param.put("pnotaStatoCode", siacDPrimaNotaStatoEnum.getCodice());
		}
	}
	
	//SIAC-6564
	private void appendFilterSiacDPnDefAccettazioneStatoEnums(StringBuilder jpql, Map<String, Object> param, Collection<String> siacDPnDefAccettazioneStatoEnums) {
		if(siacDPnDefAccettazioneStatoEnums != null && !siacDPnDefAccettazioneStatoEnums.isEmpty()){			
			jpql.append(" AND ( ");			
				jpql.append(" 	EXISTS ( ");
				jpql.append("     FROM SiacRPrimaNota rpn, SiacRPnDefAccettazioneStato ps ");  
				jpql.append("     WHERE rpn.dataCancellazione IS NULL ");
				jpql.append("     AND   rpn.siacTPrimaNotaPadre = p ");
				jpql.append("     AND   rpn.siacTPrimaNotaFiglio = ps.siacTPrimaNota ");
				jpql.append("     AND   ps.dataCancellazione IS NULL ");			
				jpql.append("     AND ps.siacDPnDefAccettazioneStato.pnStaAccDefCode IN (:accettazioneStatoCodes) "); 
				jpql.append("    ) ");		
				
				if (siacDPnDefAccettazioneStatoEnums.contains(SiacDPnDefAccettazioneStatoEnum.DaAccettare.getCodice())){
					//jpql.append(" OR p.siacRPnDefAccettazioneStatos IS NULL ");
					jpql.append(" 	OR ");
					jpql.append(" 	NOT EXISTS ( ");
					jpql.append("     SELECT 1 ");
					jpql.append("     FROM SiacRPrimaNota rpn, SiacRPnDefAccettazioneStato ps ");  
					jpql.append("     WHERE rpn.dataCancellazione IS NULL ");
					jpql.append("     AND   rpn.siacTPrimaNotaPadre = p ");
					jpql.append("     AND   rpn.siacTPrimaNotaFiglio = ps.siacTPrimaNota ");
					jpql.append("     AND   ps.dataCancellazione IS NULL ");			
					jpql.append("    ) ");
				}
				
			jpql.append(" ) ");
			param.put("accettazioneStatoCodes", siacDPnDefAccettazioneStatoEnums);
		}
	}
	
	              
	private void appendFilterSiacDPrimaNotaStatoEnums(StringBuilder jpql, Map<String, Object> param, Collection<String> siacDPrimaNotaStatoEnums) {
		if(siacDPrimaNotaStatoEnums != null && !siacDPrimaNotaStatoEnums.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacRPrimaNotaStatos ps ");
			jpql.append("     WHERE ps.dataCancellazione IS NULL ");
			jpql.append("     AND ps.siacDPrimaNotaStato.pnotaStatoCode IN (:pnotaStatoCodes) ");
			jpql.append(" ) ");
			
			param.put("pnotaStatoCodes", siacDPrimaNotaStatoEnums);
		}
	}

	
	private void appendNativeFilterSiacDPrimaNotaStatoEnum(StringBuilder sql, Map<String, Object> param, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum) {
		if(siacDPrimaNotaStatoEnum != null){
			sql.append(" AND pns.pnota_stato_code = :pnotaStatoCode ");
			param.put("pnotaStatoCode", siacDPrimaNotaStatoEnum.getCodice());
		}
	}
	
	private void appendNativeFilterSiacDPrimaNotaStatoEnums(StringBuilder sql, Map<String, Object> param, Collection<String> siacDPrimaNotaStatoEnums) {
		if(siacDPrimaNotaStatoEnums != null && !siacDPrimaNotaStatoEnums.isEmpty()){
			sql.append(" AND pns.pnota_stato_code IN (:pnotaStatoCode) ");
			param.put("pnotaStatoCode", siacDPrimaNotaStatoEnums);
		}
	}
	
	//SIAC-6564
	private void appendNativeFilterSiacDPnDefAccettazioneStatoEnums(StringBuilder sql, Map<String, Object> param, Collection<String> siacDPnDefAccettazioneStatoEnums) {
		if(siacDPnDefAccettazioneStatoEnums != null && !siacDPnDefAccettazioneStatoEnums.isEmpty()){						
			sql.append(" AND ( ");
			sql.append(" dpdas.pn_sta_acc_def_code IN (:pnotaAccettazioneStatoCode) ");
				if (siacDPnDefAccettazioneStatoEnums.contains(SiacDPnDefAccettazioneStatoEnum.DaAccettare.getCodice())){
					sql.append(" OR dpdas.pn_sta_acc_def_code IS NULL ");
				}
			sql.append(") ");
			param.put("pnotaAccettazioneStatoCode", siacDPnDefAccettazioneStatoEnums);
		}
	}
	
	private void appendFilterPnotaNumero(StringBuilder jpql, Map<String, Object> param, Integer pnotaNumero) {
		if(pnotaNumero != null && pnotaNumero != 0){
			jpql.append(" AND  p.pnotaNumero = :pnotaNumero ");
			param.put("pnotaNumero", pnotaNumero);
		}
	}
	
	private void appendNativeFilterPnotaNumero(StringBuilder jpql, Map<String, Object> param, Integer pnotaNumero) {
		if(pnotaNumero != null && pnotaNumero != 0){
			jpql.append(" AND  p.pnota_numero = :pnotaNumero ");
			param.put("pnotaNumero", pnotaNumero);
		}
	}
	
	private void appendFilterPnotaNumeroRegistrazione(StringBuilder jpql, Map<String, Object> param, Integer pnotaProgressivogiornale) {
		if(pnotaProgressivogiornale != null && pnotaProgressivogiornale != 0){
			jpql.append(" AND  p.pnotaProgressivogiornale = :pnotaProgressivogiornale ");
			param.put("pnotaProgressivogiornale", pnotaProgressivogiornale);
		}
	}
	
	private void appendNativeFilterPnotaNumeroRegistrazione(StringBuilder jpql, Map<String, Object> param, Integer pnotaProgressivogiornale) {
		if(pnotaProgressivogiornale != null && pnotaProgressivogiornale != 0){
			jpql.append(" AND  p.pnota_progressivogiornale = :pnotaProgressivogiornale ");
			param.put("pnotaProgressivogiornale", pnotaProgressivogiornale);
		}
	}

	//SIAC-8595
	private void appendNativeFilterPnotaAnno(StringBuilder jpql, Map<String, Object> param, Integer pnotaAnno) {
		if(pnotaAnno != null && pnotaAnno != 0){
			jpql
			.append("AND EXISTS (SELECT 1 FROM siac_t_movgest tm ")
			.append("	WHERE tm.movgest_anno=:pnotaAnno ")
			.append("	AND (dct.collegamento_tipo_code IN ('")
															.append(SiacDCollegamentoTipoEnum.Impegno.getCodice())     
															.append("', '")
															.append(SiacDCollegamentoTipoEnum.Accertamento.getCodice())     
															.append("') ")
			.append("		AND rerm.campo_pk_id=tm.movgest_id")
			.append("		OR dct.collegamento_tipo_code IN ('")
															.append(SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa.getCodice())     
															.append("', '")
															.append(SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata.getCodice())     
															.append("') ")
			.append("		AND EXISTS (SELECT 1 ")
			.append("			FROM siac_t_movgest_ts tmt, ")
			.append("			siac_t_movgest_ts_det_mod tmtdm, ")
			.append("			siac_r_modifica_stato rms,")
			.append("			siac_t_modifica tmd,")
			.append("			siac_d_modifica_stato dms")
			.append("			WHERE rerm.campo_pk_id=tmd.mod_id")
			.append("			AND tmt.movgest_id=tm.movgest_id ")
			.append("			AND tmtdm.movgest_ts_id=tmt.movgest_ts_id")
			.append("			AND tmtdm.mod_stato_r_id=rms.mod_stato_r_id")
			.append("			AND rms.mod_stato_id=dms.mod_stato_id")
//			.append("			AND dms.mod_stato_code!='A'")
			.append("			AND rms.mod_id=tmd.mod_id ")
			.append("			AND rerm.campo_pk_id=tmd.mod_id")
			.append("			AND tmt.data_cancellazione IS NULL")
			.append("			AND rms.data_cancellazione IS NULL")
			.append("			AND tmtdm.data_cancellazione IS NULL")
			.append("			AND tmd.data_cancellazione IS NULL")
			.append("		)")
			.append("		OR dct.collegamento_tipo_code IN ('")
															.append(SiacDCollegamentoTipoEnum.SubImpegno.getCodice())     
															.append("', '")
															.append(SiacDCollegamentoTipoEnum.SubAccertamento.getCodice())     
															.append("') ")
			.append("		AND EXISTS (SELECT 1 ")
			.append("			FROM siac_t_movgest_ts tmt, ")
			.append("			siac_d_movgest_ts_tipo dmtt ")
			.append("			WHERE rerm.campo_pk_id=tmt.movgest_ts_id")
			.append("			AND tmt.movgest_id=tm.movgest_id ")
			.append("			AND tmt.movgest_ts_tipo_id=dmtt.movgest_ts_tipo_id ")
			.append("			AND dmtt.movgest_ts_tipo_code='S'")
			.append("			AND dmtt.data_cancellazione IS NULL")
			.append("			AND tmt.data_cancellazione IS NULL")
			.append(")))");
			
			param.put("pnotaAnno", pnotaAnno);
		}
	}
	
	private void appendFilterSiacDCausaleEpTipoEnum(StringBuilder jpql, Map<String, Object> param, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum) {
		if(siacDCausaleEpTipoEnum != null){
			jpql.append(" AND p.siacDCausaleEpTipo.causaleEpTipoCode = :causaleEpTipoCode ");
			param.put("causaleEpTipoCode", siacDCausaleEpTipoEnum.getCodice());
		}
	}
	
	private void appendNativeFilterSiacDCausaleEpTipoEnum(StringBuilder jpql, Map<String, Object> param, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum) {
		if(siacDCausaleEpTipoEnum != null){
			jpql.append(" AND cet.causale_ep_tipo_code = :causaleEpTipoCode ");
			param.put("causaleEpTipoCode", siacDCausaleEpTipoEnum.getCodice());
		}
	}


	private void componiQueryRicercaSinteticaPrimaNota(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer pnotaNumero, Integer pnotaProgressivogiornale, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, String pnotaDesc,
			Collection<Integer> eventoIds, Integer causaleEpId, Integer pdceContoId, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA,
			BigDecimal importo, Integer missioneId, Integer programmaId, Integer gsaClassifId,Integer cespiteId,Integer tipoEventoId) {
		
		// Query base
		componiQueryRicercaSinteticaPrimaNotaBase( jpql, param,  enteProprietarioId, bilId, siacDAmbitoEnum,
				siacDCausaleEpTipoEnum, pnotaNumero, pnotaProgressivogiornale, siacDPrimaNotaStatoEnum, pnotaDesc,
				pdceContoId, causaleEpId, soggettoId, dataRegistrazioneDa, dataRegistrazioneA, gsaClassifId,cespiteId,tipoEventoId);
		
		appendFilterSiacDEvento(jpql, param, eventoIds);
		appendFilterImporto(jpql, param, importo);
		
		appendFilterClassMovEpDet(jpql, param, missioneId, "Missione");
		appendFilterClassMovEpDet(jpql, param, programmaId, "Programma");
	}

	private void appendFilterClassMovEpDet(StringBuilder jpql, Map<String, Object> param, Integer classifId, String alias) {
		if(classifId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM SiacTMovEpDet tmed, SiacRMovEpDetClass rmedc ");
			jpql.append("     WHERE tmed.siacTMovEp.siacTPrimaNota = p ");
			jpql.append("     AND rmedc.siacTMovEpDet = tmed ");
			jpql.append("     AND tmed.dataCancellazione IS NULL ");
			jpql.append("     AND tmed.siacTMovEp.dataCancellazione IS NULL");
			jpql.append("     AND rmedc.dataCancellazione IS NULL ");
			jpql.append("     AND rmedc.siacTClass.classifId = :classifId").append(alias).append(" ");
			jpql.append(" ) ");
			
			param.put("classifId" + alias, classifId);
		}
	}

	private void appendFilterImporto(StringBuilder jpql, Map<String, Object> param, BigDecimal importo) {
		if(importo != null) {
			// SIAC-4935: la ricerca va per importo dare o avere (la differenza e' 0)
			jpql.append(" AND :importo = ( ");
			jpql.append("     SELECT SUM(COALESCE(tmed.movepDetImporto, 0)) ");
			jpql.append("     FROM SiacTMovEpDet tmed ");
			jpql.append("     WHERE tmed.siacTMovEp.siacTPrimaNota = p ");
			jpql.append("     AND tmed.dataCancellazione IS NULL ");
			jpql.append("     AND tmed.movepDetSegno = :movepDetSegno ");
			jpql.append(" ) ");
			
			param.put("importo", importo);
			// Dare o avere, questo e' il dilemma...
			param.put("movepDetSegno", SiacDOperazioneEpEnum.SegnoContoDare.getDescrizione());
		}
	}

	private void appendFilterSiacTCausaleEp(StringBuilder jpql, Map<String, Object> param, Integer causaleEpId) {
		if( causaleEpId != null && causaleEpId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND m.siacTCausaleEp.causaleEpId = :causaleEpId ");
			jpql.append(" ) ");
			param.put("causaleEpId", causaleEpId);
		}
	}
	
	private void appendNativeFilterSiacTCausaleEp(StringBuilder sql, Map<String, Object> param, Integer causaleEpId) {
		if( causaleEpId != null && causaleEpId != 0){
			sql.append(" AND tme.causale_ep_id = :causaleEpId ");
			param.put("causaleEpId", causaleEpId);
		}
	}
	
	private void appendFilterSiacTSoggetto(StringBuilder jpql, Map<String, Object> param, Integer soggettoId) {
		if( soggettoId != null && soggettoId != 0){
			jpql.append(" AND p.siacTSoggetto.soggettoId = :soggettoId ");
			param.put("soggettoId", soggettoId);
		}
	}
	
	private void appendNativeFilterSiacTSoggetto(StringBuilder sql, Map<String, Object> param, Integer soggettoId) {
		if( soggettoId != null && soggettoId != 0){
			sql.append(" AND p.soggetto_id = :soggettoId ");
			param.put("soggettoId", soggettoId);
		}
	}
	
	/**
	 * Checks if the value is non-null and non-zero
	 * @param value the value
	 * @return whether the value is both non-null and non-zero
	 */
	private boolean nonZero(Integer value) {
		return value != null && value.intValue() != 0;
	}
	
	private void appendFilterSiacTGsaClassif(StringBuilder jpql, Map<String, Object> param, Integer gsaClassifId) {
		if(nonZero(gsaClassifId)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacRGsaClassifPrimaNotas rgcpn ");
			jpql.append("     WHERE rgcpn.dataCancellazione IS NULL ");
			jpql.append("     AND rgcpn.siacTGsaClassif.gsaClassifId = :gsaClassifId ");
			jpql.append(" ) ");
			param.put("gsaClassifId", gsaClassifId);
		}
	}
	
	private void appendNativeFilterIdGsaClassif(StringBuilder sql, Map<String, Object> param, Integer gsaClassifId) {
		if(nonZero(gsaClassifId)) {
			sql.append(" AND EXISTS ( ");
			sql.append("     SELECT 1 ");
			sql.append("     FROM siac_r_gsa_classif_prima_nota rgcpn ");
			sql.append("     WHERE rgcpn.data_cancellazione IS NULL ");
			sql.append("     AND rgcpn.pnota_id = p.pnota_id ");
			sql.append("     AND rgcpn.gsa_classif_id = :gsaClassifId ");
			sql.append(" ) ");
			param.put("gsaClassifId", gsaClassifId);
		}
	}

	private void appendFilterSiacDEvento(StringBuilder jpql, Map<String, Object> param, Collection<Integer> eventoIds) {
		if(eventoIds != null && !eventoIds.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTCausaleEp.siacREventoCausales re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.siacDEvento.eventoId IN (:eventoIds) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("eventoIds", eventoIds);
		}
	}
	
	private void appendFilterSiacDEventoTipo(StringBuilder jpql, Map<String, Object> param, Integer eventoTipoId) {
		if(nonZero(eventoTipoId)){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTCausaleEp.siacREventoCausales re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.siacDEvento.siacDEventoTipo.eventoTipoId = :eventoTipoId ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			param.put("eventoTipoId", eventoTipoId);
		}
	}
	
	//SIAC-6617
	private void appendFilterSiacDEventoTipo(StringBuilder jpql, Map<String, Object> param, SiacDEventoTipoEnum siacDEventoTipoEnum) {
		if(siacDEventoTipoEnum != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m, SiacREventoCausale re ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND re.dataCancellazione IS NULL ");
			jpql.append("     AND m.siacTCausaleEp = re.siacTCausaleEp ");
			jpql.append("     AND re.siacDEvento.siacDEventoTipo.eventoTipoCode = :eventoTipoCode ");
			jpql.append(" ) ");
			param.put("eventoTipoCode", siacDEventoTipoEnum.getCodice());
		}
	}
	
	private void appendFilterHasSiacTPdceContoInventario(StringBuilder jpql, Map<String, Object> params, Integer pdceContoId) {
		if(pdceContoId != null) {
			// Ho gia' il filtro sul conto, non rieffettuo il calcolo
			return;
		}
		// TODO: tutti i conti inventario hanno tipo CES? E' l'unico controllo?
		jpql.append(" AND EXISTS ( ");
		jpql.append("     FROM p.siacTMovEps m, SiacTMovEpDet tmed ");
		jpql.append("     WHERE m.dataCancellazione IS NULL ");
		jpql.append("     AND tmed.dataCancellazione IS NULL ");
		jpql.append("     AND tmed.siacTMovEp = m ");
		jpql.append("     AND tmed.siacTPdceConto.siacDPdceContoTipo.pdceCtTipoCode = 'CES' ");
//		jpql.append("     AND tmed.siacTPdceConto.siacTPdceFamTree.siacDPdceFam.pdceFamCode = 'AP' ");
		jpql.append(" ) ");
	}
	
	private void appendNativeFilterHasSiacTPdceContoInventario(StringBuilder sql, Map<String, Object> params, Integer pdceContoId) {
		if(pdceContoId != null) {
			// Ho gia' il filtro sul conto, non rieffettuo il calcolo
			return;
		}
		sql.append(" AND EXISTS ( ");
		sql.append("     SELECT 1 ");
		sql.append("     FROM siac_t_mov_ep_det tmed ");
		sql.append("     JOIN siac_t_pdce_conto tpc ON (tpc.pdce_conto_id = tmed.pdce_conto_id AND tpc.data_cancellazione IS NULL) ");
		sql.append("     JOIN siac_d_pdce_conto_tipo dpct ON (dpct.pdce_ct_tipo_id = tpc.pdce_ct_tipo_id AND dpct.data_cancellazione IS NULL) ");
		sql.append("     JOIN siac_t_pdce_fam_tree tpft ON (tpft.pdce_fam_tree_id = tpc.pdce_fam_tree_id AND tpft.data_cancellazione IS NULL) ");
		sql.append("     JOIN siac_d_pdce_fam dpf ON (dpf.pdce_fam_id = tpft.pdce_fam_id AND dpf.data_cancellazione IS NULL) ");
		sql.append("     WHERE tmed.data_cancellazione IS NULL ");
		sql.append("     AND tmed.movep_id = tme.movep_id ");
		sql.append("     AND dpct.pdce_ct_tipo_code = 'CES' ");
//		sql.append("     AND dpf.pdce_fam_code = 'AP' ");
		sql.append(" ) ");
	}
	
	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaIntegrata(Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, 
			Collection<Integer> listaEventoTipoId, Integer eventoTipoId, Collection<Integer> eventoIds, Integer pnotaNumero, Integer pnotaProgressivogiornale, 
			Integer pdceContoId, Collection<Integer> idMovimento, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId,
			String pnotaDesc, Integer causaleEpId, Date dataRegistrazioneDa, Date dataRegistrazioneA, Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA,
			Integer docId, Integer gsaClassifId, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaPrimaNotaIntegrata";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		componiQueryRicercaSinteticaPrimaNotaIntegrata(jpql, param, enteProprietarioId, bilId, siacDAmbitoEnum, siacDCausaleEpTipoEnum, listaEventoTipoId, eventoTipoId, eventoIds, 
				pnotaNumero, pnotaProgressivogiornale, pdceContoId, idMovimento, siacDPrimaNotaStatoEnum, 
				 classifId, pnotaDesc, causaleEpId, null /*soggettoId*/, dataRegistrazioneDa, dataRegistrazioneA, dataRegistrazioneProvvisoriaDa, dataRegistrazioneProvvisoriaA, docId, gsaClassifId);
		
		jpql.append(" ORDER BY p.pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrata(Integer enteProprietarioId, Integer bilId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos,
			SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Collection<Integer> listaEventoTipoId, Integer eventoTipoId, Collection<Integer> eventoIds,
			Integer pnotaNumero, Integer pnotaProgressivogiornale, Integer pnotaAnno, Integer pdceContoId, Collection<Integer> idMovimento, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId,
			String pnotaDesc, Integer causaleEpId, Date dataRegistrazioneDa, Date dataRegistrazioneA, Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA,
			List<Integer> docIds, 
			
			Integer uidAttoAmm,
			Integer uidMovGest,
			Integer uidSubMovGest,
			Integer uidSac,
			Integer soggettoId, 
			BigDecimal importoDocumentoDa, 
			BigDecimal importoDocumentoA,	
			
			Integer bilElemId,
			Integer gsaClassifId,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaNativaPrimaNotaIntegrata";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		//SIAC-8595
		componiNativeQueryRicercaSinteticaPrimaNotaIntegrata(jpql, param, enteProprietarioId, bilId,siacDCollegamentoTipos, siacDAmbitoEnum, siacDCausaleEpTipoEnum, listaEventoTipoId,
				eventoTipoId, eventoIds, pnotaNumero, pnotaProgressivogiornale, pnotaAnno, pdceContoId, idMovimento, siacDPrimaNotaStatoEnum, 
				 classifId, pnotaDesc, causaleEpId, soggettoId, dataRegistrazioneDa, dataRegistrazioneA, dataRegistrazioneProvvisoriaDa, dataRegistrazioneProvvisoriaA, docIds,
				 uidAttoAmm,uidMovGest,uidSubMovGest, uidSac, importoDocumentoDa, importoDocumentoA, bilElemId, gsaClassifId);
		
		jpql.append(" ORDER BY p.pnota_numero ");
		
		log.debug (methodName, "Query della prima nota integrata SQL to execute: " + jpql.toString());
		log.debug(methodName, "Ricerca Registazione uidSac: " + uidSac);

		return getNativePagedList(jpql.toString(), param, pageable, SiacTPrimaNota.class);
	}
	
	


	private void componiQueryRicercaSinteticaPrimaNotaIntegrata(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Collection<Integer> listaEventoTipoId, Integer eventoTipoId, Collection<Integer> eventoIds,
			Integer pnotaNumero, Integer pnotaProgressivogiornale, Integer pdceContoId, Collection<Integer> idMovimento, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId,
			String pnotaDesc, Integer causaleEpId, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA, Integer docId, Integer gsaClassifId) {
		
		componiQueryRicercaSinteticaPrimaNotaBase( jpql, param,  enteProprietarioId, bilId, siacDAmbitoEnum,
				siacDCausaleEpTipoEnum, pnotaNumero, pnotaProgressivogiornale, siacDPrimaNotaStatoEnum, pnotaDesc,
				pdceContoId, causaleEpId, soggettoId, dataRegistrazioneDa, dataRegistrazioneA,null, null,null);
		
		appendFilterDataRegistrazioneProvvisoriaDa(jpql, param, dataRegistrazioneProvvisoriaDa);
		appendFilterDataRegistrazioneProvvisoriaA(jpql, param, dataRegistrazioneProvvisoriaA);
		appendFilterSiacDEventoIntegrata(jpql, param, eventoIds);
		appendFilterSiacDEventoTipoIntegrata(jpql, param, eventoTipoId);
		appendFilterListaSiacDEventoTipoIntegrata(jpql, param, listaEventoTipoId);
		appendFilterSiacTClass(jpql, param, classifId);
		appendFilterIdMovimento(jpql, param, eventoIds, idMovimento);
		appendFilterIdDocumento(jpql, param, docId);
		appendFilterSiacTGsaClassif(jpql, param, gsaClassifId);
	}
	
	//SIAC-8595
	private void componiNativeQueryRicercaSinteticaPrimaNotaIntegrata(StringBuilder sql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId,
			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos, SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Collection<Integer> listaEventoTipoId, Integer eventoTipoId, Collection<Integer> eventoIds, Integer pnotaNumero, Integer pnotaProgressivogiornale,Integer pnotaAnno,
			Integer pdceContoId, Collection<Integer> idMovimento, SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId,
			String pnotaDesc, Integer causaleEpId, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA, List<Integer> docId,
			Integer uidAttoAmm,Integer uidMovGest,Integer uidSubMovGest,Integer uidSac,	BigDecimal importoDocumentoDa, BigDecimal importoDocumentoA, Integer uidBilElem, Integer gsaClassifId) {
		
		componiNativeQueryRicercaSinteticaPrimaNotaBase( sql, param,  enteProprietarioId, bilId, siacDAmbitoEnum,
				siacDCausaleEpTipoEnum, pnotaNumero, pnotaProgressivogiornale, pnotaAnno, siacDPrimaNotaStatoEnum, pnotaDesc,
				pdceContoId, causaleEpId, soggettoId, dataRegistrazioneDa, dataRegistrazioneA);
		
		appendNativeFilterDataRegistrazioneProvvisoriaDa(sql, param, dataRegistrazioneProvvisoriaDa);
		appendNativeFilterDataRegistrazioneProvvisoriaA(sql, param, dataRegistrazioneProvvisoriaA);
		appendNativeFilterSiacDEventoIntegrata(sql, param, eventoIds);
		appendNativeFilterSiacDEventoTipoIntegrata(sql, param, eventoTipoId);
		appendNativeFilterListaSiacDEventoTipoIntegrata(sql, param, listaEventoTipoId);
		appendNativeFilterSiacTClass(sql, param, classifId);
		appendNativeFilterIdMovimento(sql, param, idMovimento);
		appendNativeFilterIdDocumento(sql, param, docId);
		appendNativeFilterIdAttoAmm(sql, param, uidAttoAmm, siacDCollegamentoTipos);		
		//SIAC-5799
		log.info("componiNativeQueryRicercaSinteticaPrimaNotaIntegrata","uidMovGest -->" +uidMovGest);
		//log.info("componiNativeQueryRicercaSinteticaPrimaNotaIntegrata","uidSac -->" +uidSac);
		appendNativeFilterIdMovGest(sql, param, uidMovGest, siacDCollegamentoTipos);
		
		appendNativeFilterIdSubMovGest(sql, param, uidSubMovGest, siacDCollegamentoTipos);
		
		appendNativeFilterIdBilElemBySac(sql, param, uidSac, siacDCollegamentoTipos);				
		appendNativeFilterImportoDocumento(sql, param, importoDocumentoDa, importoDocumentoA, docId);		
		appendNativeFilterIdBilElem(sql, param, uidBilElem, siacDCollegamentoTipos);		
		appendNativeFilterIdGsaClassif(sql, param, gsaClassifId);
		
	}

	private void appendNativeFilterImportoDocumento(StringBuilder sql, Map<String, Object> param,
			BigDecimal importoDocumentoDa, BigDecimal importoDocumentoA, List<Integer> docIds /*opzionale*/ ) {
		
		if(importoDocumentoDa == null && importoDocumentoA == null){
			return;
		}
		
		sql.append(" AND dct.collegamento_tipo_code IN ('")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCodice())
			.append("', '")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCodice())
			.append("') ");
		
		sql.append(" AND rerm.campo_pk_id IN ( ");
		sql.append("     SELECT subdoc_id ");
		sql.append("     FROM siac_t_subdoc s ");
		sql.append("     JOIN siac_t_doc d ON s.doc_id = d.doc_id ");
		sql.append("     WHERE d.data_cancellazione IS NULL ");
		if(docIds!=null & !docIds.isEmpty()) { //aggiunto per performance: qualora ci sia il docId e' inutile avere tutti i docs.
			sql.append("     AND d.doc_id IN (:docIds) ");
			param.put("docId", docIds);
		}
		if(importoDocumentoDa!=null) {
			sql.append("     AND d.doc_importo >= :importoDocumentoDa ");
			param.put("importoDocumentoDa", importoDocumentoDa);
		}
		if(importoDocumentoA!=null) {
			sql.append("     AND d.doc_importo <= :importoDocumentoA ");
			param.put("importoDocumentoA", importoDocumentoA);
		}
		
		sql.append(" ) ");
		
	}
	
	
	
	private void appendNativeFilterIdAttoAmm(StringBuilder sql, Map<String, Object> param, Integer attoammId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdMovGest";
		
		if(attoammId == null || attoammId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per attoammId=" + attoammId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
				
		Set<MovimentiFromAttoAmmJpqlEnum> movimentiFromAttoAmmJpqlEnum = MovimentiFromAttoAmmJpqlEnum.toMovimentiFromAttoAmmJpqlEnum(siacDCollegamentoTipos);		
		sql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromAttoAmmJpqlEnum rfbeje : movimentiFromAttoAmmJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();			
			tmp.append(" ( dct.collegamento_tipo_code IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND rerm.campo_pk_id IN ( ").append(rfbeje.getNativeSql()).append(" ) ) ");			
			chunks.add(tmp.toString());			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		sql.append(StringUtils.join(chunks, " OR "));		
		sql.append(" ) ");		
		param.put("attoammId", attoammId);
	}
	
	/*
	private void appendNativeFilterIdAttoAmm(StringBuilder sql, Map<String, Object> param, Integer attoammId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdAttoAmm";
		
		if(attoammId == null || attoammId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per attoammId="+attoammId+" in quanto non posso dedurre il tipo di collegamento.");
			return; //non posso applicare il filtro
		}
		
		Set<MovimentiFromAttoAmmJpqlEnum> movimentiFromAttoAmmJpqlEnum = MovimentiFromAttoAmmJpqlEnum.toMovimentiFromAttoAmmJpqlEnum(siacDCollegamentoTipos);
		
		int i = 1;
		for(MovimentiFromAttoAmmJpqlEnum mfaaje : movimentiFromAttoAmmJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes"+i;
			
			sql.append(" AND dct.collegamento_tipo_code IN (:"+paramNameCollegamentoTipoCodes+") ");
			sql.append(" AND rerm.campo_pk_id IN ( ");
			
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: "+mfaaje.getSiacDCollegamentoTipoEnums() + " > "+ mfaaje + " paramName:"+paramNameCollegamentoTipoCodes);
			sql.append(mfaaje.getNativeSql());
			
			sql.append("                         ) ");
			
			param.put(paramNameCollegamentoTipoCodes, mfaaje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		
		param.put("attoammId", attoammId);
		
	}

	*/
	private void appendNativeFilterIdBilElem(StringBuilder sql, Map<String, Object> param, Integer bilElemId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdBilElem";
		
		if(bilElemId == null || bilElemId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per bilElemId=" + bilElemId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		
		
		Set<MovimentiFromBilElemJpqlEnum> movimentiFromBilElemJpqlEnum = MovimentiFromBilElemJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipos);
		
		sql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromBilElemJpqlEnum rfbeje : movimentiFromBilElemJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			
			tmp.append(" ( dct.collegamento_tipo_code IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND rerm.campo_pk_id IN ( ").append(rfbeje.getNativeSql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		sql.append(StringUtils.join(chunks, " OR "));
		
		sql.append(" ) ");
		
		param.put("elemId", bilElemId);
		
	}
	
	private void appendNativeFilterIdBilElemBySac(StringBuilder sql, Map<String, Object> param, Integer sacId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdBilElemBySac";
		
		if(sacId == null || sacId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per sacId=" + sacId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		
		
		Set<MovimentiFromBilElemBySacJpqlEnum> movimentiFromBilElemBySacJpqlEnum = MovimentiFromBilElemBySacJpqlEnum.toMovimentiFromBilElemBySacJpqlEnum(siacDCollegamentoTipos);
		
		sql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromBilElemBySacJpqlEnum rfbeje : movimentiFromBilElemBySacJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			
			tmp.append(" ( dct.collegamento_tipo_code IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND rerm.campo_pk_id IN ( ").append(rfbeje.getNativeSql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		sql.append(StringUtils.join(chunks, " OR "));
		
		sql.append(" ) ");
		
		param.put("sacId", sacId);
		
	}
	
	
	//SIAC-5799
	private void appendNativeFilterIdMovGest(StringBuilder sql, Map<String, Object> param, Integer movgestId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdMovGest";
		
		if(movgestId == null || movgestId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per movGestId=" + movgestId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		
		
		Set<MovimentiFromMovgestJpqlEnum> movimentiFromMovgestJpqlEnum = MovimentiFromMovgestJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipos);
		
		sql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromMovgestJpqlEnum rfbeje : movimentiFromMovgestJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			
			tmp.append(" ( dct.collegamento_tipo_code IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND rerm.campo_pk_id IN ( ").append(rfbeje.getNativeSql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		sql.append(StringUtils.join(chunks, " OR "));
		
		sql.append(" ) ");
		
		param.put("movgestId", movgestId);
		
	}
	
	//SIAC-5799
	private void appendNativeFilterIdSubMovGest(StringBuilder sql, Map<String, Object> param, Integer movgestTsId, Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos) {
		String methodName = "appendNativeFilterIdSubMovGest";
		
		if(movgestTsId == null || movgestTsId.equals(Integer.valueOf(0))){
			return;
		}
		if(siacDCollegamentoTipos==null || siacDCollegamentoTipos.isEmpty()){
			log.warn(methodName, "Filtro non applicabile per movGesTstId=" + movgestTsId + " in quanto non posso dedurre il tipo di collegamento.");
			// non posso applicare il filtro
			return;
		}
		
		
		Set<MovimentiFromMovgestTsJpqlEnum> movimentiFromMovgestTsJpqlEnum = MovimentiFromMovgestTsJpqlEnum.toMovimentiFromBilElemJpqlEnum(siacDCollegamentoTipos);
		
		sql.append(" AND ( ");
		List<String> chunks = new ArrayList<String>();
		
		int i = 1;
		for(MovimentiFromMovgestTsJpqlEnum rfbeje : movimentiFromMovgestTsJpqlEnum) {
			String paramNameCollegamentoTipoCodes = "collegamentoTipoCodes" + i;
			StringBuilder tmp = new StringBuilder();
			
			tmp.append(" ( dct.collegamento_tipo_code IN (:").append(paramNameCollegamentoTipoCodes).append(") ");
			log.debug(methodName, "Aggiungo filtro per collegamento tipo: " + rfbeje.getSiacDCollegamentoTipoEnums() + " > "+ rfbeje + " paramName:" + paramNameCollegamentoTipoCodes);
			tmp.append(" AND rerm.campo_pk_id IN ( ").append(rfbeje.getNativeSql()).append(" ) ) ");
			
			chunks.add(tmp.toString());
			
			param.put(paramNameCollegamentoTipoCodes, rfbeje.getSiacDCollegamentoTipoCodes());
			i++;
		}
		sql.append(StringUtils.join(chunks, " OR "));
		
		sql.append(" ) ");
		
		param.put("movgestTsId", movgestTsId);
	
	}
	
	private void appendFilterIdDocumento(StringBuilder jpql, Map<String, Object> param, Integer docId) {
		if(docId != null && docId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.campoPkId IN ( ");
			jpql.append("             SELECT s.subdocId ");
			jpql.append("             FROM SiacTSubdoc s ");
			jpql.append("             WHERE s.siacTDoc.docId = :docId ");
			jpql.append("             AND s.dataCancellazione IS NULL ");
			jpql.append("         ) ");
			jpql.append("         AND re.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode IN ('SS', 'SE') ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("docId", docId);
		}
	}
	
	
	private void appendNativeFilterIdDocumento(StringBuilder sql, Map<String, Object> param, List<Integer> docIds) {
		if(docIds != null && !docIds.isEmpty()){
			
			sql.append(" AND rerm.campo_pk_id IN (SELECT s.subdoc_id from siac_t_subdoc s WHERE s.doc_id IN (:docIds) ) ");
			sql.append(" AND dct.collegamento_tipo_code IN ('" + SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCodice() + "', '" + SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCodice() + "') ");
			
			
			param.put("docIds", docIds);
		}
	}
	

	
	
	
	
	private void appendFilterEvento(StringBuilder jpql, Map<String, Object> param, Collection<String> eventoCodes) {
		if(eventoCodes != null && !eventoCodes.isEmpty()){
			jpql.append(" AND EXISTS ( FROM p.siacTMovEps m");
			jpql.append("  				WHERE m.dataCancellazione IS NULL ");
			jpql.append("  				AND EXISTS ( FROM m.siacTRegMovfin.siacREventoRegMovfins re  ");
			jpql.append("  						  	 WHERE re.siacDEvento.eventoCode IN (:eventoCodes) ");
			jpql.append("  						  	 AND re.dataCancellazione IS NULL");
			jpql.append("  						  	) ");
			jpql.append("  			) ");
			param.put("eventoCodes", eventoCodes);
		}
		
	}

	private void appendFilterIdMovimento(StringBuilder jpql, Map<String, Object> param, Collection<Integer> eventoIds, Collection<Integer> idMovimento) {
		if(idMovimento != null && !idMovimento.isEmpty() /*&& eventoIds != null && !eventoIds.isEmpty()*/){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re  ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
//			jpql.append("         AND re.siacDEvento.eventoId IN (:eventoIds)  ");
			jpql.append("         AND re.campoPkId IN ( :campoPkId ) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
//			param.put("eventoIds", eventoIds);
			param.put("campoPkId", idMovimento);
		}
	}
	
	private void appendNativeFilterIdMovimento(StringBuilder sql, Map<String, Object> param, Collection<Integer> idMovimento) {
		if(idMovimento != null && !idMovimento.isEmpty()){
			
			sql.append(" AND rerm.campo_pk_id IN (:campoPkIds) ");
			
			param.put("campoPkIds", idMovimento);
		}

	}


	private void appendFilterSiacTClass(StringBuilder jpql, Map<String, Object> param, Integer classifId) {
		if(classifId != null && classifId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND m.siacTRegMovfin.siacTClass1.classifId = :classifId ");
			jpql.append(" ) ");
			param.put("classifId", classifId);
		}
	}
	
	private void appendNativeFilterSiacTClass(StringBuilder sql, Map<String, Object> param, Integer classifId) {
		if(classifId != null && classifId != 0){
			sql.append(" AND EXISTS ( ");
			sql.append("     SELECT 1 FROM siac_t_reg_movfin reg ");
			sql.append("     WHERE reg.data_cancellazione IS NULL ");
			sql.append("     AND tme.regmovfin_id = reg.regmovfin_id ");
			sql.append("     AND reg.classif_id_aggiornato = :classifId ");
			sql.append(" ) ");
			
			param.put("classifId", classifId);
		}
	}

	private void appendFilterSiacDEventoTipoIntegrata(StringBuilder jpql, Map<String, Object> param, Integer eventoTipoId) {
		if( eventoTipoId != null && eventoTipoId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.siacDEvento.siacDEventoTipo.eventoTipoId = :eventoTipoId ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("eventoTipoId", eventoTipoId);
		}
	}
	
	private void appendNativeFilterSiacDEventoTipoIntegrata(StringBuilder sql, Map<String, Object> param, Integer eventoTipoId) {
		if( eventoTipoId != null && eventoTipoId != 0){
			
			sql.append(" AND de.evento_tipo_id = :eventoTipoId ");
			
			param.put("eventoTipoId", eventoTipoId);
		}
	}
	
	private void appendNativeFilterSiacDEventoTiposIntegrata(StringBuilder sql, Map<String, Object> param, List<Integer> eventoTipoIds) {
		if( eventoTipoIds != null && !eventoTipoIds.isEmpty()){
			
			sql.append(" AND de.evento_tipo_id IN (:eventoTipoIds) ");
			
			param.put("eventoTipoIds", eventoTipoIds);
		}
	}
	
	private void appendFilterListaSiacDEventoTipoIntegrata(StringBuilder jpql, Map<String, Object> param, Collection<Integer> listaEventoTipoId) {
		if( listaEventoTipoId != null && !listaEventoTipoId.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.siacDEvento.siacDEventoTipo.eventoTipoId IN (:eventoTipoId) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("eventoTipoId", listaEventoTipoId);
		}
	}
	
	private void appendNativeFilterListaSiacDEventoTipoIntegrata(StringBuilder sql, Map<String, Object> param, Collection<Integer> listaEventoTipoId) {
		if( listaEventoTipoId != null && !listaEventoTipoId.isEmpty()){
			
			sql.append(" AND rerm.evento_id IN (SELECT evento_id FROM siac_d_evento where evento_tipo_id IN (:listaEventoTipoId) ) ");
			
			param.put("listaEventoTipoId", listaEventoTipoId);
		}
	}
	

	
	

	private void appendFilterSiacDEventoIntegrata(StringBuilder jpql, Map<String, Object> param, Collection<Integer> eventoIds) {
		if( eventoIds != null && !eventoIds.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.siacDEvento.eventoId IN (:eventoIds) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("eventoIds", eventoIds);
		}
	}
	
	private void appendNativeFilterSiacDEventoIntegrata(StringBuilder sql, Map<String, Object> param, Collection<Integer> eventoIds) {
		if( eventoIds != null && !eventoIds.isEmpty()){
			sql.append(" AND rerm.evento_id IN (:eventoIds) ");
			param.put("eventoIds", eventoIds);
		}
	}

	@Override
	public SiacTPrimaNota ricercaDettaglioPrimaNotaIntegrata(Integer pnotaId, Integer regmovfinId, Integer docId, String ambitoCode, Collection<String> eventoCodes, Collection<SiacDPrimaNotaStatoEnum> stati) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaDettaglioPrimaNotaBaseIntegrata( jpql, param,  pnotaId, regmovfinId, docId, ambitoCode, eventoCodes, stati);
		
		try{
			Query query = createQuery(jpql.toString(), param);
			SiacTPrimaNota siacTPrimaNota = (SiacTPrimaNota) query.getSingleResult();
			return siacTPrimaNota;
		}catch(NoResultException nre){
				return null;
		} catch(NonUniqueResultException nure){
			throw new IllegalArgumentException("Trovato più di una prima nota per i parametri di ricerca inseriti", nure);
		}
	}


	private void componiQueryRicercaDettaglioPrimaNotaBaseIntegrata( StringBuilder jpql, Map<String, Object> param,  Integer pnotaId, Integer regmovfinId, Integer docId,
			String ambitoCode, Collection<String> eventoCodes, Collection<SiacDPrimaNotaStatoEnum> stati) {
		
		jpql.append("FROM SiacTPrimaNota p "); 
		jpql.append(" WHERE ");
		jpql.append(" p.dataCancellazione IS NULL ");
		
		appendFilterPnotaId(jpql, param, pnotaId);
		appendFilterSiacTRegMovfin(jpql, param, regmovfinId);
		appendFilterIdDocumento(jpql, param, docId);
		appendFilterEvento(jpql, param, eventoCodes);
		appendFilterAmbito(jpql, param, ambitoCode);
		appendFilterStati(jpql, param, stati);//TODO devo escludere le annulalte SIAC-3507!!
	}
	
	private void appendFilterStati(StringBuilder jpql, Map<String, Object> param, Collection<SiacDPrimaNotaStatoEnum> statiDaEscludere) {
		if(statiDaEscludere != null && !statiDaEscludere.isEmpty()){
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append("     FROM p.siacRPrimaNotaStatos ps ");
			jpql.append("     WHERE ps.dataCancellazione IS NULL ");
			jpql.append("     AND ps.siacDPrimaNotaStato.pnotaStatoCode IN (:pnotaStatoCodes) ");
			jpql.append(" ) ");
			
			List<String> statoCodes = new ArrayList<String>();
			for(SiacDPrimaNotaStatoEnum stato : statiDaEscludere){
				statoCodes.add(stato.getCodice());
			}
			
			param.put("pnotaStatoCodes", statoCodes);
		}
	}

	private void appendFilterAmbito(StringBuilder jpql, Map<String, Object> param, String ambitoCode) {
		if(StringUtils.isNotBlank(ambitoCode)) {
			jpql.append(" AND p.siacDAmbito.ambitoCode = :ambitoCode ");
			
			param.put("ambitoCode", ambitoCode);
		}
	}

	private void appendFilterSiacTRegMovfin(StringBuilder jpql, Map<String, Object> param, Integer regmovfinId) {
		if(regmovfinId != null && regmovfinId != 0){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND m.siacTRegMovfin.regmovfinId = :regmovfinId ");
			jpql.append(" ) ");
					
			param.put("regmovfinId", regmovfinId);
		}
	}

	private void appendFilterPnotaId(StringBuilder jpql, Map<String, Object> param, Integer pnotaId) {
		if(pnotaId != null && pnotaId != 0){
			jpql.append(" AND p.pnotaId = :pnotaId ");
			param.put("pnotaId", pnotaId);
		}
	}


	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaIntegrataValidabile(Integer enteProprietarioId, Integer bilId, SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Integer eventoTipoId, Collection<Integer> eventoIds, Integer pnotaNumero, Integer pnotaProgressivogiornale, Integer pdceContoId, Integer causaleEpId, Collection<Integer> idMovimento,
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId, String pnotaDesc, Integer soggettoId, Date dataRegistrazioneDa, Date dataRegistrazioneA,
			Collection<String> collegamentoTipoCodesSubdoc, Collection<String> eventoCassaEconomaleCodes, Collection<String> eventoCodes, Integer gsaClassifId, Pageable pageable) {
		final String methodName = "ricercaSinteticaPrimaNotaIntegrataValidabile";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaPrimaNotaIntegrata(jpql, param, enteProprietarioId, bilId, siacDAmbitoEnum, siacDCausaleEpTipoEnum,
				null, eventoTipoId, eventoIds, pnotaNumero, pnotaProgressivogiornale, pdceContoId, idMovimento, siacDPrimaNotaStatoEnum, classifId,
				pnotaDesc, causaleEpId, soggettoId, null, null, dataRegistrazioneDa, dataRegistrazioneA , null, gsaClassifId);
		
		boolean shouldAppendFilterSubdocumento = shouldAppendFilterSubdocumento(eventoTipoId, eventoIds /*, collegamentoTipoCodesSubdoc*/);
		
		if(shouldAppendFilterSubdocumento) {
			appendFilterSubdocumentiConRegistrazione(jpql, param, collegamentoTipoCodesSubdoc,/* eventoNotaCreditoCodes, */eventoCassaEconomaleCodes, eventoCodes, siacDAmbitoEnum.getCodice());
		}
		
		jpql.append(" ORDER BY p.pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrataValidabile(Integer enteProprietarioId, Integer bilId,
			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos,
			SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer eventoTipoId, Collection<Integer> eventoIds,
			Integer pnotaNumero, Integer pnotaProgressivogiornale, Integer pnotaAnno, Integer pdceContoId, Integer causaleEpId, Collection<Integer> idMovimento,
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum, Integer classifId, String pnotaDesc, Integer soggettoId, Date dataRegistrazioneDa,
			Date dataRegistrazioneA, Integer uidAttoAmm,Integer uidMovGest,Integer uidSubMovGest,Integer uidSac, Integer uidBilElem, Integer gsaClassifId,
			//Collection<String> collegamentoTipoCodesSubdoc, Collection<String> eventoCassaEconomaleCodes, Collection<String> eventoCodes, 
			Pageable pageable) {
		
		StringBuilder sql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		//SIAC-8595
		componiNativeQueryRicercaSinteticaPrimaNotaIntegrata(sql, param, enteProprietarioId, bilId, siacDCollegamentoTipos, siacDAmbitoEnum, siacDCausaleEpTipoEnum,
				null, eventoTipoId, eventoIds, pnotaNumero, pnotaProgressivogiornale, pnotaAnno, pdceContoId, idMovimento, siacDPrimaNotaStatoEnum, classifId,
				pnotaDesc, causaleEpId, soggettoId, null, null, dataRegistrazioneDa, dataRegistrazioneA , null /*docId*/,
				 uidAttoAmm,uidMovGest,uidSubMovGest,uidSac, null /*importoDocumentoDa*/, null /*importoDocumentoA*/, uidBilElem, gsaClassifId);
		
		boolean shouldAppendFilterSubdocumento = shouldAppendFilterSubdocumento(eventoTipoId, eventoIds /*, collegamentoTipoCodesSubdoc*/);
		
		if(shouldAppendFilterSubdocumento) {
			appendNativeFilterSubdocumentiConRegistrazione(sql, param /*, eventoCassaEconomaleCodes, eventoCodes, siacDAmbitoEnum.getCodice()*/);
		}
		
		sql.append(" ORDER BY p.pnota_numero ");
		
		return getNativePagedList(sql.toString(), param, pageable, SiacTPrimaNota.class);
	}

	private boolean shouldAppendFilterSubdocumento(Integer eventoTipoId, Collection<Integer> eventoIds) {
		
				
		// SIAC-3885: dovrebbe ottimizzare in tutti i casi in cui il tipo di collegamento viene fornito e non e' di tipo subdoc
		Collection<String> tipoCollegamentoCodes = new HashSet<String>();
		// SIAC-5334: necessario utilizzare l'evento id multiplo. Per il subdocumento considero in realta' solo il primo evento.
		// TODO: valutare se prendere piu' eventi
		Integer eventoId = null;
		if(!eventoIds.isEmpty()) {
			eventoId = (Integer) CollectionUtils.get(eventoIds, 0);
		}
		
		if(eventoId != null) {
			SiacDEvento siacDEvento = siacDEventoRepository.findOne(eventoId);
			SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
			tipoCollegamentoCodes.add(siacDCollegamentoTipo.getCollegamentoTipoCode());
		} else if(eventoTipoId != null) {
			List<SiacDCollegamentoTipo> siacDCollegamentoTipos = siacDEventoRepository.findSiacDCollegamentoTipoByEventoTipoId(eventoTipoId);
			for(SiacDCollegamentoTipo siacDCollegamentoTipo : siacDCollegamentoTipos) {
				tipoCollegamentoCodes.add(siacDCollegamentoTipo.getCollegamentoTipoCode());
			}
		}
		
		if(tipoCollegamentoCodes.isEmpty()) {
			// Non conosco il tipo collegamento: devo appendere il filtro dei subdoc
			return true;
		}
		for(String tipoCollegamentoCode : tipoCollegamentoCodes) {
			if(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCollegamentoTipoCode(),
					SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCollegamentoTipoCode()).contains(tipoCollegamentoCode)) {
				// Esiste un tipo di collegamento di tipo subdoc
				return true;
			}
		}
		return false;
	}
	
	private void appendNativeFilterSubdocumentiConRegistrazione(StringBuilder jpql, Map<String, Object> param) {
		
		jpql.append(" AND (dct.collegamento_tipo_code NOT IN ('")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCodice())
			.append("', '")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCodice()).append("') ");
		jpql.append(" OR NOT EXISTS ( ");
		jpql.append("     WITH due AS ( ");
		jpql.append("         SELECT p2.data_creazione, p2.pnota_id, ts2.doc_id,ts2.subdoc_id,de2.evento_id,rm2.ambito_id,de2.evento_code, drms2.regmovfin_stato_code, ct2.collegamento_tipo_code ");
		jpql.append("         FROM siac_t_subdoc ts2, ");
		jpql.append("             siac_r_evento_reg_movfin rerm2, ");
		jpql.append("             siac_r_reg_movfin_stato rrmfs2, ");
		jpql.append("             siac_d_reg_movfin_stato drms2, ");
		jpql.append("             siac_d_evento de2, ");
		jpql.append("             siac_t_reg_movfin rm2, ");
		jpql.append("             siac_d_collegamento_tipo ct2, ");
		jpql.append("             siac_t_mov_ep tme2, ");
		jpql.append("             siac_t_prima_nota p2, ");
		jpql.append("             siac_r_prima_nota_stato rpns2, ");
		jpql.append("             siac_d_prima_nota_stato pns2 ");
		jpql.append("         WHERE ts2.ente_proprietario_id = :enteProprietarioId ");
		
		//data_cancellazione IS NULL
		jpql.append("         AND ts2.data_cancellazione IS NULL ");
		jpql.append("         AND rerm2.data_cancellazione IS NULL ");
		jpql.append("         AND rrmfs2.data_cancellazione IS NULL ");
		jpql.append("         AND drms2.data_cancellazione IS NULL ");
		jpql.append("         AND de2.data_cancellazione IS NULL ");
		jpql.append("         AND rm2.data_cancellazione IS NULL ");
		jpql.append("         AND ct2.data_cancellazione IS NULL ");
		jpql.append("         AND tme2.data_cancellazione IS NULL ");
		jpql.append("         AND p2.data_cancellazione IS NULL ");
		jpql.append("         AND rpns2.data_cancellazione IS NULL ");
		jpql.append("         AND pns2.data_cancellazione IS NULL ");
		
		//Join
		jpql.append("         AND rerm2.campo_pk_id = ts2.subdoc_id ");
		jpql.append("         AND rm2.regmovfin_id = rerm2.regmovfin_id ");
		jpql.append("         AND rrmfs2.regmovfin_id = rerm2.regmovfin_id ");
		jpql.append("         AND drms2.regmovfin_stato_id = rrmfs2.regmovfin_stato_id ");
		jpql.append("         AND rerm2.evento_id = de2.evento_id ");
		jpql.append("         AND p2.pnota_id = tme2.regep_id ");
		jpql.append("         AND rerm2.regmovfin_id = tme2.regmovfin_id ");
		jpql.append("         AND de2.collegamento_tipo_id = ct2.collegamento_tipo_id ");
		jpql.append("         AND rpns2.pnota_id = p2.pnota_id ");
		jpql.append("         AND rpns2.pnota_stato_id = pns2.pnota_stato_id ");
		
		//Filters
//		jpql.append("         AND drms2.regmovfin_stato_code = 'A' ");
		jpql.append("         AND pns2.pnota_stato_code = 'P' ");
		jpql.append("         AND ct2.collegamento_tipo_code IN ('")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoSpesa.getCodice())
			.append("', '")
			.append(SiacDCollegamentoTipoEnum.SubdocumentoEntrata.getCodice())
			.append("') ");
		jpql.append("     ) ");
		jpql.append("     SELECT 1 ");
		jpql.append("     FROM siac_t_subdoc s1 ");
		jpql.append("     WHERE ( ");
		jpql.append("         de.evento_code IN (:eventoCassaEconomaleCodes) ");
		jpql.append("         AND s1.subdoc_id = rerm.campo_pk_id ");
		jpql.append("         AND s1.subdoc_id NOT IN ( ");
		jpql.append("             SELECT d1.subdoc_id FROM due d1 WHERE d1.evento_code IN (:eventoCassaEconomaleCodes) ");
		jpql.append("         ) ");
		jpql.append("     ) OR ( ");
		jpql.append("         de.evento_code IN (:eventoCodes) ");
		jpql.append("         AND s1.subdoc_id = rerm.campo_pk_id ");
		jpql.append("         AND s1.subdoc_id NOT IN ( ");
		jpql.append("             SELECT d1.subdoc_id FROM due d1 WHERE d1.evento_code IN (:eventoCodes) ");
		jpql.append("         ) ");
		jpql.append("     ) ");
		jpql.append(" ) ");
		jpql.append(" ) ");
		
		Collection<String> eventoCassaEconomaleCodes = new ArrayList<String>();
		for(SiacDEventoEnum sdee: SiacDEventoEnum.byCollegamentoTipoECassaEconomale(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, true)){
			eventoCassaEconomaleCodes.add(sdee.getCodice());
		}
		Collection<String> eventoCodes = new ArrayList<String>();
		for(SiacDEventoEnum sdee: SiacDEventoEnum.byCollegamentoTipoECassaEconomale(SiacDCollegamentoTipoEnum.SubdocumentoEntrata, false)){
			eventoCodes.add(sdee.getCodice());
		}
		for(SiacDEventoEnum sdee: SiacDEventoEnum.byCollegamentoTipoECassaEconomale(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, false)){
			eventoCodes.add(sdee.getCodice());
		}
		
		param.put("eventoCassaEconomaleCodes", eventoCassaEconomaleCodes);
		param.put("eventoCodes", eventoCodes);
	}

	private void appendFilterSubdocumentiConRegistrazione(StringBuilder jpql, Map<String, Object> param, Collection<String> collegamentoTipoCodesSubdoc, 
					Collection<String> eventoCassaEconomaleCodes, Collection<String> eventoCodes, String ambitoCode) {
		
		
		jpql.append(" AND EXISTS( ");
		jpql.append("     SELECT tme ");
		jpql.append("     FROM SiacTMovEp tme, SiacREventoRegMovfin rerm, SiacDEvento de ");
		jpql.append("     WHERE tme.siacTPrimaNota = p "); //join
		jpql.append("     AND tme.dataCancellazione IS NULL ");
		jpql.append("     AND tme.siacTRegMovfin.dataCancellazione IS NULL ");
		jpql.append("     AND rerm.siacTRegMovfin = tme.siacTRegMovfin "); //join
		jpql.append("     AND rerm.dataCancellazione IS NULL ");
		jpql.append("     AND de = rerm.siacDEvento "); //join
		jpql.append("     AND NOT EXISTS ( "); //Non esistono...
		jpql.append("         SELECT ts ");
		jpql.append("         FROM SiacTSubdoc ts, SiacREventoRegMovfin rerm2, SiacRRegMovfinStato rrmfs "); //...subdoc del documento...
		jpql.append("         WHERE ts.dataCancellazione IS NULL ");
		jpql.append("         AND ts.siacTDoc.docId = ( ");
		jpql.append("             SELECT ts2.siacTDoc.docId ");
		jpql.append("             FROM SiacTSubdoc ts2 ");
		jpql.append("             WHERE ts2.subdocId = rerm.campoPkId ");
		jpql.append("         ) ");
		jpql.append("         AND rerm2.dataCancellazione IS NULL ");
		jpql.append("         AND rerm2.campoPkId = ts.subdocId "); //join
		jpql.append("         AND rerm2.siacTRegMovfin.dataCancellazione IS NULL ");
		jpql.append("         AND rrmfs.dataCancellazione IS NULL ");   
		jpql.append("         AND rrmfs.siacTRegMovfin = rerm2.siacTRegMovfin ");
		jpql.append("         AND rrmfs.siacDRegMovfinStato.regmovfinStatoCode = 'A'  ");  //...con la registrazione Annullata.
		jpql.append("         AND ((de.eventoCode in (:eventoCassaEconomaleCodes) AND rerm2.siacDEvento.eventoCode in (:eventoCassaEconomaleCodes)) ");
		jpql.append("             OR (de.eventoCode in (:eventoCodes) AND rerm2.siacDEvento.eventoCode in (:eventoCodes))) ");
		jpql.append("         AND rerm2.siacTRegMovfin.siacDAmbito.ambitoCode = :ambitoCode ");
		jpql.append("     ) ");
		jpql.append(" ) ");
		
		param.put("eventoCassaEconomaleCodes", eventoCassaEconomaleCodes);
		param.put("eventoCodes", eventoCodes);
		param.put("ambitoCode", ambitoCode);
	}
	
	

	@Override
	public Page<SiacTPrimaNota> ricercaPrimeNote(Integer enteProprietarioId,
			Integer annoPrimaNota,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			SiacDEventoTipo siacDEventoTipo,
			SiacDAmbitoEnum siacDAmbitoEnum,
			Integer pnotaProgressivogiornale,
			Collection<Integer> idMovimento, 
			String classifCode, 
			Pageable pageable) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTPrimaNota p ");
		jpql.append(" WHERE p.dataCancellazione IS NULL ");
		jpql.append(" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(annoPrimaNota != null && annoPrimaNota != 0){
			jpql.append(" AND p.siacTBil.siacTPeriodo.anno = :anno ");
			param.put("anno", annoPrimaNota.toString());
		}
		
		appendFilterSiacDCausaleEpTipoEnum(jpql, param, siacDCausaleEpTipoEnum);
		appendFilterSiacDAmbitoEnum(jpql, param, siacDAmbitoEnum);
		appendFilterPnotaNumeroRegistrazione(jpql, param, pnotaProgressivogiornale);
		appendFilterSiacDPrimaNotaStatoEnum(jpql, param, SiacDPrimaNotaStatoEnum.Definitivo);
		
		if(siacDEventoTipo != null){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("	  AND EXISTS ( FROM m.siacTCausaleEp.siacREventoCausales ev");
			jpql.append("    			   WHERE ev.dataCancellazione IS NULL ");
			jpql.append("     			   AND ev.siacDEvento.siacDEventoTipo.eventoTipoCode = :eventoTipoCode");
			jpql.append(" 		) ");
			jpql.append(" ) ");
			param.put("eventoTipoCode", siacDEventoTipo.getEventoTipoCode());
		}
		
		if(idMovimento != null && !idMovimento.isEmpty()){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM m.siacTRegMovfin.siacREventoRegMovfins re  ");
			jpql.append("         WHERE re.dataCancellazione IS NULL ");
			jpql.append("         AND re.campoPkId IN ( :campoPkId ) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			param.put("campoPkId", idMovimento);
		}
		
		if(classifCode != null && StringUtils.isNotBlank(classifCode)){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM p.siacTMovEps m ");
			jpql.append("     WHERE m.dataCancellazione IS NULL ");
			jpql.append("     AND m.siacTRegMovfin.siacTClass2.classifCode = :classifCode ");
			jpql.append(" ) ");
			param.put("classifCode", classifCode);
		}
		
		jpql.append(" ORDER BY p.pnotaNumero");
		
		return getPagedList(jpql.toString(), param, pageable);
		
	}

	@Override
	public Page<Integer> ricercaUidSiacTBaseByPrimaNota(Integer primaNotaId, SiacDCollegamentoTipoEnum collegamentoTipo, Pageable pageable) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rerm.campoPkId ")
			.append(" FROM SiacREventoRegMovfin rerm ")
			.append(" WHERE rerm.dataCancellazione IS NULL ")
			.append(" AND rerm.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode = :collegamentoTipoCode");
		param.put("collegamentoTipoCode", collegamentoTipo.getCodice());
		
		jpql.append(" AND EXISTS ( ")
			.append("     FROM rerm.siacTRegMovfin.siacTMovEps tme ")
			.append("     WHERE tme.dataCancellazione IS NULL ")
			.append("     AND tme.siacTPrimaNota.pnotaId = :pnotaId ")
			.append(" ) ");
		param.put("pnotaId", primaNotaId);
		
		jpql.append(" ORDER BY rerm.campoPkId ");
		
		//Query query = createQuery(jpql.toString(), param);
		//@SuppressWarnings("unchecked")
		//Page<Integer> res = query.getResultList();
		
		//return res;
		return getPagedList(jpql.toString(), param, pageable);
	}
	@Override
	public List<SiacTPrimaNota> ricercaScrittureByEntitaInventario(Integer enteProprietarioId, Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum, String statoOperativoCode, String statoAccettazionePrimaNotaProv, String statoAccettazionePrimaNotaDef, Boolean escludiAnnullati,String ambitoCode) {
		
		final String methodName = "ricercaScrittureByEntitaInventario";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		
		componiQueryRicercaScrittureCespiti(jpql, param, enteProprietarioId, uidEntitaCollegata,uidCespiteCollegatoAdEntitaGenerante, uidPrimaNota, jpqlEnum, statoOperativoCode, statoAccettazionePrimaNotaProv, statoAccettazionePrimaNotaDef, escludiAnnullati, ambitoCode);
		
		jpql.append(" ORDER BY rpn.").append(jpqlEnum.getNomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante()).append(".pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTPrimaNota> res = query.getResultList();
		return res;
		
	}
	
	@Override
	public Page<SiacTPrimaNota>ricercaScrittureRegistroAByCespite(Integer enteProprietarioId, Integer uidCespite, Pageable pageable){
		final String methodName = "ricercaScrittureByEntitaInventario";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT rpn.siacTPrimaNotaPadre ");
		jpql.append(" FROM SiacRPrimaNota rpn  ");
		jpql.append(" WHERE rpn.dataCancellazione IS NULL ");
		jpql.append(" AND rpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId  ");
		jpql.append(" AND rpn.siacTPrimaNotaPadre.siacDAmbito.ambitoCode = 'AMBITO_FIN'  ");
		
		jpql.append(" AND EXISTS ( FROM SiacRCespitiMovEpDet rcmed ");
		jpql.append("     WHERE rcmed.dataCancellazione IS NULL ");
		
		if(uidCespite != null && uidCespite.intValue() != 0) {
			jpql.append(" AND rcmed.siacTCespiti.cesId = :cesId ");
			param.put("cesId", uidCespite);
		}
		jpql.append(" 	  AND rcmed.siacTMovEpDet.siacTMovEp.siacTPrimaNota = rpn.siacTPrimaNotaFiglio  ");
		jpql.append(" 	  AND rcmed.siacTMovEpDet.siacTMovEp.siacTPrimaNota.siacDAmbito.ambitoCode = 'AMBITO_INV'");
		jpql.append(" )");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		
		jpql.append(" ORDER BY rpn.siacTPrimaNotaPadre.pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	/**
	 * @param jpql
	 * @param param
	 * @param enteProprietarioId
	 * @param uidEntitaCollegata
	 * @param uidCespiteCollegatoAdEntitaGenerante
	 * @param jpqlEnum
	 */
	private void componiQueryDaJpqlEnum(StringBuilder jpql, Map<String, Object> param, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum,  Integer enteProprietarioId,
			Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota) {
		
		jpql.append(" FROM ").append(jpqlEnum.getNomeTabellaCollegamentoEntitaGeneranteEPrimaNota()).append(" rpn, SiacTPrimaNota tp ");
		jpql.append(" WHERE rpn.dataCancellazione IS NULL");
		jpql.append(" AND rpn.").append(jpqlEnum.getNomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante()).append(" = tp ");
		jpql.append(" AND tp.dataCancellazione IS NULL ");
		jpql.append(" AND rpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(uidEntitaCollegata != null){
			jpql.append(" AND rpn.").append(jpqlEnum.getPatternUidEntita()).append( " = :uidEntita ");
			param.put("uidEntita", uidEntitaCollegata);
		}
		
		if(uidPrimaNota != null && uidPrimaNota.intValue()!= 0) {
			jpql.append(" AND tp.pnotaId = :pnotaId ");
			param.put("pnotaId", uidPrimaNota);
		}
		
		if(uidCespiteCollegatoAdEntitaGenerante != null){
			jpql.append("AND EXISTS ( FROM SiacTCespiti tc ");
			jpql.append("		WHERE tc = rpn.").append(jpqlEnum.getCollegamentoSiacTCespiti());
			jpql.append("		AND tc.dataCancellazione IS NULL  ");
			jpql.append("		AND tc.cesId = :cesId  ");
			jpql.append("	)  ");
			
			param.put("cesId", uidCespiteCollegatoAdEntitaGenerante);
		}
	}

	private void componiQueryRicercaScrittureCespiti(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, 
			Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum,
			String statoOperativoPrimaNotaCode,  String statoAccettazionePrimaNotaProv, String statoAccettazionePrimaNotaDefCode, Boolean escludiAnnullati,
			String ambitoCode) {
		
		jpql.append(" SELECT tp ");
		componiQueryDaJpqlEnum(jpql, param, jpqlEnum, enteProprietarioId, uidEntitaCollegata, uidCespiteCollegatoAdEntitaGenerante, uidPrimaNota);
		
		if(StringUtils.isNotBlank(statoOperativoPrimaNotaCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPrimaNotaStato rpns ");
			jpql.append(" WHERE rpns.dataCancellazione IS NULL ");
			jpql.append(" AND rpns.siacTPrimaNota = tp ");
			jpql.append(" AND rpns.siacDPrimaNotaStato.pnotaStatoCode = :pnotaStatoCode ");
			jpql.append(" ) ");
			param.put("pnotaStatoCode", statoOperativoPrimaNotaCode);
		}else if(Boolean.TRUE.equals(escludiAnnullati)) {
			jpql.append(" AND NOT EXISTS ( ");
			jpql.append(" FROM SiacRPrimaNotaStato rpns ");
			jpql.append(" WHERE rpns.dataCancellazione IS NULL ");
			jpql.append(" AND rpns.siacTPrimaNota = tp ");
			jpql.append(" AND rpns.siacDPrimaNotaStato.pnotaStatoCode = 'A' ");
			jpql.append(" ) ");
		}
		
		if(StringUtils.isNotBlank(statoAccettazionePrimaNotaDefCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPnProvAccettazioneStato rap ");
			jpql.append(" WHERE rap.dataCancellazione IS NULL ");
			jpql.append(" AND rap.siacTPrimaNota = tp ");
			jpql.append(" AND rap.siacDPnProvAccettazioneStato.pnStaAccDefCode = :pnStaAccProvCode ");
			jpql.append(" ) ");
			param.put("pnStaAccDefCode", statoAccettazionePrimaNotaDefCode);
		}
		
		if(StringUtils.isNotBlank(statoAccettazionePrimaNotaDefCode)) {
			jpql.append(" AND EXISTS ( ");
			jpql.append(" FROM SiacRPnDefAccettazioneStato rap ");
			jpql.append(" WHERE rap.dataCancellazione IS NULL ");
			jpql.append(" AND rap.siacTPrimaNota = tp ");
			jpql.append(" AND rap.siacDPnDefAccettazioneStato.pnStaAccDefCode = :pnStaAccDefCode ");
			jpql.append(" ) ");
			param.put("pnStaAccDefCode", statoAccettazionePrimaNotaDefCode);
		}
		
		if(StringUtils.isNotBlank(ambitoCode)) {
			jpql.append(" AND rpn.").append(jpqlEnum.getNomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante()).append(".siacDAmbito.ambitoCode = :ambitoCode");
			param.put("ambitoCode", ambitoCode);
		}
	}


	private void appendFilterSiacTCespite(StringBuilder jpql, Map<String, Object> param, Integer cesId) {

		if(nonZero(cesId)) {		
			jpql.append(" AND ( ");
				jpql.append("     EXISTS ( ");				
					jpql.append("     FROM  SiacRCespitiPrimaNota rcpn ");
					jpql.append("     WHERE rcpn.dataCancellazione IS NULL ");
					jpql.append("     AND rcpn.siacTPrimaNota = p ");
					jpql.append("     AND rcpn.siacTCespiti.cesId = :cesId ");				
				jpql.append(" ) ");
	
				jpql.append(" OR ");				
				jpql.append("     EXISTS ( ");								
					jpql.append("     FROM  SiacRCespitiVariazionePrimaNota rcvpn ");
					jpql.append("     WHERE rcvpn.dataCancellazione IS NULL ");
					jpql.append("     AND rcvpn.siacTPrimaNota = p ");
					jpql.append("     AND rcvpn.siacTCespitiVariazione.siacTCespiti.cesId = :cesId ");				
				jpql.append(" ) ");

				jpql.append(" OR ");				
				jpql.append("     EXISTS ( ");				

					jpql.append("     FROM  SiacRCespitiDismissioniPrimaNota rcdpn, SiacTCespiti tc");
					jpql.append("     WHERE rcdpn.dataCancellazione IS NULL ");
					jpql.append("     AND rcdpn.siacTPrimaNota = p ");
					jpql.append("     AND rcdpn.siacTCespitiDismissioni = tc.siacTCespitiDismissioni ");	
					jpql.append("     AND tc.cesId = :cesId ");		
					
					jpql.append("  	  AND EXISTS ( FROM SiacTCespiti tc ");
					jpql.append(" 				WHERE tc = rcdpn.siacTCespitiAmmortamentoDett.siacTCespitiAmmortamento.siacTCespiti ");
					jpql.append("  				AND tc.dataCancellazione IS NULL  ");
					jpql.append("  				AND tc.cesId = :cesId  ");
					jpql.append(" 	  )  ");

				
				jpql.append(" ) ");
				
				jpql.append(" OR ");				
				jpql.append("     EXISTS ( ");				
					jpql.append("     FROM  SiacTCespitiAmmortamentoDett rtcad ");
					jpql.append("     WHERE rtcad.dataCancellazione IS NULL ");
					jpql.append("     AND rtcad.siacTPrimaNota = p ");
					jpql.append("     AND rtcad.siacTCespitiAmmortamento.siacTCespiti.cesId = :cesId ");	
				jpql.append(" ) ");
				
			jpql.append(" ) ");
			
			param.put("cesId", cesId);
		
		}
	}

	
	/**
	 * Annulla.
	 *
	 * @param siacTPrimaNota the siac T prima nota
	 */
	@Override
	public void annulla(SiacTPrimaNota siacTPrimaNota) {
		SiacTPrimaNota eAttuale = this.findById(siacTPrimaNota.getUid());
		Date now = new Date();
		aggiornaStatoOperativo(eAttuale, now, siacTPrimaNota.getLoginOperazione(), StatoOperativoPrimaNota.ANNULLATO);
		super.update(eAttuale);	
	}
	
	/**
	 * Aggiorna stato prima nota.
	 *
	 * @param uidPrimaNota the uid prima nota
	 * @param loginOperazione the login operazione
	 * @param statoOperativoPrimaNota the stato operativo prima nota
	 * @param statoAccettazionePrimaNotaProvvisoria the stato accettazione prima nota provvisoria
	 */
	@Override
	public void aggiornaStatoPrimaNota(Integer uidPrimaNota, String loginOperazione, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaProvvisoria statoAccettazionePrimaNotaProvvisoria) {
		SiacTPrimaNota eAttuale = this.findById(uidPrimaNota);
		Date now = new Date();
		aggiornaStatoOperativo(eAttuale, now, loginOperazione, statoOperativoPrimaNota);
		aggiornaStatoAccettazionePrimaNotaProvvisoria(eAttuale, loginOperazione, now, statoOperativoPrimaNota, statoAccettazionePrimaNotaProvvisoria);
		super.update(eAttuale);
		
	}
	
	@Override
	public void aggiornaStatoPrimaNota(int uidPrimaNota, String loginOperazione, StatoOperativoPrimaNota statoOperativoPrimaNota,
			StatoAccettazionePrimaNotaDefinitiva statoAccettazionePrimaNotaDefinitiva) {
		SiacTPrimaNota eAttuale = this.findById(uidPrimaNota);
		Date now = new Date();
		aggiornaStatoOperativo(eAttuale, now, loginOperazione, statoOperativoPrimaNota);
		aggiornaStatoAccettazionePrimaNotaDefinitiva(eAttuale, loginOperazione, now, statoOperativoPrimaNota, statoAccettazionePrimaNotaDefinitiva);
		super.update(eAttuale);
		
	}

	private void aggiornaStatoAccettazionePrimaNotaDefinitiva(SiacTPrimaNota eAttuale, String loginOperazione, Date now, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaDefinitiva statoAccettazionePrimaNotaDefinitiva) {
		if(StatoOperativoPrimaNota.PROVVISORIO.equals(statoOperativoPrimaNota) || statoAccettazionePrimaNotaDefinitiva == null) {
			return;
		}
		//
		
		if(eAttuale.getSiacRPnDefAccettazioneStatos() == null) {
			eAttuale.setSiacRPnDefAccettazioneStatos(new ArrayList<SiacRPnDefAccettazioneStato>());
		}
		List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStatos  = eAttuale.getSiacRPnDefAccettazioneStatos();
		boolean addNewStato = siacRPnDefAccettazioneStatos.isEmpty();
		SiacDPnDefAccettazioneStatoEnum  siacDPnDefAccettazioneStatoEnum = SiacDPnDefAccettazioneStatoEnum.byStatoAccettazioneDef(statoAccettazionePrimaNotaDefinitiva);
		for(SiacRPnDefAccettazioneStato r : eAttuale.getSiacRPnDefAccettazioneStatos()){
			if(r.getDataCancellazione() == null && r.getSiacDPnDefAccettazioneStato()!= null && !siacDPnDefAccettazioneStatoEnum.getCodice().equals(r.getSiacDPnDefAccettazioneStato().getPnStaAccDefCode())) {
				r.setDataCancellazioneIfNotSet(now);
				addNewStato = true;
			}				
		}
		if(!addNewStato) {
			return;
		}
		SiacRPnDefAccettazioneStato siacRPrimaNotaStato = new SiacRPnDefAccettazioneStato();
		SiacDPnDefAccettazioneStato siacDPnDefAccettazioneStato = eef.getEntity(siacDPnDefAccettazioneStatoEnum, eAttuale.getSiacTEnteProprietario().getUid());
		siacRPrimaNotaStato.setSiacDPnDefAccettazioneStato(siacDPnDefAccettazioneStato);	
		siacRPrimaNotaStato.setSiacTPrimaNota(eAttuale);			
		siacRPrimaNotaStato.setSiacTEnteProprietario(eAttuale.getSiacTEnteProprietario());
		siacRPrimaNotaStato.setDataInizioValidita(now);
		siacRPrimaNotaStato.setDataCreazione(now);
		siacRPrimaNotaStato.setDataModifica(now);
		siacRPrimaNotaStato.setLoginOperazione(loginOperazione);
		eAttuale.addSiacRPnDefAccettazioneStatos(siacRPrimaNotaStato);		
	}

	/**
	 * Aggiorna stato accettazione prima nota provvisoria.
	 *
	 * @param eAttuale the e attuale
	 * @param loginOperazione the login operazione
	 * @param now the now
	 * @param statoOperativoPrimaNota the stato operativo prima nota
	 * @param statoAccettazionePrimaNotaProvvisoria the stato accettazione prima nota provvisoria
	 */
	private void aggiornaStatoAccettazionePrimaNotaProvvisoria(SiacTPrimaNota eAttuale, String loginOperazione, Date now, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaProvvisoria statoAccettazionePrimaNotaProvvisoria) {
		if(!StatoOperativoPrimaNota.PROVVISORIO.equals(statoOperativoPrimaNota) || statoAccettazionePrimaNotaProvvisoria == null) {
			return;
		}
		boolean addNewStato = false;
		SiacDPnProvAccettazioneStatoEnum  siacDPnProvAccettazioneStatoEnum = SiacDPnProvAccettazioneStatoEnum.byStatoAccettazioneProv(statoAccettazionePrimaNotaProvvisoria);
		for(SiacRPnProvAccettazioneStato r : eAttuale.getSiacRPnProvAccettazioneStatos()){
			if(r.getDataCancellazione() == null && r.getSiacDPnProvAccettazioneStato()!= null && !siacDPnProvAccettazioneStatoEnum.getCodice().equals(r.getSiacDPnProvAccettazioneStato().getPnStaAccProvCode())) {
				r.setDataCancellazioneIfNotSet(now);
				addNewStato = true;
			}				
		}
		if(!addNewStato) {
			return;
		}
		SiacRPnProvAccettazioneStato siacRPrimaNotaStato = new SiacRPnProvAccettazioneStato();
		SiacDPnProvAccettazioneStato siacDPnProvAccettazioneStato = eef.getEntity(siacDPnProvAccettazioneStatoEnum, eAttuale.getSiacTEnteProprietario().getUid());
		siacRPrimaNotaStato.setSiacDPnProvAccettazioneStato(siacDPnProvAccettazioneStato);	
		siacRPrimaNotaStato.setSiacTPrimaNota(eAttuale);			
		siacRPrimaNotaStato.setSiacTEnteProprietario(eAttuale.getSiacTEnteProprietario());
		siacRPrimaNotaStato.setDataInizioValidita(now);
		siacRPrimaNotaStato.setDataCreazione(now);
		siacRPrimaNotaStato.setDataModifica(now);
		siacRPrimaNotaStato.setLoginOperazione(loginOperazione);
		eAttuale.addSiacRPnProvAccettazioneStato(siacRPrimaNotaStato);
	}
	
	/**
	 * @param loginOperazione
	 * @param statoOperativoPrimaNota
	 * @param eAttuale
	 * @param now
	 */
	private void aggiornaStatoOperativo(SiacTPrimaNota eAttuale, Date now, String loginOperazione, StatoOperativoPrimaNota statoOperativoPrimaNota) {
		boolean addNewStato = false;
		SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoOperativoEnum = SiacDPrimaNotaStatoEnum.byStatoOperativo(statoOperativoPrimaNota);
		for(SiacRPrimaNotaStato r : eAttuale.getSiacRPrimaNotaStatos()){
			if(r.getDataCancellazione() == null && r.getSiacDPrimaNotaStato() != null && !siacDPrimaNotaStatoOperativoEnum.getCodice().equals(r.getSiacDPrimaNotaStato().getPnotaStatoCode())) {
				r.setDataCancellazioneIfNotSet(now);
				addNewStato = true;
			}				
		}
		if(!addNewStato) {
			return;
		}
		SiacRPrimaNotaStato siacRPrimaNotaStato = new SiacRPrimaNotaStato();
		SiacDPrimaNotaStato siacDPrimaNotaStato = eef.getEntity(siacDPrimaNotaStatoOperativoEnum, eAttuale.getSiacTEnteProprietario().getUid());
		siacRPrimaNotaStato.setSiacDPrimaNotaStato(siacDPrimaNotaStato);	
		siacRPrimaNotaStato.setSiacTPrimaNota(eAttuale);			
		siacRPrimaNotaStato.setSiacTEnteProprietario(eAttuale.getSiacTEnteProprietario());
		siacRPrimaNotaStato.setDataInizioValidita(now);
		siacRPrimaNotaStato.setDataCreazione(now);
		siacRPrimaNotaStato.setDataModifica(now);
		siacRPrimaNotaStato.setLoginOperazione(loginOperazione);
		eAttuale.addSiacRPrimaNotaStato(siacRPrimaNotaStato);
	}
	
	//TODO ANTO
	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaLiberaRegistroA(Integer enteProprietarioId, Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum, SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, Integer pnotaNumero,
			Integer pnotaProgressivogiornale, Collection<String> siacDPrimaNotaStatoEnums,Collection<String> siacDPnDefAccettazioneStatoEnums, String pnotaDesc,
			Collection<Integer> eventoIds, SiacDEventoTipoEnum siacDEventoTipoEnum, Integer causaleEpId, Integer pdceContoId, Date dataRegistrazioneDa,
			Date dataRegistrazioneA, Date dataRegistrazioneProvvisoriaDa, Date dataRegistrazioneProvvisoriaA,
			Pageable pageable) {
		final String methodName = "ricercaSinteticaPrimaNotaLiberaRegistroA";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTPrimaNota p ");
		jpql.append(" WHERE p.dataCancellazione IS NULL ");
		jpql.append(" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND p.siacTBil.bilId = :bilId ");
		
		
		params.put("enteProprietarioId", enteProprietarioId);
		params.put("bilId", bilId); //siamo sicuri!?!?
		
		// Aggiunta dei filtri
		appendFilterSiacDAmbitoEnum(jpql, params, siacDAmbitoEnum);
		appendFilterSiacDCausaleEpTipoEnum(jpql, params, siacDCausaleEpTipoEnum);
		appendFilterPnotaNumero(jpql, params, pnotaNumero);
		appendFilterPnotaNumeroRegistrazione(jpql, params, pnotaProgressivogiornale);
		appendFilterSiacDEvento(jpql, params, eventoIds);
		appendFilterSiacTCausaleEp(jpql, params, causaleEpId);
		appendFilterSiacTPdceConto(jpql, params, pdceContoId);
		appendFilterSiacDPrimaNotaStatoEnums(jpql, params, siacDPrimaNotaStatoEnums);
		//SIAC-6564
		appendFilterSiacDPnDefAccettazioneStatoEnums(jpql, params, siacDPnDefAccettazioneStatoEnums);
		
		appendFilterPnotaDesc(jpql, params, pnotaDesc);
		appendFilterDataRegistrazioneDa(jpql, params, dataRegistrazioneDa);
		appendFilterDataRegistrazioneA(jpql, params, dataRegistrazioneA);
		appendFilterDataRegistrazioneProvvisoriaDa(jpql, params, dataRegistrazioneProvvisoriaDa);
		appendFilterDataRegistrazioneProvvisoriaA(jpql, params, dataRegistrazioneProvvisoriaA);
		
		// Per registro A
		appendFilterSiacDEventoTipo(jpql, params, siacDEventoTipoEnum);
		appendFilterHasSiacTPdceContoInventario(jpql, params, pdceContoId);
		
		jpql.append(" ORDER BY p.pnotaNumero ");
		
		log.info(methodName, "Query della prima nota libera registro A JPQL : " + jpql.toString());
		log.info(methodName, "Query della prima nota libera registro A params: " + params.toString());
		
		return getPagedList(jpql.toString(), params, pageable);
	}


	@Override
	public Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrataRegistroA(
			Integer enteProprietarioId,
			Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Integer pnotaNumero,
			Integer pnotaProgressivogiornale,
			Collection<String> siacDPrimaNotaStatoEnums,
			Collection<String> siacDPnDefAccettazioneStatoEnums,
			String pnotaDesc,
			Collection<Integer> eventoIds,
			Integer causaleEpId,
			Integer pdceContoId,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA,
			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos,
			Collection<Integer> listaEventoTipoId,
			List<Integer> eventoTipoIds,
			Collection<Integer> idMovimento,
			Integer classifId,
			List<Integer> docIds,
			Integer uidAttoAmm,
			Integer uidMovimentoGestione,
			Integer uidSubMovimentoGestione,
			Integer uidSac,
			Integer soggettoId,
			BigDecimal importoDocumentoDa,
			BigDecimal importoDocumentoA,
			Integer bilElemId,
			Integer gsaClassifId,
			Pageable pageable) {
		final String methodName = "ricercaSinteticaNativaPrimaNotaIntegrataRegistroA";
		
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		sql.append(" SELECT ");
		sql.append("     DISTINCT p.* ");
		sql.append(" FROM siac_t_prima_nota p ");
		// Joins
		sql.append(" JOIN siac_d_ambito da ON (p.ambito_id = da.ambito_id AND da.data_cancellazione IS NULL) ");
		sql.append(" JOIN siac_d_causale_ep_tipo cet ON (p.causale_ep_tipo_id = cet.causale_ep_tipo_id AND cet.data_cancellazione IS NULL) ");
		
		sql.append(" JOIN siac_r_prima_nota_stato rpns ON (rpns.pnota_id = p.pnota_id AND rpns.data_cancellazione IS NULL) ");
		sql.append(" JOIN siac_d_prima_nota_stato pns ON (rpns.pnota_stato_id = pns.pnota_stato_id AND pns.data_cancellazione IS NULL) ");
		
		sql.append(" JOIN siac_t_mov_ep tme ON (p.pnota_id = tme.regep_id AND tme.data_cancellazione IS NULL) ");
		sql.append(" JOIN siac_r_evento_reg_movfin rerm ON (rerm.regmovfin_id = tme.regmovfin_id AND rerm.data_cancellazione IS NULL) ");
		sql.append(" JOIN siac_d_evento de ON (rerm.evento_id = de.evento_id AND de.data_cancellazione IS NULL) ");
		sql.append(" JOIN siac_d_collegamento_tipo dct ON (de.collegamento_tipo_id = dct.collegamento_tipo_id AND dct.data_cancellazione IS NULL) ");

		//SIAC-6564 join utile solo se si filtra per stato accettazione
		if(siacDPnDefAccettazioneStatoEnums != null && !siacDPnDefAccettazioneStatoEnums.isEmpty()){
			sql.append(" LEFT OUTER JOIN siac_r_prima_nota rpn ON (rpn.pnota_id_da = p.pnota_id AND rpn.data_cancellazione IS NULL) ");
			sql.append(" LEFT OUTER JOIN siac_r_pn_def_accettazione_stato rpdas ON (rpn.pnota_id_a = rpdas.pnota_id AND rpdas.data_cancellazione IS NULL ) ");
			sql.append(" LEFT OUTER JOIN siac_d_pn_def_accettazione_stato dpdas ON (rpdas.pn_sta_acc_def_id = dpdas.pn_sta_acc_def_id AND dpdas.data_cancellazione IS NULL) ");
		}                                                                                                           

		//Base Filter
		sql.append(" WHERE p.data_cancellazione IS NULL ");
		sql.append(" AND p.ente_proprietario_id = :enteProprietarioId ");
		sql.append(" AND p.bil_id = :bilId ");
		
		params.put("enteProprietarioId", enteProprietarioId);
		params.put("bilId", bilId);
		
		//Aggiunta dei filtri opzionali
		appendNativeFilterSiacDAmbitoEnum(sql, params, siacDAmbitoEnum);
		appendNativeFilterSiacDCausaleEpTipoEnum(sql, params, siacDCausaleEpTipoEnum);
		appendNativeFilterPnotaNumero(sql, params, pnotaNumero);
		appendNativeFilterPnotaNumeroRegistrazione(sql, params, pnotaProgressivogiornale);
		appendNativeFilterSiacDPrimaNotaStatoEnums(sql, params, siacDPrimaNotaStatoEnums);
		//SIAC-6564
		appendNativeFilterSiacDPnDefAccettazioneStatoEnums(sql, params, siacDPnDefAccettazioneStatoEnums);
		
		appendNativeFilterPnotaDesc(sql, params, pnotaDesc);
		appendNativeFilterSiacTPdceConto(sql, params, pdceContoId);
		appendNativeFilterSiacTCausaleEp(sql, params, causaleEpId);
		appendNativeFilterSiacTSoggetto(sql, params, soggettoId);
		appendNativeFilterDataRegistrazioneDa(sql, params, dataRegistrazioneDa);
		appendNativeFilterDataRegistrazioneA(sql, params, dataRegistrazioneA);
		
		appendNativeFilterDataRegistrazioneProvvisoriaDa(sql, params, dataRegistrazioneProvvisoriaDa);
		appendNativeFilterDataRegistrazioneProvvisoriaA(sql, params, dataRegistrazioneProvvisoriaA);
		appendNativeFilterSiacDEventoIntegrata(sql, params, eventoIds);
		appendNativeFilterSiacDEventoTiposIntegrata(sql, params, eventoTipoIds);
		appendNativeFilterListaSiacDEventoTipoIntegrata(sql, params, listaEventoTipoId);
		appendNativeFilterSiacTClass(sql, params, classifId);
		appendNativeFilterIdMovimento(sql, params, idMovimento);
		appendNativeFilterIdDocumento(sql, params, docIds);
		appendNativeFilterIdAttoAmm(sql, params, uidAttoAmm, siacDCollegamentoTipos);
		
		appendNativeFilterIdMovGest(sql, params, uidMovimentoGestione, siacDCollegamentoTipos);
		
		appendNativeFilterIdSubMovGest(sql, params, uidSubMovimentoGestione, siacDCollegamentoTipos);
		
		appendNativeFilterIdBilElemBySac(sql, params, uidSac, siacDCollegamentoTipos);
		appendNativeFilterImportoDocumento(sql, params, importoDocumentoDa, importoDocumentoA, docIds);
		appendNativeFilterIdBilElem(sql, params, bilElemId, siacDCollegamentoTipos);
		appendNativeFilterIdGsaClassif(sql, params, gsaClassifId);
		
		// Per registro A
		appendNativeFilterHasSiacTPdceContoInventario(sql, params, pdceContoId);
		
		sql.append(" ORDER BY p.pnota_numero ");
		
		log.info(methodName, "Query della prima nota integrata registro A SQL to execute: " + sql.toString());
		log.info(methodName, "Query della prima nota integrata registro A SQL; params: " + params.toString());

		return getNativePagedList(sql.toString(), params, pageable, SiacTPrimaNota.class);
	}

	/**
	 * Count prima nota collegata ad entita tramite jpql.
	 *
	 * @param uidEntitaCollegata the uid entita collegata
	 * @param uidCespiteCollegatoAdEntitaGenerante the uid cespite collegato ad entita generante
	 * @param uidPrimaNota the uid prima nota
	 * @param enteProprietarioId the ente proprietario id
	 * @param jpqlEnum the jpql enum
	 * @return the long
	 */
	@Override
	public Long countPrimaNotaCollegataAdEntitaTramiteJpql(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum) {
		final String methodName = "isPrimaNotaCollegataAdEntitaTramiteJpqlEnum";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT COALESCE(COUNT(*), 0) ");
		componiQueryDaJpqlEnum(jpql, param, jpqlEnum, enteProprietarioId, uidEntitaCollegata, uidCespiteCollegatoAdEntitaGenerante, uidPrimaNota);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		
		return (Long) query.getSingleResult();
	}
	
	/**
	 * Gets the prima nota collegata ad entita tramite jpql.
	 *
	 * @param uidEntitaCollegata the uid entita collegata
	 * @param uidCespiteCollegatoAdEntitaGenerante the uid cespite collegato ad entita generante
	 * @param uidPrimaNota the uid prima nota
	 * @param enteProprietarioId the ente proprietario id
	 * @param jpqlEnum the jpql enum
	 * @return the prima nota collegata ad entita tramite jpql
	 */
	@Override
	public List<SiacTBase> getEntitaCespiteTramiteJpql(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum) {
		final String methodName = "isPrimaNotaCollegataAdEntitaTramiteJpqlEnum";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT rpn.").append(jpqlEnum.getPercorsoDaTabellaCollegamentoAEntitaGenerante());
		componiQueryDaJpqlEnum(jpql, param, jpqlEnum, enteProprietarioId, uidEntitaCollegata, uidCespiteCollegatoAdEntitaGenerante, uidPrimaNota);
		
		jpql.append(" ORDER BY rpn.siacTPrimaNota.pnotaNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTBase> res = query.getResultList();
		return res;
	}
	
	@Override
	@Deprecated
	public	Page<SiacTMovEp> ricercaSinteticaMovimentoEPRegistroA(Integer uidPrimaNota, Integer enteProprietarioId, Pageable pageable){
		final String methodName ="ricercaSinteticaMovimentoEPRegistroA";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		componiQueryRicercaSinteticaMovimentoEPRegistroA(jpql, param, uidPrimaNota, enteProprietarioId);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaMovimentoEPRegistroA(StringBuilder jpql, Map<String, Object> param, Integer uidPrimaNota, Integer enteProprietarioId) {
		jpql.append(" FROM SiacTMovEp tme ");
		jpql.append(" WHERE tme.dataCancellazione IS NULL ");
		jpql.append(" AND tme.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		if(uidPrimaNota != null && uidPrimaNota.intValue() != 0) {
			jpql.append(" AND tme.siacTPrimaNota.pnotaId = :pnotaId");
			param.put("pnotaId", uidPrimaNota);
		}
		param.put("enteProprietarioId", enteProprietarioId);
	}

	public List<Object[]> calcolaImportiRegistroA(Integer pnotaId){
		final String methodName = "calcolaImportiRegistroA";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" SELECT tpc.pdceContoCode, tpc.pdceContoDesc, tmd.movepDetImporto, COALESCE(SUM(rmed.importoSuPrimaNota), 0), tmd.movepDetId" );
		jpql.append(" FROM SiacTPdceConto tpc, SiacTMovEpDet tmd, SiacRCespitiMovEpDet rmed ");
		jpql.append(" WHERE tmd.siacTPdceConto = tpc " );
		jpql.append(" AND rmed.siacTMovEpDet = tmd " );
		jpql.append(" AND tmd.siacTMovEp.siacTPrimaNota.pnotaId = :pnotaId ");
		jpql.append(" AND tpc.dataCancellazione IS NULL  ");
		jpql.append(" AND tmd.dataCancellazione IS NULL  ");
		jpql.append(" AND rmed.dataCancellazione IS NULL	");
		jpql.append(" GROUP BY tpc.pdceContoCode, tpc.pdceContoDesc, tmd.movepDetImporto, tmd.movepDetId ");
		
		params.put("pnotaId", pnotaId);
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		Query query = createQuery(jpql.toString(), params);
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
		return result;
	}
	
	public Page<SiacTMovEpDet> ricercaSinteticaMovimentoDetRegistroA(Integer uidPrimaNota, Integer enteProprietarioId, Pageable pageable){
		final String methodName ="ricercaSinteticaMovimentoEPRegistroA";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		componiQueryRicercaSinteticaMovimentoDettaglioRegistroA(jpql, param, uidPrimaNota, enteProprietarioId);
		
		jpql.append(" ORDER BY tmdet.siacTMovEp.movepId ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	private void componiQueryRicercaSinteticaMovimentoDettaglioRegistroA(StringBuilder jpql, Map<String, Object> param, Integer uidPrimaNota, Integer enteProprietarioId) {
		jpql.append(" FROM SiacTMovEpDet tmdet ");
		jpql.append(" WHERE tmdet.dataCancellazione IS NULL ");
		jpql.append(" AND tmdet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		jpql.append(" AND tmdet.siacTPdceConto.siacDPdceContoTipo.pdceCtTipoCode = 'CES' ");
		if(uidPrimaNota != null && uidPrimaNota.intValue() != 0) {
			jpql.append(" AND tmdet.siacTMovEp.siacTPrimaNota.pnotaId = :pnotaId");
			param.put("pnotaId", uidPrimaNota);
		}
		param.put("enteProprietarioId", enteProprietarioId);
	}
	
}
