/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTTrasfMiss;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleDatiTrasfertaConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleDatiTrasfertaConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacTTrasfMisses() == null || src.getSiacTTrasfMisses().isEmpty()){
			return dest;
		}
		for(SiacTTrasfMiss siacTTrasfMiss : src.getSiacTTrasfMisses()){
			if(siacTTrasfMiss.getDataCancellazione() == null){
				DatiTrasfertaMissione dati = new DatiTrasfertaMissione();
				map(siacTTrasfMiss, dati, CecMapId.SiacTTrasfMiss_DatiTrasfertaMissione);
				dest.setDatiTrasfertaMissione(dati);
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		DatiTrasfertaMissione datiTrasfertaMissione = src.getDatiTrasfertaMissione();
		if(datiTrasfertaMissione == null){
			return dest;
		}
		
		datiTrasfertaMissione.setEnte(src.getEnte());
		datiTrasfertaMissione.setLoginOperazione(src.getLoginOperazione());
		
		SiacTTrasfMiss siacTTrasfMiss = new SiacTTrasfMiss();
		
		//Non più necessari.
		//siacTTrasfMiss.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		//siacTTrasfMiss.setLoginOperazione(dest.getLoginOperazione());
		
		map(datiTrasfertaMissione, siacTTrasfMiss, CecMapId.SiacTTrasfMiss_DatiTrasfertaMissione);
		
		dest.setSiacTTrasfMisses(new ArrayList<SiacTTrasfMiss>());
		dest.addSiacTTrasfMiss(siacTTrasfMiss);
		return dest;
		
	}



	

}
