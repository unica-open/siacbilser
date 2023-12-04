/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacSProvCassaFin;

public interface SiacSProvCassaFinRepository extends JpaRepository<SiacSProvCassaFin, Integer> {
	
	@Query(" FROM SiacSProvCassaFin st"
		 + " WHERE st.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
		 + " AND st.dataCancellazione IS NULL "
		 + " AND st.siacTProvCassaFin.provcId = :provcId"
		 + " ORDER BY st.dataCreazione DESC")
	public List<SiacSProvCassaFin> findByprovcIdOrderedByDate(@Param("provcId") Integer provcId,@Param("enteProprietarioId") Integer enteProprietarioId);
	
	
}