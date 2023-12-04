/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTNumeroAtto;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTNumeroAttoRepository.
 */
public interface SiacTNumeroAttoRepository extends JpaRepository<SiacTNumeroAtto, Integer> {

	/**
	 * Ricerca il numero di una nuova variazione da inserire a partire dall'id del bilancio e dell'ente proprietario.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @param attoammTipoId the attoamm tipo id
	 * @param tipoNumerazione the tipo numerazione
	 * @return the siac t numero atto
	 */
	@Query("FROM SiacTNumeroAtto stna " 
		+ " WHERE stna.annoAtto = :anno "
		+ " AND stna.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
		+ " AND stna.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId "
		+ " AND stna.tipoNumerazione = :tipoNumerazione ")
	SiacTNumeroAtto findByAnnoEnteTipoAttoTipoNumerazione(@Param("anno") Integer anno, @Param("enteProprietarioId") Integer enteProprietarioId, @Param ("attoammTipoId") Integer attoammTipoId, 
			@Param("tipoNumerazione") String tipoNumerazione);
	
}
