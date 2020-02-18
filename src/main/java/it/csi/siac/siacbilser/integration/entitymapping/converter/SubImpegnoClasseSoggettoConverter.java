/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogclasse;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;

/**
 * The Class SubImpegnoClasseSoggettoConverter.
 */
@Component
public class SubImpegnoClasseSoggettoConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {

	/**
	 * Instantiates a new subimpegno - classe soggetto converter.
	 */
	public SubImpegnoClasseSoggettoConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}
	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		if(src.getSiacRMovgestClasses() != null) {
			for(SiacRMovgestTsSogclasse siacRMovgestTsSogclasse : src.getSiacRMovgestTsSogclasses()){
				if(siacRMovgestTsSogclasse.getDataCancellazione() == null){
					SiacDSoggettoClasse siacDSoggettoClasse = siacRMovgestTsSogclasse.getSiacDSoggettoClasse();
					
					ClasseSoggetto classeSoggetto = new ClasseSoggetto();
					classeSoggetto.setUid(siacDSoggettoClasse.getSoggettoClasseId());
					classeSoggetto.setCodice(siacDSoggettoClasse.getSoggettoClasseCode());
					classeSoggetto.setDescrizione(siacDSoggettoClasse.getSoggettoClasseDesc());
					
					// Prendo la prima classe ed esco
					dest.setClasseSoggetto(classeSoggetto);
					return dest;
				}
			}
		}
		
		return dest;
	}

	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		
		return dest;
	}

}
