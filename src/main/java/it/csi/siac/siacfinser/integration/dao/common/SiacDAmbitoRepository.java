/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;

public interface SiacDAmbitoRepository extends JpaRepository<SiacDAmbitoFin, Integer>{
	
	//"AMBITO_FIN"
	
	
	public static String QUERY = "FROM SiacDAmbitoFin c WHERE UPPER(c.ambitoCode) =UPPER(:codice)  "
			                    +"AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND c.dataCancellazione IS NULL ";
	//select by codice dove il code e' costante
	
	@Query(QUERY)
	public SiacDAmbitoFin findAmbitoByCode(@Param("codice") String codice,
			  							@Param("enteProprietarioId") Integer enteProprietarioId);
}
