/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.integration.dad.mapper.base;


import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.mapper.ente.EnteSiacTEnteProprietarioMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteBase;
import it.csi.siac.siaccommonser.util.mapper.EntitaSiacTBaseMapper;
import it.csi.siac.siaccorser.model.EntitaEnte;

public abstract class EntitaEnteSiacTEnteBaseMapper<A extends EntitaEnte, B extends SiacTEnteBase> extends EntitaSiacTBaseMapper<A, B> {

	
	@Autowired private EnteSiacTEnteProprietarioMapper enteSiacTEnteProprietarioMapper;
	@Override
	public void map(A a, B b) {
		super.map(a, b);
		b.setSiacTEnteProprietario(enteSiacTEnteProprietarioMapper.map(a.getEnte()));
	}

}
