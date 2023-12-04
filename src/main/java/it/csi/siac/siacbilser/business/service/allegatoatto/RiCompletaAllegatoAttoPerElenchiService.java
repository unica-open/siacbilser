/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.LiquidazioneBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulare;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulareResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

/**
 * Gestisce la RiCompleta di uno o più elenchi o parte di essi.
 * 
 * @author Nazha Ahmad, Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RiCompletaAllegatoAttoPerElenchiService extends CheckedAccountBaseService<RiCompletaAllegatoAttoPerElenchi,RiCompletaAllegatoAttoPerElenchiResponse> {

	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired 
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private LiquidazioneBilDad liquidazioneBilDad;
	
	@Autowired
	private LiquidazioneService liquidazioneService;
	
	private AllegatoAtto allegatoAtto;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//this.allegatoAtto = req.getAllegatoAtto(); NON devo distruggere la request!
		
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkNotNull(req.getAllegatoAtto().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente allegato atto"));
		checkCondition(req.getAllegatoAtto().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente allegato atto"), false);
		this.ente = req.getAllegatoAtto().getEnte();
		
		checkNotNull(req.getAllegatoAtto().getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
		checkCondition(!req.getAllegatoAtto().getElenchiDocumentiAllegato().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
		
	}
	
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
	
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public RiCompletaAllegatoAttoPerElenchiResponse executeServiceTxRequiresNew(RiCompletaAllegatoAttoPerElenchi serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	
	@Override
	@Transactional
	public RiCompletaAllegatoAttoPerElenchiResponse executeService(RiCompletaAllegatoAttoPerElenchi serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {
		String methodName = "execute";
		
		this.allegatoAtto = caricaAllegatoAtto(req.getAllegatoAtto().getUid());
		stampaDettaglioOperazione();
		checkStatoOperativoAllegatoAttoConvalidato(allegatoAtto);
			
		//Per ogni elenco passato...
		for(ElencoDocumentiAllegato elencoDocumentiAllegatoReq : req.getAllegatoAtto().getElenchiDocumentiAllegato()){
			ElencoDocumentiAllegato elencoDocumentiAllegato = caricaElencoDocumentiAllegato(elencoDocumentiAllegatoReq.getUid());

			try{
				checkStatoElenco(elencoDocumentiAllegato);
			} catch (BusinessException be){
				log.info(methodName, "elencoDocumentiAllegato scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
						+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
				res.addErrore(be.getErrore());
				res.getElenchiScartati().add(elencoDocumentiAllegato);
				continue; //L'elenco viene scartato. Si continua con il prossimo.
			}
			
			//A differenza del convalida, qui non posso scegliere le quote, devo prendere tutte quelle dell'elenco
//			L’operazione dovra':
//			-Aggiornare i sub-movimenti di entrata
//			  tipoConvalida = NULLO
//			-Aggiornare le liquidazioni legate ai sub-movimenti di spesa
//			  tipoConvalida = NULLO
//			  Stato Operativo = Provvisorio
//			-Aggiornare Allegato Atto
//			  Stato Operativo Atto in Convalida = PC o C a seconda che tutte le sue quote siano state riportate allo stato precedente
			List<Subdocumento<?, ?>> subdocumenti = elencoDocumentiAllegato.getSubdocumenti();
			for(Subdocumento<?, ?> subdocReq : subdocumenti){
				Subdocumento<?, ?> subdoc = caricaSubdocumentoConDatiDiBase(subdocReq);
				try{
					checkTipoConvalidaEOrdinativoQuota(subdoc);
				} catch (BusinessException be){
					log.info(methodName, "subdocumento scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
							+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
					res.addErrore(be.getErrore());
					res.getSubdocumentiScartati().add(subdoc);
					continue; //Il subdocumento viene scartato. Si continua con il prossimo.
				}

				aggiornaTipoConvalida(subdoc);
				if(subdoc instanceof SubdocumentoSpesa){
					aggiornaLiquidazioneModulare((SubdocumentoSpesa)subdoc);
				}
				res.incCountQuoteRiCompletate();
			}
		}
		
		aggiornaStatoOperativoAllegatoAtto();

	}
	
	
	//-------------------------CARICAMENTI---------------------------------------
	
	/**
	 * Ottiene i dati dell'allegato atto il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the allegato atto
	 */
	private AllegatoAtto caricaAllegatoAtto(Integer uid) {
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
	private ElencoDocumentiAllegato caricaElencoDocumentiAllegato(Integer uid) {
		final String methodName = "caricaElencoDocumentiAllegato";
		
		ElencoDocumentiAllegato eda = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoById(uid);
		if(eda == null) {
			log.debug(methodName, "Nessun elenco documenti con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("elenco docuementi ", "uid: "+ uid));
		}
		return eda;
	}
	
	/**
	 * Carica il subdocumento con dati di base.
	 *
	 * @param subdocReq the subdoc req
	 * @return the subdocumento
	 */
	private Subdocumento<?, ?> caricaSubdocumentoConDatiDiBase(Subdocumento<?, ?> subdocReq) {
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
	
	
	//-------------------------CHECK--------------------------------------------
	
	/**
	 * SE  statoOperativoAllegatoAtto <>  CV – CONVALIDATO o PC – PARZIALMENTE CONVALIDATO 
	 * Allora  l’allegato deve essere scartato con la motivazione  <FIN_ERR_0226, Stato Allegato Atto incongruente>.
	 *
	 * @param aa the allegatoAtto
	 */
	private void checkStatoOperativoAllegatoAttoConvalidato(AllegatoAtto aa) {
		final String methodName = "checkStatoOperativoAllegatoAttoCompletato";
		log.debug(methodName, "stato :" + aa.getStatoOperativoAllegatoAtto() );
		
		if(!StatoOperativoAllegatoAtto.CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto()) 
				&& !StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto())) {
			
			log.error(methodName, "Stato non valido: " + aa.getStatoOperativoAllegatoAtto());
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	/**
	 * SE  statoOperativoElenco <> C – COMPLETATO
	 * Allora  l’elenco deve essere scartato con la motivazione  <COR_ERR_0028 Operazione incompatibile con stato dell'entità (entità:elenco  stato: statoOperativo)>.
	 *
	 * @param elenco the elenco
	 */
	private void checkStatoElenco(ElencoDocumentiAllegato elenco) {
		String methodName = "checkStatoElenco";
		log.debug(methodName, "STATO ELENCO: " + elenco.getStatoOperativoElencoDocumenti());
		if(!StatoOperativoElencoDocumenti.COMPLETATO.equals(elenco.getStatoOperativoElencoDocumenti())){
			log.error(methodName, "Stato non valido: " + elenco.getStatoOperativoElencoDocumenti()
					+ ". Atteso " + StatoOperativoElencoDocumenti.COMPLETATO);
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("elenco " + elenco.getAnno() + "/" + elenco.getNumero() ,  elenco.getStatoOperativoElencoDocumenti()));
		}
	}
	
	/**
	 *SE tipoConvalida è nullo O  SE la quota è collegata a Ordinativo non annullato 
	 *allora il sub-documento deve essere scartato con la motivazione
 	 *<FIN_ERR_0141, Documento non aggiornabile perché stato incongruente> 
	 * @param subdoc the subdoc
	 */
	private void checkTipoConvalidaEOrdinativoQuota(Subdocumento<?, ?> subdoc) {
		String methodName = "checkTipoConvalidaEOrdinativoQuota";
		if(subdoc.getFlagConvalidaManuale() == null){
			log.debug(methodName, "quota da scartare perche' non ancora convalidata");
			throw new BusinessException(ErroreFin.DOCUMENTO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
		}
		if(subdocumentoDad.isSubdocCollegatoAdOrdinativoValido(subdoc)){
			log.debug(methodName, "quota da scartare perche' convalidata ma gia' collegata ad ordinativo");
			throw new BusinessException(ErroreFin.DOCUMENTO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
		}
	}
	
	
	//-------------------------AGGIORNAMENTI---------------------------------------------
	
	private void aggiornaLiquidazioneModulare(SubdocumentoSpesa subdoc) {
		
		Liquidazione liqDaAggiornare = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(subdoc);
		Bilancio bilancio = liquidazioneBilDad.findBilancioAssociatoALiquidazione(liqDaAggiornare.getUid());
		
		//Imposto lo stato a PROVVISORIO
		liqDaAggiornare.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
		//Modifico il tipo convalida a null
		liqDaAggiornare.setLiqManuale(null);
		
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
	 * Il sistema deve aggiornare SubDocumento di Entrata.tipoConvalida NULLO 
	 *
	 * @param subdoc the subdoc
	 */
	private void aggiornaTipoConvalida(Subdocumento<?, ?> subdoc) {
		subdoc.setFlagConvalidaManuale(null);
		subdocumentoDad.updateFlagConvalidaManuale(subdoc);		
	}
	
	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto per l'AllegatoAtto passato come parametro.
	 *
	 * @param aa the allegatoAtto
	 * @param stato the stato
	 */
	private void aggiornaStatoOperativoAllegatoAtto() {
		StatoOperativoAllegatoAtto stato = determinaStato();
		String methodName = "aggiornaStatoOperativoAllegatoAtto";
		log.info(methodName, "stato AllegatoAtto.uid = " + allegatoAtto.getUid() + " da impostare a " + stato);
		allegatoAttoDad.aggiornaStatoAllegatoAtto(allegatoAtto.getUid(), stato);
	}
	
	/**
	 * Aggiornare Allegato Atto con 
	 * SE dopo l’elaborazione nessuno dei sub-documenti sotto l’allegato è convalidato 
	 * (hanno tutti tipoConvalida nullo)  Stato Operativo Atto da RiCompletare = C - Completato 
	 * SE non alcuni sub-documenti sotto l’allegato sono convalidati Stato Operativo Atto da RiCompletare  = PC – Parzialmente Convalidato 
	 * @param quoteRiCompletate
	 * @param quoteInElenco 
	 *  
	 */
	private StatoOperativoAllegatoAtto determinaStato() {
		 
		Long quoteInElenco = allegatoAtto.countQuoteInElenco();
		Long quoteConvalidate = subdocumentoDad.countQuoteConvalidateByAllegatoAtto(allegatoAtto);

		if(quoteConvalidate.equals(Long.valueOf(0))) {
			return StatoOperativoAllegatoAtto.COMPLETATO;
		}
		if(quoteConvalidate.equals(quoteInElenco)){
			return StatoOperativoAllegatoAtto.CONVALIDATO;
		}
		return StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO;
	}
		
	
	//--------------------------------------------------------------------------------------------------
	
	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		String codiceElaborazione ="RICOMPLETA_ATTO";
		sb.append("Elaborazione Ritorna A Completato per ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getAnno() !=0) ? allegatoAtto.getAttoAmministrativo().getAnno() :" ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getNumero() !=0) ? allegatoAtto.getAttoAmministrativo().getNumero() :" ");
		sb.append("-");
		sb.append(allegatoAtto.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(sb.toString());
		res.addMessaggio(m);
	}
	
}
