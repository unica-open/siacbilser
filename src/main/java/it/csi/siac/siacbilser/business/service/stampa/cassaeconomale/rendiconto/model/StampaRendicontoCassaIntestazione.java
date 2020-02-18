/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccorser.model.Ente;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRendicontoCassaIntestazione {
	
	private TipoStampa tipoStampa; //A1
	private Integer numeroDiPagina; //A2
	private Ente ente; //A3
	private String direzione; //A4
	private String settore; //A5
	private String ufficio; //A6
	private String riferimentoCassaEconomale; //A7
	private Date dataStampaRendiconto;//A8
	private Integer numeroRendiconto; //A9
	private Date periodoInizio; //A10
	private Date periodoFine; //A11
	
	private Integer annoDiRiferimentoContabile;
	
	private Date dataCreazioneReport;

	/**
	 * @return the tipoStampa
	 */
	public TipoStampa getTipoStampa() {
		return tipoStampa;
	}

	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	/**
	 * @return the numeroDiPagina
	 */
	public Integer getNumeroDiPagina() {
		return numeroDiPagina;
	}

	/**
	 * @param numeroDiPagina the numeroDiPagina to set
	 */
	public void setNumeroDiPagina(Integer numeroDiPagina) {
		this.numeroDiPagina = numeroDiPagina;
	}

	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	/**
	 * @return the direzione
	 */
	public String getDirezione() {
		return direzione;
	}

	/**
	 * @param direzione the direzione to set
	 */
	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	/**
	 * @return the settore
	 */
	public String getSettore() {
		return settore;
	}

	/**
	 * @param settore the settore to set
	 */
	public void setSettore(String settore) {
		this.settore = settore;
	}

	/**
	 * @return the ufficio
	 */
	public String getUfficio() {
		return ufficio;
	}

	/**
	 * @param ufficio the ufficio to set
	 */
	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	/**
	 * @return the riferimentoCassaEconomale
	 */
	public String getRiferimentoCassaEconomale() {
		return riferimentoCassaEconomale;
	}

	/**
	 * @param riferimentoCassaEconomale the riferimentoCassaEconomale to set
	 */
	public void setRiferimentoCassaEconomale(String riferimentoCassaEconomale) {
		this.riferimentoCassaEconomale = riferimentoCassaEconomale;
	}

	/**
	 * @return the dataStampaRendiconto
	 */
	public Date getDataStampaRendiconto() {
		return dataStampaRendiconto == null ? null : new Date(dataStampaRendiconto.getTime());
	}

	/**
	 * @param dataStampaRendiconto the dataStampaRendiconto to set
	 */
	public void setDataStampaRendiconto(Date dataStampaRendiconto) {
		this.dataStampaRendiconto = dataStampaRendiconto == null ? null : new Date(dataStampaRendiconto.getTime());
	}

	/**
	 * @return the numeroRendiconto
	 */
	public Integer getNumeroRendiconto() {
		return numeroRendiconto;
	}

	/**
	 * @param numeroRendiconto the numeroRendiconto to set
	 */
	public void setNumeroRendiconto(Integer numeroRendiconto) {
		this.numeroRendiconto = numeroRendiconto;
	}

	/**
	 * @return the periodoInizio
	 */
	public Date getPeriodoInizio() {
		return periodoInizio == null ? null : new Date(periodoInizio.getTime());
	}

	/**
	 * @param periodoInizio the periodoInizio to set
	 */
	public void setPeriodoInizio(Date periodoInizio) {
		this.periodoInizio = periodoInizio == null ? null : new Date(periodoInizio.getTime());
	}



	/**
	 * @return the periodoFine
	 */
	public Date getPeriodoFine() {
		return periodoFine == null ? null : new Date(periodoFine.getTime());
	}

	/**
	 * @param periodoFine the periodoFine to set
	 */
	public void setPeriodoFine(Date periodoFine) {
		this.periodoFine = periodoFine == null ? null : new Date(periodoFine.getTime());
	}

	/**
	 * @return the annoDiRiferimentoContabile
	 */
	public Integer getAnnoDiRiferimentoContabile() {
		return annoDiRiferimentoContabile;
	}

	/**
	 * @param annoDiRiferimentoContabile the annoDiRiferimentoContabile to set
	 */
	public void setAnnoDiRiferimentoContabile(Integer annoDiRiferimentoContabile) {
		this.annoDiRiferimentoContabile = annoDiRiferimentoContabile;
	}

	/**
	 * @return the dataCreazioneReport
	 */
	public Date getDataCreazioneReport() {
		return dataCreazioneReport == null ? null : new Date(dataCreazioneReport.getTime());
	}

	/**
	 * @param dataCreazioneReport the dataCreazioneReport to set
	 */
	public void setDataCreazioneReport(Date dataCreazioneReport) {
		this.dataCreazioneReport = dataCreazioneReport == null ? null : new Date(dataCreazioneReport.getTime());
	}
	
	

}

