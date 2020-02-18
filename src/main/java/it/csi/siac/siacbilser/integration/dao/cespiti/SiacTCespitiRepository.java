/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiRepository extends JpaRepository<SiacTCespiti, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacTCespiti o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.cesCode = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacTCespiti findCespitiByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query("FROM SiacTCespiti tc "
			+ " WHERE tc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tc.numInventario = :numInventario "
			+ " AND tc.dataCancellazione IS NULL ")
	SiacTCespiti findByNumInventarioEEnte(@Param("numInventario") String numInventario, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(  "SELECT COUNT(*) " +
			" FROM SiacTCespiti o " +
			" WHERE o.siacDCespitiBeneTipo.cesBeneTipoId = :cesBeneTipoId " +
			" AND o.dataCancellazione IS NULL "+
			" AND o.siacDCespitiBeneTipo.dataCancellazione IS NULL ")
	Long countCespitiByUidTipoBene(@Param("cesBeneTipoId") Integer cesBeneTipoId);
	
	@Query( " FROM SiacTCespiti o WHERE o.cesId = :uid  AND   o.dataCancellazione IS NULL ")
	SiacTCespiti findDettaglioCespiteById(@Param("uid") Integer uid);
	
	@Query( " FROM SiacTCespiti o " + 
			" WHERE o.cesId IN :cesIds  " + 
			" AND o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND   o.dataCancellazione IS NULL ")
	List<SiacTCespiti> findCespitiByIdsAndEnte(@Param("cesIds") List<Integer> cesIds, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" FROM SiacTCespiti o " + 
			" WHERE o.siacTCespitiDismissioni.cesDismissioniId = :cesDismissioniId  " + 
			" AND o.flgStatoBene = :flgStatoBene " + 
			" AND o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND o.dataCancellazione IS NULL ")
	public List<SiacTCespiti> findCespitiCollegatiByDismissioneId(@Param("cesDismissioniId") Integer cesDismissioniId, @Param("flgStatoBene") Boolean flgStatoBene, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query( " SELECT COUNT(*) " + 
            " FROM SiacRCespitiPrimaNota rcpn " + 
			" WHERE rcpn.dataCancellazione IS NULL " +
            " AND rcpn.siacTCespiti.cesId = :cesId"
			)
	public Long countSiacTPrimeNoteAssociateACespiti(@Param("cesId") Integer cesId);
}