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

import it.csi.siac.siacfinser.integration.entity.SiacRBilStatoOpFin;

public interface SiacRBilStatoOpRepository extends JpaRepository<SiacRBilStatoOpFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRBilStatoOpFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTBil.bilId = :idBil  AND "+condizione )
	public List<SiacRBilStatoOpFin> findValido(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("idBil") Integer idBil,@Param("dataInput") Timestamp  dataInput);

}
