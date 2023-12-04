/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;

/**
 * The Class DettaglioVariazioneComponenteImportoCapitoloFlagConverter.
 * <p>
 * La decodifica utilizzata &eacute;, con la priorit&agrave; fattorizzata:
 * <ul>
 *   <li><strong><code>N &Leftrightarrow; FlagNuovaComponenteCapitolo</code></strong></li>
 *   <li><strong><code>A &Leftrightarrow; FlagEliminaComponenteCapitolo</code></strong></li>
 * </ul>
 */
public class DettaglioVariazioneComponenteImportoCapitoloFlagConverter extends DozerConverter<DettaglioVariazioneComponenteImportoCapitolo, SiacTBilElemDetVarComp> {
	
	/**
	 * Constructor
	 */
	public DettaglioVariazioneComponenteImportoCapitoloFlagConverter() {
		super(DettaglioVariazioneComponenteImportoCapitolo.class, SiacTBilElemDetVarComp.class);
	}

	@Override
	public DettaglioVariazioneComponenteImportoCapitolo convertFrom(SiacTBilElemDetVarComp src, DettaglioVariazioneComponenteImportoCapitolo dest) {
		// N <=> FlagNuovaComponenteCapitolo
		// A <=> FlagEliminaComponenteCapitolo
		dest.setFlagNuovaComponenteCapitolo("N".equals(src.getElemDetFlag()));
		dest.setFlagEliminaComponenteCapitolo("A".equals(src.getElemDetFlag()));
		return dest;
	}

	@Override
	public SiacTBilElemDetVarComp convertTo(DettaglioVariazioneComponenteImportoCapitolo src, SiacTBilElemDetVarComp dest) {
		// N <=> FlagNuovaComponenteCapitolo
		// A <=> FlagEliminaComponenteCapitolo
		String elemDetFlag = src.isFlagNuovaComponenteCapitolo()
			? "N"
			: src.isFlagEliminaComponenteCapitolo()
				? "A"
				: null;
		dest.setElemDetFlag(elemDetFlag);
		return dest;
	}

}
