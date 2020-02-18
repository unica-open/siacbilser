/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;

public interface SiacDBilElemCategoriaRepository  extends JpaRepository<SiacDBilElemCategoria, Integer> {
	
	/**
	 * Find codici bollo by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param integer 
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDBilElemCategoria c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND c.dataCancellazione IS NULL " +
			" AND c.dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (c.dataFineValidita IS NULL OR c.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND EXISTS ( " +
			"     FROM c.siacRBilElemTipoCategorias rbetc " +
			"     WHERE rbetc.dataCancellazione IS NULL " +
			"     AND rbetc.dataInizioValidita < CURRENT_TIMESTAMP "+
			"     AND (rbetc.dataFineValidita IS NULL OR rbetc.dataFineValidita > CURRENT_TIMESTAMP) " +
			"     AND rbetc.siacDBilElemTipo.elemTipoId = :elemTipoId " +
			" ) " +
			" ORDER BY c.elemCatCode ")
	List<SiacDBilElemCategoria> findCategoriaCapitoloByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("elemTipoId") Integer elemTipoId);
	
	
	/**
	 * Find codici bollo by codice.
	 *
	 * @param codice the codice
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac d codicebollo
	 */
	@Query(  "SELECT c " +
			" FROM SiacDBilElemCategoria c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND elemCatCode = :elemCatCode " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.elemCatCode ")
	SiacDBilElemCategoria findCategoriaCapitoloByCodice(@Param("elemCatCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);

}
