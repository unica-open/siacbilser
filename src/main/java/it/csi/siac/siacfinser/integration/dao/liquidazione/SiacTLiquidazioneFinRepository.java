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

import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;

public interface SiacTLiquidazioneFinRepository extends JpaRepository<SiacTLiquidazioneFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput) AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	String condizioneRstato = " ( (rstato.dataInizioValidita < :dataInput) AND (rstato.dataFineValidita IS NULL OR :dataInput < rstato.dataFineValidita) AND rstato.dataCancellazione IS NULL ) ";
	String condizioneLiq = " ( (liq.dataInizioValidita < :dataInput)  AND (liq.dataFineValidita IS NULL OR :dataInput < liq.dataFineValidita) AND liq.dataCancellazione IS NULL ) ";
	String condizioneRelLiqMovGest = " ( (relLiqMovGest.dataInizioValidita < :dataInput) AND (relLiqMovGest.dataFineValidita IS NULL OR :dataInput < relLiqMovGest.dataFineValidita) AND relLiqMovGest.dataCancellazione IS NULL ) ";
	
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
	
	

	

	@Query( " SELECT rcap.siacTBilElem "
			+ " FROM SiacTMovgestFin mov, SiacRMovgestBilElemFin rcap "
			+ " WHERE rcap.siacTMovgest = mov "
			+ " AND rcap.dataCancellazione IS NULL "			
			+ " AND EXISTS ("
			+ " 	FROM SiacRLiquidazioneMovgestFin rmov, SiacTLiquidazioneFin liq " //, SiacTPeriodo per"
			+ "     WHERE rmov.siacTLiquidazione = liq "
			+ " 	AND rmov.siacTMovgestT.siacTMovgest = mov "
			+ " 	AND rmov.dataCancellazione IS NULL "
			+ "     AND liq.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "     AND liq.liqAnno = :liqAnno "
			+ "     AND liq.liqNumero = :liqNumero "
			+ "     AND liq.siacTBil.siacTPeriodo.anno = :annoBilancio "
			+ " ) "
			+ ")"
						
			)
	public List<SiacTBilElemFin> caricaCapitoloAssociatoALiquidazione(@Param("liqAnno") Integer liqAnno,@Param("liqNumero") BigDecimal liqNumero, @Param("annoBilancio") String annoBilancio, @Param("enteProprietarioId") Integer enteProprietarioId );
	
	
}