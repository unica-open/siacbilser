/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDNoteTesoriereFin;

public interface SiacDNoteTesoriereFinRepository extends JpaRepository<SiacDNoteTesoriereFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDNoteTesoriereFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND notetesCode = :code AND " + condizione)
	public SiacDNoteTesoriereFin findDNoteTesoriereValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                  @Param("code") String code,
			                                                  @Param("dataInput") Timestamp  dataInput);
	
	static final String FIND_NOTE_TESORIERE_BY_ENTE ="FROM SiacDNoteTesoriereFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND "+condizione;

	@Query(FIND_NOTE_TESORIERE_BY_ENTE)
	public List<SiacDNoteTesoriereFin> findNoteTesoriereByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
}