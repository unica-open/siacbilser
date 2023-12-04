/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface AmmortamentoAnnuoCespiteDao extends Dao<SiacTCespitiAmmortamento, Integer> {
	
	SiacTCespitiAmmortamento create(SiacTCespitiAmmortamento r);
	
	SiacTCespitiAmmortamento update(SiacTCespitiAmmortamento r);
	
	SiacTCespitiAmmortamento delete(int uidCespiti, String loginOperazione);
	
}
