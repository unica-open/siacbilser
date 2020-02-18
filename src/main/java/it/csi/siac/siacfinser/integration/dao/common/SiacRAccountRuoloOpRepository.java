/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRAccountRuoloOpFin;

public interface SiacRAccountRuoloOpRepository extends JpaRepository<SiacRAccountRuoloOpFin, Integer>{

	@Query("from SiacRAccountRuoloOpFin where siacTAccount.accountId = :idAccount")
	List<SiacRAccountRuoloOpFin> findByAccount(@Param("idAccount") Integer idAccount);
}
