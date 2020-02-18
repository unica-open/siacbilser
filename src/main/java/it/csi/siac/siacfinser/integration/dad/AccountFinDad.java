/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.integration.dao.common.SiacTAccountFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTAccountFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;

/**
 * The Class AccountDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccountFinDad extends BaseDadImpl  {
	
	/** The siac t bil repository. */
	@Autowired
	private SiacTAccountFinRepository siacTAccountRepository;

	public Ente findEnteAssocciatoAdAccount(int uidAccount){
		SiacTAccountFin siacTAccount = siacTAccountRepository.findOne(uidAccount);
		if(siacTAccount==null){
			throw new IllegalArgumentException("Impossibile trovare Account con uid: "+uidAccount);
		}
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTAccount.getSiacTEnteProprietario();
		return mapNotNull(siacTEnteProprietario,Ente.class,FinMapId.SiacTEnteProprietario_Ente_GestioneLivelliFin);
	}
   
		
}
