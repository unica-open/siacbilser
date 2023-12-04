/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttoAmmHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacintegser.model.custom.oopp.Provvedimento;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTAttoAmmProvvedimentoMapper extends BaseMapper<SiacTAttoAmm, Provvedimento> {

	@Autowired private SiacTAttoAmmHelper siacTAttoAmmHelper;
	
	@Override
	public void map(SiacTAttoAmm o1, Provvedimento o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setAnno(NumberUtil.safeParseInt(o1.getAttoammAnno()));
		o2.setNumero(o1.getAttoammNumero());
		o2.setCodiceTipo(o1.getSiacDAttoAmmTipo().getAttoammTipoCode());
		o2.setStrutturaAmministrativa(new SiacTClassStrutturaAmministrativaMapper().map(siacTAttoAmmHelper.getSiacTClass(o1)));
		
	}

	public Provvedimento map(SiacTAttoAmm o1, SiacTMovgest siacTMovgest) {

		Provvedimento o2 = super.map(o1);
		
		if (o2 == null) {
			return null;
		}
		
		o2.setDataEsecutivita(siacTMovgest.getParereFinanziarioDataModifica());

		return o2;
	}

}
