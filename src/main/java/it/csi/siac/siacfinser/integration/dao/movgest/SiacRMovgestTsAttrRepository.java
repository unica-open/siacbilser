/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;

public interface SiacRMovgestTsAttrRepository extends JpaRepository<SiacRMovgestTsAttrFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMovgestTsAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsAttrFin> findListaSiacRMovgestTsAttr(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacRMovgestTsAttrFin where siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione+" AND siacTAttr.attrCode = :code")
	public List<SiacRMovgestTsAttrFin>  findValidoByIdMovGestTsAndCode(@Param("idMovgestTs") Integer idMovgestTs, @Param("code") String code,@Param("dataInput") Timestamp  dataInput);

	
	@Query("from SiacRMovgestTsAttrFin where siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacRMovgestTsAttrFin>  findAllValidiByMovgestTs(@Param("idMovgestTs") Integer idMovgestTs,@Param("dataInput") Timestamp  dataInput);
}


