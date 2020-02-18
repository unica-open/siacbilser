/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacDModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacRModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class ModalitaPagamentoSoggettoStatoConverter.
 */
public class ModalitaPagamentoSoggettoStatoConverter extends DozerConverter<ModalitaPagamentoSoggetto, SiacTModpag> {
	
	/**
	 * Instantiates a new modalita pagamento soggetto stato converter
	 */
	public ModalitaPagamentoSoggettoStatoConverter() {
		super(ModalitaPagamentoSoggetto.class, SiacTModpag.class);
	}

	@Override
	public ModalitaPagamentoSoggetto convertFrom(SiacTModpag src, ModalitaPagamentoSoggetto dest) {
		if(src.getSiacRModpagStatos() != null){
			for(SiacRModpagStato srms : src.getSiacRModpagStatos()){
				if(srms.getDataCancellazione() == null) {
					SiacDModpagStato siacDModpagStato = srms.getSiacDModpagStato();
					dest.setIdStatoModalitaPagamento(siacDModpagStato.getUid());
					dest.setCodiceStatoModalitaPagamento(siacDModpagStato.getModpagStatoCode());
					dest.setDescrizioneStatoModalitaPagamento(siacDModpagStato.getModpagStatoDesc());
				}
			}	
		}
		
		return dest;
	}
	
	@Override
	public SiacTModpag convertTo(ModalitaPagamentoSoggetto src, SiacTModpag dest) {
		return dest;
	}



}
