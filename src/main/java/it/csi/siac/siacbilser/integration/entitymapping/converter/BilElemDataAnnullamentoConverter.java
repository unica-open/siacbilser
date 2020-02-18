/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.model.Capitolo;

/**
 * The Class BilElemDataAnnullamentoConverter.
 */
public class BilElemDataAnnullamentoConverter extends DozerConverter<Capitolo<?, ?>, SiacTBilElem> {

	/**
	 * Instantiates a new bil elem data annullamento converter.
	 */
	@SuppressWarnings("unchecked")
	public BilElemDataAnnullamentoConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		
		if (src.getSiacRBilElemStatos() != null) {
			for (SiacRBilElemStato rstato : src.getSiacRBilElemStatos()) {
				if (rstato.getDataCancellazione() == null) {
					String statoCode = rstato.getSiacDBilElemStato().getElemStatoCode();
					if (SiacDBilElemStatoEnum.byCodice(statoCode).equals(SiacDBilElemStatoEnum.Annullato)) {
						dest.setDataAnnullamento(rstato.getDataModifica());
					}
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {
		return dest;
	}

}
