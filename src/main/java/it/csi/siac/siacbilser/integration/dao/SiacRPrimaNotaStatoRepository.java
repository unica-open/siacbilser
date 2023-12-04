/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;

/**
 * The Interface SiacTPdceContoRepository.
 */
public interface SiacRPrimaNotaStatoRepository extends JpaRepository<SiacRPrimaNotaStato, Integer> {
	
	@Query("SELECT pns.siacDPrimaNotaStato FROM SiacRPrimaNotaStato pns "
			+ " WHERE pns.siacTPrimaNota=:siacTPrimaNota "
			+ " AND pns.dataCancellazione IS NULL "
			+ " AND pns.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (pns.dataFineValidita IS NULL OR pns.dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacDPrimaNotaStato findStatoAttualePrimaNota(@Param("siacTPrimaNota") SiacTPrimaNota siacTPrimaNota);
}
