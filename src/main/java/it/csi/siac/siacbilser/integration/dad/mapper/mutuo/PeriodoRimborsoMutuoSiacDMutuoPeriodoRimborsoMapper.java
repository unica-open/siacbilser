/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoPeriodoRimborso;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaSiacTBaseMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PeriodoRimborsoMutuoSiacDMutuoPeriodoRimborsoMapper extends EntitaSiacTBaseMapper<PeriodoRimborsoMutuo, SiacDMutuoPeriodoRimborso> {
	
	@Override
	public void map(PeriodoRimborsoMutuo s, SiacDMutuoPeriodoRimborso d) {
		super.map(s, d);
		
		d.setMutuoPeriodoRimborsoCode(s.getCodice());
		d.setMutuoPeriodoRimborsoDesc(s.getDescrizione());
		d.setMutuoPeriodoNumeroMesi(s.getNumeroMesi());
	}
}
