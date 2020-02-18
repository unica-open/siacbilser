/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.frontend.webservice.msg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.ServiceResponse;

public class AggiornaCapitoloLimiteImpegnabileResponse extends ServiceResponse
{
	private List<Messaggio> messaggi = new ArrayList<Messaggio>();

	public List<Messaggio> getMessaggi()
	{
		return messaggi;
	}

	public void setMessaggi(List<Messaggio> messaggi)
	{
		this.messaggi = messaggi;
	}

	public void addMessaggio(Messaggio messaggio)
	{
		getMessaggi().add(messaggio);
	}

	public void addMessaggi(Collection<Messaggio> messaggi)
	{
		getMessaggi().addAll(messaggi);
	}
}
