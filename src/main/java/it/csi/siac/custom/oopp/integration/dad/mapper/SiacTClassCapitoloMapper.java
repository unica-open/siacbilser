/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTClassHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.integ.Capitolo;
import it.csi.siac.siacintegser.model.integ.Categoria;
import it.csi.siac.siacintegser.model.integ.Macroaggregato;
import it.csi.siac.siacintegser.model.integ.Missione;
import it.csi.siac.siacintegser.model.integ.Programma;
import it.csi.siac.siacintegser.model.integ.TipoFinanziamento;
import it.csi.siac.siacintegser.model.integ.Tipologia;
import it.csi.siac.siacintegser.model.integ.Titolo;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassCapitoloMapper extends BaseMapper<SiacTClass, Capitolo> {

	@Autowired private SiacTClassHelper siacTClassHelper;

	@Override
	public void map(List<SiacTClass> siacTClassList, Capitolo capitolo) {
		
		super.map(siacTClassList, capitolo);

		map(siacTClassHelper.getSiacTClassGerarchia(siacTClassList, SiacDClassTipoEnum.Macroaggregato, SiacDClassTipoEnum.TitoloSpesa), capitolo);
		map(siacTClassHelper.getSiacTClassGerarchia(siacTClassList, SiacDClassTipoEnum.Programma, SiacDClassTipoEnum.Missione), capitolo);
		SiacTClass tipologia = siacTClassHelper.getSiacTClassGerarchia(siacTClassList, SiacDClassTipoEnum.Categoria, SiacDClassTipoEnum.Tipologia);
		map(tipologia, capitolo);
		map(siacTClassHelper.getSiacTClassGerarchia(tipologia, SiacDClassTipoEnum.TitoloEntrata), capitolo);
	}
	
	@Override
	public void map(SiacTClass o1, Capitolo o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TipoFinanziamento)) {
			o2.setTipoFinanziamento(new TipoFinanziamento(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

//		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.CofogGruppo)) {
//			o2.setCofog(new Cofog(o1.getClassifCode(), o1.getClassifDesc()));
//			return;
//		}
//
		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Macroaggregato)) {
			o2.setMacroaggregato(new Macroaggregato(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Programma)) {
			o2.setProgramma(new Programma(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Categoria)) {
			o2.setCategoria(new Categoria(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TitoloEntrata) ||
			check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TitoloSpesa)) {
			o2.setTitolo(new Titolo(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Missione)) {
			o2.setMissione(new Missione(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Tipologia)) {
			o2.setTipologia(new Tipologia(o1.getClassifCode(), o1.getClassifDesc()));
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
