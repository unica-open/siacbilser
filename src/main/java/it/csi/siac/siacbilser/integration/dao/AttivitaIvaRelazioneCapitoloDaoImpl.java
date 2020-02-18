/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemIvaAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class AttivitaIvaRelazioneCapitoloDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttivitaIvaRelazioneCapitoloDaoImpl extends JpaDao<SiacRBilElemIvaAttivita, Integer> implements AttivitaIvaRelazioneCapitoloDao {
	
	@Override
	public SiacRBilElemIvaAttivita create(SiacRBilElemIvaAttivita r) {
		Date now = new Date();
		r.setDataModificaInserimento(now);
		r.setUid(null);
		super.save(r);
		return r;
	}

	@Override
	public void delete(SiacRBilElemIvaAttivita r) {
		if(r!=null){
			Date now = new Date();
			r.setDataCancellazioneIfNotSet(now);
		}
	}

		
	
}
