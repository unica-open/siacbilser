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

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;

public interface SiacTMovgestTsDetModRepository extends JpaRepository<SiacTMovgestTsDetModFin, Integer>  {
	
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneJoin = " ( (mv.dataInizioValidita < :dataInput)  AND (mv.dataFineValidita IS NULL OR :dataInput < mv.dataFineValidita) AND  mv.dataCancellazione IS NULL ) ";

	
	public static final String COUNTER = " FROM SiacTMovgestTsDetModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId = :tsId AND " + condizione;
	
	public static final String COUNTER_BY_IDS = " FROM SiacTMovgestTsDetModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId IN :tsIds AND " + condizione;
	
	
	@Query("FROM SiacTMovgestTsDetModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacTMovgestTsDetModFin> findListaSiacTMovgestTsDetMod(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTMovgestTsDetModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestTsDet.movgestTsDetId =:idMovgestTsDet AND "+condizione)
	public List<SiacTMovgestTsDetModFin> findValidiByMovgestTsDet(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("dataInput") Timestamp  dataInput,@Param("idMovgestTsDet") Integer idMovgestTsDet);
	
	
	@Query(COUNTER)
	public List<SiacTMovgestTsDetModFin> findValidiByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("tsId") Integer tsId , @Param("dataInput")Timestamp dataImput);
	
	@Query(COUNTER_BY_IDS)
	public List<SiacTMovgestTsDetModFin> findValidiByMovgestTsIds(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("tsIds") List<Integer> tsIds , @Param("dataInput")Timestamp dataImput);
	
	/*
	public static final String MODIFICA = "SELECT mv FROM SiacTMovgestTsDetModFin mv, SiacTModificaFin mod, SiacRModificaStatoFin stato "+
	                                      "WHERE stato.siacTModifica.modId = mod.modId AND mv.siacRModificaStato.modStatoRId = stato.modStatoRId "+
			                              "AND mv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
	                                      "AND mv.movgestTsDetModId = :modificaId " +
	                                      "AND "+ condizioneJoin;
	
	@Query(MODIFICA)
	public List<SiacTMovgestTsDetModFin> findListaFromModifica(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("modificaId") Integer modificaId, @Param("dataInput")Timestamp dataImput);*/
	public static final String MODIFICA = "SELECT mv FROM SiacTMovgestTsDetModFin mv, SiacTModificaFin mod, SiacRModificaStatoFin stato "+
            "WHERE stato.siacTModifica.modId = mod.modId AND mv.siacRModificaStato.modStatoRId = stato.modStatoRId "+
            "AND mv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
            "AND mod.modId = :modificaId " +
            "AND "+ condizioneJoin;

	@Query(MODIFICA)
	public List<SiacTMovgestTsDetModFin> findListaFromModifica(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("modificaId") Integer modificaId, @Param("dataInput")Timestamp dataImput);
//	
}
