/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;

import java.util.List;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacintegser.model.integ.Recapito;

public class RecapitoListIndirizzoSoggettoConverter extends
		DozerConverter<Recapito, List<IndirizzoSoggetto>>
{

	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	@SuppressWarnings("unchecked")
	public RecapitoListIndirizzoSoggettoConverter()
	{
		super(Recapito.class, (Class<List<IndirizzoSoggetto>>) (Class<?>) List.class);
	}

	public RecapitoListIndirizzoSoggettoConverter(Class<Recapito> prototypeA,
			Class<List<IndirizzoSoggetto>> prototypeB)
	{
		super(prototypeA, prototypeB);
	}

	@Override
	public List<IndirizzoSoggetto> convertTo(Recapito paramA, List<IndirizzoSoggetto> paramB)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Recapito convertFrom(List<IndirizzoSoggetto> listIndirizzoSoggetto, Recapito recapito)
	{
		for (IndirizzoSoggetto indirizzoSoggetto : listIndirizzoSoggetto)
		{
			if (indirizzoSoggetto.isIndirizzoPrincipale())
			{
				recapito.setCap(indirizzoSoggetto.getCap());
				recapito.setCodiceNazione(indirizzoSoggetto.getCodiceNazione());
				recapito.setDescrizioneNazione(indirizzoSoggetto.getNazione());
				recapito.setComune(indirizzoSoggetto.getComune());
				recapito.setProvincia(indirizzoSoggetto.getProvincia());
				recapito.setCodiceIstatComune(indirizzoSoggetto.getCodiceIstatComune());
				recapito.setIndirizzo(indirizzoSoggetto.getDenominazione());
				recapito.setNumeroCivico(indirizzoSoggetto.getNumeroCivico());
				recapito.setSedime(indirizzoSoggetto.getSedime());

				return recapito;
			}
		}
		;

		return recapito;
	}

}
