/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloDettaglioComponenteImportiCapitoloConverter extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	public TipoComponenteImportiCapitoloDettaglioComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {
		// FIXME [TipoComponenteImportiCapitolo]: desumere la logica corretta
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setEditabile(true);
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dest.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setEditabile(false);
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dest.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		return dest;
	}
}
