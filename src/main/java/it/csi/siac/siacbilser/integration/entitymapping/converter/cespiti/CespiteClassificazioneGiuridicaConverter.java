/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiClassificazioneGiuridica;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiClassificazioneGiuridicaEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteClassificazioneGiuridicaConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
@Autowired private EnumEntityFactory eef;
	

	public CespiteClassificazioneGiuridicaConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		String methodName = "convertFrom";
		if(src.getSiacDCespitiClassificazioneGiuridica() == null){
			log.warn(methodName, " Cespiti [uid: "+src.getUid()+"] priva di Classificazione giuridica! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		ClassificazioneGiuridicaCespite cgc = SiacDCespitiClassificazioneGiuridicaEnum.byCodice(src.getSiacDCespitiClassificazioneGiuridica().getCesClassGiuCode()).getClassificazioneGiuridicaCespite();
		
		dest.setClassificazioneGiuridicaCespite(cgc);

		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		String methodName = "convertTo";
		if(src.getClassificazioneGiuridicaCespite() == null) {
			throw new IllegalArgumentException("Cespite associato alla Classificazione Giuridica obbligatorio. non specificato. [null]");
		}
		
		SiacDCespitiClassificazioneGiuridicaEnum siacDAmbitoEnum = SiacDCespitiClassificazioneGiuridicaEnum.byClassificazioneGiuridicaCespite(src.getClassificazioneGiuridicaCespite());
		
		SiacDCespitiClassificazioneGiuridica siacDCespitiClassificazioneGiuridica = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCespitiClassificazioneGiuridica.class); 
		
		dest.setSiacDCespitiClassificazioneGiuridica(siacDCespitiClassificazioneGiuridica);
		return dest;
	}
}
