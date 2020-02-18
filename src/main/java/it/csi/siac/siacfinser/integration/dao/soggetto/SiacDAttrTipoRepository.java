/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAttrTipoFin;

public interface SiacDAttrTipoRepository extends JpaRepository<SiacDAttrTipoFin, Integer> {

	
	
	String Query="Select t.attrTipoDesc from SiacDAttrTipoFin t where t.attrTipoId = :id AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId";
	
	
	@Query(Query)
	public String getAttrTipoDescByIdANDEnte(@Param("id") Integer idSiacDAttrTipo,@Param("enteProprietarioId")Integer enteProprietarioId );
	

}
