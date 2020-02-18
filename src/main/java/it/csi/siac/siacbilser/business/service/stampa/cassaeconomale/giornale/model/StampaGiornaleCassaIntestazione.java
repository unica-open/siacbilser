/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccorser.model.Ente;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaGiornaleCassaIntestazione {
	
	private Ente ente;
	private String direzione;
	private String settore;
	private Date dataStampaGiornale;
	
	private TipoStampa tipoStampa;
	private Integer numeroDiPagina;
	private Integer annoDiRiferimentoContabile;
	
	private String riferimentoCassaEconomale; 
	
	private Date dataCreazioneReport;

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
	 * @return the dataStampaGiornale
	 */
	public Date getDataStampaGiornale() {
		return dataStampaGiornale == null ? null : new Date(dataStampaGiornale.getTime());
	}

	/**
	 * @param dataStampaGiornale the dataStampaGiornale to set
	 */
	public void setDataStampaGiornale(Date dataStampaGiornale) {
		this.dataStampaGiornale = dataStampaGiornale == null ? null : new Date(dataStampaGiornale.getTime());
	}

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
