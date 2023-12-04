/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDDistintaFin;

public interface SiacDDistintaRepository extends JpaRepository<SiacDDistintaFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL )  order by distCode ASC";
	
	static final String FIND_DISTINTA_BY_CODE ="FROM SiacDDistintaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+" AND siacDDistintaTipo.distTipoCode = :codeTipo "
            +" AND distCode = :code " 
            +" AND "+condizione;
	
	
	static final String FIND_DISTINTA_BY_TYPE ="FROM SiacDDistintaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND siacDDistintaTipo.distTipoCode = :codeTipo " 
            +"AND "+condizione;

	static final String FIND_DISTINTA_BY_TYPE_AND_CODE ="FROM SiacDDistintaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND siacDDistintaTipo.distTipoCode = :codeTipo "
            +"AND distCode = :code "
            +"AND "+condizione;
	
	@Query(FIND_DISTINTA_BY_CODE)
	public SiacDDistintaFin findDistintaByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput, @Param("codeTipo") String codeTipo);

	@Query(FIND_DISTINTA_BY_TYPE)
	public List<SiacDDistintaFin> findDistintaByCodTipo(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput, @Param("codeTipo") String codeTipo);

	@Query(FIND_DISTINTA_BY_TYPE_AND_CODE)
	public SiacDDistintaFin findDDistintaValidaByEnteAndTipoAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
																 @Param("codeTipo") String codeTipo,
            													 @Param("code") String code,
            													 @Param("dataInput") Timestamp  dataInput);
}