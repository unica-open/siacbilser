/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;

public interface SiacRLiquidazioneOrdRepository extends JpaRepository<SiacRLiquidazioneOrdFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRLiquidazioneOrdFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRLiquidazioneOrdFin> findListaSiacRLiquidazioneOrd(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                        @Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacRLiquidazioneOrdFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTOrdinativoT.ordTsId = :idOrdinativoTs AND " + condizione)
	public List<SiacRLiquidazioneOrdFin> findValidoByIdOrdinativoTs(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                     @Param("dataInput") Timestamp  dataInput,
			                                                     @Param("idOrdinativoTs") Integer idOrdinativoTs);
	
	@Query("FROM SiacRLiquidazioneOrdFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTLiquidazione.liqId = :idLiquidazione AND " + condizione)
	public List<SiacRLiquidazioneOrdFin> findValidoByIdLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                     @Param("dataInput") Timestamp  dataInput,
			                                                     @Param("idLiquidazione") Integer idLiquidazione);
}