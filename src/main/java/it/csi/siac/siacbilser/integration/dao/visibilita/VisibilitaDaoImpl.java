/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.visibilita;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTVisibilita;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class VisibilitaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VisibilitaDaoImpl extends JpaDao<SiacTVisibilita, Integer> implements VisibilitaDao {
	
	@Override
	public List<SiacTVisibilita> search(Integer azioneId, String visFunzionalita, Integer enteProprietarioId) {
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTVisibilita tv ");
		jpql.append(" WHERE tv.dataCancellazione IS NULL ");
		jpql.append(" AND tv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(azioneId != null && azioneId.intValue() != 0) {
			jpql.append(" AND tv.siacTAzione.azioneId = :azioneId ");
			param.put("azioneId", azioneId);
		}
		if(StringUtils.isNotBlank(visFunzionalita)) {
			jpql.append(" AND tv.visFunzionalita = :visFunzionalita ");
			param.put("visFunzionalita", visFunzionalita);
		}
		TypedQuery<SiacTVisibilita> query = createTypedQuery(jpql.toString(), param);
		return query.getResultList();
	}
	
}
