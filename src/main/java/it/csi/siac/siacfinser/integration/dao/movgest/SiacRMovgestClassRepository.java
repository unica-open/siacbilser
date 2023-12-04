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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;

public interface SiacRMovgestClassRepository extends JpaRepository<SiacRMovgestClassFin, Integer>  {
	
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneInJoin = " ( (sr.dataInizioValidita < :dataInput)  AND (sr.dataFineValidita IS NULL OR :dataInput < sr.dataFineValidita) AND  sr.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMovgestClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestClassFin> findListaSiacRMovgestClass(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacRMovgestClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" siacTClass.siacDClassTipo.classifTipoId = :idClassTipo AND siacTMovgestT.movgestTsId = :idMovGestTs AND "+condizione)
	public List<SiacRMovgestClassFin> findByTipoAndEnteAndMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,
			@Param("idClassTipo") Integer idClassTipo,@Param("idMovGestTs") Integer idMovGestTs);
	
	@Query("FROM SiacRMovgestClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" siacTClass.siacDClassTipo.classifTipoId = :idClassTipo AND siacTMovgestT.movgestTsId IN (:idMovGestTsIds) AND "+condizione)
	public List<SiacRMovgestClassFin> findByTipoAndEnteAndMovgestTsList(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,
			@Param("idClassTipo") Integer idClassTipo,@Param("idMovGestTsIds") List<Integer> idMovGestTsIds);
	
	@Query("SELECT sr FROM SiacRMovgestClassFin sr, SiacTClassFin st WHERE sr.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			"  st.siacDClassTipo.classifTipoCode IN (:classifTipoCodes)  AND sr.siacTClass.classifId = st.classifId AND sr.siacTMovgestT.movgestTsId = :idMovGestTs AND " + condizioneInJoin )
	public List<SiacRMovgestClassFin> findByTipoCodesAndEnteAndMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,
			@Param("classifTipoCodes") List<String> classifTipoCodes,@Param("idMovGestTs") Integer idMovGestTs);
	
	
	@Query("FROM SiacRMovgestClassFin rmc "
			+ " WHERE rmc.siacTMovgestT.movgestTsId=:idMovGestTs "
			+ " AND rmc.siacTClass.siacDClassTipo.classifTipoCode=:classifTipoCode "
			+ " AND rmc.dataInizioValidita <= CURRENT_TIMESTAMP "
			+ " AND (rmc.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < rmc.dataFineValidita) "
			+ " AND rmc.dataCancellazione IS NULL ")
	public List<SiacRMovgestClassFin> findByClassifTipoCodeAndMovgestTs(@Param("idMovGestTs") Integer idMovGestTs, @Param("classifTipoCode") String classifTipoCodes);
	
	
	@Query("FROM SiacRMovgestClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" siacTMovgestT.movgestTsId = :idMovGestTs AND "+condizione)
	public List<SiacRMovgestClassFin> findAllValidiByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,
			@Param("idMovGestTs") Integer idMovGestTs);
	
	
	@Query("FROM SiacRMovgestClassFin rmc "
			+ " WHERE rmc.siacTMovgestT.movgestTsId=:idMovGestTs "
			+ " AND rmc.siacTClass.siacDClassTipo.classifTipoCode=:classifTipoCode "
			+ " AND rmc.dataCancellazione IS NOT NULL ")
	public List<SiacRMovgestClassFin> findStoricoByClassifTipoCodeAndMovgestTs(@Param("idMovGestTs") Integer idMovGestTs, @Param("classifTipoCode") String classifTipoCodes);
	
}
