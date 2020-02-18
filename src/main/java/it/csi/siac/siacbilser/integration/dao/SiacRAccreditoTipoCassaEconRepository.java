/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;

public interface SiacRAccreditoTipoCassaEconRepository extends JpaRepository<SiacRAccreditoTipoCassaEcon, Integer>{
 
	@Query(" FROM SiacRAccreditoTipoCassaEcon r"
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "AND r.siacDAccreditoTipo.accreditoTipoCode = :accreditoTipoCode ")
	SiacRAccreditoTipoCassaEcon findByModalitaAccreditoSoggetto(@Param("accreditoTipoCode")String accreditoTipoCode, @Param("enteProprietarioId")Integer enteProprietarioId);
	
	

}
