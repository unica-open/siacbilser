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

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;

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
			+ " AND tafde.dataFineValidita IS NULL "
			+ " AND tafde.siacTBilElem.elemId = :elemId "
			+ " AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilId ")
	Long countByElemIdAndAfdeBilId(@Param("elemId") Integer elemId, @Param("afdeBilId") Integer afdeBilId);
	
	/**
	 * Conta le occorrenze di SiacTAccFondiDubbiaEsig collegate a un dato elemId
	 * @param elemId l'elemId da utilizzare come filtro
	 * @param afdeTipoCode il tipo
	 * @param pageable il paginatore
	 * @return il numero delle istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" FROM SiacTAccFondiDubbiaEsig tafde "
			+ " WHERE tafde.dataCancellazione IS NULL "
			+ " AND tafde.dataFineValidita IS NULL "
			+ " AND tafde.siacTBilElem.elemId = :elemId "
			+ " AND tafde.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode ")
	Page<SiacTAccFondiDubbiaEsig> findByElemIdAndAfdeTipoCode(@Param("elemId") Integer elemId, @Param("afdeTipoCode") String afdeTipoCode, Pageable pageable);
	
	/**
	 * Trova le occorrenze equivalenti per un dato tipo
	 * @param accFdeId l'accFdeId da utilizzare come filtro
	 * @return il numero delle istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" SELECT tafdeEquiv "
			+ " FROM SiacTAccFondiDubbiaEsig tafdeEquiv, "
			+ "   SiacTAccFondiDubbiaEsig tafde "
			+ " WHERE tafdeEquiv.dataCancellazione IS NULL "
			+ " AND tafdeEquiv.dataFineValidita IS NULL "
			+ " AND tafdeEquiv.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode "
			+ " AND tafde.accFdeId = :accFdeId "
			+ " AND tafdeEquiv.siacTBilElem.elemCode = tafde.siacTBilElem.elemCode "
			+ " AND tafdeEquiv.siacTBilElem.elemCode2 = tafde.siacTBilElem.elemCode2 "
			+ " AND tafdeEquiv.siacTBilElem.elemCode3 = tafde.siacTBilElem.elemCode3 "
			+ " AND tafdeEquiv.siacTBilElem.siacTBil = tafde.siacTBilElem.siacTBil ")
	Page<SiacTAccFondiDubbiaEsig> findEquivalenteByAccFdeIdAndAfdeTipoCode(@Param("accFdeId") Integer accFdeId, @Param("afdeTipoCode") String afdeTipoCode, Pageable pageable);
	
	/**
	 * Trova le occorrenze equivalenti per un dato tipo
	 * @param elemId l'elemId da utilizzare come filtro
	 * @return il numero delle istanze di SiacTAccFondiDubbiaEsig collegate al filtro
	 */
	@Query(" SELECT tafdeEquiv "
			+ " FROM SiacTAccFondiDubbiaEsig tafdeEquiv, SiacTBilElem tbe "
			+ " WHERE tafdeEquiv.dataCancellazione IS NULL "
			+ " AND tafdeEquiv.dataFineValidita IS NULL "
			+ " AND tafdeEquiv.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode "
			+ " AND tbe.elemId = :elemId "
			+ " AND tafdeEquiv.siacTBilElem.elemCode = tbe.elemCode "
			+ " AND tafdeEquiv.siacTBilElem.elemCode2 = tbe.elemCode2 "
			+ " AND tafdeEquiv.siacTBilElem.elemCode3 = tbe.elemCode3 "
			+ " AND tafdeEquiv.siacTBilElem.siacTBil = tbe.siacTBil ")
	Page<SiacTAccFondiDubbiaEsig> findEquivalenteByElemIdAndAfdeTipoCode(@Param("elemId") Integer elemId, @Param("afdeTipoCode") String afdeTipoCode, Pageable pageable);
	
	@Query(" SELECT tbe "
			+ " FROM SiacTBilElem tbe, SiacTBilElem tbeOld "
			+ " WHERE tbe.siacTBil.bilId = :bilId "
			+ " AND tbe.dataCancellazione IS NULL "
			+ " AND tbe.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND tbe.siacTEnteProprietario = tbeOld.siacTEnteProprietario "
			+ " AND tbe.elemCode = tbeOld.elemCode "
			+ " AND tbe.elemCode2 = tbeOld.elemCode2 "
			+ " AND tbe.elemCode3 = tbeOld.elemCode3 "
			+ " AND tbeOld.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ( "
			+ "   FROM tbe.siacTAccFondiDubbiaEsigs tafde "
			+ "   WHERE tafde.dataCancellazione IS NULL "
			+ "   AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "   FROM tbeOld.siacTAccFondiDubbiaEsigs tafde "
			+ "   WHERE tafde.dataCancellazione IS NULL "
			+ "   AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilIdOld "
			+ " ) "
			)
	List<SiacTBilElem> findSiacTBilElemEquivalentiNonCollegati(@Param("bilId") Integer bilId, @Param("afdeBilId") Integer afdeBilId, @Param("afdeBilIdOld") Integer afdeBilIdOld, @Param("elemTipoCode") String elemTipoCode);

	@Query(" SELECT tbe "
			+ " FROM SiacTBilElem tbe "
			+ " JOIN tbe.siacRBilElemClasses srbec "
			+ " JOIN srbec.siacTClass stc "
			+ " JOIN stc.siacRClassFamTreesFiglio srcft "
			+ " JOIN srcft.siacTClassFamTree stcft "
			+ " JOIN stcft.siacDClassFam sdcf "
			+ " JOIN tbe.siacTEnteProprietario step "
			+ " JOIN step.siacTBilElems tbeOld "
			+ " WHERE tbe.siacTBil.bilId = :bilId "
			+ " AND tbe.dataCancellazione IS NULL "
			+ " AND tbe.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND tbe.siacTEnteProprietario = tbeOld.siacTEnteProprietario "
			+ " AND tbe.elemCode = tbeOld.elemCode "
			+ " AND tbe.elemCode2 = tbeOld.elemCode2 "
			+ " AND tbe.elemCode3 = tbeOld.elemCode3 "
			+ " AND tbeOld.dataCancellazione IS NULL "
			+ " AND srcft.dataCancellazione IS NULL "
			+ " AND sdcf.classifFamCode = '00003' "
			+ " AND srcft.livello = 3 "
			+ " AND NOT EXISTS ( "
			+ "   FROM tbe.siacTAccFondiDubbiaEsigs tafde "
			+ "   WHERE tafde.dataCancellazione IS NULL "
			+ "   AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "   FROM tbeOld.siacTAccFondiDubbiaEsigs tafde "
			+ "   WHERE tafde.dataCancellazione IS NULL "
			+ "   AND tafde.siacTAccFondiDubbiaEsigBil.afdeBilId = :afdeBilIdOld "
			+ " ) "
			+ " GROUP BY tbe.elemId, tbe.elemCode, tbe.elemCode2, tbe.elemCode3, stc.classifCode "
			+ " ORDER BY stc.classifCode ASC, tbe.elemCode ASC, tbe.elemCode2 ASC, tbe.elemCode3 ASC "
			)
	List<SiacTBilElem> findSiacTBilElemEquivalentiNonCollegatiJPA(@Param("bilId") Integer bilId, @Param("afdeBilId") Integer afdeBilId, @Param("afdeBilIdOld") Integer afdeBilIdOld, @Param("elemTipoCode") String elemTipoCode);

	@Query(value = " SELECT tbe "
			+ " FROM siac_t_bil_elem tbe "
			+ " JOIN siac_r_bil_elem_class srbec ON tbe.elem_id = srbec.elem_id  "
			+ " JOIN siac_r_class_fam_tree srcft ON srcft.classif_id = srbec.classif_id  "
			+ " JOIN siac_t_class_fam_tree stcft ON srcft.classif_fam_tree_id = stcft.classif_fam_tree_id  "
			+ " JOIN siac_d_class_fam sdcf ON sdcf.classif_fam_id = stcft.classif_fam_id  "
			+ " JOIN siac_t_bil stb ON tbe.bil_id = stb.bil_id  "
			+ " JOIN siac_d_bil_elem_tipo sdbet ON tbe.elem_tipo_id = sdbet.elem_tipo_id  "
			+ " JOIN siac_t_ente_proprietario step ON tbe.ente_proprietario_id = step.ente_proprietario_id  "
			+ " JOIN siac_t_ente_proprietario stepOld ON step.ente_proprietario_id = stepOld.ente_proprietario_id "
			+ " JOIN siac_t_bil_elem tbeOld ON tbeOld.ente_proprietario_id = stepOld.ente_proprietario_id "
			+ " JOIN siac_t_class stc ON stc.classif_id = srbec.classif_id  "
			+ " JOIN siac_d_class_tipo sdct ON stc.classif_tipo_id = sdct.classif_tipo_id  "
			+ " WHERE stb.bil_id = :bilId "
			+ " AND tbe.data_cancellazione IS NULL "
			+ " AND sdbet.elem_tipo_code = :elemTipoCode "
			+ " AND tbe.elem_code = tbeOld.elem_code "
			+ " AND tbe.elem_code2 = tbeOld.elem_code2 "
			+ " AND tbe.elem_code3 = tbeOld.elem_code3 "
			+ " AND sdcf.classif_fam_code = '00003' "
			+ " AND srcft.livello = 3 "
			+ " AND tbeOld.data_cancellazione IS NULL "
			+ " AND srbec.data_cancellazione IS NULL "
			+ " AND srcft.data_cancellazione IS NULL "
			+ " AND NOT EXISTS ( "
			+ " 	SELECT 1 "
			+ " 	FROM siac_t_acc_fondi_dubbia_esig tafde "
			+ " 	JOIN siac_t_acc_fondi_dubbia_esig_bil tafdeb ON tafde.afde_bil_id = tafdeb.afde_bil_id  "
			+ " 	WHERE tafde.data_cancellazione IS NULL "
			+ " 	AND tafde.elem_id = tbe.elem_id  "
			+ " 	AND tafdeb.afde_bil_id = :afdeBilId "
			+ " ) "
			+ " AND EXISTS ( "
			+ " 	SELECT 1 "
			+ " 	FROM siac_t_acc_fondi_dubbia_esig tafde "
			+ " 	JOIN siac_t_acc_fondi_dubbia_esig_bil tafdeb ON tafde.afde_bil_id = tafdeb.afde_bil_id  "
			+ " 	WHERE tafde.data_cancellazione IS NULL "
			+ " 	AND tafde.elem_id = tbeOld.elem_id  "
			+ " 	AND tafdeb.afde_bil_id = :afdeBilIdOld "
			+ " ) "
			+ " GROUP BY tbe.elem_id, tbe.elem_code, tbe.elem_code2, tbe.elem_code3, stc.classif_code "
			+ " ORDER BY stc.classif_code ASC, CAST(tbe.elem_code AS INTEGER) ASC, CAST(tbe.elem_code2 AS INTEGER) ASC, CAST(tbe.elem_code3 AS INTEGER) ASC "
			, nativeQuery = true)
	List<SiacTBilElem> findSiacTBilElemEquivalentiNonCollegatiNative(@Param("bilId") Integer bilId, @Param("afdeBilId") Integer afdeBilId, @Param("afdeBilIdOld") Integer afdeBilIdOld, @Param("elemTipoCode") String elemTipoCode);
	
}
