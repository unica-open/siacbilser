/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class EliminaQuotaDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaQuotaDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<EliminaQuotaDocumentoSpesa, EliminaQuotaDocumentoSpesaResponse> {
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		subdoc = req.getSubdocumentoSpesa();
		bilancio = req.getBilancio();
		
		checkEntita(bilancio, "bilancio");
		checkEntita(subdoc, "subdocumento spesa");
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public EliminaQuotaDocumentoSpesaResponse executeService(EliminaQuotaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {	
		caricaDettaglioSubdocumento();
		checkQuotaEliminabile();
		subdocumentoSpesaDad.eliminaSubdocumentoSpesa(subdoc);	
		
		if(req.isAggiornaStatoDocumento()){
			DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid()).getDocumentoSpesa();
			subdoc.getDocumento().setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			subdoc.getDocumento().setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setSubdocumentoSpesa(subdoc);

	}
	
	private void caricaDettaglioSubdocumento() {
		subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdoc.getUid());
		if(subdoc==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("elimina quota", "Quota con uid: "+req.getSubdocumentoSpesa().getUid()));
		}
	}

	private void checkQuotaEliminabile() {
		
		if(req.isSaltaCheckQuotaEliminabile()){
			return;
		}
		
		//non deve avere numero regitrazione Iva valorizzato
		if(subdoc.getNumeroRegistrazioneIVA()!=null && !subdoc.getNumeroRegistrazioneIVA().isEmpty()){
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("Presente numero registrazione IVA"), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		//non deve essere collegata ad un provvedimento
		if(subdoc.getAttoAmministrativo() != null && subdoc.getAttoAmministrativo().getUid() != 0) {
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("Presente atto amministrativo."), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		//non deve essere l'unica quota legata al documento
		List<SubdocumentoSpesa> quote = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(subdoc.getDocumento().getUid());
		if(quote.size()==1){
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("E' l'unica quota del documento."), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		
		// non e' eliminabile se l'indicatore 'pagatoinCEC' = TRUE
		if(Boolean.TRUE.equals(subdoc.getPagatoInCEC())) {
			throw new BusinessException(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getErrore("Quota già pagata in Cassa Economale"), Esito.FALLIMENTO, req.isRoolbackOnly());
		}
		
	}	

}
