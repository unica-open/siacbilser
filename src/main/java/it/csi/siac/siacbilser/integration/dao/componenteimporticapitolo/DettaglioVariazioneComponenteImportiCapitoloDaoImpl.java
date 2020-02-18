/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DettaglioVariazioneComponenteImportiCapitoloDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioVariazioneComponenteImportiCapitoloDaoImpl extends JpaDao<SiacTBilElemDetVarComp, Integer> implements DettaglioVariazioneComponenteImportiCapitoloDao {
	
	@Override
	public SiacTBilElemDetVarComp create(SiacTBilElemDetVarComp c){
		Date now = new Date();
		
		c.setDataModificaInserimento(now);
		c.setUid(null);
		
		super.save(c);
		return c;
	}

	@Override
	public SiacTBilElemDetVarComp update(SiacTBilElemDetVarComp d){
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		
		super.update(d);
		return d;
	}

	@Override
	public SiacTBilElemDetVarComp deleteLogically(SiacTBilElemDetVarComp d) {
		Date now = new Date();
		d.setDataCancellazioneIfNotSet(now);
		
		super.update(d);
		return d;
	}

	@Override
	public SiacTBilElemDetVarComp deleteLogically(Integer id) {
		SiacTBilElemDetVarComp currentRecord = findById(id);
		if(currentRecord == null) {
			throw new IllegalArgumentException("No record found for id " + id);
		}
		return deleteLogically(findById(id));
	}

}
