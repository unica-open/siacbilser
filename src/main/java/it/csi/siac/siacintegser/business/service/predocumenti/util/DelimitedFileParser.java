/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;

@Deprecated
public class DelimitedFileParser<P extends Predocumento>
{
	private InputStream in;
	private String[] fields;
	private String separator;
	private int lineNumber = 0;
	private Class<P> preDocumentoClass;

	public DelimitedFileParser(InputStream in, Class<P> preDocumentoClass)
	{
		this.in = in;
		this.preDocumentoClass = preDocumentoClass;
	}

	public void init(String separator, String... fields)
	{
		this.separator = separator;
		this.fields = fields;

	}

	public Iterator<P> iterator()
	{
		final LineIterator it = IOUtils.lineIterator(new InputStreamReader(in));

		return new Iterator<P>()
		{

			@Override
			public boolean hasNext()
			{
				return it.hasNext();
			}

			@Override
			public P next()
			{
				lineNumber++;

				String line = it.next();

				String[] values = StringUtils.splitPreserveAllTokens(line, separator);

				if (values.length != fields.length)
					throw new IllegalStateException("Numero campi non coerente - linea "
							+ lineNumber);

				try
				{
					P obj = preDocumentoClass.newInstance();

					for (int i = 0; i < fields.length; i++)
					{
						if (StringUtils.isNotEmpty(fields[i]))
							PropertyUtils.setNestedProperty(obj, fields[i], values[i]);
					}

					return obj;
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

}
