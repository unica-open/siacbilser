/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.integration.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccorser.business.dtomapping.converter.YearDateConverter;

// TODO: Auto-generated Javadoc
/**
 * Implementazione del DAO per le codifche. ATTENZIONE i Dao sollevano della
 * unchecked exceptions: per catturarle occorre catturare le RuntimeException
 * 
 * @author rmontuori
 * @version $Id: $
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodificaDaoImpl extends JpaDao<SiacTClass, Integer> implements CodificaDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccorser.integration.dao.CodificaDao#findCodifiche(int, int)
	 */
	@Override
	public List<SiacTClass> findCodifiche(int anno, int enteProprietarioId) {
		final String methodName = "findCodifiche";
		log.debugStart(methodName, "");
		
//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(
//				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);

		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c FROM SiacTClass c "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
						
						// + " AND  (c.dataFineValidita is null or c.dataFineValidita >= :endAnnoEsercizioDate)",
						+ getSiacTClassDataValiditaSql("c", "anno"),
						
						SiacTClass.class);
		query.setParameter("enteProprietarioId", enteProprietarioId);
//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);

		List<SiacTClass> siacTClasses = query.getResultList();

		return siacTClasses;
	}

	@Override
	public List<SiacTClass> findCodificheByIdPadre(int anno,
			int enteProprietarioId, int classifIdPadre) {

		final String methodName = "findCodificheByIdPadre";
		log.debugStart(methodName, "");

		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
		Date endAnnoEsercizioDate = DateUtils.addDays(
				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);

		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c  from SiacTClass c "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND c.dataCancellazione IS NULL "
						+ " AND EXISTS ( "
						+ "		FROM c.siacRClassFamTreesFiglio f "
						+ " 	WHERE f.siacTClassPadre.classifId = :classifIdPadre "
						+ " ) "
						//+ "  c.dataInizioValidita BETWEEN :startAnnoEsercizioDate AND :endAnnoEsercizioDate "
						//+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
						
						//+ " AND  (c.dataFineValidita is null or c.dataFineValidita >= :endAnnoEsercizioDate)"
						+ getSiacTClassDataValiditaSql("c", "anno")
						
						+ " ORDER BY c.classifCode ",
						SiacTClass.class);

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifIdPadre", classifIdPadre);

//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);

		List<SiacTClass> siacTClasses = query.getResultList();
		if (log.isDebugEnabled()) {
			for (SiacTClass siacTClass : siacTClasses) {
				log.debug(methodName, "siacTClass.getSiacDClassTipo: " + siacTClass.getSiacDClassTipo().getClassifTipoCode());
			}
		}

		return siacTClasses;
	}

	
	

	@Override
	public List<SiacTClass> findCodificheByIdFiglio(int anno,
			int enteProprietarioId, int classifIdFiglio) {

		final String methodName = "findCodificheByIdFiglio";
		log.debugStart(methodName, "");

		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
		Date endAnnoEsercizioDate = DateUtils.addDays(
				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);

		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c  from SiacTClass c "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND c.dataCancellazione IS NULL "
						+ " AND EXISTS ( "
						+ "		FROM c.siacRClassFamTreesPadre f "
						+ " 	WHERE f.siacTClassFiglio.classifId = :classifIdFiglio "
						+ " ) "
						//+ "  c.dataInizioValidita BETWEEN :startAnnoEsercizioDate AND :endAnnoEsercizioDate "
						//+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
						
						//+ " AND  (c.dataFineValidita is null or c.dataFineValidita >= :endAnnoEsercizioDate)"
						+ getSiacTClassDataValiditaSql("c", "anno")
						
						+ " ORDER BY c.classifCode ",
						SiacTClass.class);

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifIdFiglio", classifIdFiglio);

//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);

		List<SiacTClass> siacTClasses = query.getResultList();
		if (log.isDebugEnabled()) {
			for (SiacTClass siacTClass : siacTClasses) {
				log.debug(methodName, "siacTClass.getSiacDClassTipo: " + siacTClass.getSiacDClassTipo().getClassifTipoCode());
			}
		}

		return siacTClasses;
	}

	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccorser.integration.dao.CodificaDao#findCodificaFamigliaTreeDto(int, int, java.lang.String)
	 */
	@Override
	public List<SiacTClass> findCodificaFamigliaTreeDto(int anno, int enteProprietarioId, String codiceFamigliaTree) {
		final String methodName = "findCodificaFamigliaTreeDto";
		log.debugStart(methodName, "");

		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT DISTINCT c "
						+ " FROM SiacTClass c "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND EXISTS ( "
						+ "		FROM c.siacRClassFamTreesFiglio f "
						//+ " c.padre.uid is null and" 
						+ " 	WHERE f.siacTClassPadre IS NULL "
						+ " 	OR f.siacTClassPadre.classifId IS NULL "
						+ " ) "
						//+ " c.codificaFamiglia.uid= :famigliaTreeId and "
						//+ " c.codificaFamiglia.codice= :codiceFamigliaTree and "
						+ " AND EXISTS ( "
						+ " 	FROM c.siacRClassFamTreesFiglio f "
						+ " 	WHERE f.siacTClassFamTree.siacDClassFam.classifFamCode = :classifFamCode "
						+ " 	AND f.siacTClassFamTree.siacDClassFam.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " 	AND f.siacTClassFamTree.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " ) "
//						+ " c.codificaFamiglia.codiceCodificaFamigliaDto.codice = :famigliaTreeCodice and "	
//						+ " c.codificaFamiglia.codiceCodificaFamigliaDto.enteProprietario.uid = :enteProprietarioId and "
//						+ " c.codificaFamiglia.enteProprietario.uid = :enteProprietarioId and "
						+ " AND c.dataCancellazione IS NULL " 
						//+ " c.dataInizioValidita BETWEEN :startAnnoEsercizioDate AND :endAnnoEsercizioDate "
						//+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
						
						// + " AND  (c.dataFineValidita IS NULL or c.dataFineValidita >= :endAnnoEsercizioDate)"
						+ getSiacTClassDataValiditaSql("c", "anno")
						
						+ " ORDER BY c.classifId",
						SiacTClass.class);

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(
//				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);

//		query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifFamCode", codiceFamigliaTree);

		List<SiacTClass> dtos = query.getResultList();

		return dtos;
	}

}
