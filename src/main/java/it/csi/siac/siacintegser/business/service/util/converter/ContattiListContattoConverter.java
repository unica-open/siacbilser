/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;


import java.util.List;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacintegser.model.integ.Contatti;


public class ContattiListContattoConverter extends DozerConverter<Contatti, List<Contatto>> {
	

	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	@SuppressWarnings("unchecked")
	public ContattiListContattoConverter() {
		super(Contatti.class, (Class<List<Contatto>>)(Class<?>)List.class);
	}
	
	
	public ContattiListContattoConverter(Class<Contatti> prototypeA, Class<List<Contatto>> prototypeB)
	{
		super(prototypeA, prototypeB);
	}

	@Override
	public Contatti convertFrom(List<Contatto> arg0, Contatti arg1)
	{
		for (Contatto contatto : arg0)
		{
			if ("telefono".equals(contatto.getContattoCodModo()))
				arg1.setTelefono(contatto.getDescrizione());
			
			if ("cellulare".equals(contatto.getContattoCodModo()))
				arg1.setCellulare(contatto.getDescrizione());
			
			if ("fax".equals(contatto.getContattoCodModo()))
				arg1.setFax(contatto.getDescrizione());
			
			if ("email".equals(contatto.getContattoCodModo()))
				arg1.setEmail(contatto.getDescrizione());
			
			if ("PEC".equals(contatto.getContattoCodModo()))
				arg1.setPec(contatto.getDescrizione());
		}
		
		return arg1;
	}

	@Override
	public List<Contatto> convertTo(Contatti arg0, List<Contatto> arg1)
	{
		throw new UnsupportedOperationException();
	}


}
