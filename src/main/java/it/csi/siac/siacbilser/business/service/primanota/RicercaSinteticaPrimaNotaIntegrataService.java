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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Ricerca sintetica di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaPrimaNotaIntegrataService extends CheckedAccountBaseService<RicercaSinteticaPrimaNotaIntegrata, RicercaSinteticaPrimaNotaIntegrataResponse> {
	
	@Autowired 
	private PrimaNotaDad primaNotaDad;
	
	private PrimaNota primaNota;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
//		checkNotNull(primaNota.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo registrazione"));
	}
	
	
	@Override
	@Transactional(readOnly=true, timeout=120)
	public RicercaSinteticaPrimaNotaIntegrataResponse executeService(RicercaSinteticaPrimaNotaIntegrata serviceRequest) {
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
			list = primaNotaDad.ricercaSinteticaPrimeNoteIntegrate(
					req.getBilancio(),
					primaNota,
					req.getConto(),
					req.getTipoElenco(),
					req.getTipoEvento(),
					Arrays.asList(req.getEvento()),
					req.getAnnoMovimento(),
					req.getNumeroMovimento(),
					req.getNumeroSubmovimento(),
					req.getRegistrazioneMovFin(),
					req.getCausaleEP(),
					req.getDataRegistrazioneDa(),
					req.getDataRegistrazioneA(),
					req.getDataRegistrazioneProvvisoriaDa(),
					req.getDataRegistrazioneProvvisoriaA(),
					req.getDocumento() != null && req.getDocumento().getUid() != 0 ? Arrays.asList(Integer.valueOf(req.getDocumento().getUid())) : null,
					
					//Nuovi filtri SIAC-4644
					req.getAttoAmministrativo(),

					//SIAC-5799
					req.getMovimentoGestione(),	
					req.getSubMovimentoGestione(),
					req.getStrutturaAmministrativoContabile(),

					req.getSoggetto(),
					req.getImportoDocumentoDa(),
					req.getImportoDocumentoA(),
					
					// SIAC-5291
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
