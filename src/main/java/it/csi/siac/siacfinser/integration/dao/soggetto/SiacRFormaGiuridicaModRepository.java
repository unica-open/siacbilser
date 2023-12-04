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

import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaModFin;

public interface SiacRFormaGiuridicaModRepository extends JpaRepository<SiacRFormaGiuridicaModFin, Integer>{
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRFormaGiuridicaModFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacRFormaGiuridicaModFin>  findValidaByIdSoggetto(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);

	@Query("from SiacRFormaGiuridicaModFin where siacTSoggettoMod.sogModId = :idSoggettoMod AND "+condizione)
	public List<SiacRFormaGiuridicaModFin>  findValidaByIdSoggettoMod(@Param("idSoggettoMod") Integer idSoggetto, @Param("dataInput") Timestamp  dataInput);

}
