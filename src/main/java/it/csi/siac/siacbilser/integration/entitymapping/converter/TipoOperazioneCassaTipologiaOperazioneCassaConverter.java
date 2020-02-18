/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipologiaOperazioneCassa;

/**
 * Converter per la tipologia dell'operazione di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/09/2015
 */
public class TipoOperazioneCassaTipologiaOperazioneCassaConverter extends DozerConverter<TipoOperazioneCassa, SiacDCassaEconOperazTipo> {

	/**
	 *  Costruttore vuoto.
	 */
	public TipoOperazioneCassaTipologiaOperazioneCassaConverter() {
		super(TipoOperazioneCassa.class, SiacDCassaEconOperazTipo.class);
	}

	@Override
	public TipoOperazioneCassa convertFrom(SiacDCassaEconOperazTipo src, TipoOperazioneCassa dest) {
		if(src == null) {
			return dest;
		}
		TipologiaOperazioneCassa tipologiaOperazioneCassa = TipologiaOperazioneCassa.byCodice(src.getCassaeconopTipoEntrataspesa());
		dest.setTipologiaOperazioneCassa(tipologiaOperazioneCassa);
		
		return dest;
	}

	@Override
	public SiacDCassaEconOperazTipo convertTo(TipoOperazioneCassa src, SiacDCassaEconOperazTipo dest) {
		if(src == null || src.getTipologiaOperazioneCassa() == null) {
			return dest;
		}
		dest.setCassaeconopTipoEntrataspesa(src.getTipologiaOperazioneCassa().getCodice());
		return dest;
	}
	
}
