/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidaStatoOperativoPreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidaStatoOperativoPreDocumentoEntrataService extends CrudPreDocumentoDiEntrataBaseService<ValidaStatoOperativoPreDocumentoEntrata, ValidaStatoOperativoPreDocumentoEntrataResponse> {
	
	/** The nuovo stato. */
	StatoOperativoPreDocumento nuovoStato; 
	
	/** The vecchio stato. */
	StatoOperativoPreDocumento vecchioStato;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoEntrata();
		nuovoStato = req.getStatoOperativoPreDocumento();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		checkNotNull(nuovoStato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo da validare"));
		
		vecchioStato = preDoc.getStatoOperativoPreDocumento();
		checkNotNull(vecchioStato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo predocumento"));
		checkCondition(preDoc.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid preDoc"));
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public ValidaStatoOperativoPreDocumentoEntrataResponse executeService(ValidaStatoOperativoPreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if (StatoOperativoPreDocumento.ANNULLATO.equals(vecchioStato) || StatoOperativoPreDocumento.DEFINITO.equals(vecchioStato)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("la predisposizione di incasso",vecchioStato.getDescrizione()));
		} 
		
		if(nuovoStato.equals(StatoOperativoPreDocumento.COMPLETO)){
			preDoc = preDocumentoEntrataDad.findPreDocumentoById(preDoc.getUid());
			log.logXmlTypeObject(preDoc, "predoc trovato");
			validaCompleto();
		}
		
		preDoc.setStatoOperativoPreDocumento(nuovoStato);
		res.setPreDocumentoEntrata(preDoc);
		
	}


	/**
	 * Valida completo.
	 */
	private void validaCompleto() {
		if ( preDoc.getSoggetto() == null || preDoc.getSoggetto().getUid() == 0 ||
				( 	(preDoc.getCapitoloEntrataGestione() == null || preDoc.getCapitoloEntrataGestione().getUid() == 0) && 
					(preDoc.getAccertamento()==null || preDoc.getAccertamento().getUid() == 0) && 
				    (preDoc.getSubAccertamento()==null || preDoc.getSubAccertamento().getUid() == 0) 
				 ) 
			) {
			throw new BusinessException(ErroreFin.STATO_OPERATIVO_PREDOCUMENTO_NON_VALIDO.getErrore("completo","Devono essere indicate le imputazioni contabili"));
		} 
	}

}
