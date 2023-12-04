/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.StampaAllegatoAttoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;

/**
 * The Class InserisceStampa Allegato Atto Service.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceStampaAllegatoAttoService extends CheckedAccountBaseService<InserisceStampaAllegatoAtto, InserisceStampaAllegatoAttoResponse> {
	
	@Autowired
	StampaAllegatoAttoDad stampaAllegatoAttoDad;

	private AllegatoAttoStampa allegatoAttoStampa;
		
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		allegatoAttoStampa = req.getAllegatoAttoStampa();
		checkNotNull(allegatoAttoStampa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa allegato atto"));
		
		checkNotNull(allegatoAttoStampa.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente stampa allegato atto"));
		checkCondition(allegatoAttoStampa.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente stampa allegato atto"));
		
		checkNotNull(allegatoAttoStampa.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto stampa"));
		checkCondition(allegatoAttoStampa.getAllegatoAtto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"));
		checkNotNull(allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo allegato atto stampa"));
		checkNotNull(StringUtils.trimToNull(allegatoAttoStampa.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stampa allegato atto"));
		checkNotNull(allegatoAttoStampa.getAnnoEsercizio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio stampa allegato atto"));
		checkNotNull(allegatoAttoStampa.getVersioneInvioFirma(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("versione invio firma"));
		checkNotNull(allegatoAttoStampa.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa"));
		
		
		checkNotNull(allegatoAttoStampa.getFiles(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("file stampa"));
	}
	
	@Override
	protected void init() {
		stampaAllegatoAttoDad.setLoginOperazione(loginOperazione);
		stampaAllegatoAttoDad.setEnte(allegatoAttoStampa.getEnte());
		stampaAllegatoAttoDad.setBilancio(allegatoAttoStampa.getBilancio());
	}
	
	@Transactional
	@Override
	public InserisceStampaAllegatoAttoResponse executeService(InserisceStampaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		stampaAllegatoAttoDad.inserisciAllegatoAttostampa(allegatoAttoStampa);
		res.setAllegatoAttoStampa(allegatoAttoStampa);
	}

}
