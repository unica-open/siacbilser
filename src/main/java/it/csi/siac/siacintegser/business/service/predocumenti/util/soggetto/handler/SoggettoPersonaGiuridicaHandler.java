/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacfinser.model.codifiche.NaturaGiuridicaSoggetto;
import it.csi.siac.siacfinser.model.codifiche.TipoSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.SoggettoUtils;


public abstract class SoggettoPersonaGiuridicaHandler<P extends Predocumento> extends BaseSoggettoHandler<P>
{
	@Override
	public void initInfoPersona(P predocumento)
	{
		predocumento.setCodiceNaturaGiuridica("PGI");
	}
	
	@Override
	protected ParametroRicercaSoggetto buildParametroRicercaSoggetto(String partitaIva)
	{
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		parametroRicercaSoggetto.setPartitaIva(partitaIva);
		
		return parametroRicercaSoggetto;
	}
	
	
	protected Soggetto buildSoggettoInserimento(P predocumento) throws SoggettoPredocumentoException
	{
		Soggetto soggetto = new Soggetto();
		soggetto.setNome(predocumento.getNome());
		soggetto.setCognome(predocumento.getCognome());
		soggetto.setPartitaIva(predocumento.getCodiceFiscale());
		soggetto.setCodiceFiscale(predocumento.getCodiceFiscale());
		soggetto.setDenominazione(StringUtils.trim(String.format("%s %s", predocumento.getNome(), predocumento.getCognome())));
		NaturaGiuridicaSoggetto naturaGiuridicaSoggetto  = new NaturaGiuridicaSoggetto();
		naturaGiuridicaSoggetto.setSoggettoTipoCode("DI");
		soggetto.setNaturaGiuridicaSoggetto(naturaGiuridicaSoggetto);

		List<IndirizzoSoggetto> indirizzi = buildIndirizziSoggettoInserimento(predocumento);

		soggetto.setIndirizzi(indirizzi);

		soggetto.setComuneNascita(predocumento.getComuneNascita());
		soggetto.setTipoSoggetto(TipoSoggetto.newInstanceWithCodice(predocumento.getCodiceNaturaGiuridica()));
		soggetto.setSesso(SoggettoUtils.getSesso(predocumento.getSesso()));
		
		return soggetto;
	}
}
