/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.sac;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassStrutturaAmministrativoContabileMapper extends SiacTBaseEntitaMapper<SiacTClass, StrutturaAmministrativoContabile > {

	@Override
	public void map(SiacTClass a, StrutturaAmministrativoContabile b) {
		super.map(a, b);
		b.setCodice(a.getClassifCode());
		b.setDescrizione(a.getClassifDesc());
	}
}