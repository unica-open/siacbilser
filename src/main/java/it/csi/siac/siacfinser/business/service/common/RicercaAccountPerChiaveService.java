/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccountPerChiaveService extends AbstractBaseService<RicercaAccountPerChiave, RicercaAccountPerChiaveResponse>{

	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
	}
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaAccountPerChiaveResponse executeService(RicercaAccountPerChiave serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "RicercaAccountPerChiaveService - execute()";
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		
//		dato un certo codice utente va a verificare se corrisponde allo stesso utente con cui ci si e' loggati
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.INSERIMENTO, null);
		
		String codeUtente = commonDad.determinaUtenteLogin(datiOperazione);
		
		res.setAccountCode(codeUtente);
		res.setEsito(Esito.SUCCESSO);
	}

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		
	}
}
