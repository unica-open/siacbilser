/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DocumentoDao.
 */
public interface ComponenteImportiCapitoloDao extends Dao<SiacTBilElemDetComp, Integer> {
	
	/**
	 * Crea una SiacTBilElemDetComp.
	 *
	 * @param d la SiacTBilElemDetComp da inserire
	 * @return la SiacTBilElemDetComp inserita
	 */
	SiacTBilElemDetComp create(SiacTBilElemDetComp d);

}
