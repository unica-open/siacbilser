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

import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;

public interface SiacRMutuoVoceLiquidazioneRepository extends JpaRepository<SiacRMutuoVoceLiquidazioneFin, Integer> {
	

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMutuoVoceLiquidazioneFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND" +
			" siacTLiquidazione.liqId = :idLiq AND "+condizione)
	public List<SiacRMutuoVoceLiquidazioneFin> findByEnteAndLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idLiq") Integer idLiq);
	
	
}