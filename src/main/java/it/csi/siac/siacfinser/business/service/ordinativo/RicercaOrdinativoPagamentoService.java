/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoPagamentoService extends AbstractBaseService<RicercaOrdinativo, RicercaOrdinativoResponse> {


	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad; 
	
	@Override
	protected void init() {
		final String methodName="RicercaOrdinativoPagamentoService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaOrdinativoResponse executeService(RicercaOrdinativo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "RicercaOrdinativoPagamentoService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		Richiedente richiedente = req.getRichiedente();
		Calendar calendar = Calendar.getInstance();
		java.util.Date nowDate = calendar.getTime();
		java.sql.Timestamp now = new java.sql.Timestamp(nowDate.getTime());
		ParametroRicercaOrdinativoPagamento parametroRicercaOrdinativoPagamento = req.getParametroRicercaOrdinativoPagamento();

		// RM: per la jira 4556  FIN - Ordinativi e liquidazioni - Toglietre 'Ricerca tropo estesa'
		// Si invoca il metodo calcolaNumeroOrdinativiPagamentoDaEstrarre che ci restituisce il numero di risultati attesi dalla query composta
		// secondo i parametri di ricerca
		Long conteggioRecords = ordinativoPagamentoDad.calcolaNumeroOrdinativiPagamentoDaEstrarre(parametroRicercaOrdinativoPagamento, idEnte);
		//...solo se il numero di risultati attesi e minore del numero massimo accettabile si procede con il caricamento di tutti i dati:
		//if(conteggioRecords <= CostantiFin.MAX_RIGHE_ESTRAIBILI.longValue()){
			
			List<OrdinativoPagamento> listaRisultati = ordinativoPagamentoDad.ricercaSinteticaOrdinativiPagamento(richiedente, parametroRicercaOrdinativoPagamento, idEnte, req.getNumPagina(), req.getNumRisultatiPerPagina(),now);
				
			BigDecimal totImporti = ordinativoPagamentoDad.totImporti(idEnte, parametroRicercaOrdinativoPagamento, req.getNumPagina(), req.getNumRisultatiPerPagina());
			res.setTotImporti(totImporti);
			
			res.setErrori(null);
			res.setEsito(Esito.SUCCESSO);
			res.setElencoOrdinativoPagamento(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
//		} else {
//			
//			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
//			res.setEsito(Esito.FALLIMENTO);
//			res.setElencoOrdinativoPagamento(null);
//		}
		
	}
	

}