/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

/**
 * The Class BooleanToBooleanConverter.
 */
public class BooleanToBooleanConverter extends DozerConverter<Boolean, Boolean> {
	
	private Boolean defaultValue;
	
	/**
	 * Instantiates a new boolean to boolean converter.
	 */
	public BooleanToBooleanConverter() {
		super(Boolean.class, Boolean.class);
	}
	
	@Override
	public void setParameter(String parameter) {
		defaultValue = Boolean.valueOf(parameter);
	}

	@Override
	public Boolean convertFrom(Boolean src, Boolean dest) {
		return convert(src);
	}

	@Override
	public Boolean convertTo(Boolean src, Boolean dest) {
		return convert(src);
	}
	
	/**
	 * Metodo comune di conversione. Imposta il valore di default se non specificato
	 * @param src la sorgente
	 * @return il valore sorgente se differente da null, il valore di default altrimenti
	 */
	private Boolean convert(Boolean src) {
		return src == null ? defaultValue : src;
	}

}
