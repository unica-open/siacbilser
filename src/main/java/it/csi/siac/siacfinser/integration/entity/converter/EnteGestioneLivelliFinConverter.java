/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.Date;
import java.util.HashMap;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRGestioneEnte;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;

/**
 * The Class EnteGestioneLivelloConverter.
 * 
 * @author Domenico
 */
@Component
public class EnteGestioneLivelliFinConverter extends DozerConverter<Ente, SiacTEnteProprietarioFin> {
	
	private LogUtil log =new LogUtil(EnteGestioneLivelliFinConverter.class);
	/**
	 * Instantiates a new ente gestione livello converter.
	 */
	public EnteGestioneLivelliFinConverter() {
		super(Ente.class, SiacTEnteProprietarioFin.class);
	}

	@Override
	public Ente convertFrom(SiacTEnteProprietarioFin src, Ente dest) {
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
		
		log.debug(methodName, "Gestione livelli: "+dest.getGestioneLivelli());
		
		return dest;
	}

	

	@Override
	public SiacTEnteProprietarioFin convertTo(Ente src, SiacTEnteProprietarioFin dest) {
		return dest;
	}

}
