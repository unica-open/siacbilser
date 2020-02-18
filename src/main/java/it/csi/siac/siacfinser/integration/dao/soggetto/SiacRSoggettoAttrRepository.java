/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;

public interface SiacRSoggettoAttrRepository extends JpaRepository<SiacRSoggettoAttrFin, Integer> {

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	
	@Query("from SiacRSoggettoAttrFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione+" AND siacTAttr.attrCode = :notecod")
	public List<SiacRSoggettoAttrFin>  findValidaByIdSoggettoAndCode(@Param("idSoggetto") Integer idSoggetto, @Param("notecod") String notecod,@Param("dataInput") Timestamp  dataInput);

	
	@Query("from SiacRSoggettoAttrFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacRSoggettoAttrFin>  findAllValidiByIdSoggetto(@Param("idSoggetto") Integer idSoggetto, @Param("dataInput") Timestamp  dataInput);
}
