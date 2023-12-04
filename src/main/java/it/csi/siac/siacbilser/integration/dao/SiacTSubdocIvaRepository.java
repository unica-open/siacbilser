/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Repository per l'entity SiacTSubdocRepository.
 *
 */
public interface SiacTSubdocIvaRepository extends JpaRepository<SiacTSubdocIva, Integer> {
	
	/**
	 * Compone parte della query da utilizzare in findByRegistroIva
	 */
	static final String SIACTSUBDOCIVA_DOC_FAM_TIPO_FILTER =  " AND ( "
			+ "	EXISTS(FROM d.siacRSubdocSubdocIvas r "
			+ "          WHERE r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN( :docFamTipoCode,  :docFamTipoCodeIva )  "
			+ "			AND r.dataCancellazione IS NULL "
			+ " 		   ) "
			+ " OR "
			+ " 	EXISTS(FROM d.siacRDocIva r "
			+ " 		 WHERE r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCode,  :docFamTipoCodeIva )   "
			+ "		 AND r.dataCancellazione IS NULL "
			+ "     	 ) "		
			+ " OR "
			+ "	EXISTS(FROM d.siacRSubdocIvasFiglio si "
			+ " 		   WHERE "
			+ "	          EXISTS(FROM si.siacTSubdocIvaPadre.siacRSubdocSubdocIvas r "
			+ "          		     WHERE r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCode,  :docFamTipoCodeIva )  "
			+ "			         AND r.dataCancellazione IS NULL "
			+ " 		      ) "
			+ " 	      ) "
			+ " OR "
			+ "	EXISTS(FROM d.siacRSubdocIvasFiglio si "
			+ " 		   WHERE "
			+ " 			  EXISTS(FROM si.siacTSubdocIvaPadre.siacRDocIva r "
			+ " 		             WHERE r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCode,  :docFamTipoCodeIva )   "
			+ "		             AND r.dataCancellazione IS NULL "
			+ "     	      ) "
			+ " 	      ) "
			+ " ) ";
	
	/**
	 * Compone parte della query da utilizzare in findByRegistroIva. Permette di utilizzare <code>n</code> codici distinti per la famiglia
	 */
	static final String SIACTSUBDOCIIVA_DOC_FAM_TIPO_CODES_FILTER = " AND ( "
			+ "     EXISTS( "
			+ "         FROM d.siacRSubdocSubdocIvas r "
			+ "         WHERE r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN (:docFamTipoCodes) "
			+ "         AND r.dataCancellazione IS NULL "
			+ "     ) "
			+ "     OR EXISTS( "
			+ "         FROM d.siacRDocIva r "
			+ "         WHERE r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) "
			+ "         AND r.dataCancellazione IS NULL "
			+ "     ) "
			+ "     OR EXISTS( "
			+ "         FROM d.siacRSubdocIvasFiglio si "
			+ "         WHERE EXISTS( "
			+ "             FROM si.siacTSubdocIvaPadre.siacRSubdocSubdocIvas r "
			+ "             WHERE r.siacTSubdoc.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) "
			+ "             AND r.dataCancellazione IS NULL "
			+ "         ) "
			+ "         OR EXISTS( "
			+ "             FROM si.siacTSubdocIvaPadre.siacRDocIva r "
			+ "             WHERE r.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode IN(:docFamTipoCodes) "
			+ "             AND r.dataCancellazione IS NULL "
			+ "         ) "
			+ "     ) "
			+ " ) ";
	
	/**
	 * Ottiene la lista di SiacTSubdocIva legati al documento avente l'id passato come parametro.
	 * 
	 * @param docId id del documento.
	 * 
	 * @return la lista di SiacTSubdocIva.
	 */
	@Query("SELECT r.siacTSubdocIvas "
			+ " FROM SiacRDocIva r "
			+ " WHERE r.siacTDoc.docId = :docId "
			+ " AND r.dataCancellazione IS NULL " 
			)
	List<SiacTSubdocIva> findSiacTSubdocIvaByDocId(@Param("docId") Integer docId);

