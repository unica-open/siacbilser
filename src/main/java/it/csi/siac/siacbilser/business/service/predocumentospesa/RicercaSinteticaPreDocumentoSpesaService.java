/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaPreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaPreDocumentoSpesaService extends CheckedAccountBaseService<RicercaSinteticaPreDocumentoSpesa, RicercaSinteticaPreDocumentoSpesaResponse> {
	
	/** The pre documento spesa dad. */
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	/** The pre doc. */
	private PreDocumentoSpesa preDoc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));		

		checkNotNull(preDoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(preDoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getElementiPerPagina()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
	
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaSinteticaPreDocumentoSpesaResponse executeService(RicercaSinteticaPreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		if(!Boolean.TRUE.equals(req.getSoloImporto())) {
			// Ricerca dei documenti
			// SIAC-4772
			ListaPaginata<PreDocumentoSpesa> listaDocumentoSpesa = preDocumentoSpesaDad.ricercaSinteticaPreDocumento(preDoc, req.getTipoCausale(), req.getDataCompetenzaDa(),
					req.getDataCompetenzaA(), req.getCausaleSpesaMancante(), req.getContoTesoreriaMancante(), req.getSoggettoMancante(),
					req.getProvvedimentoMancante(), req.getNonAnnullati(), req.getOrdinativoPagamento(), req.getParametriPaginazione());
			res.setPreDocumenti(listaDocumentoSpesa);
		}
		
		//Calcolo totale
		BigDecimal importoTotale = preDocumentoSpesaDad.ricercaSinteticaPreDocumentoSpesaImportoTotale(preDoc, req.getTipoCausale(), req.getDataCompetenzaDa(),
				req.getDataCompetenzaA(), req.getCausaleSpesaMancante(), req.getContoTesoreriaMancante(), req.getSoggettoMancante(), req.getProvvedimentoMancante(), req.getNonAnnullati(),
				req.getOrdinativoPagamento());
		res.setImportoTotale(importoTotale);
		

	}
}
