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

import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceCartacontDet;

public interface SiacRMutuoVoceCartacontDetRepository extends JpaRepository<SiacRMutuoVoceCartacontDet, Integer> {
	

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMutuoVoceCartacontDet WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND" +
			" siacTCartacontDet.cartacDetId = :cartacDetId AND "+condizione)
	public List<SiacRMutuoVoceCartacontDet> findByEnteAndCartacont(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("cartacDetId") Integer cartacDetId);
	
	
}