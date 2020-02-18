/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class AllegatoAttoBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	@Autowired
	protected AllegatoAttoDad allegatoAttoDad;
	
	@Autowired 
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	

	protected AllegatoAtto allegatoAtto;
	
	
	
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
	}

	

	/**
	 * Ottiene i dati dell'allegato atto dal database.
	 */
	protected void caricaAllegatoAtto() {
		allegatoAtto = caricaAllegatoAtto(allegatoAtto.getUid());
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
	
//	/**
//	 * Carica il dettaglio di tutti gli elenchi collegati all'allegatoAtto con tutte le quote in elenco.
//	 * NB: questa operazione &egrave; troppo onerosa! Meglio se possibile evitarla!!
//	 */
//	@Deprecated
//	protected void caricaDettaglioElenchiDocumentiAllegato() {
//		String methodName = "caricaDettaglioElenchi";
//		
//		for(ElencoDocumentiAllegato elenco : allegatoAtto.getElenchiDocumentiAllegato()) {
//			log.debug(methodName, "carico dettaglio elenco con uid: "+(elenco!=null?elenco.getUid():"null"));
//			elenco = caricaElencoDocumentiAllegato(elenco.getUid());
//			
//		}
//		
//	}
	

	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto
	 * 
	 * @param stato
	 */
	protected void aggiornaStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto stato) {
		aggiornaStatoOperativoAllegatoAtto(this.allegatoAtto, stato);
	}
	
	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto per l'AllegatoAtto passato come parametro.
	 *
	 * @param aa the allegatoAtto
	 * @param stato the stato
	 */
	protected void aggiornaStatoOperativoAllegatoAtto(AllegatoAtto aa, StatoOperativoAllegatoAtto stato) {
		String methodName = "aggiornaStatoOperativoAllegatoAtto";
		log.info(methodName, "stato AllegatoAtto.uid = " + aa.getUid() + " da impostare a " + stato);
		allegatoAttoDad.aggiornaStatoAllegatoAtto(aa.getUid(), stato);
	}
	
	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto per l'AllegatoAtto passato come parametro.
	 *
	 * @param elencoDocumentiAllegato the allegatoAtto
	 * @param stato the stato
	 */
	protected void aggiornaStatoOperativoElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato, StatoOperativoElencoDocumenti stato) {
		String methodName = "aggiornaStatoOperativoAllegatoAtto";
		log.info(methodName, "stato AllegatoAtto.uid = " + elencoDocumentiAllegato.getUid() + " da impostare a " + stato);
		elencoDocumentiAllegatoDad.aggiornaStatoElencoDocumentiAllegato(elencoDocumentiAllegato.getUid(), stato);
	}
	
	
	/**
	 * Ottiene i dati dell'elenco
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
	 * Controllo lo stato operativo dell'allegato atto.
	 * <br>
	 * L'allegato deve essere in stato COMPLETATO, in caso contrario si invia il messaggio &lt;FIN_ERR_0226, Stato Allegato atto incongruente&gt;.
	 */
	protected void checkStatoOperativoAllegatoAttoCompleto() {
		final String methodName = "checkStatoOperativoAllegatoAttoCompleto";
		// Nota bene: la seconda condizione e' equivalente alla prima. Cfr. StateMachine dello StatoOperativoAllegatoAtto
		if(!StatoOperativoAllegatoAtto.COMPLETATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			log.debug(methodName, "Stato non valido: " + allegatoAtto.getStatoOperativoAllegatoAtto()
					+ ". Atteso " + StatoOperativoAllegatoAtto.COMPLETATO);
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	
	protected void checkStatoOperativoAllegatoAttoNonCompletatoOParzialmenteConvalidato() {
		final String methodName = "checkStatoOperativoAllegatoAttoNonCompletatoOParzialmenteCompletato";
		// Nota bene: la seconda condizione e' equivalente alla prima. Cfr. StateMachine dello StatoOperativoAllegatoAtto
		if(!StatoOperativoAllegatoAtto.COMPLETATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			log.debug(methodName, "Stato non valido: " + allegatoAtto.getStatoOperativoAllegatoAtto());
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	






	


	

}
