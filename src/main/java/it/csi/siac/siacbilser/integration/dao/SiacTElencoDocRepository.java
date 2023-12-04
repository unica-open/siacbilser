/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;

/**
 * Repository per l'entity SiacTSubdoc.
 *
 * @author Domenico
 * @author Marchino Alessandro
 */
public interface SiacTElencoDocRepository extends JpaRepository<SiacTElencoDoc, Integer> {
	
	
	
	/**
	 * count countQuoteSenzaLiquidazioneByElencoId
	 *
	 * @param elencoId the elencoId
	 * @return Long
	 */	
	@Query(   
			  " SELECT COALESCE(COUNT(tsd), 0) "
		    + " FROM SiacRElencoDocSubdoc reds, SiacTSubdoc tsd "
			+ " WHERE reds.siacTElencoDoc.eldocId  = :elencoId "
			+ " AND  reds.siacTSubdoc =  tsd "
			+ " AND  reds.dataCancellazione is null "
			+ " AND (tsd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'S' OR tsd.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'IS')"
			+ " AND  NOT EXISTS ( "
			+ "      FROM SiacRSubdocLiquidazione rsl "
			+ "      WHERE rsl.siacTSubdoc = tsd "
			+ "      AND rsl.dataCancellazione is null "
			+ "     ) "
			)
	Long countQuoteSpesaSenzaLiquidazioneByElencoId(@Param("elencoId") Integer elencoId);

		
	/**
	 * Somma dei sub-documenti di Spesa o Entrata non annullati collegati all'elenco escluse i
	 * documenti di tipo <em>nota di credito</em>, tenendo conto che l'importo deve essere
	 * decurtato degli <em>importi da dedurre</em>.
	 *
	 * @param eldocId the eldoc id
	 * @param docFamTipoCodes the doc fam tipo codes
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(sd.subdocImporto - sd.subdocImportoDaDedurre), 0) AS tot "
			+ " FROM SiacTSubdoc sd, SiacTDoc d " 
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND sd.siacTDoc = d "
			+ " AND EXISTS (FROM sd.siacRElencoDocSubdocs sde "
			+ "             WHERE sde.dataCancellazione IS NULL "
			+ "             AND sde.siacTElencoDoc.eldocId = :eldocId "
			+ "            )  "
			+ " AND EXISTS (FROM d.siacRDocStatos r "
			+ "		        WHERE r.dataCancellazione IS NULL "
			+ "             AND r.siacDDocStato.docStatoCode NOT IN ( 'A' ) "
			+ "            ) "
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) " 
			+ " AND NOT EXISTS ( "
			+ "     FROM SiacDDocGruppo ddg "
			+ "     WHERE ddg.docGruppoTipoCode = 'NCD' "
			+ "     AND d.siacDDocTipo.siacDDocGruppo = ddg "
			+ " ) "
			)
	BigDecimal calcolaTotaleQuote(@Param("eldocId") Integer eldocId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);

	
	
	
	
	/**
	 * Somma delle liquidazioni Definitive legate all'elenco senza ordinativo non annullato (non deve avere ordinativi validi).
	 *
	 * @param eldocId the eldoc id
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(l.liqImporto), 0) as tot "
			+ " FROM SiacTLiquidazione l "
			+ " WHERE l.dataCancellazione IS NULL "
			+ " AND (l.dataFineValidita IS NULL OR l.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND EXISTS (FROM l.siacRSubdocLiquidaziones srsl, SiacTSubdoc s "
			+ "             WHERE srsl.siacTSubdoc = s "
			+ "             AND srsl.dataCancellazione IS NULL "
			+ " 			AND (srsl.dataFineValidita IS NULL OR srsl.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "             AND (s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'S' OR s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'IS')"
			+ "             AND EXISTS (FROM s.siacRElencoDocSubdocs sreds "
			+ "                         WHERE sreds.dataCancellazione IS NULL "
			+ " 						AND (sreds.dataFineValidita IS NULL OR sreds.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "                         AND sreds.siacTElencoDoc.eldocId = :eldocId "
			+ "                        ) "
			+ "            ) "
			+ " AND EXISTS (FROM l.siacRLiquidazioneStatos srls"
			+ "             WHERE l.dataCancellazione IS NULL "
			+ " 			AND (l.dataFineValidita IS NULL OR l.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "             AND srls.dataCancellazione IS NULL "
			+ " 			AND (srls.dataFineValidita IS NULL OR srls.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "             AND srls.siacDLiquidazioneStato.liqStatoCode = 'V' " //stato liquidazione VALIDO! (DEFINITIVO non esiste)
	        + "            ) "
	        + " AND NOT EXISTS (FROM l.siacRLiquidazioneOrds srlo, SiacTOrdinativo o"
			+ "             WHERE srlo.dataCancellazione IS NULL "
			+ " 			AND (srlo.dataFineValidita IS NULL OR srlo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "             AND srlo.siacTOrdinativoT.siacTOrdinativo = o "
			+ "             AND EXISTS ( FROM o.siacROrdinativoStatos os"
			+ "                                     WHERE os.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			+ "                                     AND os.dataCancellazione IS NULL "
			+ "                                     AND (os.dataFineValidita IS NULL OR os.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "                                    ) "
	        + "            ) "
	        )
	BigDecimal calcolaTotaleDaPagare(@Param("eldocId") Integer eldocId);

	/**
	 * Somma delle liquidazioni Definitive legate all'elenco con ordinativo valido.
	 *
	 * @param eldocId the eldoc id
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(l.liqImporto), 0) as tot "
			+ " FROM SiacTLiquidazione l "
			+ " WHERE l.dataCancellazione IS NULL "
			+ " AND (l.dataFineValidita IS NULL OR l.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND EXISTS ( "
			+ "     FROM l.siacRSubdocLiquidaziones srsl, SiacTSubdoc s, SiacRElencoDocSubdoc sreds, SiacRDocStato rds "
			+ "     WHERE srsl.siacTSubdoc = s "
			+ "     AND sreds.siacTSubdoc = s "
			+ "     AND rds.siacTDoc = s.siacTDoc "
			+ "     AND srsl.dataCancellazione IS NULL "
			+ "     AND sreds.dataCancellazione IS NULL "
			+ "     AND rds.dataCancellazione IS NULL "
			+ " 	AND (srsl.dataFineValidita IS NULL OR srsl.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND (sreds.dataFineValidita IS NULL OR sreds.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN ('S', 'IS') "
			+ "     AND rds.siacDDocStato.docStatoCode <> 'A' "
			+ "     AND sreds.siacTElencoDoc.eldocId = :eldocId"
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM l.siacRLiquidazioneStatos srls"
			+ "     WHERE l.dataCancellazione IS NULL "
			+ " 	AND (l.dataFineValidita IS NULL OR l.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND srls.dataCancellazione IS NULL "
			+ " 	AND (srls.dataFineValidita IS NULL OR srls.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND srls.siacDLiquidazioneStato.liqStatoCode = 'V' " //stato liquidazione VALIDO! (DEFINITIVO non esiste)
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM l.siacRLiquidazioneOrds srlo, SiacTOrdinativo o, SiacROrdinativoStato os "
			+ "     WHERE srlo.siacTOrdinativoT.siacTOrdinativo = o "
			+ "     AND os.siacTOrdinativo = o "
			+ "     AND srlo.dataCancellazione IS NULL "
			+ "     AND os.dataCancellazione IS NULL"
			+ " 	AND (srlo.dataFineValidita IS NULL OR srlo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND (os.dataFineValidita IS NULL OR os.dataFineValidita > CURRENT_TIMESTAMP)"
			+ "     AND os.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			+ " ) ")
	BigDecimal calcolaTotalePagato(@Param("eldocId") Integer eldocId);

	/**
	 * Somma dei sub-documenti di entrata con tipoConvalida diversa da nullo senza un
	 * ordinativo di incasso non annullato.
	 *
	 * @param eldocId the eldoc id
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(sd.subdocImporto), 0) AS tot "
			+ " FROM SiacTSubdoc sd, SiacTDoc d " 
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND sd.siacTDoc = d "
			+ " AND (sd.subdocConvalidaManuale IS NOT NULL AND sd.subdocConvalidaManuale <> '') "
			+ " AND EXISTS ( "
			+ "     FROM sd.siacRElencoDocSubdocs sde "
			+ "     WHERE sde.dataCancellazione IS NULL "
			+ "     AND (sde.dataFineValidita IS NULL OR sde.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND sde.siacTElencoDoc.eldocId = :eldocId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM d.siacRDocStatos r "
			+ "     WHERE  r.dataCancellazione IS NULL "
			+ "     AND (r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND r.siacDDocStato.docStatoCode NOT IN ( 'A' ) "
			+ " ) "
			+ " AND (d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'E' OR d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'IE') "
			+ " AND NOT EXISTS (FROM sd.siacRSubdocOrdinativoTs r, SiacTOrdinativo o"
			+ "            		 WHERE r.dataCancellazione IS NULL "
			+ " 			     AND (r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "            		 AND r.siacTOrdinativoT.siacTOrdinativo = o "
			+ "                  AND EXISTS (   FROM o.siacROrdinativoStatos os "
			+ "                                  WHERE os.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			+ "                                  AND os.dataCancellazione IS NULL "
			+ "                                  AND (os.dataFineValidita IS NULL OR os.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "                               ) "
			+ "            ) "
			)
	BigDecimal calcolaTotaleDaIncassare(@Param("eldocId") Integer eldocId);
	
	/**
	 * Somma dei sub-documenti di entrata con tipoConvalida diversa da nullo con un
	 * ordinativo di incasso valido.
	 *
	 * @param eldocId the eldoc id
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(sd.subdocImporto), 0) AS tot "
			+ " FROM SiacTSubdoc sd, SiacTDoc d " 
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND sd.siacTDoc = d "
			+ " AND (sd.subdocConvalidaManuale IS NOT NULL AND sd.subdocConvalidaManuale <> '') "
			+ " AND EXISTS ( "
			+ "     FROM sd.siacRElencoDocSubdocs sde "
			+ "     WHERE sde.dataCancellazione IS NULL "
			+ "     AND (sde.dataFineValidita IS NULL OR sde.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND sde.siacTElencoDoc.eldocId = :eldocId "
			+ " )  "
			+ " AND EXISTS ( "
			+ "     FROM d.siacRDocStatos r "
			+ "     WHERE r.dataCancellazione IS NULL "
			+ "     AND (r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND r.siacDDocStato.docStatoCode <> 'A' "
			+ " ) "
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN('E', 'IE') "
			+ " AND EXISTS ( "
			+ "     FROM sd.siacRSubdocOrdinativoTs r, SiacTOrdinativo o, SiacROrdinativoStato os"
			+ "     WHERE r.siacTOrdinativoT.siacTOrdinativo = o "
			+ "     AND os.siacTOrdinativo = o "
			+ "     AND r.dataCancellazione IS NULL "
			+ "     AND os.dataCancellazione IS NULL "
			+ "     AND (r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND (os.dataFineValidita IS NULL OR os.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND os.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			+ " ) ")
	BigDecimal calcolaTotaleIncassato(@Param("eldocId") Integer eldocId);

	/**
	 * Somma dei sub-documenti di spesa/entrata con tipoConvalida nullo.
	 *
	 * @param eldocId the eldoc id
	 * @param docFamTipoCode the doc fam tipo code
	 * @return the big decimal
	 */
	@Query("SELECT COALESCE(SUM(sd.subdocImporto), 0) AS tot "
			+ " FROM SiacTSubdoc sd, SiacTDoc d " 
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND sd.siacTDoc = d "
			//SIAC-5580: il flag convalida manuale deve essere null
			+ " AND (sd.subdocConvalidaManuale IS NULL OR sd.subdocConvalidaManuale = '' ) "
			+ " AND EXISTS (FROM sd.siacRElencoDocSubdocs sde "
			+ "             WHERE sde.dataCancellazione IS NULL"
			+ "             AND sde.siacTElencoDoc.eldocId = :eldocId "
			+ "            )  "
			+ " AND EXISTS (FROM d.siacRDocStatos r "
			+ "		        WHERE  r.dataCancellazione IS NULL "
			+ "             AND r.siacDDocStato.docStatoCode NOT IN ( 'A' ) "
			+ "            ) "
			+ " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) " 
			)
	BigDecimal calcolaTotaleDaConvalidare(@Param("eldocId") Integer eldocId, @Param("docFamTipoCodes") List<String> docFamTipoCodes);
	
	
	
