/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneBeneficiario;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ConciliazionePerBeneficiarioDaoImpl.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConciliazionePerBeneficiarioDaoImpl extends JpaDao<SiacRConciliazioneBeneficiario, Integer> implements ConciliazionePerBeneficiarioDao {
	
	@Override
	public SiacRConciliazioneBeneficiario create(SiacRConciliazioneBeneficiario r) {
		Date now = new Date();
		r.setDataModificaInserimento(now);
		r.setUid(null);
		super.save(r);
		return r;
	}

	@Override
	public SiacRConciliazioneBeneficiario update(SiacRConciliazioneBeneficiario r) {
		Date now = new Date();
		r.setDataModificaAggiornamento(now);
		super.update(r);
		return r;
	}

	@Override
	public void elimina(SiacRConciliazioneBeneficiario entity) {
		SiacRConciliazioneBeneficiario e = this.findById(entity.getUid());
		Date now = new Date();
		e.setDataCancellazioneIfNotSet(now);
	}

	@Override
	public Page<SiacRConciliazioneBeneficiario> ricercaSinteticaConciliazioniPerBeneficiario(int enteProprietarioId, Integer soggettoId, String elemTipoCode, String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, Pageable pageable) {
		final String methodName = "ricercaSinteticaConciliazioniPerBeneficiario";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaConciliazioniPerBeneficiario(jpql, param, enteProprietarioId, soggettoId, elemTipoCode, annoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB);
		
//		jpql.append(" ORDER BY  ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaConciliazioniPerBeneficiario( StringBuilder jpql, Map<String, Object> param,
			int enteProprietarioId, Integer soggettoId, String elemTipoCode, String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB) {

		jpql.append(" FROM SiacRConciliazioneBeneficiario cb ");
		jpql.append(" WHERE cb.dataCancellazione IS NULL");
		jpql.append(" AND cb.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if( soggettoId != null && soggettoId != 0){
			jpql.append(" AND cb.siacTSoggetto.soggettoId = :soggettoId ");
			param.put("soggettoId", soggettoId);			
		}
		
		if(StringUtils.isNotBlank(elemTipoCode)) {
			jpql.append(" AND cb.siacTBilElem.siacDBilElemTipo.elemTipoCode = :elemTipoCode ");
			param.put("elemTipoCode", elemTipoCode);
		}
		
		if(StringUtils.isNotBlank(annoCapitolo)) {
			jpql.append(" AND cb.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoBilancio ");
			param.put("annoBilancio", annoCapitolo);
		}
		if(StringUtils.isNotBlank(numeroCapitolo)) {
			jpql.append(" AND cb.siacTBilElem.elemCode = :numeroCapitolo ");
			param.put("numeroCapitolo", numeroCapitolo);
		}
		if(StringUtils.isNotBlank(numeroArticolo)) {
			jpql.append(" AND cb.siacTBilElem.elemCode2 = :numeroArticolo ");
			param.put("numeroArticolo", numeroArticolo);
		}
		if(StringUtils.isNotBlank(numeroUEB)) {
			jpql.append(" AND cb.siacTBilElem.elemCode3 = :numeroUEB ");
			param.put("numeroUEB", numeroUEB);
	    }
		
	}
}
