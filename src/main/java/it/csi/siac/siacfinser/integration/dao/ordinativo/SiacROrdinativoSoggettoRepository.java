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

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;

public interface SiacROrdinativoSoggettoRepository extends JpaRepository<SiacROrdinativoSoggettoFin, Integer> {
	
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("FROM SiacROrdinativoSoggettoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacROrdinativoSoggettoFin> findListaSiacROrdinativoSoggetto(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                              @Param("dataInput") Timestamp  dataInput);	
	
	@Query("FROM SiacROrdinativoSoggettoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND siacTSoggetto.soggettoId = :idSoggetto AND " + condizione)
	public List<SiacROrdinativoSoggettoFin> findValidoByIdSoggetto(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("dataInput") Timestamp  dataInput,
			                                                    @Param("idSoggetto") Integer idSoggetto);
	
	@Query("FROM SiacROrdinativoSoggettoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND siacTSoggetto.soggettoId = :idSoggetto AND siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacROrdinativoSoggettoFin> findValidoByIdOrdinativoAndIdSoggetto(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                   @Param("dataInput") Timestamp  dataInput,
			                                                                   @Param("idSoggetto") Integer idSoggetto,
			                                                                   @Param("idOrdinativo") Integer idOrdinativo);
	
	@Query("FROM SiacROrdinativoSoggettoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacROrdinativoSoggettoFin> findValidoByIdOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                      @Param("dataInput") Timestamp  dataInput,
			                                                      @Param("idOrdinativo") Integer idOrdinativo);
}