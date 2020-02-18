/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class ListStringListSoggettiConverter extends DozerConverter<List<String>, List<Soggetto>>
{

	private LogUtil log = new LogUtil(this.getClass());

	@SuppressWarnings("unchecked")
	public ListStringListSoggettiConverter()
	{
		super((Class<List<String>>) (Class<?>) List.class,
				(Class<List<Soggetto>>) (Class<?>) List.class);
	}

	public ListStringListSoggettiConverter(Class<List<String>> prototypeA,
			Class<List<Soggetto>> prototypeB)
	{
		super(prototypeA, prototypeB);
	}

	@Override
	public List<String> convertFrom(List<Soggetto> arg0, List<String> arg1)
	{
		if (arg0==null)
			return null;
		
		arg1 = new ArrayList<String>();
		
		for (Soggetto s : arg0)
		{
			arg1.add(s.getCodiceSoggetto());
		}
		
		
		return arg1;
	}

	@Override
	public List<Soggetto> convertTo(List<String> arg0, List<Soggetto> arg1)
	{
		throw new UnsupportedOperationException();
	}

}
