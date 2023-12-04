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

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazTipoCassa;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoOperazioneDiCassaDaoImpl extends JpaDao<SiacDCassaEconOperazTipo, Integer> implements TipoOperazioneDiCassaDao {
	
	public SiacDCassaEconOperazTipo create(SiacDCassaEconOperazTipo c){
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		//inserimento elementi nuovi (non faccio alcunche' sulla relazione con le operazioni di cassa)
		
		if(c.getSiacRCassaEconOperazTipoCassas()!=null){
			for(SiacRCassaEconOperazTipoCassa r : c.getSiacRCassaEconOperazTipoCassas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		c.setUid(null);
		super.save(c);
		return c;
	}

	public SiacDCassaEconOperazTipo update(SiacDCassaEconOperazTipo c){
		
		SiacDCassaEconOperazTipo cAttuale = this.findById(c.getUid());
		
		Date now = new Date();
		c.setDataModificaAggiornamento(now);
		
		
		//cancellazione elementi collegati (non faccio alcunche' sulla relazione con le operazioni di cassa)
		if(cAttuale.getSiacRCassaEconOperazTipoCassas()!=null){
			for(SiacRCassaEconOperazTipoCassa r : cAttuale.getSiacRCassaEconOperazTipoCassas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		//inserimento elementi nuovi (non faccio alcunche' sulla relazione con le operazioni di cassa)
		if(c.getSiacRCassaEconOperazTipoCassas()!=null){
			for(SiacRCassaEconOperazTipoCassa r : c.getSiacRCassaEconOperazTipoCassas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		
		super.update(c);
		return c;
	}



	@Override
	public Page<SiacDCassaEconOperazTipo> ricercaSinteticaTipoOperazioneCassa(
			Integer enteProprietarioId,
			String cassaeconopTipoCode,
			String cassaeconopTipoDesc,
			Integer cassaeconId,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaTipoOperazioneCassa";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaTipoOperazione( jpql, param, enteProprietarioId, cassaeconopTipoCode, cassaeconopTipoDesc, cassaeconId);
		
		jpql.append(" ORDER BY cot.cassaeconopTipoCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}



	private void componiQueryRicercaSinteticaTipoOperazione(StringBuilder jpql,
			Map<String, Object> param,
			Integer enteProprietarioId,
			String cassaeconopTipoCode,
			String cassaeconopTipoDesc,
			Integer cassaeconId) {
		
		jpql.append("FROM SiacDCassaEconOperazTipo cot ");
		jpql.append(" WHERE ");
		jpql.append(" cot.dataCancellazione IS NULL ");
		jpql.append(" AND cot.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(cassaeconopTipoCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("cot.cassaeconopTipoCode", "CONCAT('%', :cassaeconopTipoCode, '%')") + " ");
			param.put("cassaeconopTipoCode", cassaeconopTipoCode);
		}
		
		if(!StringUtils.isEmpty(cassaeconopTipoDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("cot.cassaeconopTipoDesc", "CONCAT('%', :cassaeconopTipoDesc, '%')") + " ");
			param.put("cassaeconopTipoDesc", cassaeconopTipoDesc);
		}
		
		if(cassaeconId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM cot.siacRCassaEconOperazTipoCassas rceotc ");
			jpql.append("     WHERE rceotc.dataCancellazione IS NULL ");
			jpql.append("     AND rceotc.siacTCassaEcon.cassaeconId = :cassaeconId ");
			jpql.append(" ) ");
			param.put("cassaeconId", cassaeconId);
		}
		
//		if(!StringUtils.isEmpty(cassaeconopTipoCode)){
//			jpql.append(" AND cot.cassaeconopTipoCode = :cassaeconopTipoCode ");
//			param.put("cassaeconopTipoCode", cassaeconopTipoCode);
//		}
		
	}
	
	

}
