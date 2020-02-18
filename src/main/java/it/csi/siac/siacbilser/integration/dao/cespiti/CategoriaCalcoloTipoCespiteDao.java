/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;



import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoriaCalcoloTipo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CategoriaCalcoloTipoCespiteDao.
 *
 * @author Antonino
 */
public interface CategoriaCalcoloTipoCespiteDao extends Dao<SiacDCespitiCategoriaCalcoloTipo, Integer> {
	
	SiacDCespitiCategoriaCalcoloTipo create(SiacDCespitiCategoria r);

	SiacDCespitiCategoriaCalcoloTipo update(SiacDCespitiCategoria r);

}
