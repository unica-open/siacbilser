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

import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class InserisceSubdocumentoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceSubdocumentoIvaEntrataService extends CrudSubdocumentoIvaEntrataBaseService<InserisceSubdocumentoIvaEntrata, InserisceSubdocumentoIvaEntrataResponse>{
	
	/** The subdocumento iva entrata dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaEntrata();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
		
		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva entrata"), false);
		
//		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
//		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		subdocIva.setEnte(ente);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva entrata"), false);
		
		checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva entrata (non entrambi)"));
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));
		
		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		checkCondition(subdocIva.getRegistroIva().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro iva"), false);
		checkNotNull(subdocIva.getTipoRegistrazioneIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione iva"));
		checkCondition(subdocIva.getTipoRegistrazioneIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo registrazione iva"), false);
		//checkNotNull(subdocIva.getRegistroIva().getTipoRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registro iva registro iva"));
		
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaEntrataDad.setEnte(ente);
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(ente);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceSubdocumentoIvaEntrataResponse executeService(InserisceSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {		
		caricaDettaglioDocumentoESubdocumentoEntrataAssociato();
		checkSubdocumentoIvaGiaAssociato();
		
		caricaRegistroIva();
		checkRegistroIva();
//		caricaPeriodi();
		
		checkMovimentoIvaDocumento();		
		

		if(subdocIva.getDataProtocolloProvvisorio()!=null){
			checkDataRegistrazioneProtocolloProvvisorio();
		} else if(subdocIva.getDataProtocolloDefinitivo()!=null){
			checkDataRegistrazioneProtocolloDefinitivo();
		}
		
		//in inserimento l'anno del subdocumento iva e' sempre l'anno del bilancio in cui sto lavorando
		Integer numeroSubdocumentoIva = subdocumentoIvaEntrataDad.staccaNumeroSubdocumento(subdocIva.getAnnoEsercizio());
		subdocIva.setProgressivoIVA(numeroSubdocumentoIva);
		subdocIva.setDataRegistrazione(new Date());
		
		impostaStatoSubdocumentoIva();
		if(!req.isSenzaProtocollo()){
			impostaNumeroProtocollo();
		}
		impostaFlagRegistrazioneIva();
		
		subdocumentoIvaEntrataDad.inserisciAnagraficaSubdocumentoIvaEntrata(subdocIva);		
		
		
		/*
		 * SIAC-6677
		 * IVA VENDITE IMMEDIATA, NUMERAZIONE AUTOMATICA DA IVA 
		 * numeroFattura = numeroFattura||” – “||numeroProtocolloIVA||”/”||CodiceRegistroIVA
		 */
		if(subdocIva!= null && subdocIva.getRegistroIva()!= null && subdocIva.getRegistroIva().getTipoRegistroIva()!= null && subdocIva.getRegistroIva().getTipoRegistroIva().getCodice()!= null &&
				TipoRegistroIva.VENDITE_IVA_IMMEDIATA.getCodice().equals(subdocIva.getRegistroIva().getTipoRegistroIva().getCodice()) &&
				documentoEntrata.getTipoDocumento().getFlagNumerazioneAutomaticaDaIVA()!= null && documentoEntrata.getTipoDocumento().getFlagNumerazioneAutomaticaDaIVA()){
			
			StringBuilder numFattura = new StringBuilder();
			numFattura.append(documentoEntrata.getNumero());
			numFattura.append("-");
			numFattura.append(subdocIva.getNumeroProtocolloDefinitivo() != null ? subdocIva.getNumeroProtocolloDefinitivo() : " ");
			numFattura.append("/");
			numFattura.append((subdocIva.getRegistroIva()!= null && subdocIva.getRegistroIva().getCodice()!= null) ? subdocIva.getRegistroIva().getCodice() : " ");
			documentoEntrataDad.aggiornaNumeroDocumentoEntrata(documentoEntrata.getUid(), numFattura.toString());
		}
		
		res.setSubdocumentoIvaEntrata(subdocIva);
		aggiornaNumeroRegistrazioneIvaSubdocumenti();
		
		//aggiorno lo stato del documento entrata collegato che sia di famiglia Iva_entrata o meno. In questo modo scatta, se necessario, l'attivazione nel Registro GEN.
//		documentoEntrataDad.aggiornaStatoDocumentoEntrata(documentoEntrata.getUid(), StatoOperativoDocumento.VALIDO);
		AggiornaStatoDocumentoDiEntrata reqASDDS = new AggiornaStatoDocumentoDiEntrata();
		reqASDDS.setRichiedente(req.getRichiedente());
		reqASDDS.setDocumentoEntrata(documentoEntrata);
		reqASDDS.setBilancio(req.getBilancio());
		serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqASDDS);
		
	}
	
	
	/**
	 * Controlla se esiste un suboducumentoIva gia' associato per il documento/subdocumento. 
	 * 
	 */
	private void checkSubdocumentoIvaGiaAssociato() {
		if(isLegatoAlDocumento()){
			if(!documentoEntrata.getListaSubdocumentoIva().isEmpty()){ //Esiste gia' un subdocumentoIva legato al Documento.
				
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il documento "+documentoEntrata.getDescAnnoNumeroTipoDoc() 
					+" e' gia' associato al subdocumento IVA con progressivo "+documentoEntrata.getListaSubdocumentoIva().get(0).getProgressivoIVA()));
			}
			
			//Controllo se esiste un subdocumento con il subdocumentoIVA. In tal caso blocco.
			for(SubdocumentoEntrata subdoc : documentoEntrata.getListaSubdocumenti()){
				if(subdoc.getSubdocumentoIva()!=null){
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile inserire il subdocumento IVA sull'intero documento "+documentoEntrata.getDescAnnoNumeroTipoDoc()
					+" in quanto ha gia' almeno una quota con associato il subdocumento IVA. (Vedi quota "+subdoc.getNumero()+") "));
				}
			}
			
		} else /*if(isLegatoAlSubdocumento())*/ {
			
			if(subdocumentoEntrata.getSubdocumentoIva()!=null){ //Esiste gia' un subdocumentoIva legato alla Quota.
				
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La quota "+subdocumentoEntrata.getNumero()+" del documento "+documentoEntrata.getDescAnnoNumeroTipoDoc() 
					+" e' gia' associata al subdocumento IVA con progressivo "+subdocumentoEntrata.getSubdocumentoIva().getProgressivoIVA()));
			}
			
			if(!documentoEntrata.getListaSubdocumentoIva().isEmpty()){ //Esiste gia' un subdocumentoIva legato al Documento.
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile inserire il subdocumento IVA per la quota "+subdocumentoEntrata.getNumero()+" del documento "+documentoEntrata.getDescAnnoNumeroTipoDoc() 
				+" in quanto e' presente un subdocumento IVA su intero documento con progressivo "+documentoEntrata.getListaSubdocumentoIva().get(0).getProgressivoIVA()));
			}
		}
		
	}
	
	private void checkRegistroIva() {
		if(!Boolean.FALSE.equals(subdocIva.getRegistroIva().getFlagBloccato())) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Inserimento subdocumento iva", 
				"Dati Iva non gestibili perche' il registro selezionato e' stato bloccato."));
		}
	}
	
}
