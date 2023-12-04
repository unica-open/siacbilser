/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface SiacDCassaEconOperazTipoDao.
 */
public interface SiacDCassaEconOperazTipoDao extends Dao<SiacDCassaEconOperazTipo, Integer>{
	
	/**
	 * Find by codice E ente E cassa E tipo.
	 *
	 * @param cassaeconopTipoCode the cassaeconop tipo code
	 * @param enteProprietarioId the ente proprietario id
	 * @param anno the anno
	 * @param cassaeconId the cassaecon id
	 * @param cassaeconopTipoEntrataspesa the cassaeconop tipo entrataspesa
	 * @return the list
	 */
	List<SiacDCassaEconOperazTipo> findByCodiceEEnteECassaETipo(String cassaeconopTipoCode,
			Integer enteProprietarioId, Integer anno, Integer cassaeconId, String cassaeconopTipoEntrataspesa);
}
