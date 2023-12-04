/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class LiquidazioneOrdinativoConverter
 */
@Component
public class LiquidazioneModPagConverter extends ModalitaPagamentoSoggettoBaseConverter<Liquidazione, SiacTLiquidazione> {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;

	/**
	 * Instantiates a new liquidazione ordinativo converter.
	 */
	public LiquidazioneModPagConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		
		if(src.getSiacTModpag() != null) {
			
			ModalitaPagamentoSoggetto modPag = map (src.getSiacTModpag(), ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
			dest.setModalitaPagamentoSoggetto(modPag);
			
		}else if(src.getCessioneId() != null && src.getCessioneId() != 0){
			
			 SiacTModpag mdpDef = siacTSoggettoRepository.ricercaModalitaPagamentoCessionePerChiaveDef(src.getCessioneId());
			 if (mdpDef != null){
				 for(SiacRLiquidazioneSoggetto r : src.getSiacRLiquidazioneSoggettos()){
					 if(r.getDataCancellazione() == null){
						ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto(mdpDef, r.getSiacTSoggetto());
						dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto); 
						break;
					 }
				 }
			}
		}
		
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		// Nothing
		return dest;
	}


	

}
