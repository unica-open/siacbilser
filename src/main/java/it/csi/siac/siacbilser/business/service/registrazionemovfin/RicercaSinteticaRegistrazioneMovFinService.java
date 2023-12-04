/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Ricerca sintetica di una RegistrazioneMovFin
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaRegistrazioneMovFinService extends CheckedAccountBaseService<RicercaSinteticaRegistrazioneMovFin, RicercaSinteticaRegistrazioneMovFinResponse> {
	
	@Autowired 
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	private RegistrazioneMovFin registrazioneMovFin;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRegistrazioneMovFin(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registrazione"));
		registrazioneMovFin = req.getRegistrazioneMovFin();
		checkEntita(registrazioneMovFin.getBilancio(), "bilancio");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		
		// SIAC-2761 - la gestione con il documento puo' non avere un evento univoco
		checkCondition((req.getTipoEvento() != null && req.getTipoEvento().getUid() != 0) ||(req.getEvento() != null && req.getEvento().getUid() != 0) || (req.getIdDocumento() != null && req.getIdDocumento().intValue() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Evento, tipo evento o documento"));
	
		
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaRegistrazioneMovFinResponse executeService(RicercaSinteticaRegistrazioneMovFin serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		registrazioneMovFinDad.setEnte(ente);
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		
		 ListaPaginata<RegistrazioneMovFin> list = registrazioneMovFinDad.ricercaSinteticaRegistrazioneMovFin(
				registrazioneMovFin,
				req.getTipoEvento(),
				req.getEvento(),
				req.getEventoRegistrazioneIniziale(),
				null,
				req.getIdDocumento(),
				req.getAnnoMovimento(),
				req.getNumeroMovimento(),
				req.getNumeroSubmovimento(),
				req.getDataRegistrazioneDa(),
				req.getDataRegistrazioneA(),
				null,
				req.getCapitolo(),
				req.getSoggetto(),
				req.getMovimentoGestione(),
				req.getSubmovimentoGestione(),
				req.getAttoAmministrativo(),
				req.getStrutturaAmministrativoContabile(),
				req.getParametriPaginazione()
				);
		
		 res.setRegistrazioniMovFin(list);
		
	}
	

}
