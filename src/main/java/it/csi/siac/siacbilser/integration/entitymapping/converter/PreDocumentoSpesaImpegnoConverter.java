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
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * The Class PreDocumentoSpesaImpegnoConverter.
 */
@Component
public class PreDocumentoSpesaImpegnoConverter extends GenericImpegnoSubimpegnoBaseConverter<PreDocumentoSpesa, SiacTPredoc> {
	
	/**
	 * Instantiates a new pre documento spesa impegno converter.
	 */
	public PreDocumentoSpesaImpegnoConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		if(src.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT r : src.getSiacRPredocMovgestTs()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				impostaImpegnoESubImpegno(dest,  r.getSiacTMovgestT());
			}
		}
		
		return dest;
	}
	

	
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
		Integer movgestId = getMovgestId(src.getImpegno(), src.getSubImpegno());
		
		if(movgestId == null){
			return dest;
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