	/**
	 * Cerca un SiacTSubdocIva per id del subdocumento, anno e numero registrazione
	 * @param subdocId uid del documento
	 * @param anno anno del subdocumento iva
	 * @param numero numero del subdocumento iva
	 * 
	 * @return la SiacTSubdocIva trovata
	 */
	@Query(" FROM SiacTSubdocIva s "
			+ " WHERE s.subdocivaAnno = :anno"
			+ " AND s.subdocivaNumero = :numero " 
			+ " AND s.dataCancellazione IS NULL "			
			+ " AND ( "
			+ "	EXISTS(FROM s.siacRSubdocSubdocIvas rs "
			+ " 		WHERE rs.siacTSubdoc.subdocId = :subdocId " 
			+ "   		AND rs.dataCancellazione IS NULL ) " 
			+ " OR " 
			+ " EXISTS(FROM s.siacRDocIva rd " 
			+ " 		WHERE EXISTS (FROM rd.siacTDoc.siacTSubdocs sd " 
			+ "							WHERE  sd.subdocId = :subdocId " 
			+ "							AND sd.dataCancellazione IS NULL) " 	
			+ "			AND rd.dataCancellazione IS NULL)"
			+ " ) "			
			) 
	SiacTSubdocIva findByNumRegistrazioneIva(@Param("subdocId") Integer subdocId, @Param("anno") String anno, @Param("numero")  Integer numero);
	
	/**
	 *  Cerca SiacTSubdocIva filtrando per registro iva e tipo famiglia del documento padre.
	 * 
	 * @param ivaregId uid del registro iva
	 * @param docFamTipoCode codice del tipo famiglia documento
	 * 
	 * @return la lista di SiacTSubdocIva trovata
	 */
	@Query(" FROM SiacTSubdocIva d "
			+ " WHERE d.dataCancellazione IS NULL "
			+ " AND d.siacTIvaRegistro.ivaregId = :ivaregId"
			
			+ SIACTSUBDOCIVA_DOC_FAM_TIPO_FILTER
			) 
	List<SiacTSubdocIva> findByRegistroIva(@Param("ivaregId") Integer ivaregId, @Param("docFamTipoCode") String docFamTipoCode, @Param("docFamTipoCodeIva") String docFamTipoCodeIva);

