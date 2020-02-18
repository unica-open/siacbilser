/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTCartacont;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoVoce;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocSospensione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;

/**
 * Repository per l'entity SiacTSubdocRepository.
 *
 */
public interface SiacTSubdocRepository extends JpaRepository<SiacTSubdoc, Integer> {
	
	
	final String AND_COLLEGATI_AD_ALLEGATO_ATTO =  " AND EXISTS (FROM s.siacRElencoDocSubdocs sreds, SiacTElencoDoc e "
									+ "             WHERE sreds.dataCancellazione IS NULL "
									+ "             AND sreds.siacTElencoDoc = e "
									+ "             AND EXISTS (FROM e.siacRAttoAllegatoElencoDocs sraaed "
									+ "                         WHERE sraaed.siacTAttoAllegato.attoalId = :attoalId "
									+ "                         AND sraaed.dataCancellazione IS NULL "
									+ "                        ) "
									+ "            ) ";
	
	final String AND_NOT_COLLEGATO_A_LIQUIDAZIONE_NON_ANNULLATA = " AND NOT EXISTS (FROM s.siacRSubdocLiquidaziones rsl, SiacTLiquidazione l "
			+ "                               WHERE rsl.dataCancellazione IS NULL "
			+ "                               AND l.dataCancellazione IS NULL "
			+ "                               AND rsl.siacTLiquidazione = l "
			+ " 							  AND EXISTS ( FROM l.siacRLiquidazioneStatos rs "
			+ "     								WHERE rs.dataCancellazione IS NULL "
			+ "     								AND rs.siacDLiquidazioneStato.liqStatoCode <> 'A' "
			+ "     								AND ( rs.dataFineValidita IS NULL OR rs.dataFineValidita > CURRENT_TIMESTAMP)"
			+ "               							)"	
			+ "                               ) ";
	
	final String AND_RILEVANTE_IVA = " AND EXISTS (FROM s.siacRSubdocAttrs sa "
									+ "            WHERE sa.dataCancellazione IS NULL "
									+ "  		   AND sa.siacTAttr.attrCode = 'flagRilevanteIVA' "
									+ "            AND sa.boolean_ = 'S' "
								    + "				)";
	
	// SIAC-5592: utilizzo del multi-sospensione
	final String AND_NOT_SOSPESI = " AND NOT EXISTS ( "
			+ "     FROM s.siacTSubdocSospensiones tss "
			+ "     WHERE tss.dataCancellazione IS NULL "
			+ "     AND tss.subdocSospData IS NOT NULL "
			+ "     AND (tss.subdocSospDataRiattivazione IS NULL OR tss.subdocSospDataRiattivazione < tss.subdocSospData) "
			+ " ) ";
	
	
	final String AND_NOT_SOGGETTI_SOSPESI = " AND NOT EXISTS ( "
			+ " from "
			+ "   SiacRDocSog  rds"
			+ "  ,SiacTSubdoc tsd"
			+ "  ,SiacRSubdocAttoAmm rsam"
			+ "  ,SiacTAttoAmm taam"
			+ "  ,SiacTAttoAllegato taa"
			+ "  ,SiacRAttoAllegatoSog  raas"
			+ " where "
			+ "                tsd = s "
			+ "            AND rds.siacTDoc.docId = tsd.siacTDoc.docId"
			+ "            AND tsd.subdocId = rsam.siacTSubdoc.subdocId"			
			+ "            AND rsam.siacTAttoAmm.attoammId = taam.attoammId"			
			+ "            AND taam.attoammId= taa.siacTAttoAmm.attoammId"
			+ "            AND taa.attoalId = raas.siacTAttoAllegato.attoalId"
			+ "            AND rds.siacTSoggetto.soggettoId = raas.siacTSoggetto.soggettoId"  			
			+ "            AND rds.dataCancellazione is null"
			+ "            AND rsam.dataCancellazione is null"
			+ "            AND raas.dataCancellazione is null"			
			+ "           AND (raas.attoalSogDataRiatt IS NULL OR raas.attoalSogDataRiatt > CURRENT_TIMESTAMP)"
			+ " ) ";
	
	
	
	
	
	/**
	 * Ottiene la lista di SiacTSubdoc legati al documento avente l'id passato come parametro.
	 * 
	 * @param docId id del documento.
	 * 
	 * @return la lista di SiacTSubdoc.
	 */
	@Query(" SELECT COALESCE(COUNT(ts), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.dataCancellazione IS NULL "
			+ " AND ts.subdocId IN (:subdocIds) "
			+ " AND ts.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ")
	Long countBySubdocId(@Param("subdocIds") Collection<Integer> subdocIds, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);
	
	/**
	 * Ottiene la lista di SiacTSubdoc legati al documento avente l'id passato come parametro.
	 * 
	 * @param docId id del documento.
	 * 
	 * @return la lista di SiacTSubdoc.
	 */
	@Query("FROM SiacTSubdoc "
			+ "WHERE siacTDoc.docId = :docId "
			+ "AND dataCancellazione IS NULL " 
//			+ "AND dataInizioValidita < CURRENT_TIMESTAMP "
//			+ "AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) "
			+" ORDER BY subdocNumero "
			)
	List<SiacTSubdoc> findSiacTSubdocByDocId(@Param("docId") Integer docId);
	
