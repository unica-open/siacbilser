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

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModalitaPagamentoPerChiaveService extends AbstractBaseService<RicercaModalitaPagamentoPerChiave, RicercaModalitaPagamentoPerChiaveResponse>{

	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		final String methodName="RicercaModalitaPagamentoPerChiaveService-init()";
		log.debug(methodName, " Begin");
	}	
	
//	@Transactional(readOnly = true)
//	public RicercaModalitaPagamentoPerChiaveResponse executeService(RicercaModalitaPagamentoPerChiave serviceRequest)
//	{
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName="RicercaModalitaPagamentoPerChiaveService-execute()";
		log.debug(methodName, " Begin");
		
		String codiceAmbito = req.getCodificaAmbito();
		
		if (org.apache.commons.lang.StringUtils.isEmpty(codiceAmbito))
			codiceAmbito = Constanti.AMBITO_FIN;

		//1. Leggo i dati ricevuti in input al servizio:
		// Ente
		Ente enteIn = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = enteIn.getUid();
		// Codice Soggetto (bisogna analizzare se il chiamante ci ha passato il codice di un soggetto o di una modalita' di pagamento):
		Soggetto soggettoIn = req.getSoggetto();
		String codiceSoggettoIn = "";
		if(req.getSedeSecondariaSoggetto() != null){
			//Ci si sta riferendo ad una sede secondaria
			codiceSoggettoIn = String.valueOf(req.getSedeSecondariaSoggetto().getCodiceSedeSecondaria());
		} else {
			//Ci si sta riferendo ad un soggetto
			codiceSoggettoIn = soggettoIn.getCodiceSoggetto();
		}

		//Id Modalita Pagamento
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = req.getModalitaPagamentoSoggetto();
		Integer idModalitaPagamento = modalitaPagamentoSoggetto.getUid();
		
		
		//2. Invoco il metodo ricercaModalitaPagamentoPerChiave che si occupa di caricare la modalita' di pagamento richiesta:
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoList = soggettoDad.ricercaModalitaPagamentoPerChiave(codiceAmbito, idEnte, 
																													  codiceSoggettoIn, 
																													  idModalitaPagamento,
																													  modalitaPagamentoSoggetto.getCodiceModalitaPagamento());
		
		
		//3. Costruzione della response di ritorno:
		//Response
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoOut = null;
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoInModifica = null;
		if(modalitaPagamentoSoggettoList != null && !modalitaPagamentoSoggettoList.isEmpty()){
			if(modalitaPagamentoSoggettoList.size() == 2){
				//se modalitaPagamentoSoggettoList ha due elementi significa che la modalita' di pagamento ha anche il record in modifica (vedi tabelle _mod)
				modalitaPagamentoSoggettoInModifica = modalitaPagamentoSoggettoList.get(1);
			}
			//mentre in prima posizione c'e' sempre la modalita' di pagamento vera e propria:
			modalitaPagamentoSoggettoOut = modalitaPagamentoSoggettoList.get(0);
		}
		//setto i dati nella responce:
		res.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettoOut);
		res.setModalitaPagamentoSoggettoInModifica(modalitaPagamentoSoggettoInModifica);
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName="RicercaModalitaPagamentoPerChiaveService-checkServiceParam() ";
		log.debug(methodName, "Begin");
		
		
		Ente ente = req.getEnte();
		Soggetto soggetto = req.getSoggetto();
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = req.getModalitaPagamentoSoggetto();
		
		if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO ENTE NON INIZIALIZZATO"));
		} else if(soggetto == null || soggetto.getCodiceSoggetto() == null || "".equalsIgnoreCase(soggetto.getCodiceSoggetto())){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO SOGGETTO NON INIZIALIZZATO"));
		} else if(modalitaPagamentoSoggetto == null || (modalitaPagamentoSoggetto.getUid() == 0 && StringUtils.isEmpty(modalitaPagamentoSoggetto.getCodiceModalitaPagamento()) )){
			// caso in cui nella MDP sia uid che il codice Mdp siano vuoti allora e' errore
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO MODALITA PAGAMENTO SOGGETTO NON INIZIALIZZATO"));
		} else if(modalitaPagamentoSoggetto != null && !StringUtils.isEmpty(modalitaPagamentoSoggetto.getCodiceModalitaPagamento())){
			    // nel caso ci sia il codice inserito deve 
			    // essere un numero
			    if(!StringUtils.contieneSoloNumeri(modalitaPagamentoSoggetto.getCodiceModalitaPagamento())){
			    	checkCondition(false, ErroreFin.FORMATO_NON_VALIDO.getErrore("CODICE DI MODALITA PAGAMENTO SOGGETTO", "NUMERO"));
			    }
		}
	}	
}
