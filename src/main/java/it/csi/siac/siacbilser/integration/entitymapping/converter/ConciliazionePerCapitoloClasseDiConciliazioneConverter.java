/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDConciliazioneClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * The Class ConciliazionePerCapitoloClasseDiConciliazioneConverter.
 */
@Component
public class ConciliazionePerCapitoloClasseDiConciliazioneConverter extends ExtendedDozerConverter<ConciliazionePerCapitolo, SiacRConciliazioneCapitolo> {
	
//	@Autowired
//	private SiacTPdceContoRepository siacTPdceContoRepository;
//	@Autowired
//	private SiacRConciliazioneTitoloRepository siacRConciliazioneTitoloRepository;
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new conciliazione per capitolo classe di conciliazione converter.
	 */
	public ConciliazionePerCapitoloClasseDiConciliazioneConverter() {
		super(ConciliazionePerCapitolo.class, SiacRConciliazioneCapitolo.class);
	}

	@Override
	public ConciliazionePerCapitolo convertFrom(SiacRConciliazioneCapitolo src, ConciliazionePerCapitolo dest) {
//		if(src.getSiacTPdceConto() == null) {
//			return dest;
//		}
//		List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos = siacRConciliazioneTitoloRepository.findByEnteProprietarioIdAndContoIdAndClassifId(src.getSiacTEnteProprietario().getUid(),
//				src.getSiacTPdceConto().getUid(), src.getSiacTBilElem().getUid());
//		for(SiacRConciliazioneTitolo srct : siacRConciliazioneTitolos) {
//			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(srct.getSiacDConciliazioneClasse().getConcclaCode());
//			ClasseDiConciliazione classeDiConciliazione = siacDConciliazioneClasseEnum.getClasseDiConciliazione();
//			dest.setClasseDiConciliazione(classeDiConciliazione);
//			break;
//		}
//		return dest;
		
		if(src.getSiacDConciliazioneClasse() == null) {
			return dest;
		}
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(src.getSiacDConciliazioneClasse().getConcclaCode());
		ClasseDiConciliazione classeDiConciliazione = siacDConciliazioneClasseEnum.getClasseDiConciliazione();
		dest.setClasseDiConciliazione(classeDiConciliazione);
		return dest;
	}

	@Override
	public SiacRConciliazioneCapitolo convertTo(ConciliazionePerCapitolo src, SiacRConciliazioneCapitolo dest) {
		if(src.getClasseDiConciliazione() == null) {
			return dest;
		}
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(src.getClasseDiConciliazione());
		SiacDConciliazioneClasse siacDConciliazioneClasse = eef.getEntity(siacDConciliazioneClasseEnum, dest.getSiacTEnteProprietario().getUid(), SiacDConciliazioneClasse.class);
		
		dest.setSiacDConciliazioneClasse(siacDConciliazioneClasse);
		
		return dest;
	}

}
