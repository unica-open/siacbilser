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

import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;

public interface SiacTAttoAmmFinRepository extends JpaRepository<SiacTAttoAmmFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	public static final String RICERCA_ATTO_BY_IMPEGNO_UID = "SELECT attoAmm FROM SiacTAttoAmmFin attoAmm , SiacTMovgestFin mvg , SiacTMovgestTsFin mvgTs , SiacRMovgestTsAttoAmmFin rMovgestTsAttoAmm , SiacDAttoAmmTipoFin attoAmmTipo WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
			+ "mvg.movgestId = :impegnoUid AND mvgTs.siacTMovgest.movgestId = mvg.movgestId AND mvgTs.movgestTsId = rMovgestTsAttoAmm.siacTMovgestT.movgestTsId AND "
			+ "rMovgestTsAttoAmm.siacTAttoAmm.attoammId = attoAmm.attoammId AND attoAmm.siacDAttoAmmTipo.attoammTipoId = attoAmmTipo.attoammTipoId";
	

	String condizioneUsoInJoin = " ( (attoAmm.dataInizioValidita < :dataInput)  AND (attoAmm.dataFineValidita IS NULL OR :dataInput < attoAmm.dataFineValidita) AND attoAmm.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTAttoAmmFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND attoammAnno = :anno AND attoammNumero = :numero")
	public List<SiacTAttoAmmFin> findNumeroByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("anno") String anno,@Param("numero") Integer numero);
	
	@Query(RICERCA_ATTO_BY_IMPEGNO_UID)
	public SiacTAttoAmmFin findAttoByMovgestId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("impegnoUid")Integer impegnoUid);
	@Query("SELECT attoAmm FROM SiacTAttoAmmFin attoAmm, SiacDAttoAmmTipoFin attoTipo WHERE attoAmm.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND attoTipo.attoammTipoCode = :tipoCode " +
			" AND attoAmm.siacDAttoAmmTipo.attoammTipoId = attoTipo.attoammTipoId " +
			" AND attoAmm.attoammAnno = :anno AND attoAmm.attoammNumero = :numero" +
			" AND "+condizioneUsoInJoin)
	public List<SiacTAttoAmmFin> getValidoByNumeroAndAnnoAndTipo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("anno") String  anno,@Param("dataInput") Timestamp  dataInput,
			@Param("tipoCode") String tipoCode,@Param("numero") Integer numero);


	@Query("SELECT attoAmm FROM SiacTAttoAmmFin attoAmm, SiacDAttoAmmTipoFin attoTipo WHERE attoAmm.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND attoTipo.attoammTipoCode = :attoAmmTipoCode "
			+ " AND attoAmm.siacDAttoAmmTipo.attoammTipoId = attoTipo.attoammTipoId " +
			" AND attoAmm.attoammAnno = :anno AND attoAmm.attoammNumero = :numero" +
			" AND "+condizioneUsoInJoin)
	public List<SiacTAttoAmmFin> getValidoByNumeroAndAnnoAndTipoId(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("anno") String  anno,@Param("dataInput") Timestamp  dataInput, @Param("attoAmmTipoCode") String attoAmmTipoCode, @Param("numero") Integer numero);
	
	
	
}