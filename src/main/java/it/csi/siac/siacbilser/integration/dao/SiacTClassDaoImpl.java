/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * Dao dei classificatori.
 * 
 * @author Domenico
 *
 */
@Component
@Transactional
public class SiacTClassDaoImpl extends JpaDao<SiacTClass, Integer> implements SiacTClassDao {
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.SiacTClassDao#findFigliClassificatoreIds(it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum, java.lang.String[])
	 */
	public List<Integer> findFigliClassificatoreIds(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes) {
		
		List<Integer> result = new ArrayList<Integer>();
		List<SiacTClass> figli = findFigliClassificatore(annoEsercizio, tipoClassificatore, siacTEnteProprietarioId, classifCodes);
		for (SiacTClass figlio : figli) {
			result.add(figlio.getClassifId());
		}
		
		return result;
	}	
	
	
	/**
	 * Restituisce i figli di un classificatore.
	 * Ad esempio per piano dei conti con classifCode = "E.9.02.00.00.000" viene restituito tra i figli anche il classificatore "E.9.02.03.00.000"
	 * (Nota per piano dei conti con classifCode = "E.9.02.03.03.000"  -> i padri sono: "+ "E.9.02.03.00.000" +"E.9.02.00.00.000" +"E.9.00.00.00.000")	
	 *
	 * @param tipoClassificatore the tipo classificatore
	 * @param siacTEnteProprietarioId the siac t ente proprietario id
	 * @param annoEsercizio the anno esercizio
	 * @param classifCodes the classif codes
	 * @return the list
	 */
	public List<SiacTClass> findFigliClassificatore(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes) {
		SiacTClass siacTClass;
		
		try {
			siacTClass = findClassificatore(annoEsercizio, tipoClassificatore, siacTEnteProprietarioId, classifCodes);			
		} catch (IllegalArgumentException iae){
			return new ArrayList<SiacTClass>();
		}
		
		return findFigliClassificatore(siacTClass.getUid());
	}
	

	/**
	 * Restituisce i figli di un classificatore.
	 * Ad esempio per piano dei conti con classifCode = "E.9.02.00.00.000" viene restituito tra i figli anche il classificatore "E.9.02.03.00.000"
	 * 
	 * (Nota per piano dei conti con classifCode = "E.9.02.03.03.000"  -> i padri sono: "+ "E.9.02.03.00.000" +"E.9.02.00.00.000" +"E.9.00.00.00.000")		
	 *
	 * @param classifId the classif id
	 * @return the list
	 */
	public List<SiacTClass> findFigliClassificatore(Integer classifId) {
		if(classifId==null) {
			return new ArrayList<SiacTClass>();
		}
		
		SiacTClass tclass = siacTClassRepository.findOne(classifId);		
		List<SiacTClass> figli = recursiveFindFigliClassificatore(tclass);		
		return figli;
	}


	/**
	 * Dato un clasificatore gerarchico ricerca ricorsivamente tutti i suoi figli.
	 *
	 * @param tclass the tclass
	 * @return the list
	 */
	public List<SiacTClass> recursiveFindFigliClassificatore(SiacTClass tclass) {
		final String methodName = "recursiveFindFigliClassificatore";
		List<SiacTClass> figli = new ArrayList<SiacTClass>();
		
		List<SiacRClassFamTree> siacRClassFamTreesFiglio = tclass.getSiacRClassFamTreesPadre();		
		figli.add(tclass);
		
		for (SiacRClassFamTree siacRClassFamTree : siacRClassFamTreesFiglio) {
			SiacTClass figlio = siacRClassFamTree.getSiacTClassFiglio();
			if (log.isDebugEnabled()) {
				log.debug(methodName,
						siacRClassFamTree.getSiacTClassFamTree().getSiacDClassFam().getClassifFamCode() + "."
								+ figlio.getSiacDClassTipo().getClassifTipoCode() + " Trovato figlio -> id: " + figlio.getClassifId() + " code: "
								+ figlio.getClassifCode() + " desc: " + figlio.getClassifDesc());
			}
			figli.addAll(recursiveFindFigliClassificatore(figlio));
		}
	
		return figli;
	}
	
	
	/**
	 * Ricerca un classificatore a partire dal suo tipo e codice.
	 *
	 * @param tipoClassificatore il tipo del primo codice classificatore passato come parametro
	 * @param siacTEnteProprietarioId l'id dell'enteProprietario
	 * @param classifCodes elenco dei codici nella gerarchia da ricercare.
	 * @return the siac t class
	 */
	public SiacTClass findClassificatore(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes) {
		final String methodName = "findClassificatore";		
		
		if(classifCodes == null || classifCodes.length==0 || classifCodes[0] == null){
			throw new IllegalArgumentException("E' necessario specificare il codice del classificatore da ricercare.");
		}
		
		log.info(methodName, "called findClassificatore for "+ tipoClassificatore + " with codes: "+ ToStringBuilder.reflectionToString(classifCodes, ToStringStyle.SIMPLE_STYLE));
		
		List<SiacTClass> tclasses = findByClassifCodeAndClasifTipoCodeAndFamCode(classifCodes[0], tipoClassificatore.getCodice(),
				tipoClassificatore.getFamiglia().getCodice(), siacTEnteProprietarioId, annoEsercizio);
		
		if(tclasses == null || tclasses.isEmpty()) {	
			log.debug(methodName, "No elements found for " + tipoClassificatore + " with codes: "+ ToStringBuilder.reflectionToString(classifCodes));
			throw new IllegalStateException("Impossibile trovare il classificatore di tipo "+tipoClassificatore + " con codice: "+classifCodes[0] + ".");
		}	
				
		if(classifCodes.length == 1) {	
			SiacTClass tclass = tclasses.get(0);						
			log.debug(methodName, "Returning first element found of " + tclasses.size() + " (1 was expected) elements.");
			log.debug(methodName, "Returning: id: " + tclass.getClassifId() + " code: " + tclass.getClassifCode() + " desc: " + tclass.getClassifDesc());
			return tclass;			
		} 
		
		return recursiveFindClassificatore(tclasses.get(0),tipoClassificatore.getClassTipoFiglio(), Arrays.copyOfRange(classifCodes, 1, classifCodes.length));
		
	}

