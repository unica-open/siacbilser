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
public class SubmovimentoGestioneImportiConverter extends DozerConverter<MovimentoGestione, SiacTMovgestT> {
	
	/**
	 * Instantiates a new movimento gestione importi converter
	 */
	public SubmovimentoGestioneImportiConverter() {
		super(MovimentoGestione.class, SiacTMovgestT.class);
	}

	@Override
	public MovimentoGestione convertFrom(SiacTMovgestT src, MovimentoGestione dest) {
		
		if(src.getSiacTMovgestTsDets() != null){
			for(SiacTMovgestTsDet siacTMovgestTsDet : src.getSiacTMovgestTsDets()){
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
		}
		
		return dest;
	}
	

	@Override
	public SiacTMovgestT convertTo(MovimentoGestione src, SiacTMovgestT dest) {
		return dest;
	}

}
