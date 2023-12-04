/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;

/**
 * The Interface SiacTMovgestBilRepository.
 */
public interface SiacTMovgestBilRepository extends JpaRepository<SiacTMovgest, Integer> {
	
	@Query(" SELECT m.movgestId "
			+ " FROM SiacTMovgest m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movgestAnno = :movgestAnno "
			+ " AND m.movgestNumero = :movgestNumero "
			+ " AND m.siacDMovgestTipo.movgestTipoCode = :movgestTipoCode "
			+ " AND m.siacTBil.bilId = :bilId")
	List<Integer> findUidMovgestByAnnoNumeroBilancio(
			@Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero,
			@Param("movgestTipoCode") String movgestTipoCode,
			@Param("bilId") Integer bilId
	);
	
		
//	@Query(" SELECT m FROM SiacTMovgest m "
//	+ " JOIN m.siacTBil tBil "//
//	+ " JOIN tBil.siacTPeriodo tPeriodo "//
//	+ " JOIN m.siacTMovgestTs mt "
//	+ " JOIN mt.siacRMovgestTsAttoAmms mtaa "
//	+ " WHERE mtaa.siacTAttoAmm.attoammNumero=:attoammNumero "
//	+ " AND tPeriodo.anno = :annoBilancio "//
//	+ " AND mtaa.siacTAttoAmm.attoammAnno=:attoammAnno "
//	+ " AND mtaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
//	+ " AND mtaa.siacTAttoAmm.dataCancellazione IS NULL "
//	+ " AND mtaa.dataCancellazione IS NULL "
//	+ " AND mt.siacDMovgestTsTipo.movgestTsTipoCode='T' "
//	+ " AND m.dataCancellazione IS NULL "
//	+ " AND (:attoammSacId IS NULL OR EXISTS ("
//	+ "	SELECT 1 FROM mtaa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
//	+ " ) "
//	+ " AND m.siacTEnteProprietario.enteProprietarioId =:enteProprietarioId")
	
