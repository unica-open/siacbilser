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

import it.csi.siac.siacfinser.integration.entity.SiacRBilFaseOperativaFin;

public interface SiacRBilFaseOperativaRepository extends JpaRepository<SiacRBilFaseOperativaFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRBilFaseOperativaFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTBil.bilId = :idBil  AND "+condizione )
	public List<SiacRBilFaseOperativaFin> findValido(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                      @Param("idBil") Integer idBil,
			                                      @Param("dataInput") Timestamp  dataInput);
}
