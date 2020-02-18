/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDVincoloGenere;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloGenere;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.model.GenereVincolo;
import it.csi.siac.siacbilser.model.Vincolo;

/**
 * Converte da Vincolo a SiacTVincolo e viceversa. Il campo di interesse &eacute; il genereVincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/07/2017
 */
@Component
public class VincoloGenereVincoloConverter extends ExtendedDozerConverter<Vincolo, SiacTVincolo> {
	
	/**
	 * Instantiates a new vincolo genere vincolo converter.
	 */
	public VincoloGenereVincoloConverter() {
		super(Vincolo.class, SiacTVincolo.class);
	}

	@Override
	public Vincolo convertFrom(SiacTVincolo src, Vincolo dest) {
		if(src.getSiacRVincoloGeneres() == null) {
			return dest;
		}
		for(SiacRVincoloGenere siacRVincoloGenere : src.getSiacRVincoloGeneres()) {
			if(siacRVincoloGenere.getDataCancellazione() == null) {
				// TODO: usare dozer?
				GenereVincolo genereVincolo = new GenereVincolo();
				genereVincolo.setUid(siacRVincoloGenere.getSiacDVincoloGenere().getVincoloGenId().intValue());
				genereVincolo.setCodice(siacRVincoloGenere.getSiacDVincoloGenere().getVincoloGenCode());
				genereVincolo.setDescrizione(siacRVincoloGenere.getSiacDVincoloGenere().getVincoloGenDesc());
				
				dest.setGenereVincolo(genereVincolo);
				break;
			}
		}
		
		
		return dest;
	}

	@Override
	public SiacTVincolo convertTo(Vincolo src, SiacTVincolo dest) {
		if(src.getGenereVincolo() == null || src.getGenereVincolo().getUid() == 0) {
			return dest;
		}
		
		SiacRVincoloGenere siacRVincoloGenere = new SiacRVincoloGenere();
		
		siacRVincoloGenere.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVincoloGenere.setLoginOperazione(dest.getLoginOperazione());
		siacRVincoloGenere.setSiacTVincolo(dest);
		
		SiacDVincoloGenere siacDVincoloGenere = new SiacDVincoloGenere();
		siacDVincoloGenere.setUid(src.getGenereVincolo().getUid());
		siacRVincoloGenere.setSiacDVincoloGenere(siacDVincoloGenere);
		
		List<SiacRVincoloGenere> siacRVincoloGeneres = new ArrayList<SiacRVincoloGenere>();
		siacRVincoloGeneres.add(siacRVincoloGenere);
		dest.setSiacRVincoloGeneres(siacRVincoloGeneres);
		
		return dest;
	}
	

}
