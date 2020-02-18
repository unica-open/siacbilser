/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.soggetto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacfinser.business.service.soggetto.RicercaCodiceSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettoPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCodiceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCodiceSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaDettaglioSoggetti;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaDettaglioSoggettiResponse;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioSoggettoService extends
		BaseRicercaSoggettiService<RicercaDettaglioSoggetti, RicercaDettaglioSoggettiResponse>
{
	private Map<String, Soggetto> codiceSoggettoMap = new HashMap<String, Soggetto>();
	private Map<Integer, String> uidSoggettoCodiceMap = new HashMap<Integer, String>();

	@Override
	protected RicercaDettaglioSoggettiResponse execute(RicercaDettaglioSoggetti ireq)
	{
		RicercaDettaglioSoggettiResponse ires;

		if (StringUtils.isNotBlank(ireq.getCodice()))
			ires = ricercaSoggettoPerCodice(ireq.getCodice());
		else
			ires = ricercaDettaglioSoggetti(ireq);

		mapSoggettiCollegati(ires);

		return ires;
	}

	private RicercaDettaglioSoggettiResponse ricercaSoggettoPerCodice(String codiceSoggetto)
	{
		RicercaSoggettoPerChiaveResponse res = ricercaSoggettoPerChiave(codiceSoggetto);
		
		RicercaDettaglioSoggettiResponse ires = map(res,
				RicercaDettaglioSoggettiResponse.class,
				IntegMapId.RicercaDettaglioSoggettiResponse_RicercaSoggettoPerChiaveResponse);

		if (res.getSoggetto() == null)
		{
			ires.clearSoggetti();

			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO,
					String.format("codice %s inesistente", codiceSoggetto));
		}
		
		return ires;
	}

	private RicercaDettaglioSoggettiResponse ricercaDettaglioSoggetti(RicercaDettaglioSoggetti ireq)
	{
		RicercaSoggettiResponse ricercaSoggettiResponse = ricercaSoggetti(ireq,
				IntegMapId.BaseRicercaSoggetti_RicercaSoggetti);

		ricercaSoggettiResponse.setSoggetti(ricercaSoggettiPerChiave(ricercaSoggettiResponse.getSoggetti()));

		RicercaDettaglioSoggettiResponse ires = map(ricercaSoggettiResponse, RicercaDettaglioSoggettiResponse.class,
				IntegMapId.RicercaDettaglioSoggettiResponse_RicercaSoggettiResponse);

		if (ires.getSoggetti().isEmpty())
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "nessun criterio di ricerca soddisfatto");

		return ires;
	}

	private List<Soggetto> ricercaSoggettiPerChiave(List<Soggetto> soggetti)
	{
		List<Soggetto> soggettiPerChiave = new ArrayList<Soggetto>();

		for (Soggetto soggetto : soggetti)
		{
			RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveResponse = ricercaSoggettoPerChiave(soggetto
					.getCodiceSoggetto());

			soggettiPerChiave.add(ricercaSoggettoPerChiaveResponse.getSoggetto());
		}

		return soggettiPerChiave;
	}

	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(String codiceSoggetto)
	{
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();

		ricercaSoggettoPerChiave.setEnte(ente);
		ricercaSoggettoPerChiave.setRichiedente(richiedente);

		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();

		parametroSoggettoK.setCodice(codiceSoggetto);

		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroSoggettoK);

		RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveResponse = appCtx.getBean(
				RicercaSoggettoPerChiaveService.class).executeService(ricercaSoggettoPerChiave);

		checkBusinessServiceResponse(ricercaSoggettoPerChiaveResponse);

		codiceSoggettoMap.put(codiceSoggetto, ricercaSoggettoPerChiaveResponse.getSoggetto());

		return ricercaSoggettoPerChiaveResponse;
	}

	private void mapSoggettiCollegati(RicercaDettaglioSoggettiResponse ires)
	{
		for (it.csi.siac.siacintegser.model.integ.Soggetto soggettoInteg : ires.getSoggetti())
		{
			Soggetto soggetto = codiceSoggettoMap.get(soggettoInteg.getCodice());

			soggettoInteg
					.setCodiciSoggettiCollegatiPrecedenti(mapSoggettiCollegati(soggetto.getIdsSoggettiPrecedenti()));

			soggettoInteg
					.setCodiciSoggettiCollegatiSuccessivi(mapSoggettiCollegati(soggetto.getIdsSoggettiSuccessivi()));
		}
	}

	private List<String> mapSoggettiCollegati(List<Integer> uidSoggetti)
	{
		Set<String> codiciSoggetto = new HashSet<String>();

		if (uidSoggetti != null && !uidSoggetti.isEmpty())
			for (Integer uidSoggetto : uidSoggetti)
				codiciSoggetto.add(getCodiceSoggettoByUidSoggetto(uidSoggetto));

		return new ArrayList<String>(codiciSoggetto);
	}

	private String getCodiceSoggettoByUidSoggetto(Integer uidSoggetto)
	{
		if (!uidSoggettoCodiceMap.containsKey(uidSoggetto))
			uidSoggettoCodiceMap.put(uidSoggetto, ricercaCodiceSoggettoByUidSoggetto(uidSoggetto).getCodiceSoggetto());

		return uidSoggettoCodiceMap.get(uidSoggetto);
	}

	private RicercaCodiceSoggettoResponse ricercaCodiceSoggettoByUidSoggetto(Integer uidSoggetto)
	{
		RicercaCodiceSoggetto ricercaCodiceSoggetto = new RicercaCodiceSoggetto();

		ricercaCodiceSoggetto.setRichiedente(richiedente);
		ricercaCodiceSoggetto.setUidSoggetto(uidSoggetto);

		RicercaCodiceSoggettoResponse ricercaCodiceSoggettoResponse = appCtx
				.getBean(RicercaCodiceSoggettoService.class).executeService(ricercaCodiceSoggetto);

		checkBusinessServiceResponse(ricercaCodiceSoggettoResponse);

		return ricercaCodiceSoggettoResponse;
	}
}
