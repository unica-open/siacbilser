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
import it.csi.siac.siacbilser.integration.entity.SiacDGsaClassifStato;
import it.csi.siac.siacbilser.integration.entity.SiacRGsaClassifStato;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGsaClassifStatoEnum;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class ClassificatoreGSAStatoConverter extends ExtendedDozerConverter<ClassificatoreGSA, SiacTGsaClassif > {
	
	/** The eef. */
	@Autowired private EnumEntityFactory eef;

	/**
	 * Instantiates a new classificatore GSA classificatore GSA stato converter.
	 */
	public ClassificatoreGSAStatoConverter() {
		super(ClassificatoreGSA.class, SiacTGsaClassif.class);
	}

	@Override
	public ClassificatoreGSA convertFrom(SiacTGsaClassif src, ClassificatoreGSA dest) {
		if(src==null){
			return dest;
		}
		
		for (SiacRGsaClassifStato siacRGsaClassifStato : src.getSiacRGsaClassifStatos()) {
			if(siacRGsaClassifStato.getDataCancellazione()==null){
				StatoOperativoClassificatoreGSA statoOperativoClassificatoreGSA = SiacDGsaClassifStatoEnum.byCodice(siacRGsaClassifStato.getSiacDGsaClassifStato().getGsaClassifStatoCode()).getStatoOperativoClassificatoreGSA();
				dest.setStatoOperativoClassificatoreGSA(statoOperativoClassificatoreGSA);
				break;
			}
		}
		return dest;
		
	}


	@Override
	public SiacTGsaClassif convertTo(ClassificatoreGSA src, SiacTGsaClassif dest) {
		final String methodName = "convertTo";
		if(dest== null) {
			return dest;
		}
		
		List<SiacRGsaClassifStato> siacRGsaClassifStatos = new ArrayList<SiacRGsaClassifStato>();
		
		SiacRGsaClassifStato siacRGsaClassifStato = new SiacRGsaClassifStato();
		
		SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum = SiacDGsaClassifStatoEnum.byStatoOperativo(src.getStatoOperativoClassificatoreGSA());
		
		SiacDGsaClassifStato siacDGsaClassifStato = eef.getEntity(siacDGsaClassifStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDGsaClassifStato.class); 
				
		log .debug(methodName, "setting siacDDocStato to: "+siacDGsaClassifStato.getGsaClassifStatoCode()+ " ["+siacDGsaClassifStato.getUid()+"]");
		siacRGsaClassifStato.setSiacDGsaClassifStato(siacDGsaClassifStato); //SiacDGsaClassifStato(siacDGsaClassifStato);
		siacRGsaClassifStato.setSiacTGsaClassif(dest);
		
		siacRGsaClassifStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRGsaClassifStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRGsaClassifStatos.add(siacRGsaClassifStato);
		dest.setSiacRGsaClassifStatos(siacRGsaClassifStatos);
		return dest;
	}


}
