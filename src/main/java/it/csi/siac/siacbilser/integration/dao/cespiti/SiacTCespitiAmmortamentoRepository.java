/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiAmmortamentoRepository extends JpaRepository<SiacTCespitiAmmortamento, Integer> {
	
	@Query(" SELECT  o.cesId, o.valoreAttuale, o.dataIngressoInventario, rcalc.aliquotaAnnua, rcalc.siacDCespitiCategoriaCalcoloTipo.cescatCalcoloTipoCode " + 
	        " FROM SiacTCespiti o , SiacRCespitiBeneTipoContoPatrCat rben, SiacRCespitiCategoriaAliquotaCalcoloTipo rcalc   " + 
	        " WHERE o.dataCancellazione IS NULL " +
	        " AND o.cesId IN (:uidsCespiti) " + 
	        " AND o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		    " AND o.siacDCespitiBeneTipo = rben.siacDCespitiBeneTipo " + 
		    " AND rben.siacDCespitiCategoria = rcalc.siacDCespitiCategoria " +
	        " AND o.siacDCespitiBeneTipo.dataCancellazione IS NULL " +
	        " AND (o.siacDCespitiBeneTipo.dataFineValidita IS NULL OR :dataInput <= o.siacDCespitiBeneTipo.dataFineValidita) " +
	        " AND rben.dataCancellazione IS NULL " + 
	        " AND (rben.dataFineValidita IS NULL OR :dataInput <= rben.dataFineValidita) " +
			" AND rben.siacDCespitiCategoria.dataCancellazione IS NULL " +
			" AND (rben.siacDCespitiCategoria.dataFineValidita IS NULL OR :dataInput <= rben.siacDCespitiCategoria.dataFineValidita) " +
			" AND rcalc.dataCancellazione IS NULL " + 
			" AND ( rcalc.dataFineValidita IS NULL OR :dataInput <=  rcalc.dataFineValidita) "
			)
	List<Object[]> findDatiCespitiAmmortamento(@Param("uidsCespiti") List<Integer> uidsCespiti, @Param("dataInput") Date dataInput, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	@Query(" FROM SiacTCespitiAmmortamento tca " + 
			" WHERE tca.dataCancellazione IS NULL " + 
			" AND tca.siacTCespiti.cesId = :cesId  " + 
			" AND tca.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" ORDER BY tca.dataCreazione DESC "
			)
	List<SiacTCespitiAmmortamento> findSiacTCespitiAmmortamentosByCesId(@Param("cesId") Integer cesId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query( " SELECT MAX(tcead.cesAmmDettAnno), COALESCE(SUM(tcead.cesAmmDettImporto), 0), tc.valoreAttuale " + 
			" FROM SiacTCespitiAmmortamentoDett tcead, SiacTCespiti tc " + 
			" WHERE tcead.dataCancellazione IS NULL " + 
			" AND tcead.siacTCespitiAmmortamento.siacTCespiti = tc " +
			" AND tcead.numRegDefAmmortamento IS NOT NULL " +
			" AND tcead.numRegDefAmmortamento <> '' " +
			" AND tc.dataCancellazione IS NULL " + 
			" AND tcead.siacTCespitiAmmortamento.cesAmmId = :cesAmmId  " + 
			" AND tcead.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" GROUP BY tc.valoreAttuale "
			)
	List<Object[]> findDatiRegistrazioneDefinitivaTestataAmmortamento(@Param("cesAmmId") Integer ceAmmId, @Param("enteProprietarioId") Integer enteProprietarioId);
}
