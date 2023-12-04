/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneOrd;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * The Class LiquidazioneOrdinativoConverter
 */
@Component
public class LiquidazioneOrdinativoConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione> {

	/**
	 * Instantiates a new liquidazione ordinativo converter.
	 */
	public LiquidazioneOrdinativoConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		List<OrdinativoPagamento> listaOrdinativi = new ArrayList<OrdinativoPagamento>();
		if(src.getSiacRLiquidazioneOrds() != null) {
			for(SiacRLiquidazioneOrd siacRLiquidazioneOrd : src.getSiacRLiquidazioneOrds()) {
				if(siacRLiquidazioneOrd.getDataCancellazione() == null) {
					SiacTOrdinativo siacTOrdinativo = siacRLiquidazioneOrd.getSiacTOrdinativoT().getSiacTOrdinativo();
					OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
					map(siacTOrdinativo, ordinativoPagamento, BilMapId.SiacTOrdinativo_Ordinativo);
					// TODO: aggiungere il sub
					listaOrdinativi.add(ordinativoPagamento);
				}
			}
		}
		// Imposto la lista
		dest.setListaOrdinativi(listaOrdinativi);
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		// Nothing
		return dest;
	}



	

}
