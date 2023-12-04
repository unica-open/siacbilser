/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaEntrataResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceNotaCreditoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceQuotaIvaDifferitaEntrataService extends CrudSubdocumentoIvaEntrataBaseService<InserisceQuotaIvaDifferitaEntrata, InserisceQuotaIvaDifferitaEntrataResponse>{
	
	/** The subdocumento iva entrata dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
		

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaEntrata();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
		
		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva entrata"), false);
		
		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva entrata"), false);
		
		checkNotNull(subdocIva.getSubdocumentoIvaPadre(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva padre"), false);
		checkCondition(subdocIva.getSubdocumentoIvaPadre().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva padre"), false);
		
		//checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva entrata (non entrambi)"));
		
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));
		
		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva registro iva"));
		checkNotNull(subdocIva.getTipoRegistrazioneIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione iva"));
		checkCondition(subdocIva.getTipoRegistrazioneIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo registrazione iva"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoIvaEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaEntrataDad.setEnte(subdocIva.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceQuotaIvaDifferitaEntrataResponse executeService(InserisceQuotaIvaDifferitaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {		
//		caricaDettaglioDocumentoESubdocumentoEntrataAssociato();		
		
//		checkSubdocumentoIvaGiaEsistente();
//		checkMovimentoIvaDocumento();		
		
//		checkDataRegistrazioneProtocolloProvvisorio();
//		checkDataRegistrazioneProtocolloDefinitivo();
		
		Integer numeroSubdocumentoIva = subdocumentoIvaEntrataDad.staccaNumeroSubdocumento(subdocIva.getAnnoEsercizio());
		subdocIva.setProgressivoIVA(numeroSubdocumentoIva);
		subdocIva.setDataRegistrazione(new Date());
		
		impostaStatoSubdocumentoIva();
		impostaNumeroProtocollo();
		
		subdocumentoIvaEntrataDad.inserisciAnagraficaSubdocumentoIvaEntrata(subdocIva);		
		res.setSubdocumentoIvaEntrata(subdocIva);

//		aggiornaNumeroRegistrazioneIvaSubdocumenti();
	}
	

}
