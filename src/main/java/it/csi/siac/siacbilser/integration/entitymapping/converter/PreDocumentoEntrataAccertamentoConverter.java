/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * The Class PreDocumentoEntrataAccertamentoConverter.
 */
@Component
public class PreDocumentoEntrataAccertamentoConverter extends GenericAccertamentoSubaccertamentoBaseConverter<PreDocumentoEntrata, SiacTPredoc > {
	
	
	/**
	 * Instantiates a new pre documento entrata accertamento converter.
	 */
	public PreDocumentoEntrataAccertamentoConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		if(src.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT siacRPredocMovgestT : src.getSiacRPredocMovgestTs()){
				if(siacRPredocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				
				impostaAccertamentoESubAccertamento(dest, siacRPredocMovgestT.getSiacTMovgestT());
				
			}
		}
		
		return dest;
	}
	

	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
		Integer movgestId = getMovgestId(src.getAccertamento(), src.getSubAccertamento());
		
		if(movgestId==null){
			return null;
		}
		
		dest.setSiacRPredocMovgestTs(new ArrayList<SiacRPredocMovgestT>());
		
		SiacRPredocMovgestT siacRPredocMovgestT = new SiacRPredocMovgestT();
		siacRPredocMovgestT.setSiacTPredoc(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRPredocMovgestT.setSiacTMovgestT(siacTMovgestT);
		
		siacRPredocMovgestT.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocMovgestT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocMovgestT(siacRPredocMovgestT);
		
		return dest;
	}




}
