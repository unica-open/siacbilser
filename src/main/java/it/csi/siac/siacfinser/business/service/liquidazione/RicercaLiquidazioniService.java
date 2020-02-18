/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioniResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaLiquidazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaLiquidazioniService extends AbstractBaseService<RicercaLiquidazioni, RicercaLiquidazioniResponse> {

	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Override
	protected void init() {
		final String methodName="RicercaLiquidazioniService : init()";
		log.debug(methodName, " - Begin");
		
	}	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaLiquidazioniResponse executeService(RicercaLiquidazioni serviceRequest) {
//		return super.executeService(serviceRequest);
//	}	
	
	@Override
	public void execute() {
		String methodName = "RicercaLiquidazioniService - execute()";
		log.debug(methodName, " - Begin");

		//Lettura variabili di input
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		ParametroRicercaLiquidazione parametroRicercaLiquidazione = req.getParametroRicercaLiquidazione();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);

		if(parametroRicercaLiquidazione.getTipoRicerca()==null){
			//imposto un default che è LIQUIDAZIONE
			parametroRicercaLiquidazione.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);
		}
		
		
		//Calcolo del numero di liquidazioni da estrarre
		Long conteggioRecords = liquidazioneDad.calcolaNumeroLiquidazioniDaEstrarre(parametroRicercaLiquidazione, idEnte);

		// Verifica che il numero stia nel range massimo di ricerca
		// RM: per la jira 4556  FIN - Ordinativi e liquidazioni - Toglietre 'Ricerca tropo estesa'
		//if(conteggioRecords <= Constanti.MAX_RIGHE_ESTRAIBILI.longValue()){
			//Estrazione delle liquidazioni ricercate 
			List<Liquidazione> listaRisultati = liquidazioneDad.ricercaLiquidazioni(parametroRicercaLiquidazione,Constanti.AMBITO_FIN, idEnte, req.getNumPagina(), req.getNumRisultatiPerPagina(),datiOperazione);
			res.setEsito(Esito.SUCCESSO);
			res.setElencoLiquidazioni(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
//		} else {
//			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
//			res.setEsito(Esito.FALLIMENTO);
//			res.setElencoLiquidazioni(null);
//		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="RicercaLiquidazioniService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		ParametroRicercaLiquidazione parametroRicercaLiquidazione = req.getParametroRicercaLiquidazione();
		
		String elencoParamentriNonInizializzati = "";
		
		if(null==parametroRicercaLiquidazione){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", PARAMETRO_RICERCA_LIQUIDAZIONE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "PARAMETRO_RICERCA_LIQUIDAZIONE";
		}
		
		if(null==ente) {
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
		}
		
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));	
		} else {
			if((null!=parametroRicercaLiquidazione.getAnnoProvvedimento()) && (null==parametroRicercaLiquidazione.getNumeroProvvedimento())){
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_PROVVEDIMENTO"));
			} else if((null==parametroRicercaLiquidazione.getAnnoProvvedimento()) && (null!=parametroRicercaLiquidazione.getTipoProvvedimento())){
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("TIPO_PROVVEDIMENTO"));
			}
		}
	}	
}
