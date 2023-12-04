/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;

/**
 * The Class MovimentoModalitaPagamentoConverter.
 */
@Component
public class MovimentoModalitaPagamentoDipendenteConverter extends ExtendedDozerConverter<Movimento, SiacTMovimento> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoModalitaPagamentoDipendenteConverter() {
		super(Movimento.class, SiacTMovimento.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Movimento convertFrom(SiacTMovimento src, Movimento dest) {
		
		if(src.getSiacRAccreditoTipoCassaEcon() == null){
			return dest;
		}
		
		ModalitaPagamentoDipendente mpd = new ModalitaPagamentoDipendente();
		map(src.getSiacRAccreditoTipoCassaEcon(), mpd, CecMapId.SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente);
		dest.setModalitaPagamentoDipendente(mpd);
			
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovimento convertTo(Movimento src, SiacTMovimento dest) {
		ModalitaPagamentoDipendente mpd = src.getModalitaPagamentoDipendente();
		
		if(mpd == null){
			return dest;
		}
		
		SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon = new SiacRAccreditoTipoCassaEcon(); //siacRAccreditoTipoCassaEconRepository.findOne(mpd.getUid());
		siacRAccreditoTipoCassaEcon.setUid(mpd.getUid());
		dest.setSiacRAccreditoTipoCassaEcon(siacRAccreditoTipoCassaEcon);
		
		return dest;
		
		
		
	}



	

}
