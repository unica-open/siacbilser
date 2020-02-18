/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AmmortamentoAnnuoCespiteDaoImpl extends JpaDao<SiacTCespitiAmmortamento, Integer> implements AmmortamentoAnnuoCespiteDao {
	
	public SiacTCespitiAmmortamento create(SiacTCespitiAmmortamento e){
		Date now = new Date();
		e.setDataModificaInserimento(now);		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	public SiacTCespitiAmmortamento update(SiacTCespitiAmmortamento e){		
		SiacTCespitiAmmortamento eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		e.setDataInizioValidita(eAttuale.getDataInizioValidita());
		e.setDataModifica(now);
		entityManager.flush();		
		super.update(e);
		return e;
	}
	
	@Override
	public SiacTCespitiAmmortamento delete(int uidAmmortamento, String loginOperazione) {		
		SiacTCespitiAmmortamento eAttuale = this.findById(uidAmmortamento);		
		Date now = new Date();		
		eAttuale.setDataCancellazioneIfNotSet(now);
		eAttuale.setLoginOperazione(loginOperazione);
		super.update(eAttuale);		
		return eAttuale;
	}
	
}
