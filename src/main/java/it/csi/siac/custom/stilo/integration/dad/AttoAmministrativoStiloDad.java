/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integration.dad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dao.provvedimento.SiacTAttoAmmDao;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AttoAmministrativoStiloDad extends AttoAmministrativoDad {

	@Autowired
	@Qualifier("siacTAttoAmmStiloDaoImpl")
	private SiacTAttoAmmDao siacTAttoAmmDao;
	
	@Override
	protected Boolean isAnnullabileAttoAmm(AttoAmministrativo attoAmministrativo) {
		return siacTAttoAmmDao.isAnnullabileAttoAmm(attoAmministrativo.getUid());
	}
	
	public void annullaMovimentiGestioneCollegatiAllAttoAmm(AttoAmministrativo attoAmministrativo) {
		siacTAttoAmmDao.annullaMovimentiGestioneCollegatiAllAttoAmm(attoAmministrativo.getUid(), loginOperazione);
	}
}
