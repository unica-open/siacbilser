/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiVariazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.VariazioneCespite;

/**
 * The Class VariazioneCespiteStatoVariazioneCespiteConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/08/2018
 */
@Component
public class VariazioneCespiteStatoVariazioneCespiteConverter extends ExtendedDozerConverter<VariazioneCespite, SiacTCespitiVariazione> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	/** Costruttore */
	public VariazioneCespiteStatoVariazioneCespiteConverter() {
		super(VariazioneCespite.class, SiacTCespitiVariazione.class);
	}

	@Override
	public VariazioneCespite convertFrom(SiacTCespitiVariazione src, VariazioneCespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDCespitiVariazioneStato() == null){
			log.warn(methodName, "Variazione cespite [uid: " + src.getUid() + "] priva dello Stato! Controllare su DB. Entita associata: " + src.getClass().getSimpleName());
			return dest;
		}
		
		dest.setStatoVariazioneCespite(SiacDCespitiVariazioneStatoEnum.byCodice(src.getSiacDCespitiVariazioneStato().getCesVarStatoCode()).getStatoVariazioneCespite());
		return dest;
	}

	@Override
	public SiacTCespitiVariazione convertTo(VariazioneCespite src, SiacTCespitiVariazione dest) {
		if(src.getStatoVariazioneCespite() == null) {
			return dest;
		}
		
		SiacDCespitiVariazioneStatoEnum siacDCespitiVariazioneStatoEnum = SiacDCespitiVariazioneStatoEnum.byStatoVariazioneCespite(src.getStatoVariazioneCespite());
		SiacDCespitiVariazioneStato siacDCespitiVariazioneStato = eef.getEntity(siacDCespitiVariazioneStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCespitiVariazioneStato.class);
		dest.setSiacDCespitiVariazioneStato(siacDCespitiVariazioneStato);
		return dest;
	}


}
