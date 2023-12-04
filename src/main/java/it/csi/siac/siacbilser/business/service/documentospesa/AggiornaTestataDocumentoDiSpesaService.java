/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Aggiornamento di un documento di spesa .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTestataDocumentoDiSpesaService extends AggiornaDocumentoDiSpesaService{
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
//		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		//checkNotNull(doc.getFlagBeneficiarioMultiplo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag beneficiario multiplo"));
		if(doc.getFlagBeneficiarioMultiplo()==null) {
			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		}
		
		checkNotNull(doc.getStatoOperativoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		// SIAC-3191
		checkNumero();
		checkImportoPositvo();
//		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
//		checkDataSospensione();
//		checkDataRiattivazione();
		
	}
	
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		
		checkDocumentoAggiornabile();
		
		checkSoggetto();
		checkAnno();
		
		documentoSpesaDad.aggiornaAnagraficaDocumentoSpesa(doc);
		aggiornaImportoQuote();
		
		DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc).getDocumentoSpesa();
		doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());		
		
		res.setDocumentoSpesa(doc);
	}

	/**
	 * Aggiornamento dell'importo delle quote del documento 
	 */
	private void aggiornaImportoQuote() {
		final String methodName = "aggiornaImportoQuote";
		List<SubdocumentoSpesa> subdocs = subdocumentoSpesaDad.findSubdocumentiSpesaModelDetailByIdDocumento(doc.getUid());
		for(SubdocumentoSpesa s : subdocs) {
			if(s.getImporto().compareTo(doc.getImporto()) != 0) {
				log.debug(methodName, "Necessario aggiornare l'importo della quota [" + s.getUid() + "]: importo originale " + s.getImporto() + ", importo nuovo " + doc.getImporto());
				s.setImporto(doc.getImporto());
				subdocumentoSpesaDad.aggiornaImportoSubdocumentoSpesa(s);
			}
		}
	}

}
