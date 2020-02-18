/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Ricerca sintetica di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaPrimaNotaIntegrataValidabileService extends CheckedAccountBaseService<RicercaSinteticaPrimaNotaIntegrataValidabile, RicercaSinteticaPrimaNotaIntegrataValidabileResponse> {
	
	@Autowired 
	private PrimaNotaDad primaNotaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		checkNotNull(req.getPrimaNota().getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"));
		
		checkEntita(req.getTipoEvento(), "tipo evento");
	}
	
	
	@Override
	@Transactional(readOnly=true /**, timeout=3600*/)
	public RicercaSinteticaPrimaNotaIntegrataValidabileResponse executeService(RicercaSinteticaPrimaNotaIntegrataValidabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		primaNotaDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<PrimaNota> list;
		try {
			list = primaNotaDad.ricercaSinteticaPrimeNoteIntegrateValidabili(
					req.getBilancio(),
					req.getPrimaNota(),
					req.getConto(),
					req.getCausaleEP(),
					req.getTipoEvento(),
					Arrays.asList(req.getEvento()),
					req.getAnnoMovimento(),
					req.getNumeroMovimento(),
					req.getNumeroSubmovimento(),
					req.getRegistrazioneMovFin(),
					req.getDataRegistrazioneDa(),
					req.getDataRegistrazioneA(),
					//Nuovo SIAC-4644
					req.getAttoAmministrativo(),
					//Nuovo SIAC-5799
					req.getMovimentoGestione(),
					req.getSubMovimentoGestione(),
					req.getStrutturaAmministrativoContabile(),
					// SIAC-5292
					req.getCapitolo(),
					req.getParametriPaginazione());
		} catch(DadException de) {
			// Ignoro l'errore
			// TODO: loggare qualcosa?
			res.addErrore(ErroreBil.ERRORE_GENERICO.getErrore(de.getMessage()));
			return;
		}
		res.setPrimeNote(list);
	}
	

}
