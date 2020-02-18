/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.registroiva.AnnullaRegistrazioneIvaPagatiService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagatiResponse;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.AnnullaOrdinativoPagamentoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacgenser.model.TipoCollegamento;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaOrdinativoPagamentoService extends AbstractInserisceAggiornaAnnullaOrdinativoPagamentoService<AnnullaOrdinativoPagamento, AnnullaOrdinativoPagamentoResponse> {


	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	AnnullaRegistrazioneIvaPagatiService annullaRegistrazioneIvaPagatiService;
	
	private DatiOperazioneDto datiOperazioneAnnulla = new DatiOperazioneDto();

	@Override
	protected void init() {
		final String methodName = "AnnullaOrdinativoPagamentoService : init()";
		log.debug(methodName, "- Begin");
			
	}
	
	@Override
	@Transactional
	public AnnullaOrdinativoPagamentoResponse executeService(AnnullaOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	/**
	 * Per checkare se siamo nel caso di un ordinativo in reintroito
	 * @return
	 */
	private boolean daReintroito(){
		OrdinativoPagamento ordinativo = req.getOrdinativoPagamentoDaAnnullare();
		return ordinativoPagamentoDad.presenteRelezioneReintroito(ordinativo.getAnno(), ordinativo.getNumero(), Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneAnnulla);
	}
		
	@Override
	public void execute() {
		final String methodName = "AnnullaOrdinativoPagamentoService : execute()";
		log.debug(methodName, " - Begin");

		//1. Vengono letti i valori ricevuti in input dalla request
		setBilancio(req.getBilancio());
		Integer annoOrdinativoPagamento = req.getOrdinativoPagamentoDaAnnullare().getAnno();
		Integer numeroOrdinativoPagamento = req.getOrdinativoPagamentoDaAnnullare().getNumero();
		Richiedente richiedente = req.getRichiedente();
		boolean isDaReintroito = daReintroito();
		//2. Si effettuano i vari controlli di merito definiti in analisi:
		if(!isDaReintroito){
			//CONTROLLO DA SALTARE SE SONO IN REINTROITO
			boolean verificaCondizioni = verificaCondizioniPerAnnullaOrdinativoPagamento();
			if(!verificaCondizioni){
				return;
			}
		}
		
		//3. Si caricano i dati dell'ordinativo di pagamento per avere il riferimento prima dell'annullamento:
		RicercaOrdinativoPagamentoK pk = new RicercaOrdinativoPagamentoK();
		pk.setBilancio(bilancio);
		OrdinativoPagamento ordPag = new OrdinativoPagamento();
		ordPag.setAnno(annoOrdinativoPagamento);
		ordPag.setNumero(numeroOrdinativoPagamento);
		pk.setOrdinativoPagamento(ordPag);
		
		RicercaOrdinativoPerChiaveDto ordinativoPagamentoDto = ordinativoPagamentoDad.ricercaOrdinativoPagamento(pk, datiOperazioneAnnulla, richiedente);
		OrdinativoPagamento ordinativoPagamento = ordinativoPagamentoDto.getOrdinativoPagamento();
		
		//4. Si invoca il metodo "valutaSubOrdinativi", tale metodo veste un oggetto che servira' da "riassunto" per quanto riguarda i dati dei subordinativi 
		//in funzione della gestione in doppia gestione (va pero' fatto prima dell'annullamento per aver una "foto corretta" da dare al gestore della doppia gest)
		List<SubOrdinativoPagamento> elencoSubOrdinativiDiPagamento = new ArrayList<SubOrdinativoPagamento>();
		ordinativoPagamento.setElencoSubOrdinativiDiPagamento(elencoSubOrdinativiDiPagamento);
		SubOrdinativoInModificaInfoDto subOrdinativoInModificaInfoDto = ordinativoPagamentoDad.valutaSubOrdinativi(elencoSubOrdinativiDiPagamento, (Integer) ordinativoPagamento.getIdOrdinativo().intValue(), datiOperazioneAnnulla,
				bilancio, richiedente,Constanti.ORDINATIVO_TIPO_PAGAMENTO, ente);
		
		//5. annullmento ordinativo (si invoca il metodo "core" rispetto all'operazione di annullamento di un ordinativo):
		AnnullaOrdinativoPagamentoInfoDto annullaInfo = ordinativoPagamentoDad.annullaOrdinativoPagamento(bilancio.getAnno(), annoOrdinativoPagamento , numeroOrdinativoPagamento, datiOperazioneAnnulla, richiedente);
		
		//6. DOPPIA GESTIONE
		EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoPagamento, bilancio, richiedente, req.getEnte(), datiOperazioneAnnulla, subOrdinativoInModificaInfoDto,Operazione.ANNULLA);
		
		if(!isEmpty(resDg.getListaErrori())){
			//errori in doppia gestione:
			//Costruzione response esito negativo:
			res.setErrori(resDg.getListaErrori());
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
		
		//7. Annullamento liquidazione automatica
		ordinativoPagamentoDad.annullaLiquidazioneAutomatica(annullaInfo, datiOperazioneAnnulla, bilancio.getAnno(), richiedente);
		
		// CR - 1914 Annullamento documento di spesa (richiamare ser aggiorna documento di spesa)
		
		OrdinativoPagamento ordinativoPerAnnullaDocSpesa = ordinativoPagamentoDad.impostaOrdinativoPagPerAggiornaStatoDocSpesa(annullaInfo);
		//SIAC-6365
		boolean hasRegistrazioneIvaCollegata = false;
		
		for (SubOrdinativoPagamento subOrdinativoPagamento : ordinativoPerAnnullaDocSpesa.getElencoSubOrdinativiDiPagamento()) {
			AggiornaStatoDocumentoDiSpesa reqDoc = new AggiornaStatoDocumentoDiSpesa();
			
			if(subOrdinativoPagamento.getSubDocumentoSpesa()!=null){
				
				if(!hasRegistrazioneIvaCollegata &&  StringUtils.isNotBlank(subOrdinativoPagamento.getSubDocumentoSpesa().getNumeroRegistrazioneIVA())) {
					hasRegistrazioneIvaCollegata = true;
				}
				
				reqDoc.setDocumentoSpesa(subOrdinativoPagamento.getSubDocumentoSpesa().getDocumento());
				reqDoc.setRichiedente(richiedente);
				//SIAC-7350
				reqDoc.setBilancio(bilancio);
				reqDoc.setAnnoBilancio(bilancio.getAnno());
				
				log.debug(methodName, "Richiamo il ser Aggiorna stato documento di spesa per richiedente.account.uid: "+richiedente.getAccount().getUid());
				AggiornaStatoDocumentoDiSpesaResponse resDoc = executeExternalServiceSuccess(aggiornaStatoDocumentoDiSpesaService,reqDoc);
				if(resDoc.isFallimento() && resDoc.getErrori()!=null){
					log.debug(methodName, "Il servizio è andato in errore: "+resDoc.getErrori().get(0).getTesto());
				}
				
			}
			
		}
		
		if(!isDaReintroito && hasRegistrazioneIvaCollegata) {
			StringBuilder sb = new StringBuilder();
			sb.append("I subdocumenti collegati all'ordinativo presentano registrazioni iva collegate. ");
			AnnullaRegistrazioneIvaPagati reqIva = new AnnullaRegistrazioneIvaPagati();
			reqIva.setRichiedente(req.getRichiedente());
			reqIva.setAnnoBilancio(bilancio.getAnno());
			reqIva.setDataOra(new Date());
			reqIva.setOrdinativo(ordinativoPerAnnullaDocSpesa);
			AnnullaRegistrazioneIvaPagatiResponse resIva = annullaRegistrazioneIvaPagatiService.executeService(reqIva);
			boolean hasErrori = resIva.hasErrori();
			if(hasErrori) {
				sb.append("Annullamento registrazioni iva non possibile. Errori riscontrati: ");
				for (Errore errore : resIva.getErrori()) {
					sb.append(errore.getTesto()).append(",");
				}
			}
			boolean hasMessaggi = resIva.getMessaggi()!= null && !resIva.getMessaggi().isEmpty();
			if(hasMessaggi) {
				sb.append(" Messaggi: ");
				for (Messaggio messaggio : resIva.getMessaggi()) {
					sb.append(messaggio.getCodice()).append(" - ").append(messaggio.getDescrizione()).append(",");
				}
			}
			if(!hasErrori && ! hasMessaggi) {
				sb.append("Annullamento eseguito con successo.");
			}
			
			res.addMessaggio(new Messaggio("QUOTE_IVA_COLLEGATE: ", sb.toString()));
			
		}
		
		//DICEMBRE 2017 introdotta la possibilita' di escludere le registrazioni,
		//il chiamante (l'esigenza e' nata con il servizio di reintroito) che deve
		//orchestrare in maniera atomica piu servizi che possono fallire e metterci tempo si occupera'
		//di richiamare i gen solo dopo essere certo che siano terminati tutti correttamente
		if(req.isRegistraGen()){
			// innesto fin - gen
			annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento.ORDINATIVO_PAGAMENTO, ordinativoPagamento);
		}

	}
	
	/**
	 * Qui vengono implementati i controlli di merito relativi a questo servizio.
	 * Si tratta dei controlli per i quali e' necessario interrogare il database per validare certi dati input
	 * @return
	 */
	private boolean verificaCondizioniPerAnnullaOrdinativoPagamento()
	{
		// VERIFICA PRESENZA ORDINATIVI COLLEGATI:
		return checkOrdinativiCollegatiPerAnnulla(datiOperazioneAnnulla,
				req.getOrdinativoPagamentoDaAnnullare(), Constanti.D_ORDINATIVO_TIPO_PAGAMENTO);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaMutuoService : checkServiceParam()";
		log.debug(methodName, "- Begin");

		StringBuffer elencoParamentriNonInizializzati = new StringBuffer();
		
		// verifica parametri e loro corretta inizializzazione
		
		if(req.getEnte()==null || req.getRichiedente()==null){
			elencoParamentriNonInizializzati.append("ente o richiedente");
		}else {
			datiOperazioneAnnulla = commonDad.inizializzaDatiOperazione(req.getEnte(), req.getRichiedente(), Operazione.ANNULLA, null);
		}
		

		if(req.getBilancio()==null){
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("bilancio");
		}else if (req.getBilancio().getAnno()==0) {
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("anno bilancio");
		}
		
		if(req.getOrdinativoPagamentoDaAnnullare()==null){
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("Ordinativo Pagamento Da Annullare");
		}else if (req.getOrdinativoPagamentoDaAnnullare().getAnno()==null || req.getOrdinativoPagamentoDaAnnullare().getNumero()==null) {
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("Anno o Numero Ordinativo Pagamento Da Annullare");
		}
		// in caso ci siano restituisce l'elenco dei msg
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}
		
		if(!daReintroito()){
			//QUITANZATO NON VA CONTROLLATO SE SONO IN REINTROITO
			
			boolean isQuietanziato=ordinativoPagamentoDad.checkTipoOrdinativo(req.getOrdinativoPagamentoDaAnnullare().getAnno(), req.getOrdinativoPagamentoDaAnnullare().getNumero(), StatoOperativoOrdinativo.QUIETANZATO, Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneAnnulla);
			
			if (isQuietanziato) {
				checkCondition(false, ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("QUIETANZATO","ANNULLATO","Ordinativo Pagamento"));
			}
			
		}
		
		boolean isAnnullato=ordinativoPagamentoDad.checkTipoOrdinativo(req.getOrdinativoPagamentoDaAnnullare().getAnno(), req.getOrdinativoPagamentoDaAnnullare().getNumero(), StatoOperativoOrdinativo.ANNULLATO, Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazioneAnnulla);
		
		if (isAnnullato) {
			checkCondition(false, ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("ANNULLATO","ANNULLATO","Ordinativo Pagamento"));
		}
	}	
}