	/**
	 * Ottiene la lista dei subdocumenti collegati all'allegato atto.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return la lista di siacTSubdoc
	 */
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);
	
	/**
	 * Ottiene il numero dei subdocumenti di un certo tipo associati ad un allegato atto
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return il count dei subdoc
	 */
	@Query("SELECT COALESCE(COUNT(s), 0)"
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) " //docFamTipoCodes = 'S'
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO 
			)
	Long countSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);
	
	/**
	 * Ottiene il numero dei subdocumenti associati ad un allegato atto
	 *
	 * @param attoalId the attoal id
	 * @return il count dei subdoc
	 */
	@Query("SELECT COALESCE(COUNT(s), 0)"
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			) 
	Long countSiacTSubdocByAttoalId(@Param("attoalId") Integer attoalId);
	
	/**
	 * Ottiene il numero dei subdocumenti sospesi associati ad un allegato atto
	 *
	 * @param attoalId the attoal id
	 * @return il count dei subdoc
	 */
	@Query("SELECT COALESCE(COUNT(s), 0)"
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			// Sospeso
			+ " AND EXISTS ( "
			+ "     FROM s.siacRSubdocAttrs r "
			+ "     WHERE r.siacTAttr.attrCode = 'data_sospensione' "
			+ "     AND r.testo IS NOT NULL "
			+ "     AND r.testo <> '' "
			+ "     AND r.dataCancellazione IS NULL "
			+ " ) "
			// Non riattivato
			+ " AND NOT EXISTS ( "
			+ "     FROM s.siacRSubdocAttrs r "
			+ "     WHERE r.siacTAttr.attrCode = 'data_riattivazione' "
			+ "     AND r.testo IS NOT NULL "
			+ "     AND r.testo <> '' "
			+ "     AND r.dataCancellazione IS NULL "
			+ " ) ")
	Long countSiacTSubdocSospesiByAttoalId(@Param("attoalId") Integer attoalId);

	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeInAndDocTipoCode(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes,
			@Param("docTipoCode") String docTipoCode);

	@Query(" FROM SiacRSubdocAttr r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTSubdoc.subdocId = :subdocId ")
	SiacRSubdocAttr findAttributeBySiacTSubdocAndSiacTAttr(@Param("subdocId") Integer subdocId, @Param("attrCode") String attrCode);
	
	
	/**
	 * Find siac t subdoc by attoal id and doc fam tipo code and not liquidazione and not nreg iva.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the list
	 */
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+   AND_NOT_COLLEGATO_A_LIQUIDAZIONE_NON_ANNULLATA
			+   AND_RILEVANTE_IVA
			+ " AND (s.subdocNregIva IS NULL OR s.subdocNregIva = '') "
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndNotNregIva(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);

	/**
	 * Find siac t subdoc by attoal id and doc fam tipo code and not liquidazione and importo da pagare maggiore di zero.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the list
	 */
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "  
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+   AND_NOT_COLLEGATO_A_LIQUIDAZIONE_NON_ANNULLATA
			+ 	AND_NOT_SOSPESI
			//+   AND_NOT_SOGGETTI_SOSPESI
			+ " AND (s.subdocImporto - s.subdocImportoDaDedurre) > 0 ) "
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndImportoDaPagareMaggioreDiZeroAndNonSospesi(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);

	/**
	 * count siac t subdoc by attoal id and doc fam tipo code and not liquidazione and importo da pagare maggiore di zero.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the list
	 */
	@Query(   "SELECT COALESCE(COUNT(s), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+   AND_NOT_COLLEGATO_A_LIQUIDAZIONE_NON_ANNULLATA
			//+ 	AND_NOT_SOSPESI
			+ " AND (s.subdocImporto - s.subdocImportoDaDedurre) > 0 ) "
			)
	Long countSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndImportoDaPagareMaggioreDiZero(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);



			
			
	/**
	 * Find siac t subdoc by attoal id and doc fam tipo code and not movimento.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the list
	 */
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+ " AND NOT EXISTS (FROM s.siacRSubdocMovgestTs rsmt "
			+ "                 WHERE rsmt.dataCancellazione IS NULL "
			+ "                 AND rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
			+ "                ) "
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotMovimento(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes, @Param("bilId") Integer bilId);
	

	/**
	 * Find siac t subdoc by attoal id and doc fam tipo code and not movimento.
	 *
	 * @param attoalId the attoal id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the list
	 */
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+   AND_NOT_COLLEGATO_A_LIQUIDAZIONE_NON_ANNULLATA
			+ " AND NOT EXISTS (FROM s.siacRSubdocMovgestTs rsmt "
			+ "                 WHERE rsmt.dataCancellazione IS NULL "
			+ "                 AND rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
			+ "                ) "
			)
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotMovimentoAndNotLiquidazione(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes, @Param("bilId") Integer bilId);

	/**
	 * Counts the siac t docs padre.
	 * 
	 * @param subdocId the subdoc id
	 * @param docFamTipoCode the doc fam tipo code for the padre
	 * 
	 * @return the count
	 */
	@Query("SELECT COALESCE(COUNT(r), 0) "
			+ " FROM SiacRDoc r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "	    FROM SiacTSubdoc s "
			+ "     WHERE r.siacTDocFiglio = s.siacTDoc "
			+ "     AND s.subdocId = :subdocId "
			+ " ) "
			+ " AND r.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ " AND r.siacTDocPadre.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ")
	Long countSiacTDocPadre(@Param("subdocId") Integer subdocId, @Param("relazTipoCode") String relazTipoCode, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);

	@Query("SELECT COALESCE(COUNT(r), 0) "
			+ " FROM SiacRDoc r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "	    FROM SiacTSubdoc s "
			+ "     WHERE r.siacTDocFiglio = s.siacTDoc "
			+ "     AND s.subdocId = :subdocId "
			+ " ) "
			+ " AND r.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ " AND r.siacTDocPadre.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) ")
	Long countSiacTDocNotaCredito(@Param("subdocId") Integer subdocId, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);
	
	@Query("SELECT COALESCE(COUNT(*), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.dataCancellazione IS NULL "
			+ " AND s.siacTDoc = ( SELECT s.siacTDoc  "
			+ "	    FROM SiacTSubdoc s "
			+ "     WHERE s.subdocId = :subdocId "
			+ "     AND s.dataCancellazione IS NULL "
			+ " ) ")
	Long countSiacTSubdocsOfSiacTDocBySubdocId(@Param("subdocId") Integer subdocId);
	
	@Query("SELECT r.siacTSubdoc "
			+ " FROM SiacRElencoDocSubdoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL "
			)
	List<SiacTSubdoc> findQuoteLegateAdElenco(@Param("eldocId") Integer eldocId);

	@Query("SELECT r.siacTSubdoc "
			+ " FROM SiacRSubdocAttr r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND r.boolean_ = 'S' "
			+ " AND r.siacTAttr.attrCode = '"+SiacTAttrEnum.FlagRilevanteIVA_Codice+"' "
			)
	List<SiacTSubdoc> findQuoteRilevantiIvaByIdDocumento(@Param("docId") Integer uid);
	
	@Query("SELECT r.siacTMovgestT "
			+ " FROM SiacRSubdocMovgestT r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			
			)
	SiacTMovgestT findMovgestTSByIdSubdoc(@Param("subdocId") Integer uid);
	
	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			// SIAC-5592
			+ " AND EXISTS ( "
			+ "     FROM s.siacTSubdocSospensiones tss "
			+ "     WHERE tss.dataCancellazione IS NULL "
			+ "     AND tss.subdocSospData IS NOT NULL "
			+ "     AND (tss.subdocSospDataRiattivazione IS NULL OR tss.subdocSospDataRiattivazione < tss.subdocSospData) "
			+ " ) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			)
	List<SiacTSubdoc> findSiacTSubdocSospesiByAttoalIdAndDocFamTipoCodeIn(@Param("attoalId") Integer attoalId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);
	
	@Query("SELECT r.siacTMutuoVoce "
			+ " FROM SiacRMutuoVoceSubdoc r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			
			)
	SiacTMutuoVoce findIdVoceMutuoBySubdoc(@Param("subdocId") Integer uid);

	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.dataCancellazione IS NULL "
			+ " AND s.siacTDoc.dataCancellazione IS NULL "
			+ " AND s.siacTDoc.docId = :docId "
			+ " AND NOT EXISTS ( FROM s.siacRSubdocOrdinativoTs rso, SiacTOrdinativoT ots , SiacTOrdinativo o"
			+ "				 WHERE rso.dataCancellazione IS NULL "
			+ "				 AND rso.siacTOrdinativoT = ots "
			+"				 AND ots.dataCancellazione IS NULL "	
			+ "				 AND ots.siacTOrdinativo = o "
			+ "				 AND o.dataCancellazione IS NULL "
			+ " 			AND EXISTS ( FROM o.siacROrdinativoStatos st "
			+ "						   	 WHERE st.dataCancellazione IS NULL"
			+ "							 AND st.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A')"
			+ "							)"
			+ "				)"
			
			)
	Long countQuoteDocumentoNonEmesse(@Param("docId") Integer docId);

	@Query(" SELECT COALESCE(SUM(s.subdocImporto), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId IN (:subdocIds) ")
	BigDecimal sumSubdocImportoBySubdocIds(@Param("subdocIds") Collection<Integer> subdocIds);
	
	@Query(" SELECT COALESCE(SUM(s.subdocImportoDaDedurre), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId IN (:subdocIds) ")
	BigDecimal sumSubdocImportoDaDedurreBySubdocIds(@Param("subdocIds") Collection<Integer> subdocIds);
	
	@Query(" SELECT COALESCE(s.subdocImporto, 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId = :subdocId ")
	BigDecimal findImportoBySubdocId(@Param("subdocId") Integer subdocId);
	
	@Query(" SELECT COALESCE(s.subdocImportoDaDedurre, 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId = :subdocId ")
	BigDecimal findImportoDaDedurreBySubdocId(@Param("subdocId") Integer subdocId);
	
	@Query(" SELECT COALESCE(SUM(s.subdocSplitreverseImporto), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId IN (:subdocIds) ")
	BigDecimal sumSubdocImportoSpliReverseBySubdocIds(@Param("subdocIds") Collection<Integer> subdocIds);
	
	@Query(" SELECT s.subdocSplitreverseImporto "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId = :subdocId ")
	BigDecimal findImportoSplitReverseBySubdocId(@Param("subdocId") Integer subdocId);

	
	/*
		//Il subdocIva e' collegato alla notaCreditoIva.
		//Il subdocIve e' collegato a subdoc xor doc.
		//quindi:
		//	(subdoc -> subdocIva - NcdIva)
		// 	xor
		//	(subdoc -> doc -> subdocIva -NcdIva)
	 */
	
	@Query(" SELECT rsi.siacTSubdocIvaFiglio "
			+ " FROM SiacRSubdocIva rsi "
			+ " WHERE rsi.dataCancellazione IS NULL "
			+ " AND rsi.siacDRelazTipo.relazTipoCode = '"+SiacDRelazTipoEnum.CodiceNotaCreditoIva+"' "
			+ " AND (rsi.siacTSubdocIvaPadre IN (SELECT rssi.siacTSubdocIva " //Legato alla quota...
			+ "                                 FROM SiacRSubdocSubdocIva rssi "
			+ "                                 WHERE rssi.siacTSubdoc.subdocId = :subdocId "
			+ "                                 AND rssi.dataCancellazione IS NULL "
			+ "                                )  "
//			+ "      OR rsi.siacTSubdocIvaPadre IN (SELECT rdi.siacTSubdocIvas "//...oppure legato al Documento.
//			+ "                                    FROM SiacRDocIva rdi, SiacTDoc d "
//			+ "                                    WHERE rdi.siacTDoc = d "
//			+ "                                    AND EXISTS (FROM d.siacTSubdocs s "
//			+ "                                                WHERE s.subdocId = :subdocId "
//			+ "                                                AND s.dataCancellazione IS NULL "
//			+ "                                               ) "
//			+ "                                    ) "
			+ "      OR rsi.siacTSubdocIvaPadre IN (SELECT si "
			+ "                                    FROM SiacTSubdocIva si, SiacTDoc d "//...oppure legato al Documento.
			+ "                                    WHERE si.dataCancellazione IS NULL "
			+ "                                    AND si.siacRDocIva.dataCancellazione IS NULL"
			+ "                                    AND si.siacRDocIva.siacTDoc = d  "
			+ "                                    AND EXISTS (FROM d.siacTSubdocs s "
			+ "                                                WHERE s.subdocId = :subdocId "
			+ "                                                AND s.dataCancellazione IS NULL "
			+ "                                               ) "
			+ "                                    ) "
			
			+ "     ) "
			+ "")
	SiacTSubdocIva findSiacTSubdocIvaNotaCreditoIvaAssociata(@Param("subdocId") Integer subdocId);

	
	
	
	
	
	
	/**
	 * Restituisce la quota solo se NON ha un ordinativo associato NON ANNULLATO e se NON ha il flagPagatoCEC=TRUE 
	 * 
	 * @param subdocId
	 * @return
	 */
	@Query(" FROM SiacTSubdoc s "
		+ " WHERE s.dataCancellazione IS NULL"
		+ " AND s.subdocId = :subdocId "
		+ " AND s.subdocPagatoCec <> TRUE " //NON ha il flagPagatoCEC=TRUE
		+ " AND NOT EXISTS ( " //Senza Ordinativo NON Annullato
		+ "                 FROM s.siacRSubdocOrdinativoTs srsot, SiacTOrdinativo o "
		+ "                 WHERE srsot.dataCancellazione IS NULL "
		+ "                 AND o.dataCancellazione IS NULL "
		+ "                 AND o = srsot.siacTOrdinativoT.siacTOrdinativo "
		+ "                 AND EXISTS ( "
		+ "                             FROM o.siacROrdinativoStatos ros "
		+ "                             WHERE ros.dataCancellazione IS NULL "
		+ "                             AND ( ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) "
		+ "                             AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A') "
		+ "                            ) "
		+ "                ) "
		)
	SiacTSubdoc findSiacTSubdocSenzaOrdinativoAssociatoNonAnnullatoENonPagatoCEC(@Param("subdocId") Integer subdocId);

	/**
	 * Restituisce la quota solo se NON ha un ordinativo associato NON ANNULLATO e se NON ha il flagPagatoCEC=TRUE 
	 * 
	 * @param subdocId
	 * @return
	 */
	@Query(" FROM SiacTSubdoc s "
		+ " WHERE s.dataCancellazione IS NULL"
		+ " AND s.subdocId = :subdocId "
		+ " AND EXISTS ( " //Con Ordinativo NON Annullato
		+ "                 FROM s.siacRSubdocOrdinativoTs srsot, SiacTOrdinativo o "
		+ "                 WHERE srsot.dataCancellazione IS NULL "
		+ "                 AND o.dataCancellazione IS NULL "
		+ "                 AND o = srsot.siacTOrdinativoT.siacTOrdinativo "
		+ "                 AND EXISTS ( "
		+ "                             FROM o.siacROrdinativoStatos ros "
		+ "                             WHERE ros.dataCancellazione IS NULL "
		+ "                             AND ( ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) "
		+ "                             AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A') "
		+ "                            ) "
		+ "                ) "
		)
	SiacTSubdoc findSiacTSubdocConOrdinativoValidoAssociato(@Param("subdocId") Integer subdocId);

	/**
	 * Ottiene la lista dei subdocumenti collegati all'allegato atto e con convalida manuale valorizzata.
	 *
	 * @param attoalId the attoal id
	 * @return la lista di siacTSubdoc
	 */
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocConvalidaManuale IS NOT NULL  "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND s.siacTDoc.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			)
	Long countSiacTSubdocsConvalidateByAllegatoAttoUid(@Param("attoalId") Integer attoalId);

	/**
	 * 
	 * @param subdocId
	 * @return
	 */
	@Query(" SELECT COUNT(*) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.subdocId = :subdocId "
			+ " AND EXISTS ( "
			+ "     FROM SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed, SiacRAttoAllegatoSog raas "
			// Join conditions
			+ "     WHERE reds.siacTSubdoc = ts "
			+ "     AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ "     AND raas.siacTAttoAllegato = raaed.siacTAttoAllegato "
			// Date cancellazione
			+ "     AND reds.dataCancellazione IS NULL "
			+ "     AND raaed.dataCancellazione IS NULL "
			+ "     AND raas.dataCancellazione IS NULL "
			// Condizioni di business
			+ "     AND raas.siacTSoggetto.soggettoId = :soggettoId "
			+ "     AND raas.attoalSogDataSosp IS NOT NULL "
			+"      AND raas.attoalSogCausaleSosp IS NOT NULL "
			+"      AND raas.attoalSogDataRiatt IS NULL "
			+ " ) "
			)
		Long findSiacTSubdocSoggettoAttoSospeso(@Param("subdocId") Integer subdocId,@Param("soggettoId") Integer soggettoId);

	@Query(" SELECT COUNT(*) "
			+ " FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM tsi.siacRSubdocSubdocIvas rssi "
			+ "     WHERE rssi.siacTSubdoc.subdocId = :subdocId "
			+ "     AND rssi.dataCancellazione IS NULL "
			+ " ) "
			+ " OR EXISTS ( "
			+ "     FROM SiacRDocIva rdi "
			+ "     WHERE tsi.siacRDocIva = rdi "
			+ "     AND rdi.siacTDoc.docId = :docId "
			+ " ) ")
	Long countSiacTSubdocIvaBySubdocIdAndDocId(@Param("subdocId") Integer subdocId, @Param("docId") Integer docId);

	@Query(" SELECT COALESCE(SUM(ts.subdocImporto), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND ts.dataCancellazione IS NULL " )
	BigDecimal sumSubdocImportoByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT COALESCE(SUM(ts.subdocImporto), 0), COALESCE(SUM(ts.subdocImportoDaDedurre), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND ts.dataCancellazione IS NULL " )
	Object[] sumSubdocImportoSubdocImportoDaDedurreByDocIds(@Param("docId") Integer docIds);
	
	@Query(" SELECT COALESCE(SUM(ts.subdocImporto), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM ts.siacRSubdocAttrs rsa "
			+ "     WHERE rsa.dataCancellazione IS NULL "
			+ "     AND rsa.boolean_ = 'S' "
			+ "     AND rsa.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteIVA_Codice + "' "
			+ " ) ")
	BigDecimal sumSubdocImportoRilevanteIvaByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT COALESCE(SUM(ts.subdocImporto), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ( "
			+ "     FROM ts.siacRSubdocAttrs rsa "
			+ "     WHERE rsa.dataCancellazione IS NULL "
			+ "     AND rsa.boolean_ = 'S' "
			+ "     AND rsa.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteIVA_Codice + "' "
			+ " ) ")
	BigDecimal sumSubdocImportoNonRilevanteIvaByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT COALESCE(COUNT(ts), 0) "
			+ " FROM SiacTSubdoc ts "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM ts.siacRSubdocAttrs rsa "
			+ "     WHERE rsa.dataCancellazione IS NULL "
			+ "     AND rsa.boolean_ = 'S' "
			+ "     AND rsa.siacTAttr.attrCode = '" + SiacTAttrEnum.FlagRilevanteIVA_Codice + "' "
			+ " ) ")
	Long countSubdocRilevanteIvaByDocId(@Param("docId") Integer docId);

	@Query("FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND s.dataCancellazione IS NULL "
			+   AND_COLLEGATI_AD_ALLEGATO_ATTO
			+ " AND EXISTS ( "
			+ "     FROM SiacRDocSog rds "
			+ "     WHERE rds.siacTDoc = s.siacTDoc "
			+ "     AND rds.siacTSoggetto.soggettoId = :soggettoId "
			+ "     AND rds.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY s.siacTDoc.siacDDocTipo.docTipoCode, s.siacTDoc.docAnno, s.siacTDoc.docNumero, s.subdocNumero ")
	List<SiacTSubdoc> findSiacTSubdocByAttoalIdAndSoggettoIdAndDocFamTipoCodeIn(@Param("attoalId") Integer attoalId, @Param("soggettoId") Integer soggettoId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);

	@Query(" SELECT rcds.siacTCartacontDet.siacTCartacont"
			+ " FROM SiacRCartacontDetSubdoc rcds "
			+ " WHERE rcds.dataCancellazione IS NULL "
			+ " AND rcds.siacTCartacontDet.dataCancellazione IS NULL "
			+ " AND rcds.siacTCartacontDet.siacTCartacont.dataCancellazione IS NULL "
			+ " AND (rcds.dataFineValidita IS NULL OR rcds.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND (rcds.siacTCartacontDet.dataFineValidita IS NULL OR rcds.siacTCartacontDet.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND (rcds.siacTCartacontDet.siacTCartacont.dataFineValidita IS NULL OR rcds.siacTCartacontDet.siacTCartacont.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND rcds.siacTSubdoc.subdocId = :subdocId ")
	List<SiacTCartacont> findSiacTCartacontBySubdocId(@Param("subdocId") Integer subdocId);

	@Query(" SELECT COALESCE(SUM(ts.subdocSplitreverseImporto), 0) " +
			" FROM SiacTSubdoc ts " +
			" WHERE ts.dataCancellazione IS NULL " +
			" AND ts.siacTDoc.docId = :docId " +
			" AND EXISTS ( " +
			"      FROM ts.siacRSubdocSplitreverseIvaTipos r " +
			"      WHERE r.dataCancellazione IS NULL " +
			"      AND r.siacDSplitreverseIvaTipo.srivaTipoCode = :srivaTipoCode " +
			" ) ")
	BigDecimal sommaImportiSplitreverseImporto(@Param("docId") Integer docId, @Param("srivaTipoCode") String code);
	
	@Query("SELECT COALESCE(COUNT(srsot), 0)"
			+ " FROM SiacRSubdocOrdinativoT srsot, SiacTOrdinativo o "
			+ " WHERE o = srsot.siacTOrdinativoT.siacTOrdinativo "
			+ " AND srsot.dataCancellazione IS NULL "
			+ " AND (srsot.dataFineValidita IS NULL OR srsot.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND o.dataCancellazione IS NULL "
			+ " AND srsot.siacTSubdoc.subdocId = :subdocId"
			+ " AND EXISTS ( "
			+ "     FROM o.siacROrdinativoStatos ros "
			+ "     WHERE ros.dataCancellazione IS NULL "
			+ "     AND ( ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A') "
			+ " ) "
	)
	Long countSiacROrdinativoBySubdocId(@Param("subdocId") Integer subdocId);
	
	
	@Query("SELECT COALESCE(COUNT(s), 0)"
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.dataCancellazione IS NULL "
			// Collegamento atto allegato
			+ " AND EXISTS ( "
			+ "     FROM SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
			+ "     WHERE reds.siacTElencoDoc = raaed.siacTElencoDoc "
			+ "     AND reds.siacTSubdoc = s "
			+ "     AND reds.dataCancellazione IS NULL "
			+ "     AND raaed.dataCancellazione IS NULL "
			+ "     AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " ) "
			+ " AND ( "
			// Collegamento ordinativo, entrata
			+ "     EXISTS ( "
			+ "         FROM s.siacRSubdocOrdinativoTs rsot, SiacROrdinativoStato ros "
			+ "         WHERE ros.siacTOrdinativo = rsot.siacTOrdinativoT.siacTOrdinativo "
			+ "         AND rsot.dataCancellazione IS NULL "
			+ "         AND ros.dataCancellazione IS NULL "
			+ "         AND (rsot.dataFineValidita IS NULL OR rsot.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "         AND (ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "         AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A') "
			+ "     ) "
			// Collegamento ordinativo, spesa (via liquidazione)
			+ "     OR EXISTS ( "
			+ "         FROM s.siacRSubdocLiquidaziones rsl, SiacRLiquidazioneOrd rlo, SiacROrdinativoStato ros "
			+ "         WHERE rsl.siacTLiquidazione = rlo.siacTLiquidazione "
			+ "         AND ros.siacTOrdinativo = rlo.siacTOrdinativoT.siacTOrdinativo "
			+ "         AND rsl.dataCancellazione IS NULL "
			+ "         AND rlo.dataCancellazione IS NULL "
			+ "         AND ros.dataCancellazione IS NULL "
			+ "         AND (rsl.dataFineValidita IS NULL OR rlo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "         AND (rlo.dataFineValidita IS NULL OR rlo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "         AND (ros.dataFineValidita IS NULL OR ros.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "         AND ros.siacDOrdinativoStato.ordinativoStatoCode NOT IN ('A') "
			+ "     ) "
			+ " ) ")
	Long countSiacTSubdocByAttoalIdWithSiacTOrdinativo(@Param("attoalId") Integer attoalId);
	
	@Query(" FROM SiacTSubdocSospensione tss "
			+ " WHERE tss.siacTSubdoc.subdocId = :subdocId "
			+ " AND tss.dataCancellazione IS NULL ")
	List<SiacTSubdocSospensione> findSiacTSubdocSospensionesBySubdocId(@Param("subdocId") Integer subdocId);
	
	
	@Query(" SELECT ts.subdocId, ts.subdocDataScadenza, rsa.testo "
			+ " FROM SiacTSubdoc ts, SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
			+ " LEFT JOIN ts.siacRSubdocAttrs rsa "
			+ " WHERE reds.siacTSubdoc = ts "
			+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ " AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND ts.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode "
			+ " AND rsa.siacTAttr.attrCode = :attrCode "
			+ " AND ts.subdocDataScadenza IS NOT NULL "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND reds.dataCancellazione IS NULL "
			+ " AND raaed.dataCancellazione IS NULL "
			+ " AND rsa.dataCancellazione IS NULL "
			+ " AND reds.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND raaed.siacTAttoAllegato.dataCancellazione IS NULL ")
	List<Object[]> findSubdocDataScadenzaByAllegatoAttoAndDocTipoCode(@Param("attoalId") Integer attoalId, @Param("docTipoCode") String docTipoCode, @Param("attrCode") String attrCode);
	
	@Query(" SELECT r.boolean_ " //valori contenuti: 'S', 'N'
			+ " FROM SiacRMovgestTsAttr r , SiacRSubdocMovgestT rs "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND rs.dataCancellazione IS NULL "
			+ " AND rs.siacTSubdoc.subdocId = :subdocId "
			+ " AND (r.siacTMovgestT = rs.siacTMovgestT or r.siacTMovgestT = rs.siacTMovgestT.siacTMovgestIdPadre) "
			+ " AND r.dataCancellazione IS NULL ")
	List<String> findBooleanAttrValuesMovgestCollegato(@Param("subdocId") Integer subdocId, @Param("attrCode")String attrCode);

	@Query( " SELECT ts  "
			+ " FROM SiacTSubdoc tsub, SiacTSoggetto ts   "
			+ " WHERE tsub.dataCancellazione IS NULL "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND tsub.subdocId = :subdocId "
			+ " AND ts.dataFineValiditaDurc IS NOT NULL "
			+ " AND ts.dataFineValiditaDurc < CURRENT_TIMESTAMP "
			+ " AND ( EXISTS( "
			+ " 	FROM SiacRDocSog rds "
			+ " 	WHERE rds.dataCancellazione IS NULL "
			+ " 	AND rds.siacTSoggetto = ts "
			+ " 	AND rds.siacTDoc = tsub.siacTDoc "
			+ " )OR EXISTS( "
			+ " 	FROM SiacRSubdocModpag rsm "
			+ " 	WHERE rsm.dataCancellazione IS NULL "
			+ " 	AND rsm.siacRSoggrelModpag.siacRSoggettoRelaz.siacTSoggetto2 = ts "
			+ " 	AND rsm.siacTSubdoc = tsub "
			+ " )) "
			 )
	List<SiacTSoggetto> findSoggettiDurcScadutoBySubdocIds(@Param("subdocId") List<Integer> subdocIds);
	
	@Query(value="" 
			+ " with soggetto_cessione as ("
			+ "		select sog_cessione.soggetto_id, sog_cessione.soggetto_fine_validita_durc as durc, ts.subdoc_id as id "
			+ "		from siac_t_subdoc ts  "
			+ "		cross join siac_t_soggetto sog_cessione "
			+ "		where (ts.data_cancellazione is null) "
			+ "		and (sog_cessione.data_cancellazione is null) "
			+ "		and ts.subdoc_id in (:subodcIds) "
			+ "		and (sog_cessione.data_cancellazione is null)  "
			+ "		and (exists ( "
			+ "			select 1 "
			+ "			from siac_r_subdoc_modpag rsm   "
			+ "			join siac_r_soggrel_modpag rsmdp on (rsmdp.modpag_id=rsm.modpag_id ) "
			+ "			join siac_r_soggetto_relaz rsr on (rsmdp.soggetto_relaz_id=rsr.soggetto_relaz_id) "
			+ "			where (rsm.data_cancellazione is null) "
			+ "			and rsr.soggetto_id_a=sog_cessione.soggetto_id "
			+ "			and rsm.subdoc_id = ts.subdoc_id "
			+ "		))  "
			+ "	), soggetto_documento as (  "
			+ "		select sog_doc.soggetto_fine_validita_durc as durc, ts_doc.subdoc_id as id          "
			+ "		from siac_t_subdoc ts_doc "
			+ "		cross join siac_t_soggetto sog_doc "
			+ "		where (sog_doc.data_cancellazione is null) "
			+ "		and ts_doc.subdoc_id in (:subodcIds) "
			+ "		and (ts_doc.data_cancellazione is null)  "
			+ "		and (sog_doc.data_cancellazione is null)    "
			+ "		and (sog_doc.soggetto_fine_validita_durc is not null) "
			+ "		and exists (  "
			+ "			select 1  "
			+ "			from siac_r_doc_sog rdoc "
			+ "			where ts_doc.doc_id = rdoc.doc_id "
			+ "	        and rdoc.soggetto_id = sog_doc.soggetto_id  "
			+ "			AND rdoc.data_cancellazione is null "
			+ "		) "
			+ "	) "
			+ "	select "
			+ "	    coalesce(soggetto_cessione.durc, soggetto_documento.durc) as data_durc "
			+ "	from soggetto_documento  "
			+ "	full outer join soggetto_cessione ON (soggetto_documento.id = soggetto_cessione.id) "
			// devo considerare il caso in cui il soggetto cessione non sia scaduto ma il soggetto si
			+ " where (soggetto_cessione.soggetto_id is null or soggetto_cessione.durc is not null )"
			+ " order by data_durc "
			+ " limit 1 ",
			nativeQuery = true)
	List<Date> getDataFineValiditaDurcBySubdocIds(@Param("subodcIds")List<Integer> subodcIds);
	
	@Query(value="" 
			+ " with soggetto_cessione as ( "
			+ "		select sog_cessione.soggetto_id, sog_cessione.soggetto_fine_validita_durc as durc, ts.subdoc_id as id, "
			+ "	    sog_cessione.soggetto_code as soggetto_code "
			+ "		from siac_t_subdoc ts "
			+ "		cross join siac_t_soggetto sog_cessione "
			+ "		where ts.data_cancellazione is null "
			+ "		and sog_cessione.data_cancellazione is null "
			+ "		and ts.subdoc_id in (:subodcIds) "
			+ "		and sog_cessione.data_cancellazione is null "
			+ "		and exists ( "
			+ "			select 1  "
			+ "			from siac_r_subdoc_modpag rsm  "
			+ "			join siac_r_soggrel_modpag rsmdp on (rsmdp.modpag_id=rsm.modpag_id ) "
			+ "			join siac_r_soggetto_relaz rsr on (rsmdp.soggetto_relaz_id=rsr.soggetto_relaz_id) "
			+ "			where rsm.data_cancellazione is null "
			+ "			and rsr.soggetto_id_a=sog_cessione.soggetto_id "
			+ "			and rsm.subdoc_id = ts.subdoc_id "
			+ "		) "
			+ "	), soggetto_documento as ( "
			+ "		select sog_doc.soggetto_fine_validita_durc as durc, ts_doc.subdoc_id as id ,"
			+ "	    sog_doc.soggetto_code as soggetto_code "
			+ "		from siac_t_subdoc ts_doc "
			+ "		cross join siac_t_soggetto sog_doc "
			+ "		where sog_doc.data_cancellazione is null "
			+ "		and ts_doc.subdoc_id in (:subodcIds) "
			+ "		and ts_doc.data_cancellazione is null "
			+ "		and sog_doc.data_cancellazione is null "
			+ "		and sog_doc.soggetto_fine_validita_durc is not null "
			+ "		and exists ( "
			+ "			select 1 "
			+ "			from siac_r_doc_sog rdoc "
			+ "			where ts_doc.doc_id = rdoc.doc_id "
			+ "	        and rdoc.soggetto_id = sog_doc.soggetto_id "
			+ "			AND rdoc.data_cancellazione is null "
			+ "		)"
			+ "	) "
			+ "	select" 
			+ "	    coalesce(soggetto_cessione.durc, soggetto_documento.durc) as data_durc, "
			+ "	    coalesce(soggetto_cessione.soggetto_code, soggetto_documento.soggetto_code) as code_durc "
			+ "	from soggetto_documento "
			+ "	full outer join soggetto_cessione ON (soggetto_documento.id = soggetto_cessione.id)  "
			+ "	where (soggetto_cessione.soggetto_id is null or soggetto_cessione.durc is not null) "
			+ "	order by data_durc",
			nativeQuery = true)
	List<Object[]> getDataFineValiditaDurcAndSoggettoCodeBySubdocIds(@Param("subodcIds")List<Integer> subodcIds);
}
