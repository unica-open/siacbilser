/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StampeCassaFile;

@Component
public class StampeCassaFileOperazioneCassaConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {

	protected StampeCassaFileOperazioneCassaConverter() {
		super(StampeCassaFile.class,SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src,
			StampeCassaFile dest) {
		if (src.getSiacRCassaEconOperazStampas() != null) {
			
			for (SiacRCassaEconOperazStampa siacRCassaEconOperazStampa : src.getSiacRCassaEconOperazStampas()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRCassaEconOperazStampa.getDataCancellazione()!=null){
					continue;
				}

				SiacTCassaEconOperaz siacTCassaEconOperaz =siacRCassaEconOperazStampa.getSiacTCassaEconOperaz();

				OperazioneCassa operazioneCassa = map(siacTCassaEconOperaz, OperazioneCassa.class, getMapIdOperazioniCassa());
				
				dest.addOperazioneCassa(operazioneCassa);
			}
			
		}
		return dest;
	}
	
	protected CecMapId getMapIdOperazioniCassa() {
		
		return CecMapId.SiacTCassaEconOperaz_OperazioneCassa;
		

	}

	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src,	SiacTCassaEconStampa dest) {
		
		dest.setSiacRCassaEconOperazStampas(new ArrayList<SiacRCassaEconOperazStampa>());
		for(OperazioneCassa operazioneCassa : src.getOperazioniCassa()){
			
			SiacRCassaEconOperazStampa siacRCassaEconOperazStampa = new SiacRCassaEconOperazStampa();	
			
			SiacTCassaEconOperaz siacTCassaEconOperaz = map(operazioneCassa,SiacTCassaEconOperaz.class, getMapIdOperazioniCassa());	
			siacRCassaEconOperazStampa.setSiacTCassaEconOperaz(siacTCassaEconOperaz);
			
			siacRCassaEconOperazStampa.setLoginOperazione(dest.getLoginOperazione());
			siacRCassaEconOperazStampa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
					
			dest.addSiacRCassaEconOperazStampa(siacRCassaEconOperazStampa);
		}
		return dest;
	}

}
