/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.consultazioneentita;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.consultazioneentita.RicercaFigliEntitaConsultabileService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

public class ConsultazioneEntitaServiceTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaFigliEntitaConsultabileService ricercaFigliEntitaConsultabileService;
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Test
	public void testFunc() {
		final String methodName = "testFunc";
		Query q = entityManager.createNativeQuery("SELECT * FROM fnc_cons_entita_variazioni(:a,:b,:c) "/*, Object[].class*/);
//		Query q = entityManager.createNativeQuery("select bil_code, bil_desc from siac_t_bil");
		
		q.setParameter("a", 1);
		q.setParameter("b", 10);
		q.setParameter("c", 1);
		
		log.debug(methodName, ">>>>>>>>>>>>>>>" + q.getClass());
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.getResultList();
		
		for (Object[] result : resultList) {
			Object[] r = result;
			for (Object r1: r) {
				log.debug(methodName, r1 + "(" + (r1 != null ? r1.getClass() : "ND") + ") ");
			}
			log.debug(methodName, "");
		}
		log.debug(methodName, resultList);
	}
	
	@Test
	public void testFunc2a() {
		final String methodName = "testFunc2a";
		Integer enteProprietarioId = 1;
		List<Map<String, Object>> result = executeConsultazioneFunction("fnc_cons_entita_variazioni", enteProprietarioId, 1, 10, 1);
		
		log.debug(methodName, ">>>>>>>>>>> result: "+result);
		
	}

	private List<Map<String, Object>> executeConsultazioneFunction(String functionName, Integer enteProprietarioId,  Object... functionParams) {
		List<String> columns = getColumns(functionName, enteProprietarioId);
		List<Object[]> records = invokeFunctionWithTableResult(functionName, functionParams);
		List<Map<String, Object>> result = getListOfMap(columns, records);
		return result;
	}

	private List<Map<String, Object>> getListOfMap(List<String> columns, List<Object[]> records) {
		final String methodName = "getListOfMap";
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (Object[] record : records) {
			int i = 0;
			Map<String,Object> m = new HashMap<String,Object>();
			for (Object columnValue: record) {
				String key = columns.get(i);
				i++;
				m.put(key, columnValue);
				log.debug(methodName, key + ": " + columnValue + "(" + (columnValue != null ? columnValue.getClass() : "ND") + ") ");
			}
			log.debug(methodName, "");
			result.add(m);
		}
		return result;
	}

	private List<Object[]> invokeFunctionWithTableResult(String functionName, Object... params) {
		final String methodName = "invokeFunctionWithTableResult";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM "+functionName+"(");
		
		for (int i = 0; i < params.length; i++) {
			sql.append(":p" + i);
			if((i + 1) < params.length){
				sql.append(", ");
			}
		}
		sql.append(")");
		
		log.debug(methodName, "SQL: " + sql.toString());
		
		Query q = entityManager.createNativeQuery(sql.toString());
		
		for (int i = 0; i < params.length; i++) {
			q.setParameter("p"+i, params[i]);
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> records = q.getResultList();
		return records;
	}

	private List<String> getColumns(String functionName, Integer enteProprietarioId) {
		final String methodName = "getColumns";
		Query qColumns = entityManager.createNativeQuery("SELECT * FROM " + functionName + "_desc(:enteProprietarioId) ");
		qColumns.setParameter("enteProprietarioId", enteProprietarioId);
		// TODO valutare se eliminare questo parametro!
		// Tanto le colonne di ritorno della function vera e propria (quella senza suffisso desc) non possono essere dinamiche.
		
		Object[] columnsObjs = (Object[]) qColumns.getSingleResult();
		log.debug(methodName, "Colonne ottenute: "+columnsObjs);
		List<String> columns = new ArrayList<String>();
		for (int i = 0; i < columnsObjs.length; i++) {
			columns.add((String)columnsObjs[i]);
		}
		return columns;
	}
	
	@Test
	public void testFunc2() {
		final String methodName = "testFunc2";
		Session session = entityManager.unwrap(Session.class);
		SQLQuery q = session.createSQLQuery("SELECT * FROM fnc_cons_entita_variazioni(:a,:b,:c)");
		
		q.setParameter(1, 1);
		q.setParameter(2, 10);
		q.setParameter(3, 1);
		
		log.debug(methodName, "return aliases: " + q.getReturnAliases());
		
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.list();
		
		for (Object[] result : resultList) {
			Object[] r = result;
			for (Object r1: r) {
				log.debug(methodName, r1 + "("+(r1!=null?r1.getClass():"ND")+") ");
			}
			log.debug(methodName, "");
		}
	}
	
	@Test
	public void ricercaFigliEntitaConsultabile() {
		RicercaFigliEntitaConsultabile req = new RicercaFigliEntitaConsultabile();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setAnnoEsercizio(Integer.valueOf(2018));
		req.setEntitaDaCercare(TipoEntitaConsultabile.IMPEGNO);
//		req.setEntitaDaCercare(TipoEntitaConsultabile.ELENCO);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		EntitaConsultabile entitaPadre = new EntitaConsultabile();
		entitaPadre.setTipoEntitaConsultabile(TipoEntitaConsultabile.CAPITOLOSPESA);
		entitaPadre.setUid(97156);
		req.setEntitaPadre(entitaPadre);
		
		req.setRequestImporto(true);
		
		RicercaFigliEntitaConsultabileResponse res = ricercaFigliEntitaConsultabileService.executeService(req);
		assertNotNull(res);
	}
	
}
