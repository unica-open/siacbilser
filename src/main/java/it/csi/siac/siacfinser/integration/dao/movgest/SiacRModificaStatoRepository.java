/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;

public interface SiacRModificaStatoRepository extends JpaRepository<SiacRModificaStatoFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRModificaStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRModificaStatoFin> findListaSiacRModificaStato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacRModificaStatoFin srmsf JOIN srmsf.siacTMovgestTsDetMods stmtdm WHERE stmtdm.movgestTsDetModId = :uid AND " + condizione)
	public List<SiacRModificaStatoFin> findListaSiacRModificaStatoBySiacTMovgestTsDetModFin(@Param("uid") Integer uidSiacTMovgestTsDetModFin,  @Param("dataInput") Date dataInput);
}
