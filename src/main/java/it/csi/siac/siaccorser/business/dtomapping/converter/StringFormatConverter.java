/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.business.dtomapping.converter;

import org.dozer.ConfigurableCustomConverter;

// TODO: Auto-generated Javadoc
/**
 * Converte qualunque Object (non String) in uno String a partire dal formato passato come parametro.
 * Per il formato vedere java.util.Formatter
 * 
 * http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax
 * 
 * @author Domenico
 *
 */
public class StringFormatConverter implements ConfigurableCustomConverter {

	/** The format mask. */
	private String formatMask = "%s"; //se non specificata il default e' di tipo stringa

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(Object a, Object b, Class<?> ac, Class<?> bc) {
		
		if("java.lang.String".equals(bc.getName())){
			return String.format(formatMask, a);
		} else if ("java.lang.String".equals(ac.getName())){
			return String.format(formatMask, b);
		}		
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.ConfigurableCustomConverter#setParameter(java.lang.String)
	 */
	@Override
	public void setParameter(String formatMask) {
		this.formatMask = formatMask; //example: "%05d"		
	}
	
	
	

}
