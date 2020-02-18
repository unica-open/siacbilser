/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDFileTipo;

/**
 * Repository per l'entity SiacDFileTipo.
 *
 */
public interface SiacDFileTipoRepository extends JpaRepository<SiacDFileTipo, Integer> {
	
	@Query(" FROM SiacDFileTipo dft "
			+ " WHERE dft.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dft.fileTipoCode = :fileTipoCode"
			+ " AND dft.dataCancellazione IS NULL ")
	SiacDFileTipo findByFileTipoCode(@Param("fileTipoCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
