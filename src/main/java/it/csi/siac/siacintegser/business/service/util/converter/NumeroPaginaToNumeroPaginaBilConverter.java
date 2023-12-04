/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**

 * Ricalcola il numero di pagina per i servizi di bil che partono da 0
 * @author 1513
 *
 */
public class NumeroPaginaToNumeroPaginaBilConverter extends DozerConverter<Integer, Integer>
{
	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	public NumeroPaginaToNumeroPaginaBilConverter()
	{
		super(Integer.class, Integer.class);
	}

	@Override
	public Integer convertFrom(Integer src, Integer dest)
	{
		String methodName = "convertFrom";
		try
		{
			dest = src == null ? null : src-1;
		}
		catch (NumberFormatException nfe)
		{
			log.debug(methodName, nfe.getMessage());
		}
		
		return dest;
	}

	@Override
	public Integer convertTo(Integer src, Integer dest)
	{
		return src == null ? null : src -1;
	}

}
