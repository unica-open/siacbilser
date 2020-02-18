/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoSog;

/**
 * Repository per l'entity SiacRAttoAllegatoSog.
 *
 */
public interface SiacRAttoAllegatoSogRepository extends JpaRepository<SiacRAttoAllegatoSog, Integer> {
	
	@Query(" FROM SiacRAttoAllegatoSog r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND r.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND r.siacTSoggetto.soggettoId = :soggettoId "
			+ " AND r.siacTSoggetto.dataCancellazione IS NULL ")
	List<SiacRAttoAllegatoSog> findBySiacTAttoAllegatoAndSiacTSoggetto(@Param("attoalId") Integer attoalId, @Param("soggettoId") Integer soggettoId);

	@Query(" FROM SiacRAttoAllegatoSog r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND r.siacTAttoAllegato.dataCancellazione IS NULL ")
	List<SiacRAttoAllegatoSog> findBySiacTAttoAllegato(@Param("attoalId") Integer attoalId);
	
	
	/**
	 * Ottiene i dati di SiacRAttoAllegatoSog dato un allegato atto e il soggetto di un subdocId.
	 * 
	 * @param attoalId
	 * @param subdocId
	 * @return
	 */
	@Query(" SELECT r " 
			+ " FROM SiacRAttoAllegatoSog r, SiacTAttoAllegato aa "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTAttoAllegato = aa"
			+ " AND aa.attoalId = :attoalId "
			+ " AND aa.dataCancellazione IS NULL "
			+ " AND r.siacTSoggetto.dataCancellazione IS NULL "
			
			// AND il soggetto e' quello del subdocumento (identificato da subdocId)
			+ " AND r.siacTSoggetto IN (SELECT rds.siacTSoggetto  "
			+ "                         FROM SiacRDocSog rds, SiacTDoc d "
			+ "                         WHERE rds.dataCancellazione IS NULL"
			+ "                         AND rds.siacTDoc = d "
			+ "                         AND EXISTS (FROM d.siacTSubdocs ss "
			+ "                                       WHERE ss.dataCancellazione IS NULL "
			+ "                                       AND ss.subdocId = :subdocId "
			+ "                                      ) "
			+ "                        ) "
			
			// AND il subdocumento (identificato da subdocId) e' all'interno di uno degli elenchi dell'allegato atto
			+ " AND EXISTS (FROM aa.siacRAttoAllegatoElencoDocs raaed, SiacTElencoDoc ed "
			+ "             WHERE raaed.dataCancellazione IS NULL "
			+ "             AND raaed.siacTElencoDoc = ed "
			+ "             AND EXISTS (FROM ed.siacRElencoDocSubdocs reds "
			+ "                         WHERE reds.dataCancellazione IS NULL "
			+ "                         AND reds.siacTSubdoc.subdocId = :subdocId "
			+ "                        )"
			+ "            )"
			)
	SiacRAttoAllegatoSog findByAttoAllegatoAndSubdocId(@Param("attoalId") Integer attoalId, @Param("subdocId") Integer subdocId);
	
	@Query(" SELECT DISTINCT r.attoalSogCausaleSosp, r.attoalSogDataRiatt, r.attoalSogDataSosp"
	       + " FROM SiacRAttoAllegatoSog r "                                                     
	       + " WHERE r.dataCancellazione IS NULL "                                               
	       + " AND r.siacTAttoAllegato.attoalId = :attoalId"                                     
        )             
	List<Object[]> findDatiSospensioneByAllegatoAtto(@Param("attoalId") Integer attoalId);
		
	@Query( " SELECT DISTINCT r.attoalSogCausaleSosp, r.attoalSogDataRiatt, r.attoalSogDataSosp"
				+ " FROM SiacRAttoAllegatoSog r, SiacTAttoAllegato aa "
				+ " WHERE r.siacTAttoAllegato = aa"
				+ " AND r.siacTSoggetto in (" 
				+ " SELECT DISTINCT rds.siacTSoggetto.soggettoId "
				+ " FROM SiacRDocSog rds, SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
				+ " WHERE rds.siacTDoc = reds.siacTSubdoc.siacTDoc "
				+ " AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
				+ " AND rds.dataCancellazione IS NULL "
				+ " AND reds.dataCancellazione IS NULL"
				+ " AND raaed.dataCancellazione IS NULL "
				+ " AND rds.siacTSoggetto.dataCancellazione IS NULL "
				+ " AND rds.siacTDoc.dataCancellazione IS NULL "
				+ " AND reds.siacTSubdoc.dataCancellazione IS NULL "
				+ " AND reds.siacTElencoDoc.eldocId = :eldocId "
				+ " AND reds.siacTElencoDoc.dataCancellazione IS NULL "
				+ " AND raaed.siacTAttoAllegato= aa "
				 + " ) "
		)
	List<Object[]> findDatiSospensioneAllegatoByElenco(@Param("eldocId") Integer eldocId /*,@Param("enteProprietarioId") Integer enteProprietarioId*/);
			
	
}
