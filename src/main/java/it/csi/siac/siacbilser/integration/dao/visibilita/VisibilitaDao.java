/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.visibilita;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTVisibilita;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface VisiblitaDao.
 * @author interlogic
 * @version 1.0.0 - 10/05/2021
 */
public interface VisibilitaDao extends Dao<SiacTVisibilita, Integer> {
	
	List<SiacTVisibilita> search(Integer azioneId, String visFunzionalita, Integer enteProprietarioId);
	
}
