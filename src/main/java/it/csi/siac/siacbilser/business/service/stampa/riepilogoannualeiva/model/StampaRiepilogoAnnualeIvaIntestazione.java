/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRiepilogoAnnualeIvaIntestazione {
	
	private Ente ente;
	private Soggetto soggetto;
	private IndirizzoSoggetto indirizzoSoggetto;
	
	private Date dataCreazioneReport;
	private String titolo;
	
	private TipoStampa tipoStampa;
	private Integer numeroDiPagina;
	private Integer annoDiRiferimentoContabile;
	
	private GruppoAttivitaIva gruppoAttivitaIva;

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
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the indirizzoSoggetto
	 */
	public IndirizzoSoggetto getIndirizzoSoggetto() {
		return indirizzoSoggetto;
	}

	/**
	 * @param indirizzoSoggetto the indirizzoSoggetto to set
	 */
	public void setIndirizzoSoggetto(IndirizzoSoggetto indirizzoSoggetto) {
		this.indirizzoSoggetto = indirizzoSoggetto;
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

	/**
	 * @return the titolo
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
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
	 * @return the gruppoAttivitaIva
	 */
	public GruppoAttivitaIva getGruppoAttivitaIva() {
		return gruppoAttivitaIva;
	}

	/**
	 * @param gruppoAttivitaIva the gruppoAttivitaIva to set
	 */
	public void setGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		this.gruppoAttivitaIva = gruppoAttivitaIva;
	}
	
	
	

}
