/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfinser.model.TipoFonteDurc;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
public class SoggettoDatiDurcConverter extends ExtendedDozerConverter<Soggetto, SiacTSoggetto> {

	public SoggettoDatiDurcConverter() {
		super(Soggetto.class, SiacTSoggetto.class);
	}

	@Override
	public Soggetto convertFrom(SiacTSoggetto siacTSoggetto, Soggetto soggetto) {

		soggetto.setDataFineValiditaDurc(siacTSoggetto.getDataFineValiditaDurc());
		soggetto.setLoginModificaDurc(siacTSoggetto.getLoginModificaDurc());
		soggetto.setTipoFonteDurc(siacTSoggetto.getTipoFonteDurc());
		
		if (TipoFonteDurc.AUTOMATICA.getCodice().equals(siacTSoggetto.getTipoFonteDurc())) {
			soggetto.setDescrizioneFonteDurc(siacTSoggetto.getFonteDurcAutomatica());
		} 
		
		SiacTClass SiacTClass = siacTSoggetto.getFonteDurc();
		
		if (TipoFonteDurc.MANUALE.getCodice().equals(siacTSoggetto.getTipoFonteDurc()) && SiacTClass != null) {
				soggetto.setFonteDurcClassifId(SiacTClass.getClassifId());
				soggetto.setDescrizioneFonteDurc(String.format("%s - %s", SiacTClass.getClassifCode(), SiacTClass.getClassifDesc()));
		} 
		
		soggetto.setNoteDurc(siacTSoggetto.getNoteDurc());
		
		return soggetto;
	}

	@Override
	public SiacTSoggetto convertTo(Soggetto soggetto, SiacTSoggetto siacTSoggetto) {
		return siacTSoggetto;
	}





	

}
