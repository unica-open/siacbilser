/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDCausaleRepository.
 */
public interface SiacDCausaleRepository extends JpaRepository<SiacDCausale, Integer> {
	
	
	/**
	 * Find causale by ente proprietario id and onere id.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param onereId the onere id
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDCausale c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND EXISTS ( " +
			" 	SELECT r " + 
			"	FROM c.siacROnereCausales r " +
			"	WHERE r.siacDOnere.onereId = :onereId ) " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.causCode ")
	List<SiacDCausale> findCausaleByEnteProprietarioIdAndOnereId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("onereId") Integer onereId);
	
	
	/**
	 * Find causale valida by codice.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param causCode the caus code
	 * @param causFamTipoCode the caus fam tipo code
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDCausale c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND UPPER(c.causCode) LIKE UPPER ( :causCode)" +
			" AND EXISTS ( FROM  c.siacRCausaleTipos ct "+
			" 	 	WHERE ct.siacDCausaleTipo.siacDCausaleFamTipo.causFamTipoCode = :causFamTipoCode "+
			" 	  ) "+
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.causCode ")
	List<SiacDCausale> findCausaleValidaByCodice(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("causCode") String causCode, @Param("causFamTipoCode") String causFamTipoCode);
	
	
	/**
	 * Find date storico causale.
	 *
	 * @param causId the caus id
	 * @return the list
	 */
	@Query(  " SELECT cs.dataInizioValidita " +
			 " FROM SiacRCausaleTipo cs " +
			 " WHERE cs.siacDCausale.causId = :causId "+
			 " ORDER BY cs.dataInizioValidita DESC")
	List<Date> findDateStoricoCausale(@Param("causId") Integer causId);

}
