/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDModpagStatoFin;

public interface SiacDModpagStatoRepository extends JpaRepository<SiacDModpagStatoFin, Integer> {

	public String entity = "SiacDModpagStatoFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacDModpagStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND modpagStatoCode = :code ")
	public List<SiacDModpagStatoFin> findModPagStatoDValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);
}
