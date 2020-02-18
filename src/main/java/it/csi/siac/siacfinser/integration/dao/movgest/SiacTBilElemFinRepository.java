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

import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;

public interface SiacTBilElemFinRepository extends JpaRepository<SiacTBilElemFin, Integer> {
	
	String condizione =  " ( (stbe.dataInizioValidita < :dataInput)  AND (stbe.dataFineValidita IS NULL OR :dataInput < stbe.dataFineValidita) AND stbe.dataCancellazione IS NULL ) ";
	
	
//	  elem_code character varying(200) NOT NULL, -- codice capitolo
//	  elem_code2 character varying(200) NOT NULL, -- codice articolo
//	  elem_code3 character varying(200) NOT NULL DEFAULT '1'::character varying, -- codice ueb
	String condizioneCodici = " stbe.elemCode = :codeCapitolo AND stbe.elemCode2 = :codeArticolo AND stbe.elemCode3 = :codeUeb ";
	
	
	
//	@Query("SELECT stbe FROM SiacTBilElemFin stbe, SiacTBilFin stbil WHERE stbe.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND stbil.siacTPeriodo.anno = :anno " +
//			" AND stbe.siacTBil.bilId = stbil.bilId " +
//			" AND " + condizioneCodici +
//			" AND "+condizione)
//	public List<SiacTBilElemFin> getValidoByCodes(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("anno") String  anno,@Param("dataInput") Timestamp  dataInput,
//			@Param("codeCapitolo") String codeCapitolo,@Param("codeArticolo") String codeArticolo,@Param("codeUeb") String codeUeb);
	
	
	
	@Query("SELECT stbe FROM SiacTBilElemFin stbe, SiacTBilFin stbil WHERE stbe.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND stbil.siacTPeriodo.anno = :anno " +
			" AND stbe.siacTBil.bilId = stbil.bilId " +
			" AND stbe.siacDBilElemTipo.elemTipoCode = :tipo"+
			" AND " + condizioneCodici +
			" AND "+condizione)
	public List<SiacTBilElemFin> getValidoByCodes(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("anno") String  anno,@Param("dataInput") Timestamp  dataInput,
			@Param("codeCapitolo") String codeCapitolo,@Param("codeArticolo") String codeArticolo,@Param("codeUeb") String codeUeb,
			@Param("tipo") String tipo);
	
	
}