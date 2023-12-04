/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;

/**
 * The Interface SiacTIvaRegistroRepository.
 */
public interface SiacTIvaRegistroRepository extends JpaRepository<SiacTIvaRegistro, Integer> {
	
	/**
	 * Ricerca un registro iva per codice, ente proprioetario e gruppo iva.
	 * 
	 * @param ivaregCode codice del registro iva
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivagruId uid del gruppo iva
	 * 
	 * @return siacTIvaRegistro
	 */
	@Query( " SELECT r " +
			" FROM SiacTIvaRegistro r " +
			" WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" +
			" AND r.ivaregCode = :ivaregCode " +
			" AND r.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			" 		FROM r.siacRIvaRegistroGruppos rg   " +
			"		WHERE rg.siacTIvaGruppo.ivagruId = :ivagruId" +
			"		AND rg.dataCancellazione IS NULL" +
			" 	) " )
	SiacTIvaRegistro findByCodice( @Param("ivaregCode") String ivaregCode, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("ivagruId") Integer ivagruId);

	
	/**
	 * Ricerca registri iva per ente proprietario, gruppo iva e codice del tipo registro.
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivagruId uid del gruppo iva
	 * @param ivaregTipoCode codice del tipo registro iva
	 * 
	 * @return la lista di siacTIvaRegistro
	 */
	@Query( " SELECT r " +
			" FROM SiacTIvaRegistro r " +
			" WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND r.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			" 	FROM r.siacRIvaRegistroGruppos rg " +
			" 	WHERE rg.siacTIvaGruppo.ivagruId = :ivagruId" +
			" 	AND rg.dataCancellazione IS NULL " +
			" ) " +
			" AND r.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
			" AND r.siacDIvaRegistroTipo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	List<SiacTIvaRegistro> findBySiacTEnteProprietarioAndSiacTIvaGruppoAndSiacDIvaTipo(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode);
	
	
	/**
	 * Ricerca registri iva per ente proprietarioe e gruppo iva.
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivagruId uid del gruppo iva
	 * 
	 * @return la lista di siacTIvaRegistro
	 */
	@Query( " SELECT r " +
			" FROM SiacTIvaRegistro r " +
			" WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND r.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			" 	FROM r.siacRIvaRegistroGruppos rg " +
			" 	WHERE rg.siacTIvaGruppo.ivagruId = :ivagruId" +
			" 	AND rg.dataCancellazione IS NULL " +
			" ) " +
			" AND r.siacDIvaRegistroTipo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" ORDER BY r.ivaregId ")
	List<SiacTIvaRegistro> findBySiacTEnteProprietarioAndSiacTIvaGruppo(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivagruId") Integer ivagruId);

	@Query(" SELECT COALESCE(MAX(tisv.ultimonumprotocollodef), '0'), COALESCE(MAX(tisv.ultimonumprotocolloprovv), '0') "
			+ " FROM SiacTIvaStampaValore tisv, SiacTIvaStampa tis "
			+ " WHERE tisv.dataCancellazione IS NULL "
			+ " AND tisv.siacTIvaStampa = tis "
			+ " AND tis.dataCancellazione IS NULL "
			+ " AND tis.siacTPeriodo.anno = :anno "
			+ " AND EXISTS ( "
			+ "     FROM tis.siacRIvaStampaRegistros risr "
			+ "     WHERE risr.dataCancellazione IS NULL "
			+ "     AND risr.siacTIvaRegistro.ivaregId = :ivaregId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tis.siacRIvaStampaStatos riss "
			+ "     WHERE riss.dataCancellazione IS NULL "
			+ "     AND riss.siacDIvaStampaStato.ivastStatoCode = 'D' "
			+ " ) ")
	Object[] findSiacTIvaStampaValoreByIvaRegistro(@Param("ivaregId") Integer ivaregId, @Param("anno") String anno);
	
	@Query(" SELECT COALESCE(COUNT(tri), 0) "
			+ " FROM SiacTIvaRegistro tir "
			+ " WHERE tir.dataCancellazione IS NULL "
			+ " AND tir.ivaregFlagbloccato = :ivaregFlagbloccato "
			+ " AND EXISTS ( "
			+ "     FROM tir.siacTSubdocIvas r, SiacTSubdocIva tsi "
			+ "     WHERE r = tsi "
			+ "     AND tsi.dataCancellazione IS NULL "
			+ "     AND ( "
			+ "         EXISTS ( "
			+ "             FROM tsi.siacRSubdocSubdocIvas rssi "
			+ "             WHERE rssi.dataCancellazione IS NULL "
			+ "             AND rssi.siacTSubdoc.subdocId IN (:subdocIds) "
			+ "         ) "
			+ "         OR EXISTS ( "
			+ "             FROM tsi.siacRDocIva rdi "
			+ "             WHERE rdi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rdi.siacTDoc.siacTSubdocs ts "
			+ "                 WHERE ts.dataCancellazione IS NULL "
			+ "                 AND ts.subdocId IN (:subdocIds) "
			+ "             ) "
			+ "         ) "
			+ "     ) "
			+ " ) ")
	Long oountBySubdocIdsAndIvaregFlagbloccato(@Param("subdocIds") Collection<Integer> subdocIds, @Param("ivaregFlagbloccato") Boolean ivaregFlagbloccato);
	
	@Query(" SELECT COALESCE(COUNT(tri), 0) "
			+ " FROM SiacTIvaRegistro tir "
			+ " WHERE tir.dataCancellazione IS NULL "
			+ " AND tir.ivaregFlagbloccato = :ivaregFlagbloccato "
			+ " AND EXISTS ( "
			+ "     FROM tir.siacTSubdocIvas r, SiacTSubdocIva tsi "
			+ "     WHERE r = tsi "
			+ "     AND tsi.dataCancellazione IS NULL "
			+ "     AND ( "
			+ "         EXISTS ( "
			+ "             FROM tsi.siacRSubdocSubdocIvas rssi "
			+ "             WHERE rssi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rssi.siacTSubdoc.siacRElencoDocSubdocs reds"
			+ "                 WHERE reds.dataCancellazione IS NULL "
			+ "                 AND reds.siacTElencoDoc.eldocId IN (:eldocIds) "
			+ "             ) "
			+ "         ) "
			+ "         OR EXISTS ( "
			+ "             FROM tsi.siacRDocIva rdi "
			+ "             WHERE rdi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rdi.siacTDoc.siacTSubdocs ts "
			+ "                 WHERE ts.dataCancellazione IS NULL "
			+ "                 AND EXISTS ( "
			+ "                     FROM ts.siacRElencoDocSubdocs reds"
			+ "                     WHERE reds.dataCancellazione IS NULL "
			+ "                     AND reds.siacTElencoDoc.eldocId IN (:eldocIds) "
			+ "                 ) "
			+ "             ) "
			+ "         ) "
			+ "     ) "
			+ " ) ")
	Long oountByEldocIdsAndIvaregFlagbloccato(@Param("eldocIds") Collection<Integer> eldocIds, @Param("ivaregFlagbloccato") Boolean ivaregFlagbloccato);
	
	@Query(" SELECT COALESCE(COUNT(tri), 0) "
			+ " FROM SiacTIvaRegistro tir "
			+ " WHERE tir.dataCancellazione IS NULL "
			+ " AND tir.ivaregFlagbloccato = :ivaregFlagbloccato "
			+ " AND EXISTS ( "
			+ "     FROM tir.siacTSubdocIvas r, SiacTSubdocIva tsi "
			+ "     WHERE r = tsi "
			+ "     AND tsi.dataCancellazione IS NULL "
			+ "     AND ( "
			+ "         EXISTS ( "
			+ "             FROM tsi.siacRSubdocSubdocIvas rssi "
			+ "             WHERE rssi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rssi.siacTSubdoc.siacRElencoDocSubdocs r2, SiacRElencoDocSubdoc reds "
			+ "                 WHERE r2 = reds "
			+ "                 AND reds.dataCancellazione IS NULL "
			+ "                 AND EXISTS ( "
			+ "                     FROM reds.siacTElencoDoc.siacRAttoAllegatoElencoDocs raaed "
			+ "                     WHERE raaed.dataCancellazione IS NULL "
			+ "                     AND raaed.siacTAttoAllegato.attoalId IN (:attoalIds) "
			+ "                 ) "
			+ "             ) "
			+ "         ) "
			+ "         OR EXISTS ( "
			+ "             FROM tsi.siacRDocIva rdi "
			+ "             WHERE rdi.dataCancellazione IS NULL "
			+ "             AND EXISTS ( "
			+ "                 FROM rdi.siacTDoc.siacTSubdocs ts "
			+ "                 WHERE ts.dataCancellazione IS NULL "
			+ "                 AND EXISTS ( "
			+ "                     FROM ts.siacRElencoDocSubdocs r2, SiacRElencoDocSubdoc reds "
			+ "                     WHERE reds.dataCancellazione IS NULL "
			+ "                     AND EXISTS ( "
			+ "                         FROM reds.siacTElencoDoc.siacRAttoAllegatoElencoDocs raaed "
			+ "                         WHERE raaed.dataCancellazione IS NULL "
			+ "                         AND raaed.siacTAttoAllegato.attoalId IN (:attoalIds) "
			+ "                     ) "
			+ "                 ) "
			+ "             ) "
			+ "         ) "
			+ "     ) "
			+ " ) ")
	Long oountByAttoalIdsAndIvaregFlagbloccato(@Param("attoalIds") Collection<Integer> attoalIds, @Param("ivaregFlagbloccato") Boolean ivaregFlagbloccato);
}
