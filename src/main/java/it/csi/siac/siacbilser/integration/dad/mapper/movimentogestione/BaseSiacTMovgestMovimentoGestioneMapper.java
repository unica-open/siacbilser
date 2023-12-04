/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.mapper.base.SiacTEnteBaseEntitaEnteMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.bilancio.SiacTBilBilancioMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class BaseSiacTMovgestMovimentoGestioneMapper<MG extends MovimentoGestione> extends SiacTEnteBaseEntitaEnteMapper<SiacTMovgest, MG> {
	
	private @Autowired SiacTBilBilancioMapper siacTBilBilancioMapper;
	
	@Override
	public void map(SiacTMovgest s,MG d) {
		super.map(s, d);
		
		d.setAnnoMovimento(s.getMovgestAnno());
		d.setNumeroBigDecimal(s.getMovgestNumero());
		d.setBilancio(siacTBilBilancioMapper.map(s.getSiacTBil()));
	}
}
