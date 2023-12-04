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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;

public interface SiacRMovgestTsRepository extends JpaRepository<SiacRMovgestTsFin,Integer>{

	final String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	
	@Query("FROM SiacRMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+
           "						  siacTMovgestTsB.movgestTsId = :impegnoTsId AND  "+condizione)
	public List<SiacRMovgestTsFin> findVincoliByImpegno(@Param("enteProprietarioId") Integer enteProprietarioId,
												  	 @Param("impegnoTsId") Integer impegnoTsId,
												  	 @Param("dataInput") Timestamp  dataInput);

	//SIAC-7779
	@Query(value=" SELECT srmt.* " + 
			" FROM siac_r_movgest_ts srmt " + 
			" JOIN siac_r_modifica_vincolo srmv ON srmv.movgest_ts_r_id = srmt.movgest_ts_r_id and srmv.modvinc_tipo_operazione = 'INSERIMENTO' " + 
			" JOIN siac_t_modifica stm ON stm.mod_id = srmv.mod_id " + 
			" JOIN siac_r_movgest_aggiudicazione srma ON stm.mod_id = srma.mod_id " + 
			" JOIN siac_t_ente_proprietario step ON srmt.ente_proprietario_id = step.ente_proprietario_id AND step.ente_proprietario_id = :enteProprietarioId " + 
			" WHERE srmt.movgest_ts_b_id = :impegnoTsId " + 
			" AND srmv.data_cancellazione is NULL " + 
			" AND ( srmv.validita_fine is NULL OR srmt.validita_fine < CURRENT_TIMESTAMP ) " + 
			" AND srmt.data_cancellazione is NULL " + 
			" AND ( srmt.validita_fine is NULL OR srmt.validita_fine < CURRENT_TIMESTAMP ) " + 
			" AND srma.data_cancellazione is NULL " + 
			" AND ( srma.validita_fine is NULL OR srma.validita_fine < CURRENT_TIMESTAMP ) ", nativeQuery = true)
	public List<SiacRMovgestTsFin> findVincoliByImpegno(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("impegnoTsId") Integer impegnoTsId);
	
}
