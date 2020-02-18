/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaDaoImpl extends JpaDao<SiacTAccFondiDubbiaEsig, Integer> implements AccantonamentoFondiDubbiaEsigibilitaDao {
	
	@Override
	public SiacTAccFondiDubbiaEsig create(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig){
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataModificaInserimento(now);
		
		if(siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs() != null) {
			for(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig : siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs()) {
				siacRBilElemAccFondiDubbiaEsig.setDataModificaInserimento(now);
			}
		}
		
		siacTAccFondiDubbiaEsig.setUid(null);
		
		super.save(siacTAccFondiDubbiaEsig);
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public SiacTAccFondiDubbiaEsig update(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig){
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsigAttuale = this.findById(siacTAccFondiDubbiaEsig.getUid());
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataModificaAggiornamento(now);
		
		// Cancellazione vecchi dati
		if(siacTAccFondiDubbiaEsigAttuale.getSiacRBilElemAccFondiDubbiaEsigs() != null) {
			for(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig : siacTAccFondiDubbiaEsigAttuale.getSiacRBilElemAccFondiDubbiaEsigs()) {
				siacRBilElemAccFondiDubbiaEsig.setDataCancellazioneIfNotSet(now);
			}
		}
		
		// Inserimento nuovi dati
		if(siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs() != null) {
			for(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig : siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs()) {
				siacRBilElemAccFondiDubbiaEsig.setDataModificaInserimento(now);
			}
		}
		
		super.update(siacTAccFondiDubbiaEsig);
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public SiacTAccFondiDubbiaEsig logicalDelete(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig) {
		Date now = new Date();
		siacTAccFondiDubbiaEsig.setDataCancellazioneIfNotSet(now);
		
		// Cancellazione vecchi dati
		if(siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs() != null) {
			for(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig : siacTAccFondiDubbiaEsig.getSiacRBilElemAccFondiDubbiaEsigs()) {
				siacRBilElemAccFondiDubbiaEsig.setDataCancellazioneIfNotSet(now);
			}
		}
		return siacTAccFondiDubbiaEsig;
	}

	@Override
	public Page<SiacTAccFondiDubbiaEsig> ricercaSintetica(Integer enteProprietarioId, Integer bilId, String elemTipoCode, Pageable pageable) {
		
		final String methodName = "ricercaSintetica";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("SELECT tafde ");
		componiQueryRicercaSintetica(jpql, param, enteProprietarioId, bilId, elemTipoCode);
		jpql.append(" ORDER BY CAST(rbeafde.siacTBilElem.elemCode AS integer), CAST(rbeafde.siacTBilElem.elemCode2 AS integer), CAST(rbeafde.siacTBilElem.elemCode3 AS integer) ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	/**
	 * Compone la query per la ricerca sintetica.
	 * 
	 * @param jpql the jpql
	 * @param param the param
	 * @param bilId l'id del bilancio
	 */
	private void componiQueryRicercaSintetica(StringBuilder jpql, Map<String, Object> param, Integer enteProprietarioId, Integer bilId, String elemTipoCode) {
		
		jpql.append(" FROM SiacTAccFondiDubbiaEsig tafde, SiacRBilElemAccFondiDubbiaEsig rbeafde ");
		jpql.append(" WHERE tafde.dataCancellazione IS NULL ");
		jpql.append(" AND rbeafde.siacTAccFondiDubbiaEsig = tafde ");
		jpql.append(" AND rbeafde.dataCancellazione IS NULL ");
		jpql.append(" AND tafde.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND rbeafde.siacTBilElem.siacDBilElemTipo.elemTipoCode = :elemTipoCode ");
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("elemTipoCode", elemTipoCode);
		
		if(bilId != null){
			jpql.append(" AND rbeafde.siacTBilElem.siacTBil.bilId = :bilId ");
			
			param.put("bilId", bilId);
		}
	}

}
