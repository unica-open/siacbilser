/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModificaResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaModalitaPagamentoInModificaService extends AbstractBaseService<AnnullaModalitaPagamentoInModifica, AnnullaModalitaPagamentoInModificaResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;

	@Override
	protected void init() {
		log.debug("","AnnullaModalitaPagamentoInModificaService - init()");
	}	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public AnnullaModalitaPagamentoInModificaResponse executeService(
			AnnullaModalitaPagamentoInModifica serviceRequest) {
		
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "AnnullaModalitaPagamentoInModificaService - execute()";
		//1. Leggo i dati in input al servizio
		Soggetto soggettoDaModificare = req.getSoggetto();
		ModalitaPagamentoSoggetto modPagModToDelete = req.getModalitaPagamentoSoggetto();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		//2. invocol il metodo che si occupa di annullare la modalita' di pagamento in modifica
		soggettoDad.annullaModalitaPagamentoInModifica(modPagModToDelete, soggettoDaModificare, richiedente, ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		

		Soggetto soggettoInput = req.getSoggetto();
		Ente ente = req.getEnte();
		
		if(soggettoInput == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto"));			
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}
		
		else if(soggettoInput.getUid()==0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto.idSoggetto"));
		}		
	}
}
