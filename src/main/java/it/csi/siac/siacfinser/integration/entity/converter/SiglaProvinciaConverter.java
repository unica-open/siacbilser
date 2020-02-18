/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;

public class SiglaProvinciaConverter extends  DozerConverter<String,SiacTComuneFin> {

	
	public SiglaProvinciaConverter() {
		super(String.class, SiacTComuneFin.class);
		
	}
	
	
	@Override
	public String convertFrom(SiacTComuneFin siacTComune, String s) {
		//log.debug("","SiglaProvinciaConverter convertFrom");
		String ris = null;
		if(null==siacTComune){
				return ris;
		}else if(null==siacTComune.getSiacRComuneProvincias()){
			
				return ris;
		}else {
			
			for (int i = 0; i < siacTComune.getSiacRComuneProvincias().size(); i++) {
				
				ris = siacTComune.getSiacRComuneProvincias().get(i).getSiacTProvincia().getSiglaAutomobilistica();
				
				
			}
			
		}
		
		return ris;
	}

	
	// type="one-way" --> quindi questo verso non serve
	
	@Override
	public SiacTComuneFin convertTo(String s, SiacTComuneFin siacTComune) {
		
		return null;
	}

}
