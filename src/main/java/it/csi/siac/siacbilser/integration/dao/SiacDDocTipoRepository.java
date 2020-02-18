/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDDocTipoRepository.
 */
public interface SiacDDocTipoRepository extends JpaRepository<SiacDDocTipo, Integer> {

	/**
	 * Find by codice tipo codice fam tipo and ente proprietario.
	 *
	 * @param docTipoCode the doc tipo code
	 * @param docFamTipoCode the doc fam tipo code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac d doc tipo
	 */
	@Query("FROM SiacDDocTipo t WHERE t.docTipoCode = :docTipoCode "
			+ " AND t.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode "
			+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SiacDDocTipo findByCodiceTipoCodiceFamTipoAndEnteProprietario(@Param("docTipoCode") String docTipoCode, 
			@Param("docFamTipoCode") String docFamTipoCode , @Param("enteProprietarioId") Integer enteProprietarioId);

	
	@Query(" UPDATE SiacRSubdocAttr r "
			+ " SET boolean_ = :flagValue "
			+ " WHERE r.siacTAttr IN ( "
			+ "     FROM SiacTAttr sta "
			+ "     WHERE sta.dataCancellazione IS NULL "
			+ "     AND sta.attrCode = :attrCode "
			+ "     AND sta.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " ) "
			+ " AND r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc IN ( "
			+ "     FROM SiacTSubdoc s"
			+ "     WHERE s.dataCancellazione IS NULL"
			+ "     AND s.siacTDoc.docId = :docId "
			+ " ) ")
	@Modifying
	void impostaFlagSulleQuote(@Param("docId") int docId, @Param("attrCode") String attrCode, @Param("flagValue") String flagValue, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(" SELECT s.siacTDoc.siacDDocTipo "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId = :subdocId ")
	SiacDDocTipo findBySubdocId(@Param("subdocId") Integer subdocId);
	

	
}
