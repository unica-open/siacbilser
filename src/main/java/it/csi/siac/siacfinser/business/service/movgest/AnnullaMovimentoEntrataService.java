/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaMovimentoEntrataService extends AbstractBaseService<AnnullaMovimentoEntrata, AnnullaMovimentoEntrataResponse> {
	
	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		final String methodName = "AnnullaMovimentoEntrataService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	
	@Override
	@Transactional
	public AnnullaMovimentoEntrataResponse executeService(
			AnnullaMovimentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "AnnullaMovimentoEntrataService : execute()";
		log.debug(methodName, "- Begin");
		
		//1. Leggiamo i dati ricevuti dalla request:
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Accertamento accertamento = req.getAccertamento();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		setBilancio(req.getBilancio());
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi 
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.ANNULLA, bilancio.getAnno());
		
		//2.1. Si valorizza l'oggetto ImpegnoInModificaInfoDto, dto di comodo specifico di questo servizio
		Accertamento accertamentoClone = caricaAnnoENumeroSeVuotiMaUidPresente(clone(accertamento));
		@SuppressWarnings("rawtypes")
		ImpegnoInModificaInfoDto accertamentoInModificaInfoDto = impegnoOttimizzatoDad.getDatiGeneraliImpegnoInAggiornamento(accertamentoClone, datiOperazione, bilancio);
		
		//2.2. Carichiamo i dati del capitolo:
		HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = 
				caricaCapitolEntrataGestioneEResiduo(richiedente,accertamentoClone.getChiaveCapitoloEntrataGestione(), accertamentoInModificaInfoDto.getChiaveCapitoloResiduo());
		
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioEntrata(capitoliDaServizio);
		
		//3. CONTROLLI di merito per l'annullamento:
		List<Errore> listaErrori = accertamentoOttimizzatoDad.controlliDiMeritoAnnullamentoAccertamentoServizio(richiedente,  ente, bilancio, accertamento, datiOperazione);
		if(listaErrori!=null && listaErrori.size()>0){
			//il servizio termina per fallimento nei controlli
			res.setErrori(listaErrori);
			res.setAccertamento(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		//setto l'atto amministrativo se nullo da chiamante:
		accertamento = caricaAnnoENumeroSeVuotiMaUidPresente(accertamento);
		accertamento = impegnoOttimizzatoDad.caricaAttoAmministrativoSeNonValorizzato(accertamento);
		//
		
		String tipoMovimento = CostantiFin.MOVGEST_TIPO_ACCERTAMENTO;
		boolean inserireDoppiaGestione = accertamentoOttimizzatoDad.inserireDoppiaGestione(bilancio, (Accertamento)accertamento, datiOperazione);
		Accertamento accertamentoCaricatoPerDoppiaGestione = null;
		if(inserireDoppiaGestione){
			String annoEsercizio = Integer.toString(bilancio.getAnno());
			Integer annoMovimento = accertamento.getAnnoMovimento();
			BigDecimal numeroMovimento = accertamento.getNumeroBigDecimal();
			accertamentoCaricatoPerDoppiaGestione = ricaricaMovimentoPerAnnullaModifica(richiedente, numeroMovimento, annoEsercizio, annoMovimento, tipoMovimento);
		}
		
		//4. Si invoca il metodo che esegue l'operazione "core" di annullamento di impegni o accertamenti:
		EsitoAggiornamentoMovimentoGestioneDto esitoAnnullaMovimento = accertamentoOttimizzatoDad.annullaMovimento(ente, richiedente, accertamento, tipoMovimento, datiOperazione,bilancio,capitoliInfo,accertamentoInModificaInfoDto,accertamentoCaricatoPerDoppiaGestione);
	
		if (req.isVerificaImportiDopoAnnullamentoModifica()) { // SIAC-8090
			accertamentoOttimizzatoDad.verificaImportiDopoAnnullamentoModifica(ente.getUid(), req.getBilancio().getUid(), "A", accertamento.getAnnoMovimento(), accertamento.getNumeroBigDecimal());
		}
		
		//5. Costruzione response:
		if ( (esitoAnnullaMovimento.getListaErrori()!=null && esitoAnnullaMovimento.getListaErrori().size()>0) || esitoAnnullaMovimento.getMovimentoGestione()==null) {
			//Esito negativo da operazione interna
			res.setErrori(esitoAnnullaMovimento.getListaErrori());
			res.setAccertamento(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
			
		} 
		else  
		
		{
			TransactionAspectSupport.currentTransactionStatus().flush();
			//String annoEsercizio = Integer.toString(bilancio.getAnno());
			Integer annoAccertamento = accertamento.getAnnoMovimento();
			BigDecimal numeroAccertamento = accertamento.getNumeroBigDecimal();
			if(annoAccertamento != null && annoAccertamento.intValue()>0 && numeroAccertamento!=null && numeroAccertamento.intValue()>0){
				
				//ricarico i dati puliti del movimento annullato per restituirlo in output al servizio:
				Accertamento accertamentoReload = (Accertamento) accertamentoOttimizzatoDad.ricercaMovimentoPk(
						richiedente, ente, Integer.toString(annoBilancioRequest), annoAccertamento, numeroAccertamento,
						CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true, false);
				accertamentoReload = completaDatiRicercaAccertamentoPk(richiedente, accertamentoReload);
				
				res.setAccertamento(accertamentoReload);
				
			} else {
				
				//impossibile ricaricare (ramo in cui si entra solo per malfunzionamento di accertamentoOttimizzatoDad.annullaMovimento
				res.setErrori(null);
				res.setAccertamento((Accertamento)esitoAnnullaMovimento.getMovimentoGestione());
				res.setEsito(Esito.SUCCESSO);
			}
			
			
			//innesto
			// innesto fin - gen
			// In caso di annullamento subImpegno in request arriva il sub da annullare 
			
			
			if(accertamento.getElencoSubAccertamenti()!=null && 
					accertamento.getElencoSubAccertamenti().size() == 1){
				
				SubAccertamento sub = accertamento.getElencoSubAccertamenti().get(0);
				annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.SUBACCERTAMENTO, sub, annoBilancioRequest);
								
				// se annullo il sub devo annullare anche tutte le possibili modifiche di importo / soggetto
				if(sub.getListaModificheMovimentoGestioneEntrata()!=null && !sub.getListaModificheMovimentoGestioneEntrata().isEmpty()){
					
					for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : sub.getListaModificheMovimentoGestioneEntrata()) {
					
						annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, modificaMovimentoGestioneEntrata, annoBilancioRequest);
					}
					
				}
				
			}
			
			// In caso di annullamento di una modifica di importo in request arriva la modifica da annullare 
			if(accertamento.getListaModificheMovimentoGestioneEntrata()!=null && 
					accertamento.getListaModificheMovimentoGestioneEntrata().size() == 1)
				annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, accertamento.getListaModificheMovimentoGestioneEntrata().get(0), annoBilancioRequest);
			
			res.setEsito(Esito.SUCCESSO);
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaMovimentoEntrataService : checkServiceParam()";
		log.debug(methodName, "- Begin");
	
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Accertamento accertamento = req.getAccertamento();
		Bilancio bilancio = req.getBilancio();
		Integer annoBilancio = req.getBilancio().getAnno();
		Integer idAccertamento = req.getAccertamento().getUid();
		
		if(null==ente && null==accertamento && null==bilancio){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN_PARAMETRO_DI_RICERCA_INIZIALIZZATO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		} else if(null==accertamento){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ACCERTAMENTO"));
		} else if(null==bilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		} else if(null==annoBilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_BILANCIO"));
		} else if(null==idAccertamento){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ID_ACCERTAMENTO"));
		}
	}	
}