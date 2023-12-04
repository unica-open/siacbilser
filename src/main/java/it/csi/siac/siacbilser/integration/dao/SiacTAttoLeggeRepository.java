/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoLeggeTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTAttoLeggeRepository.
 */
public interface SiacTAttoLeggeRepository extends
		JpaRepository<SiacTAttoLegge, Integer> {
		
	/**
	 * Ricerca atto.
	 *
	 * @param enteDto the ente dto
	 * @param anno the anno
	 * @param comma the comma
	 * @param articolo the articolo
	 * @param numero the numero
	 * @param punto the punto
	 * @param tipoAtto the tipo atto
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("FROM SiacTAttoLegge atto "
			+ "WHERE  atto.siacTEnteProprietario = :ente "
			+ "       AND atto.dataCancellazione IS NULL "
			//+ "       AND atto.loginOperazione = :loginOperazione "
			+ "       AND (( :anno = '' OR :anno IS NULL ) "
			+ "              OR atto.attoleggeAnno = :anno ) "
			+ "       AND (( :comma = '' OR :comma IS NULL ) "
			+ "              OR atto.attoleggeComma = :comma ) "
			+ "       AND (( :articolo = '' OR :articolo IS NULL ) "
			+ "              OR atto.attoleggeArticolo = :articolo ) "
			+ "       AND (( :numero IS NULL ) "
			+ "              OR atto.attoleggeNumero = :numero ) "
			+ "       AND (( :tipoAtto = 0) "
			+ "              OR atto.siacDAttoLeggeTipo.attoleggeTipoId = :tipoAtto ) "
			+ "       AND (( :punto = '' OR :punto IS NULL ) "
			+ "              OR atto.attoleggePunto = :punto ) "
			//+ " ORDER BY atto.attoleggeAnno, atto.attoleggeNumero " //NON si può fare qui. va fatto come attributo in Pageable
			)
	Page<SiacTAttoLegge> ricercaAtto(@Param("ente") SiacTEnteProprietario enteDto, @Param("anno") String anno,
			@Param("comma") String comma, @Param("articolo") String articolo, @Param("numero") Integer numero, 
			@Param("punto") String punto, @Param("tipoAtto") int tipoAtto, /*@Param("loginOperazione") String loginOperazione,*/ Pageable pageable);

		
	/**
	 * Ricerca tipo atto.
	 *
	 * @param codice the codice
	 * @return the list
	 */
	@Query("FROM SiacDAttoLeggeTipo tipoAtto "
			+ "WHERE  tipoAtto.attoleggeTipoCode = :codice "
			+ "AND tipoAtto.dataCancellazione IS NULL ")
	List<SiacDAttoLeggeTipo> ricercaTipoAtto(@Param("codice") String codice);
	
}
