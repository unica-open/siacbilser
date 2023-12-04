/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.integration.dao.base.SiacTBaseDaoImpl;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMutuoDaoImpl extends SiacTBaseDaoImpl<SiacTMutuo, Integer> implements SiacTMutuoDao {
	
	@PostConstruct
	public void init() {
		log.debug("init SiacTMutuoDaoImpl", "just testing hashCode: " + this.hashCode());
	}
	
	@Override
	public Page<SiacTMutuo> ricercaSinteticaMutuo(Integer enteProprietarioId, Integer mutuoNumero, String  mutuoTipoTassoCode, Integer attoammId,  Integer attoammAnno,Integer attoammNumero,Integer attoammTipoId,Integer attoammSacId, String mutuoOggetto, Integer soggettoId, String mutuoStatoCode, Integer mutuoPeriodoRimborsoId, Pageable pageable) {
		final String methodName = "ricercaSinteticaMutuo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTMutuo p ");
		jpql.append(" WHERE ");
		jpql.append(" p.dataCancellazione IS NULL "); // TODO verificare con analisi
		
		jpql.append(getEnteClause("p"));
		param.put("enteProprietarioId", enteProprietarioId);
		
		if (mutuoNumero != null) {
			jpql.append(" AND p.mutuoNumero = :mutuoNumero");
			param.put("mutuoNumero", mutuoNumero);
		}
		if (StringUtils.isNotBlank(mutuoTipoTassoCode)) {
			jpql.append(" AND p.siacDMutuoTipoTasso.mutuoTipoTassoCode = :mutuoTipoTassoCode")
			.append(getDateValiditaCancellazioneClauses("p.siacDMutuoTipoTasso"));
			param.put("mutuoTipoTassoCode", mutuoTipoTassoCode);
		}
		if (NumberUtil.isValidAndGreaterThanZero(attoammId)) {
			jpql.append(" AND p.siacTAttoAmm.attoammId = :attoammId");
			param.put("attoammId", attoammId);
		}
		if (NumberUtil.isValidAndGreaterThanZero(attoammAnno)) {
			jpql.append(" AND p.siacTAttoAmm.attoammAnno = CAST(:attoammAnno AS string) ");
			param.put("attoammAnno", attoammAnno);
		}
		if (NumberUtil.isValidAndGreaterThanZero(attoammNumero)) {
			jpql.append(" AND p.siacTAttoAmm.attoammNumero = :attoammNumero");
			param.put("attoammNumero", attoammNumero);
		}
		if (NumberUtil.isValidAndGreaterThanZero(attoammTipoId)) {
			jpql.append(" AND p.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId");
			param.put("attoammTipoId", attoammTipoId);
		}
		if (NumberUtil.isValidAndGreaterThanZero(attoammSacId)) {
			jpql.append(" AND EXISTS (select 1 from  p.siacTAttoAmm.siacRAttoAmmClasses raac where raac.siacTClass.classifId = :attoammSacId ")
			.append(getDateValiditaCancellazioneClauses("raac"))
			.append(")");
			param.put("attoammSacId", attoammSacId);
		}
		if(StringUtils.isNotBlank(mutuoOggetto)) {
			jpql.append(" AND " + Utility.toJpqlSearchLike("p.mutuoOggetto", "CONCAT('%', :mutuoOggetto, '%')"));
			param.put("mutuoOggetto", mutuoOggetto);	
		}
		if (NumberUtil.isValidAndGreaterThanZero(soggettoId)) {
			jpql.append(" AND p.siacTSoggetto.soggettoId = :soggettoId");
			param.put("soggettoId", soggettoId);
		}
		if (StringUtils.isNotBlank(mutuoStatoCode)) {
			jpql.append(" AND p.siacDMutuoStato.mutuoStatoCode = :mutuoStatoCode")
			.append(getDateValiditaCancellazioneClauses("p.siacDMutuoStato"));
			param.put("mutuoStatoCode", mutuoStatoCode);
		}
		if (NumberUtil.isValidAndGreaterThanZero(mutuoPeriodoRimborsoId)) {
			jpql.append(" AND p.siacDMutuoPeriodoRimborso.mutuoPeriodoRimborsoId = :mutuoPeriodoRimborsoId");
			param.put("mutuoPeriodoRimborsoId", mutuoPeriodoRimborsoId);
		}
		
		jpql.append(" ORDER BY p.mutuoNumero ");
		
		String jpqlString = jpql.toString();
		log.debug(methodName, jpqlString);
		
		return getPagedList(jpqlString, param, pageable);
	}
	
}