	@Query(   " FROM SiacTElencoDoc sed "
			+ " WHERE sed.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM sed.siacRAttoAllegatoElencoDocs res "
			+ "              WHERE res.dataCancellazione IS NULL "
			+ " 			 AND res.siacTAttoAllegato.attoalId = :attoalId "
			+ "            ) "
			+ " AND sed.dataCreazione = ( SELECT MAX (ed.dataCreazione) "
			+ "							  FROM SiacTElencoDoc ed "
			+ "							  WHERE ed.dataCancellazione IS NULL "
			+ "                          ) "
			)
	SiacTElencoDoc findPiuRecenteByIdAllegato(@Param("attoalId") Integer uid);
	
	
	
	/**
	 * Elenco delle delle quote collegate all'elenco &eacute; collegate a un provvisorio di cassa.
	 *
	 * @param eldocId the eldoc id
	 * @return the list
	 */
	
	@Query(" SELECT sd "
			+ " FROM SiacTSubdoc sd, SiacTDoc d " 
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND sd.siacTDoc = d "
			+ " AND EXISTS (FROM sd.siacRElencoDocSubdocs sde "
			+ "             WHERE sde.dataCancellazione IS NULL"
			+ "             AND sde.siacTElencoDoc.eldocId = :eldocId "
			+ "            )  "
			+ " AND EXISTS (FROM d.siacRDocStatos r "
			+ "		        WHERE r.dataCancellazione IS NULL "
			+ "             AND r.siacDDocStato.docStatoCode NOT IN ( 'A' ) "
			+ "            ) "
			+ " AND EXISTS (FROM sd.siacRSubdocProvCassas r "
			+ "		        WHERE r.dataCancellazione IS NULL "
			+ "            ) "
			)
	List<SiacTSubdoc> findQuoteCollegateAProvvisorioDiCassa(@Param("eldocId") Integer eldocId);
	
	
	
