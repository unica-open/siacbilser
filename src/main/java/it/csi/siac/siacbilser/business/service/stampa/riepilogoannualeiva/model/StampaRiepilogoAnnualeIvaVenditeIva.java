/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRiepilogoAnnualeIvaVenditeIva {
	
	/*
	 * La lista di elementi &egrave; composta dal numero 
	 * dei codici Aliquota Iva utilizzati nell'anno di esercizio 
	 * da tutti i Registri Iva di tipo "VENDITE IVA IMMEDIATA" del Gruppo selezionato.
	 */
	@XmlElements({
		@XmlElement(name="venditaIva", type=StampaRiepilogoAnnualeIvaVenditaIva.class),
	})	
	@XmlElementWrapper(name = "venditeIva")
	private List<StampaRiepilogoAnnualeIvaVenditaIva> venditeIva = new ArrayList<StampaRiepilogoAnnualeIvaVenditaIva>();
	

	private BigDecimal totaliImponibile;
	private BigDecimal totaliIva;
	
	private BigDecimal totaliEsNiFci;
	
	private BigDecimal totaliImponibileEsclusoFci;
	private BigDecimal totaliIvaEsclusoFci;
	
	
	/**
	 * Somma a totaliImponibile un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaliImponibile(BigDecimal tot){
		if(totaliImponibile==null){
			totaliImponibile = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaliImponibile = totaliImponibile.add(tot);
	}
	
	
	/**
	 * Somma a totaliIva un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaliIva(BigDecimal tot){
		if(totaliIva==null){
			totaliIva = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaliIva = totaliIva.add(tot);
	}
	
	
	/**
	 * Somma a totaliEsNiFci un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaliEsNiFci(BigDecimal tot){
		if(totaliEsNiFci==null){
			totaliEsNiFci = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaliEsNiFci = totaliEsNiFci.add(tot);
	}
	
	
	/**
	 * Somma a totaliImponibileEsclusoFci un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaliImponibileEsclusoFci(BigDecimal tot){
		if(totaliImponibileEsclusoFci==null){
			totaliImponibileEsclusoFci = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaliImponibileEsclusoFci = totaliImponibileEsclusoFci.add(tot);
	}
	
	
	/**
	 * Somma a totaliIvaEsclusoFci un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaliIvaEsclusoFci(BigDecimal tot){
		if(totaliIvaEsclusoFci==null){
			totaliIvaEsclusoFci = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaliIvaEsclusoFci = totaliIvaEsclusoFci.add(tot);
	}
	
	/**
	 * @param acquistiIva the acquistiIva to set
	 */
	public void addVenditaIva(StampaRiepilogoAnnualeIvaVenditaIva venditaIva) {
		getVenditeIva().add(venditaIva);
	}
	
	
	
	/**
	 * @return the venditeIva
	 */
	@XmlTransient
	public List<StampaRiepilogoAnnualeIvaVenditaIva> getVenditeIva() {
		return venditeIva;
	}
	/**
	 * @param venditeIva the venditeIva to set
	 */
	public void setVenditeIva(List<StampaRiepilogoAnnualeIvaVenditaIva> venditeIva) {
		if(venditeIva==null){
			venditeIva = new ArrayList<StampaRiepilogoAnnualeIvaVenditaIva>();
		}
		this.venditeIva = venditeIva;
	}
	/**
	 * @return the totaliImponibile
	 */
	public BigDecimal getTotaliImponibile() {
		return totaliImponibile;
	}
	/**
	 * @param totaliImponibile the totaliImponibile to set
	 */
	public void setTotaliImponibile(BigDecimal totaliImponibile) {
		this.totaliImponibile = totaliImponibile;
	}
	/**
	 * @return the totaliIva
	 */
	public BigDecimal getTotaliIva() {
		return totaliIva;
	}
	/**
	 * @param totaliIva the totaliIva to set
	 */
	public void setTotaliIva(BigDecimal totaliIva) {
		this.totaliIva = totaliIva;
	}
	/**
	 * @return the totaliEsNiFci
	 */
	public BigDecimal getTotaliEsNiFci() {
		return totaliEsNiFci;
	}
	/**
	 * @param totaliEsNiFci the totaliEsNiFci to set
	 */
	public void setTotaliEsNiFci(BigDecimal totaliEsNiFci) {
		this.totaliEsNiFci = totaliEsNiFci;
	}
	/**
	 * @return the totaliEsclusoFciImponibile
	 */
	public BigDecimal getTotaliImponibileEsclusoFci() {
		return totaliImponibileEsclusoFci;
	}
	/**
	 * @param totaliEsclusoFciImponibile the totaliEsclusoFciImponibile to set
	 */
	public void setTotaliImponibileEsclusoFci(BigDecimal totaliEsclusoFciImponibile) {
		this.totaliImponibileEsclusoFci = totaliEsclusoFciImponibile;
	}
	/**
	 * @return the totaliEsclusoFciIva
	 */
	public BigDecimal getTotaliIvaEsclusoFci() {
		return totaliIvaEsclusoFci;
	}
	/**
	 * @param totaliEsclusoFciIva the totaliEsclusoFciIva to set
	 */
	public void setTotaliIvaEsclusoFci(BigDecimal totaliEsclusoFciIva) {
		this.totaliIvaEsclusoFci = totaliEsclusoFciIva;
	}

	
	
	
	
	

}
