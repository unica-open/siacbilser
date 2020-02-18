/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DettaglioVariazioneComponenteImportiCapitoloDao.
 */
public interface DettaglioVariazioneComponenteImportiCapitoloDao extends Dao<SiacTBilElemDetVarComp, Integer> {
	
	SiacTBilElemDetVarComp deleteLogically(SiacTBilElemDetVarComp d);
	SiacTBilElemDetVarComp deleteLogically(Integer id);
	
}
