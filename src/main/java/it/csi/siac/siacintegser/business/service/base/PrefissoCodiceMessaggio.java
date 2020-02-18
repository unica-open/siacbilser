/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import java.util.Map;

import it.csi.siac.siacintegser.frontend.webservice.util.IntegUtils;

public enum PrefissoCodiceMessaggio
{
	INTEG("0"), COR("1"), BIL("2"), FIN("3"), GEN("4"), CEC("5");

	private String codice;

	private static Map<String, PrefissoCodiceMessaggio> reverseEnumMap = IntegUtils
			.getEnumMap(PrefissoCodiceMessaggio.class);

	PrefissoCodiceMessaggio(String codice)
	{
		this.codice = codice;
	}

	public static String codiceByLabel(String label)
	{
		if (reverseEnumMap.containsKey(label))
			return String.valueOf(reverseEnumMap.get(label).codice);
		
		return "X";
	}
}