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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoDocumentoFELDaoImpl extends ExtendedJpaDao<SirfelDTipoDocumento, Integer> implements TipoDocumentoFELDao {
	
	@Override
	public SirfelDTipoDocumento create(SirfelDTipoDocumento entity){
		
		Date now = new Date();
		super.save(entity);
		return entity;
	}

	@Override
	public SirfelDTipoDocumento update(SirfelDTipoDocumento entity){
		// SiacDBilElemDetCompTipo dAttuale = this.findById(d.getUid());
		
		Date now = new Date();
//		entity.setDataModifica(now);
//
//		Date dataInizioValidita = entity.getDataInizioValidita();
		
		super.update(entity);
		
		//forceUpdateDataInizioValidita(entity.getUid(), dataInizioValidita);
		
		return entity;
	}
	
	@Override
	public List<SirfelDTipoDocumento> ricercaTipoDocumentoFEL(
			Integer enteProprietarioId, 
			String codice,
			String descrizione) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		buildJpqlRicercaTipoDocumentoFEL(enteProprietarioId, codice, descrizione,null,null,   jpql, params);

		return getList(jpql.toString(), params);
	}	
	
	@Override
	public Page<SirfelDTipoDocumento> ricercaPaginataTipoDocumentoFEL(
			Integer enteProprietarioId, 
			String codice,
			String descrizione, 
			TipoDocumento tipoDocContabiliaEntrata,
			TipoDocumento tipoDocContabiliaSpesa,
			Pageable pageable) {
		final String methodName = "ricercaPaginataTipoDocumentoFEL";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		buildJpqlRicercaTipoDocumentoFEL(enteProprietarioId,codice, descrizione,tipoDocContabiliaEntrata  ,tipoDocContabiliaSpesa, jpql, params);
		log.debug(methodName, "JPQL TO EXECUTE: "+ jpql.toString());
		
		return getPagedList(jpql.toString(), params, pageable);
	}
	
	private void buildJpqlRicercaTipoDocumentoFEL(
			Integer enteProprietarioId, 
			String codice,
			String descrizione,  
			TipoDocumento tipoDocContabiliaEntrata,
			TipoDocumento tipoDocContabiliaSpesa,
			StringBuilder jpql, 
			Map<String, Object> params) {
		
		params.put("enteProprietarioId", enteProprietarioId);
		
		jpql.append(" SELECT dbedct ")
			.append(" FROM SirfelDTipoDocumento dbedct");
		
//		if(codiciSottoTipoDaEscludere != null && !codiciSottoTipoDaEscludere.isEmpty()) {
//			jpql.append(" LEFT JOIN dbedct.siacDBilElemDetCompSottoTipo sottoTipo ");
//		}
		
		jpql.append(" WHERE dbedct.id.enteProprietarioId = :enteProprietarioId ");
		params.put("enteProprietarioId", enteProprietarioId);
		
		if (StringUtils.isNotBlank(codice)) {
			jpql.append(" AND dbedct.id.codice LIKE CONCAT('%', :codice, '%') ");
			params.put("codice", codice);
		}
		
		if (StringUtils.isNotBlank(descrizione)) {
			jpql.append(" AND dbedct.descrizione LIKE CONCAT('%', :descrizione, '%') ");
			params.put("descrizione", descrizione);
		}
		 
		if (tipoDocContabiliaEntrata != null && tipoDocContabiliaEntrata.getUid() > 0) {
			jpql.append("     AND dbedct.siacDDocTipoE.docTipoId = :docTipoIdEntrata");
			params.put("docTipoIdEntrata", tipoDocContabiliaEntrata.getUid());
		}
		
		if (tipoDocContabiliaSpesa != null && tipoDocContabiliaSpesa.getUid() > 0) {
			jpql.append("     AND dbedct.siacDDocTipoS.docTipoId = :docTipoIdSpesa");
			params.put("docTipoIdSpesa", tipoDocContabiliaSpesa.getUid());
		}
		

		 
		
		jpql.append(" ORDER BY dbedct.id.codice ");
	}
	   
	  

}
