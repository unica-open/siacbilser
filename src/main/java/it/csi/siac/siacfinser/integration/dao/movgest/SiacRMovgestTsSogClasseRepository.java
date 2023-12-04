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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;

public interface SiacRMovgestTsSogClasseRepository extends JpaRepository<SiacRMovgestTsSogclasseFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneNonValidita = " ( (dataInizioValidita >= :dataInput)  OR (dataFineValidita IS NOT NULL AND :dataInput >= dataFineValidita) OR  dataCancellazione IS NOT NULL ) ";
	
	@Query("FROM SiacRMovgestTsSogclasseFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsSogclasseFin> findListaSiacRMovgestTsSogClasse(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacRMovgestTsSogclasseFin soggClasse WHERE soggClasse.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND soggClasse.siacTMovgestT.movgestTsId = :idMovgestTs AND " + condizione)
	 public List<SiacRMovgestTsSogclasseFin> findValidoMovGestTsSogClasseByIdMovGestAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("idMovgestTs") Integer idMovgestTs,  @Param("dataInput") Timestamp dataInput);

	@Query("FROM SiacRMovgestTsSogclasseFin soggClasse WHERE soggClasse.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND soggClasse.siacTMovgestT.movgestTsId = :idMovgestTs AND " + condizioneNonValidita + " ORDER BY dataCreazione ASC")
	 public List<SiacRMovgestTsSogclasseFin> findUltimoInvalidato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("idMovgestTs") Integer idMovgestTs,  @Param("dataInput") Timestamp dataInput);

	@Query("select count(r) FROM SiacRMovgestTsSogclasseFin r "
			+ " WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND r.siacDSoggettoClasse.soggettoClasseId = :idSoggettoClasse"
			+ " AND " + condizione)
	 public Long countValidiBySogClasseIdAndEnte(
			 @Param("enteProprietarioId") Integer enteProprietarioId, 
			 @Param("idSoggettoClasse") Integer idSoggClasse,
			 @Param("dataInput") Timestamp dataInput);
	
}


