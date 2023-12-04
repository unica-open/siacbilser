/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemIvaAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTIvaAttivitaRepository.
 */
public interface SiacTIvaAttivitaRepository extends JpaRepository<SiacTIvaAttivita, Integer> {
	
	/**
	 * Find by ente proprietario and by codice and by descrizione.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivaattCode the ivaatt code
	 * @param ivaattDesc the ivaatt desc
	 * @return the list
	 */
	@Query( " SELECT a " +
			" FROM SiacTIvaAttivita a " +
			" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (a.dataFineValidita IS NULL OR a.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND (:ivaattCode = '' OR :ivaattCode IS NULL OR a.ivaattCode = :ivaattCode) " +
			" AND (:ivaattDesc = '' OR :ivaattDesc IS NULL OR a.ivaattDesc LIKE CONCAT('%', :ivaattDesc, '%')) " +
			" AND a.dataCancellazione IS NULL " +
			" ORDER BY a.ivaattCode ")
	List<SiacTIvaAttivita> findByEnteProprietarioAndByCodiceAndByDescrizione(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivaattCode") String ivaattCode, @Param("ivaattDesc") String ivaattDesc);

	
	/**
	 * Find by gruppo attivita iva.
	 *
	 * @param ivagruId the ivagru id
	 * @return the list
	 */
	@Query( " SELECT a " +
			" FROM SiacTIvaAttivita a " +
			" WHERE EXISTS ( " +
			" 	SELECT rga " +
			" 	FROM a.siacRIvaGruppoAttivitas rga " +
			" 	WHERE rga.siacTIvaGruppo.ivagruId = :ivagruId " +
			" 	AND (rga.dataFineValidita IS NULL OR rga.dataFineValidita > CURRENT_TIMESTAMP) " +
			" 	AND rga.dataCancellazione IS NULL  " +
			" ) " +
			" AND (a.dataFineValidita IS NULL OR a.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND a.dataCancellazione IS NULL " +
			" ORDER BY a.ivaattCode ")
	List<SiacTIvaAttivita> findByGruppoAttivitaIva(@Param("ivagruId") Integer ivagruId);
	
	
	/**
	 * Find by capitolo.
	 *
	 * @param elemId the elem id
	 * @return the list
	 */
	@Query( " SELECT a " +
			" FROM SiacTIvaAttivita a " +
			" WHERE EXISTS ( " +
			" 	SELECT rba " +
			" 	FROM a.siacRBilElemIvaAttivitas rba " +
			" 	WHERE rba.siacTBilElem.elemId = :elemId " +
			" 	AND rba.dataCancellazione IS NULL " +
			" ) " +
			" AND a.dataCancellazione IS NULL " +
			" ORDER BY a.ivaattCode ")
	List<SiacTIvaAttivita> findByCapitolo(@Param("elemId") Integer elemId);


	/**
	 * Find relazione attivita iva capitolo.
	 *
	 * @param elemId the elem id
	 * @param ivaattId the ivaatt id
	 * @return the list
	 */
	@Query( " SELECT rac " +
			" FROM SiacRBilElemIvaAttivita rac " +
			" WHERE rac.siacTBilElem.elemId = :elemId " +
			" AND rac.siacTIvaAttivita.ivaattId = :ivaattId " +
			" AND rac.dataCancellazione IS NULL " )
	List<SiacRBilElemIvaAttivita> findRelazioneAttivitaIvaCapitolo(@Param("elemId") Integer elemId , @Param("ivaattId") Integer ivaattId );
	
	
}
