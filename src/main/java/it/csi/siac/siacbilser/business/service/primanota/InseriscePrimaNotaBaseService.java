/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Servizio base di inserimento di una PrimaNota
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/12/2017
 */
public abstract class InseriscePrimaNotaBaseService<REQ extends InseriscePrimaNota, RES extends InseriscePrimaNotaResponse> extends CheckedAccountBaseService<REQ, RES> {

	protected PrimaNota primaNota;
	
	@Autowired
	protected PrimaNotaDad primaNotaDad;
	@Autowired
	protected RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired
	private ContoDad contoDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		// Controlli di base
		checkNotNull(req.getPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prima nota"));
		primaNota = req.getPrimaNota();
		checkNotNull(primaNota.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo causale"), false);
		checkNotNull(primaNota.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(primaNota.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione"), false);
		checkCondition(primaNota.getDescrizione() == null || primaNota.getDescrizione().length() <= 500, ErroreCore.FORMATO_NON_VALIDO.getErrore("descrizione", "deve essere minore uguale a 500 caratteri."), false);
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				checkEntita(mov.getCausaleEP(), "causale");
				if(mov.getListaMovimentoDettaglio() != null){
					for(MovimentoDettaglio det : mov.getListaMovimentoDettaglio()){
						checkNotNull(det.getConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto movimento ep"));
						checkNotNull(det.getNumeroRiga(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero riga"));
						checkNotNull(det.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo"));
						checkNotNull(det.getSegno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("segno"));
					}
				}
				
			}
		}
		if(primaNota.getListaPrimaNotaFiglia() != null){
			for(PrimaNota pNota : primaNota.getListaPrimaNotaFiglia()){
				checkEntita(pNota, "prima nota figlia");
			}
		}
		
		if(primaNota.getListaPrimaNotaPadre() != null){
			for(PrimaNota pNota : primaNota.getListaPrimaNotaPadre()){
				checkEntita(pNota, "prima nota padre");
			}
		}
	}
	
	
	@Override
	protected void init() {
		primaNotaDad.setLoginOperazione(loginOperazione);
		primaNotaDad.setEnte(ente);
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
		registrazioneMovFinDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		contoDad.setEnte(ente);
	}
	

	protected boolean isPrimaNotaIntegrata() {
		return TipoCausale.Integrata.equals(primaNota.getTipoCausale());
	}
	
	protected void checkImportiDareAvere() {
		if(primaNota.getListaMovimentiEP() == null){
			return;
		}
		for(MovimentoEP movEP : primaNota.getListaMovimentiEP()){
			if(movEP.getListaMovimentoDettaglio() == null){
				continue;
			}
			
			BigDecimal totaleDare = BigDecimal.ZERO;
			BigDecimal totaleAvere = BigDecimal.ZERO;
			boolean dare = false;
			boolean avere = false;
			
			for(MovimentoDettaglio det : movEP.getListaMovimentoDettaglio()){
				if(OperazioneSegnoConto.DARE.equals(det.getSegno())){
					totaleDare = totaleDare.add(det.getImporto());
					dare = true;
				}else{
					totaleAvere = totaleAvere.add(det.getImporto());
					avere = true;
				}
			}
			
			if( !(dare && avere) ){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("devono essere presenti almeno due conti con segni differenti."));
			}
			if(totaleDare.compareTo(totaleAvere) != 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale DARE deve essere UGUALE al totale AVERE."));
			}
		}
	}

	protected void popolaMovimentiEP() {
		if(primaNota.getListaMovimentiEP() == null){
			return;
		}
		for(MovimentoEP mov : primaNota.getListaMovimentiEP()){
			mov.setDescrizione(primaNota.getDescrizione());
			Integer numero = primaNotaDad.staccaNumeroMovimentoEP(primaNota.getBilancio().getAnno(), primaNota.getAmbito());
			mov.setNumero(numero);
		}
	}


}
