/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

/**
 * The Class BooleanToStringConverter.
 */
public class BooleanStringToStringFinConverter extends DozerConverter<String, String> {
	
	/**
	 * Instantiates a new causale entrata accertamento converter.
	 */
	public BooleanStringToStringFinConverter() {
		super(String.class, String.class);
	}

	@Override
	public String convertFrom(String src, String dest) {
		// null -> null; S -> Boolean.TRUE; otherwise -> Boolean.FALSE
		return src == null || src.isEmpty() ? null : Boolean.toString("S".equalsIgnoreCase(src));
	}

	@Override
	public String convertTo(String src, String dest) {
		// null -> null; Boolean.TRUE -> S; Boolean.FALSE -> N
		return src == null ? null : (Boolean.TRUE.equals(Boolean.valueOf(src)) ? "S" : "N");
	}

}
