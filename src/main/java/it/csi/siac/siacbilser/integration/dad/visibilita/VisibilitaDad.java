/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.visibilita;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.visibilita.VisibilitaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTVisibilita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.visibilita.Visibilita;
import it.csi.siac.siacbilser.model.visibilita.modeldetail.VisibilitaModelDetail;

/**
 * The Class VisibilitaDad.
 * @author interlogic
 * @version 1.0.0 - 10/05/2021
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VisibilitaDad extends ExtendedBaseDadImpl  {
	
	@Autowired private VisibilitaDao visibilitaDao;

	public List<Visibilita> getListVisibilita(Visibilita template, VisibilitaModelDetail... modelDetails) {
		List<SiacTVisibilita> siacTVisibilitas = visibilitaDao.search(
				template.getAzione() != null ? template.getAzione().getUid() : null,
				template.getFunzionalita(),
				ente.getUid());
		
		return convertiLista(siacTVisibilitas, Visibilita.class, BilMapId.SiacTVisibilita_Visibilita_ModelDetail, Converters.byModelDetails(modelDetails));
	}

}
