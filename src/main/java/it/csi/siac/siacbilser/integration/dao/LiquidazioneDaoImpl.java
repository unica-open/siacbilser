/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Interface LiquidazioneDao.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LiquidazioneDaoImpl extends JpaDao<SiacTLiquidazione, Integer>  implements LiquidazioneDao {
	
	private static final String CODICE_TIPO_DOCUMENTO_ALLEGATO = "ALG";

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTLiquidazione> findByAttoalId(Integer attoalId, Boolean docTipoCodeAllegato) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTLiquidazione l ")
			.append(" WHERE l.dataCancellazione IS NULL ")
			.append(" AND EXISTS ( ")
			.append("     FROM l.siacRSubdocLiquidaziones srsl, SiacTSubdoc s ")
			.append("     WHERE srsl.siacTSubdoc = s ")
			.append("     AND srsl.dataCancellazione IS NULL ");
		if(Boolean.TRUE.equals(docTipoCodeAllegato)) {
			jpql.append("     AND s.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode ");
		} else {
			jpql.append("     AND s.siacTDoc.siacDDocTipo.docTipoCode <> :docTipoCode");
		}
		jpql.append("     AND EXISTS ( ")
			.append("         FROM s.siacRElencoDocSubdocs sreds, SiacTElencoDoc e ")
			.append("         WHERE sreds.siacTElencoDoc = e ")
			.append("         AND sreds.dataCancellazione IS NULL ")
			.append("         AND EXISTS ( ")
			.append("             FROM e.siacRAttoAllegatoElencoDocs sraaed ")
			.append("             WHERE sraaed.siacTAttoAllegato.attoalId = :attoalId ")
			.append("             AND sraaed.dataCancellazione IS NULL ")
			.append("         ) ")
			.append("     ) ")
			.append(" ) ");
		
		param.put("docTipoCode", CODICE_TIPO_DOCUMENTO_ALLEGATO);
		param.put("attoalId", attoalId);
		
		Query query = createQuery(jpql.toString(), param);
			
		return (List<SiacTLiquidazione>)query.getResultList();
	}
	
	@Override
	public List<SiacRSubdocLiquidazione> findSiacRSubdocLiquidazioneByLiquidazioneId(Integer liqId, Boolean docTipoCodeAllegato) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacRSubdocLiquidazione r ")
			.append(" WHERE r.dataCancellazione IS NULL ")
			.append(" AND r.siacTLiquidazione.liqId = :liqId ");
		if(Boolean.TRUE.equals(docTipoCodeAllegato)) {
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode ");
		} else {
			jpql.append(" AND r.siacTSubdoc.siacTDoc.siacDDocTipo.docTipoCode <> :docTipoCode ");
		}
		
		param.put("docTipoCode", CODICE_TIPO_DOCUMENTO_ALLEGATO);
		param.put("liqId", liqId);
		Query query = createQuery(jpql.toString(), param);
		
		return (List<SiacRSubdocLiquidazione>)query.getResultList();
	}
	
}
