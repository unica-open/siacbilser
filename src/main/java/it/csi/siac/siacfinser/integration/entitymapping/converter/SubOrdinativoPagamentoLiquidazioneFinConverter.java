/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;

@Component
public class SubOrdinativoPagamentoLiquidazioneFinConverter extends ExtendedDozerConverter<SubOrdinativoPagamento, SiacTOrdinativoTFin > {
	
	/**
	 * Instantiates a new sub ordinativo pagamento liquidazione fin converter.
	 */
	public SubOrdinativoPagamentoLiquidazioneFinConverter() {
		super(SubOrdinativoPagamento.class, SiacTOrdinativoTFin.class);
	}

	@Override
	public SubOrdinativoPagamento convertFrom(SiacTOrdinativoTFin src, SubOrdinativoPagamento dest) {
		if(src.getSiacRLiquidazioneOrds()!=null){
			for (SiacRLiquidazioneOrdFin  siacRLiquidazioneOrd : src.getSiacRLiquidazioneOrds()) {
				if(siacRLiquidazioneOrd.getDataCancellazione() == null){
					
					SiacTLiquidazioneFin siacTLiquidazione = siacRLiquidazioneOrd.getSiacTLiquidazione();
					
					Liquidazione liquidazione = mapNotNull(siacTLiquidazione, Liquidazione.class, FinMapId.SiacTLiquidazione_Liquidazione_Base);
					dest.setLiquidazione(liquidazione);
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTOrdinativoTFin convertTo(SubOrdinativoPagamento src, SiacTOrdinativoTFin dest) {
		
		if(src.getLiquidazione() == null || src.getLiquidazione().getUid() == 0){
			return dest;
		}
		
		SiacRLiquidazioneOrdFin siacRLiquidazioneOrdFin = new SiacRLiquidazioneOrdFin();
		
		SiacTLiquidazioneFin siacTLiquidazione = new SiacTLiquidazioneFin();
		siacTLiquidazione.setUid(src.getLiquidazione().getUid());
		siacRLiquidazioneOrdFin.setSiacTLiquidazione(siacTLiquidazione);
		
		siacRLiquidazioneOrdFin.setSiacTOrdinativoT(dest);
		siacRLiquidazioneOrdFin.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRLiquidazioneOrdFin.setLoginOperazione(dest.getLoginOperazione());
		
		dest.setSiacRLiquidazioneOrds(new ArrayList<SiacRLiquidazioneOrdFin>());
		dest.addSiacRLiquidazioneOrd(siacRLiquidazioneOrdFin);
		
		return dest;
	
	}

}
