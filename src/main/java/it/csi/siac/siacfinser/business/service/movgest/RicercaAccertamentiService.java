/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentiService extends AbstractBaseService<RicercaAccertamenti, RicercaAccertamentiResponse> {

	
	@Override
	protected void init() {
		final String methodName = "RicercaAccertamentiService : init()";
		log.debug(methodName, "- Begin");		
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaAccertamentiResponse executeService(RicercaAccertamenti serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaAccertamentiService : execute()";
		//1. Leggiamo i dati ricevuti dalla request:
		RicercaAccertamento paramentroRicercaAccertamento = req.getpRicercaAccertamento();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		List<Accertamento> accertamentiList = new ArrayList<Accertamento>();
		
		//2. Si invoca il metodo ricercaAccertamenti che ci restituisce il numero di risultati attesi dalla query composta
		//secondo i parametri di ricerca
		
		accertamentiList = accertamentoOttimizzatoDad.ricercaAccertamenti(req.getRichiedente() ,idEnte, paramentroRicercaAccertamento);
		
		//3. si invoca il metodo che carica tutti i dati rispetto alla query composta dall'input ricevuto:
		// CR 1908, si ricerca anche per i subaccertamenti e si elimina il MAX_RIGHE_ESTRAIBILI
		List<Accertamento> paginata = getPaginata(accertamentiList, req.getNumPagina(), req.getNumRisultatiPerPagina());
		
		paginata = accertamentoOttimizzatoDad.completaPresenzaSub(paginata);
		
		//4. Viene costruita la response per esito OK
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getpRicercaAccertamento().getAnnoBilancio());
		res.setBilancio(bilancio);
		res.setEsito(Esito.SUCCESSO);
		res.setAccertamenti(paginata);
		res.setNumRisultati(accertamentiList.size());
		res.setNumPagina(req.getNumPagina());

	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegniService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaAccertamento() " + req.getpRicercaAccertamento());

		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		if(req.getpRicercaAccertamento() == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Paramentro_Ricerca_Accertamento"));
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
		
	}	

}
