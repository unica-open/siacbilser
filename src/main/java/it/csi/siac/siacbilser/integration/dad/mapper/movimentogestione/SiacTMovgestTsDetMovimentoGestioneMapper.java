/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestTHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacfinser.model.MovimentoGestione;

@Component("SiacTMovgestTsDetMovimentoGestioneMapper")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestTsDetMovimentoGestioneMapper<MG extends MovimentoGestione> extends BaseMapper<SiacTMovgestTsDet, MG> {

	@Autowired SiacTMovgestTHelper siacTMovgestTHelper;
	
	@Override
	public void map(SiacTMovgestTsDet o1, MG o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (siacTMovgestTHelper.checkSiacDMovgestTsDetTipo(o1.getSiacDMovgestTsDetTipo(), SiacDMovgestTsDetTipoEnum.Iniziale)) {
			o2.setImportoIniziale(o1.getMovgestTsDetImporto());
		}

		if (siacTMovgestTHelper.checkSiacDMovgestTsDetTipo(o1.getSiacDMovgestTsDetTipo(), SiacDMovgestTsDetTipoEnum.Attuale)) {
			o2.setImportoAttuale(o1.getMovgestTsDetImporto());
		}
	}

}
