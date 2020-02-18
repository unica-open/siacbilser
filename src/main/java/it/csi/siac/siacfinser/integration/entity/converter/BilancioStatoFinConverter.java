/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;



import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.StatoBilancio;
import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;
import it.csi.siac.siacfinser.integration.entity.SiacRBilStatoOpFin;
import it.csi.siac.siacfinser.integration.entity.enumeration.SiacDBilStatoOpFinEnum;

/**
 * Converter per lo stato del Bilancio.
 * 
 * @author 
 * @version 1.0.0 - 09/01/2014
 *
 */
@Component
public class BilancioStatoFinConverter extends DozerConverter<StatoBilancio, List<SiacRBilStatoOpFin>> {
	
	@Autowired
	private EnumEntityFinFactory eef;

	/** Costruttore vuoto */
	@SuppressWarnings("unchecked")
	public BilancioStatoFinConverter() {
		super(StatoBilancio.class, (Class<List<SiacRBilStatoOpFin>>)(Class<?>)List.class);
	}

	@Override
	public StatoBilancio convertFrom(List<SiacRBilStatoOpFin> src, StatoBilancio dest) {
		for(SiacRBilStatoOpFin siacRBilStatoOp : src) {
			if(siacRBilStatoOp.getDataCancellazione() == null) {
				return SiacDBilStatoOpFinEnum.byCodice(siacRBilStatoOp.getSiacDBilStatoOp().getBilStatoOpCode()).getStatoBilancio();
			}
		}
		return null;
	}

	@Override
	public List<SiacRBilStatoOpFin> convertTo(StatoBilancio src, List<SiacRBilStatoOpFin> dest) {
		return dest;
	}
	
}
