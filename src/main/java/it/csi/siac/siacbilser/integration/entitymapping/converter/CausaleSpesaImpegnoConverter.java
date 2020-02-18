/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class CausaleSpesaImpegnoConverter.
 */
@Component
public class CausaleSpesaImpegnoConverter extends GenericImpegnoSubimpegnoBaseConverter<CausaleSpesa, SiacDCausale > {
	
	/**
	 * Instantiates a new causale spesa impegno converter.
	 */
	public CausaleSpesaImpegnoConverter() {
		super(CausaleSpesa.class, SiacDCausale.class);
	}

	@Override
	public CausaleSpesa convertFrom(SiacDCausale src, CausaleSpesa dest) {
		if(src.getSiacRCausaleMovgestTs()!=null){
			for(SiacRCausaleMovgestT siacRCausaleMovgestT : src.getSiacRCausaleMovgestTs()){
				if((src.getDateToExtract() == null && siacRCausaleMovgestT.getDataCancellazione() != null)
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleMovgestT.getDataInizioValidita()))){
					continue;
				}
				SiacTMovgestT siacTMovgestT = siacRCausaleMovgestT.getSiacTMovgestT();
				impostaImpegnoESubImpegno(dest, siacTMovgestT);
			}
		}
		
		return dest;
	}
	
	@Override
	protected void impostaDatiAggiuntiviSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno s) {
		super.impostaDatiAggiuntiviSubImpegno(siacTMovgestT, s);
		aggiungiAnnoOrigineCapitolo(siacTMovgestT, s);
		aggiungiInformazioneDisponibilitaPagare(siacTMovgestT, s);
	}
	
	@Override
	protected void impostaDatiAggiuntiviImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		super.impostaDatiAggiuntiviImpegno(siacTMovgestT, impegno);
		aggiungiInformazioneDisponibilitaPagare(siacTMovgestT, impegno);
		
	}
	

	

	@Override
	public SiacDCausale convertTo(CausaleSpesa src, SiacDCausale dest) {
		Integer movgestId = getMovgestId(src.getImpegno(), src.getSubImpegno());
		
		if(movgestId == null){
			return dest;
		}
		
		dest.setSiacRCausaleMovgestTs(new ArrayList<SiacRCausaleMovgestT>());
		
		SiacRCausaleMovgestT siacRCausaleMovgestT = new SiacRCausaleMovgestT();
		siacRCausaleMovgestT.setSiacDCausale(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRCausaleMovgestT.setSiacTMovgestT(siacTMovgestT);
		
		siacRCausaleMovgestT.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleMovgestT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleMovgestT(siacRCausaleMovgestT);
		
		return dest;
	}

}
