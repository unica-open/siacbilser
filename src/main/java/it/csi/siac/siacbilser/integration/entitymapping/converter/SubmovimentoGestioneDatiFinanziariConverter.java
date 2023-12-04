/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class MovimentoGestioneImportiConverter.
 */
@Component
public class SubmovimentoGestioneDatiFinanziariConverter extends DozerConverter<MovimentoGestione, SiacTMovgestT> {
	
	/**
	 * Instantiates a new movimento gestione importi converter
	 */
	public SubmovimentoGestioneDatiFinanziariConverter() {
		super(MovimentoGestione.class, SiacTMovgestT.class);
	}

	@Override
	public MovimentoGestione convertFrom(SiacTMovgestT src, MovimentoGestione dest) {
		
		if(src.getSiacTMovgestTsDets() != null){
			if(src.getSiacTMovgest().getSiacTMovgestTs()!= null 
					&& !src.getSiacTMovgest().getSiacTMovgestTs().isEmpty() ){
				if  (src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
					if (dest.getSiopeAssenzaMotivazione() == null) {
						dest.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
					}
					dest.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
			    }
				if  (src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
						&& !src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
					dest.setClasseSoggetto(new ClasseSoggetto());
					dest.getClasseSoggetto().setDescrizione(src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
				}
				
				if  (src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
						&& !src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
					dest.setSoggetto(new Soggetto());
					//SIAC-7327: corretta regressione su contabilita generale 
					dest.getSoggetto().setUid(src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoId());
					dest.getSoggetto().setCodiceSoggetto(src.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
				}
				
			}
			
		}
		
		return dest;
	}
	

	@Override
	public SiacTMovgestT convertTo(MovimentoGestione src, SiacTMovgestT dest) {
		return dest;
	}

}
