/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccorser.integration.dao.CodificaDao;

// TODO: Auto-generated Javadoc
/**
 * The Interface CodificaBilDao.
 */
public interface CodificaBilDao extends CodificaDao {
	
	/**
	 * Ricerca i classificatori semplici per tipo elemento di bilancio in input .
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceTipoElemBilancio the codice tipo elem bilancio
	 * @return the list
	 */
	List<SiacTClass> findCodificheByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio);
	
	List<SiacTClass> findCodificheByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio, String classifTipoCode);

	/**
	 * Ricerca i classificatori con livello per tipo elemento di bilancio in input .
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceTipoElemBilancio the codice tipo elem bilancio
	 * @return the list
	 */
	List<SiacTClass> findCodificheConLivelloByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio);
	
	
	/**
	 * Ricerca il piano dei conti per Id Codifica Padre 
	 * es: per la spesa Id codifica padre è il Macroaggregato e rappresenta il 2° livello 
	 * 	   per le entrate Id codifica padre è la Categoria e rapprendenta il 3° livello  .
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param famigliaTreeCodice the famiglia tree codice
	 * @param idCodificaPadre the id codifica padre
	 * @return the list
	 */
	List<SiacTClass> findTreeByCodiceFamiglia(int anno,
			int enteProprietarioId, String famigliaTreeCodice, Integer idCodificaPadre, boolean senzaPadre);
	
	/**
	 * Ricerca i classificatori generici per tipo elemento di bilancio in input.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceTipoElemBilancio the codice tipo elem bilancio
	 * @param classifTipoCode il codice del tipo del classificatore
	 * @return the list
	 */
	
	List<SiacTClass> findCodificheGenericiTipoElemBilancio(int anno, int enteProprietarioId, String codiceTipoElemBilancio, String classifTipoCode);
	
}
