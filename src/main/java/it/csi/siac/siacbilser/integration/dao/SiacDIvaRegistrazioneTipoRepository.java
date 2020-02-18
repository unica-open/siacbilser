/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistrazioneTipo;

/**
 * The Interface SiacDIvaRegistrazioneTipoRepository.
 */
public interface SiacDIvaRegistrazioneTipoRepository extends JpaRepository<SiacDIvaRegistrazioneTipo, Integer> {
	
	/**
	 * Ricerca SiacDIvaRegistrazioneTipo per ente proprietario e codice del tipo famiglia.
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param docFamTipoCode codice del tipo famiglia
	 * 
	 * @return la lista di SiacDIvaRegistrazioneTipo trovate
	 */
	@Query( " SELECT rt " +
			" FROM SiacDIvaRegistrazioneTipo rt " +
			" WHERE rt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (:docFamTipoCode = '' OR EXISTS (FROM rt.siacRIvaRegTipoDocFamTipos r " +
			"                                      WHERE r.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode) ) " +			
			" AND (rt.dataFineValidita IS NULL OR rt.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND rt.dataCancellazione IS NULL " +
			" ORDER BY rt.regTipoCode")
	List<SiacDIvaRegistrazioneTipo> findByEnteProprietarioFilterByFlags(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("docFamTipoCode") String docFamTipoCode);
	



	/**
	 * Ricerca SiacDIvaRegistrazioneTipo per ente proprietario e codice del tipo di registrazione.
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param regTipoCode codice del tipo di registrazione
	 * @return
	 */
	@Query( " SELECT rt " +
			" FROM SiacDIvaRegistrazioneTipo rt " +
			" WHERE rt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND  rt.regTipoCode = :regTipoCode " +
			" AND (rt.dataFineValidita IS NULL OR rt.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND rt.dataCancellazione IS NULL " )
	SiacDIvaRegistrazioneTipo findByEnteProprietarioECodice(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("regTipoCode") String regTipoCode);

}
