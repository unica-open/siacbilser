/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaTestataDocumentoSpesaService extends CheckedAccountBaseService<RicercaSinteticaDocumentoSpesa, RicercaSinteticaDocumentoSpesaResponse> {
	
	/** The documento spesa dad. */
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	/** The doc. */
	private DocumentoSpesa doc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		doc = req.getDocumentoSpesa();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));		

		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getElementiPerPagina()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaDocumentoSpesaResponse executeService(RicercaSinteticaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		//Ricerca dei documenti
		ListaPaginata<DocumentoSpesa> listaDocumentoSpesa = documentoSpesaDad.ricercaSinteticaTestataDocumentoSpesa(doc, req.getAttoAmministrativo(), req.getImpegno(), req.getRilevanteIva(), req.getParametriPaginazione());
		res.setDocumenti(listaDocumentoSpesa);
		
		//Calcolo del totale importo dei documenti filtrati per gli stessi parametri della ricerca.
		BigDecimal importoTotale = documentoSpesaDad.ricercaSinteticaTestataDocumentoSpesaImportoTotale(doc, req.getAttoAmministrativo(), req.getImpegno(), req.getRilevanteIva(), req.getParametriPaginazione());		
		res.setImportoTotale(importoTotale);		
		
		
	}

}
