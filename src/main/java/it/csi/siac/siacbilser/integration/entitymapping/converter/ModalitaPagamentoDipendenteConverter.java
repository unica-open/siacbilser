/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siaccecser.model.ModalitaAccreditoCassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;

/**
 * The Class ModalitaPagamentoDipendenteModalitaPagamentoConverter.
 */
@Component
public class ModalitaPagamentoDipendenteConverter extends ExtendedDozerConverter<ModalitaPagamentoDipendente, SiacRAccreditoTipoCassaEcon> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public ModalitaPagamentoDipendenteConverter() {
		super(ModalitaPagamentoDipendente.class, SiacRAccreditoTipoCassaEcon.class);
	}

	@Override
	public ModalitaPagamentoDipendente convertFrom(SiacRAccreditoTipoCassaEcon src, ModalitaPagamentoDipendente dest) {
		if(src.getSiacDAccreditoTipo()!=null){
			
			//TODO valutare utilizzo di un converter!
			ModalitaAccreditoSoggetto mas = new ModalitaAccreditoSoggetto();
			mas.setUid(src.getSiacDAccreditoTipo().getUid());
			mas.setCodice(src.getSiacDAccreditoTipo().getAccreditoTipoCode());
			mas.setDescrizione(src.getSiacDAccreditoTipo().getAccreditoTipoDesc());
			dest.setModalitaAccreditoSoggetto(mas);
			
		} else if(src.getSiacDAccreditoTipoCassaEcon()!=null){
			
			//TODO valutare utilizzo di un converter!
			ModalitaAccreditoCassaEconomale mace = new ModalitaAccreditoCassaEconomale();
			mace.setUid(src.getSiacDAccreditoTipoCassaEcon().getUid());
			mace.setCodice(src.getSiacDAccreditoTipoCassaEcon().getCecAccreditoTipoCode());
			mace.setDescrizione(src.getSiacDAccreditoTipoCassaEcon().getCecAccreditoTipoDesc());
			dest.setModalitaAccreditoCassaEconomale(mace);
			
		} else {
			throw new IllegalStateException(ModalitaPagamentoDipendente.class.getSimpleName() + " con uid: "+ src.getUid() 
					+ " non e' associato ne' ad una "+ModalitaAccreditoSoggetto.class+" ne' ad una "+ModalitaAccreditoCassaEconomale.class+".");
		}
		
		return dest;
	}

	@Override
	public SiacRAccreditoTipoCassaEcon convertTo(ModalitaPagamentoDipendente src, SiacRAccreditoTipoCassaEcon dest) {
		
		return dest;
	}



	

}
