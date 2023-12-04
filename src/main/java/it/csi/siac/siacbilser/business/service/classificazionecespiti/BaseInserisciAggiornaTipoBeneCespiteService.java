/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CategoriaCespitiDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class BaseInserisciAggiornaTipoBeneCespiteService.
 *
 * @author elisa
 * @version 1.0.0 - 25-06-2018
 * @param <REQUEST> the generic type
 * @param <RESPONSE> the generic type
 */
public abstract class BaseInserisciAggiornaTipoBeneCespiteService<REQUEST extends ServiceRequest, RESPONSE extends ServiceResponse> extends CheckedAccountBaseService<REQUEST, RESPONSE> {

	//DAD
	@Autowired
	protected TipoBeneCespiteDad tipoBeneCespiteDad;
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	protected CategoriaCespitiDad categoriaCespitiDad;
	@Autowired
	protected CausaleEPDad causaleEPDad;

	protected TipoBeneCespite tipoBeneCespite;
	protected Date inizioAnnoBilancio;
	
	private static final String EVENTO_INV_COGE = "INV-COGE";
	
	@Override
	protected void init() {
		super.init();
		tipoBeneCespiteDad.setEnte(ente);
		tipoBeneCespiteDad.setLoginOperazione(loginOperazione);		
		contoDad.setEnte(ente);
		categoriaCespitiDad.setEnte(ente);
	}
	
	protected void checkContiTipoBene() {
		Conto contoPatrimoniale = caricaContoSeEsistente(tipoBeneCespite.getContoPatrimoniale(),ContoTipoBeneSelector.Patrimoniale);
		checkUnivocitaContoPatrimoniale(contoPatrimoniale);
		tipoBeneCespite.setContoPatrimoniale(contoPatrimoniale);
		
		Conto contoAmmortamento = caricaContoSeEsistente(tipoBeneCespite.getContoAmmortamento(), ContoTipoBeneSelector.Ammortamento);
		tipoBeneCespite.setContoAmmortamento(contoAmmortamento);
		
		Conto contoFondoAmmortamento = caricaContoSeEsistente(tipoBeneCespite.getContoFondoAmmortamento(), ContoTipoBeneSelector.Fondo_Ammortamento);
		tipoBeneCespite.setContoFondoAmmortamento(contoFondoAmmortamento);
		
		Conto contoIncremento = caricaContoSeEsistente(tipoBeneCespite.getContoIncremento(), ContoTipoBeneSelector.Incremento);
		tipoBeneCespite.setContoIncremento(contoIncremento);
		
		Conto contoDecremento = caricaContoSeEsistente(tipoBeneCespite.getContoDecremento(), ContoTipoBeneSelector.Decremento);
		tipoBeneCespite.setContoDecremento(contoDecremento);
		
		Conto contoMinusvalenza = caricaContoSeEsistente(tipoBeneCespite.getContoMinusValenza(), ContoTipoBeneSelector.MinusValenza);
		tipoBeneCespite.setContoMinusValenza(contoMinusvalenza);
		
		Conto contoPlusvalenza = caricaContoSeEsistente(tipoBeneCespite.getContoPlusvalenza(), ContoTipoBeneSelector.PlusValenza);
		tipoBeneCespite.setContoPlusvalenza(contoPlusvalenza);
		
		
		Conto contoAlienazione = caricaContoSeEsistente(tipoBeneCespite.getContoAlienazione(), ContoTipoBeneSelector.Alienazione);
		tipoBeneCespite.setContoAlienazione(contoAlienazione);
		
		Conto contoDonazione = caricaContoSeEsistente(tipoBeneCespite.getContoDonazione(), ContoTipoBeneSelector.Donazione);
		tipoBeneCespite.setContoDonazione(contoDonazione);
		
	}
	
	private void checkUnivocitaContoPatrimoniale(Conto contoPatrimoniale) {
		// il conto patrimoniale deve essere associato ad un solo tipo bene
		if(contoPatrimoniale == null || StringUtils.isBlank(contoPatrimoniale.getCodice())) {
			return;
		}
		TipoBeneCespite t = new TipoBeneCespite();
		t.setDataInizioValiditaFiltro(inizioAnnoBilancio);
		ListaPaginata<TipoBeneCespite> tipiBeneCespite = tipoBeneCespiteDad.ricercaSinteticaTipoBeneCespite(t, contoPatrimoniale, null, new TipoBeneCespiteModelDetail[] {}, new ParametriPaginazione(0, 1));
//		if(tipiBeneCespite != null && !tipiBeneCespite.isEmpty() && tipiBeneCespite.get(0) != null) {
//			throw new BusinessException("Esiste gia il tipo bene " + tipiBeneCespite.get(0).getCodice() + " associato a questo conto patrimoniale.");
//		}
	}

