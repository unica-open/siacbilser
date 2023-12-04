/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRCartacontEsteraAttrFin;

public interface SiacRCartacontEsteraAttrRepository extends JpaRepository<SiacRCartacontEsteraAttrFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";
	
	@Query("FROM SiacRCartacontEsteraAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		       "                               siacTCartacontEstera.cartacestId = :idCartaContEstera AND " + 
			   "                               siacTAttr.attrCode = :attrCode AND " + condizione)
	public List<SiacRCartacontEsteraAttrFin> findValidaByIdCartaEsteraAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
																	       @Param("idCartaContEstera") Integer idCartaContEstera,
																	       @Param("attrCode") String attrCode,
																	       @Param("dataInput") Timestamp  dataInput);
	
}
