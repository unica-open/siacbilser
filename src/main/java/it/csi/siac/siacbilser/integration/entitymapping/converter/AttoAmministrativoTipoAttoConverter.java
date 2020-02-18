/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;

@Component
public class AttoAmministrativoTipoAttoConverter extends ExtendedDozerConverter<AttoAmministrativo, SiacTAttoAmm> {

	protected AttoAmministrativoTipoAttoConverter() {
		super(AttoAmministrativo.class, SiacTAttoAmm.class);
	}

	@Override
	public AttoAmministrativo convertFrom(SiacTAttoAmm src, AttoAmministrativo dest) {
		SiacDAttoAmmTipo siacDAttoAmmTipo = src.getSiacDAttoAmmTipo();
		TipoAtto tipoAtto = map(siacDAttoAmmTipo, TipoAtto.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
		dest.setTipoAtto(tipoAtto);
		return dest;
	}

	@Override
	public SiacTAttoAmm convertTo(AttoAmministrativo src, SiacTAttoAmm dest) {
		TipoAtto tipoAtto = src.getTipoAtto();
		SiacDAttoAmmTipo siacDAttoAmmTipo = map(tipoAtto, SiacDAttoAmmTipo.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
		dest.setSiacDAttoAmmTipo(siacDAttoAmmTipo);
		return dest;
	}

}
