/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import it.csi.siac.siacfinser.CostantiFin;
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

/**
 * The Class AnnullaMovimentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaMovimentoSpesaService extends AbstractBaseService<AnnullaMovimentoSpesa, AnnullaMovimentoSpesaResponse> {
	
	@Autowired
	CommonDad commonDad;
	@Autowired
	private ModificaMovimentoGestioneDad modificaMovimentoGestioneDad;
	
	private static final String[] MOD_TIPO_CODES_NON_IN_CALCOLO_DISPONIBILE_CAPITOLO = new String[] {"ECONB"};
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Impegno impegno = req.getImpegno();
		Bilancio bilancio = req.getBilancio();
		
		checkCondition(ente != null || impegno != null || bilancio != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN_PARAMETRO_DI_RICERCA_INIZIALIZZATO"));
		checkCondition(ente != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		checkCondition(impegno != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IMPEGNO"));
		checkCondition(bilancio != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_BILANCIO"));
		checkCondition(req.getImpegno().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ID_IMPEGNO"));
	}	
	
	@Override
	protected void init() {
		final String methodName = "init";
		log.debug(methodName, "- Begin");
	}	

	@Override
	@Transactional
	public AnnullaMovimentoSpesaResponse executeService(AnnullaMovimentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		
		//1. Leggiamo i dati ricevuti dalla request:
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = req.getImpegno();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		setBilancio(req.getBilancio());
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.ANNULLA, bilancio.getAnno());
		
		//SIAC-6865
		checkImpegnoCollegatoAggiudicazione(impegno);
		
		//2.1. Si valorizza l'oggetto ImpegnoInModificaInfoDto, dto di comodo specifico di questo servizio
		Impegno impegnoClone = caricaAnnoENumeroSeVuotiMaUidPresente(clone(impegno));
		@SuppressWarnings("rawtypes")
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = impegnoOttimizzatoDad.getDatiGeneraliImpegnoInAggiornamento(impegnoClone, datiOperazione, bilancio);
		
		//2.2. Carichiamo i dati del capitolo:
		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = 
				caricaCapitoloUscitaGestioneEResiduo(richiedente, impegnoClone.getChiaveCapitoloUscitaGestione(), impegnoInModificaInfoDto.getChiaveCapitoloResiduo());
		
		//setto l'atto amministrativo se nullo da chiamante:
		impegno = caricaAnnoENumeroSeVuotiMaUidPresente(impegno);
		impegno = impegnoOttimizzatoDad.caricaAttoAmministrativoSeNonValorizzato(impegno);
		
		//SIAC-6750
		checkDisponibilitaModificheNegativeSpesa(impegno, impegnoClone.getChiaveCapitoloUscitaGestione(), impegnoInModificaInfoDto.getChiaveCapitoloResiduo(), capitoliDaServizio, impegnoClone.getAnnoMovimento(), annoBilancioRequest);
		
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioUscita(capitoliDaServizio);
		
		//
		
		String tipoMovimento = CostantiFin.MOVGEST_TIPO_IMPEGNO;
		boolean inserireDoppiaGestione = impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, (Impegno)impegno, datiOperazione);
		Impegno impegnoCaricatoPerDoppiaGestione = null;
		if(inserireDoppiaGestione){
			String annoEsercizio = Integer.toString(bilancio.getAnno());
			Integer annoMovimento = impegno.getAnnoMovimento();
			BigDecimal numeroMovimento = impegno.getNumeroBigDecimal();
			impegnoCaricatoPerDoppiaGestione = ricaricaMovimentoPerAnnullaModifica(richiedente, numeroMovimento, annoEsercizio, annoMovimento, tipoMovimento);
		}
		
		//3. Si invoca il metodo che esegue l'operazione "core" di annullamento di impegni o accertamenti:
		EsitoAggiornamentoMovimentoGestioneDto esitoAnnullaMovimento = impegnoOttimizzatoDad.annullaMovimento(ente, richiedente, impegno,tipoMovimento ,datiOperazione,bilancio,capitoliInfo,impegnoInModificaInfoDto,impegnoCaricatoPerDoppiaGestione);
		
		if (req.isVerificaImportiDopoAnnullamentoModifica()) { // SIAC-8090
			impegnoOttimizzatoDad.verificaImportiDopoAnnullamentoModifica(ente.getUid(), req.getBilancio().getUid(), "I", impegno.getAnnoMovimento(),  impegno.getNumeroBigDecimal());
		}
		
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
			BigDecimal numeroImpegno = impegno.getNumeroBigDecimal();
			if(annoImpegno != null && annoImpegno.intValue()>0 && numeroImpegno!=null && numeroImpegno.intValue()>0){
				// Ricarico l'impegno modificato 
				
				//MARZO 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
				PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
				paginazioneSubMovimentiDto.setNoSub(true);
				
				EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(
						richiedente, ente, Integer.toString(annoBilancioRequest), annoImpegno, numeroImpegno,
						paginazioneSubMovimentiDto , null, CostantiFin.MOVGEST_TIPO_IMPEGNO, true, true);
				
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

	
	private void checkImpegnoCollegatoAggiudicazione(Impegno impegno) {
		List<ModificaMovimentoGestioneSpesa> mods = impegno.getListaModificheMovimentoGestioneSpesa();
		if(mods == null){
			return;
		}
		
		for (ModificaMovimentoGestioneSpesa mod : mods) {
			 List<String> chiaviMovgestAgg = modificaMovimentoGestioneDad.getChiaviLogicheMovimentoGestioneAggiudicazioneDaModifica(mod);
			 if(chiaviMovgestAgg == null || chiaviMovgestAgg.isEmpty()) {
				 continue;
			 }
			 throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("annullamento impossibile, provvedere ad annullare prima i seguenti impegni di aggiudicazione: " + StringUtils.join(chiaviMovgestAgg.toArray(), ",")));
		} 
	}

	/**
	 * Checks if is controllo su disponibilita da effettuare.
	 * Saltare il controllo se:
	 * <ul>
	 *     <li>l'anno non e' compreso tra l'anno di bilancio e l'anno di bilancio +2 (come accade per gli impegni residui)</li>
	 *     <li>flagSDF = true</li>
	 *     <li>il provvedimento collegato e' provvisorio</li>
	 * </ul> 
	 * 
	 * @param impegno the impegno
	 * @param annoMovimento the anno movimento
	 * @param annoBilancioRequest the anno bilancio request
	 * @param capitoliDaServizio 
	 * @param listaModificheMovimentoGestioneSpesa 
	 * @param datiOperazione the dati operazione
	 * @return true, if is controllo su disponibilita da effettuare
	 */
	private boolean saltaControlloModificheNegativePerCaratteristicheImpegno(Impegno impegno,int annoMovimento, Integer annoBilancioRequest, List<ModificaMovimentoGestioneSpesa> listaModificheMovimentoGestioneSpesa, HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio) {
		
		if(listaModificheMovimentoGestioneSpesa ==null || listaModificheMovimentoGestioneSpesa.isEmpty() || capitoliDaServizio == null){
			//non sto annullando una modifica o non ho i dati per effettuare il controllo
			return true;
		}
		
		//SIAC-7390
		boolean annoMovimentoCompatibileConControlloDisponibilita =annoBilancioRequest != null &&  annoMovimento >= annoBilancioRequest.intValue() && annoMovimento <= (annoBilancioRequest.intValue() + 2);
		if(!annoMovimentoCompatibileConControlloDisponibilita) {
			//l'anno non e' compreso tra anno esercizio e anno esercizio + 2
			return true;
		}
		

		// SIAC-7390
		Boolean bFlagSDF = impegnoOttimizzatoDad.getAttributoBoolean(impegno.getUid(), "flagSDF");
		 
		if(!Boolean.FALSE.equals(bFlagSDF)) {
			//se null, salto il controllo
			return true;
		}
		
		return false;
		
	}
	
	
	private boolean saltaControlloModificheNegativePerCaratteristicheModifica(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa,BigDecimal importoAttualeModifica, List<String> listaTipiSenzaControlloDisponibilita ) {
		
		if(importoAttualeModifica == null || importoAttualeModifica.signum() >=0) {
			return true;
		}
		
		String tipoModificaCode = modificaMovimentoGestioneDad.getTipoModifica(modificaMovimentoGestioneSpesa);
		if(tipoModificaCode != null && listaTipiSenzaControlloDisponibilita.contains(tipoModificaCode)) {
			return true;
		}
		//SIAC-7480
		String statoProvvedimentoCode =  modificaMovimentoGestioneDad.determinaStatoOperativoProvvedimentoCollegato(modificaMovimentoGestioneSpesa);//impegnoOttimizzatoDad.determinaStatoImpegno(datiOperazione.getSiacTEnteProprietario().getEnteProprietarioId(), impegno);
		if(CostantiFin.ATTO_AMM_STATO_PROVVISORIO.equals(statoProvvedimentoCode)) {
			return true;
		}
		return false;
	}
	
	
	private void checkDisponibilitaModificheNegativeSpesa(Impegno impegno, int chiaveCapitoloUscitaGestione,Integer chiaveCapitoloResiduo, HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio, int annoMovimento, Integer annoBilancioRequest) {
		List<ModificaMovimentoGestioneSpesa> listaModificheMovimentoGestioneSpesa = impegno.getListaModificheMovimentoGestioneSpesa();
		
		boolean saltaControllo = saltaControlloModificheNegativePerCaratteristicheImpegno(impegno, annoMovimento, annoBilancioRequest, listaModificheMovimentoGestioneSpesa, capitoliDaServizio); 
		if(saltaControllo) {
			return;
		}
		
		BigDecimal sommaImportiModificheNegative = BigDecimal.ZERO;
		List<String> listaTipiSenzaControlloDisponibilita =Arrays.asList(MOD_TIPO_CODES_NON_IN_CALCOLO_DISPONIBILE_CAPITOLO);
		
		for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa : listaModificheMovimentoGestioneSpesa) {
			BigDecimal importoAttualeModifica = modificaMovimentoGestioneDad.getImportoAttualeModifica(modificaMovimentoGestioneSpesa);
			
			if(saltaControlloModificheNegativePerCaratteristicheModifica(modificaMovimentoGestioneSpesa, importoAttualeModifica, listaTipiSenzaControlloDisponibilita)) {
				continue;
			}
			
			//modifica di importo negativa
			sommaImportiModificheNegative = sommaImportiModificheNegative.add(importoAttualeModifica);
		}
		
		CapitoloUscitaGestione capitoloUscitaGestione = capitoliDaServizio.get(chiaveCapitoloUscitaGestione);
		BigDecimal disponibilitaImpegnare = capitoloUscitaGestione.getImportiCapitolo().getDisponibilitaImpegnareAnno1();

		
		if(BigDecimal.ZERO.compareTo(sommaImportiModificheNegative) > 0 && (disponibilitaImpegnare == null || disponibilitaImpegnare.add(sommaImportiModificheNegative).compareTo(BigDecimal.ZERO) <0)) {
			String stringDisponibilita = disponibilitaImpegnare != null? disponibilitaImpegnare.toString() : "null";
			// SIAC-7405
			String errore = "Impossibile annullare una modifica se l'importo del movimento di gestione dopo l'annullamento supera la disponibilit&agrave; ad impegnare del capitolo. "
					+ "Disponibilita ad impegnare del capitolo " + capitoloUscitaGestione.getAnnoNumeroArticolo() + " [uid: " + chiaveCapitoloUscitaGestione + "] : "
					+ stringDisponibilita;
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(errore));

		}
	}

}