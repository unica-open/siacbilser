/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDCausaleFin;

public interface SiacDCausaleFinRepository extends JpaRepository<SiacDCausaleFin, Integer>{
	
	String condizione =  " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query(  "SELECT c " +
			" FROM SiacDCausaleFin c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND UPPER(c.causCode) LIKE UPPER ( :causCode)" +
			" AND EXISTS ( FROM  c.siacRCausaleTipos ct "+
			" 	 	WHERE ct.siacDCausaleTipo.siacDCausaleFamTipo.causFamTipoCode = :causFamTipoCode "+
			" 	  ) "+
			"  AND "+condizione+
			" ORDER BY c.causCode ")
	public List<SiacDCausaleFin> findCausaleValidaByCodice(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("dataInput") Timestamp  dataInput,
			@Param("causCode") String causCode, 
			@Param("causFamTipoCode") String causFamTipoCode);
	
	
	@Query(  "SELECT c " +
			" FROM SiacDCausaleFin c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND c.causId = :causaleId " +
			"  AND "+condizione)
	public SiacDCausaleFin findByUidEnteAndValida(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("causaleId") Integer causaleId,
			@Param("dataInput") Timestamp  dataInput
			);

}
