/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;

public enum PrefissoCodiceMessaggio
{
	X("X"), INTEG("0"), COR("1"), BIL("2"), FIN("3"), GEN("4"), CEC("5");

	private String codice;

	PrefissoCodiceMessaggio(String codice)
	{
		this.codice = codice;
	}

	public String getCodice() {
		return codice;
	}
	
	public static String getCodice(String name)
	{
		return EnumUtils.isValidEnum(PrefissoCodiceMessaggio.class, name) ? 
				ObjectUtils.defaultIfNull(PrefissoCodiceMessaggio.valueOf(name), X).getCodice() :
				X.getCodice();
	}
}

