/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistroIvaRiepilogoIva extends DatiIva{
	
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIva; // A2, B2, C2, D2
	
	private BigDecimal imponibile;            // E2
	private BigDecimal iva;                   // F2
	private BigDecimal totale;                // G2
	private BigDecimal progressivoImponibile; // H2
	private BigDecimal progressivoIva;        // I2
	private BigDecimal totaleProgressivo;     // L2
	/**
	 * @return the aliquotaSubdocumentoIva
	 */
	public AliquotaSubdocumentoIva getAliquotaSubdocumentoIva() {
		return aliquotaSubdocumentoIva;
	}
	/**
	 * @param aliquotaSubdocumentoIva the aliquotaSubdocumentoIva to set
	 */
	public void setAliquotaSubdocumentoIva(
			AliquotaSubdocumentoIva aliquotaSubdocumentoIva) {
		this.aliquotaSubdocumentoIva = aliquotaSubdocumentoIva;
	}
	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return imponibile;
	}
	/**
	 * @param imponibile the imponibile to set
	 */
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}
	/**
	 * @return the iva
	 */
	public BigDecimal getIva() {
		return iva;
	}
	/**
	 * @param iva the iva to set
	 */
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		return totale;
	}
	/**
	 * @param totale the totale to set
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	/**
	 * @return the progressivoImponibile
	 */
	public BigDecimal getProgressivoImponibile() {
		return progressivoImponibile;
	}
	/**
	 * @param progressivoImponibile the progressivoImponibile to set
	 */
	public void setProgressivoImponibile(BigDecimal progressivoImponibile) {
		this.progressivoImponibile = progressivoImponibile;
	}
	/**
	 * @return the progressivoIva
	 */
	public BigDecimal getProgressivoIva() {
		return progressivoIva;
	}
	/**
	 * @param progressivoIva the progressivoIva to set
	 */
	public void setProgressivoIva(BigDecimal progressivoIva) {
		this.progressivoIva = progressivoIva;
	}
	/**
	 * @return the totaleProgressivo
	 */
	public BigDecimal getTotaleProgressivo() {
		return totaleProgressivo;
	}
	/**
	 * @param totaleProgressivo the totaleProgressivo to set
	 */
	public void setTotaleProgressivo(BigDecimal totaleProgressivo) {
		this.totaleProgressivo = totaleProgressivo;
	}
	
	/**
	 * Ottiene la percentuale di indetraibilita per il singolo riepilogo.
	 * 
	 * @return la percentuale di indetraibilita
	 */
	public BigDecimal ottieniPercentualeIndetraibilita() {
		return aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeIndetraibilita();
	}
	
}
