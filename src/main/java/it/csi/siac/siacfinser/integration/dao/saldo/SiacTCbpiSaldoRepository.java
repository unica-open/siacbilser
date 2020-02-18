/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.saldo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoFin;

public interface SiacTCbpiSaldoRepository extends JpaRepository<SiacTCbpiSaldoFin, Integer>
{
	@Query(" FROM SiacTCbpiSaldoFin s "
			+ " WHERE s.contoCorrente.classifId=:idClassifContoCorrente "
			+ " AND s.bilancio=:bilancio "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId ")
	public SiacTCbpiSaldoFin readSaldoByContoCorrenteAndAnno(
			@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("bilancio") SiacTBil bilancio,
			@Param("idClassifContoCorrente") Integer idClassifContoCorrente
		);
}
