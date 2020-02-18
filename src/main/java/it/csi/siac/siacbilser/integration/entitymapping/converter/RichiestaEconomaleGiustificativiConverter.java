/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleGiustificativiConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleGiustificativiConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacTGiustificativoDets() == null || src.getSiacTGiustificativoDets().isEmpty()){
			return dest;
		}
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		for(SiacTGiustificativoDet siacTGiustificativoDet : src.getSiacTGiustificativoDets()){
			if(siacTGiustificativoDet.getDataCancellazione() == null && siacTGiustificativoDet.getSiacTGiustificativo() == null){
				Giustificativo giustificativo = new Giustificativo();
				map(siacTGiustificativoDet,giustificativo,CecMapId.SiacTGiustificativoDet_Giustificativo_Medium);
				giustificativi.add(giustificativo);
			}
		}
		dest.setGiustificativi(giustificativi);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		if(src.getGiustificativi() == null || src.getGiustificativi().isEmpty()){
			return dest;
		}
		List<SiacTGiustificativoDet> siacTGiustificativoDets = new ArrayList<SiacTGiustificativoDet>();
		for(Giustificativo giustificativo : src.getGiustificativi()){
			giustificativo.setLoginOperazione(src.getLoginOperazione());
			giustificativo.setEnte(src.getEnte());
			SiacTGiustificativoDet siacTGiustificativoDet = new SiacTGiustificativoDet();
			map(giustificativo,siacTGiustificativoDet,CecMapId.SiacTGiustificativoDet_Giustificativo);
			siacTGiustificativoDet.setSiacTRichiestaEcon(dest);
			
			//TODO: da togliere! Per ora lo imposto così perché non è ancora da sviluppare ma su DB è NOT NULL
			//siacTGiustificativoDet.setGstInclusoInPag("S");
			
			siacTGiustificativoDets.add(siacTGiustificativoDet);
		}
		dest.setSiacTGiustificativoDets(siacTGiustificativoDets);
		return dest;
	}



	

}
