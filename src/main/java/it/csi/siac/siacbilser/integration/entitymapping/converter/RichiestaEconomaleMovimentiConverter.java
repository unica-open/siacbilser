/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleMovimentiConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleMovimentiConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacTMovimentos() == null || src.getSiacTMovimentos().isEmpty()){
			return dest;
		}
		
		for(SiacTMovimento siacTMovimento: src.getSiacTMovimentos()){
			if(siacTMovimento.getDataCancellazione() == null && siacTMovimento.getSiacTGiustificativo() == null){
				Movimento movimento = new Movimento();
				map(siacTMovimento, movimento, CecMapId.SiacTMovimento_Movimento);
				dest.setMovimento(movimento);
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		if(src.getMovimento() == null){
			return dest;
		}
		List<SiacTMovimento> siacTMovimentos = new ArrayList<SiacTMovimento>();
		
		Movimento movimento = src.getMovimento();
		movimento.setLoginOperazione(src.getLoginOperazione());
		movimento.setEnte(src.getEnte());
		
		SiacTMovimento siacTMovimento = new SiacTMovimento();
		
		map(movimento, siacTMovimento,CecMapId.SiacTMovimento_Movimento);
		siacTMovimento.setSiacTRichiestaEcon(dest);
		siacTMovimentos.add(siacTMovimento);
		
		dest.setSiacTMovimentos(siacTMovimentos);
		return dest;
	}



	

}
