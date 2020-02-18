/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCronopElemBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;

/**
 * The Class DettaglioCronoprogrammaEntrataBilElemConverter.
 */
@Component
public class DettaglioCronoprogrammaEntrataBilElemConverter extends ExtendedDozerConverter<DettaglioEntrataCronoprogramma, SiacTCronopElem> {
	
	
	/**
	 * Instantiates a new dettaglio cronoprogramma entrata bil elem converter.
	 */
	public DettaglioCronoprogrammaEntrataBilElemConverter() {
		super(DettaglioEntrataCronoprogramma.class, SiacTCronopElem.class);
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioEntrataCronoprogramma convertFrom(SiacTCronopElem src, DettaglioEntrataCronoprogramma dest) {
		for(SiacRCronopElemBilElem siacRCronopElemBilElem : src.getSiacRCronopElemBilElems()){
			if(siacRCronopElemBilElem.getDataCancellazione()==null){
				
				SiacTBilElem siacTBilElem = siacRCronopElemBilElem.getSiacTBilElem();
				CapitoloEntrataPrevisione cap = new CapitoloEntrataPrevisione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);
				
				dest.setCapitolo(cap);
				// Campi piatti
				dest.setNumeroCapitolo(cap.getNumeroCapitolo());
				dest.setNumeroArticolo(cap.getNumeroArticolo());
				dest.setNumeroUEB(cap.getNumeroUEB());
			}
			
		}

		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronopElem convertTo(DettaglioEntrataCronoprogramma src, SiacTCronopElem dest) {
		Capitolo<?,?> cap = src.getCapitolo();
		if(cap==null || cap.getUid()==0){
			return dest;
		}
		
		dest.setSiacRCronopElemBilElems(new ArrayList<SiacRCronopElemBilElem>());		
		
		SiacRCronopElemBilElem siacRCronopElemBilElem = new SiacRCronopElemBilElem();
		
		siacRCronopElemBilElem.setSiacTCronopElem(dest);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(cap.getUid());
		siacRCronopElemBilElem.setSiacTBilElem(siacTBilElem);
		
		siacRCronopElemBilElem.setLoginOperazione(dest.getLoginOperazione());
		siacRCronopElemBilElem.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		
		dest.addSiacRCronopElemBilElem(siacRCronopElemBilElem);
		
		cap.getUid();
		
		
		
		
		return dest;
	}

	

	

}
