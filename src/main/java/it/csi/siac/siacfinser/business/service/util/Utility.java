/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.util;



import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
/**
 * utility per la parte server
 * 
 * @author 
 *
 */
public class Utility {
		
		private static LogSrvUtil log = new LogSrvUtil(Utility.class);

		/**
		 * Logga un oggetto complesso con annotazione XmlType.
		 * 
		 * @param obj oggetto con annotazione XmlType da convertire in xml per il logging
		 * @param msg nome del parametro da loggare (per il logging)
		 * 
		 */
		public static void logXmlTypeObject(Object obj, String msg) {
			String methodName = "logXmlTypeObject - ";
			if (log.isDebugEnabled()) {			
				String result;
				try {
					result = toXml(obj);
				} catch (PropertyException e) {
					result = e.getMessage();
					log.warn(methodName, "Impossibile loggare "+msg,e);
				} catch (JAXBException e) {
					result = e.getMessage();
					log.warn(methodName, "Impossibile loggare "+msg,e);
				}
				
				log.debug(methodName, msg +": "+result);
			}
		}

		/**
		 * Trasforma un oggetto con annotazione XmlType in una stringa xml.
		 * 
		 * @param obj
		 * @return
		 * @throws JAXBException
		 * @throws PropertyException
		 * 
		 */
		public static String toXml(Object obj) throws JAXBException, PropertyException {
			if(obj == null){
				return null;
			}		
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JAXBElement jx = new JAXBElement(new QName(obj.getClass().getSimpleName()), obj.getClass(), obj);
			marshaller.marshal(jx, sw);
			return sw.toString();

		}
		
		
		
		/**
		 * Ricerca i field dell'oggetto che hanno per tipo la classe passata come parametro 
		 * e restituisce una mappa di tutti i field contenente come chiave il nome del parametro e come valore il valore del parametro.
		 * @param obj
		 * @param typeToSearch
		 * @return
		 * 
		 */
		public static<T> Map<String,T> getFieldNameValueMapByType(Object obj, Class<T> typeToSearch) {		
			return getFieldNameValueMapByType(new BeanWrapperImpl(obj), typeToSearch);
		}

		/**
		 * Ricerca i field dell'oggetto che hanno per tipo la classe passata come parametro 
		 * e restituisce una mappa di tutti i field contenente come chiave il nome del parametro e come valore il valore del parametro.
		 * 
		 * @param bwic
		 * @param typeToSearch
		 * @return
		 *
		 */
		public static<T> Map<String,T> getFieldNameValueMapByType(BeanWrapper bwic, Class<T> typeToSearch) {
			Map<String,T> result = new HashMap<String,T>();		
			
			PropertyDescriptor[] propertyDescriptors = bwic.getPropertyDescriptors();
			
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				Class<?> propertyType = propertyDescriptor.getPropertyType();
				if(propertyType.equals(typeToSearch)){
					String fieldName = propertyDescriptor.getName();
					@SuppressWarnings("unchecked")
					T fieldValue = (T)bwic.getPropertyValue(fieldName);
					result.put(fieldName,fieldValue);
				}
			}
			
			return result;
		}
		
		
		/**
		 * Restituisce il codice jpql per la ricerca case-insensitive e accent-insensitive di un campo.
		 * 
		 * @param jpqlParam
		 * @return
		 */
		public static String toJpqlSearchParam(String jpqlParam) {		
			return " UPPER(TRANSLATE("+jpqlParam+",'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) ";		
		}
		
		
		/**
		 * Restituisce il codice jpql per il confronto case-insensitive e accent-insensitive tra due campi.
		 * 
		 * @param jpql
		 * @return
		 */
		public static String toJpqlSearchLike(String jpqlParamA, String jpqlParamB) {
			return toJpqlSearchParam(jpqlParamA) + " LIKE " + toJpqlSearchParam(jpqlParamB);
		}
		
		
		public static Date convertiDataStringInDate(String dataString)  {		
			
			DateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.ITALIAN);
			Date date = null;
			try {
				date = format.parse(dataString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return date;
			
		}

 }