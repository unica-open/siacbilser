/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.capitolo;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;

public abstract class SiacTBilElemCapitoloMapper<B extends Capitolo<?, ?>> extends SiacTBaseEntitaMapper<SiacTBilElem, B> {

	@Override
	public void map(SiacTBilElem a, B b) {
		super.map(a, b);
		b.setAnnoCapitolo(Integer.parseInt(a.getSiacTBil().getSiacTPeriodo().getAnno()));
		b.setNumeroCapitolo(Integer.parseInt(a.getElemCode()));
		b.setNumeroArticolo(Integer.parseInt(a.getElemCode2()));
	}
}