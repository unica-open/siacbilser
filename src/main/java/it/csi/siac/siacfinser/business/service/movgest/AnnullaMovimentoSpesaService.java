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

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ModificaMovimentoGestioneDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaMovimentoSpesaService extends AbstractBaseService<AnnullaMovimentoSpesa, AnnullaMovimentoSpesaResponse> {
	
	@Autowired
	CommonDad commonDad;
	@Autowired
	private ModificaMovimentoGestioneDad modificaMovimentoGestioneDad;
	
	@Override
	protected void init() {
		final String methodName = "AnnullaMovimentoSpesaService : init()";
		log.debug(methodName, "- Begin");
	}	

	@Override
	@Transactional
	public AnnullaMovimentoSpesaResponse executeService(AnnullaMovimentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "AnnullaMovimentoSpesaService : execute()";
		log.debug(methodName, "- Begin");
		
		//1. Leggiamo i dati ricevuti dalla request:
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = req.getImpegno();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		setBilancio(req.getBilancio());
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.ANNULLA, bilancio.getAnno());
		
		//2.1. Si valorizza l'oggetto ImpegnoInModificaInfoDto, dto di comodo specifico di questo servizio
		Impegno impegnoClone = caricaAnnoENumeroSeVuotiMaUidPresente(clone(impegno));
		@SuppressWarnings("rawtypes")
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = impegnoOttimizzatoDad.getDatiGeneraliImpegnoInAggiornamento(impegnoClone, datiOperazione, bilancio);
		
		//2.2. Carichiamo i dati del capitolo:
		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = 
				caricaCapitoloUscitaGestioneEResiduo(richiedente, impegnoClone.getChiaveCapitoloUscitaGestione(), impegnoInModificaInfoDto.getChiaveCapitoloResiduo());
		
		checkDisponibilitaModificheNegativeSpesa(impegno, impegnoClone.getChiaveCapitoloUscitaGestione(), impegnoInModificaInfoDto.getChiaveCapitoloResiduo(), capitoliDaServizio);
		
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioUscita(capitoliDaServizio);
		
		//setto l'atto amministrativo se nullo da chiamante:
		impegno = caricaAnnoENumeroSeVuotiMaUidPresente(impegno);
		impegno = impegnoOttimizzatoDad.caricaAttoAmministrativoSeNonValorizzato(impegno);
		//
		
		String tipoMovimento = Constanti.MOVGEST_TIPO_IMPEGNO;
		boolean inserireDoppiaGestione = impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, (Impegno)impegno, datiOperazione);
		Impegno impegnoCaricatoPerDoppiaGestione = null;
		if(inserireDoppiaGestione){
			String annoEsercizio = Integer.toString(bilancio.getAnno());
			Integer annoMovimento = impegno.getAnnoMovimento();
			BigDecimal numeroMovimento = impegno.getNumero();
			impegnoCaricatoPerDoppiaGestione = ricaricaMovimentoPerAnnullaModifica(richiedente, numeroMovimento, annoEsercizio, annoMovimento, tipoMovimento);
		}
		
		//3. Si invoca il metodo che esegue l'operazione "core" di annullamento di impegni o accertamenti:
		EsitoAggiornamentoMovimentoGestioneDto esitoAnnullaMovimento = impegnoOttimizzatoDad.annullaMovimento(ente, richiedente, impegno,tipoMovimento ,datiOperazione,bilancio,capitoliInfo,impegnoInModificaInfoDto,impegnoCaricatoPerDoppiaGestione);
		
		//5. Costruzione response:
		if ( (esitoAnnullaMovimento.getListaErrori()!=null && esitoAnnullaMovimento.getListaErrori().size()>0) || esitoAnnullaMovimento.getMovimentoGestione()==null) {
			//Esito negativo da operazione interna
			res.setErrori(esitoAnnullaMovimento.getListaErrori());
			res.setImpegno(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		} else {
			TransactionAspectSupport.currentTransactionStatus().flush();
			//String annoEsercizio = Integer.toString(bilancio.getAnno());
			Integer annoImpegno = impegno.getAnnoMovimento();
			BigDecimal numeroImpegno = impegno.getNumero();
			if(annoImpegno != null && annoImpegno.intValue()>0 && numeroImpegno!=null && numeroImpegno.intValue()>0){
				// Ricarico l'impegno modificato 
				
				//MARZO 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
				PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
				paginazioneSubMovimentiDto.setNoSub(true);
				
				EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, Integer.toString(annoBilancioRequest), annoImpegno, numeroImpegno,paginazioneSubMovimentiDto , null, Constanti.MOVGEST_TIPO_IMPEGNO, true);
				
				Impegno impegnoReload = null;
				if(esitoRicercaMov!=null){
					impegnoReload = (Impegno) esitoRicercaMov.getMovimentoGestione();
				}
				
				impegnoReload = completaDatiRicercaImpegnoPk(richiedente, impegnoReload, Integer.toString(annoBilancioRequest));

				res.setImpegno(impegnoReload);
			} else {
				
				res.setErrori(null);
				res.setImpegno((Impegno)esitoAnnullaMovimento.getMovimentoGestione());
				res.setEsito(Esito.SUCCESSO);
			}
			
			
			// innesto fin - gen
			// In caso di annullamento subImpegno in request arriva il sub da annullare 
			if(impegno.getElencoSubImpegni()!=null && 
					impegno.getElencoSubImpegni().size() == 1){
				
				SubImpegno sub =  impegno.getElencoSubImpegni().get(0);
				annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.SUBIMPEGNO, sub, annoBilancioRequest);
				
				// se annullo il sub devo annullare anche tutte le possibili modifiche di importo / soggetto
				if(sub.getListaModificheMovimentoGestioneSpesa()!=null && !sub.getListaModificheMovimentoGestioneSpesa().isEmpty()){
					
					for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa : sub.getListaModificheMovimentoGestioneSpesa()) {
					
						annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, modificaMovimentoGestioneSpesa, annoBilancioRequest);
					}
					
				}
			
			}
			
			// In caso di annullamento di una modifica di importo in request arriva la modifica da annullare 
			if(impegno.getListaModificheMovimentoGestioneSpesa()!=null && 
					impegno.getListaModificheMovimentoGestioneSpesa().size() == 1)
				annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, impegno.getListaModificheMovimentoGestioneSpesa().get(0), annoBilancioRequest);
			
			res.setEsito(Esito.SUCCESSO);
		}
	}

	
	private void checkDisponibilitaModificheNegativeSpesa(Impegno impegno, int chiaveCapitoloUscitaGestione,Integer chiaveCapitoloResiduo, HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio) {
		List<ModificaMovimentoGestioneSpesa> listaModificheMovimentoGestioneSpesa = impegno.getListaModificheMovimentoGestioneSpesa();
		if(listaModificheMovimentoGestioneSpesa ==null || listaModificheMovimentoGestioneSpesa.isEmpty() || capitoliDaServizio == null){
			//non sto annullando una modifica
			return;
		}
		BigDecimal sommaImportiModificheNegative = BigDecimal.ZERO;
		for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa : listaModificheMovimentoGestioneSpesa) {
			BigDecimal importoAttualeModifica = modificaMovimentoGestioneDad.getImportoAttualeModifica(modificaMovimentoGestioneSpesa);
			if(importoAttualeModifica == null || importoAttualeModifica.signum() >=0) {
				continue;
			}
			//modifica di importo negativa
			sommaImportiModificheNegative = sommaImportiModificheNegative.add(importoAttualeModifica);
		}
		if(BigDecimal.ZERO.compareTo(sommaImportiModificheNegative) <=0){
			//non ho modifiche di importo negative
			return;
		}
		CapitoloUscitaGestione capitoloUscitaGestione = capitoliDaServizio.get(chiaveCapitoloUscitaGestione);
		BigDecimal disponibilitaImpegnare = capitoloUscitaGestione.getImportiCapitolo().getDisponibilitaImpegnareAnno1();
		
		if(disponibilitaImpegnare == null || disponibilitaImpegnare.add(sommaImportiModificheNegative).compareTo(BigDecimal.ZERO) <0) {
			String stringDisponibilita = disponibilitaImpegnare != null? disponibilitaImpegnare.toString() : "null";
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile annullare una modifica se l'importo del movimento di gestione dopo l'annullamento supera la disponibilit&agrave; ad impegnare del capitolo. Dispoonibilita ad impegnare del capitolo  " + chiaveCapitoloUscitaGestione + " : " + stringDisponibilita));
		}
		
		
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaMovimentoSpesaService : checkServiceParam()";
		log.debug(methodName, "- Begin");
	
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Impegno impegno = req.getImpegno();
		Bilancio bilancio = req.getBilancio();
		Integer annoBilancio = req.getBilancio().getAnno();
		Integer idImpegno = req.getImpegno().getUid();
		
		if(null==ente && null==impegno && null==bilancio){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN_PARAMETRO_DI_RICERCA_INIZIALIZZATO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		} else if(null==impegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IMPEGNO"));
		} else if(null==bilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		} else if(null==annoBilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_BILANCIO"));
		} else if(null==idImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ID_IMPEGNO"));
		}
	}	
	
	

	
}