/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceControregistrazioneService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceControregistrazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceControregistrazioneResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Implementazione del servizio InserisceSubdocumentoIvaSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceSubdocumentoIvaSpesaService extends CrudSubdocumentoIvaSpesaBaseService<InserisceSubdocumentoIvaSpesa, InserisceSubdocumentoIvaSpesaResponse>{
	
	
	private InserisceControregistrazioneService inserisceControregistrazioneService;
	
	@Autowired
	private ApplicationContext appCtx;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocIva = req.getSubdocumentoIvaSpesa();
				
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva spesa"));
		
		checkNotNull(subdocIva.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio subdocumento iva spesa"), false);
		
//		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
//		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		subdocIva.setEnte(ente);
		
		checkNotNull(subdocIva.getStatoSubdocumentoIva(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato subdocumento iva spesa"), false);
		
		checkCondition(isLegatoAlDocumento() ^ isLegatoAlSubdocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento o subdocumento iva spesa (non entrambi)"));
		
		checkCondition(!subdocIva.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquote subdocumenti iva"));
		
		/*for(AliquotaSubdocumentoIva aliquota : subdocIva.getListaAliquotaSubdocumentoIva()){
			checkNotNull(aliquota, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquota"));
			
			checkNotNull(aliquota.getImponibile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("imponibile aliquota"),false);
			checkNotNull(aliquota.getImposta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("imposta aliquota"), false);
			checkNotNull(aliquota.getTotale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("totale aliquota"), false);
		}*/
		
		checkNotNull(subdocIva.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		checkCondition(subdocIva.getRegistroIva().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro iva"), false);
		checkNotNull(subdocIva.getTipoRegistrazioneIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione iva"));
		checkCondition(subdocIva.getTipoRegistrazioneIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo registrazione iva"), false);
		
		if(Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
			checkNotNull(subdocIva.getSubdocumentoIvaEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva entrata"));
			checkNotNull(subdocIva.getSubdocumentoIvaEntrata().getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva subdocumento iva entrata"));
		}
		
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
			
		inserisceControregistrazioneService = appCtx.getBean(Utility.toDefaultBeanName(InserisceControregistrazioneService.class), InserisceControregistrazioneService.class);
	}

	
	@Transactional
	@Override
	public InserisceSubdocumentoIvaSpesaResponse executeService(InserisceSubdocumentoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {		
		caricaDettaglioDocumentoESubdocumentoSpesaAssociato();
		checkSubdocumentoIvaGiaAssociato();
		
		caricaRegistroIva();
		checkRegistroIva();
//		caricaPeriodi();
		
		if(!Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
			checkMovimentoIvaDocumento();
		}
		
		if(subdocIva.getDataProtocolloProvvisorio() != null){
			checkDataRegistrazioneProtocolloProvvisorio();
		} else if(subdocIva.getDataProtocolloDefinitivo() != null){
			checkDataRegistrazioneProtocolloDefinitivo();
		}	
		
		//in inserimento l'anno del subdocumento iva e' sempre l'anno del bilancio in cui sto lavorando
		Integer numeroSubdocumentoIva = subdocumentoIvaSpesaDad.staccaNumeroSubdocumento(subdocIva.getAnnoEsercizio());		
		subdocIva.setProgressivoIVA(numeroSubdocumentoIva);
		subdocIva.setDataRegistrazione(new Date());
		
		impostaStatoSubdocumentoIva();
		if(!req.isSenzaProtocollo()){
			impostaNumeroProtocollo();
		}
		impostaFlagRegistrazioneIva();
		
		subdocumentoIvaSpesaDad.inserisciAnagraficaSubdocumentoIvaSpesa(subdocIva);
		
		if(Boolean.TRUE.equals(subdocIva.getFlagIntracomunitario())){
			inserisciControregistrazione();
		}
		
		res.setSubdocumentoIvaSpesa(subdocIva);
		
		aggiornaNumeroRegistrazioneIvaSubdocumenti();	
		
		//aggiorno lo stato del documento Spesa collegato che sia di famiglia Iva_spesa o meno. In questo modo scatta, se necessario, l'attivazione nel Registro GEN.
		//documentoSpesaDad.aggiornaStatoDocumentoSpesa(documentoSpesa.getUid(), StatoOperativoDocumento.VALIDO);
		AggiornaStatoDocumentoDiSpesa reqASDDS = new AggiornaStatoDocumentoDiSpesa();
		reqASDDS.setRichiedente(req.getRichiedente());
		reqASDDS.setDocumentoSpesa(documentoSpesa);
		reqASDDS.setBilancio(req.getBilancio());
		serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqASDDS);
		
	}

	

	/**
	 * Controlla se esiste un suboducumentoIva gia' associato per il documento/subdocumento. 
	 * 
	 */
	private void checkSubdocumentoIvaGiaAssociato() {
		if(isLegatoAlDocumento()){
			if(!documentoSpesa.getListaSubdocumentoIva().isEmpty()){ //Esiste gia' un subdocumentoIva legato al Documento.
				
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il documento "+documentoSpesa.getDescAnnoNumeroTipoDoc() 
					+" e' gia' associato al subdocumento IVA con progressivo "+documentoSpesa.getListaSubdocumentoIva().get(0).getProgressivoIVA()));
			}
			
			//Controllo se esiste un subdocumento con il subdocumentoIVA. In tal caso blocco.
			for(SubdocumentoSpesa subdoc : documentoSpesa.getListaSubdocumenti()){
				if(subdoc.getSubdocumentoIva()!=null){
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile inserire il subdocumento IVA sull'intero documento "+documentoSpesa.getDescAnnoNumeroTipoDoc()
					+" in quanto ha gia' almeno una quota con associato il subdocumento IVA. (Vedi quota "+subdoc.getNumero()+") "));
				}
			}
			
		} else { // if(isLegatoAlSubdocumento()){
			
			if(subdocumentoSpesa.getSubdocumentoIva()!=null){ //Esiste gia' un subdocumentoIva legato alla Quota.
				
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La quota "+subdocumentoSpesa.getNumero()+" del documento "+documentoSpesa.getDescAnnoNumeroTipoDoc() 
					+" e' gia' associata al subdocumento IVA con progressivo "+subdocumentoSpesa.getSubdocumentoIva().getProgressivoIVA()));
			}
			
			if(!documentoSpesa.getListaSubdocumentoIva().isEmpty()){ //Esiste gia' un subdocumentoIva legato al Documento.
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile inserire il subdocumento IVA per la quota "+subdocumentoSpesa.getNumero()+" del documento "+documentoSpesa.getDescAnnoNumeroTipoDoc() 
				+" in quanto e' presente un subdocumento IVA su intero documento con progressivo "+documentoSpesa.getListaSubdocumentoIva().get(0).getProgressivoIVA()));
			}
		}
		
	}


	/**
	 *  Inserisce una controregistrazione (subDocumentoIva di entrata).
	 */
	private void inserisciControregistrazione() {
		
		SubdocumentoIvaEntrata controregistrazione = popolaControregistrazione();
		
		InserisceControregistrazione reqIC = new InserisceControregistrazione();
		reqIC.setBilancio(req.getBilancio());
		reqIC.setRichiedente(req.getRichiedente());
		reqIC.setSubdocumentoIvaEntrata(controregistrazione);
		
		InserisceControregistrazioneResponse resIC = executeExternalServiceSuccess(inserisceControregistrazioneService,reqIC);
		
		subdocIva.setSubdocumentoIvaEntrata(resIC.getSubdocumentoIvaEntrata());
	}
	
	private void checkRegistroIva() {
		if(!Boolean.FALSE.equals(subdocIva.getRegistroIva().getFlagBloccato())) {
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Inserimento subdocumento iva",
				"Dati Iva non gestibili perche' il registro selezionato e' stato bloccato."));
		}
	}

}
