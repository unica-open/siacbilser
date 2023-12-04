/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

/**
 * The Class BooleanToStringBaseConverter.
 */
public abstract class BooleanToStringBaseConverter extends DozerConverter<Boolean, String> {
	
	private String trueString;
	private String falseString;
	
	/**
	 * Instantiates a new boolean to string base converter.
	 *
	 * @param trueString the true string
	 * @param falseString the false string
	 */
	public BooleanToStringBaseConverter(String trueString, String falseString) {
		super(Boolean.class, String.class);
		if(trueString == null){
			throw new IllegalArgumentException("trueString non puo' essere null");
		}
		this.trueString = trueString;
		if(falseString == null){
			throw new IllegalArgumentException("falseString non puo' essere null");
		}
		this.falseString = falseString;
	}

	@Override
	public Boolean convertFrom(String src, Boolean dest) {
		// null -> null; trueString -> Boolean.TRUE; otherwise -> Boolean.FALSE
		return src == null || src.isEmpty() ? null : Boolean.valueOf(trueString.equalsIgnoreCase(src));
	}

	@Override
	public String convertTo(Boolean src, String dest) {
		// null -> null; Boolean.TRUE -> trueString; Boolean.FALSE -> falseString
		return src == null ? null : (Boolean.TRUE.equals(src) ? trueString : falseString);
	}

}
