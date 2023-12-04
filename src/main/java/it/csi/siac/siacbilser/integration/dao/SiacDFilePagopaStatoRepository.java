/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDFilePagopaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;

public interface SiacDFilePagopaStatoRepository extends JpaRepository<SiacDFilePagopaStato, Integer> {
	@Query("SELECT fs FROM SiacDFilePagopaStato fs "
			+ " WHERE fs.siacTEnteProprietario=:ente AND fs.dataCancellazione IS NULL ")
	List<SiacDFilePagopaStato> getElencoStatoFilePagopa(
			@Param("ente") SiacTEnteProprietario ente
	);
	
	@Query("SELECT fs FROM SiacDFilePagopaStato fs "
			+ " WHERE fs.filePagopaStatoCode=:codice "
			+ " AND fs.siacTEnteProprietario=:ente AND fs.dataCancellazione IS NULL ")
	SiacDFilePagopaStato getStatoByCodice(
			@Param("ente") SiacTEnteProprietario ente,
			@Param("codice") String codice
	);
}
