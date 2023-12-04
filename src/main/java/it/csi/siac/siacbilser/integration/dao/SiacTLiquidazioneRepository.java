/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;

/**
 * Repository per l'entity SiacTLiquidazione.
 *
 */
public interface SiacTLiquidazioneRepository extends JpaRepository<SiacTLiquidazione, Integer> {
	
	@Query(" FROM SiacTLiquidazione l "
			+ " WHERE l.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM l.siacRSubdocLiquidaziones r "
			+ "     WHERE r.dataCancellazione IS NULL "
			+ "     AND ( r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP)"
			+ "     AND r.siacTSubdoc.dataCancellazione IS NULL "
			+ "     AND r.siacTSubdoc.subdocId = :subdocId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM l.siacRLiquidazioneStatos rs "
			+ "     WHERE rs.dataCancellazione IS NULL "
			+ "     AND rs.siacDLiquidazioneStato.liqStatoCode <> 'A' "
			+ "     AND ( rs.dataFineValidita IS NULL OR rs.dataFineValidita > CURRENT_TIMESTAMP)"
			+ "               )"	
			)
	SiacTLiquidazione findNonAnnullataBySubdocId(@Param("subdocId") Integer subdocId);
	
	@Query(" SELECT tl "
			+ " FROM SiacTLiquidazione tl, SiacRSubdocLiquidazione rsl, SiacRLiquidazioneStato rls "
			+ " WHERE rsl.siacTLiquidazione = tl "
			+ " AND rls.siacTLiquidazione = tl "
			+ " AND tl.dataCancellazione IS NULL "
			+ " AND tl.dataFineValidita IS NULL "
			+ " AND rsl.dataCancellazione IS NULL "
			+ " AND rsl.dataFineValidita IS NULL "
			+ " AND rls.dataCancellazione IS NULL "
			+ " AND rls.dataFineValidita IS NULL "
			+ " AND rsl.siacTSubdoc.dataCancellazione IS NULL "
			+ " AND rsl.siacTSubdoc.subdocId = :subdocId "
			+ " AND rls.siacDLiquidazioneStato.liqStatoCode <> 'A' "
			+ " AND ( rsl.dataFineValidita IS NULL OR rsl.dataFineValidita > CURRENT_TIMESTAMP ) "
			+ " AND ( rls.dataFineValidita IS NULL OR rls.dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacTLiquidazione> findNonAnnullateBySubdocId(@Param("subdocId") Integer subdocId);

	@Query(" SELECT r.siacTBilElem.elemId"
			+ " FROM SiacRMovgestBilElem r "
			+ " WHERE r.dataCancellazione IS NULL"
			+ " AND EXISTS (FROM SiacRLiquidazioneMovgest rl"
			+ "				WHERE rl.dataCancellazione IS NULL"
			+ "				AND rl.siacTLiquidazione.liqId = :liqId"
			+ "				AND rl.siacTMovgestT.siacTMovgest.movgestId = r.siacTMovgest.movgestId) ")
	public int findIdCapitoloByLiquidazione(@Param("liqId") Integer liqId);
	
	
	@Query( " FROM SiacTClass c " 
			+ " WHERE c.dataCancellazione IS NULL " 
			+ " AND EXISTS ( FROM c.siacRLiquidazioneClasses rlc "
			+ "			  	 WHERE rlc.dataCancellazione IS NULL "
			+ "			  	 AND rlc.siacTLiquidazione.liqId = :liqId) "
			+ "	AND " 
			+ " c.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
			)
	SiacTClass findSiacTClassByLiqIdECodiciTipo(@Param("liqId")Integer uid, @Param("listaCodici")List<String> listaCodici);
}
