/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;

public interface SiacTMutuoVoceRepository extends JpaRepository<SiacTMutuoVoceFin, Integer> {
	String condizione = " ( (voce.dataInizioValidita < :dataInput)  AND (voce.dataFineValidita IS NULL OR :dataInput < voce.dataFineValidita) AND voce.dataCancellazione IS NULL ) ";
	String condizioneRMutuoVoceMovGest = " ( (rMutuoVoceMovGest.dataInizioValidita < :dataInput) AND (rMutuoVoceMovGest.dataFineValidita IS NULL OR :dataInput < rMutuoVoceMovGest.dataFineValidita) AND rMutuoVoceMovGest.dataCancellazione IS NULL ) ";

	
	@Query("FROM SiacTMutuoVoceFin voce WHERE voce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND voce.siacTMutuo.mutId = :mutuoId AND "+condizione)
	public List<SiacTMutuoVoceFin> findVociMutuoValideByEnteEMutuoId(@Param("enteProprietarioId") Integer enteProprietarioId,
			 			  			                              @Param("mutuoId") Integer mutuoId,
			 						                              @Param("dataInput") Timestamp dataInput);
	
	@Query("SELECT voce FROM SiacTMutuoVoceFin voce, SiacRMutuoVoceMovgestFin voceMovGest WHERE voceMovGest.siacTMovgestTs.movgestTsId = :movgestTsId AND voceMovGest.siacTMutuoVoce.mutVoceId = voce.mutVoceId AND "+ condizione)
	public List<SiacTMutuoVoceFin> findVociMutuoValideByImpegnoId(@Param("movgestTsId") Integer movgestTsId,
			                                                   @Param("dataInput") Timestamp dataInput);
	
	
	@Query("SELECT SUM(voce.mutVoceImportoAttuale) FROM SiacTMutuoVoceFin voce, SiacRMutuoVoceMovgestFin rMutuoVoceMovGest " +
			" WHERE rMutuoVoceMovGest.siacTMovgestTs.movgestTsId = :movgestTsId AND " + condizioneRMutuoVoceMovGest + " AND " +
			" rMutuoVoceMovGest.siacTMutuoVoce.mutVoceId = voce.mutVoceId AND "+ condizione)
	public BigDecimal findSommaVociMutuoValideByImpegnoId(@Param("movgestTsId") Integer movgestTsId,
			                                                        @Param("dataInput") Timestamp dataInput);
}