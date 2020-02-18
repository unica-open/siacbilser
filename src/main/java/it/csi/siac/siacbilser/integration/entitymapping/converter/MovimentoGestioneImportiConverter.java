/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class MovimentoGestioneImportiConverter.
 */
@Component
public class MovimentoGestioneImportiConverter extends DozerConverter<MovimentoGestione, SiacTMovgest> {
	
	private final LogUtil log = new LogUtil(getClass());
	/**
	 * Instantiates a new movimento gestione importi converter
	 */
	public MovimentoGestioneImportiConverter() {
		super(MovimentoGestione.class, SiacTMovgest.class);
	}

	@Override
	public MovimentoGestione convertFrom(SiacTMovgest src, MovimentoGestione dest) {
		if(src.getSiacTMovgestTs() == null) {
			return dest;
		}
		SiacTMovgestT tmt = ottieniTestata(src);
		
		if(tmt.getSiacTMovgestTsDets() != null){
			for(SiacTMovgestTsDet siacTMovgestTsDet : tmt.getSiacTMovgestTsDets()){
				if(siacTMovgestTsDet.getDataCancellazione() == null) {
					
					BigDecimal importo = siacTMovgestTsDet.getMovgestTsDetImporto();
					SiacDMovgestTsDetTipo siacDMovgestTsDetTipo = siacTMovgestTsDet.getSiacDMovgestTsDetTipo();
					if(SiacDMovgestTsDetTipoEnum.Attuale.getCodice().equals(siacDMovgestTsDetTipo.getMovgestTsDetTipoCode())) {
						dest.setImportoAttuale(importo);
					} else if(SiacDMovgestTsDetTipoEnum.Iniziale.getCodice().equals(siacDMovgestTsDetTipo.getMovgestTsDetTipoCode())) {
						dest.setImportoIniziale(importo);
					}
				}
			}
			
			
			if(src.getSiacTMovgestTs()!= null 
					&& !src.getSiacTMovgestTs().isEmpty() ){
				
				
			 
				if  (src.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
					if (dest.getSiopeAssenzaMotivazione() == null) {
						dest.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
					}
					dest.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(src.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
			    }
				if  (src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
						&& !src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
					
					dest.setClasseSoggetto(new ClasseSoggetto());
					
					dest.getClasseSoggetto().setDescrizione(src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
				}
				
				if  (src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
						&& !src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
					dest.setSoggetto(new Soggetto());
					dest.getSoggetto().setCodiceSoggetto(src.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
				}
				
			}
			
			
			
			
			
		}
		
		return dest;
	}

	private SiacTMovgestT ottieniTestata(SiacTMovgest src) {
		final String methodName = "ottieniTestata";
		for(SiacTMovgestT tmt : src.getSiacTMovgestTs()) {
			if(tmt != null && tmt.getSiacDMovgestTsTipo() != null && SiacDMovgestTsTipoEnum.Testata.getCodice().equals(tmt.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				return tmt;
			}
		}
		log.warn(methodName, "Testata non trovata su SiacTMovgestTs");
		throw new IllegalStateException("Testata non trovata su SiacTMovgestTs per SiacTMovgest [uid: " + src.getUid() + "]");
	}
	

	@Override
	public SiacTMovgest convertTo(MovimentoGestione src, SiacTMovgest dest) {
		return dest;
	}

}
