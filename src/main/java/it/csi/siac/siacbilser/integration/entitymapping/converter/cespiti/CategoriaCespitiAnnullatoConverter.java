/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * The Class CategoriaCespitiTipoCalcoloConverter.
 *
 * @author Domenico
 */
@Component
public class CategoriaCespitiAnnullatoConverter extends ExtendedDozerConverter<CategoriaCespiti, SiacDCespitiCategoria> {
	
	/**
	 * costruttore
	 */
	public CategoriaCespitiAnnullatoConverter() {
		super(CategoriaCespiti.class, SiacDCespitiCategoria.class);
	}

	@Override
	public CategoriaCespiti convertFrom(SiacDCespitiCategoria src, CategoriaCespiti dest) {
		
		Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro();
		
		if(dataInizioValiditaFiltro == null) {
			Bilancio bilancio = Utility.BTL.get();
			dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(bilancio.getAnno());
		}
		dest.setAnnullato(src.getDataCancellazione() == null && !src.isDataValiditaCompresa(dataInizioValiditaFiltro));
		return dest;
	}

	@Override
	public SiacDCespitiCategoria convertTo(CategoriaCespiti src, SiacDCespitiCategoria dest) {
		return dest;
	}



	

}
