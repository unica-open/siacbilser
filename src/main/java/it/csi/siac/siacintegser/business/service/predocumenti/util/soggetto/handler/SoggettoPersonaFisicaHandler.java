/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccommon.util.codicefiscale.CodiceFiscale;
import it.csi.siac.siaccommon.util.date.DateConverter;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.model.codifiche.TipoSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.SoggettoUtils;


public abstract class SoggettoPersonaFisicaHandler<P extends Predocumento> extends BaseSoggettoHandler<P>
{
	@Autowired
	private transient GenericService genericService;

	@Override
	public void initInfoPersona(P predocumento) throws SoggettoPredocumentoException
	{
		CodiceFiscale codiceFiscale = CodiceFiscale.getCodiceFiscale(predocumento 
				.getCodiceFiscale());

		predocumento.setCodiceNaturaGiuridica("PF");

		predocumento.setDataNascita(DateConverter.convertToString(codiceFiscale.getDataNascita()));

		predocumento.setComuneNascita(getComuneNascitaFromCodiceIstatComune(codiceFiscale.getCodiceIstatComune()));

		predocumento.setSesso(codiceFiscale.getSesso());
	
	}

	protected Soggetto buildSoggettoInserimento(P predocumento) throws SoggettoPredocumentoException
	{
		Soggetto soggetto = new Soggetto();
		soggetto.setNome(predocumento.getNome());
		soggetto.setCognome(predocumento.getCognome());
		soggetto.setCodiceFiscale(predocumento.getCodiceFiscale());
		soggetto.setDataNascita(DateConverter.convertFromString(predocumento.getDataNascita()));
		
		List<IndirizzoSoggetto> indirizzi = buildIndirizziSoggettoInserimento(predocumento);

		soggetto.setIndirizzi(indirizzi);

		soggetto.setComuneNascita(predocumento.getComuneNascita());
		soggetto.setTipoSoggetto(TipoSoggetto.newInstanceWithCodice(predocumento.getCodiceNaturaGiuridica()));
		soggetto.setSesso(SoggettoUtils.getSesso(predocumento.getSesso()));
		
		return soggetto;
	}
	
	private ComuneNascita getComuneNascitaFromCodiceIstatComune(String codiceIstatComune) throws SoggettoPredocumentoException
	{
		ListaComuni listaComuni = new ListaComuni();
		listaComuni.setCodiceCatastale(codiceIstatComune);
		listaComuni.setRichiedente(richiedente);

		ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

		checkServiceResponseFallimento(lcr);

		// SIAC-5194: se ho piu' di un comune esco
		if (lcr.getListaComuni().isEmpty())
			throw new SoggettoPredocumentoException(String.format("Codice catastale comune %s non trovato", codiceIstatComune));

		return lcr.getListaComuni().get(0);
	}

	@Override
	protected ParametroRicercaSoggetto buildParametroRicercaSoggetto(P predocumento)
	{
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		parametroRicercaSoggetto.setCodiceFiscale(predocumento.getCodiceFiscale());
		
		return parametroRicercaSoggetto;
	}


	
	
}

