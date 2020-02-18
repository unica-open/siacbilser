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
	
}
