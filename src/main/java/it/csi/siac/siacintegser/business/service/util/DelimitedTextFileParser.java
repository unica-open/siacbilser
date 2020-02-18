/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;


public class DelimitedTextFileParser<T>
{
	private LineIterator lineIterator;
	private String delimiter;
	private Iterator<T> iterator;

	public DelimitedTextFileParser(byte[] fileBytes, String separator) {
		this(new ByteArrayInputStream(fileBytes), separator);
	}
	
	public DelimitedTextFileParser(InputStream in, String delimiter) {
		this.lineIterator = IOUtils.lineIterator(new InputStreamReader(in));
		this.delimiter = delimiter;
	}

	public void setPropertyMappingStrategy(Class<T> cls, String... properties) {
		iterator = new PropertyMapperIterator(cls, properties);
	}

	public void setObjectMappingStrategy(LineMapper<T> mapper) {
		iterator = new ObjectMapperIterator(mapper);
	}
	
	public Iterator<T> iterator() {
		return iterator;
	}
	
	public Integer getLineNumber() {
		return iterator != null ? ((BaseMapperIterator) iterator).lineNumber : null;
	}

	public List<String> getElementMessages() {
		return iterator != null ? ((BaseMapperIterator) iterator).messages : null;
	}

	public List<String> getElementErrors() {
		return iterator != null ? ((BaseMapperIterator) iterator).errors : null;
	}

	abstract class BaseMapperIterator implements Iterator<T> {
		
		protected Integer lineNumber = 0;
		protected List<String> messages;
		protected List<String> errors;
		
		@Override
		public boolean hasNext()
		{
			return lineIterator.hasNext();
		}

		@Override
		public T next()
		{
			lineNumber++;
			messages = new ArrayList<String>();
			errors = new ArrayList<String>();
					
			String line = lineIterator.next();

			String[] values = StringUtils.splitPreserveAllTokens(line, delimiter);

			return mapValues(values);
		}

		protected abstract T mapValues(String[] values);
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	class ObjectMapperIterator extends BaseMapperIterator {

		private LineMapper<T> mapper;
		
		public ObjectMapperIterator(LineMapper<T> mapper) {
			this.mapper = mapper;
		}

		@Override
		protected T mapValues(String[] values) {
			T obj = mapper.mapValues(values);
			
			messages.addAll(mapper.getMessages());
			errors.addAll(mapper.getErrors());
			
			return obj;
		}
	}
	
	class PropertyMapperIterator extends BaseMapperIterator {
		
		private Class<T> cls;
		private String[] properties;
		
		public PropertyMapperIterator(Class<T> cls, String... properties) {
			this.cls = cls;
			this.properties = properties;
		}

		@Override
		protected T mapValues(String[] values) {
			if (values.length != properties.length) {
				errors.add(String.format("Numero campi non coerente: %d", values.length));
				
				return null;
			}

			try {
				T obj = cls.newInstance();

				for (int i = 0; i < properties.length; i++) {
					if (StringUtils.isNotEmpty(properties[i]))
						PropertyUtils.setNestedProperty(obj, properties[i], values[i]);
				}

				return obj;
			}
			catch (Exception e) {
				errors.add(e.getMessage());
			}
			
			return null;
		}
	}

	public interface LineMapper<T> {
		T mapValues(String[] values);
		List<String> getMessages();
		List<String> getErrors();
	}
}

