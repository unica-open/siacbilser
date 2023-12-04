/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.integration.dad.mapper.base;


import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.mapper.ente.SiacTEnteProprietarioEnteMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteBase;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siaccorser.model.EntitaEnte;

public abstract class SiacTEnteBaseEntitaEnteMapper<A extends SiacTEnteBase, B extends EntitaEnte> extends SiacTBaseEntitaMapper<A, B> {

	
	@Autowired private SiacTEnteProprietarioEnteMapper siacTEnteProprietarioEnteMapper;
	@Override
	public void map(A a, B b) {
		super.map(a, b);
		b.setEnte(siacTEnteProprietarioEnteMapper.map(a.getSiacTEnteProprietario()));
	}

}
