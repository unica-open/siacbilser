/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconModpagTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;

/**
 * The Class MovimentoModalitaPagamentoConverter.
 */
@Component
public class MovimentoModalitaPagamentoCassaConverter extends ExtendedDozerConverter<Movimento, SiacTMovimento> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoModalitaPagamentoCassaConverter() {
		super(Movimento.class, SiacTMovimento.class);
	}

	@Override
	public Movimento convertFrom(SiacTMovimento src, Movimento dest) {
		
		if(src.getSiacDCassaEconModpagTipo() == null){
			return dest;
		}
		
		ModalitaPagamentoCassa modalitaPagamentoCassa = new ModalitaPagamentoCassa();
		modalitaPagamentoCassa.setEnte(dest.getEnte());
		map(src.getSiacDCassaEconModpagTipo(), modalitaPagamentoCassa, CecMapId.SiacDCassaEconModpagTipo_ModalitaPagamentoCassa);
		dest.setModalitaPagamentoCassa(modalitaPagamentoCassa);
			
		return dest;
	}

	@Override
	public SiacTMovimento convertTo(Movimento src, SiacTMovimento dest) {
		
		if(src.getModalitaPagamentoCassa() == null){
			return dest;
		}
		
		SiacDCassaEconModpagTipo siacDCassaEconModpagTipo = new SiacDCassaEconModpagTipo();
		siacDCassaEconModpagTipo.setUid(src.getModalitaPagamentoCassa().getUid());
		
		dest.setSiacDCassaEconModpagTipo(siacDCassaEconModpagTipo);
		
		return dest;
	}



	

}