	//SIAC-7383 Esclusione movimenti annullati
	@Query(" SELECT m FROM SiacTMovgest m "
			+ " JOIN m.siacTBil tBil "//
			+ " JOIN tBil.siacTPeriodo tPeriodo "//
			+ " JOIN m.siacTMovgestTs mt "
			+ " JOIN mt.siacRMovgestTsStatos ms "
			+ " JOIN ms.siacDMovgestStato dms "
			+ " JOIN mt.siacRMovgestTsAttoAmms mtaa "
			+ " WHERE mtaa.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND tPeriodo.anno = :annoBilancio "//
			+ " AND mtaa.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND mtaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND mtaa.siacTAttoAmm.dataCancellazione IS NULL "
			+ " AND mtaa.dataCancellazione IS NULL "
			+ " AND mt.siacDMovgestTsTipo.movgestTsTipoCode='T' "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND ms.dataCancellazione IS NULL AND dms.movgestStatoCode != 'A'"
			+ " AND (:attoammSacId IS NULL OR EXISTS ("
			+ "	SELECT 1 FROM mtaa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
			+ " ) "
			+ " AND m.siacTEnteProprietario.enteProprietarioId =:enteProprietarioId")
	List<SiacTMovgest> findSiacTMovgestBySiacTAttoAmm(
		@Param("annoBilancio") String annoBilancio,// SIAC-7365 
		@Param("attoammNumero") Integer attoammNumero, 
		@Param("attoammAnno") String attoammAnno,
		@Param("attoammTipoId") Integer attoammTipoId,
		@Param("attoammSacId") Integer attoammSacId,
		@Param("enteProprietarioId") Integer enteProprietarioId);
			
		
		
		
	@Query(" FROM SiacTMovgest tm "
			+ " WHERE tm.siacTEnteProprietario.enteProprietarioId=:idEnte "
			+ " AND (CAST(:codiceProgetto AS string) IS NULL OR EXISTS ( "
			+ " 	SELECT 1 FROM tm.siacTMovgestTs tmt "
			+ "		JOIN tmt.siacRMovgestTsProgrammas rmtp "
			+ "     WHERE rmtp.siacTProgramma.programmaCode=:codiceProgetto "
	//		+ "     AND tmt.siacDMovgestTsTipo.movgestTsTipoCode='T' " ???
			+ " 	AND tmt.dataCancellazione IS NULL "
			+ " 	AND rmtp.siacTProgramma.siacTBil=tm.siacTBil "
			+ " 	AND tmt.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (tmt.dataFineValidita IS NULL OR tmt.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 	AND rmtp.dataCancellazione IS NULL "
			+ " 	AND rmtp.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rmtp.dataFineValidita IS NULL OR rmtp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " )) "
			+ " AND (:annoBilancio IS NULL OR tm.siacTBil.siacTPeriodo.anno=CAST(:annoBilancio AS string)) "
			+ " AND (CAST(:cup AS string) IS NULL OR EXISTS ( "
			+ "		SELECT 1 FROM tm.siacTMovgestTs tmt2 "
			+ "		JOIN tmt2.siacRMovgestTsAttrs rmta "
			+ "     WHERE rmta.siacTAttr.attrCode='cup' "
			+ "		AND rmta.testo=:cup"
	//		+ "     AND tmt2.siacDMovgestTsTipo.movgestTsTipoCode='T' "
			+ " 	AND tmt2.dataCancellazione IS NULL "
			+ " 	AND tmt2.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (tmt2.dataFineValidita IS NULL OR tmt2.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 	AND rmta.dataCancellazione IS NULL "
			+ " 	AND rmta.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rmta.dataFineValidita IS NULL OR rmta.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "	)) "
			+ " AND (CAST(:cig AS string) IS NULL OR EXISTS ( "
			+ "		SELECT 1 FROM tm.siacTMovgestTs tmt3 "
			+ " 	JOIN tmt3.siacRMovgestTsAttrs rmta2 "
			+ "     WHERE rmta2.siacTAttr.attrCode='cig' "
	//		+ "     AND tmt3.siacDMovgestTsTipo.movgestTsTipoCode='T' "
			+ "		AND rmta2.testo=:cig"
			+ " 	AND tmt3.dataCancellazione IS NULL "
			+ " 	AND tmt3.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (tmt3.dataFineValidita IS NULL OR tmt3.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 	AND rmta2.dataCancellazione IS NULL "
			+ " 	AND rmta2.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rmta2.dataFineValidita IS NULL OR rmta2.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "	)) "
			+ " AND EXISTS ( "
			+ " 	SELECT 1 FROM tm.siacTMovgestTs tmt4 "
			+ "		JOIN tmt4.siacRMovgestTsStatos rmts "
			+ "     WHERE rmts.siacDMovgestStato.movgestStatoCode <> 'A' "
		//	+ "     AND tmt4.siacDMovgestTsTipo.movgestTsTipoCode='T' " ????
			+ " 	AND tmt4.dataCancellazione IS NULL "
			+ " 	AND tmt4.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (tmt4.dataFineValidita IS NULL OR tmt4.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 	AND rmts.dataCancellazione IS NULL "
			+ " 	AND rmts.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rmts.dataFineValidita IS NULL OR rmts.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " ) "			
			+ " AND tm.dataCancellazione IS NULL "
			+ " AND tm.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (tm.dataFineValidita IS NULL OR tm.dataFineValidita > CURRENT_TIMESTAMP) "
	)
	List<SiacTMovgest> findSiacTMovgest(
			@Param("idEnte") Integer idEnte,
			@Param("annoBilancio") Integer annoBilancio,
			@Param("cup") String cup,
			@Param("codiceProgetto") String codiceProgetto,
			@Param("cig") String cig			
	);
	
		

	@Query(" SELECT CAST(MIN(m.siacTBil.siacTPeriodo.anno) AS integer) "
			+ " FROM SiacTMovgest m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movgestAnno = :anno "
			+ " AND m.movgestNumero = :numero "
			+ " AND m.siacDMovgestTipo.movgestTipoCode = :codiceTipo "
			+ " AND CAST(m.siacTBil.siacTPeriodo.anno AS integer) <= :annoBilancio"
			+ " AND m.dataCancellazione IS NULL "
			+ " AND m.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (m.dataFineValidita IS NULL OR m.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND EXISTS ("
			+ "		SELECT 1 FROM m.siacTMovgestTs mt "
			+ "			JOIN mt.siacRMovgestTsStatos mts "
			+ "			WHERE mt.siacDMovgestTsTipo.movgestTsTipoCode='T' "
			+ " 		AND mts.siacDMovgestStato.movgestStatoCode <> 'A' "
			+ " 		AND mts.dataCancellazione IS NULL "
			+ " 		AND mts.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 		AND (mts.dataFineValidita IS NULL OR mts.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " ) "
			)
	Integer getAnnoRiaccertamento(
			@Param("anno") Integer anno,
			@Param("numero") BigDecimal numero,
			@Param("codiceTipo") String codiceTipo,
			@Param("annoBilancio") Integer annoBilancio
	);

	@Query(" FROM SiacTMovgest m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movgestAnno = :movgestAnno "
			+ " AND m.movgestNumero = :movgestNumero "
			+ " AND m.siacDMovgestTipo.movgestTipoCode = :movgestTipoCode "
			+ " AND m.siacTEnteProprietario.enteProprietarioId =:enteProprietarioId")
	List<SiacTMovgest> findMovgestByAnnoNumeroTipo(
			@Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero,
			@Param("movgestTipoCode") String movgestTipoCode,
			@Param("enteProprietarioId") Integer enteProprietarioId
	);
}
