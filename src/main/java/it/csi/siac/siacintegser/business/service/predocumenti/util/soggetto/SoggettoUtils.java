/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto;

import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;

public class SoggettoUtils
{
	public static Sesso getSesso(it.csi.siac.siaccommon.util.Sesso sesso)
	{
		return it.csi.siac.siaccommon.util.Sesso.MASCHIO.equals(sesso) ? Sesso.MASCHIO
				: it.csi.siac.siaccommon.util.Sesso.FEMMINA.equals(sesso) ? Sesso.FEMMINA : Sesso.NON_DEFINITO;
	}
}
