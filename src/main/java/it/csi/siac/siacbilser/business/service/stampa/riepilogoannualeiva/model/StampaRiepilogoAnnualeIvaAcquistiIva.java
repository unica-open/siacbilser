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
public class StampaRiepilogoAnnualeIvaAcquistiIva {
	
	
	@XmlElements({
		@XmlElement(name="acquistoIva", type=StampaRiepilogoAnnualeIvaAcquistoIva.class),
	})	
	@XmlElementWrapper(name = "acquistiIva")
	private List<StampaRiepilogoAnnualeIvaAcquistoIva> acquistiIva = new ArrayList<StampaRiepilogoAnnualeIvaAcquistoIva>();
	
	/*
	 * Il valore è dato dalla sommatoria dei valori 
	 * della colonna F1 (Imponibile), ognuno moltiplicato per il valore 
	 * corrispondente della % di indetraibilità riferito al proprio codice aliquota.
	 */
	private BigDecimal totaleIndetraibiliImponibile;
	
	/*
	 * Il valore è dato dalla sommatoria dei valori 
	 * della colonna G1 (IVA), ognuno moltiplicato per il valore 
	 * corrispondente della % di indetraibilità riferito al proprio codice aliquota.
	 */
	private BigDecimal totaleIndetraibiliIva;
	
	
	
	/*
	 * Il valore è dato dalla sommatoria dei valori 
	 * della colonna F1 (Imponibile), ognuno moltiplicato per 
	 * (100 - il valore corrispondente della % di indetraibilità riferito al proprio codice aliquota).
	 */
	private BigDecimal totaleDetraibiliImponibile;
	
	/*
	 * Il valore è dato dalla sommatoria dei valori 
	 * della colonna G1 (IVA), ognuno moltiplicato per 
	 * (100 - il valore corrispondente della % di indetraibilità riferito al proprio codice aliquota).
	 */
	private BigDecimal totaleDetraibiliIva;
	
	/*
	 * Il valore è dato dalla sommatoria dei soli valori della colonna F1 (Imponibile) corrispondenti ai codici aliquota delle tipologie (colonna C1):
	 *	-	N.I.
	 *	-	ES
	 *	-	F.C.I.
	 */
	private BigDecimal totaliEsNiFci;
	
	
	//Sommatoria dei valori della colonna F1	
	private BigDecimal totaliImponibile;
	
	//Sommatoria dei valori della colonna G1
	private BigDecimal totaliIva;
	
	//Sommatoria dei valori della colonna F1 – la sommatoria dei valori della colonna F1 corrispondenti alla riga delle aliquote di tipo (colonna C1) F.C.I.
	private BigDecimal totaliImponibileEsclusoFci;
	
	
	/**
	 * Somma a totaleIndetraibiliImponibile un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaleIndetraibiliImponibile(BigDecimal tot){
		if(totaleIndetraibiliImponibile==null){
			totaleIndetraibiliImponibile = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaleIndetraibiliImponibile = totaleIndetraibiliImponibile.add(tot);
	}
	
	/**
	 * Somma a totaleIndetraibiliIva un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaleIndetraibiliIva(BigDecimal tot){
		if(totaleIndetraibiliIva==null){
			totaleIndetraibiliIva = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaleIndetraibiliIva = totaleIndetraibiliIva.add(tot);
	}
	
	/**
	 * Somma a totaleDetraibiliImponibile un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaleDetraibiliImponibile(BigDecimal tot){
		if(totaleDetraibiliImponibile==null){
			totaleDetraibiliImponibile = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaleDetraibiliImponibile = totaleDetraibiliImponibile.add(tot);
	}
	
	
	/**
	 * Somma a totaleDetraibiliIva un valore passato in input
	 * 
	 * @param tot il valore da sommare
	 */
	public void addTotaleDetraibiliIva(BigDecimal tot){
		if(totaleDetraibiliIva==null){
			totaleDetraibiliIva = BigDecimal.ZERO;
		}
		if(tot==null){
			tot = BigDecimal.ZERO;
		}
		totaleDetraibiliIva = totaleDetraibiliIva.add(tot);
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
	 * @param acquistiIva the acquistiIva to set
	 */
	public void addAcquistoIva(StampaRiepilogoAnnualeIvaAcquistoIva acquistoIva) {
		getAcquistiIva().add(acquistoIva);
	}
	
	

	/**
	 * @return the acquistiIva
	 */
	@XmlTransient
	public List<StampaRiepilogoAnnualeIvaAcquistoIva> getAcquistiIva() {
		return acquistiIva;
	}

	/**
	 * @param acquistiIva the acquistiIva to set
	 */	
	public void setAcquistiIva(List<StampaRiepilogoAnnualeIvaAcquistoIva> acquistiIva) {
		if(acquistiIva==null){
			acquistiIva = new ArrayList<StampaRiepilogoAnnualeIvaAcquistoIva>();
		}
		this.acquistiIva = acquistiIva;
	}

	/**
	 * @return the totaleIndetraibiliImponibile
	 */
	public BigDecimal getTotaleIndetraibiliImponibile() {
		return totaleIndetraibiliImponibile;
	}

	/**
	 * @param totaleIndetraibiliImponibile the totaleIndetraibiliImponibile to set
	 */
	public void setTotaleIndetraibiliImponibile(BigDecimal totaleIndetraibiliImponibile) {
		this.totaleIndetraibiliImponibile = totaleIndetraibiliImponibile;
	}

	/**
	 * @return the totaleIndetraibiliIva
	 */
	public BigDecimal getTotaleIndetraibiliIva() {
		return totaleIndetraibiliIva;
	}

	/**
	 * @param totaleIndetraibiliIva the totaleIndetraibiliIva to set
	 */
	public void setTotaleIndetraibiliIva(BigDecimal totaleIndetraibiliIva) {
		this.totaleIndetraibiliIva = totaleIndetraibiliIva;
	}

	/**
	 * @return the totaleDetraibiliImponibile
	 */
	public BigDecimal getTotaleDetraibiliImponibile() {
		return totaleDetraibiliImponibile;
	}

	/**
	 * @param totaleDetraibiliImponibile the totaleDetraibiliImponibile to set
	 */
	public void setTotaleDetraibiliImponibile(BigDecimal totaleDetraibiliImponibile) {
		this.totaleDetraibiliImponibile = totaleDetraibiliImponibile;
	}

	/**
	 * @return the totaleDetraibiliIva
	 */
	public BigDecimal getTotaleDetraibiliIva() {
		return totaleDetraibiliIva;
	}

	/**
	 * @param totaleDetraibiliIva the totaleDetraibiliIva to set
	 */
	public void setTotaleDetraibiliIva(BigDecimal totaleDetraibiliIva) {
		this.totaleDetraibiliIva = totaleDetraibiliIva;
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
	 * @return the totaliImponibileEsclusoFci
	 */
	public BigDecimal getTotaliImponibileEsclusoFci() {
		return totaliImponibileEsclusoFci;
	}

	/**
	 * @param totaliImponibileEsclusoFci the totaliImponibileEsclusoFci to set
	 */
	public void setTotaliImponibileEsclusoFci(BigDecimal totaliImponibileEsclusoFci) {
		this.totaliImponibileEsclusoFci = totaliImponibileEsclusoFci;
	}
	
	
	
	
	
	

}

