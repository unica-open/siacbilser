/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacRMovgestTHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.BaseMovimentoGestione;
import it.csi.siac.siacintegser.model.custom.oopp.Vincolo;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMovgestTVincoloMapper extends BaseMapper<SiacRMovgestT, Vincolo> {

	@Autowired private SiacRMovgestTHelper siacRMovgestTHelper;
	
	@Override
	public void map(SiacRMovgestT o1, Vincolo o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		SiacTMovgest siacTMovgest = siacRMovgestTHelper.getSiacTMovgest(o1);
		
		if (siacTMovgest != null) {
			new BaseMovimentoGestioneMapper<BaseMovimentoGestione>().map(siacTMovgest, o2);
		}
		else {
			try {
				o2.setTipoMovimento(o1.getSiacTAvanzovincolo().getSiacDAvanzovincoloTipo().getAvavTipoDesc());
			} catch (NullPointerException npe) {}
		}
		
		o2.setImporto(o1.getMovgestTsImporto());
	}
}
