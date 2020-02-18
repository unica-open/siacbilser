/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;

/**
 * The Interface SiacTAccFondiDubbiaEsigRepository.
 */
public interface SiacTAccFondiDubbiaEsigRepository extends JpaRepository<SiacTAccFondiDubbiaEsig, Integer> {
	
	/**
	 * Conta le occorrenze di SiacTAccFondiDubbiaEsig collegate a un dato elemId
	 * @param elemId l'elemId da utilizzare come filtro
	 * @return il numero delle istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" SELECT COALESCE(COUNT(tafde), 0) "
			+ " FROM SiacTAccFondiDubbiaEsig tafde "
			+ " WHERE tafde.dataCancellazione IS NULL "
			+ " AND tafde.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tafde.siacRBilElemAccFondiDubbiaEsigs rbeafde "
			+ "     WHERE rbeafde.dataCancellazione IS NULL "
			+ "     AND rbeafde.dataFineValidita IS NULL "
			+ "     AND rbeafde.siacTBilElem.elemId = :elemId "
			+ " ) ")
	Long countByElemId(@Param("elemId") Integer elemId);
	
	/**
	 * Ottiene le occorrenze di SiacTAccFondiDubbiaEsig collegate a un dato elemId
	 * @param elemId l'elemId da utilizzare come filtro
	 * @return le istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" FROM SiacTAccFondiDubbiaEsig tafde "
			+ " WHERE tafde.dataCancellazione IS NULL "
			+ " AND tafde.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tafde.siacRBilElemAccFondiDubbiaEsigs rbeafde "
			+ "     WHERE rbeafde.dataCancellazione IS NULL "
			+ "     AND rbeafde.dataFineValidita IS NULL "
			+ "     AND rbeafde.siacTBilElem.elemId = :elemId "
			+ " ) ")
	List<SiacTAccFondiDubbiaEsig> findByElemId(@Param("elemId") Integer elemId);
	
	/**
	 * Ottiene le occorrenze di SiacTAccFondiDubbiaEsig collegate a un dato bilId
	 * @param bilId il bilId da utilizzare come filtro
	 * @return le istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" FROM SiacTAccFondiDubbiaEsig tafde "
			+ " WHERE tafde.dataCancellazione IS NULL "
			+ " AND tafde.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tafde.siacRBilElemAccFondiDubbiaEsigs rbeafde "
			+ "     WHERE rbeafde.dataCancellazione IS NULL "
			+ "     AND rbeafde.dataFineValidita IS NULL "
			+ "     AND rbeafde.siacTBilElem.siacTBil.bilId = :bilId "
			+ "     AND rbeafde.siacTBilElem.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " ) ")
	List<SiacTAccFondiDubbiaEsig> findByBilIdAndElemTipoCode(@Param("bilId") Integer bilId, @Param("elemTipoCode") String elemTipoCode);
	
	/**
	 * Ottiene le occorrenze di SiacTAccFondiDubbiaEsig collegate a un dato bilId
	 * @param bilId il bilId da utilizzare come filtro
	 * @return le istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" SELECT COALESCE(COUNT(rbeafde), 0) "
			+ " FROM SiacRBilElemAccFondiDubbiaEsig rbeafde "
			+ " WHERE rbeafde.dataCancellazione IS NULL "
			+ " AND rbeafde.dataFineValidita IS NULL "
			+ " AND rbeafde.siacTBilElem.siacTBil.bilId = :bilId "
			+ " AND rbeafde.siacTBilElem.siacDBilElemTipo.elemTipoCode = :elemTipoCode ")
	Long countSiacRBilElemAccFondiDubbiaEsigByBilIdAndElemTipoCode(@Param("bilId") Integer bilId, @Param("elemTipoCode") String elemTipoCode);
}
