/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccorser.model.ServiceRequest;

/**
 * Base per l'inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseCUDAccantonamentoFondiDubbiaEsigibilitaBaseService<A extends AccantonamentoFondiDubbiaEsigibilitaBase<?>, REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	private static final int MAX_SCALE_ALLOWED = 2;
	public static final MathContext MATH_CONTEXT_THREE_HALF_DOWN = new MathContext(3, RoundingMode.HALF_DOWN);
	public static final MathContext MATH_CONTEXT_FIVE_HALF_DOWN = new MathContext(5, RoundingMode.HALF_DOWN);
	public static final Map<Integer, BigDecimal> MOLTIPLICATORI_MEDIE_PONDERATE = new HashMap<Integer, BigDecimal>();
	
	static {
		// Caricamento moltiplicatori
		MOLTIPLICATORI_MEDIE_PONDERATE.put(4, new BigDecimal("0.1"));
		MOLTIPLICATORI_MEDIE_PONDERATE.put(3, new BigDecimal("0.1"));
		MOLTIPLICATORI_MEDIE_PONDERATE.put(2, new BigDecimal("0.1"));
		MOLTIPLICATORI_MEDIE_PONDERATE.put(1, new BigDecimal("0.35"));
		MOLTIPLICATORI_MEDIE_PONDERATE.put(0, new BigDecimal("0.35"));
	}
	
	//DADs
	@Autowired protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;

	// Fields
	protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;

	/**
	 * Popolamento dei dati dell'accantonamento
	 * @param afde
	 */
	protected void popolaDatiAccantonamento(A afde) {
		// I dati sono originali, pertanto non modificati dall'utente
		afde.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		
		int quinquennio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue();
		boolean riscossioneVirtuosa = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getRiscossioneVirtuosa();

		Map<Integer, BigDecimal> numeratori = calcolaNumeratori(afde);
		Map<Integer, BigDecimal> denominatori = calcolaDenominatori(afde);
		
		afde.setNumeratore(numeratori.get(quinquennio));
		afde.setNumeratore1(numeratori.get(quinquennio - 1));
		afde.setNumeratore2(numeratori.get(quinquennio - 2));
		afde.setNumeratore3(numeratori.get(quinquennio - 3));
		afde.setNumeratore4(numeratori.get(quinquennio - 4));
		
		afde.setDenominatore(denominatori.get(quinquennio));
		afde.setDenominatore1(denominatori.get(quinquennio - 1));
		afde.setDenominatore2(denominatori.get(quinquennio - 2));
		afde.setDenominatore3(denominatori.get(quinquennio - 3));
		afde.setDenominatore4(denominatori.get(quinquennio - 4));
		
		afde.setPercentualeAccantonamentoFondi(calcolaPercentuale(afde.getNumeratore(), afde.getDenominatore()));
		afde.setPercentualeAccantonamentoFondi1(calcolaPercentuale(afde.getNumeratore1(), afde.getDenominatore1()));
		afde.setPercentualeAccantonamentoFondi2(calcolaPercentuale(afde.getNumeratore2(), afde.getDenominatore2()));
		afde.setPercentualeAccantonamentoFondi3(calcolaPercentuale(afde.getNumeratore3(), afde.getDenominatore3()));
		afde.setPercentualeAccantonamentoFondi4(calcolaPercentuale(afde.getNumeratore4(), afde.getDenominatore4()));
		
		afde.setMediaSempliceTotali(calcolaMediaSempliceTotali(afde, riscossioneVirtuosa));
		afde.setMediaSempliceRapporti(calcolaMediaSempliceRapporti(afde, riscossioneVirtuosa));
		afde.setMediaPonderataTotali(calcolaMediaPonderataTotali(afde, riscossioneVirtuosa));
		afde.setMediaPonderataRapporti(calcolaMediaPonderataRapporti(afde, riscossioneVirtuosa));
		// SIAC-8377
		afde.setMediaUtente(null);
		// Per previsione e rendiconto sarebbe meglio la media semplice dei totali, in gestione esiste solo una media calcolabile dal sistema quindi il valore di default direi che puo' essere proprio quello
		// SIAC-8377 tipo media prescelta da UNTENTE => SEMPLICE_TOTALI
		afde.setTipoMediaPrescelta(TipoMediaAccantonamentoFondiDubbiaEsigibilita.SEMPLICE_TOTALI);
		
		// Popolamento dati originali
		afde.setNumeratoreOriginale(afde.getNumeratore());
		afde.setNumeratore1Originale(afde.getNumeratore1());
		afde.setNumeratore2Originale(afde.getNumeratore2());
		afde.setNumeratore3Originale(afde.getNumeratore3());
		afde.setNumeratore4Originale(afde.getNumeratore4());
		afde.setDenominatoreOriginale(afde.getDenominatore());
		afde.setDenominatore1Originale(afde.getDenominatore1());
		afde.setDenominatore2Originale(afde.getDenominatore2());
		afde.setDenominatore3Originale(afde.getDenominatore3());
		afde.setDenominatore4Originale(afde.getDenominatore4());
		
		popolaDatiUlteriori(afde);

		// Note calcolate al fondo per sfruttare tutti i dati necessari
		afde.setNote(calcolaNote(afde));
	}
	
	protected abstract Map<Integer, BigDecimal> calcolaNumeratori(A afde);
	protected abstract Map<Integer, BigDecimal> calcolaDenominatori(A afde);
	protected abstract String calcolaNote(A afde);

	/**
	 * Popolamento dei dati ulteriori
	 * @param afde l'accantonamento da popolare
	 */
	protected abstract void popolaDatiUlteriori(A afde);
	
	/**
	 * Calcolo della percentuale
	 * @param numeratore il numeratore
	 * @param denominatore il denominatore
	 * @return la percentuale
	 */
	protected BigDecimal calcolaPercentuale(BigDecimal numeratore, BigDecimal denominatore) {
		// Se i dati non esistono, imposto null (ignorato nel calcolo)
		if(numeratore == null || denominatore == null) {
			return null;
		}
		// Previene divisione per zero
		if(BigDecimal.ZERO.compareTo(denominatore) == 0) {
			return BigDecimal.ZERO;
		}
		// Se denominatore < numeratore, allora limito a 100%
		if(denominatore.compareTo(numeratore) < 0) {
			return BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
		//SIAC-8512 
		return numeratore.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).divide(denominatore, MATH_CONTEXT_FIVE_HALF_DOWN)
				.setScale(MAX_SCALE_ALLOWED, MATH_CONTEXT_FIVE_HALF_DOWN.getRoundingMode()); 
	}

	/**
	 * Calcolo della media semplice dei totali.
	 * <br/>
	 * <code>Media = SUM(numeratori) / SUM(denominatori)</code>
	 * @param afde l'accantonamento
	 * @return la media semplice, se almeno un valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaSempliceTotali(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde) {
		final String methodName = "calcolaMediaSempliceTotali";
		int divisore = 0;
		int numeroImporti = 5;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		BigDecimal denominatore = BigDecimal.ZERO;
		for(int i = 0; i < numeroImporti; i++) {
			BigDecimal incasso = afde.recuperaNumeratore(i);
			BigDecimal accertamento = afde.recuperaDenominatore(i);
			if(incasso != null && accertamento != null) {
				divisore++;
				//SIAC-8488
				numeratore = numeratore.add(incasso);
				denominatore = denominatore.add(accertamento);
			}
		}
		log.trace(methodName, "Numeratore: " + numeratore.toPlainString() + " - divisore: " + divisore);
		return divisore == 0 ? null : calcolaPercentuale(numeratore, denominatore);
	}

	/**
	 * Calcolo della media semplice dei totali con la riscossione virtuosa.
	 * <br/>
	 * <code>Media = SUM(numeratori) / SUM(denominatori)</code>
	 * @param afde l'accantonamento
	 * @param riscossioneVirtuosa la riscossione virtuosa
	 * @return la media semplice, se almeno un valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaSempliceTotali(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde, boolean riscossioneVirtuosa) {
		final String methodName = "calcolaMediaSempliceTotali";
		int divisore = 0;
		int numeroImporti = riscossioneVirtuosa ? 3 : 5;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		BigDecimal denominatore = BigDecimal.ZERO;
		for(int i = 0; i < numeroImporti; i++) {
			BigDecimal incasso = afde.recuperaNumeratore(i);
			BigDecimal accertamento = afde.recuperaDenominatore(i);
			if(incasso != null && accertamento != null) {
				divisore++;
				//SIAC-8488
				numeratore = numeratore.add(incasso);
				denominatore = denominatore.add(accertamento);
			}
		}
		log.trace(methodName, "Numeratore: " + numeratore.toPlainString() + " - divisore: " + divisore);
		return divisore == 0 ? null : calcolaPercentuale(numeratore, denominatore);
	}
	
	/**
	 * Calcolo della media semplice dei rapporti.
	 * <br/>
	 * <code>Media = SUM(rapporti) / COUNT(rapporti not NULL)</code>
	 * @param afde l'accantonamento
	 * @return la media semplice, se almeno un valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaSempliceRapporti(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde) {
		final String methodName = "calcolaMediaSempliceRapporti";
		int divisore = 0;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		for(int i = 0; i < 5; i++) {
			BigDecimal percentuale = afde.recuperaPercentualeAccantonamentoFondi(i);
			if(percentuale != null) {
				divisore++;
				//SIAC-8488
				numeratore = numeratore.add(percentuale);
			}
		}
		log.trace(methodName, "Numeratore: " + numeratore.toPlainString() + " - divisore: " + divisore);
		return divisore == 0 ? null : numeratore.divide(BigDecimal.valueOf(divisore), MATH_CONTEXT_FIVE_HALF_DOWN)
				.setScale(MAX_SCALE_ALLOWED, MATH_CONTEXT_FIVE_HALF_DOWN.getRoundingMode());
	}

	/**
	 * Calcolo della media semplice dei rapporti.
	 * <br/>
	 * <code>Media = SUM(rapporti) / COUNT(rapporti not NULL)</code>
	 * @param afde l'accantonamento
	 * @param riscossioneVirtuosa la riscossione virtuosa
	 * @return la media semplice, se almeno un valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaSempliceRapporti(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde, boolean riscossioneVirtuosa) {
		final String methodName = "calcolaMediaSempliceRapporti";
		int divisore = 0;
		int numeroImporti = riscossioneVirtuosa ? 3 : 5;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		for(int i = 0; i < numeroImporti; i++) {
			BigDecimal percentuale = afde.recuperaPercentualeAccantonamentoFondi(i);
			if(percentuale != null) {
				divisore++;
				//SIAC-8488
				numeratore = numeratore.add(percentuale);
			}
		}
		log.trace(methodName, "Numeratore: " + numeratore.toPlainString() + " - divisore: " + divisore);
		return divisore == 0 ? null : numeratore.divide(BigDecimal.valueOf(divisore), MATH_CONTEXT_FIVE_HALF_DOWN)
				.setScale(MAX_SCALE_ALLOWED, MATH_CONTEXT_FIVE_HALF_DOWN.getRoundingMode());
	}
	
	/**
	 * Calcolo della media ponderata dei totali.
	 * <br/>
	 * <code>Media = SUM(numeratore * coefficiente) / SUM(denominatore * coefficiente)</code>
	 * @param afde l'accantonamento
	 * @return la media ponderata, se ogni valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaPonderataTotali(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde) {
		if(Boolean.TRUE.equals(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getRiscossioneVirtuosa())) {
			log.debug("calcolaMediaPonderataTotali", "Riscossione virtuosa. Media ponderata non disponibile");
			return null;
		}
		int divisore = 0;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		BigDecimal denominatore = BigDecimal.ZERO;
		for(int i = 0; i < 5; i++) {
			BigDecimal numeratoreAFDE = afde.recuperaNumeratore(i);
			BigDecimal denominatoreAFDE = afde.recuperaDenominatore(i);
			if(numeratoreAFDE != null && denominatoreAFDE != null) {
				divisore++;
				BigDecimal moltiplicatore = MOLTIPLICATORI_MEDIE_PONDERATE.get(i);
				//SIAC-8488
				numeratore = numeratore.add(numeratoreAFDE.multiply(moltiplicatore));
				denominatore = denominatore.add(denominatoreAFDE.multiply(moltiplicatore));
			}
		}
		return divisore != 5 ? null : calcolaPercentuale(numeratore, denominatore);
	}

	/**
	 * Calcolo della media ponderata dei totali.
	 * <br/>
	 * <code>Media = SUM(numeratore * coefficiente) / SUM(denominatore * coefficiente)</code>
	 * @param afde l'accantonamento
	 * @param riscossioneVirtuosa la riscossione virtuosa
	 * @return la media ponderata, se ogni valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaPonderataTotali(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde, boolean riscossioneVirtuosa) {
		if(riscossioneVirtuosa) {
			log.debug("calcolaMediaPonderataTotali", "Riscossione virtuosa. Media ponderata non disponibile");
			return null;
		}
		int divisore = 0;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		BigDecimal denominatore = BigDecimal.ZERO;
		for(int i = 0; i < 5; i++) {
			BigDecimal numeratoreAFDE = afde.recuperaNumeratore(i);
			BigDecimal denominatoreAFDE = afde.recuperaDenominatore(i);
			if(numeratoreAFDE != null && denominatoreAFDE != null) {
				divisore++;
				BigDecimal moltiplicatore = MOLTIPLICATORI_MEDIE_PONDERATE.get(i);
				//SIAC-8488
				numeratore = numeratore.add(numeratoreAFDE.multiply(moltiplicatore));
				denominatore = denominatore.add(denominatoreAFDE.multiply(moltiplicatore));
			}
		}
		return divisore != 5 ? null : calcolaPercentuale(numeratore, denominatore);
	}
	
	/**
	 * Calcolo della media ponderata dei rapporti.
	 * <br/>
	 * <code>Media = SUM(rapporto * coefficiente)</code>
	 * @param afde l'accantonamento
	 * @return la media ponderata, se ogni valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaPonderataRapporti(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde) {
		if(Boolean.TRUE.equals(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getRiscossioneVirtuosa())) {
			log.debug("calcolaMediaPonderataRapporti", "Riscossione virtuosa. Media ponderata non disponibile");
			return null;
		}
		int divisore = 0;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		for(int i = 0; i < 5; i++) {
			BigDecimal percentuale = afde.recuperaPercentualeAccantonamentoFondi(i);
			if(percentuale != null) {
				divisore++;
				BigDecimal moltiplicatore = MOLTIPLICATORI_MEDIE_PONDERATE.get(i);
				//SIAC-8488
				numeratore = numeratore.add(percentuale.multiply(moltiplicatore));
			}
		}
		//SIAC-8488 eseguo l'arrotondamento dopo la somma
		return divisore != 5 ? null : applicaArrotondamento(numeratore);
	}

	/**
	 * Calcolo della media ponderata dei rapporti.
	 * <br/>
	 * <code>Media = SUM(rapporto * coefficiente)</code>
	 * @param afde l'accantonamento
	 * @param riscossioneVirtuosa la riscossione virtuosa
	 * @return la media ponderata, se ogni valore non &eacute; <code>null</code>, <code>null</code> altrimenti
	 */
	protected BigDecimal calcolaMediaPonderataRapporti(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde, boolean riscossioneVirtuosa) {
		if(riscossioneVirtuosa) {
			log.debug("calcolaMediaPonderataRapporti", "Riscossione virtuosa. Media ponderata non disponibile");
			return null;
		}
		int divisore = 0;
		
		BigDecimal numeratore = BigDecimal.ZERO;
		for(int i = 0; i < 5; i++) {
			BigDecimal percentuale = afde.recuperaPercentualeAccantonamentoFondi(i);
			if(percentuale != null) {
				divisore++;
				BigDecimal moltiplicatore = MOLTIPLICATORI_MEDIE_PONDERATE.get(i);
				//SIAC-8488
				numeratore = numeratore.add(percentuale.multiply(moltiplicatore));
			}
		}
		//SIAC-8488 eseguo l'arrotondamento dopo la somma
		return divisore != 5 ? null : applicaArrotondamento(numeratore);
	}
	
	//SIAC-8550
	private BigDecimal applicaArrotondamento(BigDecimal numeratore) {
		return numeratore.round(MATH_CONTEXT_FIVE_HALF_DOWN).setScale(MAX_SCALE_ALLOWED, MATH_CONTEXT_FIVE_HALF_DOWN.getRoundingMode());
	}

}
