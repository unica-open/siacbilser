/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaPreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaPreDocumentoEntrataService extends CheckedAccountBaseService<RicercaSinteticaPreDocumentoEntrata, RicercaSinteticaPreDocumentoEntrataResponse> {
	

	/** The pre documento entrata dad. */
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	/** The pre doc. */
	private PreDocumentoEntrata preDoc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoEntrata();
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));		
		checkEntita(preDoc.getEnte(), "ente");
		checkParametriPaginazione(req.getParametriPaginazione());
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaSinteticaPreDocumentoEntrataResponse executeService(RicercaSinteticaPreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		//Ricerca dei documenti
		if(!Boolean.TRUE.equals(req.getSoloImporto())) {
			// SIAC-4620
			// SIAC-4772
			ListaPaginata<PreDocumentoEntrata> listaDocumentoEntrata = preDocumentoEntrataDad.ricercaSinteticaPreDocumento(preDoc, req.getTipoCausale(),
					req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getDataTrasmissioneDa(), req.getDataTrasmissioneA(),
					req.getCausaleEntrataMancante(),  req.getSoggettoMancante(), req.getProvvedimentoMancante(), req.getContoCorrenteMancante(), req.getNonAnnullati(),
					req.getOrdinativoIncasso(), req.getOrdinamentoPreDocumentoEntrata(), req.getUidPredocumentiDaFiltrare(), req.getParametriPaginazione());
			res.setPreDocumenti(listaDocumentoEntrata);
		}
		
		//Calcolo totale
		// SIAC-4620
		// SIAC-4772
		BigDecimal importoTotale = preDocumentoEntrataDad.ricercaSinteticaPreDocumentoImportoTotale(preDoc, req.getTipoCausale(),
				req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getDataTrasmissioneDa(), req.getDataTrasmissioneA(),
				req.getCausaleEntrataMancante(),  req.getSoggettoMancante(), req.getProvvedimentoMancante(), req.getContoCorrenteMancante(),
				req.getOrdinativoIncasso(), req.getNonAnnullati(), req.getUidPredocumentiDaFiltrare());
		res.setImportoTotale(importoTotale);
		
	}

}
