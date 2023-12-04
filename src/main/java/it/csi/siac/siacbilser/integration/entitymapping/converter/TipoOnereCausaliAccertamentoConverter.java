/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDCausaleRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class TipoOnereCausaliAccertamentoConverter.
 */
@Component
public class TipoOnereCausaliAccertamentoConverter extends GenericAccertamentoSubaccertamentoBaseConverter<TipoOnere,SiacDOnere> {
		
	@Autowired
	private SiacDCausaleRepository siacDCausaleRepository;	
	
	/**
	 * Instantiates a new onere attr converter.
	 */
	public TipoOnereCausaliAccertamentoConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		
		if(dest.getCausali()==null){
			return dest;
		}
				
		for(Causale causale : dest.getCausali()){
			if(causale instanceof CausaleEntrata){
				CausaleEntrata causaleEntrata = (CausaleEntrata)causale;
				impostaAccertamentoESubAccertamento(dest, causaleEntrata.getAccertamento().getUid());				
			}			
		}
		
		return dest;
	}

	@Override
	public SiacDOnere convertTo(TipoOnere src, SiacDOnere dest) {
		return dest;
	}

}
