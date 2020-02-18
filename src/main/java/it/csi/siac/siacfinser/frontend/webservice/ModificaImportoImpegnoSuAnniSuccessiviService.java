/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.movgest.AggiornaImpegnoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessivi;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessiviResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Service
@Scope("prototype")
public class ModificaImportoImpegnoSuAnniSuccessiviService extends AbstractBaseService<ModificaImportoImpegnoSuAnniSuccessivi, ModificaImportoImpegnoSuAnniSuccessiviResponse>{
	
	@Autowired 
	private AggiornaImpegnoService aggiornaImpegnoService;
	
	@Autowired
	private transient MovimentoGestioneService movimentoGestionService;
	
	@Override
	protected void init() {
		final String methodName = "AggiornaImpegnoService : init()";
		log.trace(methodName, "- Begin");		
	}	

	@Override
	@Transactional(timeout= 30000)
	public ModificaImportoImpegnoSuAnniSuccessiviResponse executeService(ModificaImportoImpegnoSuAnniSuccessivi serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		boolean fallimentoAggiorna = true;
		boolean fallimentoInserisci= true;
		
		//lancio il metodo e controllo il risultato finale
		fallimentoAggiorna = lanciaAggiornaImpegno(fallimentoAggiorna);
	
		//se è un fallimento o c'è stato un rollback nel servizio interno, eseguo il rollback
		if(fallimentoAggiorna || TransactionAspectSupport.currentTransactionStatus().isRollbackOnly()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addErroreFin(ErroreFin.MODIFICA_SU_ANNI_SUCCESSIVI_NON_COMPLEATA, "Modifica Importo non completa: Errore in aggiornamento della modifica impegno per l'anno: "+ req.getAnnoBilancio());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
	
		//lancio il metodo e controllo il risultato finale
		fallimentoInserisci = lanciaInserisciImpegni(fallimentoInserisci);
		
		//se è un fallimento o c'è stato un rollback nel servizio interno, eseguo il rollback
		if(fallimentoInserisci || TransactionAspectSupport.currentTransactionStatus().isRollbackOnly()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addErroreFin(ErroreFin.MODIFICA_SU_ANNI_SUCCESSIVI_NON_COMPLEATA, "Modifica Importo non completa: Errore in inserimento della modifica impegno su anno/i successivo/i");
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		//tutto corretto, se la transazione è ancora aperta (non ha eseguito rollback) la committo
		if(!TransactionAspectSupport.currentTransactionStatus().isCompleted()) {
			TransactionAspectSupport.currentTransactionStatus().flush();
			res.setEsito(Esito.SUCCESSO);
			return;
		}
		
		
	}
	
	private boolean lanciaAggiornaImpegno(boolean fallimento) {
		if(req.getAggiornaImpegnoPadre() != null) {
			AggiornaImpegnoResponse aggiornaImpegnoResponse = aggiornaImpegnoService.executeService(req.getAggiornaImpegnoPadre());
			fallimento = analizzaResponseAggiornaImpegno(aggiornaImpegnoResponse);
		}
		return fallimento;
	}
	
	private boolean lanciaInserisciImpegni(boolean fallimento) {
		if(req.getInserisciImpegniRequest() != null) {
			InserisceImpegniResponse response;
			response = movimentoGestionService.inserisceImpegni(req.getInserisciImpegniRequest());
			fallimento = analizzaResponseInserisciImpegni(response);
		}
		return fallimento;
	}
	
	private boolean analizzaResponseAggiornaImpegno(AggiornaImpegnoResponse response){
		
		String methodName = "analizzaResponseAggiornaImpegno";
		
		boolean fallimento = false;
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			
			//ci sono errori
			
			log.debug(methodName, "Errore nella risposta del servizio");
			res.addErrori(response.getErrori());
			
			fallimento = true;
		}
		
		res.setAggiornaImpegnoResponsePadre(response);
		List<Messaggio> listMess = new ArrayList<Messaggio>();
		
		
		listMess.add(new Messaggio("FIN_INF_0070","Movimento aggiornato ( movimento = Impegno, anno = " + response.getImpegno().getAnnoMovimento() +  ", numero = "+ response.getImpegno().getNumero() +" )"));
		res.setListaMessaggi(listMess);
		
		//ritorniamo il risultato dell'analisi:
		return fallimento;
	}
	
	private boolean analizzaResponseInserisciImpegni(InserisceImpegniResponse response){
		
		String methodName = "analizzaResponseInserisciImpegni";
		
		boolean fallimento = false;
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			
			//ci sono errori
			
			log.debug(methodName, "Errore nella risposta del servizio");
			res.addErrori(response.getErrori());
			
			fallimento = true;
		} else {
			
			//tutto ok
			
			List<Impegno> impList = response.getElencoImpegniInseriti();
			List<Messaggio> listaMessaggi = res.getListaMessaggi();
			if(impList != null && impList.size() > 0){
				
				//passso alla response gli impegni inseriti se ce ne sono
				res.setListaImpegniInseriti(impList);
				for(Impegno app : impList){
					listaMessaggi.add(new Messaggio("FIN_INF_0070","Movimento inserito ( movimento = Impegno, anno = " + app.getAnnoMovimento() +  ", numero = "+ app.getNumero() +" )"));
				}
			}
			
			res.setListaMessaggi(listaMessaggi);
			res.setInserisceImpegnoResponse(response);

		}
		
		//ritorniamo il risultato dell'analisi:
		return fallimento;
	}
	
}
