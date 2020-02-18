/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioAmmortamentoAnnuoCespiteDaoImpl extends JpaDao<SiacTCespitiAmmortamentoDett, Integer> implements DettaglioAmmortamentoAnnuoCespiteDao {
	
	public SiacTCespitiAmmortamentoDett create(SiacTCespitiAmmortamentoDett e){
		Date now = new Date();
		e.setDataModificaInserimento(now);		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	public SiacTCespitiAmmortamentoDett update(SiacTCespitiAmmortamentoDett e){		
		SiacTCespitiAmmortamentoDett eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		e.setDataInizioValidita(eAttuale.getDataInizioValidita());
		e.setDataModifica(now);
		entityManager.flush();		
		super.update(e);
		return e;
	}
	
	@Override
	public SiacTCespitiAmmortamentoDett delete(int uidCespite, String loginOperazione) {		
		SiacTCespitiAmmortamentoDett eAttuale = this.findById(uidCespite);		
		Date now = new Date();		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		super.update(eAttuale);		
		return eAttuale;
	}

	@Override
	public Page<SiacTCespitiAmmortamentoDett> ricercaSinteticaDettagliAmmortamentoAnnuo(int enteProprietarioId, Integer uidCespite,Pageable pageable) {
		final String methodName = "ricercaSinteticaDettagliAmmortamentoAnnuo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaDettaglioAmmortamentoAnnuo(jpql, param, enteProprietarioId, uidCespite);
		
		jpql.append(" ORDER BY d.cesAmmDettAnno, d.cesAmmDettData ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaDettaglioAmmortamentoAnnuo(StringBuilder jpql, Map<String, Object> param,	Integer enteProprietarioId, Integer uidCespite) {
		
		jpql.append(" FROM SiacTCespitiAmmortamentoDett d ");
		jpql.append(" WHERE d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND d.siacTCespitiAmmortamento.dataCancellazione IS NULL ");
		param.put("enteProprietarioId", enteProprietarioId);
		if(uidCespite != null) {
			jpql.append(" AND d.siacTCespitiAmmortamento.siacTCespiti.cesId = :cesId ");
			param.put("cesId", uidCespite);
		}
	}

	@Override
	public List<SiacTCespitiAmmortamentoDett> ricercaDettagliAmmortamentoAnnuoByCespite(int enteProprietarioId, Integer uidCespite) {
		final String methodName="ricercaDettagliAmmortamentoAnnuoByCespite";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaDettaglioAmmortamentoAnnuo(jpql, param, enteProprietarioId, uidCespite);
		
		jpql.append(" ORDER BY d.cesAmmDettAnno, d.cesAmmDettData ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		Query query = createQuery(jpql.toString(), param);
		@SuppressWarnings("unchecked")
		List<SiacTCespitiAmmortamentoDett> res = query.getResultList();
		return res;
	}
	
}
