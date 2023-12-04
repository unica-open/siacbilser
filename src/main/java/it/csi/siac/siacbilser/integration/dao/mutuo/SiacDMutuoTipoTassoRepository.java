/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoTipoTasso;

/**
 * Repository JPA per il SiacDMutoTipoTasso.
 * 
 * @author Daniela Tarantino
 * @version 27/02/2023 
 *
 */
public interface SiacDMutuoTipoTassoRepository extends JpaRepository<SiacDMutuoTipoTasso, Integer> {

	/**
	 * Ottiene un TipoTasso a partire dal codice e dall'uid dell'Ente Proprietario.
	 * 
	 * @param tipoTassoCodice     il codice del TipoTasso
	 * 
	 * @return il SiacDMutuoTipoTasso associato
	 */
	@Query( "SELECT p " +
			" FROM SiacDMutuoTipoTasso p " +
			" WHERE UPPER(p.mutuoTipoTassoCode) = UPPER(:mutuoTipoTassoCode) " )
	SiacDMutuoTipoTasso findByCodice(@Param("mutuoTipoTassoCode") String mutuoTipoTassoCode);

}
