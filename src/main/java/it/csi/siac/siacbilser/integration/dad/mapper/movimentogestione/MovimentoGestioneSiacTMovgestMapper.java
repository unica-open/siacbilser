/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import it.csi.siac.siacbilser.integration.dad.mapper.base.EntitaEnteSiacTEnteBaseMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class MovimentoGestioneSiacTMovgestMapper<MG extends MovimentoGestione> extends EntitaEnteSiacTEnteBaseMapper<MG, SiacTMovgest> {
	
	@Override
	public void map(MG s, SiacTMovgest d) {
		super.map(s, d);
		d.setMovgestAnno(s.getAnnoMovimento());
		d.setMovgestNumero(s.getNumeroBigDecimal());
	}
}
