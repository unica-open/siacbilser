/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRGruppoAccountFin;

public interface SiacRGruppoAccountRepository extends JpaRepository<SiacRGruppoAccountFin, Integer>{

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRGruppoAccountFin where siacTAccount.accountId = :idAccount AND dataFineValidita IS NULL AND dataCancellazione IS NULL")
	List<SiacRGruppoAccountFin> findGruppiByAccountId(@Param("idAccount") Integer idAccount);		
}
