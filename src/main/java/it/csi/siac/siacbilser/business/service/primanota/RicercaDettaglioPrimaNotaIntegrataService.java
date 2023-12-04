/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Ricerca di dettaglio di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioPrimaNotaIntegrataService extends CheckedAccountBaseService<RicercaDettaglioPrimaNotaIntegrata, RicercaDettaglioPrimaNotaIntegrataResponse> {

	
	@Autowired
	private PrimaNotaDad primaNotaDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition((req.getPrimaNota() != null && req.getPrimaNota().getUid() != 0 ) ||
				(req.getRegistrazioneMovFin() != null && req.getRegistrazioneMovFin().getUid() != 0 ) ||
				(req.getDocumento() != null && req.getDocumento().getUid() != 0 ), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota, registrazione o documento"));
	}
	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioPrimaNotaIntegrataResponse executeService(RicercaDettaglioPrimaNotaIntegrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		Ambito ambito = Ambito.AMBITO_FIN;
		if(req.getRegistrazioneMovFin() != null && req.getRegistrazioneMovFin().getAmbito() != null) {
			ambito = req.getRegistrazioneMovFin().getAmbito();
		}
		PrimaNota primaNota = primaNotaDad.ricercaDettaglioPrimaNotaIntegrata(
				req.getPrimaNota(), 
				req.getRegistrazioneMovFin(), 
				req.getDocumento(), 
				ambito, 
				req.getEvento(), 
				(req.getPrimaNota()!=null && req.getPrimaNota().getUid()!=0)?
					//sto andando direttamente con l'uid della prima nota: prendo tutti gli stati.
					EnumSet.noneOf(StatoOperativoPrimaNota.class) 				
					: 
					//esclude le prime note annullate.
					EnumSet.of(StatoOperativoPrimaNota.ANNULLATO) //EnumSet.complementOf()
				
				);
		
		res.setPrimaNota(primaNota);
		
		if(primaNota == null) {
			throwNewBusinessExceptionEntitaInesistente();
		}
				
		caricaDatiAggiuntiviDocumentoSubdocumento(primaNota);
		
	}



	private void throwNewBusinessExceptionEntitaInesistente() {
		String tipoElemento = null;
		int uid = 0;
		if(req.getPrimaNota() != null && req.getPrimaNota().getUid() != 0) {
			tipoElemento = "";
			uid = req.getPrimaNota().getUid();
		} else if(req.getRegistrazioneMovFin() != null && req.getRegistrazioneMovFin().getUid() != 0) {
			tipoElemento = "collegata a registrazione mov fin ";
			uid = req.getRegistrazioneMovFin().getUid();
		} else if(req.getDocumento() != null && req.getDocumento().getUid() != 0) {
			tipoElemento = "collegata a documento ";
			uid = req.getDocumento().getUid();
		}
		
		throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Prima nota", tipoElemento + "con uid " + uid));
	}


	/**
	 * Nel caso il tipo collegamento sia Documento o Subdocumento carica dati aggiuntivi.
	 * 
	 * @param registrazioneMovFin0
	 * @param tipoCollegamento
	 */
	private void caricaDatiAggiuntiviDocumentoSubdocumento(PrimaNota primaNota) {
		RegistrazioneMovFin registrazioneMovFin0 = primaNota.getListaMovimentiEP().get(0).getRegistrazioneMovFin();
		TipoCollegamento tipoCollegamento = registrazioneMovFin0.getEvento().getTipoCollegamento();
		
		if(TipoCollegamento.DOCUMENTO_SPESA.equals(tipoCollegamento)){
			Integer docId = registrazioneMovFin0.getMovimento().getUid();
			DocumentoSpesa docSpesa = documentoSpesaDad.findDocumentoSpesaByIdMedium(docId);
			res.setDocumentoSpesa(docSpesa);
		} else if(TipoCollegamento.SUBDOCUMENTO_SPESA.equals(tipoCollegamento)){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)registrazioneMovFin0.getMovimento();
			Integer docId = subdoc.getDocumento().getUid();
			DocumentoSpesa docSpesa = documentoSpesaDad.findDocumentoSpesaByIdMedium(docId);
			res.setDocumentoSpesa(docSpesa);
		} else if(TipoCollegamento.DOCUMENTO_ENTRATA.equals(tipoCollegamento)){
			Integer docId = registrazioneMovFin0.getMovimento().getUid();
			DocumentoEntrata docEntrata = documentoEntrataDad.findDocumentoEntrataByIdMedium(docId);
			res.setDocumentoEntrata(docEntrata);
		} else if(TipoCollegamento.SUBDOCUMENTO_ENTRATA.equals(tipoCollegamento)){
			SubdocumentoEntrata subdoc = (SubdocumentoEntrata)registrazioneMovFin0.getMovimento();
			Integer docId = subdoc.getDocumento().getUid();
			DocumentoEntrata docEntrata = documentoEntrataDad.findDocumentoEntrataByIdMedium(docId);
			res.setDocumentoEntrata(docEntrata);
		}
	}
	
	
}
