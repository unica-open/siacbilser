/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siaccorser.model.Ente;

/**
 * The Class EntePartitaIvaConverter.
 */
@Component
public class EntePartitaIvaConverter extends ExtendedDozerConverter<Ente, SiacTEnteProprietario> {
	
	/**
	 * Instantiates a new causale entrata accertamento converter.
	 */
	public EntePartitaIvaConverter() {
		super(Ente.class, SiacTEnteProprietario.class);
	}

	@Override
	public Ente convertFrom(SiacTEnteProprietario src, Ente dest) {
		// partita IVA dell'Ente: siac_t_soggetto.partita_iva
		if(src.getSiacRSoggettoEnteProprietarios() != null) {
			SiacRSoggettoEnteProprietario srsep = findSiacRSoggettoEnteProprietario(src);
			
			// Se non ho trovato il legame, esco
			if(srsep == null) {
				return dest;
			}
			
			dest.setPartitaIva(srsep.getSiacTSoggetto().getPartitaIva());
		}
		return dest;
	}

	/**
	 * Cerca l'entity di relazione.
	 * 
	 * @param src l'ente da cui partire per la ricerca
	 * 
	 * @return la relazione trovata
	 */
	private SiacRSoggettoEnteProprietario findSiacRSoggettoEnteProprietario(SiacTEnteProprietario src) {
		Date now = new Date();
		for(int i = 0; i < src.getSiacRSoggettoEnteProprietarios().size(); i++) {
			SiacRSoggettoEnteProprietario temp = src.getSiacRSoggettoEnteProprietarios().get(i);
			if(temp.getDataCancellazione() == null || temp.getDataCancellazione().after(now)) {
				return temp;
			}
		}
		return null;
	}
	

	@Override
	public SiacTEnteProprietario convertTo(Ente src, SiacTEnteProprietario dest) {
		return dest;
	}

}
