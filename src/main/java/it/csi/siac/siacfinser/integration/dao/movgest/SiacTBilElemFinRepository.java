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

import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;

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


	@Query(" SELECT vinc "
            + "   FROM SiacTVincoloFin vinc, SiacRVincoloBilElemFin rcap "
            + "   WHERE rcap.siacTVincolo = vinc "
            + "   AND vinc.dataCancellazione IS NULL "
            + "   AND rcap.dataCancellazione IS NULL "
            + "   AND rcap.siacTBilElem.elemId = :elemId "
            + "   AND vinc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            + " ) ")
	public SiacTVincoloFin findVincoloSuCapitoloByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("elemId") Integer  elemId);

	

	@Query("SELECT stbe.elemCode, stbe.elemCode2, stbe.elemCode3 "
			+ " FROM SiacTBilElemFin stbe"
			+ " WHERE stbe.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND stbe.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ " 	FROM SiacRMovgestBilElemFin srm "
			+ " 	WHERE srm.dataCancellazione IS NULL "
			+ "     AND srm.siacTBilElem = stbe "
			+ "		AND srm.siacTMovgest.movgestId = :uidMovgest "
			+ " ) ")
	public List<Object[]> findCodesCapitoloByMovgestId(@Param("uidMovgest") Integer uidMovgest, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
}