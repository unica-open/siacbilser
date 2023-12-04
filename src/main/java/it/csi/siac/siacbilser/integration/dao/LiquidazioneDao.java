/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface LiquidazioneDao.
 */
public interface LiquidazioneDao extends Dao<SiacTLiquidazione, Integer> {
	
	/**
	 * Find by attoal id
	 * 
	 * @param attoalId the attoal id
	 * @param docTipoCodeAllegato the doc tipo code allegato
	 * 
	 * @return the siac t liquidaziones
	 */
	List<SiacTLiquidazione> findByAttoalId(Integer attoalId, Boolean docTipoCodeAllegato);
	
	/**
	 * Find siac r subdoc liquidazione by liquidazione id
	 * 
	 * @param liqId the liq id
	 * @param docTipoCodeAllegato the doc tipo code allegato
	 * 
	 * @return the siac r subdoc liquidaziones
	 */
	List<SiacRSubdocLiquidazione> findSiacRSubdocLiquidazioneByLiquidazioneId(Integer liqId, Boolean docTipoCodeAllegato);
	
}
