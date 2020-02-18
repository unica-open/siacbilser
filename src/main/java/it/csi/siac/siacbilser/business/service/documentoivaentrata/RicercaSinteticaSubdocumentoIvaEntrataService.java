/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaSubdocumentoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaSubdocumentoIvaEntrataService extends CheckedAccountBaseService<RicercaSinteticaSubdocumentoIvaEntrata, RicercaSinteticaSubdocumentoIvaEntrataResponse> {
	
	/** The subdocumento iva dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaDad;
	
	/** The subdoc iva. */
	private SubdocumentoIvaEntrata subdocIva;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaEntrata();
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva"));		

		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getElementiPerPagina()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaSubdocumentoIvaEntrataResponse executeService(RicercaSinteticaSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		subdocumentoIvaDad.setEnte(subdocIva.getEnte());
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<SubdocumentoIvaEntrata> listaSubdocumentoIvaEntrata = subdocumentoIvaDad.ricercaSinteticaSubdocumentoIvaEntrata(subdocIva, 
				req.getNumeroProtocolloProvvisorioDa(), req.getNumeroProtocolloProvvisorioA(), req.getProtocolloProvvisorioDa(), req.getProtocolloProvvisorioA(), 
				req.getNumeroProtocolloDefinitivoDa(), req.getNumeroProtocolloDefinitivoA(), req.getProtocolloDefinitivoDa(), req.getProtocolloDefinitivoA(),
				req.getProgressivoIVADa(), req.getProgressivoIVAA(),
				req.getParametriPaginazione());
		res.setListaSubdocumentoIvaEntrata(listaSubdocumentoIvaEntrata);		
		
	}

}
