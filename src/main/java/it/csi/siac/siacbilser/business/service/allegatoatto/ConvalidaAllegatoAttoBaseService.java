/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.LiquidazioneBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulare;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulareResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

/**
 * Service di base per la Convalida di un allegato atto.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class ConvalidaAllegatoAttoBaseService<REQ extends ConvalidaAllegatoAtto,RES extends ConvalidaAllegatoAttoResponse> extends CheckedAccountBaseService<REQ,RES> {

	//DADs..
	@Autowired
	protected AllegatoAttoDad allegatoAttoDad;
	@Autowired
	protected SubdocumentoDad subdocumentoDad;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired 
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private LiquidazioneBilDad liquidazioneBilDad;
	
	//Services..
	@Autowired
	private LiquidazioneService liquidazioneService;
	
	//Fields..
	//protected Ente ente;
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		subdocumentoEntrataDad.setEnte(ente);
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	

	/**
	 * Imposta flag convalida manuale con quello passato in request.
	 *
	 * @param subdocumenti the subdocumenti
	 */
	protected void impostaFlagConvalidaManuale(List<Subdocumento<?, ?>> subdocumenti) {
		for(Subdocumento<?, ?> subdoc : subdocumenti){
			subdoc.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		}
	}
	
	/**
	 * Carica il subdocumento con dati di base.
	 *
	 * @param subdocReq the subdoc req
	 * @return the subdocumento
	 */
	protected Subdocumento<?, ?> caricaSubdocumentoConDatiDiBase(Subdocumento<?, ?> subdocReq) {
		Subdocumento<?, ?> subdoc = null;
		String logMsg = "";
		if(subdocReq instanceof SubdocumentoSpesa){
			subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaBaseById(subdocReq.getUid());
			logMsg = "Spesa";
		}else if(subdocReq instanceof SubdocumentoEntrata){
			subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataBaseById(subdocReq.getUid());
			logMsg = "Entrata";
		} else {
			throw new IllegalArgumentException("Tipo di Subdocumento non supportato.");
		}
		
		if(subdoc==null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Subdocumento di "+ logMsg, "uid: "+ subdocReq.getUid()));
		}
		
		return subdoc;
		
	}

	/**
	 * SE  statoOperativoAllegatoAtto <> C – COMPLETATO o PC – PARZIALMENTE CONVALIDATO
	 * Allora  l’allegato deve essere scartato con la motivazione  <FIN_ERR_0226, Stato Allegato Atto incongruente>.
	 *
	 * @param aa the allegatoAtto
	 */
	protected void checkStatoOperativoAllegatoAttoCompletato(AllegatoAtto aa) {
		final String methodName = "checkStatoOperativoAllegatoAttoCompletato";
		
		if(!StatoOperativoAllegatoAtto.COMPLETATO.equals(aa.getStatoOperativoAllegatoAtto()) 
				&& !StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto())) {
			
			log.error(methodName, "Stato non valido: " + aa.getStatoOperativoAllegatoAtto());
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	
	/**
	 * Ottiene i dati dell'allegato atto il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the allegato atto
	 */
	protected AllegatoAtto caricaAllegatoAtto(Integer uid) {
		final String methodName = "caricaAllegatoAtto";
		
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(uid);
		
		if(aa == null) {
			log.error(methodName, "Nessun allegato atto con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid: "+ uid));
		}
		return aa;
	}
	
	/**
	 * Ottiene i dati dell'elenco.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	protected ElencoDocumentiAllegato caricaElencoDocumentiAllegato(Integer uid) {
		final String methodName = "caricaElencoDocumentiAllegato";
		
		ElencoDocumentiAllegato eda = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoById(uid);
		if(eda == null) {
			log.debug(methodName, "Nessun elenco documenti con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("elenco docuementi ", "uid: "+ uid));
		}
		return eda;
	}

	/**
	 * Il sistema deve aggiornare 
	 * Liquidazione.tipoConvalida come da parametro di input
	 * Liquidazione.Stato Operativo Elemento Bilancio = V
	 *
	 * @param subdoc the subdoc
	 */
	protected void aggiornaLiquidazioneModulare(SubdocumentoSpesa subdoc) {
		
		Liquidazione liqDaAggiornare = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(subdoc);
		if(liqDaAggiornare == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessuna liquidazione collegata al subdocumento " + subdoc.getUid()));
		}
		Bilancio bilancio = liquidazioneBilDad.findBilancioAssociatoALiquidazione(liqDaAggiornare.getUid());
		
		//Imposto lo stato a VALIDO
		liqDaAggiornare.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
		//Modifico il tipo convalida coerentemente con il subdocumetno
		liqDaAggiornare.setLiqManuale(Boolean.TRUE.equals(subdoc.getFlagConvalidaManuale())?Constanti.LIQUIDAZIONE_MAUALE:Constanti.LIQUIDAZIONE_AUTOMATICA);
		
		liqDaAggiornare.setSubdocumentoSpesa(subdoc);
		
		//Aggiorno
		AggiornaLiquidazioneModulare reqALM = new AggiornaLiquidazioneModulare();
		reqALM.setRichiedente(req.getRichiedente());
		reqALM.setBilancio(bilancio);
		reqALM.setDataOra(req.getDataOra());
		reqALM.setEnte(ente);
		reqALM.setLiquidazione(liqDaAggiornare);
		reqALM.setFlagAggiornaFlagManuale(true);
		reqALM.setFlagAggiornaStato(true);
		
		AggiornaLiquidazioneModulareResponse resALM = liquidazioneService.aggiornaLiquidazioneModulare(reqALM);
		checkServiceResponseFallimento(resALM);
		
		
	}

	/**
	 * Imposta la convalida per il subdocumento.
	 * Il sistema deve aggiornare SubDocumento di Entrata.tipoConvalida come da parametro di input
	 *
	 * @param subdoc the subdoc
	 */
	protected void aggiornaTipoConvalida(Subdocumento<?, ?> subdoc) {
		subdocumentoDad.updateFlagConvalidaManuale(subdoc);		
	}

	/**
	 * SE  una quota &egrave; collegata ad un documento di Spesa con intestatario sospeso 
	 * (ovvero il soggetto ha valorizzati ‘DatiSoggettoAllegato’, ma con dataRiattivazione nulla) 
	 * Allora il sub-documento deve essere scartato con la motivazione
	 * < FIN_ERR_0143	Convalida non possibile (motivazione= ‘sub-documento collegato a soggetto sospeso')>.
	 *
	 * @param allegato the allegato atto
	 * @param subdoc the subdoc
	 */
	protected void checkSoggettoSospeso(AllegatoAtto allegato, Subdocumento<?, ?> subdoc) {
		DatiSoggettoAllegato dsa = allegatoAttoDad.findDatiSoggettoAllegatoByAllegatoAndBySubdocId(allegato.getUid(),subdoc.getUid());
		
		if(dsa!=null && dsa.getDataSospensione()!=null 
				&& (dsa.getDataRiattivazione()==null || dsa.getDataRiattivazione().after(new Date()))){
			throw new BusinessException(ErroreFin.CONVALIDA_NON_POSSIBILE.getErrore("Subdocumento numero " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+" collegato a soggetto sospeso"));
		}
	}

	/**
	 * SE tipoConvalida non &egrave; nullo  allora il sub-documento deve essere scartato con la motivazione
	 * < FIN_ERR_0143	Convalida non possibile (motivazione= ‘sub-documento già convalidato')>.
	 *
	 * @param subdoc the subdoc
	 */
	protected void checkTipoConvalida(Subdocumento<?, ?> subdoc) {
		if (subdoc.getFlagConvalidaManuale() != null) {
			throw new BusinessException(ErroreFin.CONVALIDA_NON_POSSIBILE.getErrore("Subdocumento numero " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " gia' convalidato"));
		}
		if(Boolean.FALSE.equals(subdoc.getFlagConvalidaManuale()) && subdoc instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdocSpesa= (SubdocumentoSpesa) subdoc;
			if(subdocSpesa.getDatiCertificazioneCrediti() != null && subdocSpesa.getDatiCertificazioneCrediti().getNumeroCertificazione() != null){
				throw new BusinessException(ErroreFin.CONVALIDA_NON_POSSIBILE.getErrore("Subdocumento numero " + subdoc.getNumero()
						+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
						+ "con dati certificazione credito"));
			}
		}
	}

	/**
	 * SE  non sono superati i controlli sui subordinati (vedi par. 2.5.5)
	 * Allora  l’elenco deve essere scartato con la motivazione  < FIN_ERR_0280 Documenti subordinati non presenti>.
	 * <br>
	 * Data la lista dei documenti e rispettive quote oggetto della funzione, verificare che: 
	 * <ul>
	 * <li>per ciascun documento che possiede una relazione di tipo Sub-Subordinato 
	 * in cui &egrave; padre della relazione devono essere presenti nella lista i documenti figli della relazione</li>
	 *  <li>per ciascun documento che possiede una relazione di tipo Sub-Subordinato 
	 * in cui &egrave; figlio della relazione devono essere presenti nella lista i documenti figli della relazione</li>
	 * </ul>
	 * In caso di errore inviare il messaggio di errore < FIN_ERR_0280 Documenti subordinati non presenti >. 
	 * <br>
	 * 
	 * @param elencoDocumentiAllegato elenco presente sulla base dati
	 * @param elencoDocumentiAllegatoReq elenco passato nella request del servizio
	 */
	
	
	protected void checkPresenzaSubordinati(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		final String methodName = "checkPresenzaSubordinati";
		List<Subdocumento<?,?>> subdocumentiSubordinati = elencoDocumentiAllegatoDad.findSubdocDocSubordinati(elencoDocumentiAllegato.getSubdocumenti());
		
		for(Subdocumento<?,?> subdocSubordinato : subdocumentiSubordinati){
			if(!isContenutoInElenco(elencoDocumentiAllegato, subdocSubordinato)){
				log.error(methodName, "il subdoc con uid " + subdocSubordinato.getUid() + " NON è presente nell'elenco allegato atto");
				Documento<?, ?> documento = subdocSubordinato.getDocumento();
				String key = documento.getTipoDocumento().getCodice() + "/" + documento.getAnno() + "/" + documento.getNumero();
				
				//SIAC-4752 - NON sollevo piu' l'eccezione ma aggiungo un messaggio di WARNING.
				//throw new BusinessException(ErroreFin.DOCUMENTI_SUBORDINATI_NON_PRESENTI.getErrore(key));
			 	
				Errore errore = ErroreFin.DOCUMENTI_SUBORDINATI_NON_PRESENTI.getErrore(key);
				res.addMessaggio(errore.getCodice(), errore.getDescrizione()+". L'elenco verra' comunque convalidato.");
			}
		}
	
	}


	/**
	 * Controlla che il subdocumento sia contenuto nell'elenco 
	 * 
	 * @param elencoDocumentiAllegato
	 * @param subdocumento subdocumento da cercare
	 * @return
	 */
	private boolean isContenutoInElenco(ElencoDocumentiAllegato elencoDocumentiAllegato, Subdocumento<?, ?> subdocumento) {
		boolean subdocContenuto = false;
		for(Subdocumento<?,?> subdocElenco : elencoDocumentiAllegato.getSubdocumenti()){
			if(subdocumento.getUid() == subdocElenco.getUid()){
				subdocContenuto = true;
				break;
			}
		}
		return subdocContenuto;
	}

	/**
	 * SE  statoOperativoElenco <> C – COMPLETATO
	 * Allora  l’elenco deve essere scartato con la motivazione  <COR_ERR_0028 Operazione incompatibile con stato dell'entità (entità:elenco  stato: statoOperativo)>.
	 *
	 * @param elenco the elenco
	 */
	protected void checkStatoElenco(ElencoDocumentiAllegato elenco) {
		String methodName = "checkStatoElenco";
		if(!StatoOperativoElencoDocumenti.COMPLETATO.equals(elenco.getStatoOperativoElencoDocumenti())){
			log.error(methodName, "Stato non valido: " + elenco.getStatoOperativoElencoDocumenti()
					+ ". Atteso " + StatoOperativoElencoDocumenti.COMPLETATO);
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("elenco " + elenco.getAnno() + "/" + elenco.getNumero() ,  elenco.getStatoOperativoElencoDocumenti()));
		}
	}


	/**
	 * Determina
	 * Stato Operativo Atto in Convalida = PC o CV a seconda che tutte 
	 * le sue quote siano state convalidate
	 * 
	 * SE dopo l’elaborazione tutti i sub-documenti sotto l’allegato sono convalidati (tipoConvalida non nullo) 
	 * Stato Operativo Atto in Convalida = CV
	 * SE non tutti  i sub-documenti sotto l’allegato sono convalidati Stato Operativo Atto in Convalida = PC   
	 * @param aa the allegatoAtto
	 *  
	 */
	protected StatoOperativoAllegatoAtto determinaStatoOperativoAllegatoAtto(AllegatoAtto aa) {
		String methodName = "determinaStatoOperativoAllegatoAtto";
		
		Long quoteInElenco = aa.countQuoteInElenco();
		Long quoteConvalidate = subdocumentoDad.countQuoteConvalidateByAllegatoAtto(aa);
		log.debug(methodName, "quote in elenco: " + quoteInElenco);
		log.debug(methodName, "quote convalidate" + quoteConvalidate);
		
		if(quoteConvalidate.equals(Long.valueOf(0))) {
			return StatoOperativoAllegatoAtto.COMPLETATO;
		}
		if(quoteConvalidate.equals(quoteInElenco)){
			return StatoOperativoAllegatoAtto.CONVALIDATO;
		}
		return StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO;
		
	}
		
	
	
	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto per l'AllegatoAtto passato come parametro.
	 *
	 * @param aa the allegatoAtto
	 */
	protected void aggiornaStatoOperativoAllegatoAtto(AllegatoAtto aa) {
		String methodName = "aggiornaStatoOperativoAllegatoAtto";
		StatoOperativoAllegatoAtto stato = determinaStatoOperativoAllegatoAtto(aa);
		log.info(methodName, "stato AllegatoAtto.uid = " + aa.getUid() + " da impostare a " + stato);
		allegatoAttoDad.aggiornaStatoAllegatoAtto(aa.getUid(), stato);
	}





}