	/**
	 * Ricerca un classificatore a partire dal suo tipo e dalla concatenazione dei codici nella sua gerarchia.
	 *
	 * @param siacTClass the siac t class
	 * @param tipoClassificatore the tipo classificatore
	 * @param classifCodes the classif codes
	 * @return the siac t class
	 */
	private SiacTClass recursiveFindClassificatore(SiacTClass siacTClass, SiacDClassTipoEnum tipoClassificatore, String... classifCodes) {
		final String methodName = "recursiveFindClassificatore";
		
		log.info(methodName, "called recursiveFindClassificatore for "+ tipoClassificatore + " with codes: "+ ToStringBuilder.reflectionToString(classifCodes, ToStringStyle.SIMPLE_STYLE));
		
		if(classifCodes[0]==null){
			return siacTClass;
		}							
			
		for (SiacRClassFamTree siacRClassFamTree : siacTClass.getSiacRClassFamTreesPadre()) {
			SiacTClass figlio = siacRClassFamTree.getSiacTClassFiglio();		
			
			if(classifCodes[0].equals(figlio.getClassifCode())) {
				
				if(classifCodes.length == 1) {					
					
					if (log.isDebugEnabled()) {
						log.debug(methodName,
								"Returning: "+ siacRClassFamTree.getSiacTClassFamTree().getSiacDClassFam().getClassifFamCode() + "."
										+ figlio.getSiacDClassTipo().getClassifTipoCode() + " id: " + figlio.getClassifId() + " code: "
										+ figlio.getClassifCode() + " desc: " + figlio.getClassifDesc());
					}
					
					return figlio;
				}
				
				return recursiveFindClassificatore(figlio, tipoClassificatore.getClassTipoFiglio(), Arrays.copyOfRange(classifCodes, 1, classifCodes.length));
				
			}
		}
		
		log.debug(methodName,"Nessun figlio trovato con il codice "+ classifCodes[0]);
		throw new IllegalStateException("Impossibile trovare il classificatore di tipo "+tipoClassificatore + " con codice: "+classifCodes[0] + " [remaining size:"+classifCodes.length+"]");
	}
	
	
	
	/**
	 * Ricerca un classificatore gerarchico restituendo tutti i suoi padri in una mappa.
	 *
	 * @param classifId the classif id
	 * @return Mappa con chiave il codice tipo classificatore e valore l'id del classificatore
	 */
	@Override
	public  Map<String, SiacTClass> ricercaClassificatoriMapByClassId(Integer classifId){
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
		SiacTClass classif = siacTClassRepository.findOne(classifId);		
		ricercaClassifRicorsivaByClassifId(result,classif);
		return result;
	}


