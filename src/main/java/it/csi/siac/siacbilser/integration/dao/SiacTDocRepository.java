/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDDocFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;

/**
 * Repository per l'entity SiacTDoc.
 *
 */
public interface SiacTDocRepository extends JpaRepository<SiacTDoc, Integer> {

	String FROM_NOTE_CREDITO_COLLEGATE_AL_DOC = 
			  " FROM SiacRDoc rd, SiacTDoc d "
			//Il docId che sto cercando e' Padre
			+ " WHERE rd.siacTDocPadre.docId = :docId "
			//il "d" che restituisco e' figlio.
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			//Sarebbe TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice()=NCD
			+ " AND d.siacDDocTipo.siacDDocGruppo.docGruppoTipoCode = 'NCD' "
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			//esclude gli annullati
			+ " AND NOT EXISTS(FROM d.siacRDocStatos rs "
			+ "                WHERE rs.dataCancellazione IS NULL "
			+ "                AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ "               ) "
			//Escludo le noteCredito individuate che sono "multiple" (ovvero NON collegate ad uno ed un solo doc)
			+ " AND 1 = ( SELECT COUNT(rdcoll) "
			+ "            FROM SiacRDoc rdcoll "
			//il "d" che restituisco e' figlio di quanti doc.
			+ "            WHERE rdcoll.siacTDocFiglio.docId = d "
			+ "            AND rdcoll.dataCancellazione IS NULL "
			+ "            AND rdcoll.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "' "
			+ "         )";
	
	@Query(" FROM SiacTDoc d  "
			+ " WHERE d.docId = :docId "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM d.siacTSubdocs sd "
			+ "            WHERE sd.dataCancellazione IS NULL "
			+ "            AND EXISTS (FROM sd.siacRElencoDocSubdocs se "
			+ "                        WHERE se.dataCancellazione IS NULL "
			+ "             		   AND EXISTS (FROM se.siacTElencoDoc.siacRAttoAllegatoElencoDocs ae "
			+ " 								   WHERE ae.dataCancellazione IS NULL "	
			+ " 								   AND EXISTS (FROM ae.siacTAttoAllegato aa"
			+ " 												WHERE aa.dataCancellazione IS NULL "
			+ "                          					   )"
			+ "                          		   )"
			+ "                         )"
			+ "              )")
	SiacTDoc findDocCollegatoAdAllegatoAtto(@Param("docId") Integer uidDoc);

	
	@Query(" FROM SiacRDocStato r where r.siacTDoc.docId = :docId ")
	List<SiacRDocStato> findSiacRDocStatos(@Param("docId") Integer docId);
	
	@Query(" SELECT dt.siacDDocFamTipo "
			+ "	FROM SiacDDocTipo dt"
			+ " WHERE dt.docTipoId = :docTipoId"
			+ "	AND dt.dataCancellazione IS NULL"
			+ ") ")
	SiacDDocFamTipo findFamTipoByDocTipo(@Param("docTipoId") Integer docTipoId);

	@Query( " SELECT r " +
			" FROM SiacRDocOnere r, SiacTDoc d "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTDoc.dataCancellazione IS NULL "
			+ " AND r.siacTDoc = d "
			+ " AND EXISTS ( "
			+ "     FROM d.siacTSubdocs sd, SiacTSubdoc s "
			+ "     WHERE sd.dataCancellazione IS NULL "
			+ "     AND sd = s "
			+ "     AND EXISTS ( "
			+ "         FROM s.siacRSubdocLiquidaziones rsl, SiacTLiquidazione l "
			+ "         WHERE rsl.dataCancellazione IS NULL "
			+ "         AND rsl.siacTLiquidazione = l "
			+ "         AND EXISTS ( "
			+ "             FROM l.siacRLiquidazioneOrds rlo "
			+ "             WHERE rlo.dataCancellazione IS NULL "
			+ "             AND rlo.siacTOrdinativoT.ordTsId = :ordTsId "
			+ "         ) "
			+ "     ) "
			+ " ) "
			)
	List<SiacRDocOnere> /*Long*/ findSiacRDocOnereByOrdTsIs(@Param("ordTsId") Integer ordTsId);
	
	

	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL")
	Long countSiacTSubdocByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT COALESCE(COUNT(DISTINCT ts), 0) "
			+ " FROM SiacTSubdoc ts, SiacRSubdocMovgestT rsmt "
			+ " WHERE ts.siacTDoc.docId = :docId "
			+ " AND rsmt.siacTSubdoc = ts "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL ")
	Long countSiacTSubdocByDocIdHavingMovgestTs(@Param("docId") Integer docId);
	
	
	@Query(" SELECT s.siacTDoc "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.subdocId = :subdocId ")
	SiacTDoc findBySubdocId(@Param("subdocId") Integer subdocId);
	
	
	@Query(" SELECT COALESCE(SUM(s.subdocImporto), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL")
	BigDecimal sumSubdocImportoByDocId(@Param("docId") Integer docId);
	
	
	@Query(" SELECT COALESCE(SUM(s.subdocImporto - s.subdocImportoDaDedurre), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL")
	BigDecimal sumSubdocImportoMenoImportoDaDedurreByDocId(@Param("docId") Integer docId);
	
	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			)
	Long countSubdocsEscludendoQuotaAZeroByDocId(@Param("docId") Integer docId); 

	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS (FROM s.siacRSubdocOrdinativoTs rsot, SiacTOrdinativo o "
			+ "                 WHERE rsot.dataCancellazione IS NULL "
			+ "                 AND rsot.siacTOrdinativoT.siacTOrdinativo = o "
			+ "                 AND o.dataCancellazione IS NULL "
			+ "                 AND EXISTS (FROM o.siacROrdinativoStatos ros "
			+ "                             WHERE ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			//+ "                             AND ros.dataCancellazione IS NULL "
			+ "                             AND ros.dataFineValidita IS NULL "
			+ "                            ) "
			+ "                ) "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			+ "") 
	Long countSubdocsSenzaOrdinativoByDocId(@Param("docId") Integer docId); 
	

	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS (FROM s.siacRSubdocLiquidaziones rsl, SiacTLiquidazione l "
			+ "                 WHERE rsl.dataCancellazione IS NULL "
			+ "                 AND rsl.siacTLiquidazione = l "
			+ "                 AND l.dataCancellazione IS NULL "
			+ "                 AND EXISTS (FROM l.siacRLiquidazioneStatos rls "
			+ "                             WHERE rls.siacDLiquidazioneStato.liqStatoCode <> 'A' "
			//+ "                             AND rls.dataCancellazione IS NULL "
			+ "                             AND rls.dataFineValidita IS NULL "
			+ "                            ) "
			+ "                ) "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			+ "") 
	Long countSubdocsSenzaLiquidazioneEscludendoQuotaAZeroByDocId(@Param("docId") Integer docId); 
	
	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS (FROM s.siacRSubdocAttoAmms rsa, SiacTAttoAmm a "
			+ "                 WHERE rsa.dataCancellazione IS NULL "
			+ "                 AND rsa.siacTAttoAmm = a "
			+ "                 AND a.dataCancellazione IS NULL "
			+ "                 AND a.siacDAttoAmmTipo.attoammTipoCode = 'DTS' "
			+ "                ) "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			+ "") 
	Long countSubdocsSenzaDeterminaDiIncassoEscludendoQuotaAZeroByDocId(@Param("docId") Integer docId); 
	
	
	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS (FROM s.siacRSubdocMovgestTs rsmt, SiacTMovgestT m "
			+ "                 WHERE rsmt.dataCancellazione IS NULL "
			+ "                 AND rsmt.siacTMovgestT = m "
			+ "                 AND m.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'I' "
			+ "                 AND m.dataCancellazione IS NULL "
//			+ "                 AND EXISTS (FROM m.siacRMovgestTsStatos rms "
//			+ "                             WHERE rms.siacDMovgestStato.movgestStatoCode <> 'A' "
//			+ "                             AND rms.dataCancellazione IS NULL "
//			+ "                            ) "
			+ " "
			+ "                ) "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			+ "") 
	Long countSubdocsSenzaImpegnoOSubImpegnoEscludendoQuotaAZeroByDocId(@Param("docId") Integer docId);
	
	
	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS (FROM s.siacRSubdocMovgestTs rsmt, SiacTMovgestT m "
			+ "                 WHERE rsmt.dataCancellazione IS NULL "
			+ "                 AND rsmt.siacTMovgestT = m "
			+ "                 AND m.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' "
			+ "                 AND m.dataCancellazione IS NULL "
//			+ "                 AND EXISTS (FROM m.siacRMovgestTsStatos rms "
//			+ "                             WHERE rms.siacDMovgestStato.movgestStatoCode <> 'A' "
//			+ "                             AND rms.dataCancellazione IS NULL "
//			+ "                            ) "
			+ " "
			+ "                ) "
			//esclude la QUOTA A ZERO!!!//return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
			+ " AND s.subdocImporto <> s.subdocImportoDaDedurre "
			+ "") 
	Long countSubdocsSenzaAccertamentoOSubAccertamentoEscludendoQuotaAZeroByDocId(@Param("docId") Integer docId);


	@Query(" SELECT COALESCE(SUM(s.subdocSplitreverseImporto), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM s.siacRSubdocSplitreverseIvaTipos rsit "
			+ "             WHERE rsit.dataCancellazione IS NULL "
			+ "             AND rsit.siacDSplitreverseIvaTipo.srivaTipoCode = :srivaTipoCode" //RC per reverse change
			+ "            ) " 
			)
	BigDecimal sumImportoOneriSplitReverseByDocIdAndTipoSplitreverse(@Param("docId") Integer docId, @Param("srivaTipoCode") String srivaTipoCode);
	
	@Query(" SELECT rssit.siacDSplitreverseIvaTipo.srivaTipoCode, COALESCE(SUM(rssit.siacTSubdoc.subdocSplitreverseImporto), 0) "
			+ " FROM SiacRSubdocSplitreverseIvaTipo rssit "
			+ " WHERE rssit.dataCancellazione IS NULL "
			+ " AND rssit.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND rssit.siacTSubdoc.siacTDoc.docId = :docId "
			+ " GROUP BY rssit.siacDSplitreverseIvaTipo.srivaTipoCode ")
	List<Object[]> sumImportoOneriSplitReverseSubdocByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT rosit.siacDSplitreverseIvaTipo.srivaTipoCode, COALESCE(SUM(rdo.importoCaricoSoggetto), 0) "
			+ " FROM SiacRDocOnere rdo, SiacROnereSplitreverseIvaTipo rosit "
			+ " WHERE rdo.siacDOnere = rosit.siacDOnere "
			+ " AND rdo.dataCancellazione IS NULL "
			+ " AND rosit.dataCancellazione IS NULL "
			+ " AND rdo.siacTDoc.docId = :docId "
			+ " GROUP BY rosit.siacDSplitreverseIvaTipo.srivaTipoCode ")
	List<Object[]> sumImportoOneriSplitReverseRitenuteByDocId(@Param("docId") Integer docId);
	
	
	/**
	 * Totale importo delle note credito (documenti con relazione di tipo Nota Credito) associate ad un documento.
	 * 
	 * @param docId
	 * @return l'importo calcolato
	 */
	@Query(" SELECT COALESCE(SUM(d.docImporto), 0) "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocPadre.docId = :docId " 
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			//+ " AND d.siacDDocTipo.siacDDocGruppo.docGruppoTipoCode = 'NCD' " //Sarebbe TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice()=NCD
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND NOT EXISTS(FROM d.siacRDocStatos rs " //esclude gli annullati
			+ "                WHERE rs.dataCancellazione IS NULL "
			+ "                AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ "               ) "
			)
	BigDecimal sumSubdocNoteCreditoColegateByDocId(@Param("docId") Integer docId);
	
	/**
	 * Totale importo delle note credito (documenti con relazione di tipo Nota Credito) associate ad un documento.
	 * 
	 * @param docId
	 * @return l'importo calcolato
	 */
	@Query(" SELECT COALESCE(SUM(rd.docImportoDaDedurre), 0) "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocPadre.docId = :docId " 
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			//+ " AND d.siacDDocTipo.siacDDocGruppo.docGruppoTipoCode = 'NCD' " //Sarebbe TipoGruppoDocumento.NOTA_DI_CREDITO.getCodice()=NCD
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND NOT EXISTS(FROM d.siacRDocStatos rs " //esclude gli annullati
			+ "                WHERE rs.dataCancellazione IS NULL "
			+ "                AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ "               ) "
			)
	BigDecimal sumImportoDaDedurreSuFatturaNoteCreditoColegateByDocId(@Param("docId") Integer docId);


	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM s.siacRSubdocAttrs rsa "
			+ "             WHERE rsa.dataCancellazione IS NULL "
			+ "             AND rsa.siacTAttr.attrCode = 'flagRilevanteIVA' "
			+ "             AND rsa.boolean_ = 'S' "
			+ "            ) "
			+ "") 
	Long countSubdocsRilevantiIvaByDocId(@Param("docId") Integer docId);
	

	@Query(" SELECT COUNT(s) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM s.siacRSubdocAttrs rsa "
			+ "             WHERE rsa.dataCancellazione IS NULL "
			+ "             AND rsa.siacTAttr.attrCode = 'flagRilevanteIVA' "
			+ "             AND rsa.boolean_ = 'S' "
			+ "            ) "
			+ " AND (s.subdocNregIva IS NULL OR s.subdocNregIva = '') "
			+ "") 
	Long countSubdocsRilevantiIvaSenzaNumeroRegistrazioneByDocId(@Param("docId") Integer docId);
	

	@Query(" SELECT COALESCE(SUM(s.subdocImportoDaDedurre), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL")
	BigDecimal sumSubdocImportoDaDedurreByDocId(@Param("docId") Integer docId);


	@Query(" SELECT d.docContabilizzaGenpcc "
			+ " FROM SiacTDoc d "
			+ " WHERE d.docId = :docId ")
	Boolean findFlagContabilizzaGenPccByDocId(@Param("docId") Integer docId);


	
	/**
	 * Conta il numero di documenti che sono collegati 
	 * alla notaCredito il cui docId e' passato come parametro. 
	 * 
	 * @param docId della notaCredito
	 * @return numero di documenti collegati.
	 */
	@Query(" SELECT COUNT(rd) "
			+ " FROM SiacRDoc rd "
			+ " WHERE rd.siacTDocFiglio.docId = :docId"
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			)
	Long countSiacTDocPadreCollegati(@Param("docId") Integer docId);
	
	
	
	
	@Query(" SELECT DISTINCT d "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocPadre.docId = :docId "
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ " AND d.siacDDocTipo.siacDDocGruppo.docGruppoTipoCode = 'NCD' "
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ " AND NOT EXISTS( "
			+ "     FROM d.siacRDocStatos rs "
			+ "     WHERE rs.dataCancellazione IS NULL "
			+ "     AND rs.siacDDocStato.docStatoCode = :docStatoCode "
			+ " ) "
			)
	List<SiacTDoc> findSiacTDocNoteCreditoSpesaByDocId(@Param("docId") Integer docId, @Param("relazTipoCode") String relazTipoCode,
			@Param("docStatoCode") String docStatoCode, @Param("docFamTipoCodes") List<String> docFamTipoCodes);
	
	/**
	 * Ricerca le note credito (documenti con relazione di tipo Nota Credito) associate ad un documento.
	 * Esclude dai risultati le note credito "multiple", ovvero quelle che non sono collegate esclusivamente al documento 
	 * con docId uguale a quello passato come parametro.
	 * 
	 * @param docId del documento di cui ricercare le note figlie.
	 * @return l'importo calcolato
	 */
	@Query(" SELECT d "
			+ FROM_NOTE_CREDITO_COLLEGATE_AL_DOC
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = '"+SiacDDocFamTipoEnum.CODICE_SPESA+"' "
			)
	List<SiacTDoc> findSiacTDocNoteCreditoSpesaColegateByDocId(@Param("docId") Integer docId);
	
	@Query(" SELECT d "
			+ FROM_NOTE_CREDITO_COLLEGATE_AL_DOC
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = '"+SiacDDocFamTipoEnum.CODICE_ENTRATA+"' "
			)
	List<SiacTDoc> findSiacTDocNoteCreditoEntrataColegateByDocId(@Param("docId") Integer docId);


	@Query(" SELECT distinct rsmt.siacTMovgestT.siacTMovgest.siacTBil "
			+ " FROM SiacRSubdocMovgestT rsmt "
			+ " WHERE rsmt.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND rsmt.dataCancellazione IS NULL"
			
			)
	List<SiacTBil> findSiacTBilByDocIdWithMovgestT(@Param("docId") Integer docId);
	
	@Query(" SELECT distinct rsmt.siacTMovgestT.siacTMovgest.siacTBil "
			+ " FROM SiacRSubdocMovgestT rsmt, SiacTSubdoc s "
			+ " WHERE rsmt.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND rsmt.dataCancellazione IS NULL"
			+ " AND rsmt.siacTSubdoc = s "
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
	List<SiacTBil> findSiacTBilByDocIdWithMovgestTSenzaOrdinativo(@Param("docId") Integer docId);
	
	@Query(" SELECT rsmt.siacTMovgestT.siacTMovgest.siacTBil "
			+ " FROM SiacRSubdocMovgestT rsmt, SiacTSubdoc s "
			+ " WHERE rsmt.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND rsmt.dataCancellazione IS NULL"
			+ " AND rsmt.siacTSubdoc = s "
			+ " AND EXISTS ( "
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
			+ "					AND srsot.siacTOrdinativoT.dataCreazione = ( SELECT MAX(ot.dataCreazione) "
			+ "																FROM SiacTOrdinativoT ot "
			+ "																WHERE ot.dataCancellazione IS NULL "
			+ "																AND EXISTS ( FROM ot.siacRSubdocOrdinativoTs rsot "
			+ "																			WHERE rsot.dataCancellazione IS NULL "
			+ "																			AND rsot.siacTSubdoc.siacTDoc.docId = :docId)"
			+ "                												) "
			+ "               ) "
			)
	List<SiacTBil> findSiacTBilByDocIdWithMovgestTEOrdinativoPiuRecente(@Param("docId") Integer docId);
	
	
	@Query(" SELECT distinct rsl.siacTLiquidazione.siacTBil "
			+ " FROM SiacRSubdocLiquidazione rsl "
			+ " WHERE rsl.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND rsl.dataCancellazione IS NULL"
			
			)
	List<SiacTBil> findSiacTBilByDocIdWithLiquidazione(@Param("docId") Integer docId);
	
	
	@Query(" SELECT rsi.siacTSubdocIvaFiglio "// NotaCreditoIva
			+ " FROM SiacRSubdocIva rsi "
			+ " WHERE rsi.dataCancellazione IS NULL "
			+ " AND rsi.siacDRelazTipo.relazTipoCode = '"+SiacDRelazTipoEnum.CodiceNotaCreditoIva+"' "
			+ " AND (rsi.siacTSubdocIvaPadre IN (SELECT rssi.siacTSubdocIva " //Legato alla quota...
			+ "                                 FROM SiacRSubdocSubdocIva rssi "
			+ "                                 WHERE rssi.siacTSubdoc.siacTDoc.docId = :docId "
			+ "                                 AND rssi.dataCancellazione IS NULL "
			+ "                                )  "
			+ "      OR rsi.siacTSubdocIvaPadre IN (FROM SiacTSubdocIva si "//...oppure legato al Documento.
			+ "                                    WHERE si.dataCancellazione IS NULL "
			+ "                                    AND si.siacRDocIva.dataCancellazione IS NULL"
			+ "                                    AND si.siacRDocIva.siacTDoc.docId = :docId  "
			+ "                                    ) "
			+ "     ) "
			+ "")
	List<SiacTSubdocIva> findSiacTSubdocIvaNotaCreditoIvaAssociataAlDoc(@Param("docId") Integer docId);

	@Query(" SELECT r.siacDDocStato "
			+ " FROM SiacRDocStato r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTDoc.docId = :docId ")
	SiacDDocStato findStatoByDocId(@Param("docId") Integer docId);


	@Query(" SELECT d.docImporto "
			+ " FROM SiacTDoc d "
			+ " WHERE d.docId = :docId "
			+ " AND d.dataCancellazione IS NULL")
	BigDecimal findImportoDocById(@Param("docId") Integer docId);
	
	@Query(" SELECT rda.siacTDoc.docImporto + rda.numerico "
			+ " FROM SiacRDocAttr rda "
			+ " WHERE rda.siacTDoc.docId = :docId "
			+ " AND rda.siacTDoc.dataCancellazione IS NULL"
			+ " AND rda.dataCancellazione IS NULL "
			+ " AND rda.siacTAttr.attrCode = 'arrotondamento' ")
	List<BigDecimal> findImportoNettoDocById(@Param("docId") Integer docId);
	
	@Query(" SELECT td.docImporto "
			+ " FROM SiacTDoc td "
			+ " WHERE td.docAnno = :docAnno "
			+ " AND td.docNumero = :docNumero "
			+ " AND td.siacDDocTipo.docTipoId = :docTipoId "
			+ " AND td.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND EXISTS ("
			+ "     FROM td.siacRDocSogs rds "
			+ "     WHERE rds.dataCancellazione IS NULL "
			+ "     AND rds.siacTSoggetto.soggettoId = :soggettoId "
			+ " ) "
			+ " AND NOT EXISTS ( "
			+ "     FROM td.siacRDocStatos rds "
			+ "     WHERE rds.dataCancellazione IS NULL "
			+ "     AND rds.siacDDocStato.docStatoCode IN (:docStatoCodes) "
			+ " ) "
			+ " AND td.dataCancellazione IS NULL")
	BigDecimal findImportoDocByDocAnnoDocNumeroDocTipoIdSoggettoIdEnteProprietarioIdDocStatoCodeNotIn(@Param("docAnno") Integer docAnno,
			@Param("docNumero") String docNumero, @Param("docTipoId") Integer docTipoId, @Param("soggettoId") Integer soggettoId,
			@Param("enteProprietarioId") Integer enteProprietarioId, @Param("docStatoCodes") Collection<String> docStatoCodes);
	
	@Query(" SELECT COALESCE(SUM(d.docImporto), 0), COALESCE(SUM(rd.docImportoDaDedurre), 0) "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocPadre.docId = :docId " 
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) " 
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND NOT EXISTS( "
			+ "     FROM d.siacRDocStatos rs "
			+ "     WHERE rs.dataCancellazione IS NULL "
			+ "     AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ " ) ")
	Object[] sumDocImportoDocImportoDaDedurreNoteCreditoByDocId(@Param("docId") Integer docId, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);
	
	@Query(" SELECT COALESCE(SUM(d.docImporto), 0), COALESCE(SUM(rd.docImportoDaDedurre), 0) "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocPadre.docId = :docId " 
			+ " AND rd.siacTDocFiglio = d "
			//+ " AND rd.siacDRelazTipo.relazTipoCode <> '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) " 
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND NOT EXISTS( "
			+ "     FROM d.siacRDocStatos rs "
			+ "     WHERE rs.dataCancellazione IS NULL "
			+ "     AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ " ) ")
	Object[] sumDocImportoDocImportoDaDedurreDocCollegatiByDocId(@Param("docId") Integer docId, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);

	@Query(" SELECT td.docNumero "
			+ " FROM SiacTDoc td "
			+ " WHERE td.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND td.docAnno = :docAnno "
			+ " AND td.docNumero LIKE CONCAT('%', :docNumero, '%') "
			+ " AND td.siacDDocTipo.docTipoCode = :docTipoCode "
			+ " AND td.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = :docFamTipoCode "
			+ " AND EXISTS ( "
			+ "     FROM td.siacRDocStatos rds "
			+ "     WHERE rds.siacDDocStato.docStatoCode IN (:docStatoCodes) "
			+ "     AND rds.dataCancellazione IS NULL "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM td.siacRDocSogs rds "
			+ "     WHERE rds.siacTSoggetto.soggettoId = :soggettoId "
			+ "     AND rds.dataCancellazione IS NULL "
			+ " ) ")
	List<String> findDocNumeroByEnteAndAnnoAndLikeNumeroAndTipoDocAndStato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("docAnno") Integer docAnno,
			@Param("docNumero") String docNumero, @Param("docTipoCode") String docTipoCode, @Param("docFamTipoCode") String docFamTipoCode, @Param("docStatoCodes") Collection<String> docStatoCodes, @Param("soggettoId") Integer soggettoId);


	@Query(" SELECT COALESCE(SUM(rd.docImportoDaDedurre), 0) "
			+ " FROM SiacRDoc rd, SiacTDoc d "
			+ " WHERE rd.siacTDocFiglio.docId = :docIdFiglio " 
			+ " AND rd.siacTDocFiglio = d "
			+ " AND rd.siacDRelazTipo.relazTipoCode = '" +SiacDRelazTipoEnum.CodiceNotaCredito + "'"
			+ " AND rd.dataCancellazione IS NULL "
			+ " AND d.dataCancellazione IS NULL "
			+ " AND NOT EXISTS(FROM d.siacRDocStatos rs " //esclude le note di credito annullate
			+ "                WHERE rs.dataCancellazione IS NULL "
			+ "                AND rs.siacDDocStato.docStatoCode = '"+ SiacDDocStatoEnum.CodiceAnnullato + "'"
			+ "               ) "
			)
	BigDecimal sumImportoDaDedurreSuFattureByDocFiglioUid(@Param("docIdFiglio") Integer docIdFiglio);

	@Query(" SELECT rd.siacTDocFiglio "
			+ " FROM SiacRDoc rd "
			+ " WHERE rd.dataCancellazione IS NULL "
			+ " AND rd.siacTDocPadre.docId = :docId "
			+ " AND rd.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ " AND rd.siacTDocFiglio.siacDDocTipo.docTipoCode = :docTipoCode "
			+ " ORDER BY rd.siacTDocFiglio.docAnno, rd.siacTDocFiglio.docNumero ")
	List<SiacTDoc> findSiacTDocSubordinatiByDocIdAndTipoCollegamento(@Param("docId") Integer docId, @Param("relazTipoCode") String relazTipoCode, @Param("docTipoCode") String docTipoCode);


	@Query(" FROM SiacRDocOnere rdo "
			+ " WHERE rdo.dataCancellazione IS NULL "
			+ " AND rdo.siacTDoc.docId = :docId "
			+ " AND NOT EXISTS ( "
			+ "     FROM rdo.siacDOnere.siacROnereSplitreverseIvaTipos rosit "
			+ "     WHERE rosit.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY rdo.siacDOnere.onereCode, rdo.siacDOnere.onereDesc ")
	List<SiacRDocOnere> findSiacRDocOnereByDocIdNotSplitReverse(@Param("docId") Integer docId);
	
	@Query(" SELECT COALESCE(COUNT(d), 0)"
			+ FROM_NOTE_CREDITO_COLLEGATE_AL_DOC
			)
	Long countNoteCreditoCollegateByDocId(@Param("docId") Integer docId);
	
	//SIAC-6048" SELECT COALESCE(SUM(s.subdocImporto - s.subdocImportoDaDedurre), 0)
	@Query("  SELECT COALESCE(SUM(s.subdocImporto - s.subdocImportoDaDedurre), 0) "
			+ " FROM SiacTSubdoc s "
			+ " WHERE s.siacTDoc.docId = :docId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND NOT EXISTS( "
			+ "     FROM SiacRSubdocProvCassa rspc "
			+ "     WHERE rspc.dataCancellazione IS NULL "
			+ "     AND (rspc.dataFineValidita IS NULL OR rspc.dataFineValidita > CURRENT_TIMESTAMP ) "
			+ "     AND rspc.siacTSubdoc = s "
			+ "     AND rspc.siacTProvCassa.dataCancellazione IS NULL "
			+ " ) "
			)
	BigDecimal sumImportoMenoImportoDaDedurreSubdocNonACoperturaByDocId(@Param("docId") Integer docId);
}
