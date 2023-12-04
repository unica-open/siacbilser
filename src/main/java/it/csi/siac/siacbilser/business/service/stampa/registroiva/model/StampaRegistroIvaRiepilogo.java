/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.Periodo;

/**
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/ago/2014
 *
 */
@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistroIvaRiepilogo {
	
	@XmlElements({
		@XmlElement(name="riepilogoIva", type=StampaRegistroIvaRiepilogoIva.class)
	})
	@XmlElementWrapper(name = "riepiloghiIva")
	private List<DatiIva> listaRiepiloghiIva = new ArrayList<DatiIva>();
	
	private Periodo periodo;
	
	private BigDecimal totaleImponibile;
	private BigDecimal totaleIVA;
	private BigDecimal totaleTotale;
	private BigDecimal totaleProgressivoImponibile;
	private BigDecimal totaleProgressivoIva;
	private BigDecimal totaleTotaleProgressivo;
	
	//totali parziali--ci sono solo per acquisti
	private BigDecimal totaleImponibileIndetraibile;
	private BigDecimal totaleImponibileDetraibile;
	private BigDecimal totaleImponibileEsente;
	private BigDecimal totaleIVAIndetraibile;
	private BigDecimal totaleIVADetraibile;
	private BigDecimal totaleProgressivoImponibileIndetraibile;
	private BigDecimal totaleProgressivoImponibileDetraibile;
	private BigDecimal totaleProgressivoImponibileEsente;
	private BigDecimal totaleProgressivoIvaIndetraibile;
	private BigDecimal totaleProgressivoIvaDetraibile;
	private BigDecimal percProrata;
	private BigDecimal ivaIndetraibileCausaProRata;

	/**
	 * @return the listaRiepiloghiIva
	 */
	@XmlTransient
	public List<DatiIva> getListaRiepiloghiIva() {
		return listaRiepiloghiIva;
	}

	/**
	 * @param listaRiepiloghiIva the listaRiepiloghiIva to set
	 */
	public void setListaRiepiloghiIva(List<DatiIva> listaRighe) {
		this.listaRiepiloghiIva = listaRighe;
	}
	
	/**
	 * @return the totaleImponibile
	 */
	public BigDecimal getTotaleImponibile() {
		return totaleImponibile;
	}

	/**
	 * @param totaleImponibile the totaleImponibile to set
	 */
	public void setTotaleImponibile(BigDecimal totaleImponibile) {
		this.totaleImponibile = totaleImponibile;
	}

	/**
	 * @return the totaleIVA
	 */
	public BigDecimal getTotaleIVA() {
		return totaleIVA;
	}

	/**
	 * @param totaleIVA the totaleIVA to set
	 */
	public void setTotaleIVA(BigDecimal totaleIVA) {
		this.totaleIVA = totaleIVA;
	}

	/**
	 * @return the totaleTotale
	 */
	public BigDecimal getTotaleTotale() {
		return totaleTotale;
	}

	/**
	 * @param totaleTotale the totaleTotale to set
	 */
	public void setTotaleTotale(BigDecimal totaleTotale) {
		this.totaleTotale = totaleTotale;
	}

	/**
	 * @return the totaleProgressivoImponibile
	 */
	public BigDecimal getTotaleProgressivoImponibile() {
		return totaleProgressivoImponibile;
	}

	/**
	 * @param totaleProgressivoImponibile the totaleProgressivoImponibile to set
	 */
	public void setTotaleProgressivoImponibile(
			BigDecimal totaleProgressivoImponibile) {
		this.totaleProgressivoImponibile = totaleProgressivoImponibile;
	}

	/**
	 * @return the totaleProgressivoIva
	 */
	public BigDecimal getTotaleProgressivoIva() {
		return totaleProgressivoIva;
	}

	/**
	 * @param totaleProgressivoIva the totaleProgressivoIva to set
	 */
	public void setTotaleProgressivoIva(BigDecimal totaleProgressivoIva) {
		this.totaleProgressivoIva = totaleProgressivoIva;
	}

	/**
	 * @return the totaleTotaleProgressivo
	 */
	public BigDecimal getTotaleTotaleProgressivo() {
		return totaleTotaleProgressivo;
	}

	/**
	 * @param totaleTotaleProgressivo the totaleTotaleProgressivo to set
	 */
	public void setTotaleTotaleProgressivo(BigDecimal totaleTotaleProgressivo) {
		this.totaleTotaleProgressivo = totaleTotaleProgressivo;
	}

	/**
	 * @return the totaleImponibileIndetraibile
	 */
	public BigDecimal getTotaleImponibileIndetraibile() {
		return totaleImponibileIndetraibile;
	}

	/**
	 * @param totaleImponibileIndetraibile the totaleImponibileIndetraibile to set
	 */
	public void setTotaleImponibileIndetraibile(
			BigDecimal totaleImponibileIndetraibile) {
		this.totaleImponibileIndetraibile = totaleImponibileIndetraibile;
	}

	/**
	 * @return the totaleImponibileDetraibile
	 */
	public BigDecimal getTotaleImponibileDetraibile() {
		return totaleImponibileDetraibile;
	}

	/**
	 * @param totaleImponibileDetraibile the totaleImponibileDetraibile to set
	 */
	public void setTotaleImponibileDetraibile(BigDecimal totaleImponibileDetraibile) {
		this.totaleImponibileDetraibile = totaleImponibileDetraibile;
	}

	/**
	 * @return the totaleImponibileEsente
	 */
	public BigDecimal getTotaleImponibileEsente() {
		return totaleImponibileEsente;
	}

	/**
	 * @param totaleImponibileEsente the totaleImponibileEsente to set
	 */
	public void setTotaleImponibileEsente(BigDecimal totaleImponibileEsente) {
		this.totaleImponibileEsente = totaleImponibileEsente;
	}

	/**
	 * @return the totaleIVAIndetraibile
	 */
	public BigDecimal getTotaleIVAIndetraibile() {
		return totaleIVAIndetraibile;
	}

	/**
	 * @param totaleIVAIndetraibile the totaleIVAIndetraibile to set
	 */
	public void setTotaleIVAIndetraibile(BigDecimal totaleIVAIndetraibile) {
		this.totaleIVAIndetraibile = totaleIVAIndetraibile;
	}

	/**
	 * @return the totaleIVADetraibile
	 */
	public BigDecimal getTotaleIVADetraibile() {
		return totaleIVADetraibile;
	}

	/**
	 * @param totaleIVADetraibile the totaleIVADetraibile to set
	 */
	public void setTotaleIVADetraibile(BigDecimal totaleIVADetraibile) {
		this.totaleIVADetraibile = totaleIVADetraibile;
	}

	/**
	 * @return the totaleProgressivoImponibileIndetraibile
	 */
	public BigDecimal getTotaleProgressivoImponibileIndetraibile() {
		return totaleProgressivoImponibileIndetraibile;
	}

	/**
	 * @param totaleProgressivoImponibileIndetraibile the totaleProgressivoImponibileIndetraibile to set
	 */
	public void setTotaleProgressivoImponibileIndetraibile(
			BigDecimal totaleProgressivoImponibileIndetraibile) {
		this.totaleProgressivoImponibileIndetraibile = totaleProgressivoImponibileIndetraibile;
	}

	/**
	 * @return the totaleProgressivoImponibileDetraibile
	 */
	public BigDecimal getTotaleProgressivoImponibileDetraibile() {
		return totaleProgressivoImponibileDetraibile;
	}

	/**
	 * @param totaleProgressivoImponibileDetraibile the totaleProgressivoImponibileDetraibile to set
	 */
	public void setTotaleProgressivoImponibileDetraibile(
			BigDecimal totaleProgressivoImponibileDetraibile) {
		this.totaleProgressivoImponibileDetraibile = totaleProgressivoImponibileDetraibile;
	}

	/**
	 * @return the totaleProgressivoImponibileEsente
	 */
	public BigDecimal getTotaleProgressivoImponibileEsente() {
		return totaleProgressivoImponibileEsente;
	}

	/**
	 * @param totaleProgressivoImponibileEsente the totaleProgressivoImponibileEsente to set
	 */
	public void setTotaleProgressivoImponibileEsente(
			BigDecimal totaleProgressivoImponibileEsente) {
		this.totaleProgressivoImponibileEsente = totaleProgressivoImponibileEsente;
	}

	/**
	 * @return the totaleProgressivoIvaIndetraibile
	 */
	public BigDecimal getTotaleProgressivoIvaIndetraibile() {
		return totaleProgressivoIvaIndetraibile;
	}

	/**
	 * @param totaleProgressivoIvaIndetraibile the totaleProgressivoIvaIndetraibile to set
	 */
	public void setTotaleProgressivoIvaIndetraibile(
			BigDecimal totaleProgressivoIvaIndetraibile) {
		this.totaleProgressivoIvaIndetraibile = totaleProgressivoIvaIndetraibile;
	}

	/**
	 * @return the totaleProgressivoIvaDetraibile
	 */
	public BigDecimal getTotaleProgressivoIvaDetraibile() {
		return totaleProgressivoIvaDetraibile;
	}

	/**
	 * @param totaleProgressivoIvaDetraibile the totaleProgressivoIvaDetraibile to set
	 */
	public void setTotaleProgressivoIvaDetraibile(
			BigDecimal totaleProgressivoIvaDetraibile) {
		this.totaleProgressivoIvaDetraibile = totaleProgressivoIvaDetraibile;
	}

	/**
	 * @return the percProrata
	 */
	public BigDecimal getPercProrata() {
		return percProrata;
	}

	/**
	 * @param percProrata the percProrata to set
	 */
	public void setPercProrata(BigDecimal percProrata) {
		this.percProrata = percProrata;
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
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	
}
