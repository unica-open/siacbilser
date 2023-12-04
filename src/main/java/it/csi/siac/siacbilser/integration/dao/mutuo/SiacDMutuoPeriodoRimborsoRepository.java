/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoPeriodoRimborso;

/**
 * Repository JPA per il SiacDMutuoPeriodoRimborso.
 * 
 * @author Daniela Tarantino
 * @version 15/03/2023 
 *
 */
public interface SiacDMutuoPeriodoRimborsoRepository extends JpaRepository<SiacDMutuoPeriodoRimborso, Integer> {

	/**
	 * Ottiene un TipoTasso a partire dal codice e dall'uid dell'Ente Proprietario.
	 * 
	 * @param tipoTassoCodice     il codice del TipoTasso
	 * 
	 * @return il SiacDMutuoTipoTasso associato
	 */
	@Query( "SELECT p " +
			" FROM SiacDMutuoPeriodoRimborso p " +
			" WHERE UPPER(p.mutuoPeriodoRimborsoCode) = UPPER(:mutuoPeriodoRimborsoCode) " )
	SiacDMutuoPeriodoRimborso findByCodice(@Param("mutuoPeriodoRimborsoCode") String mutuoPeriodoRimborsoCode);

}
