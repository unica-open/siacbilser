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

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;

public interface SiacROrdinativoProvCassaRepository extends JpaRepository<SiacROrdinativoProvCassaFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("FROM SiacROrdinativoProvCassaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacROrdinativoProvCassaFin> findListaSiacROrdinativoProvCassa(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoProvCassaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTOrdinativo.ordId = :ordinativoId AND " + condizione)
	public List<SiacROrdinativoProvCassaFin> findROrdinativoProvCassaByIdOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                     @Param("ordinativoId") Integer ordinativoId,
                                                                                 @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoProvCassaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTProvCassa.provcId = :provvisorioId AND " + condizione)
	public List<SiacROrdinativoProvCassaFin> findROrdinativoProvCassaByIdProvvisorio(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                     @Param("provvisorioId") Integer provvisorioId,
                                                                                 @Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacROrdinativoProvCassaFin WHERE ordProvcId = :ordProvcId AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public SiacROrdinativoProvCassaFin findSiacROrdinativoProvCassaValidoById(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                               @Param("ordProvcId") Integer ordProvcId,
			                                                               @Param("dataInput") Timestamp  dataInput);
}