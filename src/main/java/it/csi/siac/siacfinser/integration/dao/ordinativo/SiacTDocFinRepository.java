/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDDocFamTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;

/**
 * Repository per l'entity SiacTDoc.
 *
 */
public interface SiacTDocFinRepository extends JpaRepository<SiacTDocFin, Integer> {

//	@Query(" FROM SiacTDoc d  "
//			+ " WHERE d.docId = :docId "
//			+ " AND d.dataCancellazione IS NULL "
//			+ " AND EXISTS (FROM d.siacTSubdocs sd "
//			+ "            WHERE sd.dataCancellazione IS NULL "
//			+ "            AND EXISTS (FROM sd.siacRElencoDocSubdocs se "
//			+ "                        WHERE se.dataCancellazione IS NULL "
//			+ "             		   AND EXISTS (FROM se.siacTElencoDoc.siacRAttoAllegatoElencoDocs ae "
//			+ " 								   WHERE ae.dataCancellazione IS NULL "	
//			+ " 								   AND EXISTS (FROM ae.siacTAttoAllegato aa"
//			+ " 												WHERE aa.dataCancellazione IS NULL "
//			+ "                          					   )"
//			+ "                          		   )"
//			+ "                         )"
//			+ "              )")
//	public SiacTDoc findDocCollegatoAdAllegatoAtto(@Param("docId") Integer uidDoc);

	
	@Query(" FROM SiacRDocStato r where r.siacTDoc.docId = :docId ")
	public List<SiacRDocStatoFin> findSiacRDocStatos(@Param("docId") Integer docId);
	
	@Query(" SELECT dt.siacDDocFamTipo "
			+ "	FROM SiacDDocTipoFin dt"
			+ " WHERE dt.docTipoId = :docTipoId"
			+ "	AND dt.dataCancellazione IS NULL"
			+ ") ")
	public SiacDDocFamTipoFin findFamTipoByDocTipo(@Param("docTipoId") Integer docTipoId);

//	@Query( //" SELECT COUNT (r) " +
//			" FROM SiacRDocOnere r "
//			+ " WHERE r.dataCancellazione IS NULL "
//			+ " AND r.siacTDoc.dataCancellazione IS NULL "
//			+ " AND EXISTS ( "
//			+ "     FROM r.siacTDoc.siacTSubdocs sd, SiacTSubdoc s "
//			+ "     WHERE sd.dataCancellazione IS NULL "
//			+ "     AND sd = s "
//			+ "     AND EXISTS ( "
//			+ "         FROM s.siacRSubdocLiquidaziones rsl, SiacTLiquidazione l "
//			+ "         WHERE rsl.dataCancellazione IS NULL "
//			+ "         AND rsl.siacTLiquidazione = l "
//			+ "         AND EXISTS ( "
//			+ "             FROM l.siacRLiquidazioneOrds rlo "
//			+ "             WHERE rlo.dataCancellazione IS NULL "
//			+ "             AND rlo.siacTOrdinativoT.ordTsId = :ordTsId "
//			+ "         ) "
//			+ "     ) "
//			+ " ) "
//			)
//	public List<SiacRDocOnere> /*Long*/ findSiacRDocOnereByOrdTsIs(@Param("ordTsId") Integer ordTsId);
//
//	@Query(" SELECT COALESCE(SUM(s.subdocImporto), 0) "
//			+ " FROM SiacTSubdoc s "
//			+ " WHERE s.siacTDoc.docId = :docId ")
//	public BigDecimal sumSubdocImportoByDocId(@Param("docId") Integer docId);
//
//	@Query(" SELECT COUNT(s) "
//			+ " FROM SiacTSubdoc s "
//			+ " WHERE s.siacTDoc.docId = :docId "
//			+ " AND s.dataCancellazione IS NULL")
//	public Long countSiacTSubdocByDocId(@Param("docId") Integer docId);
//	
//	@Query(" SELECT s.siacTDoc "
//			+ " FROM SiacTSubdoc s "
//			+ " WHERE s.subdocId = :subdocId ")
//	public SiacTDoc findBySubdocId(@Param("subdocId") Integer subdocId);
	
}
