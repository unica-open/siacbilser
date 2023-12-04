/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiClassificazioneGiuridica;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacDCespitiClassificazioneGiuridicaRepository extends JpaRepository<SiacDCespitiClassificazioneGiuridica, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacDCespitiClassificazioneGiuridica o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.cesClassGiuCode = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacDCespitiClassificazioneGiuridicaRepository findCespitiClassificazioneGiuridicaByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);

}
