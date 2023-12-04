/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		return findCodificheByTipoElemBilancio(anno, enteProprietarioId, codiceTipoElemBilancio, null);
	}
	
	@Override
	public List<SiacTClass> findCodificheByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio, String classifTipoCode) {
		final String methodName = "findCodificheByTipoElemBilancio";
		
		log.debugStart(methodName, "");
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("SELECT c FROM SiacTClass c")
		.append(" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
		.append(" AND c.siacDClassTipo.dataCancellazione IS NULL ")
		.append(" AND c.dataCancellazione IS NULL" )
		.append(getSiacTClassDataValiditaSql("c", "anno"));
	
		if (codiceTipoElemBilancio != null) {
			jpql.append(" AND EXISTS(FROM c.siacDClassTipo.siacRBilElemTipoClassTips rbetct "
				+ " WHERE rbetct.dataCancellazione IS NULL "
				+ "            AND rbetct.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
				+ ")");
		}
	
		if (classifTipoCode != null) {
			jpql.append(" AND c.siacDClassTipo.classifTipoCode= :classifTipoCode");
		}		
		
		jpql.append(" ORDER BY c.siacDClassTipo.classifTipoCode, c.classifCode");
		
		TypedQuery<SiacTClass> query = entityManager.createQuery(jpql.toString(), SiacTClass.class);
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("anno", anno);
		
		if (classifTipoCode != null) {
			query.setParameter("classifTipoCode", classifTipoCode);
		}
		
		if (codiceTipoElemBilancio != null) {
			query.setParameter("elemTipoCode", codiceTipoElemBilancio);
		}
		
		List<SiacTClass> siacTClasses = query.getResultList();
		
		return siacTClasses;
	}
	
	@Override
	public List<SiacTClass> findCodificheGenericiTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio, String classifTipoCode) {
		final String methodName = "findCodificheGenericiTipoElemBilancio";
		
		log.debug(methodName, "Delegating to findCodificheByTipoElemBilancio");
		return findCodificheByTipoElemBilancio(anno, enteProprietarioId, codiceTipoElemBilancio, classifTipoCode);
	}
	
	
	
	
	@Override
	public List<SiacTClass> findCodificheConLivelloByTipoElemBilancio(
			int anno, int enteProprietarioId, String codiceTipoElemBilancio) {

		final String methodName = "findCodificheConLivelloByTipoElemBilancio";
		log.debugStart(methodName, "");
		
		TypedQuery<SiacTClass> query = entityManager
				.createQuery("SELECT c FROM SiacTClass c " 
						+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " AND EXISTS ( "
						+ "		FROM c.siacRClassFamTreesFiglio f "
						+ " 	WHERE f.siacTClassPadre.classifId IS NULL "
						+ " ) "
						+ " AND EXISTS ( "
						+ " 	FROM SiacDBilElemTipo bet "
						+ "		WHERE bet.elemTipoCode = :elemTipoCode "
						+ "		AND bet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ " 	AND EXISTS ("
						+ " 		FROM bet.siacRBilElemTipoClassTips betct "
						+ "			WHERE betct.siacDClassTipo = c.siacDClassTipo "
						+ "  		AND betct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
						+ "		)"
						+ " ) "
						+ " AND c.dataCancellazione IS NULL "
						+ getSiacTClassDataValiditaSql("c", "anno")
						+ " ORDER BY c.classifCode",
						SiacTClass.class);

		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("elemTipoCode", codiceTipoElemBilancio);
		
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

		query.setParameter("anno", anno);
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("classifFamCode", famigliaTreeCodice);
		if(idCodificaPadre != null  && idCodificaPadre != 0 ){
			query.setParameter("classifIdPadre", idCodificaPadre);
		}
		
		List<SiacTClass> siacTClasses = query.getResultList();
		
		return siacTClasses;
	}
	

	

}
