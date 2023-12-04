/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelTDatiRitenuta;

/**
 * The Interface SirfelTDatiRitenutaRepository.
 */
public interface SirfelTDatiRitenutaRepository extends JpaRepository<SirfelTDatiRitenuta, Integer> {

	
	
	

}
