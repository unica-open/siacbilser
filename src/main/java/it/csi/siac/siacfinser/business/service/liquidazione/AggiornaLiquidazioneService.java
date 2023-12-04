/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneLiquidazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.integration.utility.threadlocal.UseClockTimeThreadLocal;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaLiquidazioneService extends AbstractBaseService<AggiornaLiquidazione, AggiornaLiquidazioneResponse> {

	private static final UseClockTimeThreadLocal USE_CLOCK_TIME = (UseClockTimeThreadLocal) ThreadLocalUtil.registerThreadLocal(UseClockTimeThreadLocal.class);
	
	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		final String methodName="AggiornaLiquidazioneService : init()";
		log.debug(methodName, " - Begin");
		
	}	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public AggiornaLiquidazioneResponse executeService(AggiornaLiquidazione serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		String methodName = "AggiornaLiquidazioneService - execute()";
		log.debug(methodName, " - Begin");
		
		USE_CLOCK_TIME.set(Boolean.TRUE);
			
		//Lettura variabili di input
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		
		setBilancio(req.getBilancio());
		
		String annoEsercizio = req.getAnnoEsercizio();
		Liquidazione liquidazione = req.getLiquidazione();
		
		//Gestione dati "freschi":
		Liquidazione liquidazioneReload = new Liquidazione();
		
		// Se il chiamante ha gia caricato la liquidazione non si ricarica 
		if(req.getRicaricaLiquidazione()){
			
			Liquidazione liquidazioneDaRicercare = new Liquidazione();
			liquidazioneDaRicercare.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
			liquidazioneDaRicercare.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
			
			DatiOperazioneDto datiOperazioneRicerca = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);
			
			
			liquidazioneReload = liquidazioneDad.ricercaLiquidazionePerChiave(liquidazioneDaRicercare,  CostantiFin.TIPO_RICERCA_DA_LIQUIDAZIONE, 
					richiedente,new Integer(annoEsercizio),CostantiFin.AMBITO_FIN, ente,datiOperazioneRicerca);
			
			//setto l'importo old in liquidazione perche' viene considerato per i controlli successivamente:
			liquidazione.setImportoLiquidazioneDaAggiornare(liquidazioneReload.getImportoAttualeLiquidazione());
		} else {
			liquidazioneReload = liquidazione;
		}
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, bilancio.getAnno());
		
		//Richiamo l'operazione interna di aggiornamento che esegue i controlli di merito ed aggiorna la liquidazione
		EsitoGestioneLiquidazioneDto esitoOperazioneInternaAggiornaLiq=liquidazioneDad.operazioneInternaAggiornaLiquidazione(ente, richiedente, annoEsercizio, bilancio, liquidazione, req.getElencoSubOrdinativi(),datiOperazione);
		
		if (esitoOperazioneInternaAggiornaLiq.getListaErrori()!=null && esitoOperazioneInternaAggiornaLiq.getListaErrori().size()>0) {
			res.setErrori(esitoOperazioneInternaAggiornaLiq.getListaErrori());
			res.setLiquidazione(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
		
		Liquidazione liquidazioneAggiornata = esitoOperazioneInternaAggiornaLiq.getLiquidazione();
		
		if(liquidazioneAggiornata!=null){
			res.setLiquidazione(liquidazioneAggiornata);
			res.setEsito(Esito.SUCCESSO);
			
			
			// Gestione innesto!
			if(liquidazioneReload.getStatoOperativoLiquidazione()!=null){
				
				// sono in aggiorna liquidazione e varia lo stato da PROVVISORIO a VALIDO, REGISTRO con LIQ-INS
				if(CostantiFin.statoOperativoLiquidazioneEnumToString(liquidazioneReload.getStatoOperativoLiquidazione()).equals(CostantiFin.LIQUIDAZIONE_STATO_PROVVISORIO) && 
						CostantiFin.statoOperativoLiquidazioneEnumToString(liquidazioneAggiornata.getStatoOperativoLiquidazione()).equals(CostantiFin.LIQUIDAZIONE_STATO_VALIDO)){
					
					if(liquidazioneReload.getSubdocumentoSpesa()!=null)
						liquidazioneAggiornata.setSubdocumentoSpesa(liquidazione.getSubdocumentoSpesa());
					
					liquidazioneAggiornata.setImpegno(liquidazioneReload.getImpegno());
					liquidazioneAggiornata.setUid(liquidazioneReload.getUid());				
					// il codice evento è LIQ-INS
					
					// Innesto registrazione fin -gen
					// SIAC-4066 (cr 297 - Gestione delle prima note a fronte di liquidazioni rilevate nell'esercizio le cui quote finanziarie sono su impegni residui)
					boolean registraPerImpegnoResiduo = liquidazioneAggiornata.getImpegno().getAnnoMovimento() < req.getBilancio().getAnno();
					String codiceEvento = "";				
					
					if(registraPerImpegnoResiduo)
						codiceEvento =  CodiceEventoEnum.INSERISCI_LIQUIDAZIONE_CON_IMPEGNO_RESIDUO.getCodice();
					else
						codiceEvento =  CodiceEventoEnum.INSERISCI_LIQUIDAZIONE.getCodice();
					
					gestisciRegistrazioneGENPerLiquidazione(liquidazioneAggiornata, codiceEvento);	
					
				}
				
				
				// sono in aggiorna liquidazione e varia lo stato da VALIDO a PROVVISORIO, annulla la registrazione della liquidazione 
				if(CostantiFin.statoOperativoLiquidazioneEnumToString(liquidazioneReload.getStatoOperativoLiquidazione()).equals(CostantiFin.LIQUIDAZIONE_STATO_VALIDO) && 
						CostantiFin.statoOperativoLiquidazioneEnumToString(liquidazioneAggiornata.getStatoOperativoLiquidazione()).equals(CostantiFin.LIQUIDAZIONE_STATO_PROVVISORIO)){
					
					annullaRegistrazioneEPrimaNotaLiquidazione(TipoCollegamento.LIQUIDAZIONE, liquidazioneAggiornata);
				}
			}
			
		} else {
			res.setLiquidazione(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="AggiornaLiquidazioneService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		
		//dati di input presi da request:
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
		
		if(null!=liq && null!=liq.getSoggettoLiquidazione() && null!=liq.getSoggettoLiquidazione().getModalitaPagamentoList() && !liq.getSoggettoLiquidazione().getModalitaPagamentoList().isEmpty()) {
			if(liq.getSoggettoLiquidazione().getModalitaPagamentoList().get(0)==null) {
				if(elencoParamentriNonInizializzati.length() > 0){
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", MODALITA_PAGAMENTO_SOGGETTO";
				}else{
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "MODALITA_PAGAMENTO_SOGGETTO";
				}	
			}
		}

		if (StringUtilsFin.isEmpty(req.getAnnoEsercizio())) {
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ANNO_ESERCIZIO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ANNO_ESERCIZIO";
			}	
		}

		if(null!=liq && null==liq.getAttoAmministrativoLiquidazione()){
			if(elencoParamentriNonInizializzati.length() > 0){
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ATTO_AMMINISTRATIVO";
			}else{
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ATTO_AMMINISTRATIVO";
			}	
		}
		
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}	
	}	
}
