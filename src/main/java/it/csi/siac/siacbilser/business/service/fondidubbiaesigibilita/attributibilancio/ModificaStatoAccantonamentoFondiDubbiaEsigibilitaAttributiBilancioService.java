/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaGestioneDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Modifica dello stato degli attributi FCDE per il bilancio
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaGestioneDad accantonamentoFondiDubbiaEsigibilitaGestioneDad;

	@Override
	@Transactional
	public ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setEnte(ente);
		
		accantonamentoFondiDubbiaEsigibilitaGestioneDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaGestioneDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita(), "stato accantonamento attributi bilancio", false);
	}
	
	@Override
	protected void execute() {
		// Load dato attuale
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		checkBusinessCondition(!current.getStatoAccantonamentoFondiDubbiaEsigibilita().equals(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita()), ErroreCore.VALORE_NON_CONSENTITO.getErrore("Stato", "non puo' essere pari al valore attuale"));
		current.setStatoAccantonamentoFondiDubbiaEsigibilita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita());
		
		if(StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA.equals(current.getStatoAccantonamentoFondiDubbiaEsigibilita())) {
			// Effettua operazioni per passaggio in definitivo
			passaInDefinitivo(current);
		}
		
		current = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.update(current);
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(current);
	}

	private void passaInDefinitivo(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current) {
		// Modifica gli stati degli altri accantonamenti
		passaDefinitiviPreEsistentiInBozza(current);
		//TODO ha davvero senso questo metodo? continua a svuotarmi tutti gli accantonamenti in gestione impostando una data cancellazione
//		aggiornaAccantonamentiGestione(current);
		
		
		
	}

	@SuppressWarnings("unused")
	private void aggiornaAccantonamentiGestione(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current) {
		if(!TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE.equals(current.getTipoAccantonamentoFondiDubbiaEsigibilita())) {
			return;
		}
		Utility.MDTL.addModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.CapitoloEntrataGestione,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.Tipo,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMediaConfronto);
		
		// Tutti gli accantonamenti di tipo GESTIONE devono essere aggiornati
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamenti = accantonamentoFondiDubbiaEsigibilitaGestioneDad.ricercaAccantonamentoFondiDubbiaEsigibilita(current);
		for(AccantonamentoFondiDubbiaEsigibilitaBase<?> afdeb : accantonamenti) {
			AccantonamentoFondiDubbiaEsigibilitaGestione afdeg = (AccantonamentoFondiDubbiaEsigibilitaGestione) afdeb;

			// TODO se il confronto fosse ereditato dalla previsione (credo sia probabile avere un 100) con la max avremmo di nuovo un 100 come massima media di confronto
			// La percentuale al fondo FCDE deve essere minima. Essendo 100-media, la media deve essere massima
			BigDecimal massimoMedie = Utility.max(
					afdeg.getMediaSempliceTotali(), 
					afdeg.getMediaSempliceRapporti(), 
					afdeg.getMediaPonderataTotali(), 
					afdeg.getMediaPonderataRapporti(), 
					afdeg.getMediaUtente()//, 
//					afdeg.getMediaConfronto()
				);
			afdeg.setMediaConfronto(massimoMedie);
			afdeg.setTipoMediaConfronto(TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
			// forzo la validita'
//			afdeg.setDataCancellazione(null);s
//			afdeg.setDataFineValidita(null);
//			accantonamentoFondiDubbiaEsigibilitaGestioneDad.update(afdeg);
		}
	}

	private void passaDefinitiviPreEsistentiInBozza(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current) {
		ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> lista = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad
				.ricercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
					current,
					Boolean.TRUE,
					new ParametriPaginazione(0, Integer.MAX_VALUE),
					AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
					AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
					AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		//SIAC-8421
		lista = adeguamentoListaPerPassaggioInBozzaPerGestione(lista, current);
		
		for(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeb : lista) {
			afdeb.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.update(afdeb);
		}
	}
	
	private ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> adeguamentoListaPerPassaggioInBozzaPerGestione(
			ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> lista, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio current) {
		
		ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> listaDefinitive = new ListaPaginataImpl<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio>();
		// Se non ho valori esco
		if(lista == null) {
			return listaDefinitive;
		}
		
		if(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE.equals(current.getTipoAccantonamentoFondiDubbiaEsigibilita())) {
			
			// Filtro la lista
			for (AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accAttrBil : lista) {
				if(StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA.equals(accAttrBil.getStatoAccantonamentoFondiDubbiaEsigibilita())) {
					listaDefinitive.add(accAttrBil);
				}
			}
			
			// Se non ho definitive esco
			if(listaDefinitive.size() == 0) {
				return listaDefinitive;
			}
			
			// controllo su quante versioni definitive sono presenti
			checkBusinessCondition(!(listaDefinitive.size() >= 2), 
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Esistono " + listaDefinitive.size() + " calcoli FCDE in assestamento definitivi"));
		
			// se sono qui la mia lista ha un numero di elementi pari a 1
			// controllo che il numero di versione corrente sia maggiore rispetto a quelli estratti
			checkBusinessCondition(!(current.getVersione().compareTo(listaDefinitive.get(0).getVersione()) < 0), 
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("E' presente la versione " + listaDefinitive.get(0).getVersione() + 
							" in stato Definitivo con numerazione successiva a quella corrente per l'assestamento"));
			
			// se la scelta e' di renderla definitiva devo passare una lista vuota per impedirne il ritorno in bozza della precedente
			if("RENDI_DEFINITIVA".equals(req.getSceltaUtente())) {
				return new ListaPaginataImpl<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio>();
			}

			// tutto quello che resta in questa lista passera' con lo stato a BOZZA
			return listaDefinitive;
		}
		
		// se non sono in GESTIONE ritorno la lista passata
		return lista;
	}

}
