/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTIndirizzoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siaccorser.model.Ente;

/**
 * The Class EnteIndirizzoPrincipaleConverter.
 */
@Component
public class EnteIndirizzoPrincipaleConverter extends ExtendedDozerConverter<Ente, SiacTEnteProprietario> {
	
	/**
	 * Instantiates a new causale entrata accertamento converter.
	 */
	public EnteIndirizzoPrincipaleConverter() {
		super(Ente.class, SiacTEnteProprietario.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Ente convertFrom(SiacTEnteProprietario src, Ente dest) {
		// Indirizzo principale dell'Ente: siac_t_indirizzo_soggetto.principale
		if(src.getSiacRSoggettoEnteProprietarios() != null) {
			SiacRSoggettoEnteProprietario srsep = findSiacRSoggettoEnteProprietario(src);
			
			// Se non ho trovato il legame, esco
			if(srsep == null) {
				return dest;
			}
			
			SiacTIndirizzoSoggetto stis = findSiacTIndirizzoSoggetto(srsep);
			
			if(stis == null) {
				return dest;
			}
			
			//dest.setIndirizzo(stis.getPrincipale());
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
		for(SiacRSoggettoEnteProprietario srsep : src.getSiacRSoggettoEnteProprietarios()) {
			if(srsep.getDataCancellazione() == null || srsep.getDataCancellazione().after(now)) {
				return srsep;
			}
		}
		return null;
	}
	
	/**
	 * Cerca l'entity di indirizzo.
	 * 
	 * @param src l'ente da cui partire per la ricerca
	 * 
	 * @return la relazione trovata
	 */
	private SiacTIndirizzoSoggetto findSiacTIndirizzoSoggetto(SiacRSoggettoEnteProprietario srsep) {
		SiacTSoggetto sts = srsep.getSiacTSoggetto();
		if(sts != null && sts.getSiacTIndirizzoSoggettos() != null) {
			Date now = new Date();
			for(SiacTIndirizzoSoggetto stis : sts.getSiacTIndirizzoSoggettos()) {
				if(stis.getDataCancellazione() == null || stis.getDataCancellazione().after(now)) {
					return stis;
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTEnteProprietario convertTo(Ente src, SiacTEnteProprietario dest) {
		return dest;
	}

}
