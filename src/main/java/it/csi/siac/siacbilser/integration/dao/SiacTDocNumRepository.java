/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTDocNum;

/**
 * The Interface SiacTDocNumRepository.
 */
public interface SiacTDocNumRepository extends JpaRepository<SiacTDocNum, Integer> {
	
	@Query(" FROM SiacTDocNum dn WHERE dn.docAnno = :docAnno AND dn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dn.siacDDocTipo.docTipoId = :docTipoId  ")
	SiacTDocNum findByAnno(@Param("docAnno") Integer docAnno,@Param("docTipoId") Integer docTipoId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
}
