/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;

/**
 * The Interface SiacTCronopElemRepository.
 */
public interface SiacTCronopElemRepository extends JpaRepository<SiacTCronopElem, Integer> {

	

}
