/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaClass;

public interface SiacRPrimaNotaClassRepository extends JpaRepository<SiacRPrimaNotaClass, Integer> {

	@Query(" FROM SiacRPrimaNotaClass relaz "
			+ " WHERE relaz.siacTPrimaNota.pnotaId = :uidPrimaNota "
			)
	List<SiacRPrimaNotaClass> findStruttureAssociatePrimaNota(@Param("uidPrimaNota") Integer uidPrimaNota);

	@Query(" FROM SiacRPrimaNotaClass relaz "
			+ " WHERE relaz.siacTPrimaNota.pnotaId = :uidPrimaNota "
			+ " AND relaz.dataCancellazione IS NULL "
			+ " AND (relaz.dataFineValidita IS NULL OR relaz.dataFineValidita > CURRENT_TIMESTAMP ) "
			)
	List<SiacRPrimaNotaClass> findStrutturePrimaNotaValide(@Param("uidPrimaNota") Integer uidPrimaNota);
	
}
