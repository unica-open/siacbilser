/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAttrTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttrFin;


public interface SiacTAttrRepository extends JpaRepository<SiacTAttrFin, Integer> {
		
	@Query("SELECT t FROM SiacTAttrFin t WHERE t.attrCode = :code AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public SiacTAttrFin getTAttrByCode(@Param("code") String attrCode,@Param("enteProprietarioId")Integer enteProprietarioId );
	
	@Query("Select t.siacDAttrTipo from SiacTAttrFin t where t.attrCode = :code AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public SiacDAttrTipoFin getTAttrTipoByCode(@Param("code") String attrCode,@Param("enteProprietarioId")Integer enteProprietarioId );
	
	
	@Query("from SiacTAttrFin t where t.attrCode = :code AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId and " 
			+ " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ")
	public SiacTAttrFin getTAttrByCode(@Param("code") String attrCode,@Param("enteProprietarioId")Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput );
	
	
}
