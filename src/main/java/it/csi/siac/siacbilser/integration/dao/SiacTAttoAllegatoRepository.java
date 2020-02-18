/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;

/**
 * Repository per l'entity SiacTAttoAllegatoRepository.
 * 
 * @author Domenico
 */
public interface SiacTAttoAllegatoRepository extends JpaRepository<SiacTAttoAllegato, Integer> {
	
	static final String QUERY_IMPEGNI_COLLEGATI_BY_BOOLEAN_ATTR_CODE_AND_VALUE = " FROM SiacRAttoAllegatoElencoDoc raaed, SiacRElencoDocSubdoc reds , SiacRSubdocMovgestT rsmt, SiacRMovgestTsAttr rma "
			+ " WHERE reds.dataCancellazione IS NULL "
			+ " AND raaed.dataCancellazione IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL "
			+ " AND rma.dataCancellazione IS NULL "
			+ " AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND raaed.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL  "
			+ " AND rsmt.siacTSubdoc = reds.siacTSubdoc "
			+ " AND rma.siacTMovgestT = rsmt.siacTMovgestT "
			+ " AND rsmt.siacTSubdoc.dataCancellazione IS NULL  "
			+ " AND rma.siacTAttr.attrCode = :attrCode  "
			+ " AND rma.boolean_ = :booleanValue ";
	
	/**
	 * Find allegati atto by provvisorio di cassa.
	 *
	 * @param provcId the provc id
	 * @return the list
	 */
	@Query(" FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM aa.siacRAttoAllegatoElencoDocs raaed, SiacTElencoDoc ed "
			+ "             WHERE raaed.dataCancellazione IS NULL "
			+ "             AND raaed.siacTElencoDoc = ed "
			+ "             AND EXISTS (FROM ed.siacRElencoDocSubdocs reds, SiacTSubdoc s "
			+ "                         WHERE reds.dataCancellazione IS NULL "
			+ "                         AND reds.siacTSubdoc = s "
			+ "                         AND EXISTS (FROM s.siacRSubdocProvCassas spc "
			+ "                                     WHERE spc.dataCancellazione IS NULL "
			+ "                                     AND spc.siacTProvCassa.provcId = :provcId "
			+ "                                    ) "
			+ "                         ) "
			+ "            ) "
			)
	List<SiacTAttoAllegato> findAllegatiAttoByProvvisorioDiCassa(@Param("provcId") Integer provcId);

	
	
	/**
	 * Restituisce l'allegatoAtto relativo all'atto amministrativo passato come parametro.
	 * 
	 * Nota: La lista di ritorno, a meno di errori su db, dovrebbe contenere un solo elemento!
	 *
	 * @param attoammId the attoamm id
	 * @return the list
	 */
	@Query(" FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND aa.siacTAttoAmm.attoammId = :attoammId "
			+ " AND EXISTS (FROM aa.siacRAttoAllegatoStatos aas "
			+ "                 WHERE aas.dataCancellazione IS NULL "
			+ "                 AND aas.siacDAttoAllegatoStato.attoalStatoCode NOT IN ( '" +SiacDAttoAllegatoStatoEnum.CODICE_ANNULLATO + "' , "
		    + "                                                                         '" +SiacDAttoAllegatoStatoEnum.CODICE_RIFIUTATO + "' ) "
			+ "            ) " 
		  )
	List<SiacTAttoAllegato> findAllegatiNonAnnullatiByAttoAmministrativo(@Param("attoammId") Integer attoammId);


	@Query(" SELECT staa.siacTAttoAmm "
			+ " FROM SiacTAttoAllegato staa "
			+ " WHERE staa.attoalId = :attoalId")
	SiacTAttoAmm findSiacTAttoAmmByAttoalId(@Param("attoalId") Integer attoalId);

	@Query(" FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM aa.siacRAttoAllegatoElencoDocs sraaed "
			+ "     WHERE sraaed.dataCancellazione IS NULL "
			+ "     AND sraaed.siacTElencoDoc.eldocId = :eldocId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM aa.siacRAttoAllegatoStatos aas "
			+ "     WHERE aas.dataCancellazione IS NULL "
			+ "     AND aas.siacDAttoAllegatoStato.attoalStatoCode NOT IN (:attoalStatoCodes)"
			+ " ) ") 
  
	List<SiacTAttoAllegato> findSiacTAttoAllegatoByEldocIdAndNotByAttoalStatoCodes(@Param("eldocId") Integer eldocId,
			@Param("attoalStatoCodes") Collection<String> attoalStatoCodes);


