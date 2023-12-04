/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconSog;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class RichiestaEconomaleSoggettoConverter.
 */
@Component
public class RichiestaEconomaleSoggettoConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {
	
	/**
	 * Instantiates a new richiesta economale soggetto converter.
	 */
	public RichiestaEconomaleSoggettoConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacRRichiestaEconSogs() == null){
			return dest;
		}
		for(SiacRRichiestaEconSog siacRRichiestaEconSog : src.getSiacRRichiestaEconSogs()){
			if(siacRRichiestaEconSog.getDataCancellazione() == null){
				Soggetto soggetto = new Soggetto();
				map(siacRRichiestaEconSog.getSiacTSoggetto(), soggetto, BilMapId.SiacTSoggetto_Soggetto_Matricola);
				dest.setSoggetto(soggetto);
				break;
			}
		}
		return dest;
	}

	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		if(src.getSoggetto() == null || src.getSoggetto().getUid() == 0) { //Facoltativo nel caso in cui il soggetto arrivi da HR.
			return dest;
		}
		List<SiacRRichiestaEconSog> siacRRichiestaEconSogs= new ArrayList<SiacRRichiestaEconSog>();
		SiacRRichiestaEconSog siacRRichiestaEconSog = new SiacRRichiestaEconSog();
		
		siacRRichiestaEconSog.setSiacTRichiestaEcon(dest);
		siacRRichiestaEconSog.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRRichiestaEconSog.setLoginOperazione(dest.getLoginOperazione());
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());
		siacRRichiestaEconSog.setSiacTSoggetto(siacTSoggetto);
		
		siacRRichiestaEconSogs.add(siacRRichiestaEconSog);
		dest.setSiacRRichiestaEconSogs(siacRRichiestaEconSogs);
		
		return dest;
	}

}
