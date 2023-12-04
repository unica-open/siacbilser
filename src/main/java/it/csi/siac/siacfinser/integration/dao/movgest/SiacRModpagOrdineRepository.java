/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;

public interface SiacRModpagOrdineRepository extends JpaRepository<SiacRModpagOrdineFin, Integer> {

	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("SELECT max(ordine) FROM SiacRModpagOrdineFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTSoggetto.soggettoId = :soggId "+
	       " AND "+condizione)
	public Integer findMaxOrdineModPag(@Param("enteProprietarioId") Integer enteProprietarioId,
			                           @Param("soggId") Integer soggettoId,
			                           @Param("dataInput") Timestamp  dataInput);
	
	
	@Query("from SiacRModpagOrdineFin where siacTEnteProprietario.enteProprietarioId = :ente AND "+ 
			" ordine = :ordine AND "+
			" siacTSoggetto.soggettoId = :idSoggetto")
	public List<SiacRModpagOrdineFin> findBySoggettoECodiceMdp(@Param("ente") Integer ente,
														    @Param("ordine") Integer ordine ,
														    @Param("idSoggetto") Integer idSoggetto);
}
