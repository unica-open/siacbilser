/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dao.vincolo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VincoloCapitoliFinDaoImpl extends JpaDao<SiacTVincoloFin, Integer> implements VincoloCapitoliFinDao {

	/**
	 * SIAC-7349 Inizio SR180 FL 02/04/2020 Recupera i vincoli dato un capitolo
	 */
	@Override
	public List<SiacTVincoloFin> ricercaSinteticaVincoloCapitoliByCapitolo(Integer enteProprietarioId,
			Integer uidCapitolo) {

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		jpql.append("FROM SiacTVincoloFin v ");
		jpql.append("WHERE v.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("AND v.dataCancellazione IS NULL ");

		param.put("enteProprietarioId", enteProprietarioId);

		jpql.append(
				" AND EXISTS( FROM v.siacRVincoloBilElems c WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.dataCancellazione IS NULL ");

		if (uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND c.siacTBilElem.elemId = :uidCapitolo ");
			param.put("uidCapitolo", uidCapitolo);
			jpql.append("  ) ");
			// return; // se c'è l'uid del capitolo gli altri parametri non sono più
			// necessari.
		}

		Query query = createQuery(jpql.toString(), param);

		@SuppressWarnings("unchecked")
		List<SiacTVincoloFin> resultList = (List<SiacTVincoloFin>) query.getResultList();

		return resultList;
	}

	/**
	 * SIAC-7349 Fine SR180 FL 02/04/2020
	 * 
	 */
	
}
