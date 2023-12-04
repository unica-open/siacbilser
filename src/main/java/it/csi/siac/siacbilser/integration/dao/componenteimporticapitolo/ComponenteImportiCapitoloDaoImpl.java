/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ComponenteImportiCapitoloDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ComponenteImportiCapitoloDaoImpl extends JpaDao<SiacTBilElemDetComp, Integer> implements ComponenteImportiCapitoloDao {
	
	@Override
	public SiacTBilElemDetComp create(SiacTBilElemDetComp c){
		Date now = new Date();
		
		c.setDataModificaInserimento(now);
		c.setUid(null);
		
		super.save(c);
		return c;
	}

	@Override
	public SiacTBilElemDetComp update(SiacTBilElemDetComp d){
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		
		super.update(d);
		return d;
	}

}
