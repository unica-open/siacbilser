/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.mutuo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaMutuoService extends AbstractBaseService<AggiornaMutuo, AggiornaMutuoResponse> {
	
	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	MutuoDad mutuoDad;
	
	@Override
	protected void init() {
		final String methodName = "AggiornaMutuoService : init()";
		log.debug(methodName, "- Begin");
	}
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public AggiornaMutuoResponse executeService(AggiornaMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "AggiornaMutuoService : execute()";
		
		log.debug(methodName, "- Begin");
		
		//dati di input presi da request:
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Mutuo mutuo = req.getMutuo();
		AttoAmministrativo attoAmministrativoInput = mutuo.getAttoAmministrativoMutuo();

		if(null!=attoAmministrativoInput){
			// Atto Amministrativo: verifica che lo stato del Provvedimento non sia annullato
			// reperimento del atto come oggetto totale
			RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimentoConUid(richiedente, attoAmministrativoInput);
			
			AttoAmministrativo attoAmministrativoEstratto = new AttoAmministrativo();
	
			// eventuali errori su atto a causa di errori del servizio oppure stato non valido dell'atto (annullato,inesistente)
			if(ricercaProvvedimentoResponse.isFallimento()){
				// ATT_ERR_0001	Provvedimento Inesistente
				// ATT_ERR_0002	Provvedimento Annullato
				List<Errore> listaErroriAttoAmministrativo = ricercaProvvedimentoResponse.getErrori();			
				res.setErrori(listaErroriAttoAmministrativo);
				res.setMutuo(null);
				res.setEsito(Esito.FALLIMENTO);
				return;
			} else {
				if(ricercaProvvedimentoResponse.getListaAttiAmministrativi() != null &&
				   ricercaProvvedimentoResponse.getListaAttiAmministrativi().size() == 1){
					attoAmministrativoEstratto = ricercaProvvedimentoResponse.getListaAttiAmministrativi().get(0);
					attoAmministrativoInput.setUid(attoAmministrativoEstratto.getUid());
					if(!attoAmministrativoEstratto.getStato().equals(StatoEntita.VALIDO)){
						
						addErroreAtt(ErroreAtt.PROVVEDIMENTO_ANNULLATO, "Provvedimento Annullato");
						return;
					}
				}else{
					
					addErroreAtt(ErroreAtt.PROVVEDIMENTO_INESISTENTE, "Provvedimento Inesistente");
					
					return;
				}
			}
		}

		// algoritmi dei controlli di merito come da specifiche di cdu 
		List<Errore> listaErrori = mutuoDad.controlliDiMeritoAggiornamentoMutuo(ente, richiedente, mutuo);
		if(listaErrori!=null && listaErrori.size()>0){
			res.setErrori(listaErrori);
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		/*
		 *  routine di aggiornamento del mutuo
		 *  - setting dei dati principali:
		 *      - tipo mutuo
		 *      - soggetto mutuo
		 *      - atto amministrativo mutuo
		 *      - gestione voci mutuo (verifica delle liste voci da inserire/annullare/modificare e gestione dei casi)
		 */
		Mutuo mutuoAggiornato = mutuoDad.aggiornaMutuo(ente, richiedente, mutuo);

		if(null!=mutuoAggiornato){
//			Viene costruita la response per esito OK
			res.setMutuo(mutuoAggiornato);
			res.setEsito(Esito.SUCCESSO);
		} else {
//			Viene costruita la response per esito KO
			res.setMutuo(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AggiornaMutuoService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Mutuo mutuo = req.getMutuo();
		
		String elencoParametriNonInizializzati = "";
		String elencoParametriNonInizializzatiVociMutuo = "";
		
		// verifica correttezza parametri alla funzione di aggiorna
		if(null==ente){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", ENTE";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "ENTE";
		}

		if(null==mutuo){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", MUTUO_DA_AGGIORNARE";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "MUTUO_DA_AGGIORNARE";
		}
		
		if(null!=mutuo && null!=mutuo.getAttoAmministrativoMutuo() && (mutuo.getAttoAmministrativoMutuo().getAnno() == 0 || mutuo.getAttoAmministrativoMutuo().getNumero() == 0)){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", PROVVEDIMENTO";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "PROVVEDIMENTO";
		}				
				
		if(null!=mutuo && null!=mutuo.getListaVociMutuo() && mutuo.getListaVociMutuo().size() > 0){
			for(VoceMutuo voceMutuo : mutuo.getListaVociMutuo()){
				List<VariazioneImportoVoceMutuo> listaVariazioniImportoVoceMutuo = voceMutuo.getListaVariazioniImportoVoceMutuo();
				Impegno impegnoVoceMutuo = voceMutuo.getImpegno();
				
				if (null==impegnoVoceMutuo){
					if(elencoParametriNonInizializzatiVociMutuo.length() > 0)
						elencoParametriNonInizializzatiVociMutuo = elencoParametriNonInizializzatiVociMutuo + ", IMPEGNO_VOCE_MUTUO";
					else
						elencoParametriNonInizializzatiVociMutuo = elencoParametriNonInizializzatiVociMutuo + "IMPEGNO_VOCE_MUTUO";
				} 

				if(elencoParametriNonInizializzatiVociMutuo.length() > 0)
					break;
			}
		}

		if(elencoParametriNonInizializzatiVociMutuo.length() > 0){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", " + elencoParametriNonInizializzatiVociMutuo;
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzatiVociMutuo;
		}

		if(elencoParametriNonInizializzati.length() > 0)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriNonInizializzati));
	}
}