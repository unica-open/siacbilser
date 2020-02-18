/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilStatoOp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilStatoOpEnum;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.StatoBilancio;

/**
 * Converter per lo stato del Bilancio.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/01/2014
 *
 */
@Component
public class BilancioStatoConverter extends DozerConverter<StatoBilancio, List<SiacRBilStatoOp>> {

	/**
	 *  Costruttore vuoto.
	 */
	@SuppressWarnings("unchecked")
	public BilancioStatoConverter() {
		super(StatoBilancio.class, (Class<List<SiacRBilStatoOp>>)(Class<?>)List.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoBilancio convertFrom(List<SiacRBilStatoOp> src, StatoBilancio dest) {
		if(src!=null){
			for(SiacRBilStatoOp siacRBilStatoOp : src) {
				if(siacRBilStatoOp.getDataCancellazione() == null) {
					return SiacDBilStatoOpEnum.byCodice(siacRBilStatoOp.getSiacDBilStatoOp().getBilStatoOpCode()).getStatoBilancio();
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<SiacRBilStatoOp> convertTo(StatoBilancio src, List<SiacRBilStatoOp> dest) {
		return dest;
	}
	
}
