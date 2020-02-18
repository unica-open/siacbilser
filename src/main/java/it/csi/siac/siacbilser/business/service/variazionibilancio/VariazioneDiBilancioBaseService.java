/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Nuova implementazione di {@link VariazioneBilancioBaseService}.
 *
 * @author Domenico Lisi
 * 
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class VariazioneDiBilancioBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	/** The core service. */
	@Autowired protected CoreService coreService;
	/** The variazioni dad. */
	@Autowired protected VariazioniDad variazioniDad;
	/** The importi capitolo dad. */
	@Autowired protected ImportiCapitoloDad importiCapitoloDad;
	/** The provvedimento dad. */
	@Autowired protected ProvvedimentoDad provvedimentoDad;
	/** The capitolo dad. */
	@Autowired protected CapitoloDad capitoloDad;
	/** The capitolo dad. */
	@Autowired protected ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	/** The variazione. */
	protected VariazioneImportoCapitolo variazione;
	
	//--- Check importi Variazione -----------------------------------------------------------------
	

	/**
	 * Controlla la congruità della variazione.
	 */
	protected void checkImporti() {
		final String methodName = "checkImporti";
		//SIAC-3044
		if(StatoOperativoVariazioneDiBilancio.ANNULLATA.equals(variazione.getStatoOperativoVariazioneDiBilancio())){
			log.info(methodName, "Sto annullando la Variazione. Salto i check sugli importi e procedo.");
			return;
		}

		List<Integer> ids = new ArrayList<Integer>();
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			ids.add(dettVarImp.getCapitolo().getUid());
		}
		
		// SIAC-6883: eliminato l'anno
		Map<String, BigDecimal> mappaImporti = variazioniDad.findStanziamentoVariazioneByUidCapitoloAndUidVariazione(ids, variazione.getUid());
		
		for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
			
			checkVariazioneImporti(dettVarImp, mappaImporti);
		}
	}

	
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importi 
	 */
	protected void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, Map<String, BigDecimal> importi) {
		boolean isCapitoloFondino = TipoCapitolo.isTipoCapitoloUscita(dettVarImp.getCapitolo()) && capitoloDad.isCapitoloFondino(dettVarImp.getCapitolo().getUid());
		checkVariazioneImporti(dettVarImp,importi, isCapitoloFondino);
	}
	/**
	 * Check variazione importi.
	 *
	 * @param dettVarImp the dett var imp
	 * @param importi 
	 */
	protected void checkVariazioneImporti(DettaglioVariazioneImportoCapitolo dettVarImp, Map<String, BigDecimal> importi, boolean capitoloFondino) {
		// SIAC-6883
		final String methodName = "checkVariazioneImporti";
		Capitolo<?,?> c = dettVarImp.getCapitolo();
		
		boolean isCapitoloUscita = TipoCapitolo.isTipoCapitoloUscita(c);
		
		//SIAC-7277
		if(isCapitoloUscita && capitoloFondino) {
			//NOTA: IN REALTA' QUI DOVREBBE ESSERCI IL CONTROLLO EFFETTUATO DA checkVariazioniCapitoliFondino
			//XXX: DA SPOSTARE APPENA POSSIBILE
			log.info(methodName, "capitolo fondino, salto il controllo sugli importi");
			return;
		}
		
		
		BigDecimal disponibilitaVariare;
		BigDecimal disponibilitaVariare1;
		BigDecimal disponibilitaVariare2;
		// Per cassa e residuo controllo solo per l'anno di bilancio
		BigDecimal disponibilitaVariareCassa;
		BigDecimal disponibilitaVariareResiduo = c.getImportiCapitolo().getStanziamentoResiduo();
		
		//SIAC-7267
		if(isCapitoloUscita) {
			disponibilitaVariare = c.getImportiCapitolo().getDisponibilitaVariareAnno1();
			disponibilitaVariare1 = c.getImportiCapitolo().getDisponibilitaVariareAnno2();
			disponibilitaVariare2 = c.getImportiCapitolo().getDisponibilitaVariareAnno3();
			disponibilitaVariareCassa = c.getImportiCapitolo().getDisponibilitaVariareCassa();
		} else {
			// Per l'entrata mi interessa controllare solo che non vadano a 0 gli stanziamenti.
			disponibilitaVariare = c.getImportiCapitolo().getStanziamento();
			disponibilitaVariare1 = c.findImportiCapitoloPerAnno(Integer.valueOf(variazione.getBilancio().getAnno() + 1)).getStanziamento();
			disponibilitaVariare2 = c.findImportiCapitoloPerAnno(Integer.valueOf(variazione.getBilancio().getAnno() + 2)).getStanziamento();
			disponibilitaVariareCassa = c.getImportiCapitolo().getStanziamentoCassa();
		}
		
		//SIAC-7267: i capitoli fondino non usano la disponibilita a variare, non devo adeguarla
		boolean capitoloPrecedentementeAssociatoAVariazione = variazioniDad.checkCapitoloAssociatoAllaVariazione(c.getUid(), variazione.getUid());
		log.debug(methodName, "il capitolo con uid " + c.getUid() +  " e' stato precedentemente associato alla variazione? " + capitoloPrecedentementeAssociatoAVariazione);
		
		if(isCapitoloUscita && capitoloPrecedentementeAssociatoAVariazione) {
			//adeguo la disponibilita' a variare solo se il capitolo e' gia associato alla variazione SIAC-3375
			BigDecimal stanziamento = importi.get(variazione.getBilancio().getAnno() + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamento1 = importi.get((variazione.getBilancio().getAnno() + 1) + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamento2 = importi.get((variazione.getBilancio().getAnno() + 2) + "_" + c.getUid() + "_" + "STA");
			BigDecimal stanziamentoCassa = importi.get(variazione.getBilancio().getAnno() + "_" + c.getUid() + "_" + "SCA");
			
			log.debug(methodName, "Adeguamento disponibilita variare e cassa per capitolo con uid: " + c.getUid());
			log.debug(methodName, "Adeguo la disponibilitaVariare prendendo come stanziamento " + stanziamento +  " e disponibilitaVariare " + disponibilitaVariare + "per il capitolo con uid: " + c.getUid());
			
			//SIAC-3044
			disponibilitaVariare = adeguaDisponibilitaVariare(stanziamento, disponibilitaVariare, "disponibilitaVariare", c, 0);
			disponibilitaVariare1 = adeguaDisponibilitaVariare(stanziamento1, disponibilitaVariare1, "disponibilitaVariare", c, 1);
			disponibilitaVariare2 = adeguaDisponibilitaVariare(stanziamento2, disponibilitaVariare2, "disponibilitaVariare", c, 2);
			disponibilitaVariareCassa = adeguaDisponibilitaVariare(stanziamentoCassa, disponibilitaVariareCassa, "disponibilitaVariareCassa", c, 0);
			
			log.debug(methodName, "Adeguo la disponibilitaVariareCassa prendendo come  stanziamentoCassa " + stanziamentoCassa +  " e disponibilitaVariareCassa " + disponibilitaVariareCassa + "per il capitolo con uid: " + c.getUid());
		}
		
		log.debug(methodName, "check Stanziamento: disponibilitaVariare + variazioneStanziamento >= 0 ");
		
		Errore errore = getErroreTemplate(dettVarImp);
		checkVariazioneImporto(disponibilitaVariare, dettVarImp.getStanziamento(), errore, "stanziamento", variazione.getBilancio().getAnno());
		checkVariazioneImporto(disponibilitaVariare1, dettVarImp.getStanziamento1(), errore, "stanziamento", variazione.getBilancio().getAnno() + 1);
		checkVariazioneImporto(disponibilitaVariare2, dettVarImp.getStanziamento2(), errore, "stanziamento", variazione.getBilancio().getAnno() + 2);
		
		checkVariazioneImporto(disponibilitaVariareResiduo, dettVarImp.getStanziamentoResiduo(), errore, "residuo", variazione.getBilancio().getAnno());
		checkVariazioneImporto(disponibilitaVariareCassa,dettVarImp.getStanziamentoCassa(), errore, "cassa", variazione.getBilancio().getAnno());
		
		boolean isCassaOk = dettVarImp.getStanziamento().setScale(2,RoundingMode.HALF_DOWN)
			.add(dettVarImp.getStanziamentoResiduo().setScale(2,RoundingMode.HALF_DOWN))
			.compareTo(dettVarImp.getStanziamentoCassa().setScale(2,RoundingMode.HALF_DOWN))<=0;
		
		log.debug(methodName, "check Competenza + Residuo <= Cassa ? "+ isCassaOk);
		
		// TODO: segnalazione SIAC-2105 - controllo non chiaro. In attesa di nuove indicazioni
//		if(!isCassaOk) {
//			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("l'importo di Cassa deve essere maggiore o uguale all'importo di competenza piu'' l'importo residuo"));
//		}
	}

	protected BigDecimal adeguaDisponibilitaVariare(BigDecimal stanziamento, BigDecimal disponibilita, String tipoDisponibilita, Capitolo<?, ?> capitolo, int delta) {
		//SIAC-3044
		final String methodName = "adeguaDisponibilitaVariare";
		
		if(variazione.getUid() == 0){
			log.debug(methodName, "Sto inserendo. Non devo adeguare l'importo.");
			return disponibilita;
		}
		log.debug(methodName, "Adeguo la " + tipoDisponibilita + " prendendo come stanziamento " + stanziamento +  " e disponibilita' " + disponibilita + " per il capitolo " + capitolo.getUid()
			+ " per l'anno " + (variazione.getBilancio().getAnno() + delta));
		
		//Sono in aggiornamento di una variazione.
		StatoOperativoVariazioneDiBilancio statoAttuale = variazioniDad.findStatoOperativoVariazioneDiBilancio(variazione);
		
		//solo se la variazione e' negativa
		if(stanziamento.compareTo(BigDecimal.ZERO) < 0
				&& (StatoOperativoVariazioneDiBilancio.BOZZA.equals(statoAttuale)
						|| StatoOperativoVariazioneDiBilancio.GIUNTA.equals(statoAttuale)
						|| StatoOperativoVariazioneDiBilancio.CONSIGLIO.equals(statoAttuale)
						// CR-3260
						|| StatoOperativoVariazioneDiBilancio.PRE_DEFINITIVA.equals(statoAttuale)
					)){ 
			BigDecimal disponibilitaVariareNew = disponibilita.setScale(2,RoundingMode.HALF_DOWN)
					.add(stanziamento.setScale(2,RoundingMode.HALF_DOWN).abs());
			
			log.info(methodName, "Variazione in " + statoAttuale + ". La disponibilitaVariare viene calcolata sottraendo il valore dello stanziamento della varizione. " 
					+ Utility.formatCurrencyAsString(disponibilita) + " (PRIMA) > " + Utility.formatCurrencyAsString(disponibilitaVariareNew) + " (DOPO)");
			
			return disponibilitaVariareNew;
		}
		return disponibilita;
	}
	
	private Errore getErroreTemplate(DettaglioVariazioneImportoCapitolo dettVarImp) {
		//SIAC-3227
		String descCapitolo;
		if(isGestioneUEB()){
			descCapitolo = dettVarImp.getCapitolo().getDescBilancioAnnoNumeroArticoloUEB();
		} else {
			descCapitolo = dettVarImp.getCapitolo().getDescBilancioAnnoNumeroArticolo();
		}
		
		return ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("Stanziamento residuo insufficiente per il capitolo " + descCapitolo.replaceFirst("null/", "") + ".");
	}

	/**
	 * Se disponibilitaVariare sommato a importoVariazione restituisce un numero negativo viene sollevata l'eccezione di business con l'errore passato come parametro.
	 *
	 * @param disponibilitaVariareCheck la disponibilita a variare
	 * @param importoVariazione  l'importo della variazione
	 * @param errore l'errore di business da sollevare.
	 */
	protected void checkVariazioneImporto(BigDecimal disponibilitaVariare, BigDecimal importoVariazione, Errore errore, String tipoStanziamento, int anno) {
		final String methodName = "checkVariazioneImporto";
		
		String msgLog = "ANNO %d. TIPO %s. %.2f + (%.2f) >= 0 ? %s";
		
		if(importoVariazione == null) {
			log.debug(methodName, String.format(msgLog, anno, tipoStanziamento, disponibilitaVariare, null, null));
			return;
		}
		BigDecimal disponibilitaVariareCheck = disponibilitaVariare == null ? BigDecimal.ZERO : disponibilitaVariare;
		
		boolean isOk = disponibilitaVariareCheck.setScale(2,RoundingMode.HALF_DOWN).add(importoVariazione.setScale(2,RoundingMode.HALF_DOWN)).compareTo(BigDecimal.ZERO) >= 0;
		
		log.debug(methodName,  String.format(msgLog, anno, tipoStanziamento, disponibilitaVariareCheck, importoVariazione, isOk));
		String erroreFormat = "%sDisponibilita' variare (anno %d, %s): %.2f; importo variazione (%s): %.2f";
		
		if(!isOk){
			errore.setDescrizione(String.format(erroreFormat, errore.getDescrizione(), anno, tipoStanziamento, disponibilitaVariareCheck, tipoStanziamento, importoVariazione));
			throw new BusinessException(errore);
		}
	}
	
	/**
	 * Controlla se la differenza tra gli stanziamenti di entrata e uscita &eacute; uguale a zero.
	 * 
	 * @return true se la quadratura &egrave; corretta false altrimenti.
	 * 
	 */
	protected boolean isQuadraturaCorrettaStanziamento() {
		final String methodName = "isQuadraturaCorrettaStanziamento";
		BigDecimal sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata();
		BigDecimal sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita();
		log.debug(methodName, "Quadratura per anno 0. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 0. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		
		// SIAC-6883: aggiunti controlli per anno + 1 e anno + 2
		sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata1();
		sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita1();
		log.debug(methodName, "Quadratura per anno 1. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 1. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		
		sommaStanziamentoEntrata = variazione.calcolaSommaStanziamentoEntrata2();
		sommaStanziamentoUscita = variazione.calcolaSommaStanziamentoUscita2();
		log.debug(methodName, "Quadratura per anno 2. E: " + Utility.formatCurrencyAsString(sommaStanziamentoEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoUscita));
		
		if(sommaStanziamentoEntrata.subtract(sommaStanziamentoUscita).compareTo(BigDecimal.ZERO) != 0) {
			log.info(methodName, "Quadratura NON corretta per anno 2. [stanziamento E: " + sommaStanziamentoEntrata + " U:" + sommaStanziamentoUscita + "]");
			return false;
		}
		log.info(methodName, "Quadratura corretta per i tre anni");
		return true;
	}
	
	/**
	 * Controlla se la differenza tra gli stanziamenti cassa di entrata e uscita &eacute; uguale a zero.
	 * 
	 * @return true se la quadratura &acute; corretta false altrimenti.
	 * 
	 */
	protected boolean isQuadraturaCorrettaStanziamentoCassa() {
		final String methodName = "isQuadraturaCorrettaStanziamentoCassa";
		BigDecimal sommaStanziamentoCassaEntrata = variazione.calcolaSommaStanziamentoCassaEntrata();
		BigDecimal sommaStanziamentoCassaUscita = variazione.calcolaSommaStanziamentoCassaUscita();	

		log.debug(methodName, "Quadratura cassa per anno 0. E: " + Utility.formatCurrencyAsString(sommaStanziamentoCassaEntrata) + " U: " + Utility.formatCurrencyAsString(sommaStanziamentoCassaUscita));
		
		if(sommaStanziamentoCassaEntrata.subtract(sommaStanziamentoCassaUscita).compareTo(BigDecimal.ZERO) == 0){
			log.info(methodName, "Quadratura corretta. [cassa E:"+sommaStanziamentoCassaEntrata + " U:"+sommaStanziamentoCassaUscita+ "]");
			return true;
		}
		
		log.info(methodName, "Quadratura NON corretta. [cassa E:"+sommaStanziamentoCassaEntrata + " U:"+sommaStanziamentoCassaUscita+ "]");
		return false;
	}
	
	
	//--- Controllo presenza Atto Amministrativo --------------------------------------------------------------------------------
	
	/**
	 * Il check di presenza dell'atto amministrativo va effettuato se tra i capitoli di spesa coinvolti nella variazione ci sono:
	 * <ol>
	 *     <li>capitoli con missione o programma differenti &eacute; obbligatorio un atto autorizzativo</li>
	 *     <li>capitoli con titolo o tipologie differenti &eacute; obbligatorio un atto autorizzativo.</li>
	 * </ol>
	 */
	protected void checkNecessarioAttoAmministrativoVariazioneDiBilancio() {
		if(isMissioneOProgrammaDifferenti() || isTitoloOTipologieDifferenti()){
			log.debug("checkNecessarioAttoAmministrativo", "Controllo l'atto amministrativo");
			checkParamAttoAmministrativo();
		}
		
	}

	/**
	 * Check param atto amministrativo.
	 */
	protected void checkParamAttoAmministrativo() {
		try{
			checkEntita(variazione.getAttoAmministrativo(), "atto amministrativo variazione");
		} catch(ServiceParamError spe){
			throw new BusinessException(spe.getErrore(),Esito.FALLIMENTO);
		}
	}	
	
	/**
	 * Checks if is missione o programma differenti.
	 *
	 * @return true, if is missione o programma differenti
	 */
	protected boolean isMissioneOProgrammaDifferenti() {
		return isCapitoliWithProgrammaDifferenti()
				|| isCapitoliWithMissioneDifferenti();
	}

	/**
	 * Checks if is capitoli with missione O programma differenti.
	 * @return <code>true <code>, se i capitoli hanno missione o progframma differenti, <code>false</code> altrimenti
	 */
	protected boolean isCapitoliWithProgrammaDifferenti() {
		final String methodName = "isCapitoliWithProgrammaDifferenti";
		Long programmaDifferenti = variazioniDad.countDifferentClassificatoriProgramma(variazione.getUid());
		log.debug(methodName, "Numero differenti programmi: " + programmaDifferenti);
		return programmaDifferenti.longValue() > 1L;
	}
	
	/**
	 * Checks if is capitoli with missione differenti.
	 * @return <code>true <code>, se i capitoli hanno missione differenti, <code>false</code> altrimenti
	 */
	protected boolean isCapitoliWithMissioneDifferenti() {
		final String methodName = "isCapitoliWithProgrammaDifferenti";
		Long missioneDifferenti = variazioniDad.countDifferentClassificatoriMissione(variazione.getUid());
		log.debug(methodName, "Numero differenti missioni: " + missioneDifferenti);
		return missioneDifferenti.longValue() > 1L;
	}
	
	/**
	 * Controlla se i capitoli passati in input abbiano titolo differenti.
	 * @return <code>true</code> se esiste almeno un capitolo con titolo macroaggregato differente dagli altri
	 */
	protected boolean isCapitoliWithTitoloSpesaDifferenti() {
		final String methodName = "isCapitoliWithTitoloSpesaDifferenti";
		Long titoloSpesaDifferenti = variazioniDad.countDifferentClassificatoriTitoloSpesa(variazione.getUid());
		log.debug(methodName, "Numero differenti titoli spesa: " + titoloSpesaDifferenti);
		return titoloSpesaDifferenti.longValue() > 1L;
	}
	

	/**
	 * Checks if is titolo o tipologie differenti.
	 *
	 * @return true, if is titolo o tipologie differenti
	 */
	protected boolean isTitoloOTipologieDifferenti() {
		return isCapitoliWithTitoloEntrataDifferenti()
				|| isCapitoliWithTipologieDifferenti();
	}

	/**
	 * Controlla se i capitoli passati in input abbiano titolo tipologie differenti.
	 *
	 * @return <code>true</code> se esiste almeno un capitolo con titolo tipologia differente dagli altri
	 */
	public boolean isCapitoliWithTipologieDifferenti() {
		final String methodName = "isCapitoliWithTipologieDifferenti";
		Long tipologiaTitoloDifferenti = variazioniDad.countDifferentClassificatoriTipologiaTitolo(variazione.getUid());
		log.debug(methodName, "Numero differenti tipologia titolo: " + tipologiaTitoloDifferenti);
		return tipologiaTitoloDifferenti.longValue() > 1L;
	}
	
	/**
	 * Controlla se i capitoli passati in input abbiano titolo tipologie differenti.
	 *
	 * @return <code>true</code> se esiste almeno un capitolo con titolo tipologia differente dagli altri
	 */
	public boolean isCapitoliWithTitoloEntrataDifferenti() {
		final String methodName = "isCapitoliWithTitoloEntrataDifferenti";
		Long titoloEntrataDifferenti = variazioniDad.countDifferentClassificatoriTitoloEntrata(variazione.getUid());
		log.debug(methodName, "Numero differenti titolo entrata: " + titoloEntrataDifferenti);
		return titoloEntrataDifferenti.longValue() > 1L;
	}
	
	protected boolean isProvvedimentoPresenteDefinitivo() {
		if(variazione.getAttoAmministrativo() == null || variazione.getAttoAmministrativo().getUid() == 0){
			return false;
		}
		// Provvedimento presente. Verifico lo stato DEFINITIVO
		StatoOperativoAtti statoOperativoAtti = provvedimentoDad.findStatoOperativoAttoAmministrativo(variazione.getAttoAmministrativo());
		return StatoOperativoAtti.DEFINITIVO.equals(statoOperativoAtti);
	}
	
	
	//--- Aggiornamento e caricamento importi del Capitolo -------------------------------------------------------------------------------
	

	



	/**
	 * Carica dentro variazione.getCapitoli().getImportiCapitolo() 
	 * gli importi del capitolo dello stesso anno di competenza della variazione reperendoli dal database.
	 */
	protected void caricaDettaglioImportiCapitoli() {
		for(DettaglioVariazioneImportoCapitolo dv : variazione.getListaDettaglioVariazioneImporto()) {
			caricaDettaglioImportiCapitolo(dv);
		}
	}

	@SuppressWarnings("unchecked")
	protected void caricaDettaglioImportiCapitolo(DettaglioVariazioneImportoCapitolo dv) {
		// SIAC-6883: carico i dettagli degli importi della variazione.
		ImportiCapitolo importiCapitolo = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno());
		ImportiCapitolo importiCapitolo1 = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno() + 1);
		ImportiCapitolo importiCapitolo2 = caricaDettaglioImportiCapitolo(dv.getCapitolo(), variazione.getBilancio().getAnno() + 2);
		
		dv.getCapitolo().setImportiCapitolo(importiCapitolo);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo1);
		dv.getCapitolo().getListaImportiCapitolo().add(importiCapitolo2);
	}	

	/**
	 * Carica dettaglio importi capitolo.
	 *
	 * @param capitolo the capitolo
	 * @param annoDiCompetenza the anno di competenza
	 * @return the importi capitolo
	 */
	protected ImportiCapitolo caricaDettaglioImportiCapitolo(@SuppressWarnings("rawtypes") Capitolo capitolo, int annoDiCompetenza) {
		final String methodName = "caricaDettaglioImportiCapitolo";
		
		ImportiCapitolo importiCapitolo =  importiCapitoloDad.findImportiCapitolo(capitolo, annoDiCompetenza, ImportiCapitolo.class, EnumSet.allOf(ImportiCapitoloEnum.class), true);
		
		log.debug(methodName, "annoDiCompetenza: "+ annoDiCompetenza + " [" + (importiCapitolo != null ? importiCapitolo.getAnnoCompetenza() : "null") + "]");
		checkBusinessCondition(importiCapitolo != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile trovare il dettaglio degli importi dell'anno " + variazione.getBilancio().getAnno()
				+ "per il capitolo avente uid: " + capitolo.getUid()));
		
		return importiCapitolo;
	}
	

	//----------------- Gestione processo
	
	/**
	 * Imposta una variabile di processo con nome e valori passati come parametro.
	 * Se la variabile esiste ne cambia il valore, altrimenti ne crea una nuova.
	 *
	 * @param azione the azione
	 * @param nome the nome
	 * @param valore the valore
	 */
	protected void setVariabileProcesso(AzioneRichiesta azione, String nome, Object valore) {
		
		VariabileProcesso variabile = new VariabileProcesso();
		
		for(VariabileProcesso vp : azione.getVariabiliProcesso() ) {
			if (nome.equals(vp.getNome())) {
				variabile = vp;
			}
		}
		
		variabile.setNome(nome);
		variabile.setValore(valore);
		
		azione.getVariabiliProcesso().add(variabile);
	}

	/**
	 * Gets the azione richiesta.
	 *
	 * @param uidAzioneRichiesta the uid azione richiesta
	 * @return the azione richiesta
	 */
	public AzioneRichiesta getAzioneRichiesta(int uidAzioneRichiesta) {
		
		GetAzioneRichiesta request = new GetAzioneRichiesta();
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azioneRichiesta.setUid(uidAzioneRichiesta);
		request.setAzioneRichiesta(azioneRichiesta);
		
		GetAzioneRichiestaResponse response = coreService.getAzioneRichiesta(request);
		return response.getAzioneRichiesta();
	}
	

	
	
	/**
	 * SIAC - 6884 checkVariazioniCapitoliFondino
	 */
	protected String controlloVariazioniCapitoloFondinoFailed(){
		final String methodName = "checkVariazioniCapitoliFondino";
		log.info(methodName, "Controllo variazioni capitoli fondino decentrato");
		try{
			checkVariazioneCapFondinoDec(variazione);
			return null;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
	
	
	//DOBBIAMO AVERE PER OGNI ANNO GLI IMPORTI SETTATI E QUELLI PRECEDENTE
		private void checkVariazioneCapFondinoDec(VariazioneImportoCapitolo v){
			//ANNO STANZIAMENTO 0
			int annoStanziamento0 = v.getBilancio().getAnno();
			for(DettaglioVariazioneImportoCapitolo dettVarImp : v.getListaDettaglioVariazioneImporto()) {
				if(dettVarImp.getCapitolo()!= null){
					//CHECK SE FONDINO
					boolean isFondino = capitoloDad.isCapitoloFondino(dettVarImp.getCapitolo().getUid());
					if(isFondino) {
							//PRENDIAMO LO STANZIAMENTO PER GLI ANNI DEL CAPITOLO
							BigDecimal stanziamentoCapitoloIniziale0 = null;
							BigDecimal stanziamentoCapitoloIniziale1 = null;
							BigDecimal stanziamentoCapitoloIniziale2 = null;
							if(dettVarImp.getCapitolo().getListaImportiCapitolo()!= null && !dettVarImp.getCapitolo().getListaImportiCapitolo().isEmpty()){
								for( int k=0; k<dettVarImp.getCapitolo().getListaImportiCapitolo().size();k++){
									ImportiCapitolo ic = (ImportiCapitolo) dettVarImp.getCapitolo().getListaImportiCapitolo().get(k);
									if(ic!= null){
										if(ic.getAnnoCompetenza() == annoStanziamento0){
											stanziamentoCapitoloIniziale0 = ic.getStanziamento();
										}else if(ic.getAnnoCompetenza() == annoStanziamento0+1){
											stanziamentoCapitoloIniziale1 = ic.getStanziamento();
										}else if(ic.getAnnoCompetenza() == annoStanziamento0+2){
											stanziamentoCapitoloIniziale2 = ic.getStanziamento();
										}
									}
								}
							}
							//CONFRONTO importo del capitolo con quello della variazione	
							//ANNO 0		
							if(stanziamentoCapitoloIniziale0!= null){
								BigDecimal stanziamentoPost0 = stanziamentoCapitoloIniziale0.add(dettVarImp.getStanziamento());
								BigDecimal stanziamentoPost0Abs = stanziamentoPost0.abs();
								BigDecimal stanziamentoCapitoloIniziale0Abs = stanziamentoCapitoloIniziale0.abs();
								int res = stanziamentoPost0Abs.compareTo(stanziamentoCapitoloIniziale0Abs);
								if(res==1){
									StringBuilder sb = new StringBuilder();
									int anno = annoStanziamento0;
									sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
									throw new RuntimeException(sb.toString());
								}
							}
							//ANNO1
							if(stanziamentoCapitoloIniziale1!= null){
								BigDecimal stanziamentoPost1 = stanziamentoCapitoloIniziale1.add(dettVarImp.getStanziamento1());
								BigDecimal stanziamentoPost1Abs = stanziamentoPost1.abs();
								BigDecimal stanziamentoCapitoloIniziale1Abs = stanziamentoCapitoloIniziale1.abs();
								int res = stanziamentoPost1Abs.compareTo(stanziamentoCapitoloIniziale1Abs);
								if(res==1){
									StringBuilder sb = new StringBuilder();
									int anno = annoStanziamento0+1;
									sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
									throw new RuntimeException(sb.toString());
								}
							}
							//ANNO2
							if(stanziamentoCapitoloIniziale2!= null){
								BigDecimal stanziamentoPost2 = stanziamentoCapitoloIniziale2.add(dettVarImp.getStanziamento2());
								BigDecimal stanziamentoPost2Abs = stanziamentoPost2.abs();
								BigDecimal stanziamentoCapitoloIniziale2Abs = stanziamentoCapitoloIniziale2.abs();
								int res = stanziamentoPost2Abs.compareTo(stanziamentoCapitoloIniziale2Abs);
								if(res==1){
									StringBuilder sb = new StringBuilder();
									int anno = annoStanziamento0+2;
									sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
									throw new RuntimeException(sb.toString());
								}
							}
							
							
								
								
							}//CONTROLLO FONDINO
						}
				}//FOR
				
			
		}
	
	
}
