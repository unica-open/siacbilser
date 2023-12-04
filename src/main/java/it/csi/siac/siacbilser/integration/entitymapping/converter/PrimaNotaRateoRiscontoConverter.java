/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaRateiRisconti;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgenser.model.Risconto;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * The Class PrimaNotaRateoRiscontoConverter.
 */
@Component
public class PrimaNotaRateoRiscontoConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	

	public PrimaNotaRateoRiscontoConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		if(src.getSiacTPrimaNotaRateiRiscontis() == null){
			return dest;
		}
		
		for(SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti : src.getSiacTPrimaNotaRateiRiscontis()){
			
			if(siacTPrimaNotaRateiRisconti.getDataCancellazione() == null){
				String codiceTipoRelazione = siacTPrimaNotaRateiRisconti.getSiacDPrimaNotaRelTipo().getPnotaRelTipoCode();
				
				RateoRisconto rateoRisconto = new RateoRisconto();
				if(TipoRelazionePrimaNota.CODICE_RATEO.equals(codiceTipoRelazione)){
					rateoRisconto = new Rateo();
				} else if(TipoRelazionePrimaNota.CODICE_RISCONTO.equals(codiceTipoRelazione)) {
					rateoRisconto = new Risconto();
				}
				
				mapNotNull(siacTPrimaNotaRateiRisconti, rateoRisconto);
				
//				rateoRisconto.setUid(siacTPrimaNotaRateiRisconti.getUid());
//				
//				rateoRisconto.setAnno(siacTPrimaNotaRateiRisconti.getAnno());
//				rateoRisconto.setImporto(siacTPrimaNotaRateiRisconti.getImporto());
//				
//				rateoRisconto.setDataCancellazione(siacTPrimaNotaRateiRisconti.getDataCancellazione());
//				rateoRisconto.setDataCreazione(siacTPrimaNotaRateiRisconti.getDataCreazione());
//				rateoRisconto.setDataFineValidita(siacTPrimaNotaRateiRisconti.getDataFineValidita());
//				rateoRisconto.setDataInizioValidita(siacTPrimaNotaRateiRisconti.getDataInizioValidita());
				
				dest.addRateoRisconto(rateoRisconto);
			}
			
		}
		
		return dest;
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		
		//i ratei non vengono inseriti contestualmente alle primeNote.
		return dest;
	}



	

}
