/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Servizio di aggiornamento di una PrimaNota Gen.
 * Rispetto a {@code AggiornaPrimaNotaService} controlla anche la correttenza del totale dei movimenti EP 
 * ripsetto al movimento finanziario sottostante.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrimaNotaGENService extends AggiornaPrimaNotaBaseService<AggiornaPrimaNota, AggiornaPrimaNotaResponse> {

	@Override
	@Transactional
	public AggiornaPrimaNotaResponse executeService(AggiornaPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		// Come in AggiornaPrimaNotaService
		checkImportiDareAvere();
		//SIAC-6195
		popolaAmbitoAPartireDalleRegistrazioni();
		checkStatoOperativo();
		checkBilancio();
		
		popolaMovimentiEP();
		primaNotaDad.aggiornaPrimaNota(primaNota);
		if(TipoCausale.Integrata.equals(primaNota.getTipoCausale())){
			aggiornaStatoRichiesta();
		}
		res.setPrimaNota(primaNota);
	}
	
	// SIAC-5802: l'importo puo' essere sforato, previa richiesta all'utente
//	@Override
//	protected void checkImportoMovimentoFinanziario(RegistrazioneMovFin registrazioneMovFin, BigDecimal totaleDare, BigDecimal totaleAvere) {
//		MovimentoHandler<? extends Entita> movimentoHandler = inizializzaMovimentoHandler(registrazioneMovFin); 
//		
//		BigDecimal importoMovimento = movimentoHandler.getImportoMovimento(registrazioneMovFin);
//		
//		if(importoMovimento == null){
//			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile ottenere l'importo del movimento finanziario."));
//		}
//		
//		if(totaleDare.compareTo(importoMovimento) != 0) {
//			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale movimenti EP deve corrispondere all'importo del movimento finanziario."));
//		}
//	}
	
	

//	private MovimentoHandler<? extends Entita> inizializzaMovimentoHandler(RegistrazioneMovFin registrazioneMovFin) {
//		
//		if(movimentoHandler!=null){
//			//MovimentoHanler gia' inizializzato.
//			return movimentoHandler;
//		}
//		MovimentoHandler<? extends Entita> movimentoHandler = MovimentoHandler.getInstance(serviceExecutor, req.getRichiedente(), ente, registrazioneMovFin.getBilancio(),
//				registrazioneMovFin.getMovimento().getClass(), 
//				registrazioneMovFin.getMovimentoCollegato()!=null?registrazioneMovFin.getMovimentoCollegato().getClass():null);
//		
//		movimentoHandler.caricaMovimento(registrazioneMovFin);
//		
//		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
//		registrazioniMovFin.add(registrazioneMovFin);
//		movimentoHandler.inizializzaDatiMovimenti(registrazioniMovFin);
//		
//		return movimentoHandler;
//	}
	
	

	
}
