/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTIvaGruppoRepository.
 */
public interface SiacTIvaGruppoRepository extends JpaRepository<SiacTIvaGruppo, Integer> {
	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query( " SELECT g " +
			" FROM SiacTIvaGruppo g " +
			" WHERE g.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (g.dataFineValidita IS NULL OR g.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND g.dataCancellazione IS NULL " +
			" ORDER BY g.ivagruCode ")
	List<SiacTIvaGruppo> findByEnteProprietario(@Param("enteProprietarioId") Integer enteProprietarioId);

	
	/**
	 * Find by codice.
	 *
	 * @param codice the codice
	 * @return the siac t iva gruppo
	 */
	@Query( " SELECT g " +
			" FROM SiacTIvaGruppo g " +
			" WHERE g.ivagruCode = :ivagruCode " +
			" AND (g.dataFineValidita IS NULL OR g.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND g.dataCancellazione IS NULL " )
	SiacTIvaGruppo findByCodice(@Param("ivagruCode") String codice);
	
	@Query(" SELECT DISTINCT rigp.ivagruproAnno"
			+ " FROM SiacRIvaGruppoProrata rigp "
			+ " WHERE rigp.dataCancellazione IS NULL "
			+ " AND rigp.siacTIvaGruppo.ivagruId = :ivagruId "
			+ " AND (rigp.siacTIvaGruppo.dataFineValidita IS NULL OR rigp.siacTIvaGruppo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " ORDER BY rigp.ivagruproAnno ")
	List<Integer> findAnnualizzazioniByUid(@Param("ivagruId") Integer ivagruId);

}
