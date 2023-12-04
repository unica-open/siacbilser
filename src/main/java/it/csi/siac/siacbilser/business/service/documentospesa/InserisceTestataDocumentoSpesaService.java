/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica del Documento di Spesa .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceTestataDocumentoSpesaService extends InserisceDocumentoSpesaService {
		
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName = "checkServiceParam";
		
		doc = req.getDocumentoSpesa();
		
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
		
		checkEntita(req.getBilancio(), "bilancio");
		bilancio = req.getBilancio();
		
		if(doc.getFlagBeneficiarioMultiplo()==null) { //NON obbligatorio! Default a FALSE
			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		}
		
		//checkNotNull(doc.getStatoOperativoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		try {
			checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
			checkCondition(doc.getListaSubdocumenti().size() == 1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		} catch (ServiceParamError spe) { // Se il subdocumento non viene passato 
											// ne viene inizializzato uno vuoto
			log.info(methodName, "Subdocumento passato come parametro non presente o ignorato (deve esserci un solo subdocumento)! Verrà creato un subdocumento che copre l'intero importo del documento.");
			SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
			subdoc.setFlagRilevanteIVA(Boolean.TRUE);
			List<SubdocumentoSpesa> listaSubdocumenti = new ArrayList<SubdocumentoSpesa>();
			listaSubdocumenti.add(subdoc);
			doc.setListaSubdocumenti(listaSubdocumenti);
		}
		
		
		// SIAC-3191
		checkNumero();
		checkImportoPositvo();
		checkDataEmissione();
		checkDataScadenza();
		
	}
	
	@Override
	protected void gestisciFlagRegolarizzazione() {	
		//Sovrascrivo il check con il vuoto! per saltare i controlli
		log.info("gestisciFlagRegolarizzazione", "Controllo Saltato!");
	}
	
	
	protected void checkDocumentoGiaEsistente() {
		DocumentoSpesa d = new DocumentoSpesa();
		d.setAnno(doc.getAnno());
		d.setNumero(doc.getNumero());
		d.setTipoDocumento(doc.getTipoDocumento());
		d.setStatoOperativoDocumento(null);
		d.setSoggetto(doc.getSoggetto());
		d.setEnte(doc.getEnte());
		
		DocumentoSpesa documentoSpesa = documentoSpesaDad.ricercaPuntualeDocumentoIvaSpesa(d, StatoOperativoDocumento.ANNULLATO);
		
		if(documentoSpesa != null) {
			res.setDocumentoSpesa(documentoSpesa);
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento Documento Spesa", documentoSpesa.getDescAnnoNumeroTipoDocSoggettoStato()), Esito.FALLIMENTO);
		}
		
	}
	
	
}
