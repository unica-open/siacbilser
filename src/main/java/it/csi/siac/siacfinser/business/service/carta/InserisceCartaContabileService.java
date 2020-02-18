/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabileResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneCartaContabileDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceCartaContabileService extends AbstractGestioneCartaContabileService<InserisceCartaContabile, InserisceCartaContabileResponse> {

	@Autowired
	CartaContabileDad cartaContabileDad; 

	@Autowired
	CommonDad commonDad;
	
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void init() {
		final String methodName="InserisceCartaContabileService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	@Override
	@Transactional
	public InserisceCartaContabileResponse executeService(InserisceCartaContabile serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		String methodName = "InserisceCartaContabileService - execute()";
		log.debug(methodName, " - Begin");
		
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		CartaContabile cartaContabile=req.getCartaContabile();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//INIZIO - Estrazione documenti dei subdocumentispesa
		if (cartaContabile!=null && cartaContabile.getListaPreDocumentiCarta()!=null && cartaContabile.getListaPreDocumentiCarta().size()>0) {
			for (PreDocumentoCarta preDocumentoCarta : cartaContabile.getListaPreDocumentiCarta()) {
				if(preDocumentoCarta.getListaSubDocumentiSpesaCollegati()!=null && preDocumentoCarta.getListaSubDocumentiSpesaCollegati().size()>0){
					for(SubdocumentoSpesa subdocumentoSpesaInput : preDocumentoCarta.getListaSubDocumentiSpesaCollegati()){
						if (subdocumentoSpesaInput.getDocumento()!=null &&
								subdocumentoSpesaInput.getDocumento().getAnno()!=null &&
								subdocumentoSpesaInput.getDocumento().getNumero()!=null &&
								subdocumentoSpesaInput.getDocumento().getTipoDocumento()!=null &&
								subdocumentoSpesaInput.getDocumento().getSoggetto()!=null) {
								
							RicercaDettaglioDocumentoSpesa req = new RicercaDettaglioDocumentoSpesa();
							req.setRichiedente(richiedente);
							req.setDocumentoSpesa(subdocumentoSpesaInput.getDocumento());
							
							RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
							if(response.isFallimento()){
								res.setErrori(response.getErrori());
								res.setEsito(Esito.FALLIMENTO);
								res.setCartaContabile(null);
								return;
							}
							
							subdocumentoSpesaInput.setDocumento(response.getDocumento());
						} else {
							//ERRORE no documento coll a subdocumento spesa
						}
					
					}
					
				}
			}
		}
		//FINE - Estrazione documenti dei subdocumentispesa

		EsitoGestioneCartaContabileDto esitoInserisciCartaContabile=cartaContabileDad.inserisciCartaContabile(richiedente, Constanti.AMBITO_FIN,
				ente, bilancio, cartaContabile, datiOperazione);
		
		if (esitoInserisciCartaContabile.getListaErrori()!=null && esitoInserisciCartaContabile.getListaErrori().size()>0) {
			res.setErrori(esitoInserisciCartaContabile.getListaErrori());
			res.setCartaContabile(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}

		if(esitoInserisciCartaContabile.getCartaContabile()!=null){
			res.setCartaContabile(esitoInserisciCartaContabile.getCartaContabile());
			res.setEsito(Esito.SUCCESSO);
		} else {
			res.setCartaContabile(null);
			res.setEsito(Esito.FALLIMENTO);
		}
			
			
		
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="InserisceCartaContabileService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Richiedente richiedente = req.getRichiedente();
		CartaContabile cartaContabile=req.getCartaContabile();

		String elencoParamentriNonInizializzati = "";
		
		if(richiedente==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", RICHIEDENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "RICHIEDENTE";
			}	
		}
		
		if(ente==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
			}	
		}

		if(bilancio==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BILANCIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BILANCIO";
			}	
		} else {
			if(bilancio.getAnno() == 0){
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ANNO_DI_ESERCIZIO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ANNO_DI_ESERCIZIO";
				}	
			}
		}
		
		if(cartaContabile==null){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CARTA_CONTABILE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CARTA_CONTABILE";
			}	
		}else {
			if(cartaContabile.getCartaEstera()!=null){
				if(cartaContabile.getCartaEstera().getCommissioniEstero()==null || 
						StringUtils.isEmpty(cartaContabile.getCartaEstera().getCommissioniEstero().getCodice())){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", COMMISSIONI_ESTERO";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "COMMISSIONI_ESTERO";
					}	
				}
				
				if(cartaContabile.getCartaEstera().getValuta()==null || 
						StringUtils.isEmpty(cartaContabile.getCartaEstera().getValuta().getCodice())){
					if(elencoParamentriNonInizializzati.length() > 0){
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", VALUTA";
					}else{
						elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "VALUTA";
					}	
				}
			}
			
			if (cartaContabile.getListaPreDocumentiCarta()==null || cartaContabile.getListaPreDocumentiCarta().size()==0) {
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", PRE_DOCUMENTI_CARTA";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "PRE_DOCUMENTI_CARTA";
				}	
			}else {
				for (PreDocumentoCarta preDocumentoCarta : cartaContabile.getListaPreDocumentiCarta()) {
					if(preDocumentoCarta.getSoggetto()==null){
						if(elencoParamentriNonInizializzati.length() > 0){
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SOGGETTO_PREDOCUMENTO";
						}else{
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SOGGETTO_PREDOCUMENTO";
						}	
					}else {
						if(preDocumentoCarta.getSoggetto().getElencoModalitaPagamento()==null || preDocumentoCarta.getSoggetto().getElencoModalitaPagamento().size()!=1){
							if(elencoParamentriNonInizializzati.length() > 0){
								elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_PAGAMENTO_SOGGETTO_PREDOCUMENTO";
							}else{
								elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_PAGAMENTO_SOGGETTO_PREDOCUMENTO";
							}	
						}
					}
					
					//SIAC-6173
//					if(isRigaDaMovimenti(preDocumentoCarta)){
						//questo controllo solo per righe da movimenti
						if(preDocumentoCarta.getContoTesoreria()==null ||
								StringUtils.isEmpty(preDocumentoCarta.getContoTesoreria().getCodice())){
							if(elencoParamentriNonInizializzati.length() > 0){
								elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CONTO_TESORIERE_PREDOCUMENTO";
							}else{
								elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CONTO_TESORIERE_PREDOCUMENTO";
							}	
						
					}
					
				}
			}
		}


		if(!StringUtils.isEmpty(elencoParamentriNonInizializzati)){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}	

}
