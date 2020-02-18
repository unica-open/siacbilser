/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;

/**
 * The Interface SiacTMovgestBilRepository.
 */
@Component
public interface SiacTMovgestBilRepository extends JpaRepository<SiacTMovgest, Integer> {
	
	@Query(" SELECT m.movgestId "
			+ " FROM SiacTMovgest m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movgestAnno = :movgestAnno "
			+ " AND m.movgestNumero = :movgestNumero "
			+ " AND m.siacDMovgestTipo.movgestTipoCode = :movgestTipoCode "
			+ " AND m.siacTBil.bilId = :bilId")
	List<Integer> findUidMovgestByAnnoNumeroBilancio(
			@Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero,
			@Param("movgestTipoCode") String movgestTipoCode,
			@Param("bilId") Integer bilId
	);
	
		
	@Query(" SELECT m FROM SiacTMovgest m "
			+ " JOIN m.siacTMovgestTs mt "
			+ " JOIN mt.siacRMovgestTsAttoAmms mtaa "
			+ " WHERE mtaa.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND mtaa.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND mtaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND mtaa.siacTAttoAmm.dataCancellazione IS NULL "
			+ " AND mtaa.dataCancellazione IS NULL "
			+ " AND mt.siacDMovgestTsTipo.movgestTsTipoCode='T' "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND (:attoammSacId IS NULL OR EXISTS ("
			+ "	SELECT 1 FROM mtaa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
			+ " ) "
			+ " AND m.siacTEnteProprietario.enteProprietarioId =:enteProprietarioId")
	List<SiacTMovgest> findSiacTMovgestBySiacTAttoAmm(
		@Param("attoammNumero") Integer attoammNumero, 
		@Param("attoammAnno") String attoammAnno,
		@Param("attoammTipoId") Integer attoammTipoId,
		@Param("attoammSacId") Integer attoammSacId,
		@Param("enteProprietarioId") Integer enteProprietarioId);
			
		
		
		
	
		

}
