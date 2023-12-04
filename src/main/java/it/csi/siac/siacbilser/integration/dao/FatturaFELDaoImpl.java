/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SirfelTBase;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entity.enumeration.SirfelDTipoDocumentoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FatturaFELDaoImpl extends JpaDao<SirfelTFattura, SirfelTFatturaPK> implements FatturaFELDao {
	
	@Override
	public SirfelTFattura findById(SirfelTFatturaPK id) {
		SirfelTFattura sirfelTFattura = super.findById(id);
		return sirfelTFattura;
	}


	@Override
	public Page<SirfelTFattura> ricercaSinteticaFatturaFEL(
			int enteProprietarioId,
			//SIAC-7557
			//SirfelDTipoDocumentoEnum sirfelDTipoDocumentoEnum,
			String codiceTipoDocFEL,
			String codicePrestatore, 
			String numero, 
			String codiceDestinatario,
			Date dataDa, 
			Date dataA, 
			String statoFattura, 
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaFatturaFEL";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		//SIAC-7557
//		componiQueryRicercaSinteticaFatturaFEL( jpql, param, enteProprietarioId, sirfelDTipoDocumentoEnum, codicePrestatore, 
//				 numero, codiceDestinatario, dataDa, dataA, statoFattura);
		
		componiQueryRicercaSinteticaFatturaFEL( jpql, param, enteProprietarioId, codiceTipoDocFEL, codicePrestatore, 
				 numero, codiceDestinatario, dataDa, dataA, statoFattura);
		
		jpql.append(" ORDER BY f.numero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}


	private void componiQueryRicercaSinteticaFatturaFEL(StringBuilder jpql,
			Map<String, Object> param, int enteProprietarioId,
			//SIAC-7557
			//SirfelDTipoDocumentoEnum sirfelDTipoDocumentoEnum,
			String codiceTipoDocFEL,
			String codicePrestatore, String numero, String codiceDestinatario,
			Date dataDa, Date dataA, String statoFattura) {
		
		jpql.append("FROM SirfelTFattura f ");
		jpql.append(" WHERE ");
		jpql.append(" f.id.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		//SIAC-7557 inziio
//		if(sirfelDTipoDocumentoEnum != null && sirfelDTipoDocumentoEnum.getCodice()!=null) {
//			jpql.append(" AND f.sirfelDTipoDocumento.id.codice = :codice ");
//			param.put("codice", sirfelDTipoDocumentoEnum.getCodice());
//		}
		if(!StringUtils.isEmpty(codiceTipoDocFEL)){
			jpql.append(" AND f.sirfelDTipoDocumento.id.codice = :codice ");
			param.put("codice", codiceTipoDocFEL);
		}
		//SIAC-7557 fine
		
		if(!StringUtils.isEmpty(codicePrestatore)){
			jpql.append(" AND f.sirfelTPrestatore.codicePrestatore = :codicePrestatore ");
			param.put("codicePrestatore", codicePrestatore);			
		}
		
		if(!StringUtils.isEmpty(numero)){
			jpql.append(" AND f.numero = :numero ");
			param.put("numero", numero);			
		}
		
		if(!StringUtils.isEmpty(codiceDestinatario)){
			jpql.append(" AND f.codiceDestinatario = :codiceDestinatario ");
			param.put("codiceDestinatario", codiceDestinatario);			
		}
		
		if(!StringUtils.isEmpty(statoFattura)){
			jpql.append(" AND f.statoFattura = :statoFattura ");
			param.put("statoFattura", statoFattura);			
		}
		
		if(dataDa != null){
			jpql.append(" AND  DATE_TRUNC('day', CAST(f.data AS date)) >= DATE_TRUNC('day', CAST(:dataDa AS date)) ");
			param.put("dataDa", dataDa);
		}
		
		if(dataA != null){
			jpql.append(" AND  DATE_TRUNC('day', CAST(f.data AS date)) <= DATE_TRUNC('day', CAST(:dataA AS date)) ");
			param.put("dataA", dataA);
		}
		
	}


	@Override
	public <T extends SirfelTBase<?>> T create(T obj) {
		entityManager.persist(obj);
		return obj;
	}
	
	@Override
	public void clean() {
		entityManager.flush();
		entityManager.clear();
	}
	
}
