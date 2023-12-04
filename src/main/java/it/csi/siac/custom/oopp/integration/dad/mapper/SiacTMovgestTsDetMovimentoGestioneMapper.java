/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;

@Component("SiacTMovgestTsDetMovimentoGestioneOOPPMapper")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestTsDetMovimentoGestioneMapper extends BaseMapper<SiacTMovgestTsDet, MovimentoGestione> {

	@Override
	public void map(SiacTMovgestTsDet o1, MovimentoGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (checkSiacDMovgestTsDetTipo(o1.getSiacDMovgestTsDetTipo(), SiacDMovgestTsDetTipoEnum.Iniziale)) {
			o2.setImportoIniziale(o1.getMovgestTsDetImporto());
		}

		if (checkSiacDMovgestTsDetTipo(o1.getSiacDMovgestTsDetTipo(), SiacDMovgestTsDetTipoEnum.Attuale)) {
			o2.setImportoAttuale(o1.getMovgestTsDetImporto());
		}
	}

	private boolean checkSiacDMovgestTsDetTipo(SiacDMovgestTsDetTipo siacDMovgestTsDetTipo, SiacDMovgestTsDetTipoEnum siacDMovgestTsDetTipoEnum) {
		return siacDMovgestTsDetTipo != null && siacDMovgestTsDetTipo.isEntitaValida() && 
				siacDMovgestTsDetTipoEnum.getCodice().equals(siacDMovgestTsDetTipo.getMovgestTsDetTipoCode());
	}

}
