/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccorser.integration.dao.CodificaDaoImpl;

/**
 * The Class CodificaBilDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodificaBilDaoImpl extends CodificaDaoImpl implements CodificaBilDao {

	@Override
	public List<SiacTClass> findCodificheByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio) {
		final String methodName = "findCodificheByTipoElemBilancio";

		log.debugStart(methodName, "");

		
		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c FROM SiacTClass c, SiacDClassTipo d " 
//						" LEFT OUTER JOIN c.codificheFamigliaTreeDto cf " +
//						" LEFT OUTER JOIN c.padre.enteProprietario e "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						//+ " AND c.padre is null"
						//+ " AND c.codificaFamiglia is null"
						
//						+ " AND EXISTS ( "
//						+ " 	FROM SiacDBilElemTipo bet "
//						+ "		WHERE bet.elemTipoCode = :elemTipoCode "
//						+ "		AND bet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
//						+ " 	AND EXISTS ("
//						+ " 		FROM bet.siacRBilElemTipoClassTips betct "
//						+ "			WHERE betct.siacDClassTipo = c.siacDClassTipo "
//						+ "  		AND betct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
//						+ "		)"
//                      + " ) "
						
						+ " AND c.siacDClassTipo = d "
						+ " AND d.dataCancellazione IS NULL "
						+ " AND EXISTS(FROM d.siacRBilElemTipoClassTips rbetct "
						+ "            WHERE rbetct.dataCancellazione IS NULL "
						+ "            AND rbetct.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
						+ "           ) "



//							+ " select teb from TipoElementoBilancioDto teb, "
//							+ " IN(teb.tipoElementoBilancio) r "
//							+ " where teb.elemTipoCode=:codiceTipoElemBilancio "
//							+ " AND r.tipoClassificatore=c.tipoClassificatore "
//							+ " AND c.enteProprietario=c.tipoClassificatore.enteProprietario "
//							+ " AND c.enteProprietario=teb.enteProprietario "
//							+ " AND c.enteProprietario=r.enteProprietario "
						
						+ " AND c.dataCancellazione IS NULL "
//						+ " AND c.dataInizioValidita BETWEEN :startAnnoEsercizioDate AND :endAnnoEsercizioDate "
						
//						+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
//						+ " AND  (c.dataFineValidita is null or c.dataFineValidita >= :endAnnoEsercizioDate)"
						
						//+ " AND c.dataInizioValidita <= (SELECT MAX(c2.dataInizioValidita) FROM SiacTClass c2 where c2.dataInizioValidita < :endAnnoEsercizioDate) "
						
						// Pre - SIAC-1630
						// + " AND (c.dataFineValidita = :endAnnoEsercizioDate OR c.dataFineValidita IS NULL) "
						// Post -SIAC-1630
						+ getSiacTClassDataValiditaSql("c", "anno")

						//+ " ORDER BY c.classifCode",
						+ " ORDER BY d.classifTipoCode, c.classifCode",
						SiacTClass.class);

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		
//		GregorianCalendar gc = new GregorianCalendar(anno, Calendar.DECEMBER, 31);		
//		Date endAnnoEsercizioDate = gc.getTime();

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("elemTipoCode", codiceTipoElemBilancio);
		//query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);
		
		long time = System.currentTimeMillis();
		List<SiacTClass> siacTClasses = query.getResultList();
		log.debug(methodName, "Query time: " + (System.currentTimeMillis() - time));

		if (log.isDebugEnabled()) {
			for (SiacTClass siacTClass : siacTClasses) {
				log.debug(methodName, String.format("siacTClass.siacDClassTipo: %s %s -- s",
						siacTClass.getSiacDClassTipo().getClassifTipoCode(), 
						siacTClass.getSiacDClassTipo().getClassifTipoDesc()));
			}
		}

		return siacTClasses;
	}
	
	
	@Override
	public List<SiacTClass> findCodificheGenericiTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio) {
		final String methodName = "findCodificheGenericiTipoElemBilancio";
		/*
		log.debugStart("findCodificheByTipoElemBilancio", "");

		
		TypedQuery<SiacTClass> query = entityManager
				.createQuery("select c from CodificaGenericaDto c " 
						+ " where c.enteProprietario.uid = :enteProprietarioId "
						+ " AND  EXISTS ( "
						+ " select teb from TipoElementoBilancioDto teb, "
						+ " IN(teb.tipoElementoBilancio) r "
						+ " where teb.elemTipoCode=:codiceTipoElemBilancio "
						+ " AND r.tipoClassificatore=c.tipoClassificatore "
						+ " AND c.enteProprietario=c.tipoClassificatore.enteProprietario "
						+ " AND c.enteProprietario=teb.enteProprietario "
						+ " AND c.enteProprietario=r.enteProprietario "
						+ ")"
						+ " AND c.dataCancellazione is null "
						
						//+ " AND c.dataInizioValidita <= (SELECT MAX(c2.dataInizioValidita) FROM SiacTClass c2 where c2.dataInizioValidita < :endAnnoEsercizioDate) "
						
						+ " AND (c.dataFineValidita = :endAnnoEsercizioDate OR c.dataFineValidita IS NULL) "

						+ " order by c.codice",
						SiacTClass.class);

		GregorianCalendar gc = new GregorianCalendar(anno, Calendar.DECEMBER, 31);		
		Date endAnnoEsercizioDate = gc.getTime();

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("codiceTipoElemBilancio", codiceTipoElemBilancio);
		query.setParameter("endAnnoEsercizioDate", endAnnoEsercizioDate);
		
		List<SiacTClass> dtos = query.getResultList();

		return dtos;*/
		log.debug(methodName, "Delegating to findCodificheByTipoElemBilancio");
		return findCodificheByTipoElemBilancio(anno, enteProprietarioId, codiceTipoElemBilancio);
	}
	
	
	
	
	@Override
	public List<SiacTClass> findCodificheConLivelloByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio) {

		final String methodName = "findCodificheConLivelloByTipoElemBilancio";
		log.debugStart(methodName, "");
		
		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c FROM SiacTClass c " 
//						" LEFT OUTER JOIN c.codificheFamigliaTreeDto cf " +
//						" LEFT OUTER JOIN c.padre.enteProprietario e "
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
//						+ " AND c.padre is null"
						+ " AND EXISTS ( "
						+ "		FROM c.siacRClassFamTreesFiglio f "
						+ " 	WHERE f.siacTClassPadre.classifId IS NULL "
						+ " ) "
						
						// XXX: nella query originale c'era, ma richiedeva fosse null una colonna not null. Controllare
//						+ " AND c.codificaFamiglia is null"
//						+ " AND EXISTS ( "
//						+ " 	FROM c.siacRClassFamTreesFiglio f "
//						+ " 	WHERE f.siacTClassFamTree.siacDClassFam IS NULL "
//						+ " ) "
						
						+ " AND EXISTS ( "
						+ " 	FROM SiacDBilElemTipo bet "
						+ "		WHERE bet.elemTipoCode = :elemTipoCode "
						+ "		AND bet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " 	AND EXISTS ("
						+ " 		FROM bet.siacRBilElemTipoClassTips betct "
						+ "			WHERE betct.siacDClassTipo = c.siacDClassTipo "
						+ "  		AND betct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ "		)"
//							+ " select teb from TipoElementoBilancioDto teb, "
//							+ " IN(teb.tipoElementoBilancio) r "
//							+ " where teb.elemTipoCode=:codiceTipoElemBilancio "
//							+ " AND r.tipoClassificatore=c.tipoClassificatore "
//							+ " AND c.enteProprietario=c.tipoClassificatore.enteProprietario "
//							+ " AND c.enteProprietario=teb.enteProprietario "
//							+ " AND c.enteProprietario=r.enteProprietario "
						+ " ) "
						+ " AND c.dataCancellazione IS NULL "
//						+ " AND c.dataInizioValidita BETWEEN :startAnnoEsercizioDate AND :endAnnoEsercizioDate "
						
//						+ " AND c.dataInizioValidita <= :startAnnoEsercizioDate "
//						+ " AND  (c.dataFineValidita is null or c.dataFineValidita >= :endAnnoEsercizioDate)"
						
						//+ " AND c.dataInizioValidita <= (SELECT MAX(c2.dataInizioValidita) FROM SiacTClass c2 where c2.dataInizioValidita < :endAnnoEsercizioDate) "
						
						
						//+ " AND (c.dataFineValidita = :endAnnoEsercizioDate OR c.dataFineValidita IS NULL) "
						+ getSiacTClassDataValiditaSql("c", "anno")

						+ " ORDER BY c.classifCode",
						SiacTClass.class);

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		
//		GregorianCalendar gc = new GregorianCalendar(anno, Calendar.DECEMBER, 31);		
//		Date endAnnoEsercizioDate = gc.getTime();

		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("elemTipoCode", codiceTipoElemBilancio);
		
		//query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);

		List<SiacTClass> siacTClasses = query.getResultList();

		if (log.isDebugEnabled()) {
			for (SiacTClass siacTClass : siacTClasses) {
				log.debug(methodName, String.format("siacTClass.siacDClassTipo: %s %s -- s",
						siacTClass.getSiacDClassTipo().getClassifTipoCode(), 
						siacTClass.getSiacDClassTipo().getClassifTipoDesc()));
			}
		}
		return siacTClasses;
	}
	
	
	@Override
	public List<SiacTClass> findTreeByCodiceFamiglia(int anno,
			int enteProprietarioId, String famigliaTreeCodice, Integer idCodificaPadre, boolean senzaPadre) {
		final String methodName = "findTreeByCodiceFamiglia";
		log.debug(methodName, "ente:" + enteProprietarioId + " famigliaTreeCodice:" + famigliaTreeCodice + " idCodificaPadre:" + idCodificaPadre);

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT c ");
		jpql.append(" from SiacTClass c ");
		
//		if(idCodificaPadre != null && idCodificaPadre != 0 ){
//			jpql.append(", IN (c.codificheFiglie) cf ");
//		}
		
		jpql.append(" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND EXISTS ( ");
		jpql.append(" 	FROM c.siacRClassFamTreesFiglio f ");
		jpql.append("	WHERE f.siacTClassFamTree.siacDClassFam.classifFamCode = :classifFamCode ");
		jpql.append("	AND f.siacTClassFamTree.siacDClassFam.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("	AND f.siacTClassFamTree.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" ) ");
		
		if(senzaPadre){
			jpql.append(" AND EXISTS ( ");
			jpql.append(" 	FROM c.siacRClassFamTreesFiglio f ");
			jpql.append("	WHERE f.siacTClassPadre.classifId IS NULL ");
			jpql.append(" ) ");
		}
		
		if(idCodificaPadre != null  && idCodificaPadre != 0 ){
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM c.siacRClasses2 b ");
			jpql.append("     WHERE b.siacTClassA.classifId = :classifIdPadre ");
			jpql.append("     AND b.siacTClassA.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			jpql.append("     AND b.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			jpql.append("     AND b.dataCancellazione IS NULL ");

			jpql.append(getSiacTClassDataValiditaSql("b", "anno"));
			
			jpql.append(" ) ");
		}
		
		//jpql.append(" AND (c.dataFineValidita = :endAnnoEsercizioDate OR c.dataFineValidita IS NULL) ");
		jpql.append(getSiacTClassDataValiditaSql("c", "anno"));
		
		jpql.append(" ORDER BY c.classifCode ");
		
		TypedQuery<SiacTClass> query = entityManager.createQuery(jpql.toString(), SiacTClass.class);	

//		Date startAnnoEsercizioDate = new YearDateConverter().convert(anno);
//		Date endAnnoEsercizioDate = DateUtils.addDays(
//				DateUtils.addYears(startAnnoEsercizioDate, 1), -1);
		
//		GregorianCalendar gc = new GregorianCalendar(anno, Calendar.DECEMBER, 31);		
//		Date endAnnoEsercizioDate = gc.getTime();
		

		//query.setParameter("startAnnoEsercizioDate", startAnnoEsercizioDate);
		query.setParameter("anno", anno);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifFamCode", famigliaTreeCodice);
//		query.setParameter("famigliaTreeCodice", Integer.parseInt(famigliaTreeCodice));
		if(idCodificaPadre != null  && idCodificaPadre != 0 ){
			query.setParameter("classifIdPadre", idCodificaPadre);
		}
		
		List<SiacTClass> siacTClasses = query.getResultList();
		
		return siacTClasses;
	}
	

	

}
