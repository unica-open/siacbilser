/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.modpag.DescrizioneInfoModPagSog;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public abstract class AbstractLiquidazioneService <REQ extends ServiceRequest, RES extends ServiceResponse> extends AbstractBaseService<REQ, RES> {
	
	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	protected List<Errore> completaDatiLiquidazione(Liquidazione liquidazione, Richiedente richiedente, Integer annoEsercizio, String tipoRicerca){
		final String methodName ="completaDatiLiquidazione";
		List<Errore> erroriRiscontrati = new ArrayList<Errore>();
		
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
					log.warn(methodName, "ImpossibileCaricare il capitolo con chiave: " + liquidazione.getImpegno().getChiaveCapitoloUscitaGestione());
				}
			}	
			
			// 11/11/2014 patch per mancanza di analisi precedente: 
			// Se esiste un subdocumento legato lo leggo ma ...leggi sotto
			// RM 28/03/2017 : lo carico solo se NON sono richiamata dall'emettitore			
			if(liquidazione.getSubdocumentoSpesa()!=null && !tipoRicerca.equalsIgnoreCase(Constanti.TIPO_RICERCA_DA_EMISSIONE_ORDINATIVO)){
				// mi metto da parte il Documento ricavato nella ricerca per chiave della liquidazione
				// cosi da non perderlo nel setSubdocumentoSpesa dopo la ricerca del dettaglio quote
				DocumentoSpesa docSpesa = liquidazione.getSubdocumentoSpesa().getDocumento(); 
				
				// se esiste devo richiamare il servizio che mi ritorna l'oggetto intero
				RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesaResponse = 
						documentoSpesaService.ricercaDettaglioQuotaSpesa(convertiPerRicercaDettaglioQuotaSpesa(liquidazione.getSubdocumentoSpesa().getUid()));
				
				
				if(ricercaDettaglioQuotaSpesaResponse.isFallimento()){
					//restituisco gli errori trovati:
					return ricercaDettaglioQuotaSpesaResponse.getErrori();
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
			
		}
		
		return erroriRiscontrati;
		
	}
	
	private RicercaDettaglioQuotaSpesa convertiPerRicercaDettaglioQuotaSpesa(Integer id){
		RicercaDettaglioQuotaSpesa ricercaDettaglioQuotaSpesa = new RicercaDettaglioQuotaSpesa();
		ricercaDettaglioQuotaSpesa.setRichiedente(req.getRichiedente());
		
		SubdocumentoSpesa subdocumentoSpesaInput = new SubdocumentoSpesa();
		subdocumentoSpesaInput.setUid(id);
		ricercaDettaglioQuotaSpesa.setSubdocumentoSpesa(subdocumentoSpesaInput);
		
		return ricercaDettaglioQuotaSpesa;
	}
	
}