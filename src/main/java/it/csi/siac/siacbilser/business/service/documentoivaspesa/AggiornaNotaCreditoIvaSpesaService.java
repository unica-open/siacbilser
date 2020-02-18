/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesaResponse;

/**
 * Implementazione del servizio AggiornaNotaCreditoIvaSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaNotaCreditoIvaSpesaService extends CrudSubdocumentoIvaSpesaBaseService<AggiornaNotaCreditoIvaSpesa, AggiornaNotaCreditoIvaSpesaResponse> {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaSpesa();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva spesa"));
		checkCondition(subdocIva.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva spesa"));

		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva spesa"), false);
		
		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva spesa"), false);
		
//		checkNotNull(subdocIva.getSubdocumentoIvaPadre(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva padre"), false);
//		checkCondition(subdocIva.getSubdocumentoIvaPadre().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva padre"), false);
		checkEntita(subdocIva.getSubdocumentoIvaPadre(), "subdocumento iva padre");
		
		//checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva spesa (non entrambi)"));
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));
		
		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva registro iva"));
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoIvaSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaSpesaDad.setEnte(ente);
		super.init();
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaNotaCreditoIvaSpesaResponse executeService(AggiornaNotaCreditoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		//caricaDettaglioDocumentoESubdocumentoSpesaAssociato();
		caricaRegistroIva();
//		caricaPeriodi();
		
		subdocumentoIvaSpesaDad.aggiornaAnagraficaSubdocumentoIvaSpesa(subdocIva);		
		res.setSubdocumentoIvaSpesa(subdocIva);
				

	}


}
