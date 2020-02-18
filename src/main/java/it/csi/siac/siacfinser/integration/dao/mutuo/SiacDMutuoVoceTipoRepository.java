/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDMutuoVoceTipoFin;

public interface SiacDMutuoVoceTipoRepository extends JpaRepository<SiacDMutuoVoceTipoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDMutuoVoceTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mutVoceTipoCode = :codeDMutuo AND " + condizione)
	public SiacDMutuoVoceTipoFin findDMutuoVoceTipoValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                        @Param("codeDMutuo") String codeDMutuo,
			                                                        @Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDMutuoVoceTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND  " + condizione)
	public List<SiacDMutuoVoceTipoFin> findDMutuoVoceTipoValidoByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,			                                               
			                                                       @Param("dataInput") Timestamp  dataInput);

}