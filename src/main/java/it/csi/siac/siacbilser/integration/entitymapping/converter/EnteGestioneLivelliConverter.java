/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRGestioneEnte;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * The Class EnteGestioneLivelloConverter.
 * 
 * @author Domenico
 */
@Component
public class EnteGestioneLivelliConverter extends ExtendedDozerConverter<Ente, SiacTEnteProprietario> {
	
	/**
	 * Instantiates a new ente gestione livello converter.
	 */
	public EnteGestioneLivelliConverter() {
		super(Ente.class, SiacTEnteProprietario.class);
	}

	@Override
	public Ente convertFrom(SiacTEnteProprietario src, Ente dest) {
		String methodName = "convertFrom";
		
		dest.setGestioneLivelli(new HashMap<TipologiaGestioneLivelli, String>());
		Date now = new Date();
		if(src.getSiacRGestioneEntes() != null) {
			for(SiacRGestioneEnte rge : src.getSiacRGestioneEntes()) {
				if(rge.getDataCancellazione()==null && (rge.getDataFineValidita() == null || rge.getDataFineValidita().compareTo(now)>=0 )){
					String codiceTipoGestione = rge.getSiacDGestioneLivello().getSiacDGestioneTipo().getGestioneTipoCode();
					TipologiaGestioneLivelli tipo = TipologiaGestioneLivelli.fromCodice(codiceTipoGestione);
					dest.getGestioneLivelli().put(tipo, rge.getSiacDGestioneLivello().getGestioneLivelloCode());
				}
			}
		}
		
		log.trace(methodName, "Gestione livelli: "+dest.getGestioneLivelli());
		
		return dest;
	}

	

	@Override
	public SiacTEnteProprietario convertTo(Ente src, SiacTEnteProprietario dest) {
		return dest;
	}

}
