/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseServiceRicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaOrdinativoPagamentoPerChiaveService extends AbstractBaseServiceRicercaOrdinativo<RicercaOrdinativoPagamentoPerChiave, RicercaOrdinativoPagamentoPerChiaveResponse> {
	
	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Autowired
	CapitoloUscitaGestioneService capitoloUscitaGestioneService;
		
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaOrdinativoPagamentoPerChiaveResponse executeService(RicercaOrdinativoPagamentoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Richiedente richiedente = req.getRichiedente();
		
		RicercaOrdinativoPagamentoK pk = req.getpRicercaOrdinativoPagamentoK();
		
		long startUno = System.currentTimeMillis();
		
		// Aggiunte queste righe perche' altrimenti andava in errore in caso di richiamo senza aver settato dataOra
		if(null==req.getDataOra()){
			req.setDataOra(new Date());
		}
		// 2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
		
		// 3. Si richiama il metodo "ottieniOrdinativoPagamento" il quale si occupa di orchestrare la ricerca per chiave e di 
		// "vestire" l'oggetto con i dati relativi al capitolo e provvedimento invocando gli opportuni servizi esterni
		OrdinativoPagamento ordinativoPagamento = ottieniOrdinativoPagamento(pk, datiOperazione, richiedente);
		
		
		log.info(methodName, "prima del controllo sull'ordinativo ");
		
		//CONTROLLO ORDINATIVO NON TROVATO:
		if(ordinativoPagamento==null || ordinativoPagamento.getNumero()==null){
			//non trovato
			addErroreCore(ErroreCore.ENTITA_NON_TROVATA, "Ordinativo di pagamento", riferimento(pk.getOrdinativoPagamento()));
			return;
		}
		
		// controllo rimosso (SIAC-6711)
//		if(ordinativoPagamento.getCodStatoOperativoOrdinativo()!=null && ordinativoPagamento.getCodStatoOperativoOrdinativo().equals("A")){
//			//ordinativo Annullato 
//			addErroreBil(ErroreBil.ENTITA_ANNULLATA, "Ordinativo di pagamento ");
//			return;
//		}

		//
		
		long endUno = System.currentTimeMillis();
		long startDue = System.currentTimeMillis();

		//4. Vengono vestiti ulteriormente i dati ottenuti con l'elenco degli ordinativi collegati:
		ordinativoPagamento = completaOrdinativiCollegati(ordinativoPagamento, req.getPaginazioneOrdinativiCollegati(), datiOperazione, richiedente);
		//
		
		//se esplicitamente richiesto anche gli ordinativi nell'altro senso della relazione:
		if(req.isCaricaOrdinativiACuiSonoCollegato()){
			ordinativoPagamento = completaOrdinativiACuiSonoCollegato(ordinativoPagamento, datiOperazione, richiedente);
		}
		//
		
		
		long endDue = System.currentTimeMillis();
		
		long totUno = endUno - startUno;
		long totDue = endDue - startDue;
		
		CommonUtils.println("totUno: " + totUno + " - totDue: " + totDue);
		
		//5. Viene composta la response:
		res.setEsito(Esito.SUCCESSO);
		res.setOrdinativoPagamento(ordinativoPagamento);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		checkEntita(req.getEnte(), "Ente");
		
		checkNotNull(req.getpRicercaOrdinativoPagamentoK(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria ordinativo pagamento"));

		OrdinativoPagamento ordPag = req.getpRicercaOrdinativoPagamentoK().getOrdinativoPagamento();

		checkNotNull(ordPag , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ordinativo pagamento"));
		checkCondition(ordPag.getNumero()!= null || ordPag.getUid() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero oppure ID ordinativo pagamento"));

		Bilancio bil = req.getpRicercaOrdinativoPagamentoK().getBilancio();

		checkEntita(bil, "RicercaOrdinativoPagamentoK.Bilancio");
    	checkCondition(bil.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno bilancio"));
	}
}