/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistroTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDIvaRegistroTipoRepository.
 */
public interface SiacDIvaRegistroTipoRepository extends JpaRepository<SiacDIvaRegistroTipo, Integer> {
	
	/**
	 * Find by ente proprietario filter by esigibilita tipo.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivaEsigibilitaTipoCode the iva esigibilita tipo code
	 * @return the list
	 */
	@Query( " SELECT rt " +
			" FROM SiacDIvaRegistroTipo rt " +
			" WHERE rt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (rt.dataFineValidita IS NULL OR rt.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND rt.dataCancellazione IS NULL " +
			" AND (:ivaesTipoCode = '' OR :ivaesTipoCode IS NULL OR rt.siacDIvaEsigibilitaTipo.ivaesTipoCode = :ivaesTipoCode) " +
			" ORDER BY rt.ivaregTipoCode")
	List<SiacDIvaRegistroTipo> findByEnteProprietarioFilterByEsigibilitaTipo(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivaesTipoCode") String ivaEsigibilitaTipoCode);
	
}
