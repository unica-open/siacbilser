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

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;

/**
 * The Interface SiacTClassRepository.
 */
public interface SiacTClassRepository extends JpaRepository<SiacTClass, Integer> {
	
	
	/*
	@Query("SELECT c FROM SiacTClass c " +	
			"WHERE c.siacDClassTipo.classifTipoCode = :classifTipoCode " +
			"AND c.classifCode = :classifCode "
			)
	List<SiacTClass> findByTClassByClassifCodeAndClassifTipoCode(@Param("classifCode") String classifCode,@Param("classifTipoCode") String classifTipoCode);
	*/
	
	
	/*
	 * SiacTClass classNew = siacTClassRepository.findOne(classifIdNew);
		//String codiceTipoNew = classNew.getSiacDClassTipo().getClassifTipoCode();
		
		String tipoCode = classNew.getSiacDClassTipo().getClassifTipoCode();
		String famigliaCode = SiacDClassTipoEnum.byCodice(tipoCode).getFamiglia().getCodice();
	 */
	/**
	 * Find codice tipo classificatore by classif id.
	 *
	 * @param classifId the classif id
	 * @return the string
	 */
	@Query("SELECT c.siacDClassTipo.classifTipoCode " +
			"FROM  SiacTClass c " +
			"WHERE c.classifId = :classifId "
			)	
	 String findCodiceTipoClassificatoreByClassifId(@Param("classifId") Integer classifId);
	
	
	/**
	 * Find codice famiglia classificatore by classif id.
	 *
	 * @param classifId the classif id
	 * @return the string
	 */
	@Query("SELECT cft.siacTClassFamTree.siacDClassFam.classifFamCode "+
			" FROM SiacRClassFamTree cft "+
			" WHERE cft.siacTClassFiglio.classifId = :classifId " 
	)
	 String findCodiceFamigliaClassificatoreByClassifId(@Param("classifId") Integer classifId);
	
	
	/**
	 * Restituisce il padre di un classificatore gerarchico dato il suo classifId.
	 * In teoria un classificatore non può avere più padri. Ma per i cofog questa possibilità è da verificare.
	 *
	 * @param classifId the classif id
	 * @return the siac t class
	 */
	@Query("SELECT cft.siacTClassPadre "+
			" FROM SiacRClassFamTree cft "+
			" WHERE cft.siacTClassFiglio.classifId = :classifId " 
	)
	SiacTClass findPadreClassificatoreByClassifId(@Param("classifId") Integer classifId);
	
	/**
	 * Restituisce il padre di un classificatore gerarchico dato il suo classifId.
	 * In teoria un classificatore non può avere più padri. Ma per i cofog questa possibilità è da verificare.
	 *
	 * @param classifId the classif id
	 * @return the siac t class
	 */
	@Query(" SELECT rcft.siacTClassPadre "
			+ " FROM SiacRClassFamTree rcft "
			+ " WHERE rcft.siacTClassFiglio.classifId = :classifId "
			+ " AND DATE_TRUNC('day', TO_TIMESTAMP(CONCAT(:annoEsercizio, ' 12 31'), 'YYYY MM DD') )"
			+ "     BETWEEN DATE_TRUNC('day', rcft.dataInizioValidita) "
			+ "     AND COALESCE(rcft.dataFineValidita, DATE_TRUNC('day', TO_TIMESTAMP(CONCAT(:annoEsercizio, ' 12 31'), 'YYYY MM DD')))"
	)
	List<SiacTClass> findPadreClassificatoreByClassifIdAndAnnoEsercizio(@Param("classifId") Integer classifId, @Param("annoEsercizio") String annoEsercizio);
	
