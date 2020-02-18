/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacfinser.integration.entity.SiacTCartacontEsteraFin;

public interface SiacTCartaContEsteraRepository extends JpaRepository<SiacTCartacontEsteraFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";

}