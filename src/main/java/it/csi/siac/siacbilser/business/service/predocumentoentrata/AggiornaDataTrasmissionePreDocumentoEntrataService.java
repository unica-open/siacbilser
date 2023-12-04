/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * The Class AggiornaDataTrasmissionePreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDataTrasmissionePreDocumentoEntrataService extends CheckedAccountBaseService<AggiornaDataTrasmissionePreDocumentoEntrata, AggiornaDataTrasmissionePreDocumentoEntrataResponse> {

	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {

		checkNotNull(req.getDataTrasmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data trasmissione"), false);
		checkNotNull(req.getPreDocumentiEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti entrata"));

		if(req.getRicercaSinteticaPreDocumentoEntrata() == null){
			checkCondition(!req.getPreDocumentiEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti entrata"), false);
			for(PreDocumentoEntrata preDoc : req.getPreDocumentiEntrata()){
				checkEntita(preDoc, "predocumento", true);
			}
		} else {
			checkNotNull(req.getRicercaSinteticaPreDocumentoEntrata().getPreDocumentoEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento ricerca sintetica"));
			checkEntita(req.getRicercaSinteticaPreDocumentoEntrata().getPreDocumentoEntrata().getEnte(), "ente predocumento ricerca sintetica", false);
		}
	}

	@Override
	@Transactional
	public AggiornaDataTrasmissionePreDocumentoEntrataResponse executeService(AggiornaDataTrasmissionePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		List<PreDocumentoEntrata> preDocumentiEntrata = getPredocumentiEntrata();

		for (PreDocumentoEntrata preDoc : preDocumentiEntrata) {
			preDocumentoEntrataDad.aggiornaDataTrasmissione(preDoc, req.getDataTrasmissione());
			log.debug(methodName, "Aggiornato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione());
			res.addMessaggio("AGGIORNATO", "Aggiornato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione());
		}
	}

	/**
	 * Gets the predocumenti entrata.
	 *
	 * @return the predocumenti entrata
	 */
	private List<PreDocumentoEntrata> getPredocumentiEntrata() {
		if(req.getRicercaSinteticaPreDocumentoEntrata() != null){
			return ricercaSinteticaPreDocumentoEntrata();
		}

		List<PreDocumentoEntrata> result = new ArrayList<PreDocumentoEntrata>();
		
		for(PreDocumentoEntrata pde : req.getPreDocumentiEntrata()) {
			PreDocumentoEntrata predoc = preDocumentoEntrataDad.findPreDocumentoByIdModelDetail(pde.getUid());
			result.add(predoc);
		}
		return result;
	}

	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @return the list
	 */
	private List<PreDocumentoEntrata> ricercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata reqSintetica = req.getRicercaSinteticaPreDocumentoEntrata();
		List<PreDocumentoEntrata> result = new ArrayList<PreDocumentoEntrata>();
		ListaPaginata<PreDocumentoEntrata> listaPaginata = null;
		
		int numeroPagina = 0;
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(100);
		
		do {
			parametriPaginazione.setNumeroPagina(numeroPagina);
			listaPaginata = preDocumentoEntrataDad.ricercaSinteticaPreDocumentoEntrataModelDetail(reqSintetica.getPreDocumentoEntrata(),
					reqSintetica.getTipoCausale(), reqSintetica.getDataCompetenzaDa(), reqSintetica.getDataCompetenzaA(), reqSintetica.getDataTrasmissioneDa(), reqSintetica.getDataTrasmissioneA(),
					reqSintetica.getCausaleEntrataMancante(), reqSintetica.getSoggettoMancante(), reqSintetica.getProvvedimentoMancante(), reqSintetica.getContoCorrenteMancante(),
					reqSintetica.getNonAnnullati(), reqSintetica.getOrdinativoIncasso(), reqSintetica.getOrdinamentoPreDocumentoEntrata(), parametriPaginazione);
			result.addAll(listaPaginata);
		} while(++numeroPagina < listaPaginata.getTotalePagine());

		return result;
	}

}
