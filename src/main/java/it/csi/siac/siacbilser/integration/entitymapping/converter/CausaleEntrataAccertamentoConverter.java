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
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * The Class CausaleEntrataAccertamentoConverter.
 */
@Component
public class CausaleEntrataAccertamentoConverter extends GenericAccertamentoSubaccertamentoBaseConverter<CausaleEntrata, SiacDCausale > {
	
	/**
	 * Instantiates a new causale entrata accertamento converter.
	 */
	public CausaleEntrataAccertamentoConverter() {
		super(CausaleEntrata.class, SiacDCausale.class);
	}

	@Override
	public CausaleEntrata convertFrom(SiacDCausale src, CausaleEntrata dest) {
		if(src.getSiacRCausaleMovgestTs()!=null){
			for(SiacRCausaleMovgestT siacRCausaleMovgestT : src.getSiacRCausaleMovgestTs()){
				if((src.getDateToExtract() == null && siacRCausaleMovgestT.getDataCancellazione() != null)
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleMovgestT.getDataInizioValidita()))){
					continue;
				}
				SiacTMovgestT siacTMovgestT = siacRCausaleMovgestT.getSiacTMovgestT();
				impostaAccertamentoESubAccertamento(dest, siacTMovgestT);
			}
		}
		
		return dest;
	}

	@Override
	protected void impostaDatiAggiuntiviSubAccertamento(SiacTMovgestT siacTMovgestT, SubAccertamento s) {
		aggiungiAnnoOrigineCapitolo(siacTMovgestT, s);
		aggiungiImportoAccertamento(siacTMovgestT, s);
		aggiungiDisponibilitaIncassare(siacTMovgestT, s);
	}
	
	@Override
	protected void impostaDatiAggiuntiviAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		aggiungiAnnoOrigineCapitolo(siacTMovgestT, accertamento);
		aggiungiImportoAccertamento(siacTMovgestT, accertamento);
		aggiungiDisponibilitaIncassare(siacTMovgestT, accertamento);
	}

	@Override
	public SiacDCausale convertTo(CausaleEntrata src, SiacDCausale dest) {
		Integer movgestId = getMovgestId(src.getAccertamento(), src.getSubAccertamento());
		
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
