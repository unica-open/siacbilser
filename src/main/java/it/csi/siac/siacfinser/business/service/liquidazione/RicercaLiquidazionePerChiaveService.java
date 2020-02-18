/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaLiquidazionePerChiaveService extends AbstractLiquidazioneService<RicercaLiquidazionePerChiave, RicercaLiquidazionePerChiaveResponse> {
	
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
		initModalitaPagamentoSoggettoHelper();
	}	
	
	@Override
	@Transactional(timeout=360, readOnly=true)
	public RicercaLiquidazionePerChiaveResponse executeService(RicercaLiquidazionePerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		long startUno = System.currentTimeMillis();
		
		log.debug("[RicercaLiquidazionePerChiaveService:: execute] ", " Tempo di partenza:  " + CommonUtils.convertiMillisecondiInData(startUno));
		
		//Lettura variabili di input
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		Integer annoEsercizio = req.getpRicercaLiquidazioneK().getAnnoEsercizio();
		
		RicercaLiquidazioneK plk = req.getpRicercaLiquidazioneK();
		Timestamp now = new Timestamp(req.getDataOra().getTime());

		if(plk.getTipoRicerca()==null){
			//imposto un default che è LIQUIDAZIONE
			plk.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);
		}
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);
		
		// Ricerca della liquidazione per chiave
		Liquidazione liquidazione = liquidazioneDad.ricercaLiquidazionePerChiave(plk.getLiquidazione(),plk.getTipoRicerca(), richiedente, annoEsercizio, Constanti.AMBITO_FIN,ente,datiOperazione);
		//
		
		//Completo i dati ulteriori:
		List<Errore> erroriCompletamentoDati = completaDatiLiquidazione(liquidazione, richiedente, annoEsercizio, plk.getTipoRicerca());
		
		if(!StringUtils.isEmpty(erroriCompletamentoDati)){
			//Ci sono errori nel completamento dati 
			res.setErrori(erroriCompletamentoDati);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		/*
		if(liquidazione!=null){
			//Se il esiste il provvedimento viene aggiornato con tutti i dati
			// RM 23/03/2016 PROVE DI OTTIMIZZAZIONE:
			// IL PROVEVDIMENTO VIENE GIA' CARICATO COMPLETAMENTE IN liquidazioneDad.ricercaLiquidazionePerChiave (VEDI CHIAMATA A EntityToModelConverter.siacTAttoToAttoAmministrativo
			// scommento per ora perchè non possono provarlo!
//			if(liquidazione.getAttoAmministrativoLiquidazione()!=null){
//				liquidazione.setAttoAmministrativoLiquidazione(estraiAttoAmministrativo(richiedente, liquidazione.getAttoAmministrativoLiquidazione()));
//			}
			
			//Se esiste il capitolo viene aggiornato con tutti i dati
			if(liquidazione.getImpegno()!=null){
				CapitoloUscitaGestione capitolo = caricaCapitoloUscitaGestione(richiedente, liquidazione.getImpegno().getChiaveCapitoloUscitaGestione(), true);
				liquidazione.setCapitoloUscitaGestione(capitolo);
				if(capitolo==null){
					//Caso che non dovrebbe verificarsi va loggato per dignostica:
					log.error("RicercaLiquidazionePerChiaveService", "plk.getTipoRicerca(): " + plk.getTipoRicerca());
					log.error("RicercaLiquidazionePerChiaveService", "annoEsercizio:" + annoEsercizio);
					log.error("RicercaLiquidazionePerChiaveService", "ente.getUid(): " + ente.getUid());
					log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getAnnoLiquidazione(): " + liquidazione.getAnnoLiquidazione());
					log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getNumeroLiquidazione():" + liquidazione.getNumeroLiquidazione());
					log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getImpegno().getNumero():" + liquidazione.getImpegno().getNumero());
					if(liquidazione.getSubImpegno()!=null){
						log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getSubImpegno().getNumero:" + liquidazione.getSubImpegno().getNumero());
						log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getSubImpegno().getChiaveCapitoloUscitaGestione:" + liquidazione.getSubImpegno().getChiaveCapitoloUscitaGestione());
					}
					log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getImpegno().getAnnoMovimento():" + liquidazione.getImpegno().getAnnoMovimento());
					log.error("RicercaLiquidazionePerChiaveService", "liquidazione.getImpegno().getChiaveCapitoloUscitaGestione():" + liquidazione.getImpegno().getChiaveCapitoloUscitaGestione());
				}
			}	
			
			// 11/11/2014 patch per mancanza di analisi precedente: 
			// Se esiste un subdocumento legato lo leggo ma ...leggi sotto
			// RM 28/03/2017 : lo carico solo se NON sono richiamata dall'emettitore			
			if(liquidazione.getSubdocumentoSpesa()!=null && !plk.getTipoRicerca().equalsIgnoreCase(Constanti.TIPO_RICERCA_DA_EMISSIONE_ORDINATIVO)){
				// mi metto da parte il Documento ricavato nella ricerca per chiave della liquidazione
				// cosi da non perderlo nel setSubdocumentoSpesa dopo la ricerca del dettaglio quote
				DocumentoSpesa docSpesa = liquidazione.getSubdocumentoSpesa().getDocumento(); 
				
				// se esiste devo richiamare il servizio che mi ritorna l'oggetto intero
				RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesaResponse = 
						documentoSpesaService.ricercaDettaglioQuotaSpesa(convertiPerRicercaDettaglioQuotaSpesa(liquidazione.getSubdocumentoSpesa().getUid()));
				
				
				if(ricercaDettaglioQuotaSpesaResponse.isFallimento()){
					res.setErrori(ricercaDettaglioQuotaSpesaResponse.getErrori());
					res.setEsito(Esito.FALLIMENTO);
					return;
				}
								
				// se ottengo qualcosa lo setto nel subdocumento
				liquidazione.setSubdocumentoSpesa(ricercaDettaglioQuotaSpesaResponse.getSubdocumentoSpesa());
				
				if(liquidazione.getSubdocumentoSpesa()!=null
						&& liquidazione.getSubdocumentoSpesa().getModalitaPagamentoSoggetto()!=null){
					// 	SIAC-5156 settiamo la nuova descrizione arricchita
					ModalitaPagamentoSoggetto modPagSubDoc = liquidazione.getSubdocumentoSpesa().getModalitaPagamentoSoggetto();
					DescrizioneInfoModPagSog infoDesc = modalitaPagamentoSoggettoHelper.componiDescrizioneArricchitaModalitaPagamento(modPagSubDoc, null, ente.getUid());
					liquidazione.getSubdocumentoSpesa().getModalitaPagamentoSoggetto().setDescrizioneInfo(infoDesc);
				}
				
				liquidazione.getSubdocumentoSpesa().setDocumento(docSpesa);
			}
			
		}*/
		
		
		res.setEsito(Esito.SUCCESSO);
		res.setLiquidazione(liquidazione);
		
		long endUno = System.currentTimeMillis();
		log.debug("[RicercaLiquidazionePerChiaveService:: execute] ", "Tempo per esecuzione exceute: " +  (endUno  - startUno) );
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		if (req.getpRicercaLiquidazioneK() == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria Liquidazione"));			
		}  else {
			
			Liquidazione liquidazione = req.getpRicercaLiquidazioneK().getLiquidazione();
			if (liquidazione == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Liquidazione"));
			}
			else{
				if(liquidazione.getAnnoLiquidazione()==null || liquidazione.getNumeroLiquidazione()==null){
					checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno / numero Liquidazione"));
				}
				
				if(req.getpRicercaLiquidazioneK().getAnnoEsercizio() == null){
					checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno Esercizio"));
				}
			}
			
		}
		Ente ente = req.getRichiedente().getAccount().getEnte();
		if (ente == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}
	}
	
}