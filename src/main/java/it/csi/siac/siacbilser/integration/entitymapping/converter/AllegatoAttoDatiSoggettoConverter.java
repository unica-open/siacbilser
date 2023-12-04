/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoSog;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;

@Component
public class AllegatoAttoDatiSoggettoConverter extends ExtendedDozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	
	/**
	 * Instantiates a new AllegatoAtto attr converter.
	 */
	public AllegatoAttoDatiSoggettoConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		List<DatiSoggettoAllegato> datiSoggettiAllegati = new ArrayList<DatiSoggettoAllegato>();
		for(SiacRAttoAllegatoSog sraas : src.getSiacRAttoAllegatoSogs()){
			if(sraas.getDataCancellazione()!=null){
				continue;
			}
			DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
			map(sraas, dsa, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);					
			datiSoggettiAllegati.add(dsa);
		}
		dest.setDatiSoggettiAllegati(datiSoggettiAllegati);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		dest.setSiacRAttoAllegatoSogs(new ArrayList<SiacRAttoAllegatoSog>());
		for(DatiSoggettoAllegato dsa : src.getDatiSoggettiAllegati()) {
			SiacRAttoAllegatoSog siacRAttoAllegatoSog = new SiacRAttoAllegatoSog();
			dsa.setEnte(src.getEnte());
			map(dsa, siacRAttoAllegatoSog, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);	
			siacRAttoAllegatoSog.setSiacTAttoAllegato(dest);
			siacRAttoAllegatoSog.setUid(null);
			siacRAttoAllegatoSog.setLoginOperazione(dest.getLoginOperazione());
			dest.addSiacRAttoAllegatoSog(siacRAttoAllegatoSog);
		}
		return dest;
	}
	

}
