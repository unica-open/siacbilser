/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class BigDecimalToStringConverter.
 */
public class BigDecimalToStringConverter extends DozerConverter<BigDecimal, String> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/**
	 * Instantiates a new boolean to string base converter.
	 */
	public BigDecimalToStringConverter() {
		super(BigDecimal.class, String.class);
	}

	@Override
	public BigDecimal convertFrom(String src, BigDecimal dest) {
		String methodName = "convertFrom";
		try {
			dest = src == null ? null : new BigDecimal(src);
		} catch(NumberFormatException nfe) {
			log.debug(methodName, nfe.getMessage());
		}
		return dest;
	}

	@Override
	public String convertTo(BigDecimal src, String dest) {
		return src == null ? null : src.toString();
	}

}
