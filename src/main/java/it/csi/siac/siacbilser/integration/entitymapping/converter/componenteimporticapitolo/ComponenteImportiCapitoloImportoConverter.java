/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import java.math.BigDecimal;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

/**
 * The Class ComponenteImportiCapitoloImportoConverter.
 */
public class ComponenteImportiCapitoloImportoConverter extends DozerConverter<ComponenteImportiCapitolo, SiacTBilElemDetComp> {
	
	/**
	 * Constructor
	 */
	public ComponenteImportiCapitoloImportoConverter() {
		super(ComponenteImportiCapitolo.class, SiacTBilElemDetComp.class);
	}

	@Override
	public ComponenteImportiCapitolo convertFrom(SiacTBilElemDetComp src, ComponenteImportiCapitolo dest) {
		// FIXME [ComponenteImportiCapitolo]: solo STANZIAMENTO
		BigDecimal stanziamento = BigDecimal.ZERO;
		if(src.getElemDetImporto() != null) {
			stanziamento = src.getElemDetImporto();
		}
		BigDecimal impegnato = BigDecimal.ZERO;
		
		String macrotipo = src.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo().getElemDetCompMacroTipoCode();
		
		addImporto(stanziamento, dest, true, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, macrotipo);
		addImporto(impegnato, dest, false, TipoDettaglioComponenteImportiCapitolo.IMPEGNATO, macrotipo);
		
		return dest;
	}

	private void addImporto(BigDecimal importo, ComponenteImportiCapitolo dest, boolean editabile, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo, String macrotipo) {
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setEditabile(editabile);
		dcic.setImporto(importo);
		dcic.setTipoDettaglioComponenteImportiCapitolo(tipoDettaglioComponenteImportiCapitolo);
		
		//SIAC-6884
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		tipoComponenteImportiCapitolo.setMacrotipoComponenteImportiCapitolo(MacrotipoComponenteImportiCapitolo.getByCode(macrotipo)); 
		dest.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo); 

		dest.getListaDettaglioComponenteImportiCapitolo().add(dcic);
	}
	

	@Override
	public SiacTBilElemDetComp convertTo(ComponenteImportiCapitolo src, SiacTBilElemDetComp dest) {
		BigDecimal importo = src.computeImportoByTipoDettaglio(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dest.setElemDetImporto(importo);
		return dest;
	}

}
