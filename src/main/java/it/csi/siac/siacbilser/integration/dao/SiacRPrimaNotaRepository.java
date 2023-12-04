/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNota;

/**
 * The Interface SiacTPrimaNotaRepository.
 */
public interface SiacRPrimaNotaRepository extends JpaRepository<SiacRPrimaNota, Integer> {

	@Query(" FROM SiacRPrimaNota r" +
			" WHERE r.siacTPrimaNotaPadre.pnotaId = :pnotaIdDa " +
			" AND r.siacTPrimaNotaFiglio.pnotaId = :pnotaIdA " +
			" AND r.dataCancellazione IS NULL ")
	List<SiacRPrimaNota> findLegamePrimeNoteByUidPadreEUidFiglio(@Param("pnotaIdDa") Integer uid, @Param("pnotaIdA") Integer uid2);

	@Query(" FROM SiacDPrimaNotaRelTipo d" +
			" WHERE d.pnotaRelTipoCode = :pnotaRelTipoCode " +
			" AND d.dataCancellazione IS NULL " +
			" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	SiacDPrimaNotaRelTipo findTipoRelazioneByCodice(@Param("pnotaRelTipoCode")String pnotaRelTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);

	
}
