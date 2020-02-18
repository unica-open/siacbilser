/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.elencodocumentiallegato;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Servizio di ricerca totali del predocumento per elenco
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTotaliPredocumentiPerElencoService extends CheckedAccountBaseService<RicercaTotaliPredocumentiPerElenco, RicercaTotaliPredocumentiPerElencoResponse> {

	// Spesa o entrata sono equivalenti
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getElencoDocumentiAllegato(), "elenco documenti allegato", true);
		
		if(req.getElencoDocumentiAllegato().getUid() == 0) {
			checkEntita(req.getEnte(), "ente");
			checkNotNull(req.getElencoDocumentiAllegato().getAnno(), "anno elenco documenti allegato");
			checkNotNull(req.getElencoDocumentiAllegato().getNumero(), "numero elenco documenti allegato");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTotaliPredocumentiPerElencoResponse executeService(RicercaTotaliPredocumentiPerElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		// Controllo di esistenza dell'elenco
		checkElenco();
		// Calcolo importi per ogni stato
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = preDocumentoSpesaDad.findImportoPreDocumentoByElencoIdAndStatiOperativiGroupByStatoOperativo(
				elencoDocumentiAllegato.getUid(), null, null, StatoOperativoPreDocumento.values());
		// Calcolo totale per ogni stato
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = preDocumentoSpesaDad.countPreDocumentoByElencoIdAndStatiOperativiGroupByStatoOperativo(
				elencoDocumentiAllegato.getUid(), null, null, StatoOperativoPreDocumento.values());
		
		log.debug(methodName, "Importi: " + importiPreDocumenti);
		log.debug(methodName, "Numeri: " + numeroPreDocumenti);
		
		res.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		res.setImportiPreDocumenti(importiPreDocumenti);
		res.setNumeroPreDocumenti(numeroPreDocumenti);
	}

	/**
	 * Controlla che l'elenco esista
	 */
	private void checkElenco() {
		if(req.getElencoDocumentiAllegato().getUid() != 0) {
			// Ricerca per uid se presente
			checkElencoByUid();
			return;
		}
		// Ricerca per anno e numero
		checkElencoByAnnoNumero();
	}

	/**
	 * Ricerca dell'elenco a partire dall'uid
	 */
	private void checkElencoByUid() {
		elencoDocumentiAllegato = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoMinimalLightById(req.getElencoDocumentiAllegato().getUid());
		if(elencoDocumentiAllegato == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Elenco", "uid " + req.getElencoDocumentiAllegato().getUid()));
		}
	}
	
	/**
	 * Ricerca dell'elenco a partire da anno e numero
	 */
	private void checkElencoByAnnoNumero() {
		elencoDocumentiAllegato = elencoDocumentiAllegatoDad.findElencoDocByAnnoNumero(req.getElencoDocumentiAllegato().getAnno(), req.getElencoDocumentiAllegato().getNumero(), req.getEnte());
		if(elencoDocumentiAllegato == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Elenco", req.getElencoDocumentiAllegato().getUid() + "/" + req.getElencoDocumentiAllegato().getNumero()));
		}
	}

}
