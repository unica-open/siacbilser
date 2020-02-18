/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovimentoStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.StampeCassaFile;
@Component
public class StampeCassaFileMovimentoConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {

	protected StampeCassaFileMovimentoConverter() {
		super(StampeCassaFile.class,SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		
		if (src.getSiacRMovimentoStampas() != null) {
			
			for (SiacRMovimentoStampa siacRMovimentoStampa : src.getSiacRMovimentoStampas()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRMovimentoStampa.getDataCancellazione()!=null){
					continue;
				}

				SiacTMovimento siacTMovimento =siacRMovimentoStampa.getSiacTMovimento();

				Movimento movimento = map(siacTMovimento, Movimento.class, getMapIdMovimento());
				
				dest.addMovimento(movimento);
			}
			
		}
		return dest;
	}

	protected CecMapId getMapIdMovimento() {
		
		return CecMapId.SiacTMovimento_Movimento;
		

	}
	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src,	SiacTCassaEconStampa dest) {
		dest.setSiacRMovimentoStampas(new ArrayList<SiacRMovimentoStampa>());
		
		for(Movimento movimento : src.getMovimenti()){
				
			SiacRMovimentoStampa siacRMovimentoStampa = new SiacRMovimentoStampa();	
			
			SiacTMovimento siacTMovimento = map(movimento,SiacTMovimento.class, getMapIdMovimento());	
			siacRMovimentoStampa.setSiacTMovimento(siacTMovimento);
			
			siacRMovimentoStampa.setLoginOperazione(dest.getLoginOperazione());
			siacRMovimentoStampa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
					
			
			dest.addSiacRMovimentoStampa(siacRMovimentoStampa);
			
		}
		
		
		return dest;
	}

}
