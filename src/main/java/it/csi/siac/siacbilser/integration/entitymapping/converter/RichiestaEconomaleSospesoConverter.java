/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconSospesa;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.Sospeso;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleSospesoConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleSospesoConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacTRichiestaEconSospesas() == null || src.getSiacTRichiestaEconSospesas().isEmpty()){
			return dest;
		}
		Sospeso sospeso = new Sospeso();
		for(SiacTRichiestaEconSospesa siacTRichiestaEconSospesa : src.getSiacTRichiestaEconSospesas()){
			if(siacTRichiestaEconSospesa.getDataCancellazione() == null){
				sospeso.setNumeroSospeso(siacTRichiestaEconSospesa.getRiceconsNumero());
				dest.setSospeso(sospeso);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		
		if(src.getSospeso() == null){
			return dest;
		}
		List<SiacTRichiestaEconSospesa> siacTRichiestaEconSospesas = new ArrayList<SiacTRichiestaEconSospesa>();
		
		SiacTRichiestaEconSospesa siacTRichiestaEconSospesa= new SiacTRichiestaEconSospesa();
		siacTRichiestaEconSospesa.setRiceconsNumero(src.getSospeso().getNumeroSospeso());
		siacTRichiestaEconSospesa.setLoginOperazione(dest.getLoginOperazione());
		siacTRichiestaEconSospesa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacTRichiestaEconSospesa.setSiacTRichiestaEcon(dest);
		siacTRichiestaEconSospesas.add(siacTRichiestaEconSospesa);
		dest.setSiacTRichiestaEconSospesas(siacTRichiestaEconSospesas);
		return dest;
	}



	

}
