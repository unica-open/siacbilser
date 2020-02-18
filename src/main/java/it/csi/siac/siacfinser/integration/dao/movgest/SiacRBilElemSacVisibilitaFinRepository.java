/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRBilElemSacVisibilitaFin;

public interface SiacRBilElemSacVisibilitaFinRepository extends JpaRepository<SiacRBilElemSacVisibilitaFin, Integer> {
	
	String condizione =  " ( (rel.dataInizioValidita < :dataInput)  AND (rel.dataFineValidita IS NULL OR :dataInput < rel.dataFineValidita) AND rel.dataCancellazione IS NULL ) ";
	
	String condizioneTClass =  " ( (rel.siacTClass.dataInizioValidita < :dataInput)  AND (rel.siacTClass.dataFineValidita IS NULL OR :dataInput < rel.siacTClass.dataFineValidita) AND rel.siacTClass.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRBilElemSacVisibilitaFin rel where rel.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND rel.siacTBilElem.elemId = :elemId  AND "+condizione )
	public List<SiacRBilElemSacVisibilitaFin> getValidiBySiacTBilElem(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("elemId") Integer elemId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT rel.siacTClass.classifId FROM SiacRBilElemSacVisibilitaFin rel where rel.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND rel.siacTBilElem.elemId = :elemId  AND "+condizione + " AND " + condizioneTClass)
	public List<Integer> getIdsTClassValidiBySiacTBilElem(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("elemId") Integer elemId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT rel.siacTClass.classifId FROM SiacRBilElemSacVisibilitaFin rel where "
			+ " rel.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND rel.siacTBilElem.elemId = :elemId  "
			+ " AND rel.siacTClass.classifCode = 'ALL'  "
			+ " AND rel.siacTClass.siacDClassTipo.classifTipoCode = 'VISIBILITACAPITOLOSAC'  "
			+ "AND "+ condizione + " AND " + condizioneTClass )
	public List<Integer> getIdTClassVisibilitaAllValidiBySiacTBilElem(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("elemId") Integer elemId,@Param("dataInput") Timestamp  dataInput);
	
	
	
}