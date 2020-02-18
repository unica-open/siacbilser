/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTOilRicevuta;

public interface SiacTOilRicevutaRepository extends JpaRepository<SiacTOilRicevuta, Integer> {
	
	//String condizione = " ( (tOilRicevuta.dataInizioValidita < :dataInput)  AND (tOilRicevuta.dataFineValidita IS NULL OR :dataInput < tOilRicevuta.dataFineValidita) AND tOilRicevuta.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTOilRicevuta tOilRicevuta WHERE tOilRicevuta.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tOilRicevuta.siacTOrdinativo.ordId = :ordId AND" +
			" tOilRicevuta.dataCancellazione IS NULL order by tOilRicevuta.oilOrdTrasmOilData DESC")
	public List<SiacTOilRicevuta> findTOilRicevutaByOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ordId") Integer ordId);
		
}