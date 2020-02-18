/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;



import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacDCespitiBeneTipoRepository extends JpaRepository<SiacDCespitiBeneTipo, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacDCespitiBeneTipo o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.cesBeneTipoCode = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacDCespitiBeneTipo findSiacDCespitiBeneTipoByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(  "SELECT COUNT(*) " +
			" FROM SiacDCespitiBeneTipo o " +
			" WHERE EXISTS ( " + 
			"   FROM SiacRCespitiBeneTipoContoPatrCat r " + 
			"   WHERE r.siacDCespitiBeneTipo = o " + 
			"   AND r.siacDCespitiCategoria.cescatId = :cescatId " +
			"   AND r.siacDCespitiCategoria.dataCancellazione IS NULL " +
			"   AND ( r.siacDCespitiCategoria.dataFineValidita IS NULL OR :dataInput <=  r.siacDCespitiCategoria.dataFineValidita) " +  
			"   AND (r.siacDCespitiCategoria.dataInizioValidita <= :dataInput) " + 
			"   AND r.dataCancellazione IS NULL " +			
			"   AND (r.dataFineValidita IS NULL OR :dataInput <= r.dataFineValidita) " +  
			"   AND (r.dataInizioValidita <= :dataInput) " +
			" ) " + 
			" AND o.dataCancellazione IS NULL " +
			" AND ( o.dataFineValidita IS NULL OR :dataInput <=  o.dataFineValidita) " +  
			" AND (o.dataInizioValidita <= :dataInput) "
			)
	Long countCespitiBeneTipoByUidCategoria(@Param("cescatId") Integer cescatId, @Param("dataInput") Date date);
	
	@Query( " SELECT o.dataFineValidita " +
			" FROM SiacDCespitiBeneTipo o " +
			" WHERE o.cesBeneTipoId = :cesBeneTipoId " +
			" AND o.dataCancellazione IS NULL ")
	Date findDataFineValiditaByTipoBeneId(@Param("cesBeneTipoId") Integer cesBeneTipoId);
}


