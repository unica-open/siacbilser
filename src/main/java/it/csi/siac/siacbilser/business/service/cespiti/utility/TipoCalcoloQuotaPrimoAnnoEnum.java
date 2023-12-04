/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.utility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public enum TipoCalcoloQuotaPrimoAnnoEnum {

	DODICESIMI("12", null, null, Integer.valueOf(GregorianCalendar.MONTH), 1),
	TRECENTOSESSANTACINQUESIMI("365", null, null, Integer.valueOf(GregorianCalendar.DAY_OF_YEAR)),
	METAQUOTA("50",BigDecimal.ONE , new BigDecimal("2"), null),
	QUOTA_INTERA("100",BigDecimal.ONE, BigDecimal.ONE, null)
	
	;
	
	private String codice;
	private BigDecimal dividend;
    private BigDecimal divisor;
	private Integer periodIdentifyer;
	private int augend;
	
	private TipoCalcoloQuotaPrimoAnnoEnum(String codice, BigDecimal dividend, BigDecimal divisor, Integer periodIdentifyer, int augen) {
		this.codice = codice;
		this.periodIdentifyer = periodIdentifyer;
		this.dividend = dividend;
		this.divisor = divisor;
		this.augend = augen;
	}
	
	private TipoCalcoloQuotaPrimoAnnoEnum(String codice, BigDecimal dividend, BigDecimal divisor, Integer periodIdentifyer) {
		this.codice = codice;
		this.periodIdentifyer = periodIdentifyer;
		this.dividend = dividend;
		this.divisor = divisor;
		this.augend = 0;
	}
	
	/**
	 * By codice tipo calcolo.
	 *
	 * @param codice the codice
	 * @return the tipo calcolo quota primo anno enum
	 */
	public static TipoCalcoloQuotaPrimoAnnoEnum byCodiceTipoCalcolo(String codice) {
		if(StringUtils.isBlank(codice)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile ottenere un enum per il calcolo prima nota: codice tipo calcolo non presente"));
		}
		for (TipoCalcoloQuotaPrimoAnnoEnum info : TipoCalcoloQuotaPrimoAnnoEnum.values()) {
			if (info.codice.equalsIgnoreCase(codice)) {
				return info;
			}
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("il codice non ha un mapping corrispondente nel'enum del tipo calcolo"));
	}
	
	
	/**
	 * A seconda del tipo calcolo del cespite, l'ammortamento dovr&agrave; essere calcolato moltiplicando il valore attuale per un numero (multiplier).
	 * Tale parametro viene calcolato in questo modo:
	 * <ul>
	 * 		<li> <b> CALCOLO IN 365-esimi: </b> (Numero giorni in azienda nell'anno di ingresso in inventario) / (Numero giorni nell'anno) </li>
	 *  	<li> <b> CALCOLO IN 12esimi: </b> (Numero mesi in azienda nell'anno di ingresso in inventario) / (Numero mesi nell'anno)</li>
	 *  	<li> <b> CALCOLO IN 50&percnt; :</b>   &frac12;</li>
	 *  	<li> <b> CALCOLO QUOTA INTERA </b>  1</li>
	 * </ul>
	 * 
	 * Il 
	 *
	 * @paramm enumTipoCalcolo the enum tipo calcolo
	 * @param dataIngressoInventario the data ingresso inventario
	 * @return the big decimal
	 */
	public BigDecimal getFattoreProporzionamentoDaDataAFineAnno(Date dataIngressoInventario) {
		if(this.dividend != null && this.divisor != null && !BigDecimal.ZERO.equals(this.divisor)) {
			return this.dividend.divide(this.divisor,MathContext.DECIMAL128);
		}
		//seconda strada, il fattore moltiplicativo in base al periodo passato in inventario
		if(this.periodIdentifyer != null) {
			GregorianCalendar cg = new GregorianCalendar();
			cg.setTime(dataIngressoInventario);
//			System.out.println("******");
//			System.out.println("GregorianCalendar.DAY_OF_MONTH: " + cg.get(GregorianCalendar.DAY_OF_MONTH));
//			System.out.println("GregorianCalendar.MONTH: " + cg.get(GregorianCalendar.MONTH));
//			System.out.println("GregorianCalendar.YEAR: " + cg.get(GregorianCalendar.YEAR));
			
			int anno = cg.getActualMaximum(this.periodIdentifyer.intValue()) + this.augend;
//			System.out.println("actual Maximum " + anno );
			int periodoPassatoInInventario = anno- cg.get(this.periodIdentifyer.intValue());
			System.out.println("day of the year / month of the year: " +  cg.get(this.periodIdentifyer.intValue()) );
			System.out.println("******");
			return new BigDecimal(periodoPassatoInInventario).divide(new BigDecimal(anno),MathContext.DECIMAL128);
			
		}
		
		//nessuna delle due strade e' percorribile
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile definire un fattore moltiplicativo per la prima quota."));
	}
	
	/**
	 * A seconda del tipo calcolo del cespite, l'ammortamento dovr&agrave; essere calcolato moltiplicando il valore attuale per un numero (multiplier).
	 * Tale parametro viene calcolato in questo modo:
	 * <ul>
	 * 		<li> <b> CALCOLO IN 365-esimi: </b> (Numero giorni in azienda nell'anno di ingresso in inventario) / (Numero giorni nell'anno) </li>
	 *  	<li> <b> CALCOLO IN 12esimi: </b> (Numero mesi in azienda nell'anno di ingresso in inventario) / (Numero mesi nell'anno)</li>
	 *  	<li> <b> CALCOLO IN 50&percnt; :</b>   &frac12;</li>
	 *  	<li> <b> CALCOLO QUOTA INTERA </b>  1</li>
	 * </ul>
	 * 
	 * Il 
	 *
	 * @paramm enumTipoCalcolo the enum tipo calcolo
	 * @param dataIngressoInventario the data ingresso inventario
	 * @return the big decimal
	 */
	public BigDecimal getFattoreProporzionamentoDaInizioAnnoAData(Date dataIngressoInventario) {
		if(this.dividend != null && this.divisor != null && !BigDecimal.ZERO.equals(this.divisor)) {
			return this.dividend.divide(this.divisor,MathContext.DECIMAL128);
		}
		//seconda strada, il fattore moltiplicativo in base al periodo passato in inventario
		if(this.periodIdentifyer != null) {
			GregorianCalendar cg = new GregorianCalendar();
			cg.setTime(dataIngressoInventario);			
			int anno = cg.getActualMaximum(this.periodIdentifyer.intValue()) + this.augend;
			int periodoPassatoInInventario = cg.get(this.periodIdentifyer.intValue()) + this.augend;
			
			return new BigDecimal(periodoPassatoInInventario).divide(new BigDecimal(anno),MathContext.DECIMAL128);
		}
		
		//nessuna delle due strade e' percorribile
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile definire un fattore moltiplicativo per la prima quota."));
	}
	
	/**
	 * A seconda del tipo calcolo del cespite, l'ammortamento dovr&agrave; essere calcolato moltiplicando il valore attuale per un numero (multiplier).
	 * Tale parametro viene calcolato in questo modo:
	 * <ul>
	 * 		<li> <b> CALCOLO IN 365-esimi: </b> (Numero giorni in azienda nell'anno di ingresso in inventario) / (Numero giorni nell'anno) </li>
	 *  	<li> <b> CALCOLO IN 12esimi: </b> (Numero mesi in azienda nell'anno di ingresso in inventario) / (Numero mesi nell'anno)</li>
	 *  	<li> <b> CALCOLO IN 50&percnt; :</b>   &frac12;</li>
	 *  	<li> <b> CALCOLO QUOTA INTERA </b>  1</li>
	 * </ul>
	 * 
	 * Il 
	 *
	 * @param dataInizio the data inizio
	 * @param dataFine the data ingresso inventario
	 * @return the big decimal
	 * @paramm enumTipoCalcolo the enum tipo calcolo
	 */
	public BigDecimal getFattoreProporzionamentoDaDataAData(Date dataInizio, Date dataFine) {
		if(this.dividend != null && this.divisor != null && !BigDecimal.ZERO.equals(this.divisor)) {
			return this.dividend.divide(this.divisor,MathContext.DECIMAL128);
		}
		//seconda strada, il fattore moltiplicativo in base al periodo passato in inventario
		if(this.periodIdentifyer != null) {
			GregorianCalendar cg = new GregorianCalendar();
			cg.setTime(dataFine);
			GregorianCalendar cgInizio = new GregorianCalendar();
			cg.setTime(dataInizio);
			//this.alias e' l'offste dovuto al fatto che per gregorian calendar i count dei mesi parte da zero
			int periodoTotale = cg.getActualMaximum(this.periodIdentifyer.intValue()) + this.augend;
			System.out.println("periodoTotale : " + periodoTotale);
			int periodoPassatoInInventario = cgInizio.get(this.periodIdentifyer.intValue()) - cg.get(this.periodIdentifyer.intValue()) - this.augend;
			System.out.println("cgInizio.get(this.periodIdentifyer.intValue())" + cgInizio.get(this.periodIdentifyer.intValue()));
			System.out.println("cg.get(this.periodIdentifyer.intValue())" + cg.get(this.periodIdentifyer.intValue()));
			System.out.println("periodoPassatoInInventario" + periodoPassatoInInventario);
			return new BigDecimal(periodoPassatoInInventario).divide(new BigDecimal(periodoTotale),MathContext.DECIMAL128);
		}
		
		//nessuna delle due strade e' percorribile
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile definire un fattore moltiplicativo per la prima quota."));
	}	

}