	/**
	 * Find attr testo by code and classif id.
	 *
	 * @param classifId the classif id
	 * @param attrCode the attr code
	 * @return the string
	 */
	@Query("SELECT rca.testo "+
			" FROM SiacRClassAttr rca "+
			" WHERE rca.siacTAttr.attrCode = :attrCode "  +
			" AND rca.siacTClass.classifId = :classifId "
	)
	String findAttrTestoByCodeAndClassifId(@Param("classifId") Integer classifId, @Param("attrCode") String attrCode);
	


	
	/**
	 * Find classif b by classif a.
	 *
	 * @param classifId the classif id
	 * @return the list
	 */
	@Query( "SELECT c.siacTClassB "
			+ "FROM SiacRClass c "
			+ "WHERE c.siacTClassA.classifId = :classifId "
			)
	List<SiacTClass> findClassifBByClassifA(@Param("classifId") Integer classifId);
	
	
	/**
	 * Find classif a by classif b.
	 *
	 * @param classifId the classif id
	 * @return the list
	 */
	@Query( "SELECT c.siacTClassA "
			+ "FROM SiacRClass c "
			+ "WHERE c.siacTClassB.classifId = :classifId "
			)
	List<SiacTClass> findClassifBByClassifB(@Param("classifId") Integer classifId);


	@Query( " SELECT rmc.siacTClass " +
			" FROM SiacRMovgestClass rmc " +
			" WHERE rmc.dataCancellazione IS NULL " +
			" AND (rmc.dataFineValidita IS NULL OR rmc.dataFineValidita >= CURRENT_DATE) " +
			" AND rmc.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' " +
			" AND rmc.siacTMovgestT.siacTMovgest.movgestId = :movgestId" +
			" AND rmc.siacTClass.siacDClassTipo.classifTipoCode = 'PDC_V' " 
			)
	SiacTClass findVLivelloPdcByAMovgest(@Param("movgestId") Integer movgestId);
	
	//SIAC-7892 cerco i figli per uid del padre e per anno di bilancio
	@Query(value = " SELECT DISTINCT stc.*, srcft.* " + 
			" FROM siac_t_class stc  " + 
			" JOIN siac_r_class_fam_tree srcft ON stc.classif_id = srcft.classif_id " + 
			" WHERE srcft.classif_id_padre = :classifIdPadre " + 
			" AND srcft.data_cancellazione is NULL " + 
			" AND ( srcft.validita_inizio is not NULL AND srcft.validita_inizio <= DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 01 01'), 'YYYY MM DD') ) ) " + 
			" AND ( srcft.validita_fine is NULL or srcft.validita_fine >= DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) " + 
			" AND stc.data_cancellazione is NULL " + 
			" AND ( stc.validita_inizio is not NULL AND stc.validita_inizio <= DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 01 01'), 'YYYY MM DD') ) ) " + 
			" AND ( stc.validita_fine is NULL or stc.validita_fine >= DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) " + 
			" ORDER BY srcft.ordine ", nativeQuery = true)
	List<SiacTClass> findChildByParentUid(@Param("classifIdPadre") Integer classifIdPadre, @Param("anno") Integer anno); 

	@Query(" FROM SiacDClassTipo dct "
			+ " WHERE dct.dataCancellazione IS NULL "
			+ " AND dct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dct.classifTipoCode IN (:classifTipoCodes) "
			+ " AND ( "
			+ "     DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') )  "
			+ "     BETWEEN  "
			+ "         DATE_TRUNC('day',dct.dataInizioValidita) "
			+ "     AND "
			+ "         COALESCE(dct.dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) "
			+ " ) "
			)
	List<SiacDClassTipo> findSiacDClassTipoByEnteProprietarioIdAndClassifTipoCodesAndAnno(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("classifTipoCodes") Collection<String> classifTipoCodes, @Param("anno") Integer anno);
	
