/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.entitymapping;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacintegser.model.integ.StrutturaAmministrativa;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class StrutturaAmministrativaStrutturaAmministrativoContabileMapper 
	extends BaseMapper<StrutturaAmministrativa, StrutturaAmministrativoContabile> {

	@Override
	public void map(StrutturaAmministrativa o1, StrutturaAmministrativoContabile o2) {
		if (o2 == null || o1 == null) {
			return;
		}
		
		o2.setCodice(o1.getCodice());
		o2.setDescrizione(o1.getDescrizione());
		o2.setTipoClassificatore(new TipoClassificatore(o1.getCodiceTipoStruttura()));
	}
}
