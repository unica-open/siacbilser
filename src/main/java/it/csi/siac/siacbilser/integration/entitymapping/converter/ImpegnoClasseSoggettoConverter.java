/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogclasse;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;

/**
 * The Class ImpegnoClasseSoggettoConverter.
 */
@Component
public class ImpegnoClasseSoggettoConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	/**
	 * Instantiates a new impegno - classe soggetto converter.
	 */
	public ImpegnoClasseSoggettoConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		if(src.getSiacTMovgestTs() == null){
			return dest;
		}
		
		List<SiacTMovgestT> testate = siacTMovgestTRepository.findSiacTMovgestTestataBySiacTMovgestId(src.getMovgestId());
		
		for(SiacTMovgestT siacTMovgestT : testate){
			if(siacTMovgestT.getSiacRMovgestTsSogclasses() == null){
				return dest;
			}
			for(SiacRMovgestTsSogclasse siacRMovgestTsSogclasse : siacTMovgestT.getSiacRMovgestTsSogclasses()){
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
	public SiacTMovgest convertTo(Impegno src, SiacTMovgest dest) {
		
		return dest;
	}

}
