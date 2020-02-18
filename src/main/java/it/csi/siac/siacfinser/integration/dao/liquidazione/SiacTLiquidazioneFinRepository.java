/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;

public interface SiacTLiquidazioneFinRepository extends JpaRepository<SiacTLiquidazioneFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput) AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	String condizioneRstato = " ( (rstato.dataInizioValidita < :dataInput) AND (rstato.dataFineValidita IS NULL OR :dataInput < rstato.dataFineValidita) AND rstato.dataCancellazione IS NULL ) ";
	String condizioneLiq = " ( (liq.dataInizioValidita < :dataInput)  AND (liq.dataFineValidita IS NULL OR :dataInput < liq.dataFineValidita) AND liq.dataCancellazione IS NULL ) ";
	String condizioneRelLiqMovGest = " ( (relLiqMovGest.dataInizioValidita < :dataInput) AND (relLiqMovGest.dataFineValidita IS NULL OR :dataInput < relLiqMovGest.dataFineValidita) AND relLiqMovGest.dataCancellazione IS NULL ) ";
	String condizioneRelMutuoVoceLiq = " ( (relMutuoVoceLiq.dataInizioValidita < :dataInput) AND (relMutuoVoceLiq.dataFineValidita IS NULL OR :dataInput < relMutuoVoceLiq.dataFineValidita) AND relMutuoVoceLiq.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTLiquidazioneFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND liqId = :idLiq AND " + condizione)
	public SiacTLiquidazioneFin findLiquidazioneValidaByEnteAndId(@Param("enteProprietarioId") Integer enteProprietarioId,
			 				   		                           @Param("idLiq") Integer idLiq,
			 						                           @Param("dataInput") Timestamp dataInput);
	
	@Query("FROM SiacTLiquidazioneFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTModpag.modpagId = :modpagId AND " + condizione)
	public List<SiacTLiquidazioneFin> findLiquidazioneValidaByEnteAndMdpId(@Param("enteProprietarioId") Integer enteProprietarioId,
			 				   		                           @Param("modpagId") Integer modpagId,
			 						                           @Param("dataInput") Timestamp dataInput);
	
	@Query("FROM SiacTLiquidazioneFin liq WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND liq.liqAnno = :anno AND liq.liqNumero = :numero AND " + condizioneLiq)
	public SiacTLiquidazioneFin findLiquidazioneByAnnoNumero(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("anno") Integer anno,
			 						 @Param("numero") BigDecimal numero,
			 						  @Param("dataInput") Timestamp dataInput);
	
	@Query("FROM SiacTLiquidazioneFin liq WHERE "
			+ "liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
			+ "liq.liqAnno = :anno AND "
			+ "liq.liqNumero = :numero AND " 
			+ "liq.siacTBil.siacTPeriodo.anno = :annoBilancio AND " 
			+ condizioneLiq)
	public SiacTLiquidazioneFin findLiquidazioneByAnnoNumeroAnnoBilancio(@Param("enteProprietarioId") Integer enteProprietarioId,
										@Param("anno") Integer anno,
										@Param("numero") BigDecimal numero,
										@Param("annoBilancio") String annoBilancio,
										@Param("dataInput") Timestamp dataInput);
	
	@Query("FROM SiacTLiquidazioneFin liq WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND liq.liqAnno = :anno "
			+ "AND liq.liqNumero = :numero AND liq.siacTBil.bilId= :bilancioId AND " 
			+ condizioneLiq)
	public SiacTLiquidazioneFin findLiquidazioneByAnnoNumeroBilancio(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("anno") Integer anno,
			 						  @Param("numero") BigDecimal numero,
			 						  @Param("dataInput") Timestamp dataInput,
			 						  @Param("bilancioId") Integer bilancioId);
	
	
	@Query("FROM SiacTLiquidazioneFin liq WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND liq.liqAnno = :anno "
			+ "	AND liq.liqNumero = :numero AND liq.siacTBil.bilId= :bilancioId" 
			+ " AND liq.liqConvalidaManuale = :liqConvalidaManuale AND "
			+ condizioneLiq)
	public SiacTLiquidazioneFin findLiquidazioneByAnnoNumeroBilancioLiqConvalida(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("anno") Integer anno,
			 						  @Param("numero") BigDecimal numero,
			 						  @Param("dataInput") Timestamp dataInput,
			 						  @Param("bilancioId") Integer bilancioId,
									  @Param("liqConvalidaManuale") String liqConvalidaManuale);
	
	
	@Query("SELECT SUM(liq.liqImporto) FROM SiacTLiquidazioneFin liq, SiacRLiquidazioneStatoFin rstato, SiacRLiquidazioneMovgestFin relLiqMovGest "+
		   " WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizioneLiq + " AND " + 
		   "       liq.liqId = rstato.siacTLiquidazione.liqId AND " + condizioneRstato + " AND " +
           "       rstato.siacDLiquidazioneStato.liqStatoCode != :statoLiquidazioneAnnullata AND " +
		   "       liq.liqId = relLiqMovGest.siacTLiquidazione.liqId AND " + condizioneRelLiqMovGest + " AND " +
	       "       relLiqMovGest.siacTMovgestT.movgestTsId = :idMovGestTs ") 
	public BigDecimal findDisponibilitaLiquidare(@Param("enteProprietarioId") Integer enteProprietarioId, 
			                                     @Param("idMovGestTs") Integer idMovGestTs, @Param("statoLiquidazioneAnnullata") String statoLiquidazioneAnnullata, @Param("dataInput") Timestamp dataInput);
	
	
	@Query("SELECT SUM(liq.liqImporto) FROM SiacTLiquidazioneFin liq, SiacRLiquidazioneStatoFin rstato, SiacRMutuoVoceLiquidazioneFin relMutuoVoceLiq "+
			   " WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizioneLiq + " AND " +
			   "       liq.liqId = rstato.siacTLiquidazione.liqId AND " + condizioneRstato + " AND " +
			   "       rstato.dataFineValidita IS NULL AND " + 
	           "       rstato.siacDLiquidazioneStato.liqStatoCode = :statoLiquidazione AND " +
			   "       liq.liqId = relMutuoVoceLiq.siacTLiquidazione.liqId AND " + condizioneRelMutuoVoceLiq + " AND " +
		       "       relMutuoVoceLiq.siacTMutuoVoce.mutVoceId = :idVoceMutuo ") 

		public BigDecimal findDisponibilitaLiquidareVoceMutuo(@Param("enteProprietarioId") Integer enteProprietarioId, 
				                                              @Param("idVoceMutuo") Integer idVoceMutuo,
				                                              @Param("statoLiquidazione") String statoLiquidazione,
				                                              @Param("dataInput") Timestamp dataInput);
	
	
	@Query("SELECT DISTINCT relMutuoVoceLiq FROM SiacTLiquidazioneFin liq, SiacRLiquidazioneStatoFin rstato, SiacRMutuoVoceLiquidazioneFin relMutuoVoceLiq "+
			   " WHERE liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizioneLiq + " AND " +
			   "       liq.liqId = rstato.siacTLiquidazione.liqId AND " + condizioneRstato + " AND " +
			   "       rstato.dataFineValidita IS NULL AND " + 
	           "       rstato.siacDLiquidazioneStato.liqStatoCode = :statoLiquidazione AND " +
			   "       liq.liqId = relMutuoVoceLiq.siacTLiquidazione.liqId AND " + condizioneRelMutuoVoceLiq + " AND " +
		       "       relMutuoVoceLiq.siacTMutuoVoce.mutVoceId IN :idVoceMutuoList ") 
		public List<SiacRMutuoVoceLiquidazioneFin> findSiacRMutuoVoceLiquidazioneFinByVociMutuo(@Param("enteProprietarioId") Integer enteProprietarioId, 
															  @Param("idVoceMutuoList") List<Integer> idVoceMutuoList,
				                                              @Param("statoLiquidazione") String statoLiquidazione,
				                                              @Param("dataInput") Timestamp dataInput);
	
	
}