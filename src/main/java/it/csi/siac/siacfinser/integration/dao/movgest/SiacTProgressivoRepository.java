/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTProgressivoFin;

public interface SiacTProgressivoRepository extends JpaRepository<SiacTProgressivoFin, Integer> {
	@Query("FROM SiacTProgressivoFin WHERE progKey = :progKey AND ambitoId = :ambitoId AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public List<SiacTProgressivoFin> findByKey(@Param("progKey") String progKey, @Param("ambitoId") Integer ambitoId, @Param("enteProprietarioId") Integer enteProprietarioId);
}