/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacDAccreditoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class ModalitaPagamentoSoggettoAssociatoAConverter.
 */
public class ModalitaPagamentoSoggettoModalitaAccreditoSoggettoConverter extends DozerConverter<ModalitaPagamentoSoggetto, SiacTModpag> {
	
	/**
	 * Instantiates a new modalita pagamento soggetto associato a converter
	 */
	public ModalitaPagamentoSoggettoModalitaAccreditoSoggettoConverter() {
		super(ModalitaPagamentoSoggetto.class, SiacTModpag.class);
	}

	@Override
	public ModalitaPagamentoSoggetto convertFrom(SiacTModpag src, ModalitaPagamentoSoggetto dest) {
		SiacDAccreditoTipo siacDAccreditoTipo = src.getSiacDAccreditoTipo();
		if(siacDAccreditoTipo != null) {
			ModalitaAccreditoSoggetto mas = new ModalitaAccreditoSoggetto();
			
			mas.setUid(siacDAccreditoTipo.getUid());
			mas.setCodice(siacDAccreditoTipo.getAccreditoTipoCode());
			mas.setDescrizione(siacDAccreditoTipo.getAccreditoTipoDesc());
			
			dest.setModalitaAccreditoSoggetto(mas);
		}
		return dest;
	}
	
	@Override
	public SiacTModpag convertTo(ModalitaPagamentoSoggetto src, SiacTModpag dest) {
		return dest;
	}



}
