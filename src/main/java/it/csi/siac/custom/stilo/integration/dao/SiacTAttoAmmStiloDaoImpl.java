/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integration.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.provvedimento.SiacTAttoAmmDaoImpl;

@Component
public class SiacTAttoAmmStiloDaoImpl extends SiacTAttoAmmDaoImpl {

	@Override
	public Boolean isAnnullabileAttoAmm(Integer attoAmmId) {
		
		final String methodName = "isAnnullabileAttoAmm";
		
		log.debug(methodName, "Calling functionName: fnc_stilo_siac_atto_amm_verifica_annullabilita for attoAmmId: "+ attoAmmId );
		String sql = "SELECT * FROM fnc_stilo_siac_atto_amm_verifica_annullabilita(:attoAmmId)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("attoAmmId", attoAmmId);
		
		Boolean result = (Boolean) query.getSingleResult();
		log.debug(methodName, "returning result: "+result);
		
		return result;	
	}

	@Override
	public void annullaMovimentiGestioneCollegatiAllAttoAmm(Integer attoAmmId, String loginOperazione) {

		final String methodName = "annullaMovimentiGestioneCollegatiAllAttoAmm";
		
		log.info(methodName, "Calling functionName: fnc_stilo_siac_atto_amm_annulla_movgest_collegati for attoAmmId: "+ attoAmmId 
				+ ", loginOperazione:" + loginOperazione);
		
		String sql = "SELECT * FROM fnc_stilo_siac_atto_amm_annulla_movgest_collegati(:attoAmmId,:loginOperazione)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("attoAmmId", attoAmmId);
		query.setParameter("loginOperazione", loginOperazione);
		
		Boolean result = (Boolean) query.getSingleResult();
		log.debug(methodName, "returning result: "+result);
	}
}
