/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CancellaSoggettoService extends AbstractSoggettoService<CancellaSoggetto, CancellaSoggettoResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		final String methodName="CancellaSoggettoService- init()";
	    log.debug(methodName, "Begin");
	}	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public CancellaSoggettoResponse executeService(CancellaSoggetto serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	
	@Override
	public void execute() {
		String methodName="CancellaSoggettoService - execute()";
		log.debug(methodName, "Begin");
		
		//1.Leggiamo i dati dalla request:
		// Ente
		Ente enteIn = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = enteIn.getUid();
		// Soggetto
		Soggetto soggettoIn = req.getSoggetto();
		Integer idSoggettoIn = soggettoIn.getUid();		
		// Login Operazione
		String loginOperazione = req.getRichiedente().getAccount().getNome();
		
		// recupero da request l'eventuale ambito
		String ambitoDaService = null;
		if(null==req.getCodificaAmbito() || "".equals(req.getCodificaAmbito())){
			// se non c'e' assumo di default che sia FIN
			ambitoDaService = CostantiFin.AMBITO_FIN;
		}
		
		// Se FIN allora devo far partire i controlli
		if(null!=ambitoDaService && ambitoDaService.equals(CostantiFin.AMBITO_FIN)){
		
			//2.Occorre verificare che non ci siano legami con altri oggetti che ci impediscono di procedere con l'operazione:
			List<Errore> listaErroriLegami = controlliDipendenzaEntita(idSoggettoIn, enteIn, req.getRichiedente(), Operazione.CANCELLAZIONE_LOGICA_RECORD);
			if(listaErroriLegami!=null && listaErroriLegami.size()>0){
				//Esito negativo, il servizio termina con fallimento
				res.setErrori(listaErroriLegami);
				res.setEsito(Esito.FALLIMENTO);
				return;
			}
		
		}
		
		//3. Invoco il metodo che si occupa della cancellazione del soggetto:
		Soggetto soggetto = soggettoDad.cancellaSoggetto(idEnte, soggettoIn, loginOperazione, req.getRichiedente());
		
		//4. Compongo la request di ritorno:
		res.setSoggetto(soggetto);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		final String methodName="checkServiceParam()";
		log.debug(methodName, "Inizio esecuzione");
		
		Ente ente = req.getEnte();
		Soggetto soggetto = req.getSoggetto();

		if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO ENTE NON INIZIALIZZATO"));
		}else if(soggetto == null || soggetto.getUid() == 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO SOGGETTO NON CORRETTAMENTE INIZIALIZZATO"));
		}
	}
}
