/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;

public interface SiacTOrdinativoTRepository extends JpaRepository<SiacTOrdinativoTFin, Integer> {
	String condizione = " ( (tOrd.dataInizioValidita < :dataInput)  AND (tOrd.dataFineValidita IS NULL OR :dataInput < tOrd.dataFineValidita) AND tOrd.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTOrdinativoTFin tOrd WHERE tOrd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tOrd.ordTsCode = :ordTsCode ")
	public SiacTOrdinativoTFin findOrdinativoTsByTsCode(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ordTsCode") BigDecimal ordTsCode);
	
	
	@Query("FROM SiacTOrdinativoTFin tOrd WHERE tOrd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tOrd.ordTsCode = :ordTsCode AND " + condizione)
	public SiacTOrdinativoTFin findOrdinativoTsValidoByTsCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						                       @Param("ordTsCode") BigDecimal ordTsCode,
			 						                       @Param("dataInput") Timestamp dataInput);
	
	
	@Query("FROM SiacTOrdinativoTFin tOrd WHERE tOrd.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tOrd.siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacTOrdinativoTFin> findSubOrdinativiByOrdinativo(@Param("idOrdinativo") Integer idOrdinativo,@Param("dataInput") Timestamp  dataInput,@Param("enteProprietarioId") Integer enteProprietarioId);
}