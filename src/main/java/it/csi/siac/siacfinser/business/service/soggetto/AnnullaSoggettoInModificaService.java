/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModificaResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaSoggettoInModificaService extends AbstractBaseService<AnnullaSoggettoInModifica, AnnullaSoggettoInModificaResponse> {
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		log.debug("","AnnullaSoggettoInModificaService - init()");
	}	
	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public AnnullaSoggettoInModificaResponse executeService(
			AnnullaSoggettoInModifica serviceRequest) {
		
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "AnnullaSoggettoInModificaService - execute()";
		//1. Leggo i dati in input al servizio:
		Soggetto soggettoDaModificare = req.getSoggetto();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		
		// recupero da request l'eventuale ambito
		String ambitoDaService = null;
		if(null==req.getCodificaAmbito() || "".equals(req.getCodificaAmbito())){
			// se non c'e' assumo di default che sia FIN
			ambitoDaService = Constanti.AMBITO_FIN;
		}
		
		
		boolean controlloCompatibilitaStatoEntita = true;
		// Se FIN allora devo far partire i controlli
		if(null!=ambitoDaService && ambitoDaService.equals(Constanti.AMBITO_FIN)){
			//2. Bisogna verificare che lo stato del soggetto indicato sia compatibile con l'operazione richiesta:		
			controlloCompatibilitaStatoEntita = soggettoDad.controlloCompatibilitaStatoSoggettoInModifica(soggettoDaModificare);
			if (controlloCompatibilitaStatoEntita == false) {
				//se l'esito del controllo e' negativo il servizio termina con esito positivo.
				res.setErrori(Arrays.asList(ErroreFin.OPERAZIONE_INCOMPATIBILE.getErrore()));
			} else {
				//se l'esito del controllo e' positivo si richiama il metodo per annullare il soggetto in modifica:
				res.setSoggetto(soggettoDad.annullaSoggettoInModifica(soggettoDaModificare, richiedente, ente));
			}
		}else{
			// Se NON sono ambito FIN faccio partire l'annulla senza controlli preliminari
			res.setSoggetto(soggettoDad.annullaSoggettoInModifica(soggettoDaModificare, richiedente, ente));
		}
		
		
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		

		log.debug("","AnnullaSoggettoInModificaService : checkServiceParam()");
		Soggetto soggettoInput = req.getSoggetto();
		Ente ente = req.getEnte();
		
		if(soggettoInput == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto"));			
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		} else if(soggettoInput.getUid()==0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto.idSoggetto"));
		}		
	}
}
