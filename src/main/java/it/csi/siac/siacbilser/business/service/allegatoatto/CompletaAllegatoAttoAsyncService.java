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
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaAllegatoAttoAsyncService extends AsyncBaseService<CompletaAllegatoAtto,
																	   CompletaAllegatoAttoResponse,
																	   AsyncServiceRequestWrapper<CompletaAllegatoAtto>,
																	   CompletaAllegatoAttoAsyncResponseHandler,
																	   CompletaAllegatoAttoService> {
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Autowired
	private ElaborazioniManager elaborazioniManager;

	private AllegatoAtto allegatoAtto;
	
	//gestione del blocco delle altre operazioni
	private ElabKeys completaAllegatoKeySelector = ElabKeys.COMPLETA_ALLEGATO_ATTO;
	private ElaborazioniAttiveKeyHandler eakh;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(originalRequest.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(originalRequest.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkEntita(originalRequest.getAllegatoAtto().getEnte(), "ente allegato atto", false);
		checkEntita(originalRequest.getBilancio(), "bilancio", false);
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<CompletaAllegatoAtto> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		elaborazioniManager.init(originalRequest.getAllegatoAtto().getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
		//instanzio l'handler per la gestione centralizzata delle chiavi di blocco delle elaborazioni
		eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getAllegatoAtto().getUid());
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		
		checkElaborazioniInCorso();
		
		log.debug(methodName, "Controlli applicativi sincroni");
//		caricaBilancio();
		caricaAllegatoAtto();

		checkStatoOperativoAllegatoAttoDaCompletare();
		checkAlmenoUnaQuotaPresente();
		checkQuoteSpesaRilevantiIVA();
		checkQuoteEntrataCollegateAMovimento();
		checkQuoteSpesaNonCollegateALiquidazioneCollegateAMovimento();
		
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
	}

	
	/**
	 * Controlla che non vi sia un servizio che sta facendo la stessa elaborazione.
	 * Nel caso in cui ci&ograve; non accada, viene lanciata un'eccezione
	 */
	private void checkElaborazioniInCorso() {
		
		boolean esisteElaborazioneAttiva = elaborazioniManager.esisteElaborazioneAttiva(eakh.creaElabServiceFromPattern(completaAllegatoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoKeySelector));
		
		if(esisteElaborazioneAttiva){
			//non posso procedere. lancio un'eccezione.
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("L'elaborazione per l'allegato atto e' gia' in corso. [uid: "+ originalRequest.getAllegatoAtto().getUid()+"]"));
		}
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		try{
			elaborazioniManager.startElaborazione(eakh.creaElabServiceFromPattern(completaAllegatoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoKeySelector));
		} catch (ElaborazioneAttivaException eae){
			String msg = "L'elaborazione per l'allegato atto e' gia' in corso. [uid: "+ originalRequest.getAllegatoAtto().getUid()+"]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		super.startService();
	}

	
//	private void caricaBilancio(){
//		this.bilancio = bilancioDad.getBilancioByUid(originalRequest.getBilancio().getUid());
//	}

	/**
	 * Ottiene i dati dell'allegato atto dal database.
	 */
	private void caricaAllegatoAtto() {
		final String methodName = "caricaAllegatoAtto";
		
		final int uid = originalRequest.getAllegatoAtto().getUid();
		
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(uid);
		
		if(aa == null) {
			log.error(methodName, "Nessun allegato atto con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid: "+ uid));
		}
		allegatoAtto = aa;
	}
	
	/**
	 * Controllo lo stato operativo dell'allegato atto.
	 * <br>
	 * L'allegato deve essere in stato DA COMPLETARE, in caso contrario si invia il messaggio &lt;FIN_ERR_0226, Stato Allegato atto incongruente&gt;.
	 * <br>
	 * Verifica se &eacute; possibile annullare l'Allegato Atto controllando il diagramma degli stati dell'atto,
	 * se non &eacute; possibile segnala il messaggio &lt;FIN_ERR_0226, Stato Allegato Atto incongruente&gt;.
	 */
	protected void checkStatoOperativoAllegatoAttoDaCompletare() {
		final String methodName = "checkStatoOperativoAllegatoAttoDaCompletare";
		// Nota bene: la seconda condizione e' equivalente alla prima. Cfr. StateMachine dello StatoOperativoAllegatoAtto
		if(!StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			log.debug(methodName, "Stato non valido: " + allegatoAtto.getStatoOperativoAllegatoAtto()
					+ ". Atteso " + StatoOperativoAllegatoAtto.DA_COMPLETARE);
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	/**
	 * CR-2996:
	 * COMPLETA ALLEGATO ATTO: l'atto deve avere almeno una riga, una quota documento collegata
	 */
	private void checkAlmenoUnaQuotaPresente() {
		final String methodName = "checkAlmenoUnaQuotaPresente";
		Boolean hasQuote = Boolean.valueOf(allegatoAttoDad.countQuoteAssociateAdAllegato(allegatoAtto) >=1);
		if(!hasQuote){
			log.debug(methodName, "L'allegato [uid:" + allegatoAtto.getUid() + "] non risulta collegato a nessuna quota documento. ");
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("completa", "allegato atto non collegato a nessuna quota"));
		}
	}
	
	/**
	 * Le quote documento rilevanti ai fini IVA (flagRilevanteIVA = true) non ancora collegate a liquidazione devono avere valorizzato l'attributo
	 * nRegistrazioneIVA altrimenti non &egrave; possibile procedere con il completamento dell'AllegatoAtto segnalando l'errore
	 * &lt;COR_ERR_0031 - Aggiornamento non possibile (&lt;entit&agrave;&gt;: Quota documento +'identificativo doc e quota,
	 * &lt;operazione&gt;: mancante di numero registrazione IVA)&gt;.
	 */
	private void checkQuoteSpesaRilevantiIVA() {
		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneSenzaNregIvaByAllegatoAtto(allegatoAtto);
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di numero registrazione IVA"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}
	
	/**
	 * Le quote di entrata devono avere collegato un movimento e il movimento deve appartenere allo stesso bilancio su cui si sta operando.
	 * &lt;COR_ERR_0031 - Aggiornamento non possibile (&lt;entit&agrave;&gt;: Quota documento +‘identificativo doc e quota,
	 * &lt;operazione&gt;: mancante di accertamento)&gt;.
	 */
	private void checkQuoteEntrataCollegateAMovimento() {
		List<SubdocumentoEntrata> subdocs = allegatoAttoDad.findSubdocumentiEntrataSenzaMovimentoDelloStessoBilancio(allegatoAtto, originalRequest.getBilancio());
		
		for(SubdocumentoEntrata subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di accertamento"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}
	
	/**
	 * Le quote di spesa non collegate a liquidazione deve avere collegato un movimento e il movimento deve appartenere allo stesso bilancio su cui si sta operando. 
	 * &lt; COR_ERR_0031 Aggiornamento non possibile (&lt;entit&agrave;&gt;>: Quota documento + 'identificativo doc e quota'
	 * &lt;operazione&gt;: mancante di impegno)&gt;.
	 */
	private void checkQuoteSpesaNonCollegateALiquidazioneCollegateAMovimento() {
		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaSenzaLiquidazioneESenzaMovimentoDelloStessoBilancio(allegatoAtto, originalRequest.getBilancio());
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di impegno"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
	@Override
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		Long threshold = getThreshold();
		Long numeroQuote = allegatoAttoDad.countQuoteAssociateAdAllegato(allegatoAtto);
		return threshold != null && numeroQuote != null && numeroQuote.compareTo(threshold) > 0;
	}
}
