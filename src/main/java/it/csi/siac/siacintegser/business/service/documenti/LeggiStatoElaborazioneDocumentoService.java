/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.DettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.LeggiStatoElaborazioneDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.LeggiStatoElaborazioneDocumentoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.LeggiStatoElaborazioneDocumentoResponse.StatoElaborazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiStatoElaborazioneDocumentoService extends IntegBaseService<LeggiStatoElaborazioneDocumento, LeggiStatoElaborazioneDocumentoResponse> {
	@Autowired
	protected OperazioneAsincronaService operazioneAsincronaService;

	@Override
	protected LeggiStatoElaborazioneDocumentoResponse execute(LeggiStatoElaborazioneDocumento ireq) {
		GetOperazioneAsincResponse getOperazioneAsincResponse = getOperazioneAsincrona(ireq.getIdOperazioneAsincrona());

		GetDettaglioOperazioneAsincronaResponse getDettaglioOperazioneAsincronaResponse = getServiceResponseOperazioneAsincrona(ireq
				.getIdOperazioneAsincrona());

		LeggiStatoElaborazioneDocumentoResponse ires = instantiateNewIRes();

		ires.setStatoElaborazione(StatoElaborazione.valueOf(getOperazioneAsincResponse.getStato()));

		setMessaggi(ires, getDettaglioOperazioneAsincronaResponse.getElencoPaginato());

		return ires;
	}

	private void setMessaggi(LeggiStatoElaborazioneDocumentoResponse ires,
			ListaPaginata<DettaglioOperazioneAsincrona> listaDett) {
		for (DettaglioOperazioneAsincrona dett : listaDett) {
			if (OperazioneAsincronaService.CODICE_SERVICE_RESPONSE.equals(dett.getCodice())) {
				ires.setResponseElaborazione(dett.getServiceResponse());
			} else if (Esito.SUCCESSO.name().equals(dett.getEsito())) {
				addMessaggio(dett.getCodice(), dett.getDescrizione());
			} else if (Esito.FALLIMENTO.name().equals(dett.getEsito())) {
				addErrore(new Errore(dett.getCodice(), dett.getDescrizione()));
			}
		}
	}

	private GetOperazioneAsincResponse getOperazioneAsincrona(Integer idOperazioneAsincrona) {
		GetOperazioneAsinc getOperazioneAsinc = new GetOperazioneAsinc();

		getOperazioneAsinc.setIdOperazione(idOperazioneAsincrona);
		getOperazioneAsinc.setRichiedente(richiedente);

		GetOperazioneAsincResponse getOperazioneAsincResponse = operazioneAsincronaService
				.getOperazioneAsinc(getOperazioneAsinc);

		checkBusinessServiceResponse(getOperazioneAsincResponse);

		return getOperazioneAsincResponse;
	}

	private GetDettaglioOperazioneAsincronaResponse getServiceResponseOperazioneAsincrona(Integer idOperazioneAsincrona) {
		GetDettaglioOperazioneAsincrona getDettaglioOperazioneAsincrona = new GetDettaglioOperazioneAsincrona();

		getDettaglioOperazioneAsincrona.setOpAsincId(idOperazioneAsincrona);
		// getDettaglioOperazioneAsincrona.setCodice(OperazioneAsincronaService.CODICE_SERVICE_RESPONSE);
		getDettaglioOperazioneAsincrona.setRichiedente(richiedente);
		getDettaglioOperazioneAsincrona.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);

		GetDettaglioOperazioneAsincronaResponse getDettaglioOperazioneAsincronaResponse = operazioneAsincronaService
				.getDettaglioOperazioneAsincrona(getDettaglioOperazioneAsincrona);

		checkBusinessServiceResponse(getDettaglioOperazioneAsincronaResponse);

		return getDettaglioOperazioneAsincronaResponse;

	}

}
