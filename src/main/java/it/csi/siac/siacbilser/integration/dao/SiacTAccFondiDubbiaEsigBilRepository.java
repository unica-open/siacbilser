/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;

/**
 * The Interface SiacTAccFondiDubbiaEsigBilRepository.
 */
public interface SiacTAccFondiDubbiaEsigBilRepository extends JpaRepository<SiacTAccFondiDubbiaEsigBil, Integer> {
	
	
	/**
	 * Find by bil id and afde tipo code.
	 *
	 * @param bilId the bil id
	 * @param afdeTipoCode the afde tipo code
	 * @return the list
	 */
	@Query(" SELECT tafdeb "
			+ " FROM SiacTAccFondiDubbiaEsigBil tafdeb, SiacTAccFondiDubbiaEsigBil tafdebCurrent "
			+ " WHERE tafdeb.dataCancellazione IS NULL "
			+ " AND tafdeb.siacTEnteProprietario = tafdebCurrent.siacTEnteProprietario "
			+ " AND tafdebCurrent.afdeBilId = :afdeBilId "
			+ " AND tafdeb.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode "
			+ " AND CAST(tafdeb.siacTBil.siacTPeriodo.anno AS int) < CAST(tafdebCurrent.siacTBil.siacTPeriodo.anno AS int) ")
	Page<SiacTAccFondiDubbiaEsigBil> findPreviousByAfdeTipoCode(@Param("afdeBilId") Integer afdeBilId, @Param("afdeTipoCode") String afdeTipoCode, Pageable pageable);
	
	/**
	 * Find by bil id and afde tipo code.
	 *
	 * @param bilId the bil id
	 * @param afdeTipoCode the afde tipo code
	 * @return the list
	 */
	@Query(" SELECT tafdeb "
			+ " FROM SiacTAccFondiDubbiaEsigBil tafdeb, SiacTAccFondiDubbiaEsigBil tafdebCurrent "
			+ " WHERE tafdeb.dataCancellazione IS NULL "
			+ " AND tafdeb.siacTEnteProprietario = tafdebCurrent.siacTEnteProprietario "
			+ " AND tafdebCurrent.afdeBilId = :afdeBilId "
			+ " AND tafdeb.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode "
			+ " AND CAST(tafdeb.siacTBil.siacTPeriodo.anno AS int) <= CAST(tafdebCurrent.siacTBil.siacTPeriodo.anno AS int) ")
	Page<SiacTAccFondiDubbiaEsigBil> findPreviousOrCurrentByAfdeTipoCode(@Param("afdeBilId") Integer afdeBilId, @Param("afdeTipoCode") String afdeTipoCode, Pageable pageable);
	
	/**
	 * Find by bil id and afde tipo code.
	 *
	 * @param bilId the bil id
	 * @param afdeTipoCode the afde tipo code
	 * @return the list
	 */
	//SIAC-8851
	@Query(" SELECT tafdeb "
			+ " FROM SiacTAccFondiDubbiaEsigBil tafdeb, SiacTAccFondiDubbiaEsigBil tafdebCurrent "
			+ " WHERE tafdeb.dataCancellazione IS NULL "
			+ " AND tafdeb.siacTEnteProprietario = tafdebCurrent.siacTEnteProprietario "
			+ " AND tafdebCurrent.afdeBilId = :afdeBilId "
			+ " AND tafdeb.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode "
			+ " AND tafdeb.siacDAccFondiDubbiaEsigStato.afdeStatoCode = :afdeStatoCode "
			+ " AND CAST(tafdeb.siacTBil.siacTPeriodo.anno AS int) = CAST(tafdebCurrent.siacTBil.siacTPeriodo.anno AS int) ")
	Page<SiacTAccFondiDubbiaEsigBil> findPreviousByAfdeTipoCodeGestione(@Param("afdeBilId") Integer afdeBilId, @Param("afdeTipoCode") String afdeTipoCode, @Param("afdeStatoCode") String afdeStatoCode, Pageable pageable);
	
}
