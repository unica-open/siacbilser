/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDViaTipoFin;

public interface SiacDViaTipoRepository extends JpaRepository<SiacDViaTipoFin, Integer> {
	
	@Query("FROM SiacDViaTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND viaTipoCode = :viaTipo AND dataFineValidita is null")
	public List<SiacDViaTipoFin> findByTipo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("viaTipo") String viaTipo);
	
	
	
	
	public static String QUERY = "FROM SiacDViaTipoFin c WHERE UPPER(c.viaTipoDesc) LIKE CONCAT(UPPER(:descrizione), '%') "
			+"AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND c.dataCancellazione IS NULL ";
	        


	@Query(QUERY)  
	public List<SiacDViaTipoFin> findListaSedimeLike(@Param("descrizione") String descrizione,
												  @Param("enteProprietarioId") Integer enteProprietarioId);
	
	}