	@Query("SELECT COUNT(*) "
			+ " FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND aa.attoalId = :attoalId "
			+ " AND EXISTS(SELECT aaed FROM aa.siacRAttoAllegatoElencoDocs aaed, SiacTElencoDoc ed"
			+ "             WHERE aaed.dataCancellazione IS NULL "
			+ "             AND aaed.siacTElencoDoc = ed "
			+ "             AND EXISTS(SELECT reds FROM ed.siacRElencoDocSubdocs reds, SiacTDoc d "
			+ "                        WHERE reds.dataCancellazione IS NULL "
			+ "                        AND reds.siacTSubdoc.siacTDoc =  d "
			+ "                        AND EXISTS(SELECT rdo FROM d.siacRDocOneres rdo "
			+ "                                   WHERE rdo.dataCancellazione IS NULL "
			+ "                                   AND rdo.siacDOnere.dataCancellazione IS NULL "
			+ "                                   ) "
			+ "                       )"
			+ "            ) "
			)
	Long countDocumentiConRitenute(@Param("attoalId") Integer attoalid);
	
	@Query("SELECT COUNT(*) "
			+ " FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND aa.attoalId = :attoalId "
			+ " AND EXISTS(SELECT aaed FROM aa.siacRAttoAllegatoElencoDocs aaed, SiacTElencoDoc ed "
			+ "             WHERE aaed.dataCancellazione IS NULL "
			+ "             AND aaed.siacTElencoDoc = ed "
			+ "             AND EXISTS(SELECT reds FROM ed.siacRElencoDocSubdocs reds, SiacTDoc d "
			+ "                        WHERE reds.dataCancellazione IS NULL "
			+ "                        AND reds.siacTSubdoc.siacTDoc =  d "
			+ "                        AND d.dataCancellazione IS NULL "
			+ "                        AND d.siacDDocTipo.docTipoCode NOT IN (:docTipoCodesDaEscludere) " //ALG, CCN
			+ "                       ) "
			+ "            ) "
			)
	Long countFatture(@Param("attoalId") Integer attoalid, @Param("docTipoCodesDaEscludere") List<String> docTipoCodesDaEscludere);
	
	
	@Query("SELECT COUNT(*) "
			+ " FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND aa.attoalId = :attoalId "
			+ " AND EXISTS(SELECT aaed FROM aa.siacRAttoAllegatoElencoDocs aaed, SiacTElencoDoc ed "
			+ "             WHERE aaed.dataCancellazione IS NULL "
			+ "             AND aaed.siacTElencoDoc = ed "
			+ "             AND EXISTS(SELECT reds FROM ed.siacRElencoDocSubdocs reds, SiacTSubdoc s "
			+ "                        WHERE reds.dataCancellazione IS NULL "
			+ "                        AND reds.siacTSubdoc =  s "
			+ "                        AND EXISTS (SELECT rsit FROM s.siacRSubdocSplitreverseIvaTipos rsit "
			+ "                             WHERE rsit.dataCancellazione IS NULL " 
			+ "                             AND rsit.siacDSplitreverseIvaTipo.srivaTipoCode IN (:splitReeversIvaTipoCodes) " //SI, SC
			+ "                             ) "
			+ "                       ) "
			+ "            ) "
			)
	Long countQuoteConSplitReverse(@Param("attoalId") Integer attoalid, @Param("splitReeversIvaTipoCodes") List<String> splitReeversIvaTipoCodes);

	@Query(" SELECT COALESCE(COUNT(rdo.siacDOnere), 0) "
			+ " FROM SiacRDocOnere rdo "
			+ " WHERE rdo.dataCancellazione IS NULL "
			+ " AND rdo.siacTDoc.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
			+ "     WHERE reds.siacTSubdoc.siacTDoc = rdo.siacTDoc "
			+ "     AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ "     AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ "     AND reds.dataCancellazione IS NULL "
			+ "     AND raaed.dataCancellazione IS NULL "
			+ "     AND reds.siacTSubdoc.dataCancellazione IS NULL "
			+ "     AND reds.siacTElencoDoc.dataCancellazione IS NULL "
			+ "     AND raaed.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " ) ")
	Long countSiacDOnereByAttoalId(@Param("attoalId") Integer attoalId);

	@Query(" FROM SiacRAttoAllegatoStato raas "
			+ " WHERE raas.siacTAttoAllegato.attoalId = :attoalId "
			+ " ORDER BY raas.dataCreazione ")
	List<SiacRAttoAllegatoStato> findSiacRAttoAllegatoStatoOrderedByDataCreazione(@Param("attoalId") Integer attoalId);

