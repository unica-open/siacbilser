/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRiepilogoAnnualeIvaReportModel {
	
	private StampaRiepilogoAnnualeIvaIntestazione intestazione;
	
	private StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaImmediata;
	private StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDifferita;
	private StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDifferitaPagata;
	
	private StampaRiepilogoAnnualeIvaVenditeIva venditeIvaImmediata;
	private StampaRiepilogoAnnualeIvaVenditeIva venditeIvaDifferita;
	private StampaRiepilogoAnnualeIvaVenditeIva venditeIvaDifferitaIncassati;
	private StampaRiepilogoAnnualeIvaVenditeIva corrispettivi;
	
	/**
	 * @return the acquistiIvaImmediata
	 */
	public StampaRiepilogoAnnualeIvaAcquistiIva getAcquistiIvaImmediata() {
		return acquistiIvaImmediata;
	}
	/**
	 * @param acquistiIvaImmediata the acquistiIvaImmediata to set
	 */
	public void setAcquistiIvaImmediata(StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaImmediata) {
		this.acquistiIvaImmediata = acquistiIvaImmediata;
	}
	/**
	 * @return the acquistiIvaDifferita
	 */
	public StampaRiepilogoAnnualeIvaAcquistiIva getAcquistiIvaDifferita() {
		return acquistiIvaDifferita;
	}
	/**
	 * @param acquistiIvaDifferita the acquistiIvaDifferita to set
	 */
	public void setAcquistiIvaDifferita(StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDifferita) {
		this.acquistiIvaDifferita = acquistiIvaDifferita;
	}
	/**
	 * @return the acquistiIvaDiffPag
	 */
	public StampaRiepilogoAnnualeIvaAcquistiIva getAcquistiIvaDifferitaPagata() {
		return acquistiIvaDifferitaPagata;
	}
	/**
	 * @param acquistiIvaDiffPag the acquistiIvaDiffPag to set
	 */
	public void setAcquistiIvaDifferitaPagata(StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDiffPag) {
		this.acquistiIvaDifferitaPagata = acquistiIvaDiffPag;
	}
	/**
	 * @return the venditaIvaImmediata
	 */
	public StampaRiepilogoAnnualeIvaVenditeIva getVenditeIvaImmediata() {
		return venditeIvaImmediata;
	}
	/**
	 * @param venditaIvaImmediata the venditaIvaImmediata to set
	 */
	public void setVenditeIvaImmediata(StampaRiepilogoAnnualeIvaVenditeIva venditaIvaImmediata) {
		this.venditeIvaImmediata = venditaIvaImmediata;
	}
	/**
	 * @return the venditaIvaDifferita
	 */
	public StampaRiepilogoAnnualeIvaVenditeIva getVenditeIvaDifferita() {
		return venditeIvaDifferita;
	}
	/**
	 * @param venditaIvaDifferita the venditaIvaDifferita to set
	 */
	public void setVenditeIvaDifferita(StampaRiepilogoAnnualeIvaVenditeIva venditaIvaDifferita) {
		this.venditeIvaDifferita = venditaIvaDifferita;
	}
	/**
	 * @return the corrispettivi
	 */
	public StampaRiepilogoAnnualeIvaVenditeIva getCorrispettivi() {
		return corrispettivi;
	}
	/**
	 * @param corrispettivi the corrispettivi to set
	 */
	public void setCorrispettivi(StampaRiepilogoAnnualeIvaVenditeIva corrispettivi) {
		this.corrispettivi = corrispettivi;
	}
	/**
	 * @return the intestazione
	 */
	public StampaRiepilogoAnnualeIvaIntestazione getIntestazione() {
		return intestazione;
	}
	/**
	 * @param intestazione the intestazione to set
	 */
	public void setIntestazione(StampaRiepilogoAnnualeIvaIntestazione intestazione) {
		this.intestazione = intestazione;
	}
	/**
	 * @return the venditaIvaDifferitaIncassati
	 */
	public StampaRiepilogoAnnualeIvaVenditeIva getVenditeIvaDifferitaIncassati() {
		return venditeIvaDifferitaIncassati;
	}
	/**
	 * @param venditaIvaDifferitaIncassati the venditaIvaDifferitaIncassati to set
	 */
	public void setVenditeIvaDifferitaIncassati(StampaRiepilogoAnnualeIvaVenditeIva venditaIvaDifferitaIncassati) {
		this.venditeIvaDifferitaIncassati = venditaIvaDifferitaIncassati;
	}
	
	
	
	
	
	

}
