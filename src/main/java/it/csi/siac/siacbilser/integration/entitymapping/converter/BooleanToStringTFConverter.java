/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;


/**
 * Converte i boolean true/false nelle stringhe T/S.
 * Il valore di default è T
 */
public class BooleanToStringTFConverter extends DozerConverter<Boolean, String> {
	
	/**
	 * Instantiates a new boolean to string manuale automatico converter.
	 */
	public BooleanToStringTFConverter() {
		super(Boolean.class, String.class);
	}

	@Override
	public Boolean convertFrom(String src, Boolean dest) {
		
		return src == null || src.isEmpty() ? null : Boolean.valueOf("T".equalsIgnoreCase(src));
	}

	@Override
	public String convertTo(Boolean src, String dest) {
		return src == null ? "T" : (Boolean.TRUE.equals(src) ? "T" : "F");
	}
}