	@Query(" FROM SiacDClassTipo dct "
			+ " WHERE dct.dataCancellazione IS NULL "
			+ " AND dct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dct.classifTipoCode IN (:classifTipoCodes) "
			+ " AND ( "
			+ "     DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') )  "
			+ "     BETWEEN DATE_TRUNC('day',dct.dataInizioValidita) "
			+ "     AND COALESCE(dct.dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM dct.siacRBilElemTipoClassTips rbetct "
			+ "     WHERE rbetct.dataCancellazione IS NULL "
			+ "     AND rbetct.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM dct.siacTClasses tc "
			+ "     WHERE tc.dataCancellazione IS NULL "
			+ "     AND ( "
			+ "         DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') )  "
			+ "         BETWEEN DATE_TRUNC('day', tc.dataInizioValidita) "
			+ "         AND COALESCE(tc.dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) "
			+ "     ) "
			+ " ) "
			)
	List<SiacDClassTipo> findSiacDClassTipoByEnteProprietarioIdAndClassifTipoCodesAndAnnoAndElemTipoCodeAndExistsSiacTClass(
			@Param("enteProprietarioId") Integer enteProprietarioId, @Param("classifTipoCodes") Collection<String> classifTipoCodes, @Param("anno") Integer anno, @Param("elemTipoCode") String elemTipoCode);
	
	//task-133
	@Query("SELECT t.classifId "
			+ " FROM SiacTClass t "
			+ " WHERE t.classifCode = :classifCode "
			+ " AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND t.siacDClassTipo.classifTipoCode = 'CATEGORIA' "
			+ " AND t.dataCancellazione IS NULL "
			)
	 List<Integer> findClassifIdTipoClassificatoreByCodiceCategoria(@Param("classifCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	//task-133
	@Query(value = " SELECT ccategoria.classif_id "
			+ "FROM siac_r_class_fam_tree  r,siac_t_class c,siac_d_class_fam fam ,siac_t_class_fam_tree tree,siac_d_class_tipo tipo, "
			+ "     siac_t_class ccategoria, siac_d_class_tipo tipoCategoria "
			+ "WHERE  fam.ente_proprietario_id = :ente_proprietario_id "
			+ "AND    fam.classif_fam_id = tree.classif_fam_id "
			+ "AND    r.classif_fam_tree_id = tree.classif_fam_tree_id "
			+ "AND    c.classif_id = r.classif_id_padre "
			+ "AND    tipo.classif_tipo_id = c.classif_tipo_id "
			+ "AND    tipo.classif_tipo_code ='TIPOLOGIA' "
			+ "AND    c.classif_code= :codiceTipologia "
			+ "AND    cCategoria.classif_id = r.classif_id "
			+ "AND    tipoCategoria.classif_tipo_id = cCategoria.classif_tipo_id "
			+ "AND    tipoCategoria.classif_tipo_code = 'CATEGORIA' "
			+ "AND    r.data_cancellazione is null "
			+ "AND    c.data_cancellazione is null "
			+ "AND    tree.data_cancellazione is null "
			+ "AND    fam.data_cancellazione is null "
			+ "AND    ccategoria.data_cancellazione is null " 
			, nativeQuery = true)	
	 List<Integer> findClassifIdTipoClassificatoreByCodiceTipologia(@Param("codiceTipologia") String codiceTipologia, @Param("ente_proprietario_id") Integer enteProprietarioId);

	//task-138
	@Query("SELECT t.classifId "
			+ " FROM SiacTClass t "
			+ " WHERE t.classifCode = :classifCode "
			+ " AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND t.siacDClassTipo.classifTipoCode = 'PROGRAMMA' "
			+ " AND t.dataCancellazione IS NULL "
			)
	 List<Integer> findClassifIdTipoClassificatoreByCodiceProgramma(@Param("classifCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	//task-138
	@Query("SELECT t.classifId "
			+ " FROM SiacTClass t "
			+ " WHERE t.classifCode = :classifCode "
			+ " AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND t.siacDClassTipo.classifTipoCode = 'MACROAGGREGATO' "
			+ " AND t.dataCancellazione IS NULL "
			)	
	 List<Integer> findClassifIdTipoClassificatoreByCodiceMacroaggregato(@Param("classifCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