	@Query(" SELECT DISTINCT rds.siacTSoggetto "
			+ " FROM SiacRDocSog rds, SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
			+ " WHERE rds.siacTDoc = reds.siacTSubdoc.siacTDoc "
			+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ " AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND rds.dataCancellazione IS NULL "
			+ " AND reds.dataCancellazione IS NULL"
			+ " AND raaed.dataCancellazione IS NULL "
			+ " AND rds.siacTSoggetto.dataCancellazione IS NULL "
			+ " AND rds.siacTDoc.dataCancellazione IS NULL "
			+ " AND reds.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND raaed.siacTAttoAllegato.dataCancellazione IS NULL ")
	List<SiacTSoggetto> findSiacTSoggettoByAttoalId(@Param("attoalId") Integer attoalId);
	
	@Query(" FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM aa.siacRAttoAllegatoElencoDocs raaed, SiacRElencoDocSubdoc reds "
			+ "     WHERE raaed.dataCancellazione IS NULL "
			+ "     AND reds.siacTElencoDoc = raaed.siacTElencoDoc "
			+ "     AND reds.siacTSubdoc.subdocId = :subdocId "
			+ " ) ") 
	List<SiacTAttoAllegato> findSiacTAttoAllegatoBySubdocId(@Param("subdocId") Integer subdocId);


	@Query(" FROM SiacTAttoAllegato aa "
			+ " WHERE aa.dataCancellazione IS NULL "
			+ " AND  aa.attoalId in (:attoalIds) "
			) 
	List<SiacTAttoAllegato> findListaAllegatiByattoAlIds(@Param("attoalIds")List<Integer> attoalIds);


	@Query( " SELECT COALESCE(COUNT(DISTINCT rsmt.siacTMovgestT.movgestTsId), 0) "
			+ QUERY_IMPEGNI_COLLEGATI_BY_BOOLEAN_ATTR_CODE_AND_VALUE
			)
	Long countImpegniWithBooleanAttrCodeAndValueByAttoalId(@Param("attoalId") Integer attoalId, @Param("attrCode") String attrCode, @Param("booleanValue") String booleanValue);
	
	@Query( " SELECT ts.dataFineValiditaDurc  "
			+ " FROM SiacRAttoAllegatoElencoDoc raaed, SiacRElencoDocSubdoc reds , SiacTSoggetto ts   "
			+ " WHERE reds.dataCancellazione IS NULL "
			+ " AND ts.dataCancellazione IS NULL "
			+ " AND raaed.dataCancellazione IS NULL "
			+ " AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND raaed.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL   "
			+ " AND ts.dataCancellazione IS NULL        "
			+ " AND ts.dataFineValiditaDurc IS NOT NULL "
			+ " AND ( EXISTS( "
			+ " 	FROM SiacRDocSog rds "
			+ " 	WHERE rds.dataCancellazione IS NULL "
			+ " 	AND rds.siacTSoggetto = ts "
			+ " 	AND rds.siacTDoc = reds.siacTSubdoc.siacTDoc "
			+ " )OR EXISTS( "
			+ " 	FROM SiacRSubdocModpag rsm "
			+ " 	WHERE rsm.dataCancellazione IS NULL "
			+ " 	AND rsm.siacRSoggrelModpag.siacRSoggettoRelaz.siacTSoggetto2 = ts "
			+ " )) "
			 )
	List<Date> getDataFineValiditaDurcByAttoalId(@Param("attoalId") Integer attoalId);
	
	@Query( " SELECT rsmt.siacTSubdoc.subdocId "
			+ " FROM SiacRAttoAllegatoElencoDoc raaed, SiacRElencoDocSubdoc reds , SiacRSubdocMovgestT rsmt, SiacRMovgestTsAttr rma "
			+ " WHERE reds.dataCancellazione IS NULL "
			+ " AND raaed.dataCancellazione IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL "
			+ " AND rma.dataCancellazione IS NULL "
			+ " AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND raaed.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL  "
			+ " AND rsmt.siacTSubdoc = reds.siacTSubdoc "
			+ " AND rma.siacTMovgestT = rsmt.siacTMovgestT "
			+ " AND rsmt.siacTSubdoc.dataCancellazione IS NULL  "
			+ " AND rma.siacTAttr.attrCode = :attrCode  "
			+ " AND rma.boolean_ = :booleanValue "
			)
	List<Integer> getUidsSubdocWithImpegniWithBooleanAttrCodeAndValueByAttoalId(@Param("attoalId") Integer attoalId, @Param("attrCode") String attrCode, @Param("booleanValue") String booleanValue);
	
}