	/**
	 * Carica conto se esistente.
	 *
	 * @param contoPatrimoniale the conto patrimoniale
	 * @param contoSelector the conto selector
	 * @return the conto
	 */
	private Conto caricaContoSeEsistente(Conto contoPatrimoniale, ContoTipoBeneSelector contoSelector) {
		if(contoPatrimoniale == null || StringUtils.isBlank(contoPatrimoniale.getCodice())) {
			return null;
		}
		Conto contoCaricato = ottieniConto(contoPatrimoniale, contoSelector);
		if(contoCaricato == null) {
			StringBuilder msg = new StringBuilder().append("Impossibile trovare un unico conto con codice: ")
					.append(contoPatrimoniale.getCodice())
					.append(" e con le seguenti caratteristiche: ")
					.append(contoSelector.getDescrizioneTipoContoSelector());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore(msg.toString()));
		}
		return contoCaricato;
			
	}

	private Conto ottieniConto(Conto contoPatrimoniale, ContoTipoBeneSelector contoSelector) {
		final String methodName = "checkSingoloConto";
		if(contoPatrimoniale == null || StringUtils.isBlank(contoPatrimoniale.getCodice())) {
			//il conto non e' obbligatorio
			log.debug(methodName, "conto  " +  contoSelector.name() + " non selezionato: esco.");
			return null;
		}
		Conto contoRicerca = creaContoPerRicerca(contoPatrimoniale.getCodice(), contoSelector);
		ListaPaginata<Conto> ricercaSinteticaConto = contoDad.ricercaSinteticaConto(contoRicerca, new ParametriPaginazione(0,1));
		if(ricercaSinteticaConto == null || ricercaSinteticaConto.getTotaleElementi() != 1) {
			return null;
		}
		return ricercaSinteticaConto.get(0);
	}
	
	/**
	 * Crea conto per ricerca.
	 *
	 * @param codice the codice
	 * @param contoSelector the conto selector
	 * @return the conto
	 */
	private Conto creaContoPerRicerca(String codice, ContoTipoBeneSelector contoSelector) {
		Conto conto = new Conto();
		conto.setCodice(codice);
		conto.setAmbito(Ambito.AMBITO_FIN);
		conto.setContoFoglia(contoSelector.getContoFoglia());
		conto.setAttivo(contoSelector.getAttivo());
		conto.setAmmortamento(contoSelector.getAmmortamento());
		conto.setDataInizioValiditaFiltro(Utility.primoGiornoDellAnno(req.getAnnoBilancio()));
		conto.setEnte(ente);
		//TODO: aggiungere campi  CR
		return conto;
	}

	
	
	protected void caricaEventiSePresenti() {
		Evento eventoAmmortamento =  caricaEvento(tipoBeneCespite.getEventoAmmortamento());
		tipoBeneCespite.setEventoAmmortamento(eventoAmmortamento);
		Evento eventoIncremento = caricaEvento(tipoBeneCespite.getEventoIncremento());
		tipoBeneCespite.setEventoIncremento(eventoIncremento);
		Evento eventoDecremento =caricaEvento(tipoBeneCespite.getEventoDecremento());
		tipoBeneCespite.setEventoDecremento(eventoDecremento);
		
	}
	protected void checkEventi() {
		checkTipoEventoCorrettoPerTipoBene(tipoBeneCespite.getEventoAmmortamento());
		checkTipoEventoCorrettoPerTipoBene(tipoBeneCespite.getEventoIncremento());
		checkTipoEventoCorrettoPerTipoBene(tipoBeneCespite.getEventoDecremento());
	}

	/**
	 * Carica E controlla evento.
	 *
	 * @param evento the evento
	 * @return the evento
	 */
	private Evento caricaEvento(Evento evento) {
		if(evento == null || evento.getUid() == 0) {
			return null;
		}
		return tipoBeneCespiteDad.findEventoByUid(evento);
		
	}

	/**
	 * Check tipo evento corretto per tipo bene.
	 *
	 * @param eventoCaricato the evento caricato
	 */
	private void checkTipoEventoCorrettoPerTipoBene(Evento eventoCaricato) {
		if(eventoCaricato != null && eventoCaricato.getTipoEvento() != null && !EVENTO_INV_COGE.equals(eventoCaricato.getTipoEvento().getCodice())){
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("L'evento " + eventoCaricato.getCodice()  + " e' legato ad un tipo evento non compatibile con il tipo bene"));
		}
	}
	
	
	/**
	 * Check categoria.
	 */
	protected void checkCategoria() {
		if(tipoBeneCespite == null || tipoBeneCespite.getCategoriaCespiti() == null) {
			return;
		}
		CategoriaCespiti categoriaCaricata = categoriaCespitiDad.findCategoriaCespitiById(tipoBeneCespite.getCategoriaCespiti().getUid(), Utility.primoGiornoDellAnno(req.getAnnoBilancio()), new CategoriaCespitiModelDetail[] {CategoriaCespitiModelDetail.Annullato});
		if(categoriaCaricata == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("categoria cespiti da associare al tipo bene", ""));
		}
		if(Boolean.TRUE.equals(categoriaCaricata.getAnnullato())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("categoria cespiti", "annullato"));
		}
		tipoBeneCespite.setCategoriaCespiti(categoriaCaricata);
	}
	
	/**
	 * Carica primo giorno dell anno.
	 */
	protected void caricaPrimoGiornoDellAnno() {
		inizioAnnoBilancio = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		if(inizioAnnoBilancio == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("imposibile determinare il primo giorno dell'anno di bilancio. "));
		}
	}
	
	protected void impostaValoriDiDefault() {
		tipoBeneCespite.setDataInizioValidita(inizioAnnoBilancio);
	}
	
	protected void caricaCausali() {
		tipoBeneCespite.setCausaleAmmortamento(caricaCausale(tipoBeneCespite.getCausaleAmmortamento()));
		tipoBeneCespite.setCausaleDecremento(caricaCausale(tipoBeneCespite.getCausaleDecremento()));
		tipoBeneCespite.setCausaleIncremento(caricaCausale(tipoBeneCespite.getCausaleIncremento()));
	}

	/**
	 * @param causaleAmmortamento2
	 */
	private CausaleEP caricaCausale(CausaleEP causaleDaCaricare) {
		if(causaleDaCaricare == null || causaleDaCaricare.getUid() == 0) {
			return null;
		}
		return causaleEPDad.findCausaleEPByIdModelDetail(causaleDaCaricare, new CausaleEPModelDetail[] {});
	}


}