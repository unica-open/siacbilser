/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoTsDetTipoEnum;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
/**
 * The Class OrdinativoSoggettoConverter.
 */
@Component
public class OrdinativoImportoConverter extends ExtendedDozerConverter<Ordinativo, SiacTOrdinativo >{

	protected OrdinativoImportoConverter() {
		super(Ordinativo.class, SiacTOrdinativo.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Ordinativo convertFrom(SiacTOrdinativo src, Ordinativo dest) {
		if(src.getSiacTOrdinativoTs() == null){
			return dest;
		}
		BigDecimal importo = BigDecimal.ZERO;
		for(SiacTOrdinativoT siacTOrdinativoT : src.getSiacTOrdinativoTs()){
			if(siacTOrdinativoT.getDataCancellazione() != null || siacTOrdinativoT.getSiacTOrdinativoTsDets() == null){
				continue;
			}
			for(SiacTOrdinativoTsDet siacTOrdinativoTsDet : siacTOrdinativoT.getSiacTOrdinativoTsDets()){
				if(siacTOrdinativoTsDet.getDataCancellazione() == null && SiacDOrdinativoTsDetTipoEnum.ATTUALE.getCodice().equals(siacTOrdinativoTsDet.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode())){
					importo = importo.add(siacTOrdinativoTsDet.getOrdTsDetImporto());
				}
			}
		}
		dest.setImportoOrdinativo(importo);
		return dest;
	}

	@Override
	public SiacTOrdinativo convertTo(Ordinativo src, SiacTOrdinativo dest) {

		return dest;
	}

}
