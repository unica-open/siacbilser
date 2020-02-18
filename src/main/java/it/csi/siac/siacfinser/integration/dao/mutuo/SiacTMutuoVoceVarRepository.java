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

import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceVarFin;

public interface SiacTMutuoVoceVarRepository extends JpaRepository<SiacTMutuoVoceVarFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTMutuoVoceVarFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMutuoVoce.mutVoceId = :voceMutuoId AND "+condizione)
	public List<SiacTMutuoVoceVarFin> findVariazioniVociMutuoValideByEnteEMutuoId(@Param("enteProprietarioId") Integer enteProprietarioId,
			 			  			                                           @Param("voceMutuoId") Integer voceMutuoId,
			 						                                           @Param("dataInput") Timestamp dataInput);
}