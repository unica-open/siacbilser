/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRComuneProvincia;
import it.csi.siac.siacbilser.integration.entity.SiacTIndirizzoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTProvincia;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

/**
 * The Class IndirizzoSoggettoProvinciaConverter.
 */
@Component
public class IndirizzoSoggettoProvinciaConverter extends DozerConverter<IndirizzoSoggetto, SiacTIndirizzoSoggetto> {
	
	/**
	 * Instantiates a new indirizzo soggetto provincia converter.
	 */
	public IndirizzoSoggettoProvinciaConverter() {
		super(IndirizzoSoggetto.class, SiacTIndirizzoSoggetto.class);
	}

	@Override
	public IndirizzoSoggetto convertFrom(SiacTIndirizzoSoggetto src, IndirizzoSoggetto dest) {
		if(src.getSiacTComune() != null && src.getSiacTComune().getSiacRComuneProvincias() != null) {
			for(SiacRComuneProvincia srcp : src.getSiacTComune().getSiacRComuneProvincias()) {
				if(srcp.getDataCancellazione() == null) {
					SiacTProvincia stp = srcp.getSiacTProvincia();
					// XXX: questo campo potrebbe essere corretto. Ma non ne sono certo
					dest.setProvincia(stp.getSiglaAutomobilistica());
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTIndirizzoSoggetto convertTo(IndirizzoSoggetto src, SiacTIndirizzoSoggetto dest) {
		return dest;
	}
	
}
