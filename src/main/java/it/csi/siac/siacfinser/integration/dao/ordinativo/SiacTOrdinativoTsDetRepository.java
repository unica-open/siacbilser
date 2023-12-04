/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;

public interface SiacTOrdinativoTsDetRepository extends JpaRepository<SiacTOrdinativoTsDetFin, Integer> {
	String condizione = " ( (tsDet.dataInizioValidita < :dataInput)  AND (tsDet.dataFineValidita IS NULL OR :dataInput < tsDet.dataFineValidita) AND tsDet.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTOrdinativoTsDetFin tsDet WHERE tsDet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tsDet.siacTOrdinativoT.ordTsId = :ordTsId ")
	public List<SiacTOrdinativoTsDetFin> findOrdinativoTsDetValidoByOrdTsId(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                             @Param("ordTsId") Integer ordTsId);	
	
	@Query("FROM SiacTOrdinativoTsDetFin tsDet WHERE tsDet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND tsDet.siacTOrdinativoT.ordTsId = :ordTsId AND " + condizione)
	public List<SiacTOrdinativoTsDetFin> findOrdinativoTsDetValidoByOrdTsId(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                             @Param("ordTsId") Integer ordTsId,
			 						                                     @Param("dataInput") Timestamp dataInput);
	
	
	@Query("FROM SiacTOrdinativoTsDetFin tsDet WHERE tsDet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND tsDet.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :tipoCode AND tsDet.siacTOrdinativoT.ordTsId = :idOrdTs AND "+condizione)
	public List<SiacTOrdinativoTsDetFin> findValidoByTipo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput
			,@Param("tipoCode") String tipoCode, @Param("idOrdTs") Integer idOrdTs);

	@Query(" FROM SiacTOrdinativoTsDetFin tsDet "
			+ "WHERE tsDet.siacTOrdinativoT.ordTsId = :ordTsId "
			+ "AND tsDet.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = :ordTsDetTipoCode "
			+ " AND tsDet.dataCancellazione IS NULL "
			+ " AND tsdet.dataFineValidita IS NULL ")
	public SiacTOrdinativoTsDetFin findOrdinativoTsDetByOrdTsIdAndOrdTsDetIdTipoCode(@Param("ordTsId") Integer ordTsId, @Param("ordTsDetTipoCode")String ordTsDetTipoCode);
	
	
}