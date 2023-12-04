/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;

public interface SiacTComuneRepository extends JpaRepository<SiacTComuneFin, Integer>{
	
	
	String condizione = " ( (c.dataInizioValidita < :dataInput)  AND (c.dataFineValidita IS NULL OR :dataInput < c.dataFineValidita) AND c.dataCancellazione IS NULL ) ";

	
	//@Query("from SiacDSoggettoTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dataFineValidita IS NULL")
	//@Query("FROM SiacTComuneFin WHERE SiacTComuneFin LIKE :descrizione AND dataFineValidita IS NULL ")
		
    //@Query(QUERY)  
	//public List<SiacTComuneFin> findListaComuni(@Param("descrizione") String descrizioneComune);

	// public static String QUERY = "FROM SiacTComuneFin WHERE SiacTComuneFin LIKE CONCAT(:descrizione, '%') "
	//	       + "AND dataFineValidita IS NULL";
	
	
	
	@Query("Select c FROM SiacTComuneFin c "
	           + "LEFT OUTER JOIN c.siacRComuneProvincias r "
               + "LEFT OUTER JOIN r.siacTProvincia "
		       + "WHERE (:descrizione IS NULL OR :descrizione = '' OR " +
		       " UPPER(c.comuneDesc) LIKE CONCAT(UPPER(:descrizione), '%') )"
	           + "AND ( :codiceCatastale IS NULL OR :codiceCatastale = '' OR " +
	           " c.codiceCatastale = :codiceCatastale ) " +
	           "AND ( :codNazione IS NULL OR :codNazione = '' " +
	           " OR c.siacTNazione.nazioneCode = :codNazione ) "  +
	           " and c.dataCancellazione IS NULL "
	           + "AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId  ORDER BY c.comuneDesc")  
	public List<SiacTComuneFin> findListaComuni(@Param("descrizione") String descrizioneComune,
											 @Param("codNazione") String codiceNazione,
											 @Param("codiceCatastale") String codiceCatastale,
											 @Param("enteProprietarioId") Integer idEnte);
	
	@Query("FROM SiacTComuneFin c WHERE UPPER(comuneDesc) LIKE (UPPER(:descrizione)) AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND c.siacTNazione.nazioneCode = :codNazione and c.dataCancellazione IS NULL ")
	public List<SiacTComuneFin> findComuneByNomeLike(@Param("descrizione") String descrizioneComune,
										@Param("codNazione") String codiceNazione,
										@Param("enteProprietarioId") Integer idEnte);
	
	@Query("FROM SiacTComuneFin c WHERE UPPER(comuneDesc) = (UPPER(:descrizione)) AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND c.siacTNazione.nazioneCode = :codNazione and c.dataCancellazione IS NULL ")
	public List<SiacTComuneFin> findComuneByNomePuntuale(@Param("descrizione") String descrizioneComune,
										@Param("codNazione") String codiceNazione,
										@Param("enteProprietarioId") Integer idEnte);
	
	@Query("FROM SiacTComuneFin c "
		+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
		+ " AND c.comuneIstatCode = :code "
		+ " AND c.siacTNazione.nazioneCode = '1' " // SIAC-8252
		+ " AND " + condizione 
		+ " ORDER by c.dataModifica DESC ") // SIAC-8252
	public List<SiacTComuneFin> findComuneItalianoValidoByCodiceIstat(
			@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("code") String code,
			@Param("dataInput") Timestamp  dataInput
	);



}
