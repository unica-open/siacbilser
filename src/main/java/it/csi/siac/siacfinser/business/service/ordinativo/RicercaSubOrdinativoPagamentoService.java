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

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubOrdinativoResponse;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoPagamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSubOrdinativoPagamentoService extends AbstractBaseService<RicercaSubOrdinativo, RicercaSubOrdinativoResponse> {


	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad; 
	
	@Override
	protected void init() {
		final String methodName="RicercaSubOrdinativoPagamentoService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSubOrdinativoResponse executeService(RicercaSubOrdinativo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "RicercaSubOrdinativoPagamentoService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		Richiedente richiedente = req.getRichiedente();
		Calendar calendar = Calendar.getInstance();
		java.util.Date nowDate = calendar.getTime();
		java.sql.Timestamp now = new java.sql.Timestamp(nowDate.getTime());
		ParametroRicercaSubOrdinativoPagamento parametroRicercaSubOrdinativoPagamento = req.getParametroRicercaSubOrdinativoPagamento();
		Integer annoBilancio = req.getBilancio().getAnno();
		
		//dati operazione:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, annoBilancio);

		//contiamo i record:
		Long conteggioRecords = ordinativoPagamentoDad.calcolaNumeroSubOrdinativiPagamentoDaEstrarre(parametroRicercaSubOrdinativoPagamento, idEnte,req.getNumPagina(), req.getNumRisultatiPerPagina());
			
		List<SubOrdinativoPagamento> listaRisultati = ordinativoPagamentoDad.ricercaSinteticaSubOrdinativiPagamento(richiedente, parametroRicercaSubOrdinativoPagamento, req.getNumPagina(), req.getNumRisultatiPerPagina(),datiOperazione);
			
		BigDecimal totImporti = ordinativoPagamentoDad.totImporti(idEnte, parametroRicercaSubOrdinativoPagamento, req.getNumPagina(), req.getNumRisultatiPerPagina());
		res.setTotImporti(totImporti);
		
		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);
		res.setElencoSubOrdinativoPagamento(listaRisultati);
		res.setNumRisultati(conteggioRecords.intValue());
		res.setNumPagina(req.getNumPagina());
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
	
		//dati di input presi da request:
		
		ParametroRicercaSubOrdinativoPagamento paramRicerca = req.getParametroRicercaSubOrdinativoPagamento();
		if(null==paramRicerca){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO_RICERCA_SUB_ORDINATIVO_PAGAMENTO"));			
		}
		
		Ente ente = req.getRichiedente().getAccount().getEnte();
		if(ente==null || ente.getUid()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
		
		Bilancio bil = req.getBilancio();
		if(bil==null || bil.getUid()<=0 || bil.getAnno()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		}
		
		Richiedente richiedente = req.getRichiedente();
		if(richiedente==null || richiedente.getAccount()==null || richiedente.getAccount().getUid()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICHIEDENTE"));
		}

	}	

}