	@Query("FROM SiacTElencoDoc e "
			+ " WHERE e.dataCancellazione IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM e.siacRAttoAllegatoElencoDocs sraaed "
			+ "     WHERE sraaed.siacTAttoAllegato.attoalId = :attoalId "
			+ "     AND sraaed.dataCancellazione IS NULL "
			+ " ) ")
	List<SiacTElencoDoc> findByAttoalId(@Param("attoalId") Integer attoalId);
	
	
	@Query("SELECT COUNT(*) "
			+ " FROM SiacRElencoDocSubdoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL "
			)
	Long calcolaNumeroQuoteLegateAdElenco(@Param("eldocId") Integer eldocId);
	
	@Query("SELECT COUNT(*) "
			+ " FROM SiacRElencoDocSubdoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM r.siacTSubdoc.siacRSubdocAttoAmms srsaa "
			+ "     WHERE srsaa.dataCancellazione IS NULL "
			+ " )")
	Long countSiacRElencoDocSubdocWithSiacTSubdocsWithSiacTAttoAmm(@Param("eldocId") Integer eldocId);
	
	@Query(" FROM SiacRElencoDocSubdoc r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			+ " AND r.siacTSubdoc.dataCancellazione IS NULL ")
	List<SiacRElencoDocSubdoc> findSiacRElencoDocSubdocsByEldocIdAndSubdocId(@Param("eldocId") Integer eldocId, @Param("subdocId") Integer subdocId);




	@Query( "FROM SiacTSubdoc sd"
			+ " WHERE sd.dataCancellazione IS NULL "
			+ " AND ( "
			+ "     sd.siacTDoc IN ( "
			+ "         SELECT tdp "
			+ "         FROM SiacRDoc rdp, SiacTDoc tdp "
			+ "         WHERE rdp.siacTDocPadre = tdp"
			+ "         AND rdp.dataCancellazione IS NULL "
			+ "         AND rdp.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ "         AND rdp.siacTDocFiglio IN ( "
			+ "             SELECT stp.siacTDoc "
			+ "             FROM SiacTSubdoc stp "
			+ "             WHERE stp.dataCancellazione IS NULL "
			+ "             AND stp.subdocId IN (:subdocIds) "
			+ "         ) "
			+ "         AND NOT EXISTS ( "
			+ "             FROM tdp.siacRDocStatos rds "
			+ "             WHERE rds.dataCancellazione IS NULL "
			+ "             AND rds.siacDDocStato.docStatoCode = 'A' "
			+ "         ) "
			+ "     ) "
			+ "     OR sd.siacTDoc IN ( "
			+ "         SELECT tdf "
			+ "         FROM SiacRDoc rdf, SiacTDoc tdf "
			+ "         WHERE rdf.siacTDocFiglio = tdf "
			+ "         AND rdf.dataCancellazione IS NULL "
			+ "         AND rdf.siacDRelazTipo.relazTipoCode = :relazTipoCode "
			+ "         AND rdf.siacTDocPadre IN ( "
			+ "             SELECT stf.siacTDoc"
			+ "             FROM SiacTSubdoc stf "
			+ "             WHERE stf.dataCancellazione IS NULL "
			+ "             AND stf.subdocId IN (:subdocIds) "
			+ "         ) "
			+ "         AND NOT EXISTS ( "
			+ "             FROM tdf.siacRDocStatos rds "
			+ "             WHERE rds.dataCancellazione IS NULL "
			+ "             AND rds.siacDDocStato.docStatoCode = 'A' "
			+ "         ) "
			+ "     )"
			+ " ) ")
	List<SiacTSubdoc> findSubdocCollegatiByTipoRelazione(@Param("subdocIds") List<Integer> subdocIds,@Param("relazTipoCode") String relazTipoCode);
	
	@Query(" FROM SiacTElencoDoc ted "
			+ " WHERE ted.eldocAnno = :eldocAnno "
			+ " AND ted.eldocNumero = :eldocNumero "
			+ " AND ted.dataCancellazione IS NULL "
			+ " AND ted.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	List<SiacTElencoDoc> findByEldocAnnoEldocNumeroEnteProprietarioId(@Param("eldocAnno") Integer eldocAnno, @Param("eldocNumero")Integer eldocNumero,
			@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" FROM SiacTElencoDoc ted "
			+ " WHERE ted.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM ted.siacRAttoAllegatoElencoDocs sraaed "
			+ "     WHERE sraaed.dataCancellazione IS NULL "
			+ "     AND sraaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM SiacRElencoDocStato sreds "
			+ "     WHERE sreds.dataCancellazione IS NULL "
			+ "     AND sreds.siacTElencoDoc =  ted "
			+ "     AND sreds.siacDElencoDocStato.eldocStatoCode = :eldocStatoCode "
			+ " ) ")
	List<SiacTElencoDoc> findEldocByAttoalIdStatoElenco(@Param("attoalId") Integer attoalId, @Param("eldocStatoCode") String eldocStatoCode);
	
	
	@Query( " SELECT reds.siacTSubdoc.subdocId "
			+ " FROM SiacRElencoDocSubdoc reds , SiacRSubdocMovgestT rsmt, SiacRMovgestTsAttr rma "
			+ " WHERE reds.dataCancellazione IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL "
			+ " AND rma.dataCancellazione IS NULL "
			+ " AND reds.siacTElencoDoc.eldocId = :eldocId"
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND rsmt.siacTSubdoc = reds.siacTSubdoc "
			+ " AND (rma.siacTMovgestT = rsmt.siacTMovgestT OR rma.siacTMovgestT =  rsmt.siacTMovgestT.siacTMovgestIdPadre) "
			+ " AND rsmt.siacTSubdoc.dataCancellazione IS NULL " 
			+ " AND rma.siacTAttr.attrCode = :attrCode "
			+ " AND rma.boolean_ = :booleanValue) "
	   )	
	List<Integer> getSubDocIdsWihtMovgestWithBooleanAttrCodeAndValueByEldocId(@Param("eldocId") Integer eldocId, @Param("attrCode") String attrCode, @Param("booleanValue") String booleanValue);
		
	@Query(value="" 
			+ " with soggetto_cessione as ("
			+ "		select sog_cessione.soggetto_id, sog_cessione.soggetto_fine_validita_durc as durc, reds.subdoc_id as id "
			+ "		from siac_r_elenco_doc_subdoc reds  "
			+ "		join siac_t_elenco_doc eldoc ON (reds.eldoc_id=eldoc.eldoc_id ) "
			+ "		cross join siac_t_soggetto sog_cessione "
			+ "		where (reds.data_cancellazione is null) "
			+ "		and (sog_cessione.data_cancellazione is null) "
			+ "	    and reds.eldoc_id= :eldocId  "
			+ "		and reds.subdoc_id in (:subodcIds) "
			+ "		and (eldoc.data_cancellazione is null) "
			+ "		and (sog_cessione.data_cancellazione is null)  "
			+ "		and (exists ( "
			+ "			select 1 "
			+ "			from siac_r_subdoc_modpag rsm   "
			+ "			join siac_r_soggrel_modpag rsmdp on (rsmdp.modpag_id=rsm.modpag_id ) "
			+ "			join siac_r_soggetto_relaz rsr on (rsmdp.soggetto_relaz_id=rsr.soggetto_relaz_id) "
			+ "			where (rsm.data_cancellazione is null) "
			+ "			and rsr.soggetto_id_a=sog_cessione.soggetto_id "
			+ "			and rsm.subdoc_id=reds.subdoc_id "
			+ "		))  "
			+ "	), soggetto_documento as (  "
			+ "		select sog_doc.soggetto_fine_validita_durc as durc, reds_doc.subdoc_id as id          "
			+ "		from siac_r_elenco_doc_subdoc reds_doc "
			+ "		join siac_t_elenco_doc eldoc_doc ON (reds_doc.eldoc_id=eldoc_doc.eldoc_id ) "
			+ "		cross join siac_t_soggetto sog_doc "
			+ "		where (reds_doc.data_cancellazione is null) "
			+ "		and (sog_doc.data_cancellazione is null) "
			+ "	    and reds_doc.eldoc_id= :eldocId "
			+ "		and reds_doc.subdoc_id in (:subodcIds) "
			+ "		and (eldoc_doc.data_cancellazione is null)  "
			+ "		and (sog_doc.data_cancellazione is null)    "
			+ "		and (sog_doc.soggetto_fine_validita_durc is not null) "
			+ "		and exists (  "
			+ "			select 1  "
			+ "			from siac_r_doc_sog rdoc "
			+ "			join siac_t_subdoc ts_doc on ts_doc.doc_id = rdoc.doc_id "
			+ "			where ts_doc.subdoc_id = reds_doc.subdoc_id "
			+ "	        and rdoc.soggetto_id = sog_doc.soggetto_id  "
			+ "			AND ts_doc.data_cancellazione is null "
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
	List<Date> getDataFineValiditaDurcByEldocIdSubdocIds(@Param("eldocId") Integer eldocId, @Param("subodcIds")List<Integer> subodcIds);

	
	
}
