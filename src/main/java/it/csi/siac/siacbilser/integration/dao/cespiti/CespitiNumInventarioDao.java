/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiNumInventario;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiNumInventarioDao.
 *
 * @author Antonino
 */
public interface CespitiNumInventarioDao extends Dao<SiacTCespitiNumInventario, Integer> {
	
	SiacTCespitiNumInventario create(SiacDCespitiCategoria r);

	SiacTCespitiNumInventario update(SiacTCespitiNumInventario r);

}
