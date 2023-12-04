/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Servizio di aggiornamento di una PrimaNota
 * 
 * @author Valentina
 *
 */
public abstract class AggiornaPrimaNotaBaseService<REQ extends AggiornaPrimaNota, RES extends AggiornaPrimaNotaResponse> extends PrimaNotaBaseService<REQ, RES> {
	
	//DADs
	@Autowired
	protected RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getPrimaNota(), "prima nota");
		primaNota = req.getPrimaNota();
		checkNotNull(primaNota.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero"), false);
		checkNotNull(primaNota.getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo causale"), false);
		checkNotNull(primaNota.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione"), false);
		checkNotNull(primaNota.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(primaNota.getStatoOperativoPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo"), false);
		
		if(primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				checkEntita(mov.getCausaleEP(), "causale", false);
				if(TipoCausale.Integrata.equals(primaNota.getTipoCausale())){
					checkEntita(mov.getRegistrazioneMovFin(), "registrazione");
				} else {
					checkNotNull(primaNota.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito prima nota"), false);
				}
				if(mov.getListaMovimentoDettaglio() != null){
					for(MovimentoDettaglio det : mov.getListaMovimentoDettaglio()){
						checkNotNull(det.getConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto movimento ep"), false);
						checkNotNull(det.getNumeroRiga(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero riga"), false);
						checkNotNull(det.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo"), false);
						checkNotNull(det.getSegno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("segno"), false);
					}
				}
				
			}
		}
		
		if(primaNota.getListaPrimaNotaFiglia() != null){
			for(PrimaNota pNota : primaNota.getListaPrimaNotaFiglia()){
				checkEntita(pNota, "prima nota figlia", false);
//				checkEntita(pNota.getTipoRelazionePrimaNota(), "tipo relazione prima nota");
			}
		}
		
		if(primaNota.getListaPrimaNotaPadre() != null){
			for(PrimaNota pNota : primaNota.getListaPrimaNotaPadre()){
				checkEntita(pNota, "prima nota padre", false);
//				checkEntita(pNota.getTipoRelazionePrimaNota(), "tipo relazione prima nota");
			}
		}
		
	}
	
	
	@Override
	protected void init() {
		primaNotaDad.setLoginOperazione(loginOperazione);
		primaNotaDad.setEnte(ente);
		
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
		registrazioneMovFinDad.setEnte(ente);
	}
	
	protected void popolaMovimentiEP() {
		if(primaNota.getListaMovimentiEP() == null){
			return;
		}
		for(MovimentoEP mov : primaNota.getListaMovimentiEP()){
			mov.setDescrizione(primaNota.getDescrizione());
			if(mov.getNumero() == null || mov.getNumero().intValue() == 0){
				Integer numero = primaNotaDad.staccaNumeroMovimentoEP(primaNota.getBilancio().getAnno(), primaNota.getAmbito());
				mov.setNumero(numero);
			}
		}
	}
	
	protected void aggiornaStatoRichiesta() {
		for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
			registrazioneMovFinDad.aggiornaStatoRegistrazioneMovFin(mov.getRegistrazioneMovFin().getUid(), StatoOperativoRegistrazioneMovFin.REGISTRATO);
		}
	}
	
	/**
	 * Popola ambito a partire dalle registrazioni.
	 * Ricava l'ambito dalla prima registrazione collegata ai movimentiEP.
	 * (si presume l'inserimento della primaNota per movimenti appartenenti a registrazioni dello stesso ambito).
	 * 
	 */
	protected void popolaAmbitoAPartireDalleRegistrazioni() { 
		//Evita di far passare l'Ambito in Request e ripsetta la congruenza dell'Ambito tra registrazioni e primeNote.
		if(TipoCausale.Integrata.equals(primaNota.getTipoCausale()) && primaNota.getListaMovimentiEP() != null){
			for(MovimentoEP mov: primaNota.getListaMovimentiEP()){
				Ambito ambito = registrazioneMovFinDad.findAmbitoByRegistrazioneMovFin(mov.getRegistrazioneMovFin().getUid());
				primaNota.setAmbito(ambito);
				return;
			}
		}
		
	}
	
	/**
	 * Controlla se lo stato operativo sia o meno congruente con lo
	 */
	protected void checkStatoOperativo() {
		//SIAC-6195
		StatoOperativoPrimaNota statoOperativoPrimaNota = primaNota.getStatoOperativoPrimaNota();
		boolean ambitoGSA = Ambito.AMBITO_GSA.equals(primaNota.getAmbito());
		boolean statoDefinitivo = StatoOperativoPrimaNota.DEFINITIVO.equals(statoOperativoPrimaNota);
		boolean statoProvvisorio = StatoOperativoPrimaNota.PROVVISORIO.equals(statoOperativoPrimaNota);
		// abilitare l'azione 'aggiorna' anche quando la prima nota è definitiva e di ambito GSA
		if(!(statoProvvisorio || (ambitoGSA && statoDefinitivo))){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore(new StringBuilder().append("stato incongruente. [Stato operativo movimento: ").append(statoOperativoPrimaNota!= null? statoOperativoPrimaNota.getDescrizione() : "null").append(" ].").toString()));
		}
	}

	protected void checkBilancio() {
		caricaBilancio();
		if(escludiSeFaseBilancioChiuso()
				|| FaseBilancio.PREVISIONE.equals(primaNota.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio())
				|| FaseBilancio.PLURIENNALE.equals(primaNota.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio())){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore("fase di bilancio incongruente."));
		}
	}


	protected boolean escludiSeFaseBilancioChiuso() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return !isPrimaNotaGSA() &&
						FaseBilancio.CHIUSO.equals(primaNota.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio());
	}
	
	private void caricaBilancio() {
		primaNota.setBilancio(bilancioDad.getBilancioByUid(primaNota.getBilancio().getUid()));
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
			for(MovimentoDettaglio det : movEP.getListaMovimentoDettaglio()){
				if(OperazioneSegnoConto.DARE.equals(det.getSegno())){
					totaleDare = totaleDare.add(det.getImporto());
				}else{
					totaleAvere = totaleAvere.add(det.getImporto());
				}
			}
			
			// SIAC-5887: modificati i controlli per non utilizzare l'equals (che non deve essere MAI usato con i BigDecimal)
			if(totaleDare.signum() == 0 || totaleDare.signum() == 0) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("devono essere presenti almeno due conti con segni differenti."));
			}
			if(totaleDare.compareTo(totaleAvere) != 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale DARE deve essere UGUALE al totale AVERE."));
			}
			
//			if(totaleDare.equals(BigDecimal.ZERO) || totaleAvere.equals(BigDecimal.ZERO)){
//				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("devono essere presenti almeno due conti con segni differenti."));
//			}
//			if(!totaleDare.equals(totaleAvere)){
//				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale DARE deve essere UGUALE al totale AVERE."));
//			}
			
			checkImportoMovimentoFinanziario(movEP.getRegistrazioneMovFin(), totaleDare, totaleAvere);
		}
	}


	protected void checkImportoMovimentoFinanziario(RegistrazioneMovFin registrazioneMovFin, BigDecimal totaleDare, BigDecimal totaleAvere) {
		//in questa implementazione non viene fatto alcuno controllo.
		
	}
	
}
