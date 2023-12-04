/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dad.mapper.sac.SiacTClassStrutturaAmministrativoContabileMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttoAmmHelper;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTAttoAmmAttoAmministrativoMapper extends SiacTBaseEntitaMapper<SiacTAttoAmm, AttoAmministrativo> {
	
	@Autowired private SiacDAttoAmmTipoTipoAttoMapper siacDAttoAmmTipoTipoAttoMapper;
	@Autowired private SiacTClassStrutturaAmministrativoContabileMapper siacTClassStrutturaAmministrativoContabileMapper;
	
	@Autowired private SiacTAttoAmmHelper siacTAttoAmmHelper;

	@Override
	public void map(SiacTAttoAmm a, AttoAmministrativo b) {
		super.map(a, b);
		b.setAnno(Integer.parseInt(a.getAttoammAnno()));
		b.setNumero(a.getAttoammNumero());
		b.setTipoAtto(siacDAttoAmmTipoTipoAttoMapper.map(a.getSiacDAttoAmmTipo()));
		b.setStrutturaAmmContabile(siacTClassStrutturaAmministrativoContabileMapper.map(siacTAttoAmmHelper.getSiacTClass(a)));
		b.setOggetto(a.getAttoammOggetto());
	}
	
}


