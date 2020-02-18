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
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconGiustificativo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoGiustificativoDaoImpl extends JpaDao<SiacDGiustificativo, Integer> implements TipoGiustificativoDao {
	
	public SiacDGiustificativo create(SiacDGiustificativo g){
		
		Date now = new Date();
		g.setDataModificaInserimento(now);
		
		if(g.getSiacRCassaEconGiustificativos() != null) {
			for(SiacRCassaEconGiustificativo r : g.getSiacRCassaEconGiustificativos()) {
				r.setDataModificaInserimento(now);
			}
		}
		
		g.setUid(null);		
		super.save(g);
		return g;
	}

	public SiacDGiustificativo update(SiacDGiustificativo g){
		
		Date now = new Date();
		g.setDataModificaAggiornamento(now);
		
		SiacDGiustificativo gAttuale = this.findById(g.getUid());
		
		// cancellazione elementi collegati
		if(gAttuale.getSiacRCassaEconGiustificativos()!=null){
			for(SiacRCassaEconGiustificativo r: gAttuale.getSiacRCassaEconGiustificativos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		// inserimento elementi nuovi
		if(g.getSiacRCassaEconGiustificativos() != null) {
			for(SiacRCassaEconGiustificativo r : g.getSiacRCassaEconGiustificativos()) {
				r.setDataModificaInserimento(now);
			}
		}
		
		super.update(g);
		return g;
	}

	@Override
	public Page<SiacDGiustificativo> ricercaSinteticaTipoGiustificativo(
					Integer enteProprietarioId, 
					String giustCode, 
					String giustDesc,
					String giustTipoCode, 
					Integer cassaeconId,
					Pageable pageable) {
		
		final String methodName = "ricercaSinteticaTipoGiustificativo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaTipoOperazione( jpql, param, enteProprietarioId, giustCode, giustDesc, giustTipoCode, cassaeconId);
		jpql.append(" ORDER BY g.giustCode");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaTipoOperazione(StringBuilder jpql,
			Map<String, Object> param,
			Integer enteProprietarioId,
			String giustCode,
			String giustDesc,
			String giustTipoCode,
			Integer cassaeconId) {
		
		jpql.append("FROM SiacDGiustificativo g ");
		jpql.append(" WHERE ");
		jpql.append(" g.dataCancellazione IS NULL ");
		jpql.append(" AND g.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(giustCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("g.giustCode", "CONCAT('%', :giustCode, '%')") + " ");
			param.put("giustCode", giustCode);			
		}
		
		if(!StringUtils.isEmpty(giustDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("g.giustDesc", "CONCAT('%', :giustDesc, '%')") + " ");
			param.put("giustDesc", giustDesc);			
		}
		
		if(giustTipoCode != null){
			jpql.append(" AND g.siacDGiustificativoTipo.giustTipoCode = :giustTipoCode ");
			param.put("giustTipoCode", giustTipoCode);
		}
		
		if(cassaeconId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM g.siacRCassaEconGiustificativos rceg ");
			jpql.append("     WHERE rceg.dataCancellazione IS NULL ");
			jpql.append("     AND rceg.siacTCassaEcon.cassaeconId = :cassaeconId ");
			jpql.append(" ) ");
			param.put("cassaeconId", cassaeconId);
		}
	}


}
