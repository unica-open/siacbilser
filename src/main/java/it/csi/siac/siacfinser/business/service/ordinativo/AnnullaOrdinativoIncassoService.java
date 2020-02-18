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

import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.registroiva.AnnullaRegistrazioneIvaPagatiService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagatiResponse;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoIncassoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.AnnullaOrdinativoIncassoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaOrdinativoIncassoService extends AbstractInserisceAggiornaAnnullaOrdinativoIncassoService<AnnullaOrdinativoIncasso, AnnullaOrdinativoIncassoResponse> {

	@Autowired
	OrdinativoIncassoDad ordinativoIncassoDad;
	
	@Autowired
	AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	@Autowired
	AnnullaRegistrazioneIvaPagatiService annullaRegistrazioneIvaPagatiService;
	
	@Autowired
	CommonDad commonDad;
	
	private DatiOperazioneDto datiOperazioneAnnulla = new DatiOperazioneDto();

	@Override
	protected void init() {
		final String methodName = "AnnullaOrdinativoIncassoService : init()";
		log.debug(methodName, "- Begin");
			
	}
	
	@Override
	@Transactional
	public AnnullaOrdinativoIncassoResponse executeService(AnnullaOrdinativoIncasso serviceRequest) {
		return super.executeService(serviceRequest);
	}	
		
	@Override
	public void execute() {
		final String methodName = "AnnullaOrdinativoIncassoService : execute()";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request
		Ente ente = req.getEnte();
		setBilancio(req.getBilancio());
		Integer annoOrdinativoIncasso = req.getOrdinativoIncassoDaAnnullare().getAnno();
		Integer numeroOrdinativoIncasso = req.getOrdinativoIncassoDaAnnullare().getNumero();
		Richiedente richiedente = req.getRichiedente();
		
		//2. Si effettuano i vari controlli di merito definiti in analisi:
		boolean verificaCondizioni = verificaCondizioniPerAnnullaOrdinativoIncasso();
		if(!verificaCondizioni){
			return;
		}
		
		
		//3. Si caricano i dati dell'ordinativo di incasso per avere il riferimento prima dell'annullamento:
		RicercaOrdinativoIncassoK pk = new RicercaOrdinativoIncassoK();
		pk.setBilancio(bilancio);
		OrdinativoIncasso ordPag = new OrdinativoIncasso();
		ordPag.setAnno(annoOrdinativoIncasso);
		ordPag.setNumero(numeroOrdinativoIncasso);
		pk.setOrdinativoIncasso(ordPag);
		
		RicercaOrdinativoPerChiaveDto ordinativoIncassoDto = ordinativoIncassoDad.ricercaOrdinativoIncasso(pk, datiOperazioneAnnulla, richiedente);
		OrdinativoIncasso ordinativoIncasso = ordinativoIncassoDto.getOrdinativoIncasso();
		
		//4. Si invoca il metodo "valutaSubOrdinativi", tale metodo veste un oggetto che servira' da "riassunto" per quanto riguarda i dati dei subordinativi 
		//in funzione della gestione in doppia gestione (va pero' fatto prima dell'annullamento per aver una "foto corretta" da dare al gestore della doppia gest)
		List<SubOrdinativoIncasso> elencoSubOrdinativiDiIncasso = new ArrayList<SubOrdinativoIncasso>();
		ordinativoIncasso.setElencoSubOrdinativiDiIncasso(elencoSubOrdinativiDiIncasso);
		SubOrdinativoInModificaInfoDto subOrdinativoInModificaInfoDto = ordinativoIncassoDad.valutaSubOrdinativi(elencoSubOrdinativiDiIncasso, (Integer) ordinativoIncasso.getIdOrdinativo().intValue(),
				datiOperazioneAnnulla, bilancio, richiedente,Constanti.ORDINATIVO_TIPO_INCASSO, ente);
		
		//5. annullmento ordinativo (si invoca il metodo "core" rispetto all'operazione di annullamento di un ordinativo):
		AnnullaOrdinativoIncassoInfoDto annullaInfo = ordinativoIncassoDad.annullaOrdinativoIncasso(bilancio, ente, annoOrdinativoIncasso, numeroOrdinativoIncasso, datiOperazioneAnnulla, richiedente);
		
		//6. DOPPIA GESTIONE
		//si invoca l'operazione di doppia gestione vera e propria:
		EsitoControlliDto resDg = operazioniPerDoppiaGestione(ordinativoIncasso, bilancio, richiedente, req.getEnte(), datiOperazioneAnnulla, subOrdinativoInModificaInfoDto,Operazione.ANNULLA);
		
		if(!isEmpty(resDg.getListaErrori())){
			//errori in doppia gestione:
			//Costruzione response esito negativo:
			res.setErrori(resDg.getListaErrori());
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
		
		// CR - 1914 Annullamento documento di spesa (richiamare ser aggiorna documento di spesa)
		OrdinativoIncasso ordinativoPerAnnullaDocEntrata = ordinativoIncassoDad.impostaOrdinativoIncPerAggiornaStatoDocEntrata(annullaInfo);
		//SIAC-6365
		boolean hasRegistrazioneIvaCollegata = false;
		if(ordinativoPerAnnullaDocEntrata!=null && ordinativoPerAnnullaDocEntrata.getElencoSubOrdinativiDiIncasso()!=null
				&& !ordinativoPerAnnullaDocEntrata.getElencoSubOrdinativiDiIncasso().isEmpty()){
			
			
			
			
			for (SubOrdinativoIncasso subOrdinativoIncasso : ordinativoPerAnnullaDocEntrata.getElencoSubOrdinativiDiIncasso()) {
				AggiornaStatoDocumentoDiEntrata reqDoc = new AggiornaStatoDocumentoDiEntrata();
				
				if(subOrdinativoIncasso.getSubDocumentoEntrata()!=null){
					
					if(!hasRegistrazioneIvaCollegata &&  StringUtils.isNotBlank(subOrdinativoIncasso.getSubDocumentoEntrata().getNumeroRegistrazioneIVA())) {
						hasRegistrazioneIvaCollegata = true;
					}
					
					reqDoc.setDocumentoEntrata(subOrdinativoIncasso.getSubDocumentoEntrata().getDocumento());
					reqDoc.setRichiedente(richiedente);
					//SIAC-7350
					reqDoc.setBilancio(bilancio);
					reqDoc.setAnnoBilancio(bilancio.getAnno());
					log.debug(methodName, "Richiamo il ser Aggiorna stato documento di entrata per richiedente.account.uid: "+richiedente.getAccount().getUid());
					AggiornaStatoDocumentoDiEntrataResponse resDoc = executeExternalServiceSuccess(aggiornaStatoDocumentoDiEntrataService,reqDoc);
					if(resDoc.isFallimento() && resDoc.getErrori()!=null){
						log.debug(methodName,"Il servizio è andato in errore: "+resDoc.getErrori().get(0).getTesto());
					}
					
				}
				
			}
		}
		
		if( hasRegistrazioneIvaCollegata) {
			StringBuilder sb = new StringBuilder();
			sb.append("I subdocumenti collegati all'ordinativo presentano registrazioni iva collegate. ");
			AnnullaRegistrazioneIvaPagati reqIva = new AnnullaRegistrazioneIvaPagati();
			reqIva.setRichiedente(req.getRichiedente());
			reqIva.setAnnoBilancio(bilancio.getAnno());
			reqIva.setDataOra(new Date());
			reqIva.setOrdinativo(ordinativoPerAnnullaDocEntrata);
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
		
		
		// innesto fin - gen
		annullaRegistrazioneEPrimaNotaOrdinativo(TipoCollegamento.ORDINATIVO_INCASSO, ordinativoIncasso);
	}
	
	/**
	 * Qui vengono implementati i controlli di merito relativi a questo servizio.
	 * Si tratta dei controlli per i quali e' necessario interrogare il database per validare certi dati input
	 * @return
	 */
	private boolean verificaCondizioniPerAnnullaOrdinativoIncasso()
	{
		// VERIFICA PRESENZA ORDINATIVI COLLEGATI:
		return checkOrdinativiCollegatiPerAnnulla(datiOperazioneAnnulla,
				req.getOrdinativoIncassoDaAnnullare(), Constanti.D_ORDINATIVO_TIPO_INCASSO);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaMutuoService : checkServiceParam()";
		log.debug(methodName, "- Begin");

		StringBuffer elencoParamentriNonInizializzati = new StringBuffer();
		
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
		
		if(req.getOrdinativoIncassoDaAnnullare()==null){
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("Ordinativo Incasso Da Annullare");
		}else if (req.getOrdinativoIncassoDaAnnullare().getAnno()==null || req.getOrdinativoIncassoDaAnnullare().getNumero()==null) {
			if (elencoParamentriNonInizializzati.length()>0) {
				elencoParamentriNonInizializzati.append(", ");
			}
			elencoParamentriNonInizializzati.append("Anno o Numero Ordinativo Incasso Da Annullare");
		}
		
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));
		}
		
		boolean isQuietanziato=ordinativoIncassoDad.checkTipoOrdinativo(req.getOrdinativoIncassoDaAnnullare().getAnno(), req.getOrdinativoIncassoDaAnnullare().getNumero(), StatoOperativoOrdinativo.QUIETANZATO, Constanti.D_ORDINATIVO_TIPO_INCASSO,datiOperazioneAnnulla);
		
		if (isQuietanziato) {
			checkCondition(false, ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("QUIETANZATO","ANNULLATO","Ordinativo Incasso"));
		}
		
		boolean isAnnullato=ordinativoIncassoDad.checkTipoOrdinativo(req.getOrdinativoIncassoDaAnnullare().getAnno(), req.getOrdinativoIncassoDaAnnullare().getNumero(), StatoOperativoOrdinativo.ANNULLATO, Constanti.D_ORDINATIVO_TIPO_INCASSO, datiOperazioneAnnulla);
		
		if (isAnnullato) {
			checkCondition(false, ErroreFin.STATO_MOVIMENTO_IMPOSSIBILE.getErrore("ANNULLATO","ANNULLATO","Ordinativo Incasso"));
		}
		
	}	

}
