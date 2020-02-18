/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceTestataDocumentoEntrataService extends InserisceDocumentoEntrataService {
		
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName = "checkServiceParam";
		
		doc = req.getDocumentoEntrata();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
//		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
				
//		checkNotNull(doc.getCodiceBollo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice bollo documento"));
//		checkCondition(doc.getCodiceBollo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid codice bollo documento"));
		
		try{
			checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
			checkCondition(doc.getListaSubdocumenti().size()==1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		} catch (ServiceParamError spe) { // Se il subdocumento non viene passato 
				// ne viene inizializzato uno vuoto
			log.info(methodName, "Subdocumento passato come parametro non presente o ignorato (deve esserci un solo subdocumento)! Verrà creato un subdocumento che copre l'intero importo del documento.");
			SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
			subdoc.setFlagRilevanteIVA(Boolean.TRUE);
			List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
			listaSubdocumenti.add(subdoc);
			doc.setListaSubdocumenti(listaSubdocumenti);
		}
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		

		// SIAC-3191
		checkNumero();
		checkImportoPositivo();
//		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
	}
	
	@Override
	protected void gestisciFlagRegolarizzazione() {	
		//Sovrascrivo il check con il vuoto! per saltare i controlli
		log.info("gestisciFlagRegolarizzazione", "Controllo Saltato!");
	}
	
	
	
	protected void checkDocumentoGiaEsistente() {
		DocumentoEntrata d = new DocumentoEntrata();
		d.setAnno(doc.getAnno());
		d.setNumero(doc.getNumero());
		d.setTipoDocumento(doc.getTipoDocumento());
		d.setStatoOperativoDocumento(null);
		d.setSoggetto(doc.getSoggetto());
		d.setEnte(doc.getEnte());
		
		DocumentoEntrata documentoEntrata = documentoEntrataDad.ricercaPuntualeDocumentoIvaEntrata(d, StatoOperativoDocumento.ANNULLATO);
		
		if(documentoEntrata != null) {
			res.setDocumentoEntrata(documentoEntrata);
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento Documento Entrata", documentoEntrata.getDescAnnoNumeroTipoDocSoggettoStato()), Esito.FALLIMENTO);
		}
		
	}
	
	
	
}
