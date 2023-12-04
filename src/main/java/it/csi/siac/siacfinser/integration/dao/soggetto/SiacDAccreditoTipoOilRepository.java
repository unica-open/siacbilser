/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoTipoOilFin;

public interface SiacDAccreditoTipoOilRepository extends JpaRepository<SiacDAccreditoTipoOilFin, Integer>{
	
	@Query(value="select CAST(coalesce (count(*),0) AS integer) " + 
			" from siac_d_accredito_tipo_oil oil ,siac_r_accredito_tipo_oil r, siac_d_accredito_tipo tipo " + 
			" where oil.ente_proprietario_id = :idEnte " + 
			" and oil.fl_pagopa is TRUE " + 
			" and r.accredito_tipo_oil_id =oil.accredito_tipo_oil_id " + 
			" and tipo.accredito_tipo_id =r.accredito_tipo_id " + 
			" and tipo.accredito_tipo_code = :codiceAccreditoTipo " + 
			" and r.data_cancellazione is null " + 
			" and r.validita_fine is null " + 
			" and oil.data_cancellazione is null " + 
			" and oil.validita_fine is null " + 
			" and tipo.data_cancellazione is null " + 
			" and tipo.validita_fine is null", nativeQuery = true)
	public int countAccreditoTipoOilPagoPA(
			@Param("idEnte") Integer idEnte,
			@Param("codiceAccreditoTipo") String codiceAccreditoTipo);
 }