	/**
	 * Ricerca ricorsivamente tutti i padri di un classificatore.
	 *
	 * @param result the result
	 * @param classif the classif
	 */
	@Override
	public void ricercaClassifRicorsivaByClassifId(Map<String, SiacTClass> result, SiacTClass classif) {
		if(classif==null){
			return;
		}
		
		SiacDClassTipo classifTipo = classif.getSiacDClassTipo();
		result.put(classifTipo.getClassifTipoCode(), classif);
		
		if(SiacDClassTipoEnum.byCodice(classifTipo.getClassifTipoCode()).getClassTipoPadre()!=null){
			SiacTClass classPadre = siacTClassRepository.findPadreClassificatoreByClassifId(classif.getUid());
			ricercaClassifRicorsivaByClassifId(result,classPadre);
		}
		
	}


	@Override
	public Page<SiacTClass> ricercaSinteticaClassificatore(Integer enteProprietarioId, SiacDClassTipoEnum siacDClassTipoEnum, Integer anno, String classifCode, String classifDesc, Pageable pageable) {
		final String methodName = "ricercaSinteticaClassificatore";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaClassificatore(jpql, param, enteProprietarioId, siacDClassTipoEnum.getCodice(), anno, classifCode, classifDesc);
		
		// TODO: order by
		//jpql.append(" ORDER BY d.docAnno, d.docNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaClassificatore(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId,
			String classifTipoCode, Integer anno, String classifCode, String classifDesc) {
		
		jpql.append("FROM SiacTClass tc ");
		jpql.append(" WHERE tc.dataCancellazione IS NULL ");
		jpql.append(" AND tc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND tc.siacDClassTipo.classifTipoCode = :classifTipoCode ");
		jpql.append(getSiacTClassDataValiditaSql("tc", "anno"));
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("classifTipoCode", classifTipoCode);
		param.put("anno", anno);
		
		if(StringUtils.isNotBlank(classifCode)) {
			jpql.append(" AND tc.classifCode = :classifCode ");
			param.put("classifCode", classifCode);
		}
		if(StringUtils.isNotBlank(classifDesc)) {
			jpql.append(" AND ");
			jpql.append(Utility.toJpqlSearchLikePercent("tc.classifDesc", ":classifDesc"));
			param.put("classifDesc", classifDesc);
		}
	}


