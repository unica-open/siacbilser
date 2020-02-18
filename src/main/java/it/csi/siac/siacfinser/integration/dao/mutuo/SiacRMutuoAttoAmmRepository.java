/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacfinser.integration.entity.SiacRMutuoAttoAmmFin;

public interface SiacRMutuoAttoAmmRepository extends JpaRepository<SiacRMutuoAttoAmmFin, Integer> {
	
}