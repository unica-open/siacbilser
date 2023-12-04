/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * The Class RicercaElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaElencoDaEmettereService extends CheckedAccountBaseService<RicercaElencoDaEmettere,RicercaElencoDaEmettereResponse> {
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private AttoAmministrativo attoAmministrativo;
	
	@Override
	@Transactional(readOnly=true)
	public RicercaElencoDaEmettereResponse executeService(RicercaElencoDaEmettere serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		attoAmministrativo = req.getAttoAmministrativo();
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco"));
		
		checkEntita(elencoDocumentiAllegato.getEnte(), "ente elenco", false);
		
		checkCondition((attoAmministrativo != null && attoAmministrativo.getUid() != 0) || elencoDocumentiAllegato.getAnno() != null || elencoDocumentiAllegato.getNumero() != null,
			ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), false);
		checkCondition(req.getStatiOperativiElencoDocumenti() != null && !req.getStatiOperativiElencoDocumenti().isEmpty(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato elenco"), false);
		
		checkParametriPaginazione(req.getParametriPaginazione());
	}

	@Override
	protected void execute() {
		
		// Ricerca degli elenchi
		ListaPaginata<ElencoDocumentiAllegato> lista = elencoDocumentiAllegatoDad.ricercaSinteticaElencoDocumentiAllegato(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti(),null, null, req.getParametriPaginazione(),req.getModelDetails());
		res.setElenchiDocumentiAllegato(lista);
		
		// Caricamento dei totali
		caricamentoTotali();
	}

	/**
	 * Caricamento dei totali per la ricerca.
	 */
	private void caricamentoTotali() {
		final String methodName = "caricamentoTotali";
		BigDecimal totaleDaPagare = elencoDocumentiAllegatoDad.calcolaTotaleDaPagare(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti());
		BigDecimal totaleDaIncassare = elencoDocumentiAllegatoDad.calcolaTotaleDaIncassare(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti());
		BigDecimal totaleEntrateCollegate = elencoDocumentiAllegatoDad.calcolaTotaleEntrateCollegate(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti());
		BigDecimal totaleSpeseCollegate = elencoDocumentiAllegatoDad.calcolaTotaleSpeseCollegate(elencoDocumentiAllegato, attoAmministrativo,
				req.getStatiOperativiElencoDocumenti());
		
		log.debug(methodName, "TOTALE DA PAGARE: " + totaleDaPagare + " --- TOTALE DA INCASSARE: " + totaleDaIncassare
				+ " --- TOTALE ENTRATE COLLEGATE: " + totaleEntrateCollegate + " --- TOTALE SPESE COLLEGATE: " + totaleSpeseCollegate);
		
		res.setTotaleDaIncassare(totaleDaIncassare);
		res.setTotaleDaPagare(totaleDaPagare);
		res.setTotaleEntrateCollegate(totaleEntrateCollegate);
		res.setTotaleSpeseCollegate(totaleSpeseCollegate);
	}

}
