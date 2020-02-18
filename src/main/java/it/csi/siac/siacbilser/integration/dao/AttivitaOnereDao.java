/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacDOnereAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface GruppoAttivitaIvaDao.
 */
public interface AttivitaOnereDao extends Dao<SiacDOnereAttivita, Integer> {
	
	
	/**
	 * Cerca le attivita onere per ente e tipo onere.
	 * 
	 * @param enteProprietarioId the ente proprietario id
	 * @param onereId the onere id
	 * 
	 * @return the siac d onere attivitas
	 */
	List<SiacDOnereAttivita> ricercaAttivitaOnereByEnteProprietarioAndTipoOnere(Integer enteProprietarioId, Integer onereId);
	
}
