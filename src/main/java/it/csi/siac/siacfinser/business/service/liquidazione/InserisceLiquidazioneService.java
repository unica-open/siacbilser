/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOptSubDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneLiquidazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceLiquidazioneService extends AbstractLiquidazioneService<InserisceLiquidazione, InserisceLiquidazioneResponse> {

	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Autowired
	ImpegnoOptSubDad impegnoOptSubDad;
	
	@Override
	protected void init() {
		final String methodName="InserisceLiquidazioneService : init()";
		log.debug(methodName, " - Begin");
	}	
	
	
	@Override
	@Transactional
	public InserisceLiquidazioneResponse executeService(InserisceLiquidazione serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	

	private boolean isCompilatoNumeroSubImpegno(){
		Liquidazione liquidazione = req.getLiquidazione();
		boolean compilato = false;
		if(liquidazione.getSubImpegno()!=null && liquidazione.getSubImpegno().getNumeroBigDecimal()!=null
				&& liquidazione.getSubImpegno().getNumeroBigDecimal().intValue()>0){
			compilato = true;
		}
		return compilato;
	}


	@Override
	public void execute() {
		String methodName = "InserisceLiquidazioneService - execute()";
		log.debug(methodName, " - Begin");
			
		//Lettura variabili di input
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		setBilancio(req.getBilancio());
		String annoEsercizio = req.getAnnoEsercizio();
		Liquidazione liquidazione = req.getLiquidazione();
		
		
		Impegno impegnoRicevuto = liquidazione.getImpegno();
		SubImpegno subRicevuto = null;
		// dobbiamo ricaricarne i dati (non possiamo fidarci di quanto indicato dal front-end):
		
		//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		boolean indicatoSub = false;
		if(isCompilatoNumeroSubImpegno()){
			//Selezionato un SUB 
			indicatoSub = true;
			paginazioneSubMovimentiDto.setNoSub(false);
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(liquidazione.getSubImpegno().getNumeroBigDecimal());
		} else {
			//Non selezionato un SUB
			indicatoSub = false;
			paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(null);
			paginazioneSubMovimentiDto.setNoSub(true);
		}
		//
		
		// se l'impegno è definito con sub annullati devo impostare i flag per non caricare i sub
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliRicercaImpegno = new  DatiOpzionaliElencoSubTuttiConSoloGliIds();
		if(impegnoRicevuto.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
			
			paginazioneSubMovimentiDto.setEscludiSubAnnullati(true);
			datiOpzionaliRicercaImpegno.setEscludiAnnullati(true);
			
		}
		
		
		EsitoRicercaMovimentoPkDto esitoRicercaMov = impegnoOttimizzatoDad.ricercaMovimentoPk(
				richiedente, ente, annoEsercizio, impegnoRicevuto.getAnnoMovimento(), 
				impegnoRicevuto.getNumeroBigDecimal(),paginazioneSubMovimentiDto, datiOpzionaliRicercaImpegno,
				CostantiFin.MOVGEST_TIPO_IMPEGNO, true, true);

		Impegno impegnoReload = null;
	    if(esitoRicercaMov!=null){
	    	impegnoReload = (Impegno) esitoRicercaMov.getMovimentoGestione();
	    }

		if(impegnoReload==null){
			addErroreFin(ErroreFin.MOVIMENTO_NON_TROVATO, impegnoRicevuto.getNumeroBigDecimal().toString());
			return;
			//Significa che non e' stato indicato un impegno esistente
		} else {
			//OK TROVATO:
			 
			//MARZO 2016: ASSIEME AD OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO AGGIUNGO QUESTO CONTROLLO CHE LATO SERVER MANCAVA:
			if(esitoRicercaMov.getElencoSubImpegniTuttiConSoloGliIds()!=null && 
					esitoRicercaMov.getElencoSubImpegniTuttiConSoloGliIds().size()>0){
				//l'impegno ha sub impegni
				if(!indicatoSub){
					//lanciare errore: non e' possibile selezionare un impegno con sub senza scegliere uno dei sub!
					addErroreFin(ErroreFin.IMPEGNO_CON_SUBIMPEGNI);
					return;
				}
			}
			//
			
			
			if(impegnoReload.getElencoSubImpegni()!=null && 
					impegnoReload.getElencoSubImpegni().size()>0){
				//l'impegno ha sub impegni
				if(!indicatoSub){
					//lanciare errore: non e' possibile selezionare un impegno con sub senza scegliere uno dei sub!
					addErroreFin(ErroreFin.IMPEGNO_CON_SUBIMPEGNI);
					return;
				}
			}
			
			if(liquidazione.getSubImpegno()!=null && liquidazione.getSubImpegno().getNumeroBigDecimal()!=null){
				subRicevuto = liquidazione.getSubImpegno();
			} else if(impegnoRicevuto.getElencoSubImpegni()!=null && impegnoRicevuto.getElencoSubImpegni().size()>0){
				subRicevuto = impegnoRicevuto.getElencoSubImpegni().get(0);
			}
			liquidazione.setImpegno(impegnoReload);
		}
		
		//come per l'impegno dobbiamo fare per il subimpegno:
		SubImpegno subImpegnoReload = null;
		if(subRicevuto!=null && subRicevuto.getNumeroBigDecimal()!=null){

			subImpegnoReload =  impegnoOttimizzatoDad.findByCode(impegnoReload.getElencoSubImpegni(), subRicevuto.getNumeroBigDecimal());
			if(subImpegnoReload==null){
				//il sub impegno non e' stato trovao:
				addErroreFin(ErroreFin.MOVIMENTO_NON_TROVATO, liquidazione.getSubImpegno().toString());
				return;
			} else {
				//sub impegno trovato:
				liquidazione.setSubImpegno(null);
				liquidazione.getImpegno().setElencoSubImpegni(CommonUtil.toList(subImpegnoReload));
			}
			
		} else if(subRicevuto!=null && subRicevuto.getNumeroBigDecimal()==null){
			//se non ricevo il numero e' come non averlo ricevuto:
			liquidazione.setSubImpegno(null);
			liquidazione.getImpegno().setElencoSubImpegni(null);
		}
			
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//Richiamo l'operazione interna di inserimento che esegue i controlli di merito ed inserisce la liquidazione
		EsitoGestioneLiquidazioneDto esitoOperazioneInternaInserisciLiq = liquidazioneDad.operazioneInternaInserisciLiquidazione(ente, richiedente, annoEsercizio, bilancio, liquidazione, 0, false,datiOperazione,null);
		
		if (esitoOperazioneInternaInserisciLiq.getListaErrori()!=null && esitoOperazioneInternaInserisciLiq.getListaErrori().size()>0) {
			res.setErrori(esitoOperazioneInternaInserisciLiq.getListaErrori());
			res.setLiquidazione(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
		
		//REINTROITI DICEMBRE 2017 per fare vedere la liquidazione
		//appena creata al ricerca liquidazione:
		commonDad.flushEntMng();
		//
		
		Liquidazione liquidazioneInserita = esitoOperazioneInternaInserisciLiq.getLiquidazione();
		
		if(liquidazione.getSubdocumentoSpesa()!=null)
			liquidazioneInserita.setSubdocumentoSpesa(liquidazione.getSubdocumentoSpesa());
		
		liquidazioneInserita.setImpegno(impegnoRicevuto);
		
		
		//DICEMBRE 2017 introdotta la possibilita di caricare i dati completi
		//della liquidazione appena inserita, serve per il servizio di reintroito
		//dove richiamando il ricerca liquidazione subito dopo l'inserimento 
		//non mi trovava nulla..quindi la carico qui dentro a richiesta:
		Liquidazione liquidazioneReload = null;
		if(req.isCaricaDatiLiquidazione()){
			
			String tipoRicerca = CostantiFin.TIPO_RICERCA_DA_LIQUIDAZIONE;
			Integer annoEsercizioInt = new Integer(annoEsercizio);
			
			// Ricerca della liquidazione per chiave
			
			Liquidazione liqK = new Liquidazione();
			liqK.setAnnoLiquidazione(liquidazioneInserita.getAnnoLiquidazione());
			liqK.setNumeroLiquidazione(liquidazioneInserita.getNumeroLiquidazione());
			
			liquidazioneReload = liquidazioneDad.ricercaLiquidazionePerChiave(liqK,tipoRicerca, richiedente, annoEsercizioInt, CostantiFin.AMBITO_FIN,ente,datiOperazione);
			
			List<Errore> erroriCompletamentoDati = completaDatiLiquidazione(liquidazioneReload, richiedente, annoEsercizioInt, tipoRicerca);
			
			if(!StringUtilsFin.isEmpty(erroriCompletamentoDati)){
				//Ci sono errori nel completamento dati 
				res.setErrori(erroriCompletamentoDati);
				res.setEsito(Esito.FALLIMENTO);
				return;
			}
		}
		
		//DICEMBRE 2017 introdotta la possibilita' di escludere le registrazioni,
		//il chiamante (l'esigenza e' nata con il servizio di reintroito) che deve
		//orchestrare in maniera atomica piu servizi che possono fallire e metterci tempo si occupera'
		//di richiamare i gen solo dopo essere certo che siano terminati tutti correttamente
		if(req.isRegistraGen()){
			EsitoAttivaRegistrazioniMovFinFINGSADto esitoAttivaRegistrazioni = gestisciRegistrazioneGENPerLiquidazioneInserita(liquidazioneInserita, impegnoRicevuto, req.getBilancio(), req.isSaltaInserimentoPrimaNotaGEN());
			RegistrazioneMovFin registrazioneMovFinFINInserita = esitoAttivaRegistrazioni.getRegistrazioneMovFinFINInserita();
			log.debug(methodName, "Inserita registrazione movfin con uid " + 
					(registrazioneMovFinFINInserita!= null? registrazioneMovFinFINInserita.getUid() : "null") +
					". L'inserimento della prima nota e' stato avviato? " + 
					!req.isSaltaInserimentoPrimaNotaGEN()
					);
			res.setRegistrazioneMovFinFIN(registrazioneMovFinFINInserita);
		}
			
		if(req.isCaricaDatiLiquidazione() && liquidazioneReload!=null){
			//e' stato esplicitamente richiesto di ricaricare tutti i dati della liquidazione appena inserita:
			res.setLiquidazione(liquidazioneReload);
		} else {
			//comportamento di default
			res.setLiquidazione(liquidazioneInserita);
		}
		
		res.setEsito(Esito.SUCCESSO);
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		String methodName = "checkServiceParam";
		Ente ente = req.getEnte();
		Bilancio bil = req.getBilancio();
		
		Liquidazione liq=req.getLiquidazione();
		
		String elencoParamentriNonInizializzati = "";

		if(null==ente){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
			}	
		}

		if(null==bil){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", BILANCIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "BILANCIO";
			}	
		}
		
		if(null==liq){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", LIQUIDAZIONE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "LIQUIDAZIONE";
			}	
		}
		
		
		boolean impegnoNonInizializzato = (liq.getImpegno() == null || (liq.getImpegno()!=null &&
				liq.getImpegno().getNumeroBigDecimal()==null && liq.getImpegno().getAnnoMovimento()<=0));
		
		log.debug(methodName, " impegno valorizzato: " +impegnoNonInizializzato);
		
		if(null!=liq && impegnoNonInizializzato){
			
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", IMPEGNO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "IMPEGNO";
			}	
		}
		
		if(null!=liq && null==liq.getAttoAmministrativoLiquidazione()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ATTO_AMMINISTRATIVO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ATTO_AMMINISTRATIVO";
			}	
		}
		
		
		
		if(null!=liq && null==liq.getSoggettoLiquidazione()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", SOGGETTO_LIQUIDAZIONE";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "SOGGETTO_LIQUIDAZIONE";
			}	
		}
		
		if(null!=liq && null!=liq.getSoggettoLiquidazione() && null==liq.getSoggettoLiquidazione().getModalitaPagamentoList()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_PAGAMENTO_SOGGETTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_PAGAMENTO_SOGGETTO";
			}	
		}
		
		if(null!=liq && null!=liq.getSoggettoLiquidazione() && null!=liq.getSoggettoLiquidazione().getModalitaPagamentoList() && liq.getSoggettoLiquidazione().getModalitaPagamentoList().isEmpty())
		{
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_PAGAMENTO_SOGGETTO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_PAGAMENTO_SOGGETTO";
			}	
		}
		
		if(null!=liq && null!=liq.getSoggettoLiquidazione() && null!=liq.getSoggettoLiquidazione().getModalitaPagamentoList() && !liq.getSoggettoLiquidazione().getModalitaPagamentoList().isEmpty())
			
		{
			if(liq.getSoggettoLiquidazione().getModalitaPagamentoList().get(0)==null)
			{
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_PAGAMENTO_SOGGETTO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_PAGAMENTO_SOGGETTO";
				}	
			}
		}
		
		
		if(elencoParamentriNonInizializzati.length() > 0){
			
			// stampo su log gli errori di validazione!
			log.debug(methodName, "Attenzione! si sono verificati i seguenti errori di validazione [ " + elencoParamentriNonInizializzati+ " ]");
			
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}	
}
