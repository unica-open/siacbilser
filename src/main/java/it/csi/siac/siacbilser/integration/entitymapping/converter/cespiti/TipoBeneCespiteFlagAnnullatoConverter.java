/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
@Component
public class TipoBeneCespiteFlagAnnullatoConverter extends ExtendedDozerConverter<TipoBeneCespite, SiacDCespitiBeneTipo > {
	
	/**
	 * Instantiates a new tipo bene cespite flag annullato converter.
	 */
	public TipoBeneCespiteFlagAnnullatoConverter() {
		super(TipoBeneCespite.class, SiacDCespitiBeneTipo.class);
	}

	@Override
	public TipoBeneCespite convertFrom(SiacDCespitiBeneTipo src, TipoBeneCespite dest) {
		Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro();
		
		if(dataInizioValiditaFiltro == null) {
			Bilancio bilancio = Utility.BTL.get();
			dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(bilancio.getAnno());
		}
		//TODO labile : vedere se implementare una cosa del tipo data_fine_validita' compresa tra...
		dest.setAnnullato(src.getDataCancellazione() == null && !src.isDataValiditaCompresa(dataInizioValiditaFiltro));
		return dest;
		
	}

	@Override
	public SiacDCespitiBeneTipo convertTo(TipoBeneCespite src, SiacDCespitiBeneTipo dest) {
		return dest;
	}


}
