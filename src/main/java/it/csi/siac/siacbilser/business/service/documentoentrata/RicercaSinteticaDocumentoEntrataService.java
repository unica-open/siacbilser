/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * The Class RicercaSinteticaDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaDocumentoEntrataService extends
		ExtendedBaseService<RicercaSinteticaDocumentoEntrata, RicercaSinteticaDocumentoEntrataResponse> {

	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	/** The doc. */
	private DocumentoEntrata doc;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));

		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));

		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina() >= 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getElementiPerPagina() > 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition( (req.getElencoDocumenti() == null) ||  
				((req.getElencoDocumenti().getAnno() != null) && (req.getElencoDocumenti().getNumero() != null)), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno e numero elenco"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaDocumentoEntrataResponse executeService(RicercaSinteticaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		String reqXML = JAXBUtility.marshall(req);
		log.error("EXECUTE_TEST", reqXML);
		
		//Ricerca dei documenti
		ListaPaginata<DocumentoEntrata> listaDocumentoEntrata = documentoEntrataDad.ricercaSinteticaDocumentoEntrata(doc, req.getAttoAmministrativo(), req.getAccertamento(), req.getRilevanteIva(), req.getElencoDocumenti(), req.getContabilizzaGenPcc(), req.getParametriPaginazione());
		res.setDocumenti(listaDocumentoEntrata);
		
		//Calcolo del totale importo dei documenti filtrati per gli stessi parametri della ricerca.
		BigDecimal importoTotale = documentoEntrataDad.ricercaSinteticaDocumentoEntrataImportoTotale(doc, req.getAttoAmministrativo(), req.getAccertamento(), req.getRilevanteIva(),req.getElencoDocumenti(), req.getContabilizzaGenPcc(), req.getParametriPaginazione());		
		res.setImportoTotale(importoTotale);

	}

}
