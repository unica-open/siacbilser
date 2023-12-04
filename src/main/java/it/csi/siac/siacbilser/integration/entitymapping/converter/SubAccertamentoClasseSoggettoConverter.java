/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogclasse;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;

/**
 * The Class SubAccertamentoClasseSoggettoConverter.
 */
@Component
public class SubAccertamentoClasseSoggettoConverter extends ExtendedDozerConverter<SubAccertamento, SiacTMovgestT> {

	/**
	 * Instantiates a new subaccertamento - classe soggetto converter.
	 */
	public SubAccertamentoClasseSoggettoConverter() {
		super(SubAccertamento.class, SiacTMovgestT.class);
	}
	@Override
	public SubAccertamento convertFrom(SiacTMovgestT src, SubAccertamento dest) {
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
	public SiacTMovgestT convertTo(SubAccertamento src, SiacTMovgestT dest) {
		
		return dest;
	}

}