	@Query(" FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND CAST(tsi.subdocivaProtProv AS integer) >= CAST(CAST(:subdocivaProtProv AS string) AS integer)"
			+ " AND tsi.subdocivaDataProtProv >= :inizioPeriodo "
			+ " AND tsi.subdocivaDataProtProv <= :finePeriodo "
			
//			+ " AND tsi.subdocivaAnno = :subdocivaAnno "
//			+ " AND ( "
//			+ "     :subdocivaProtDef IS NULL "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtDef IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtDef AS integer) >= CAST(CAST(:subdocivaProtDef AS string) AS integer) "
//			+ "     ) "
//			+ " ) "
//			+ " AND ( "
//			+ "     :subdocivaProtProv IS NULL "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtProv IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtProv AS integer) >= CAST(CAST(:subdocivaProtProv AS string) AS integer) "
//			+ "     ) "
//			+ " ) "
			+ " ORDER BY tsi.subdocivaProtProv")
	List<SiacTSubdocIva> findByIvaRegistroAndNumProtocolloProvIvaGreaterThan(@Param("ivaregId") Integer ivaregId, @Param("subdocivaProtProv") Integer subdocivaProtProv, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	@Query(" FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND CAST(tsi.subdocivaProtDef AS integer) >= CAST(CAST(:subdocivaProtDef AS string) AS integer)"
			+ " AND tsi.subdocivaDataProtDef >= :inizioPeriodo "
			+ " AND tsi.subdocivaDataProtDef <= :finePeriodo "
			+ " ORDER BY tsi.subdocivaProtDef")
	List<SiacTSubdocIva> findByIvaRegistroAndNumProtocolloDefIvaGreaterThan(@Param("ivaregId") Integer ivaregId, @Param("subdocivaProtDef") Integer subdocivaProtDef, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	@Query(" FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND CAST(tsi.subdocivaProtProv AS integer) = CAST(CAST(:subdocivaProtProv AS string) AS integer)"
			+ " AND tsi.subdocivaDataProtProv >= :inizioPeriodo "
			+ " AND tsi.subdocivaDataProtProv <= :finePeriodo " )
			
//			+ " AND ( "
//			+ "     (:subdocivaProtDef IS NULL) "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtDef IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtDef AS integer) = CAST(CAST(:subdocivaProtDef AS string) AS integer) "
//			+ "     ) "
//			+ " ) "
//			+ " AND ( "
//			+ "     (:subdocivaProtProv IS NULL) "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtProv IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtProv AS integer) = CAST(CAST(:subdocivaProtProv AS string) AS integer) "
//			+ "     ) "
//			+ " ) ")
	List<SiacTSubdocIva> findByIvaRegistroAndNumProtocolloProv(@Param("ivaregId") Integer ivaregId, @Param("subdocivaProtProv") Integer subdocivaProtProv, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	@Query(" FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND CAST(tsi.subdocivaProtDef AS integer) = CAST(CAST(:subdocivaProtDef AS string) AS integer)"
			+ " AND tsi.subdocivaDataProtDef >= :inizioPeriodo "
			+ " AND tsi.subdocivaDataProtDef <= :finePeriodo " )
			
//			+ " AND ( "
//			+ "     (:subdocivaProtDef IS NULL) "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtDef IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtDef AS integer) = CAST(CAST(:subdocivaProtDef AS string) AS integer) "
//			+ "     ) "
//			+ " ) "
//			+ " AND ( "
//			+ "     (:subdocivaProtProv IS NULL) "
//			+ "     OR ( "
//			+ "         tsi.subdocivaProtProv IS NOT NULL "
//			+ "         AND CAST(tsi.subdocivaProtProv AS integer) = CAST(CAST(:subdocivaProtProv AS string) AS integer) "
//			+ "     ) "
//			+ " ) ")
	List<SiacTSubdocIva> findByIvaRegistroAndNumProtocolloDef(@Param("ivaregId") Integer ivaregId, @Param("subdocivaProtDef") Integer subdocivaProtDef, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	@Query(" FROM SiacTSubdocIva d "
			+ " WHERE d.dataCancellazione IS NULL "
			+ " AND d.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND d.subdocivaDataProtDef >= :inizioPeriodo "
			+ " AND d.subdocivaDataProtDef <= :finePeriodo ") 
	List<SiacTSubdocIva> findByIvaRegistroAndAnnoProtDef(@Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	@Query(" FROM SiacTSubdocIva d "
			+ " WHERE d.dataCancellazione IS NULL "
			+ " AND d.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND d.subdocivaDataProtProv >= :inizioPeriodo "
			+ " AND d.subdocivaDataProtProv <= :finePeriodo ") 
	List<SiacTSubdocIva> findByIvaRegistroAndAnnoProtProv(@Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);
	
	
	@Query(" SELECT COALESCE(COUNT(tor), 0) "
			+ " FROM SiacTOrdinativo tor "
			+ " WHERE tor.dataCancellazione IS NULL "
			+ " AND tor.dataFineValidita IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM tor.siacTOrdinativoTs r1, SiacTOrdinativoT tot "
			+ "     WHERE r1.ordTsId = tot.ordTsId "
			+ "     AND tot.dataCancellazione IS NULL "
			+ "     AND tot.dataFineValidita IS NULL "
			+ "     AND EXISTS ( "
			+ "         FROM tot.siacRSubdocOrdinativoTs r2, SiacTSubdoc ts, SiacTDoc std"
			+ "         WHERE r2.siacTSubdoc.subdocId = ts.subdocId "
			+ " 		AND ts.siacTDoc.docId = std.docId"	
			+ "         AND r2.dataCancellazione IS NULL "
//			+ "         AND std.siacDDocTipo.siacDDocFamTipo.docFamTipoCode NOT IN (:docFamTipoCodes) "
			+ "         AND ( "
			+ "             EXISTS ( "
			+ "                 FROM ts.siacRSubdocSubdocIvas rssi "
			+ "                 WHERE rssi.dataCancellazione IS NULL "
			+ "                 AND rssi.siacTSubdocIva.subdocivaId = :subdocivaId "
			+ "             ) "
			+ "             OR EXISTS ( "
			+ "                 FROM std.siacRDocIvas r3, SiacRDocIva rdi "
			+ "                 WHERE r3.docivaRId = rdi.docivaRId "
			+ "                 AND rdi.dataCancellazione IS NULL "
			+ "                 AND EXISTS ( "
			+ "                     FROM rdi.siacTSubdocIvas tsi "
			+ "                     WHERE tsi.subdocivaId = :subdocivaId "
			+ "                 ) "
			+ "             ) "
			+ "         ) "
			+ "     ) "
			+ " ) ")
	Long countSiacTOrdinativoBySubdocivaId(@Param("subdocivaId")Integer subdocivaId/*, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes*/);
	
	@Query(" SELECT COALESCE(COUNT(tl), 0) "
			+ " FROM SiacTLiquidazione tl "
			+ " WHERE tl.dataCancellazione IS NULL "
			+ " AND tl.dataFineValidita IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM tl.siacRSubdocLiquidaziones r2, SiacTSubdoc ts, SiacTDoc std "
			+ "     WHERE r2.siacTSubdoc.subdocId = ts.subdocId "
			+ " 	AND ts.siacTDoc.docId = std.docId "	
			+ "     AND r2.dataCancellazione IS NULL "
//			+ "     AND ts.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode NOT IN (:docFamTipoCodes) "
			+ "     AND ( "
			+ "         EXISTS ( "
			+ "             FROM ts.siacRSubdocSubdocIvas rssi "
			+ "             WHERE rssi.dataCancellazione IS NULL "
			+ "             AND rssi.siacTSubdocIva.subdocivaId = :subdocivaId "
			+ "         ) "
			+ "         OR EXISTS ( "
			+ "             FROM std.siacRDocIvas r3, SiacRDocIva rdi "
			+ "             WHERE r3.docivaRId = rdi.docivaRId "
			+ "             AND rdi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rdi.siacTSubdocIvas tsi "
			+ "                 WHERE tsi.subdocivaId = :subdocivaId "
			+ "             ) "
			+ "         ) "
			+ "     ) "
			+ " ) ")
	Long countSiacTLiquidazioneBySubdocivaId(@Param("subdocivaId")Integer subdocivaId); //, @Param("docFamTipoCodes") Collection<String> docFamTipoCodes);

	@Query(" SELECT COALESCE(COUNT(tsi), 0) "
			+ " FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tsi.subdocivaProtDef = :subdocivaProtDef "
			+ " AND tsi.subdocivaId <> :subdocivaId ")
	Long countBySubdocivaProtDef(@Param("subdocivaId") Integer subdocivaId, @Param("subdocivaProtDef") String subdocivaProtDef, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT COALESCE(COUNT(tsi), 0) "
			+ " FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tsi.subdocivaProtProv = :subdocivaProtProv "
			+ " AND tsi.subdocivaId <> :subdocivaId ")
	Long countBySubdocivaProtProv(@Param("subdocivaId") Integer subdocivaId, @Param("subdocivaProtProv") String subdocivaProtProv, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query( " SELECT COALESCE(COUNT(si), 0) "
			+ " FROM SiacTSubdocIva si "
			+ " WHERE si.dataCancellazione IS NULL "
			+ " AND si.subdocivaId <> :subdocivaId "
			+ " AND si.siacTIvaRegistro.ivaregId = :ivaregId "
//			+ " AND si.subdocivaAnno = :subdocivaAnno "
			+ " AND si.subdocivaDataProtDef >= :inizioPeriodo "
			+ " AND si.subdocivaDataProtDef <= :finePeriodo "
			+ " AND si.subdocivaProtDef = :subdocivaProtDef "
			+ " AND NOT EXISTS ( FROM si.siacRSubdocIvasFiglio sif "
			+ " 				  WHERE sif.dataCancellazione IS NULL "	
			+ " 				  AND sif.siacTSubdocIvaPadre.subdocivaId = :subdocivaId "
			+ " 				  AND sif.siacDRelazTipo.relazTipoCode = 'QPID' "		
			+ "					)"
			+ " AND NOT EXISTS ( FROM si.siacRSubdocIvasPadre sip "
			+ " 				  WHERE sip.dataCancellazione IS NULL "	
			+ " 				  AND sip.siacTSubdocIvaFiglio.subdocivaId= :subdocivaId "
			+ " 				  AND sip.siacDRelazTipo.relazTipoCode = 'QPID' "		
			+ "					)"
			) 
	Long countByRegistroIvaEProtDefNonCollegatoComeQuotaIvaDifferita(@Param("subdocivaId")Integer subdocivaId, @Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo, @Param("subdocivaProtDef") String subdocivaProtDef);
	
	@Query( " SELECT COALESCE(COUNT(si), 0) "
			+ "FROM SiacTSubdocIva si "
			+ " WHERE si.dataCancellazione IS NULL "
			+ " AND si.subdocivaId <> :subdocivaId "
			+ " AND si.siacTIvaRegistro.ivaregId = :ivaregId "
//			+ " AND si.subdocivaAnno = :subdocivaAnno "
			+ " AND si.subdocivaDataProtProv >= :inizioPeriodo "
			+ " AND si.subdocivaDataProtProv <= :finePeriodo "
			+ " AND si.subdocivaProtProv = :subdocivaProtProv "
			+ " AND NOT EXISTS ( FROM si.siacRSubdocIvasFiglio sif "
			+ " 				  WHERE sif.dataCancellazione IS NULL "	
			+ " 				  AND sif.siacTSubdocIvaPadre.subdocivaId = :subdocivaId "
			+ " 				  AND sif.siacDRelazTipo.relazTipoCode = 'QPID' "		
			+ "					)"
			+ " AND NOT EXISTS ( FROM si.siacRSubdocIvasPadre sip "
			+ " 				  WHERE sip.dataCancellazione IS NULL "	
			+ " 				  AND sip.siacTSubdocIvaFiglio.subdocivaId= :subdocivaId "
			+ " 				  AND sip.siacDRelazTipo.relazTipoCode = 'QPID' "		
			+ "					)"
			) 
	Long countByRegistroIvaEProtProvNonCollegatoComeQuotaIvaDifferita(@Param("subdocivaId")Integer subdocivaId, @Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo") Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo, @Param("subdocivaProtProv") String subdocivaProtProv);
	
	@Query(" SELECT COALESCE(COUNT(tsi), 0) "
			+ " FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND tsi.subdocivaDataProtDef >= :inizioPeriodo "
			+ " AND tsi.subdocivaDataProtDef <= :finePeriodo  ")
	Long countByUidRegistroAndInizioFinePeriodoProtDef(@Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo")  Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT COALESCE(COUNT(tsi), 0) "
			+ " FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND tsi.subdocivaDataProtProv >= :inizioPeriodo  "
			+ " AND tsi.subdocivaDataProtProv <= :finePeriodo  ")
	Long countByUidRegistroAndInizioFinePeriodoProtProv(@Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo")  Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(" SELECT d.subdocivaDataProtProv"
			+ " FROM SiacTSubdocIva d "
			+ " WHERE d.subdocivaId = :subdocivaId ") 
	Date findDataProtoProv(@Param("subdocivaId")Integer subdocivaId);

	@Query(" SELECT d.subdocivaDataProtDef"
			+ " FROM SiacTSubdocIva d "
			+ " WHERE d.subdocivaId = :subdocivaId ") 
	Date findDataProtoDef(@Param("subdocivaId")Integer subdocivaId);

	@Query(" FROM SiacTSubdocIva tsi "
			+ " WHERE tsi.dataCancellazione IS NULL "
			+ " AND tsi.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND tsi.subdocivaDataProtDef >= :inizioPeriodo  "
			+ " AND tsi.subdocivaDataProtDef <= :finePeriodo  "
			+ " AND tsi.subdocivaNumordinativodoc IS NOT NULL"
			+ " AND EXISTS ( FROM tsi.siacRSubdocIvaStatos s "
			+ "			     WHERE s.dataCancellazione IS NULL "
			+ "				 AND s.siacDSubdocIvaStato.subdocivaStatoCode = 'PD')" )
	List<SiacTSubdocIva> findSubdocIvaDiffPagatoByRegistroEPeriodo(@Param("ivaregId") Integer ivaregId, @Param("inizioPeriodo")  Date inizioPeriodo, @Param("finePeriodo") Date finePeriodo);

	@Query( " SELECT riva.siacTSubdocIva "
			+ " FROM SiacRSubdocSubdocIva riva "
			+ " WHERE riva.dataCancellazione IS NULL "
			+ " AND riva.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND riva.siacTSubdocIva.dataCancellazione IS NULL "
			+ " AND riva.siacTSubdoc.siacTDoc.dataCancellazione IS NULL "
			+ " AND riva.siacTSubdoc.siacTDoc.docId = :docId "
			+ " AND riva.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			)
	List<SiacTSubdocIva> findSiacTSubdocIvaWithSiacTSubdocByDocId(@Param("docId") Integer docId, @Param("enteProprietarioId") Integer enteProprietarioId);

}
