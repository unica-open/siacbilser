/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.capitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTClassHelper;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassCapitoloEntrataGestioneMapper extends BaseMapper<SiacTClass, CapitoloEntrataGestione> {

	@Autowired private SiacTClassHelper siacTClassHelper;

	@Override
	public void map(List<SiacTClass> siacTClassList, CapitoloEntrataGestione capitolo) {
		
		super.map(siacTClassList, capitolo);

		SiacTClass tipologia = siacTClassHelper.getSiacTClassGerarchia(siacTClassList, SiacDClassTipoEnum.Categoria, SiacDClassTipoEnum.Tipologia);
		map(tipologia, capitolo);
		map(siacTClassHelper.getSiacTClassGerarchia(tipologia, SiacDClassTipoEnum.TitoloEntrata), capitolo);
	}
	
	@Override
	public void map(SiacTClass o1, CapitoloEntrataGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TipoFinanziamento)) {
			o2.setTipoFinanziamento(new TipoFinanziamento(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TitoloEntrata) ) {
			o2.setTitoloEntrata(new TitoloEntrata(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

	}

	private boolean check(SiacDClassTipo siacDClassTipo, SiacDClassTipoEnum siacDClassTipoEnum) {
		return 
			siacDClassTipo != null && 
			siacDClassTipo.isEntitaValida() && 
			siacDClassTipoEnum.getCodice().equals(siacDClassTipo.getClassifTipoCode());
	}


}
