/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Servizio di aggiornamento di una PrimaNota
 * 
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrimaNotaIntegrataManualeService extends AggiornaPrimaNotaBaseService<AggiornaPrimaNotaIntegrataManuale, AggiornaPrimaNotaIntegrataManualeResponse> {
	
	@Autowired
	private CodificaDad codificaDad;
	
	private Evento evento;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		
		checkCondition(primaNota.getListaMovimentiEP() != null && !primaNota.getListaMovimentiEP().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("movimenti ep"));
		
		checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito"), false);
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				checkEntita(mov.getRegistrazioneMovFin().getMovimento(), "movimento registrazione", false);
			}
		}
	}
	
	@Override
	protected void init() {
		super.init();
		codificaDad.setEnte(ente);
	}
	
	@Override
	@Transactional
	public AggiornaPrimaNotaIntegrataManualeResponse executeService(AggiornaPrimaNotaIntegrataManuale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		checkImportiDareAvere();
		checkStatoOperativo();
		checkBilancio();
		
		checkEvento();
		
		popolaMovimentiEP();
		
		// La registrazione non e' aggiornabile
		primaNotaDad.aggiornaPrimaNota(primaNota);
		if(TipoCausale.Integrata.equals(primaNota.getTipoCausale())){
			aggiornaStatoRichiesta();
		}
		res.setPrimaNota(primaNota);
	}
	
	private void checkEvento() {
		// Per sicurezza ripristino il PRECEDENTE EVENTO associato alla prima nota
		evento = findEventoOld();
		// Reimposto il valore nelle registrazioni
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			mov.getRegistrazioneMovFin().setEvento(evento);
		}
	}
	
	/**
	 * Restituisce il precedente evento
	 */
	private Evento findEventoOld() {
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			Evento e = registrazioneMovFinDad.findEventoByRegistrazioneMovFin(mov.getRegistrazioneMovFin());
			if(e != null) {
				return e;
			}
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessun evento corrispondente alla prima nota con uid [" + primaNota.getUid() + "]"));
	}
	
}
