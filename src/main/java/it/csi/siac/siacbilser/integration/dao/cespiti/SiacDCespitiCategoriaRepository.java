/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacDCespitiCategoriaRepository extends JpaRepository<SiacDCespitiCategoria, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacDCespitiCategoria o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.cescatCode = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacDCespitiCategoria findCategoriaCespitiByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);

	// XXX: da valutare la condizione nel caso di storicizzazione della categoria
	@Query( " SELECT COUNT(dcbt) "
			+ " FROM SiacDCespitiBeneTipo dcbt "
			+ " WHERE dcbt.dataCancellazione IS NULL "
			+ " AND (dcbt.dataFineValidita IS NULL OR :dataInput <= dcbt.dataFineValidita) "
			+ " AND (dcbt.dataInizioValidita <= :dataInput) "
			+ " AND dcbt.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "   FROM SiacRCespitiBeneTipoContoPatrCat r "
			+ "   WHERE r.siacDCespitiBeneTipo = dcbt "
			+ "   AND r.siacDCespitiCategoria.cescatId = :cescatId "
			+ "   AND r.siacDCespitiCategoria.dataCancellazione IS NULL "
			+ "   AND (r.siacDCespitiCategoria.dataFineValidita IS NULL OR :dataInput <=  r.siacDCespitiCategoria.dataFineValidita) "
			+ "   AND (r.siacDCespitiCategoria.dataInizioValidita <= :dataInput) "
			+ "   AND r.dataCancellazione IS NULL "
			+ "   AND (r.dataFineValidita IS NULL OR :dataInput <= r.dataFineValidita) "
			+ "   AND (r.dataInizioValidita <= :dataInput) "
			+ " ) ")
	Long countTipoBeneByCategoriaCespite(@Param("cescatId") Integer cescatId, @Param("dataInput") Date dataInput);
}


