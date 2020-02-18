/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSedeSecondariaPerChiaveService extends AbstractBaseService<RicercaSedeSecondariaPerChiave, RicercaSedeSecondariaPerChiaveResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		final String methodName="RicercaSedeSecondariaPerChiaveService-init()";
		log.debug(methodName, " Begin");
	}	
	
//	@Transactional(readOnly = true)
//	public RicercaSedeSecondariaPerChiaveResponse executeService(RicercaSedeSecondariaPerChiave serviceRequest)
//	{
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName="RicercaSedeSecondariaPerChiaveService-execute()";
		log.debug(methodName, " Begin");
		//1. Leggo i dati ricevuti in input al servizio:
		// Ente
		Ente enteIn = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = enteIn.getUid();
		// Codice Soggetto
		Soggetto soggettoIn = req.getSoggetto();
		String codiceSoggettoIn = soggettoIn.getCodiceSoggetto();
		// Id Sede Secondaria 
		SedeSecondariaSoggetto sedeSecondariaSoggettoIn = req.getSedeSecondariaSoggetto();
		Integer idSedeSecondaria = sedeSecondariaSoggettoIn.getUid();
		
		//2. Richiamo il metodo che va a caricare la sede richiesta:
		SedeSecondariaSoggetto sedeSecondariaSoggettoOut = null;
		log.debug(methodName, " chiamo ricercaSedeSecondariaPerChiave()");
		if (req.isMod()){
			//Se e' stata richiesta una sede in modifica:
			sedeSecondariaSoggettoOut = soggettoDad.ricercaSedeSecondariaModPerChiave(idEnte, codiceSoggettoIn, idSedeSecondaria);
		} else {
			//Se e' stata richiesta una sed:
			sedeSecondariaSoggettoOut = soggettoDad.ricercaSedeSecondariaPerChiave(idEnte, codiceSoggettoIn, idSedeSecondaria);
		}
			
		//3. Popolo la respose di ritorno:
		res.setSedeSecondariaSoggetto(sedeSecondariaSoggettoOut);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName="RicercaSedeSecondariaPerChiaveService-checkServiceParam() ";
		log.debug(methodName, "Begin");
		

		Ente ente = req.getEnte();
		Soggetto soggetto = req.getSoggetto();
		SedeSecondariaSoggetto sedeSecondariaSoggetto = req.getSedeSecondariaSoggetto();

		if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO ENTE NON INIZIALIZZATO"));
		}else if(soggetto == null || soggetto.getCodiceSoggetto() == null || "".equalsIgnoreCase(soggetto.getCodiceSoggetto())){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO SOGGETTO NON INIZIALIZZATO"));
		}else if(sedeSecondariaSoggetto == null || sedeSecondariaSoggetto.getUid() == 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PARAMETRO SEDE SECONDARIA SOGGETTO NON INIZIALIZZATO"));
		}
	}	
}