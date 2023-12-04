/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Util {
	
	
	 public static XMLGregorianCalendar  toXMLGregorianCalendar(String data){
		  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		  XMLGregorianCalendar xmlGregCal=null ;
		  try {  Date date = format.parse(data);

		  GregorianCalendar cal = new GregorianCalendar();
		  cal.setTime(date);

		  xmlGregCal= DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);

//		  xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
//		  xmlGregCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		  } catch (Exception e) {
			  // TODO Auto-generated catch block
			e.printStackTrace();
		  }

		  return xmlGregCal;
	  }
	 
	 
	 public static XMLGregorianCalendar  toXMLGregorianCalendar(Date date){
		  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		  XMLGregorianCalendar xmlGregCal=null ;
		  try {  

		  GregorianCalendar cal = new GregorianCalendar();
		  cal.setTime(date);

		  xmlGregCal= DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);

//		  xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
//		  xmlGregCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		  } catch (Exception e) {
			  // TODO Auto-generated catch block
			e.printStackTrace();
		  }

		  return xmlGregCal;
	  }

}
