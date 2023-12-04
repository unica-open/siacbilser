/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di aggiornamento di una PrimaNota
 * 
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrimaNotaService extends AggiornaPrimaNotaBaseService<AggiornaPrimaNota, AggiornaPrimaNotaResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
	}
	
	@Override
	@Transactional
	public AggiornaPrimaNotaResponse executeService(AggiornaPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		checkImportiDareAvere();
		//SIAC-6195
		popolaAmbitoAPartireDalleRegistrazioni();
		checkStatoOperativo();
		checkBilancio();
		
		//SIAC-8134
		popolaStrutturaCompetente();
		
		popolaMovimentiEP();
		primaNotaDad.aggiornaPrimaNota(primaNota);
		if(TipoCausale.Integrata.equals(primaNota.getTipoCausale())){
			aggiornaStatoRichiesta();
		}
		res.setPrimaNota(primaNota);
	}
	
	@Override
	protected void checkStatoOperativo() {
		//SIAC-6195 deciso con analista che per il completamento dei documenti deve valere il vecchio controllo
		StatoOperativoPrimaNota statoOperativoPrimaNota = primaNota.getStatoOperativoPrimaNota();
		boolean ambitoGSA = Ambito.AMBITO_GSA.equals(primaNota.getAmbito());
		boolean statoDefinitivo = StatoOperativoPrimaNota.DEFINITIVO.equals(statoOperativoPrimaNota);
		boolean statoProvvisorio = StatoOperativoPrimaNota.PROVVISORIO.equals(statoOperativoPrimaNota);
		// abilitare l'azione 'aggiorna' anche quando la prima nota è definitiva e di ambito GSA
		if(!(statoProvvisorio || (!req.isSaltaCheckStatoPerAmbito() && ambitoGSA && statoDefinitivo))){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore(new StringBuilder().append("stato incongruente. [Stato operativo movimento: ").append(statoOperativoPrimaNota!= null? statoOperativoPrimaNota.getDescrizione() : "null").append(" ].").toString()));
		}
	}
	
}
