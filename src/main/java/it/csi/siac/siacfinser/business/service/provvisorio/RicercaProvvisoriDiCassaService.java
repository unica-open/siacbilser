/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.provvisorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.integration.dad.ProvvisorioDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaProvvisoriDiCassaService extends AbstractBaseService<RicercaProvvisoriDiCassa, RicercaProvvisoriDiCassaResponse> {

	@Autowired
	ProvvisorioDad provvisorioDad;
	
	@Override
	protected void init() {
		final String methodName="RicercaProvvisoriService : init()";
		log.debug(methodName, " - Begin");
		
	}	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaProvvisoriDiCassaResponse executeService(RicercaProvvisoriDiCassa serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "RicercaProvvisoriDiCassaService - execute()";
		log.debug(methodName, " - Begin");
		
		//1. Vengono letti i valori ricevuti in input dalla request:
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = req.getParametroRicercaProvvisorio();
		
		
		// Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);

		//2. Si invoca il metodo ricercaProvvisoriDiCassaSoloIds che ci restituisce gli ids
		List<Integer> listaId = provvisorioDad.ricercaProvvisoriDiCassaSoloIds(parametroRicercaProvvisorio, idEnte);

		Long conteggioRecords = new Long(0);
		if(listaId!=null){
			conteggioRecords = new Long(listaId.size());
		}
		
		
		//...solo se il numero di risultati attesi e minore del numero massimo accettabile 
		//   si procede con il caricamento di tutti i dati:
//		if(conteggioRecords <= Constanti.MAX_RIGHE_ESTRAIBILI.longValue()){
			//3. si invoca il metodo che carica tutti i dati rispetto alla query composta dall'input ricevuto:
			List<ProvvisorioDiCassa> listaRisultati = provvisorioDad.caricaPaginaProvvisoriDiCassa(datiOperazione, req.getNumPagina(), req.getNumRisultatiPerPagina(),listaId);
			//4. Viene costruita la response per esito OK		
			res.setErrori(null);
			res.setEsito(Esito.SUCCESSO);
			res.setElencoProvvisoriDiCassa(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
//		} else {
//			// Viene costruita la response per esito KO
//			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
//			res.setEsito(Esito.FALLIMENTO);
//			res.setElencoProvvisoriDiCassa(null);
//		}
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="RicercaProvvisoriDiCassaService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = req.getParametroRicercaProvvisorio();
		
		String elencoParamentriNonInizializzati = "";
		
		if(null==parametroRicercaProvvisorio){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", PARAMETRO_RICERCA_PROVVISORI";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "PARAMETRO_RICERCA_PROVVISORI";
		}
		
		if(null==richiedente) {
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", RICHIEDENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "RICHIEDENTE";
		}
		
		if(null==ente) {
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
		}
	}	
}
