/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

// TODO: Auto-generated Javadoc
/**
 * Aggiornamento di un documento di entrta .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDocumentoDiEntrataService extends CrudDocumentoDiEntrataBaseService<AggiornaDocumentoDiEntrata, AggiornaDocumentoDiEntrataResponse> {
	

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkEntita(bilancio, "bilancio");
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
//		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
		
		checkNotNull(doc.getCodiceBollo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice bollo documento"));
		checkCondition(doc.getCodiceBollo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid codice bollo documento"));
		
		//checkNotNull(doc.getFlagBeneficiarioMultiplo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag beneficiario multiplo"));
//		if(doc.getFlagBeneficiarioMultiplo()==null) {
//			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
//		}
		
		checkNotNull(doc.getStatoOperativoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
//		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));
//		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		// SIAC-3191
		checkNumero();
		checkImporto();
		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaDocumentoDiEntrataResponse executeService(AggiornaDocumentoDiEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkDocumentoAggiornabile();
		
		checkSoggetto();
		checkAnno(); // TODO: controllare se possa essere spostato sopra
//		checkDataSospensione();
//		checkDataRiattivazione();
		
		
		documentoEntrataDad.aggiornaAnagraficaDocumentoEntrata(doc);
		
		if(req.isAggiornaStatoDocumento()) {
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc);
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setDocumentoEntrata(doc);
	}

	protected void checkDocumentoAggiornabile() {
		if(StatoOperativoDocumento.EMESSO.equals(doc.getStatoOperativoDocumento()) || StatoOperativoDocumento.ANNULLATO.equals(doc.getStatoOperativoDocumento()) ){
			throw new BusinessException(ErroreFin.DOCUMENTO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
		}
		
	}


}
