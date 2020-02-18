/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRBilElemAttoLeggeRepository.
 */
public interface SiacRBilElemAttoLeggeRepository extends
		JpaRepository<SiacRBilElemAttoLegge, Integer> {
	
	
	/** The Constant ricercaRel. */
	static final String RICERCA_REL="FROM SiacRBilElemAttoLegge rel "
			+ "WHERE  rel.siacTBilElem.elemId = :elemId " +
			" AND rel.siacTBilElem.siacTBil.bilId = :bilId";
	
	/**
	 * Find by elem id and bil id.
	 *
	 * @param elemId the elem id
	 * @param bilId the bil id
	 * @return the list
	 */
	@Query(RICERCA_REL)
	List<SiacRBilElemAttoLegge> findByElemIdAndBilId(@Param("elemId") Integer elemId, @Param("bilId") Integer bilId);

	
	
	/**
	 * Find by elem id and attolegge id.
	 *
	 * @param elemId the elem id
	 * @param attoleggeId the attolegge id
	 * @return the list
	 */
	@Query("FROM SiacRBilElemAttoLegge rel "
			+ " WHERE  rel.siacTBilElem.elemId = :elemId "
			+ " AND rel.siacTAttoLegge.attoleggeId = :attoleggeId ")
	List<SiacRBilElemAttoLegge> findByElemIdAndAttoleggeId(@Param("elemId") Integer elemId, @Param("attoleggeId") Integer attoleggeId);

	
}
