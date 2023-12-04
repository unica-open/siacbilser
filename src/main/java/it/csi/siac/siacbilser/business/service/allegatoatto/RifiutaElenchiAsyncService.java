/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Rifiuta uno o più elenchi ed eventualmente l'intero allegato.
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RifiutaElenchiAsyncService extends AsyncBaseService<RifiutaElenchi, RifiutaElenchiResponse,
	AsyncServiceRequestWrapper<RifiutaElenchi>,
	RifiutaElenchiAsyncResponseHandler,
	RifiutaElenchiService> {
	
	private AllegatoAtto allegatoAtto;
	//private Bilancio bilancio;
	private StatoOperativoAllegatoAtto statoOperativoAllegatoAtto;
	//private Ente ente;
	private boolean elenchiValorizzati;
	private List<ElencoDocumentiAllegato> elenchi;
	
	@Autowired
	protected AllegatoAttoDad allegatoAttoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	@Autowired 
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<RifiutaElenchi> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(originalRequest.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(originalRequest.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkNotNull(originalRequest.getAllegatoAtto().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente allegato atto"));
		checkCondition(originalRequest.getAllegatoAtto().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente allegato atto"), false);
		//this.ente = originalRequest.getAllegatoAtto().getEnte();
		
		checkEntita(originalRequest.getBilancio(), "Bilancio");
		
		this.elenchiValorizzati = originalRequest.getAllegatoAtto().getElenchiDocumentiAllegato() != null
				&& !originalRequest.getAllegatoAtto().getElenchiDocumentiAllegato().isEmpty();
		
		if(elenchiValorizzati) {
			for(ElencoDocumentiAllegato elenco : originalRequest.getAllegatoAtto().getElenchiDocumentiAllegato()){
				checkNotNull(elenco, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
				checkCondition(elenco.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco documenti allegato allegato atto"));
			}
		}
		
		this.statoOperativoAllegatoAtto = originalRequest.getAllegatoAtto().getStatoOperativoAllegatoAtto();
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		
		this.allegatoAtto = caricaAllegatoAtto(originalRequest.getAllegatoAtto().getUid());
		//this.bilancio = caricaBilancio(originalRequest.getBilancio().getUid());
		this.elenchi = elenchiValorizzati ? originalRequest.getAllegatoAtto().getElenchiDocumentiAllegato(): allegatoAtto.getElenchiDocumentiAllegato();
		
		log.debug(methodName, "Controlli applicativi sincroni");
		applicationChecks();
		
		// Inizializzazione del service
		//initAsyncService();
		
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
	}
	
	/**
	 * Controlli applicativi
	 */
	private void applicationChecks() {
		checkStatoOperativoAllegatoAttoCompletato(allegatoAtto);
		
		// Verifica dati
		ListIterator<ElencoDocumentiAllegato> listIterator = elenchi.listIterator();
		while(listIterator.hasNext()) {
			ElencoDocumentiAllegato elencoDocumentiAllegatoReq = listIterator.next();
			ElencoDocumentiAllegato elencoDocumentiAllegato = caricaElencoDocumentiAllegato(elencoDocumentiAllegatoReq.getUid());
			checkStatoElenco(elencoDocumentiAllegato);
			// Sostituisco il valore con quello calcolato da db
			listIterator.set(elencoDocumentiAllegato);
		}
	}
	
//	private void initAsyncService() {
//		service.setAllegatoAtto(allegatoAtto);
//		service.setBilancio(bilancio);
//		service.setEnte(ente);
//		service.setStatoOperativoAllegatoAtto(statoOperativoAllegatoAtto);
//		service.setElenchi(elenchi);
//	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
		// TODO Nothing to do?
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
	 * Ottiene i dati dell bilancio il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the bilancio
	 */
	protected Bilancio caricaBilancio(Integer uid) {
		final String methodName = "caricaBilancio";
		
		Bilancio bilancio = bilancioDad.getBilancioByUid(uid);
		
		if(bilancio == null) {
			log.error(methodName, "Nessun bilancio con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("bilancio", "uid: "+ uid));
		}
		return bilancio;
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
	 * Stato Elenchi: lo StatoOperativoElencoDocumento di tutti gli elenchi passati in input deve essere COMPLETATO o,
	 * se a parametro &eacute; stato passato Stato Operativo Atto in Convalida RIFIUTATO, anche RIFIUTATI.
	 * <br/>
	 * Se il controllo non ha esito positivo viene segnalato l'errore bloccante: &lt;FIN_ERR_0146: Elenco con stato incongruente o assente&gt;
	 *
	 * @param elenco the elenco
	 */
	protected void checkStatoElenco(ElencoDocumentiAllegato elenco) {
		String methodName = "checkStatoElenco";
		boolean statoOperativoCompatibile = (StatoOperativoAllegatoAtto.RIFIUTATO.equals(statoOperativoAllegatoAtto)
				&& StatoOperativoElencoDocumenti.RIFIUTATO.equals(elenco.getStatoOperativoElencoDocumenti()))
				|| StatoOperativoElencoDocumenti.COMPLETATO.equals(elenco.getStatoOperativoElencoDocumenti());
		
		if(!statoOperativoCompatibile){
			String statoAtteso = StatoOperativoAllegatoAtto.RIFIUTATO.equals(statoOperativoAllegatoAtto)
					? "Attesi: " + StatoOperativoElencoDocumenti.COMPLETATO + ", " + StatoOperativoElencoDocumenti.RIFIUTATO
					: "Atteso: " + StatoOperativoElencoDocumenti.COMPLETATO;
			log.error(methodName, "Stato non valido: " + elenco.getStatoOperativoElencoDocumenti() + ". " + statoAtteso);
			throw new BusinessException(ErroreFin.ELENCO_CON_STATO_INCONGRUENTE_O_ASSENTE.getErrore());
		}
	}

}
