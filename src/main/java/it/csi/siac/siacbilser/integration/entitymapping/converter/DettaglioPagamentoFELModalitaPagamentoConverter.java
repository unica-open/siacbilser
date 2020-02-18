/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDModalitaPagamento;
import it.csi.siac.siacbilser.integration.entity.SirfelDModalitaPagamentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTDettaglioPagamento;
import it.csi.siac.siacbilser.integration.entity.enumeration.SirfelDModalitaPagamentoEnum;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;

/**
 * The Class DettaglioPagamentoFELModalitaPagamentoConverter.
 */
@Component
public class DettaglioPagamentoFELModalitaPagamentoConverter extends DozerConverter<DettaglioPagamentoFEL, SirfelTDettaglioPagamento> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	/**
	 * Instantiates a new documento spesa stato converter.
	 */
	public DettaglioPagamentoFELModalitaPagamentoConverter() {
		super(DettaglioPagamentoFEL.class, SirfelTDettaglioPagamento.class);
	}

	@Override
	public DettaglioPagamentoFEL convertFrom(SirfelTDettaglioPagamento src, DettaglioPagamentoFEL dest) {
		if(src.getSirfelDModalitaPagamento() != null){
			SirfelDModalitaPagamentoEnum modPag = SirfelDModalitaPagamentoEnum.byCodice(src.getSirfelDModalitaPagamento().getCodice());
			dest.setModalitaPagamentoFEL(modPag.getMpf());
		}
		return dest;
	}

	@Override
	public SirfelTDettaglioPagamento convertTo(DettaglioPagamentoFEL src, SirfelTDettaglioPagamento dest) {
		if(src.getModalitaPagamentoFEL() != null) {
			
			SirfelDModalitaPagamentoPK pk = new SirfelDModalitaPagamentoPK();
			pk.setCodice(src.getModalitaPagamentoFEL().getCodice());
			pk.setEnteProprietarioId(src.getEnte().getUid());
			SirfelDModalitaPagamento sirfelDModalitaPagamento = sirfelTFatturaRepository.findSirfelDModalitaPagamentoBySirfelDModalitaPagamentoPK(pk);
			
			dest.setSirfelDModalitaPagamento(sirfelDModalitaPagamento);
			
			dest.setModalitaPagamento(src.getModalitaPagamentoFEL().getCodice());
		}
		
		return dest;
	}



	

}
