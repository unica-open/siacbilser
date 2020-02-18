/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneApplicazione;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneApplicazioneEnum;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;

/**
 * The Class VariazioneApplicazioneConverter.
 */
@Component
public class VariazioneApplicazioneConverter extends DozerConverter<VariazioneImportoCapitolo, SiacTVariazione> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	
	public VariazioneApplicazioneConverter() {
		super(VariazioneImportoCapitolo.class, SiacTVariazione.class);
	}
	
	@Override
	public VariazioneImportoCapitolo convertFrom(SiacTVariazione src, VariazioneImportoCapitolo dest) {
		ApplicazioneVariazione applicazioneVariazione = getApplicazioneVariazione(src);
		dest.setApplicazioneVariazione(applicazioneVariazione);
		return dest;
	}
	
	private ApplicazioneVariazione getApplicazioneVariazione(SiacTVariazione src) {
		if(src.getSiacDVariazioneApplicazione()==null) {
			return null;
		}
		
		String codice = src.getSiacDVariazioneApplicazione().getApplicazioneCode();
		return SiacDVariazioneApplicazioneEnum.byCodice(codice).getApplicazioneVariazione(); 
		
	}


	@Override
	public SiacTVariazione convertTo(VariazioneImportoCapitolo src, SiacTVariazione dest) {
		SiacDVariazioneApplicazioneEnum dAppVarEnum = SiacDVariazioneApplicazioneEnum.byApplicazioneVariazione(src.getApplicazioneVariazione());
		
		SiacDVariazioneApplicazione siacDVariazioneApplicazione = eef.getEntity(dAppVarEnum, dest.getSiacTEnteProprietario().getUid(), SiacDVariazioneApplicazione.class);
		dest.setSiacDVariazioneApplicazione(siacDVariazioneApplicazione);
	
		return dest;
	}


}
