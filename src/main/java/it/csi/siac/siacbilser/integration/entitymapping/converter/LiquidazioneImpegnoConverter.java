/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * The Class LiquidazioneOrdinativoConverter
 */
@Component
public class LiquidazioneImpegnoConverter extends GenericImpegnoSubimpegnoBaseConverter<Liquidazione, SiacTLiquidazione> {
	
	/**
	 * Instantiates a new pre documento spesa impegno converter.
	 */
	public LiquidazioneImpegnoConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src.getSiacRLiquidazioneMovgests()!=null){
			for(SiacRLiquidazioneMovgest r : src.getSiacRLiquidazioneMovgests()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				impostaImpegnoESubImpegno(dest,  r.getSiacTMovgestT());
			}
		}
		
		return dest;
	}
	

	
	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		
		Integer movgestId = getMovgestId(src.getImpegno(), src.getSubImpegno());
		
		if(movgestId == null){
			return dest;
		}
		
		
		dest.setSiacRLiquidazioneMovgests(new ArrayList<SiacRLiquidazioneMovgest>());
		
		SiacRLiquidazioneMovgest siacRLiquidazioneMovgest = new SiacRLiquidazioneMovgest();
		siacRLiquidazioneMovgest.setSiacTLiquidazione(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRLiquidazioneMovgest.setSiacTMovgestT(siacTMovgestT);
		
		siacRLiquidazioneMovgest.setLoginOperazione(dest.getLoginOperazione());
		siacRLiquidazioneMovgest.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRLiquidazioneMovgest(siacRLiquidazioneMovgest);
		
		return dest;
	}



}
