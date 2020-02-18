/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacREventoCausale;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;

@Component
public class CausaleEPEventoConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
//	<a>this</a> <!-- classificatoriEP -->
//	<b>this</b> <!-- siacREventoCausalees -->
	
	public CausaleEPEventoConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro() != null ? dest.getDataInizioValiditaFiltro() : Utility.ultimoGiornoDellAnno(Utility.BTL.get().getAnno());
		
		if (src.getSiacREventoCausales() != null) {
			for (SiacREventoCausale siacREventoCausale : src.getSiacREventoCausales()) {
				if (siacREventoCausale.getDataCancellazione() != null
						 || !siacREventoCausale.isDataValiditaCompresa(dataInizioValiditaFiltro)) {
					continue;
				}

				SiacDEvento siacDEvento = siacREventoCausale.getSiacDEvento();

				Evento evento = new Evento();
				map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
				dest.addEvento(evento);
			}
		}

		return dest;
	}

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		final String methodName = "convertTo";
		
		dest.setSiacREventoCausales(new ArrayList<SiacREventoCausale>());
		
		for (Evento evento : src.getEventi()) {
			addEvento(dest, evento);
			log.debug(methodName, "aggiunto evento: "+ evento.getUid());
		}
		
		return dest;
	}

	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addEvento(SiacTCausaleEp dest, Evento src) {
		
		if(src==null || src.getUid()==0){
			return;
		}
		
		SiacREventoCausale siacREventoCausale = new SiacREventoCausale();
		
		SiacDEvento siacDEvento = new SiacDEvento();
		siacDEvento.setUid(src.getUid());	
		siacREventoCausale.setSiacDEvento(siacDEvento);
		
		siacREventoCausale.setLoginOperazione(dest.getLoginOperazione());
		siacREventoCausale.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacREventoCausale(siacREventoCausale);
	}


	

}
