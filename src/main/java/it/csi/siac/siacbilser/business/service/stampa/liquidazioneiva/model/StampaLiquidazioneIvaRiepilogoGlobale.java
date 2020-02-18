/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaLiquidazioneIvaRiepilogoGlobale {
	
	private BigDecimal ivaADebito;                   // A
	private BigDecimal ivaACredito;                  // B
	private BigDecimal percentualeProRata;
	// SIAC-4301
	private BigDecimal ivaIndetraibileCausaProRata;        // C1
	private BigDecimal ivaDebitoIndetraibileCausaProRata;  // C2
	private BigDecimal totaleIvaAmmessaInDetrazione; // D1
	private BigDecimal totaleIvaDebitoAmmessaInDetrazione; // D2
	
	private BigDecimal ivaADebitoMenoIvaACredito;    // E
	private BigDecimal ivaACreditoPeriodoPrecedente; // F
	private BigDecimal ivaDaVersareIvaACredito;      // G
	
	/**
	 * @return the ivaADebito
	 */
	public BigDecimal getIvaADebito() {
		return ivaADebito;
	}
	/**
	 * @param ivaADebito the ivaADebito to set
	 */
	public void setIvaADebito(BigDecimal ivaADebito) {
		this.ivaADebito = ivaADebito;
	}
	/**
	 * @return the ivaACredito
	 */
	public BigDecimal getIvaACredito() {
		return ivaACredito;
	}
	/**
	 * @param ivaACredito the ivaACredito to set
	 */
	public void setIvaACredito(BigDecimal ivaACredito) {
		this.ivaACredito = ivaACredito;
	}
	/**
	 * @return the percentualeProRata
	 */
	public BigDecimal getPercentualeProRata() {
		return percentualeProRata;
	}
	/**
	 * @param percentualeProRata the percentualeProRata to set
	 */
	public void setPercentualeProRata(BigDecimal percentualeProRata) {
		this.percentualeProRata = percentualeProRata;
	}
	/**
	 * @return the ivaIndetraibileCausaProRata
	 */
	public BigDecimal getIvaIndetraibileCausaProRata() {
		return ivaIndetraibileCausaProRata;
	}
	/**
	 * @param ivaIndetraibileCausaProRata the ivaIndetraibileCausaProRata to set
	 */
	public void setIvaIndetraibileCausaProRata(
			BigDecimal ivaIndetraibileCausaProRata) {
		this.ivaIndetraibileCausaProRata = ivaIndetraibileCausaProRata;
	}
	/**
	 * @return the ivaDebitoIndetraibileCausaProRata
	 */
	public BigDecimal getIvaDebitoIndetraibileCausaProRata() {
		return ivaDebitoIndetraibileCausaProRata;
	}
	/**
	 * @param ivaDebitoIndetraibileCausaProRata the ivaDebitoIndetraibileCausaProRata to set
	 */
	public void setIvaDebitoIndetraibileCausaProRata(BigDecimal ivaDebitoIndetraibileCausaProRata) {
		this.ivaDebitoIndetraibileCausaProRata = ivaDebitoIndetraibileCausaProRata;
	}
	/**
	 * @return the totaleIvaAmmessaInDetrazione
	 */
	public BigDecimal getTotaleIvaAmmessaInDetrazione() {
		return totaleIvaAmmessaInDetrazione;
	}
	/**
	 * @param totaleIvaAmmessaInDetrazione the totaleIvaAmmessaInDetrazione to set
	 */
	public void setTotaleIvaAmmessaInDetrazione(
			BigDecimal totaleIvaAmmessaInDetrazione) {
		this.totaleIvaAmmessaInDetrazione = totaleIvaAmmessaInDetrazione;
	}
	/**
	 * @return the totaleIvaDebitoAmmessaInDetrazione
	 */
	public BigDecimal getTotaleIvaDebitoAmmessaInDetrazione() {
		return totaleIvaDebitoAmmessaInDetrazione;
	}
	/**
	 * @param totaleIvaDebitoAmmessaInDetrazione the totaleIvaDebitoAmmessaInDetrazione to set
	 */
	public void setTotaleIvaDebitoAmmessaInDetrazione(BigDecimal totaleIvaDebitoAmmessaInDetrazione) {
		this.totaleIvaDebitoAmmessaInDetrazione = totaleIvaDebitoAmmessaInDetrazione;
	}
	/**
	 * @return the ivaADebitoMenoIvaACredito
	 */
	public BigDecimal getIvaADebitoMenoIvaACredito() {
		return ivaADebitoMenoIvaACredito;
	}
	/**
	 * @param ivaADebitoMenoIvaACredito the ivaADebitoMenoIvaACredito to set
	 */
	public void setIvaADebitoMenoIvaACredito(BigDecimal ivaADebitoMenoIvaACredito) {
		this.ivaADebitoMenoIvaACredito = ivaADebitoMenoIvaACredito;
	}
	/**
	 * @return the ivaACreditoPeriodoPrecedente
	 */
	public BigDecimal getIvaACreditoPeriodoPrecedente() {
		return ivaACreditoPeriodoPrecedente;
	}
	/**
	 * @param ivaACreditoPeriodoPrecedente the ivaACreditoPeriodoPrecedente to set
	 */
	public void setIvaACreditoPeriodoPrecedente(
			BigDecimal ivaACreditoPeriodoPrecedente) {
		this.ivaACreditoPeriodoPrecedente = ivaACreditoPeriodoPrecedente;
	}
	/**
	 * @return the ivaDaVersareIvaACredito
	 */
	public BigDecimal getIvaDaVersareIvaACredito() {
		return ivaDaVersareIvaACredito;
	}
	/**
	 * @param ivaDaVersareIvaACredito the ivaDaVersareIvaACredito to set
	 */
	public void setIvaDaVersareIvaACredito(BigDecimal ivaDaVersareIvaACredito) {
		this.ivaDaVersareIvaACredito = ivaDaVersareIvaACredito;
	}
	
	
}
