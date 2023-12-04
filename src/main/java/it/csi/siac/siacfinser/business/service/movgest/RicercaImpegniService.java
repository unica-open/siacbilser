/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

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
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobal;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobalResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegniService extends AbstractBaseService<RicercaImpegniGlobal, RicercaImpegniGlobalResponse> {

	@Override
	protected void init() {
		final String methodName = "RicercaImpegniService : init()";
		log.debug(methodName, "- Begin");		
	}	

	@Override
	@Transactional(readOnly=true)
	public RicercaImpegniGlobalResponse executeService(RicercaImpegniGlobal serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	public void execute() {
		final String methodName = "RicercaImpegniService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getParametroRicercaImpegno() " + req.getParametroRicercaImpegno());
		//1. Leggiamo i dati ricevuti dalla request:
		ParametroRicercaImpegno parametroRicercaImpegno = req.getParametroRicercaImpegno();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();

		//2. Si invoca il metodo ricercaImpegni che ci restituisce il numero di risultati attesi dalla query composta
		//secondo i parametri di ricerca
		List<Impegno> impegniList = impegnoOttimizzatoDad.ricercaImpegni(req.getRichiedente() , CostantiFin.AMBITO_FIN, idEnte, parametroRicercaImpegno);

		// 3. Si invoca il metodo che carica tutti i dati rispetto alla query composta dall'input ricevuto:
		// CR 1907, ricercare anche x sub ed eliminare il max righe estraibili
		List<Impegno> paginata = getPaginata(impegniList, req.getNumPagina(), req.getNumRisultatiPerPagina());
		
		paginata = impegnoOttimizzatoDad.completaPresenzaSub(paginata);
		
		//4. Viene costruita la response per esito OK
		res.setNumRisultati(impegniList.size());
		res.setNumPagina(req.getNumPagina());
		res.setImpegniList(paginata);
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(parametroRicercaImpegno.getAnnoBilancio());
		res.setBilancio(bilancio);
		res.setEsito(Esito.SUCCESSO);
	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegniService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getParametroRicercaImpegno() " + req.getParametroRicercaImpegno());

		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		if(req.getParametroRicercaImpegno() == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Paramentro_Ricerca_Impegno"));
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
		
	}	
}

