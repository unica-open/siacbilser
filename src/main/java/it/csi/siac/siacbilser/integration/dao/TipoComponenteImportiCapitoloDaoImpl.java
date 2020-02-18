/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoComponenteImportiCapitoloDaoImpl extends ExtendedJpaDao<SiacDBilElemDetCompTipo, Integer> implements TipoComponenteImportiCapitoloDao {
	
	@Override
	public SiacDBilElemDetCompTipo create(SiacDBilElemDetCompTipo entity){
		
		Date now = new Date();
		entity.setDataModifica(now);
		entity.setDataCreazione(now);
		entity.setUid(null);
		super.save(entity);
		return entity;
	}

	@Override
	public SiacDBilElemDetCompTipo update(SiacDBilElemDetCompTipo entity){
		// SiacDBilElemDetCompTipo dAttuale = this.findById(d.getUid());
		
		Date now = new Date();
		entity.setDataModifica(now);

		Date dataInizioValidita = entity.getDataInizioValidita();
		
		super.update(entity);
		
		forceUpdateDataInizioValidita(entity.getUid(), dataInizioValidita);
		
		return entity;
	}
	
	@Override
	public List<SiacDBilElemDetCompTipo> ricercaTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			String codiceTipoStato,
			Integer anno,
			Integer annoBilancio,
			Boolean soloValidiPerBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere,
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		buildJpqlRicercaTipoComponenteImportiCapitolo(enteProprietarioId, tipoGestioneSoloAutomatica,
				descrizione, codiceMacroTipo, codiceSottoTipo, codiceTipoAmbito,
				codiceTipoFonte, codiceTipoFase, codiceTipoDef, codiceTipoStato, anno, annoBilancio, soloValidiPerBilancio,
				codiciMacroTipoDaEscludere, codiciSottoTipoDaEscludere, propostaDefaultComponenteImportiCapitoloDaEscludere, jpql, params);

		return getList(jpql.toString(), params);
	}	
	
	@Override
	public Page<SiacDBilElemDetCompTipo> ricercaPaginataTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			Boolean tipoGestioneSoloAutomatica,
			String descrizione,
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			String codiceTipoStato,
			Integer anno,
			Integer annoBilancio,
			Boolean soloValidiPerBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere,
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere,
			Pageable pageable) {
		final String methodName = "ricercaPaginataTipoComponenteImportiCapitolo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		buildJpqlRicercaTipoComponenteImportiCapitolo(enteProprietarioId,tipoGestioneSoloAutomatica, descrizione, codiceMacroTipo,
				codiceSottoTipo, codiceTipoAmbito, codiceTipoFonte, codiceTipoFase,
				codiceTipoDef, codiceTipoStato, anno, annoBilancio, soloValidiPerBilancio, codiciMacroTipoDaEscludere, codiciSottoTipoDaEscludere, propostaDefaultComponenteImportiCapitoloDaEscludere, jpql, params);
		log.debug(methodName, "JPQL TO EXECUTE: "+ jpql.toString());
		
		return getPagedList(jpql.toString(), params, pageable);
	}
	
	private void buildJpqlRicercaTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo, 
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			String codiceTipoStato,
			Integer anno,
			Integer annoBilancio,
			Boolean soloValidiPerBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere,
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere,
			StringBuilder jpql, 
			Map<String, Object> params) {
		
		params.put("enteProprietarioId", enteProprietarioId);
		
		jpql.append(" SELECT dbedct ")
			.append(" FROM SiacDBilElemDetCompTipo dbedct");
		if(codiciSottoTipoDaEscludere != null && !codiciSottoTipoDaEscludere.isEmpty()) {
			jpql.append(" LEFT JOIN dbedct.siacDBilElemDetCompSottoTipo sottoTipo ");
		}
		jpql.append(" WHERE dbedct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
			.append(" AND dbedct.dataInizioValidita < CURRENT_TIMESTAMP ")
			//VISUALIZZA FINE VALIDITA
			.append(" AND (dbedct.dataFineValidita IS NULL OR dbedct.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" AND dbedct.dataCancellazione IS NULL ");
		
		if (StringUtils.isNotBlank(descrizione)) {
			jpql.append(" AND dbedct.elemDetCompTipoDesc LIKE CONCAT('%', :descrizione, '%') ");
			params.put("descrizione", descrizione);
		}
		
		if(tipoGestioneSoloAutomatica != null) {
			jpql.append(" AND dbedct.elemDetCompTipoGestAut = :elemDetCompTipoGestAut ");
			params.put("elemDetCompTipoGestAut", tipoGestioneSoloAutomatica);
		}
		
		appendCodiceTipoClause("MacroTipo", codiceMacroTipo, jpql, params);
		appendCodiceTipoClause("SottoTipo", codiceSottoTipo, jpql, params);
		appendCodiceTipoClause("TipoAmbito", codiceTipoAmbito, jpql, params);
		appendCodiceTipoClause("TipoFonte", codiceTipoFonte, jpql, params);
		appendCodiceTipoClause("TipoFase", codiceTipoFase, jpql, params);
		appendCodiceTipoClause("TipoDef", codiceTipoDef, jpql, params);
		//SIAC-7263
		appendCodiceTipoClause("TipoStato", codiceTipoStato, jpql, params);
		
		appendAnnoClause(anno, jpql, params);
		appendAnnoBilancioClause(annoBilancio, soloValidiPerBilancio, jpql, params);
		
		appendCodiciMacroTipoDaEscludereClause(codiciMacroTipoDaEscludere, jpql, params);
		appendCodiciSottoTipoDaEscludereClause(codiciSottoTipoDaEscludere, jpql, params);
		appendCodiciPropostaDaEscludereClause(propostaDefaultComponenteImportiCapitoloDaEscludere, jpql, params);
		
		jpql.append(" ORDER BY dbedct.elemDetCompTipoId ");
	}
	
	private void appendCodiciPropostaDaEscludereClause(List<String> propostaDefaultComponenteImportiCapitoloDaEscludere, StringBuilder jpql, Map<String, Object> params) {
		if(propostaDefaultComponenteImportiCapitoloDaEscludere == null || propostaDefaultComponenteImportiCapitoloDaEscludere.isEmpty()){
			return;
		}
		jpql.append(" AND dbedct.siacDBilElemDetCompTipoDef.elemDetCompTipoDefCode NOT IN (:elemDetCompTipoDefCodes) ");
		params.put("elemDetCompTipoDefCodes", propostaDefaultComponenteImportiCapitoloDaEscludere);
	}

	private void appendCodiciMacroTipoDaEscludereClause(List<String> codiciMacroTipoDaEscludere, StringBuilder jpql, Map<String, Object> params) {

		if (codiciMacroTipoDaEscludere != null && !codiciMacroTipoDaEscludere.isEmpty()) {
			jpql.append(" AND dbedct.siacDBilElemDetCompMacroTipo.elemDetCompMacroTipoCode NOT IN :codiciMacroTipoDaEscludere ");
			params.put("codiciMacroTipoDaEscludere", codiciMacroTipoDaEscludere);
		}
	}

	private void appendCodiciSottoTipoDaEscludereClause(List<String> codiciSottoTipoDaEscludere, StringBuilder jpql, Map<String, Object> params) {

		if (codiciSottoTipoDaEscludere != null && !codiciSottoTipoDaEscludere.isEmpty()) {
			jpql.append(" AND ( sottoTipo.elemDetCompSottoTipoCode IS NULL OR sottoTipo.elemDetCompSottoTipoCode NOT IN (:codiciSottoTipoDaEscludere)) ");
			params.put("codiciSottoTipoDaEscludere", codiciSottoTipoDaEscludere);
		}
	}

	private void appendAnnoClause(Integer anno, StringBuilder jpql, Map<String, Object> params) {
		if (anno == null) {
			return;
		}
		
		jpql.append(" AND dbedct.siacTPeriodo.anno=:anno ") 
			.append(" AND p.siacTEnteProprietario.enteProprietarioId=dbedct.siacTEnteProprietario ")
			.append(" AND p.siacDPeriodoTipo.periodoTipoCode = 'SY' ")
			.append(" AND p.dataInizioValidita < CURRENT_TIMESTAMP ")
			.append(" AND (p.dataFineValidita IS NULL OR p.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" AND p.dataCancellazione IS NULL ");
		params.put("anno", anno.toString());
	}
	
	private void appendAnnoBilancioClause(Integer annoBilancio, Boolean soloValidiPerBilancio, StringBuilder jpql, Map<String, Object> params) {
		if(annoBilancio == null || !Boolean.TRUE.equals(soloValidiPerBilancio)) {
			return;
		}
		jpql.append(" AND dbedct.dataInizioValidita <= :inizioAnnoBilancio");
		jpql.append(" AND (dbedct.dataFineValidita IS NULL OR dbedct.dataFineValidita >= :fineAnnoBilancio)");
		params.put("inizioAnnoBilancio", getStartOfYear(annoBilancio));
		params.put("fineAnnoBilancio", getEndOfYear(annoBilancio));
	}

	private void appendCodiceTipoClause(String item, String code, StringBuilder jpql, Map<String, Object> params) {
		if (StringUtils.isBlank(code)) {
			return;
		}
		
		String refEntityField = String.format("dbedct.siacDBilElemDetComp%s", item);
		String field = String.format("elemDetComp%sCode", item);

		jpql.append(" AND ").append(refEntityField).append(".").append(field).append(" = :").append(field).append(" ");
		params.put(field, code);
	}

	private Date getEndOfYear(Integer year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year.intValue());
		setMaximum(cal, Calendar.MONTH);
		setMaximum(cal, Calendar.DAY_OF_MONTH);
		setMaximum(cal, Calendar.HOUR_OF_DAY);
		setMaximum(cal, Calendar.MINUTE);
		setMaximum(cal, Calendar.SECOND);
		setMaximum(cal, Calendar.MILLISECOND);
		return cal.getTime();
	}
	
	private Date getStartOfYear(Integer year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year.intValue());
		setMinimum(cal, Calendar.MONTH);
		setMinimum(cal, Calendar.DAY_OF_MONTH);
		setMinimum(cal, Calendar.HOUR_OF_DAY);
		setMinimum(cal, Calendar.MINUTE);
		setMinimum(cal, Calendar.SECOND);
		setMinimum(cal, Calendar.MILLISECOND);
		return cal.getTime();
	}
	
	private void setMaximum(Calendar calendar, int calendarField) {
		calendar.set(calendarField, calendar.getMaximum(calendarField));
	}
	
	private void setMinimum(Calendar calendar, int calendarField) {
		calendar.set(calendarField, calendar.getMinimum(calendarField));
	}

}
