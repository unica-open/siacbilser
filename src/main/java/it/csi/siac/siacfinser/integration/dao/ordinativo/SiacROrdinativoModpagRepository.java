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

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;

public interface SiacROrdinativoModpagRepository extends JpaRepository<SiacROrdinativoModpagFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("FROM SiacROrdinativoModpagFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacROrdinativoModpagFin> findListaSiacROrdinativoModpag(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoModpagFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacROrdinativoModpagFin> findValidoByIdOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("dataInput") Timestamp  dataInput,
			                                                    @Param("idOrdinativo") Integer idOrdinativo);
		
	@Query("FROM SiacROrdinativoModpagFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTModpag.modpagId = :modpagId AND " + condizione)
	public List<SiacROrdinativoModpagFin> findValidoByIdMdp(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("dataInput") Timestamp  dataInput,
			                                                    @Param("modpagId") Integer modpagId);
		
}