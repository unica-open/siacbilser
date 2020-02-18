/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class EliminaQuotaDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaQuotaDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<EliminaQuotaDocumentoEntrata, EliminaQuotaDocumentoEntrataResponse> {
	
//	/** The subdocumento entrata dad. */
//	@Autowired
//	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
//	/** The subdoc. */
//	private SubdocumentoEntrata subdoc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		subdoc = req.getSubdocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkEntita(subdoc, "subdocumento entrata");
		checkEntita(bilancio, "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public EliminaQuotaDocumentoEntrataResponse executeService(EliminaQuotaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDettaglioSubdocumento();
		checkQuotaEliminabile();
		subdocumentoEntrataDad.eliminaSubdocumentoEntrata(subdoc);	
		
		if(req.isAggiornaStatoDocumento()) {
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid());
			subdoc.getDocumento().setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			subdoc.getDocumento().setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setSubdocumentoEntrata(subdoc);

	}

	private void caricaDettaglioSubdocumento() {
		subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdoc.getUid());
		if(subdoc==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("elimina quota", "Quota con uid: "+ req.getSubdocumentoEntrata().getUid()));
		}
	}
	
	
	private void checkQuotaEliminabile() {
		
		//non deve avere numero regitrazione Iva valorizzato
		if(subdoc.getNumeroRegistrazioneIVA() != null && !subdoc.getNumeroRegistrazioneIVA().isEmpty()){
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("Presente numero registrazione IVA"), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		//non deve essere collegata ad un provvedimento
		if(subdoc.getAttoAmministrativo() != null && subdoc.getAttoAmministrativo().getUid() != 0 ){
				throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("Presente atto amministrativo."), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		//non deve essere l'unica quota legata al documento
		List<SubdocumentoEntrata> quote = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(subdoc.getDocumento().getUid());
		if(quote.size() == 1){
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("E' l'unica quota del documento."), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		
		
	}	

}
