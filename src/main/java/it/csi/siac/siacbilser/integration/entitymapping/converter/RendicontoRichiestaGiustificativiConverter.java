/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RendicontoRichiestaGiustificativiConverter extends ExtendedDozerConverter<RendicontoRichiesta, SiacTGiustificativo > {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RendicontoRichiestaGiustificativiConverter() {
		super(RendicontoRichiesta.class, SiacTGiustificativo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RendicontoRichiesta convertFrom(SiacTGiustificativo src, RendicontoRichiesta dest) {
		
		if(src.getSiacTGiustificativoDets()==null){
			return null;
		}
		
		for(SiacTGiustificativoDet gd : src.getSiacTGiustificativoDets()){
			if(gd.getDataCancellazione()!=null){
				continue;
			}
			Giustificativo giustificativo = map(gd, Giustificativo.class, CecMapId.SiacTGiustificativoDet_Giustificativo);
			dest.addGiustificativo(giustificativo);
		}
		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTGiustificativo convertTo(RendicontoRichiesta src, SiacTGiustificativo dest) {
	
		dest.setSiacTGiustificativoDets(new ArrayList<SiacTGiustificativoDet>());
		
		for(Giustificativo giustificativo : src.getGiustificativi()){
			giustificativo.setLoginOperazione(src.getLoginOperazione());
			giustificativo.setEnte(src.getEnte());
			
			SiacTGiustificativoDet siacTGiustificativoDet = new SiacTGiustificativoDet();
			map(giustificativo, siacTGiustificativoDet, CecMapId.SiacTGiustificativoDet_Giustificativo);
			dest.addSiacTGiustificativoDet(siacTGiustificativoDet);
			
		}
		
		return dest;
	}



	

}
