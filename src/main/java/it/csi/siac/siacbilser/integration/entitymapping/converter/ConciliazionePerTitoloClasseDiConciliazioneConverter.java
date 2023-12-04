/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDConciliazioneClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;

/**
 * The Class ConciliazionePerTitoloClasseDiConciliazioneConverter.
 */
@Component
public class ConciliazionePerTitoloClasseDiConciliazioneConverter extends ExtendedDozerConverter<ConciliazionePerTitolo, SiacRConciliazioneTitolo> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	
	/**
	 * Instantiates a new conciliazione per titolo classe di conciliazione converter.
	 */
	public ConciliazionePerTitoloClasseDiConciliazioneConverter() {
		super(ConciliazionePerTitolo.class, SiacRConciliazioneTitolo.class);
	}

	@Override
	public ConciliazionePerTitolo convertFrom(SiacRConciliazioneTitolo src, ConciliazionePerTitolo dest) {
		if(src.getSiacDConciliazioneClasse() == null) {
			return dest;
		}
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(src.getSiacDConciliazioneClasse().getConcclaCode());
		ClasseDiConciliazione classeDiConciliazione = siacDConciliazioneClasseEnum.getClasseDiConciliazione();
		dest.setClasseDiConciliazione(classeDiConciliazione);
		return dest;
	}

	@Override
	public SiacRConciliazioneTitolo convertTo(ConciliazionePerTitolo src, SiacRConciliazioneTitolo dest) {
		if(src.getClasseDiConciliazione() == null) {
			return dest;
		}
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(src.getClasseDiConciliazione());
		SiacDConciliazioneClasse siacDConciliazioneClasse = eef.getEntity(siacDConciliazioneClasseEnum, dest.getSiacTEnteProprietario().getUid(), SiacDConciliazioneClasse.class);
		
		dest.setSiacDConciliazioneClasse(siacDConciliazioneClasse);
		
		return dest;
	}

}
