/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.contotesoreria;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDContotesoreria;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDContoTesoreriaContotesoreriaMapper extends SiacTBaseEntitaMapper<SiacDContotesoreria, ContoTesoreria> {
	
	@Override
	public void map(SiacDContotesoreria a, ContoTesoreria b) {
		super.map(a, b);
		b.setCodice(a.getContotesCode());
		b.setDescrizione(a.getContotesDesc());
	}
}


