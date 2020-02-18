/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;

/**
 * The Interface SiacTVincoloRepository.
 */
public interface SiacTVincoloRepository extends JpaRepository<SiacTVincolo, Integer> {

//	@Query("SELCECT SiacTVincolo FROM SiacRVincoloBilElem r "
//			+ " WHERE r.siacTVincolo.  .siacRVincoloBilElems ")
//	List<SiacTVincolo> findVincoloValidoByCapitoloAssociatoValido(@Param("elemId") Integer elemId);

}
