/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;


/**
 * The Class DefiniscePreDocumentoEntrataPerElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTotaliPreDocumentoEntrataPerStatoService extends CheckedAccountBaseService<RicercaTotaliPreDocumentoEntrataPerStato, RicercaTotaliPreDocumentoEntrataPerStatoResponse> {
	
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		
		// Per la ricerca, almeno uno tra la data di competenza e la causale
		checkCondition(req.getDataCompetenzaDa() != null || req.getDataCompetenzaA() != null || entitaConUid(req.getCausaleEntrata()),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("almeno uno tra data di competenza e causale deve essere presente"));
		
		checkCondition(req.getDataCompetenzaDa() == null || req.getDataCompetenzaA() == null
				|| !req.getDataCompetenzaA().before(req.getDataCompetenzaDa()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data competenza", "la data di competenza da non deve essere inferiore la data di competenza a"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTotaliPreDocumentoEntrataPerStatoResponse executeService(RicercaTotaliPreDocumentoEntrataPerStato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		preDocumentoEntrataDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		// Calcolo importi per ogni stato
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = preDocumentoEntrataDad.findImportoPreDocumentoByStatiOperativiGroupByStatoOperativo(
				req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getCausaleEntrata(), StatoOperativoPreDocumento.values());
		// Calcolo totale per ogni stato
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = preDocumentoEntrataDad.countPreDocumentoByStatiOperativiGroupByStatoOperativo(
				req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getCausaleEntrata(), StatoOperativoPreDocumento.values());
		
		log.debug(methodName, "Importi: " + importiPreDocumenti);
		log.debug(methodName, "Numeri: " + numeroPreDocumenti);
		
		res.setImportiPreDocumenti(importiPreDocumenti);
		res.setNumeroPreDocumenti(numeroPreDocumenti);
	}
}
