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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModificaResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.SedeSecondariaSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaSedeInModificaService extends AbstractBaseService<AnnullaSedeInModifica, AnnullaSedeInModificaResponse> {
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		log.debug("","AnnullaSedeInModificaService - init()");
	}	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public AnnullaSedeInModificaResponse executeService(
			AnnullaSedeInModifica serviceRequest) {
		
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "AnnullaSedeInModificaService - execute()";
		log.debug(methodName, " Begin");
		//1. Leggo i dati in input al servizio
		Integer idEnte = req.getEnte().getUid();
		Integer idSede = req.getSedeDaAggiornare().getUid();
		String loginOperazione = req.getRichiedente().getAccount().getNome();
		
		//2. Invoco il metodo che si occupa di annullare la sede secondaria:
		it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto 
			sede = soggettoDad.annullaSedeSecondaria(loginOperazione, idEnte, idSede, req.getRichiedente());
		
		//3. Compongo la response di output:
		SedeSecondariaSoggetto sedeAggiornata = new SedeSecondariaSoggetto();
		sedeAggiornata.setUid(idSede);
		res.setSedeAggiornata(sedeAggiornata);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		final String methodName="AnnullaSedeInModificaService-checkServiceParam() ";
		log.debug(methodName, "Begin");

		Ente ente = req.getEnte();
		SedeSecondariaSoggetto sedeSecondariaSoggetto = req.getSedeDaAggiornare();

		if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO ENTE NON INIZIALIZZATO"));
		}else if(sedeSecondariaSoggetto == null || sedeSecondariaSoggetto.getUid() == 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO SEDE SECONDARIA SOGGETTO NON INIZIALIZZATO"));
		}
	}
}
