/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;


public interface StrutturaAmministrativaRepository extends JpaRepository<SiacTClassFin, Integer> {
	
	String condizione = " AND ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND classifCode = :codice " 
			+ " AND siacDClassTipo.classifTipoCode = :codiceTipo and  siacDClassTipo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ condizione)
	public SiacTClassFin findStrutturaByCodiceAndIdEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                     @Param("codice") String codice,
			                                                                     @Param("codiceTipo") String codiceTipo,
			                                                                     @Param("dataInput") Timestamp  dataInput);
	
	
	
	
//	@Query("FROM SiacTClass c , in(cp.siacRClassFamTreesFiglio) cf " +
//			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
//			" c.classifId = cf.siacTClassPadre AND " +
//			" cf.siacTClassFiglio.codice = :codiceCdc AND cf.siacTClassFiglio.siacDClassTipo.codice = :codiceTipoCdc " 
//			+ condizione)
//	public SiacTClass findStrutturaAmministrativaPadreByIdFiglio(@Param("enteProprietarioId") Integer enteProprietarioId,
//			                                        @Param("codiceCdc") String codiceCdc,
//			                                        @Param("codiceTipoCdc") String codiceTipoCdc,
//			                                        @Param("dataInput") Timestamp  dataInput);
}