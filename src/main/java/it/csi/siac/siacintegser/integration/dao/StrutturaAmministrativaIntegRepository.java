/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.integration.dao;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;

@Deprecated // FIXME classi da includere nelle funzionalita' di BIL/FIN gia' esistenti, non devono fare parte di INTEG

public interface StrutturaAmministrativaIntegRepository extends JpaRepository<SiacTClassFin, Integer> {
	
	String condizione = " AND ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND classifCode = :codice " 
			+ " AND siacDClassTipo.classifTipoCode = :codiceTipo and  siacDClassTipo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ condizione)
	public SiacTClassFin findStrutturaByCodiceAndIdEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                     @Param("codice") String codice,
			                                                                     @Param("codiceTipo") String codiceTipo,
			                                                                     @Param("dataInput") Timestamp  dataInput);
}