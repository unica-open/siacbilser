/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * The Class RendicontoRichiestaMovimentiConverter.
 */
@Component
public class RendicontoRichiestaMovimentiConverter extends ExtendedDozerConverter<RendicontoRichiesta, SiacTGiustificativo > {
	
	/**
	 * Instantiates a new rendiconto richiesta movimenti converter.
	 */
	public RendicontoRichiestaMovimentiConverter() {
		super(RendicontoRichiesta.class, SiacTGiustificativo.class);
	}

	
	@Override
	public RendicontoRichiesta convertFrom(SiacTGiustificativo src, RendicontoRichiesta dest) {
		
		if(src.getSiacTMovimentos()==null){
			return null;
		}
		
		for(SiacTMovimento gd : src.getSiacTMovimentos()){
			if(gd.getDataCancellazione()!=null){
				continue;
			}
			Movimento giustificativo = map(gd, Movimento.class, getMapIdMovimento());
			dest.setMovimento(giustificativo);
			break; //c'è un solo movimento non cancellato!
		}
		
		
		return dest;
	}


	protected CecMapId getMapIdMovimento() {
		return CecMapId.SiacTMovimento_Movimento;
	}

	
	@Override
	public SiacTGiustificativo convertTo(RendicontoRichiesta src, SiacTGiustificativo dest) {
		
		if(src.getMovimento()!=null) {
			
			Movimento movimento = src.getMovimento();
			movimento.setLoginOperazione(src.getLoginOperazione());
			movimento.setEnte(src.getEnte());
			
			SiacTMovimento siacTMovimento = new SiacTMovimento();
			map(movimento, siacTMovimento, CecMapId.SiacTMovimento_Movimento);
			
			SiacTRichiestaEcon siacTRichiestaEcon = new SiacTRichiestaEcon();//TODO valutare di mettere nullable la colonna sul siac_t_movimento!
			siacTRichiestaEcon.setUid(src.getRichiestaEconomale().getUid());
			siacTMovimento.setSiacTRichiestaEcon(siacTRichiestaEcon);
			
			dest.setSiacTMovimentos(new ArrayList<SiacTMovimento>());
			dest.addSiacTMovimento(siacTMovimento);
		}
		
		return dest;
	}



	

}