	@Override
	public List<SiacTClass> findClassifByEnteAndClassifTipoCodes(Integer enteProprietarioId, Integer anno,Collection<String> classifTipoCodes){
		Query query = entityManager.createQuery("SELECT c " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno") +
				" ORDER BY c.siacDClassTipo.classifTipoCode, c.classifCode ");
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		query.setParameter("classifTipoCodes", classifTipoCodes);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	@Override
	public List<SiacDClassTipo> findClassTipoByEnteAndAnnoAndTipoCode(Integer enteProprietarioId, Integer anno,	Collection<String> classifTipoCodes){
		Query query = entityManager.createQuery("SELECT c " +
				" FROM SiacDClassTipo c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.classifTipoCode IN (:classifTipoCodes) " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno") +
				" ORDER BY c.classifTipoCode ");

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		query.setParameter("classifTipoCodes", classifTipoCodes);
		
		@SuppressWarnings("unchecked")
		List<SiacDClassTipo> list = query.getResultList();
		
		return list;
	}


	@Override
	public Long countByEnteProprietarioIdAndClassifTipoCodesAndAnno(Integer enteProprietarioId,	Collection<String> classifTipoCodes, Integer anno){
		Query query = entityManager.createQuery("SELECT COALESCE(COUNT(c), 0) " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno"));

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifTipoCodes", classifTipoCodes);
		query.setParameter("anno", anno);
		
		return (Long) query.getSingleResult();
	}


	@Override
	public List<SiacTClass> findClassifByEnteAndClassifCodeAndClassifTipoCodes(Integer enteProprietarioId, String classifCode, Integer anno, Collection<String> classifTipoCodes){
		Query query = entityManager.createQuery("SELECT c " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.classifCode = :classifCode " +
				" AND c.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno") +
				" ORDER BY c.siacDClassTipo.classifTipoCode, c.classifCode ");

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifCode", classifCode);
		query.setParameter("anno", anno);
		query.setParameter("classifTipoCodes", classifTipoCodes);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	/**
	 * Find by class tipo and by anno and by ente proprietario id.
	 *
	 * @param classifTipoCodice the classif tipo codice
	 * @param anno the anno
	 * @param enteProprietarioIid the ente proprietario iid
	 * @return the list
	 */
	@Override
	public List<SiacTClass> findByClassTipoAndByAnnoAndByEnteProprietarioId(String classifTipoCodice, Integer anno,	Integer enteProprietarioId){
		Query query = entityManager.createQuery("SELECT c " 
				+ " FROM SiacTClass c " 
				+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
				+ " AND c.siacDClassTipo.classifTipoCode = :classifTipoCode " 
				+ " AND c.dataCancellazione IS NULL " 
				+ getSiacTClassDataValiditaSql("c", "anno")
				+ " ORDER BY c.classifCode ");
		
		query.setParameter("classifTipoCode", classifTipoCodice);
		query.setParameter("anno", anno);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	@Override
	public SiacTClass findClassifByCodiceAndEnteAndTipoCode(String classifCode, Integer enteProprietarioId,	Integer anno, String classifTipoCode){
		String methodName = "findClassifByCodiceAndEnteAndTipoCode";

		String jpql = "SELECT c " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.classifCode = :classifCode " +
				" AND c.siacDClassTipo.classifTipoCode = :classifTipoCode " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno");
		
		Query query = entityManager.createQuery(jpql);
		
		query.setParameter("classifCode", classifCode);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifTipoCode", classifTipoCode);
		query.setParameter("anno", anno);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> resultList = query.getResultList();

		if(resultList == null || resultList.isEmpty()) {
			return null;
		}
		if(resultList.size() > 1) {
			log.warn(methodName, "Too many results found for JPQL [" + jpql 
					+ "] (classifCode = " + classifCode + ", # of results " + resultList.size() + "). Only one was expected: returning the first");
		}

		return resultList.get(0);
	}


	/**
	 * Find classif by ente and tipo code.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param classifTipoCode the classif tipo code
	 * @return the list
	 */
	@Override
	public List<SiacTClass> findClassifByEnteAndTipoCode(Integer enteProprietarioId, Integer anno, String classifTipoCode){
		Query query = entityManager.createQuery("SELECT c " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND C.siacDClassTipo.classifTipoCode = :classifTipoCode " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno") +
				" ORDER BY c.classifCode ");
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		query.setParameter("classifTipoCode", classifTipoCode);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	@Override
	public List<SiacTClass> findClassifByCodiceAndEnteAndTipoCodeLike(String classifCode, Integer enteProprietarioId, Integer anno, Collection<String> classifTipoCodes){
		Query query = entityManager.createQuery("SELECT c " +
				" FROM SiacTClass c " +
				" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
				" AND c.classifCode = :classifCode " +
				" AND c.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
				" AND c.dataCancellazione IS NULL " +
				getSiacTClassDataValiditaSql("c", "anno"));
		
		query.setParameter("classifCode", classifCode);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		query.setParameter("classifTipoCodes", classifTipoCodes);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	/**
	 * Find classificatori by classif id padre.
	 *
	 * @param classifIdPadre the classif id padre
	 * @param enteProprietarioId the ente proprietario id
	 * @param anno the anno
	 * @return the list
	 */
	@Override
	public List<SiacTClass> findClassificatoriByClassifIdPadre(Integer classifIdPadre, Integer enteProprietarioId, String anno) {
		Query query = entityManager.createQuery("SELECT r.siacTClassFiglio "
				+ " FROM SiacRClassFamTree r, SiacTClass c "
				+ " WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND r.siacTClassPadre.classifId = :classifIdPadre "
				+ " AND r.siacTClassFiglio.dataCancellazione IS NULL "
				+ " AND c = r.siacTClassFiglio "
				+ getSiacTClassDataValiditaSql("c", "anno")
				+ " ORDER BY r.siacTClassFiglio.classifCode");
		
		query.setParameter("classifIdPadre", classifIdPadre);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}


	/**
	 * Find by classif code and clasif tipo code and fam code.
	 *
	 * @param classifCode the classif code
	 * @param classifTipoCode the classif tipo code
	 * @param famCode the fam code
	 * @return the list
	 */
	@Override
	public List<SiacTClass> findByClassifCodeAndClasifTipoCodeAndFamCode(String classifCode, String classifTipoCode,String famCode, Integer enteProprietarioId, String anno){
		Query query = entityManager.createQuery("SELECT c "
				+ " FROM SiacTClass c, SiacRClassFamTree t, SiacDClassFam td "
				+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND c.classifId = t.siacTClassFiglio.classifId "
				+ " AND t.siacTClassFamTree.siacDClassFam.classifFamId = td.classifFamId "
				+ " AND td.classifFamCode = :famCode "
				+ " AND c.siacDClassTipo.classifTipoCode LIKE concat(:classifTipoCode, '%')  " // aggiunto % per piano dei conti PDC_XXX
				+ getSiacTClassDataValiditaSql("c", "anno") 
				+ " AND c.classifCode = :classifCode ");
		
		query.setParameter("famCode", famCode);
		query.setParameter("classifCode", classifCode);
		query.setParameter("classifTipoCode", classifTipoCode);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", Integer.valueOf(anno));
		
		@SuppressWarnings("unchecked")
		List<SiacTClass> list = query.getResultList();
		
		return list;
	}
}
