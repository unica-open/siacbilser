/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;




import org.dozer.DozerConverter;

import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;

	
public class SessoConverter extends  DozerConverter<String, Soggetto>{
	
	public SessoConverter() {
		super(String.class, Soggetto.class);
		
	}
	
	@Override
	public Soggetto convertTo(String source, Soggetto destination) {
		
		if(null==source){
			return null;
		}
		
		if(Constanti.SESSO_M.equalsIgnoreCase(source)){
			    destination.setSesso(Sesso.MASCHIO); 
		     	return destination;
		}else if(Constanti.SESSO_F.equalsIgnoreCase(source)){
			    destination.setSesso(Sesso.FEMMINA); 
		     	return destination;
		}else{
			 destination.setSesso(Sesso.NON_DEFINITO); 
	     	 return destination; 
		}
		
		
		
	}
	
	
	 @Override
	 public String convertFrom(Soggetto source, String destination) {
		
		 //log.debug("","Entro in convertFrom "+source);
		 
		 if(null==source) return null;
		 
		 String ris ="";
		  if(source.getSesso() ==  Sesso.MASCHIO){
			  		ris = Constanti.SESSO_M.toLowerCase();
		  } else if(source.getSesso() ==  Sesso.FEMMINA){
		     		ris = Constanti.SESSO_F.toLowerCase();
		  } else ris = "";   
		  
		  return ris;
		 }
	 
} 

