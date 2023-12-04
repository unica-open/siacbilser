/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

// TODO: Auto-generated Javadoc
/**
 * The Interface CodificaDao.
 */
public interface CodificaDao extends Dao<SiacTClass, Integer> {

	/**
	 * Find codifiche.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	public List<SiacTClass> findCodifiche(int anno, int enteProprietarioId);

	
	/**
	 * Find codifiche by id padre.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param idPadreCodifica the id padre codifica
	 * @return the list
	 */
	public List<SiacTClass> findCodificheByIdPadre(int anno,
			int enteProprietarioId, int idPadreCodifica);
	
	/**
	 * Find codifiche by id figlio.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param idFiglioCodifica the id figlio codifica
	 * @return the list
	 */
	public List<SiacTClass> findCodificheByIdFiglio(int anno,
			int enteProprietarioId, int idFiglioCodifica);

	/**
	 * Find codifica famiglia tree dto.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceFamigliaTree the codice famiglia tree
	 * @return the list
	 */
	public List<SiacTClass> findCodificaFamigliaTreeDto(int anno, int enteProprietarioId, String codiceFamigliaTree);
	
}
