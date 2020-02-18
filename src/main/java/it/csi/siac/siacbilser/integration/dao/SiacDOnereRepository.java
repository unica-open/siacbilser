/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDSommaNonSoggettaTipo;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacDOnereRepository extends JpaRepository<SiacDOnere, Integer> {
	
	
	/**
	 * Find nature onere by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param naturaOnere the natura onere
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDOnere c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND c.siacDOnereTipo.onereTipoId = :onereTipoId " +
			" AND c.dataCancellazione IS NULL " +
			" AND (c.dataFineValidita IS NULL OR c.dataFineValidita >= :now) " +
			" ORDER BY c.onereCode ")
	List<SiacDOnere> findTipiOnereValidiByNatureOnereEEnte(
						@Param("enteProprietarioId") Integer enteProprietarioId, 
						@Param("onereTipoId") Integer naturaOnere, 
						@Param("now") Date now);

	
	
	
	
	/**
	 * Find date storico tipo onere.
	 *
	 * @param onereId the onere id
	 * @return the list
	 */
	@Query(  " SELECT DISTINCT r.dataInizioValidita " +
			 " FROM SiacROnereAttr r " +
			 " WHERE r.siacDOnere.onereId = :onereId "+
			 " ORDER BY r.dataInizioValidita DESC")
	List<Date> findDateStoricoTipoOnere(@Param("onereId") Integer onereId);




	@Query(  "SELECT o " +
			" FROM SiacDOnere o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.onereCode = :onereCode " +
			" AND o.dataCancellazione IS NULL ")
	SiacDOnere findTipoOnereByCodiceEEnte(@Param("onereCode") String onereCode, @Param("enteProprietarioId") Integer enteProprietarioId);




	@Query( " FROM SiacDOnere o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.dataCancellazione IS NULL " +
			" AND o.siacDOnereTipo.onereTipoCode = :onereTipoCode " +
			" AND EXISTS ( FROM o.siacROnereSplitreverseIvaTipos r" +
			"				WHERE r.dataCancellazione IS NULL " +
			"				AND r.siacDSplitreverseIvaTipo.srivaTipoCode = :srivaTipoCode " +
			"			) " +
			" AND EXISTS ( FROM o.siacROnereAttrs ra" +
			"				WHERE ra.dataCancellazione IS NULL " +
			"				AND ra.siacTAttr.attrCode = :aliquotaCode" +
			"				AND ra.percentuale = :aliquotaIva) ")
	List<SiacDOnere> findSiacRDocOnereByAliquotaNaturaOnereETipoIva(@Param("aliquotaIva") BigDecimal aliquotaIva, 
																			@Param("onereTipoCode") String onereTipoCode,
																			@Param("srivaTipoCode") String srivaTipoCode,
																			@Param("enteProprietarioId") Integer enteProprietarioId,
																			@Param("aliquotaCode") String aliquotaCode);




	@Query(" SELECT rosns.siacDSommaNonSoggettaTipo " +
		     " FROM SiacROnereSommaNonSoggettaTipo rosns " +
		     " WHERE rosns.dataCancellazione IS NULL " +
		     " AND rosns.siacDOnere.onereId = :onereId ")
	List<SiacDSommaNonSoggettaTipo> findSommeNonSoggetteByTipoOnere(@Param("onereId") Integer onereId);
		
}
