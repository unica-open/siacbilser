/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaLiquidazioneIvaReportModel {
	
	private StampaLiquidazioneIvaIntestazione intestazione;
	
	private StampaLiquidazioneIvaDatiERiepilogo acquistiIvaImmediata;
	private StampaLiquidazioneIvaDatiERiepilogo acquistiIvaDifferitaPagata;
	private StampaLiquidazioneIvaDatiERiepilogo venditeIvaImmediata;
	private StampaLiquidazioneIvaDatiERiepilogo venditeIvaDifferitaIncassata;
	private StampaLiquidazioneIvaDatiERiepilogo corrispettivi;
	
	private StampaLiquidazioneIvaRiepilogoGlobale riepilogoGlobale;
	
	/**
	 * @return the stampaLiquidazioneIvaIntestazione
	 */
	public StampaLiquidazioneIvaIntestazione getIntestazione() {
		return intestazione;
	}
	/**
	 * @param stampaLiquidazioneIvaIntestazione the stampaLiquidazioneIvaIntestazione to set
	 */
	public void setIntestazione(
			StampaLiquidazioneIvaIntestazione stampaLiquidazioneIvaIntestazione) {
		this.intestazione = stampaLiquidazioneIvaIntestazione;
	}
	/**
	 * @return the stampaLiquidazioneIvaDatiERiepilogoAcquistiIvaImmediata
	 */
	public StampaLiquidazioneIvaDatiERiepilogo getAcquistiIvaImmediata() {
		return acquistiIvaImmediata;
	}
	/**
	 * @param acquistiIvaImmediata the stampaLiquidazioneIvaDatiERiepilogoAcquistiIvaImmediata to set
	 */
	public void setAcquistiIvaImmediata(
			StampaLiquidazioneIvaDatiERiepilogo stampaLiquidazioneIvaPaginaAcquistiIvaImmediata) {
		this.acquistiIvaImmediata = stampaLiquidazioneIvaPaginaAcquistiIvaImmediata;
	}
	/**
	 * @return the stampaLiquidazioneIvaDatiERiepilogoAcquistiIvaDifferitaPagata
	 */
	public StampaLiquidazioneIvaDatiERiepilogo getAcquistiIvaDifferitaPagata() {
		return acquistiIvaDifferitaPagata;
	}
	/**
	 * @param acquistiIvaDifferitaPagata the stampaLiquidazioneIvaDatiERiepilogoAcquistiIvaDifferitaPagata to set
	 */
	public void setAcquistiIvaDifferitaPagata(
			StampaLiquidazioneIvaDatiERiepilogo stampaLiquidazioneIvaPaginaAcquistiIvaDifferitaPagata) {
		this.acquistiIvaDifferitaPagata = stampaLiquidazioneIvaPaginaAcquistiIvaDifferitaPagata;
	}
	/**
	 * @return the stampaLiquidazioneIvaDatiERiepilogoVenditeIvaImmediata
	 */
	public StampaLiquidazioneIvaDatiERiepilogo getVenditeIvaImmediata() {
		return venditeIvaImmediata;
	}
	/**
	 * @param venditeIvaImmediata the stampaLiquidazioneIvaDatiERiepilogoVenditeIvaImmediata to set
	 */
	public void setVenditeIvaImmediata(
			StampaLiquidazioneIvaDatiERiepilogo stampaLiquidazioneIvaPaginaVenditeIvaImmediata) {
		this.venditeIvaImmediata = stampaLiquidazioneIvaPaginaVenditeIvaImmediata;
	}
	/**
	 * @return the stampaLiquidazioneIvaDatiERiepilogoVenditeIvaDifferitaIncassata
	 */
	public StampaLiquidazioneIvaDatiERiepilogo getVenditeIvaDifferitaIncassata() {
		return venditeIvaDifferitaIncassata;
	}
	/**
	 * @param venditeIvaDifferitaIncassata the stampaLiquidazioneIvaDatiERiepilogoVenditeIvaDifferitaIncassata to set
	 */
	public void setVenditeIvaDifferitaIncassata(
			StampaLiquidazioneIvaDatiERiepilogo stampaLiquidazioneIvaPaginaVenditeIvaDifferitaIncassata) {
		this.venditeIvaDifferitaIncassata = stampaLiquidazioneIvaPaginaVenditeIvaDifferitaIncassata;
	}
	/**
	 * @return the stampaLiquidazioneIvaDatiERiepilogoCorrispettivi
	 */
	public StampaLiquidazioneIvaDatiERiepilogo getCorrispettivi() {
		return corrispettivi;
	}
	/**
	 * @param corrispettivi the stampaLiquidazioneIvaDatiERiepilogoCorrispettivi to set
	 */
	public void setCorrispettivi(
			StampaLiquidazioneIvaDatiERiepilogo stampaLiquidazioneIvaPaginaCorrispettivi) {
		this.corrispettivi = stampaLiquidazioneIvaPaginaCorrispettivi;
	}
	/**
	 * @return the stampaLiquidazioneIvaRiepilogoGlobale
	 */
	public StampaLiquidazioneIvaRiepilogoGlobale getRiepilogoGlobale() {
		return riepilogoGlobale;
	}
	/**
	 * @param stampaLiquidazioneIvaRiepilogoGlobale the stampaLiquidazioneIvaRiepilogoGlobale to set
	 */
	public void setRiepilogoGlobale(
			StampaLiquidazioneIvaRiepilogoGlobale stampaLiquidazioneIvaRiepilogoGlobale) {
		this.riepilogoGlobale = stampaLiquidazioneIvaRiepilogoGlobale;
	}
	
}
