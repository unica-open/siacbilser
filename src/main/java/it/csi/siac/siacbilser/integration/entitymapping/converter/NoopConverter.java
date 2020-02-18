/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

/**
 * The Class NoopConverter.
 * <br/>
 * Il converter non effettua alcun tipo di operazione
 */
public class NoopConverter extends DozerConverter<Object, Object> {
	
	/**
	 * Instantiates a new noop converter.
	 */
	public NoopConverter() {
		super(Object.class, Object.class);
	}

	@Override
	public Object convertFrom(Object src, Object dest) {
		return dest;
	}

	@Override
	public Object convertTo(Object src, Object dest) {
		return dest;
	}

}
