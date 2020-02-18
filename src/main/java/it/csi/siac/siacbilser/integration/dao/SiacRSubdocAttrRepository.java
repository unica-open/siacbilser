/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;

/**
 * Repository per l'entity SiacRSubdocAttrRepository.
 *
 */
public interface SiacRSubdocAttrRepository extends JpaRepository<SiacRSubdocAttr, Integer> {
	
}
