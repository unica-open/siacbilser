/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.util;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class PagoPAUtils {

	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
		return xmlGregorianCalendar == null ? null : xmlGregorianCalendar.toGregorianCalendar().getTime();
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
		if (date == null) {
			return null;
		}
		
		GregorianCalendar gc = new GregorianCalendar();
	    gc.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
	}
	
	public static Integer parseInt(String str) {
		return str == null ? null : Integer.valueOf(str);
	}

}
