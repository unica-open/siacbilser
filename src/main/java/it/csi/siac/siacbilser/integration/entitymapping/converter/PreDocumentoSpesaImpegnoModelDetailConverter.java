/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class PreDocumentoSpesaImpegnoModelDetailConverter.
 */
@Component
public class PreDocumentoSpesaImpegnoModelDetailConverter extends GenericImpegnoSubimpegnoBaseConverter<PreDocumentoSpesa, SiacTPredoc> {
	
	/**
	 * Instantiates a new pre documento spesa impegno converter.
	 */
	public PreDocumentoSpesaImpegnoModelDetailConverter() {
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
	protected Impegno toImpegno(SiacTMovgestT siacTMovgestT) {
		return mapNotNull(siacTMovgestT.getSiacTMovgest(), Impegno.class, BilMapId.SiacTMovgest_Impegno_ModelDetail, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(ImpegnoModelDetail.class)));
	}
	
	@Override
	protected SubImpegno toSubImpegno(SiacTMovgestT siacTMovgestT) {
		return mapNotNull(siacTMovgestT, SubImpegno.class, BilMapId.SiacTMovgestT_SubImpegno_ModelDetail, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(SubImpegnoModelDetail.class)));
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
