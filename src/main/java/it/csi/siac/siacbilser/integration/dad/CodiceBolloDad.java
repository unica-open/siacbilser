/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDCodicebolloRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCodicebollo;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.CodiceBollo;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per il Codice Bollo.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodiceBolloDad extends BaseDadImpl {
	
	/** The siac d codicebollo repository. */
	@Autowired
	private SiacDCodicebolloRepository siacDCodicebolloRepository;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<CodiceBollo> ricercaCodiciBollo(Ente ente) {
		
		List<SiacDCodicebollo> elencoCodiciBolloDB = siacDCodicebolloRepository.findCodiciBolloByEnte(ente.getUid());
		if(elencoCodiciBolloDB == null) {
			return new ArrayList<CodiceBollo>();
		}
		
		List<CodiceBollo> elencoCodiciBolloReturn = new ArrayList<CodiceBollo>(elencoCodiciBolloDB.size());
		
		for (SiacDCodicebollo codBollo : elencoCodiciBolloDB) {
			
			CodiceBollo codiceBolloToAdd = mapCodiceBollo(codBollo);
			elencoCodiciBolloReturn.add(codiceBolloToAdd);
		}
		return elencoCodiciBolloReturn;
	}


	/**
	 * Map codice bollo.
	 *
	 * @param codBolloDB the cod bollo db
	 * @return the codice bollo
	 */
	private CodiceBollo mapCodiceBollo(SiacDCodicebollo codBolloDB) {
		CodiceBollo codiceBolloToAdd = new CodiceBollo();
		codiceBolloToAdd.setUid(codBolloDB.getUid());
		codiceBolloToAdd.setCodice(codBolloDB.getCodbolloCode());
		codiceBolloToAdd.setDescrizione(codBolloDB.getCodbolloDesc());
		return codiceBolloToAdd;
	}


	public CodiceBollo findCodiceBolloByUid(Integer uid) {
		SiacDCodicebollo siacDCodicebollo = siacDCodicebolloRepository.findOne(uid);
		return mapCodiceBollo(siacDCodicebollo);
		
	}
}
