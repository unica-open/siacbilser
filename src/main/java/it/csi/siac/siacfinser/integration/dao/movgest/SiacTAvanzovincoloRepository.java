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

import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;

public interface SiacTAvanzovincoloRepository extends JpaRepository<SiacTAvanzovincoloFin, Integer> {
	
	String condizione =  " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	String condizioneValidoInRange = " ( "
			+ " (   (dataInizioValidita <= :inizioRange AND dataFineValidita IS NULL) "
			+ " OR (dataInizioValidita <= :inizioRange AND dataFineValidita > :inizioRange)"
			+ " OR (dataInizioValidita >= :inizioRange AND dataInizioValidita < :fineRange)  )"
			+ "  AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTAvanzovincoloFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +condizione)
	public List<SiacTAvanzovincoloFin> findAllValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTAvanzovincoloFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +condizioneValidoInRange)
	public List<SiacTAvanzovincoloFin> findAllValidiInRangeByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("inizioRange") Timestamp  inizioRange, @Param("fineRange") Timestamp  fineRange);
	
	@Query("FROM SiacTAvanzovincoloFin WHERE avavId = :idVincolo AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +condizione)
	public SiacTAvanzovincoloFin findValidoByEnte(@Param("idVincolo") Integer idVincolo, @Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTAvanzovincoloFin WHERE avavId = :idVincolo AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +condizioneValidoInRange)
	public SiacTAvanzovincoloFin findValidoInRangeByEnte(@Param("idVincolo") Integer idVincolo, @Param("enteProprietarioId") Integer enteProprietarioId,@Param("inizioRange") Timestamp  inizioRange, @Param("fineRange") Timestamp  fineRange);
	
}