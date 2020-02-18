/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteBaseExt;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;


/**
 * The Class EnteBaseConverter.
 */
@Component
public abstract class EnteBaseConverter<A extends Entita> extends ExtendedDozerConverter<A, SiacTEnteBaseExt> {
	
	/**
	 * Instantiates a EnteBaseConverter.
	 * @param prototypeA the prototype for A
	 */
	public EnteBaseConverter(Class<A> prototypeA) {
		super(prototypeA, SiacTEnteBaseExt.class);
	}

	@Override
	public A convertFrom(SiacTEnteBaseExt src, A dest) {
		Ente ente = map(src.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		setEnte(dest, ente);
		return dest;
	}
	
	protected abstract void setEnte(A dest, Ente ente);

	@Override
	public SiacTEnteBaseExt convertTo(A src, SiacTEnteBaseExt dest) {
		Ente e = getEnte(src);
		SiacTEnteProprietario siacTEnteProprietario = map(e, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		dest.setSiacTEnteProprietario(siacTEnteProprietario);
		return dest;
	}
	
	protected abstract Ente getEnte(A src);
}
