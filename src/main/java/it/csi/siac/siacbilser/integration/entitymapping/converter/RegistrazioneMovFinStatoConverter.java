/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDRegMovfinStato;
import it.csi.siac.siacbilser.integration.entity.SiacRRegMovfinStato;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRegMovFinStatoEnum;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * The Class CausaleEPStatoConverter.
 * 
 * @author Domenico
 */
@Component
public class RegistrazioneMovFinStatoConverter extends ExtendedDozerConverter<RegistrazioneMovFin, SiacTRegMovfin > {
	
//	<a>this</a> <!-- tipoCausale -->
//	<b>this</b> <!-- siacDCausaleEpStato -->
	
	@Autowired
	private EnumEntityFactory eef;
	

	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public RegistrazioneMovFinStatoConverter() {
		super(RegistrazioneMovFin.class, SiacTRegMovfin.class);
	}

	@Override
	public RegistrazioneMovFin convertFrom(SiacTRegMovfin src, RegistrazioneMovFin dest) {
		
		if(src.getSiacRRegMovfinStatos() == null){
			return dest;
		}
		for(SiacRRegMovfinStato siacRRegMovfinStato : src.getSiacRRegMovfinStatos()){
			if(siacRRegMovfinStato.getDataCancellazione() == null){
				SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum = SiacDRegMovFinStatoEnum.byCodice(siacRRegMovfinStato.getSiacDRegMovfinStato().getRegmovfinStatoCode());
				StatoOperativoRegistrazioneMovFin statoOperativoRegistrazioneMovFin = siacDRegMovFinStatoEnum.getStatoOperativo();
				dest.setStatoOperativoRegistrazioneMovFin(statoOperativoRegistrazioneMovFin);
			}
		}
		return dest;
	}

	@Override
	public SiacTRegMovfin convertTo(RegistrazioneMovFin src, SiacTRegMovfin dest) {
		
		if(src.getStatoOperativoRegistrazioneMovFin() == null){
			return dest;
		}
		List<SiacRRegMovfinStato> siacRRegMovfinStatos = new ArrayList<SiacRRegMovfinStato>();
		SiacRRegMovfinStato siacRRegMovfinStato = new SiacRRegMovfinStato();
		
		SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum = SiacDRegMovFinStatoEnum.byStatoOperativo(src.getStatoOperativoRegistrazioneMovFin());
		SiacDRegMovfinStato siacDRegMovfinStato = eef.getEntity(siacDRegMovFinStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDRegMovfinStato.class); 
		
		siacRRegMovfinStato.setSiacDRegMovfinStato(siacDRegMovfinStato);
		siacRRegMovfinStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRRegMovfinStato.setSiacTRegMovfin(dest);
		siacRRegMovfinStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRRegMovfinStatos.add(siacRRegMovfinStato);
		dest.setSiacRRegMovfinStatos(siacRRegMovfinStatos);
		return dest;
	}



	

}
