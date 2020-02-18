/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaAllegatoAttoElenco {
	
	private Soggetto soggetto;
	// SIAC-4632
	private IndirizzoSoggetto indirizzoSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private BigDecimal totaleImpegni = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;
	
	// SIAC-4201
	private Soggetto soggettoCessione;
	
	@XmlElementWrapper(name="impegniSubimpegni")
	@XmlElement(name="impegnoSubimpegno")
	private List<StampaAllegatoAttoImpegno> listaStampaAllegatoAttoImpegno = new ArrayList<StampaAllegatoAttoImpegno>();
	
	@XmlElementWrapper(name="subdocumenti")
	@XmlElement(name="subdocumento")
	private List<StampaAllegatoAttoSubdocumento> listaStampaAllegatoAttoSubdocumento = new ArrayList<StampaAllegatoAttoSubdocumento>();

	//SIAC-6190
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	
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
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the totaleImpegni
	 */
	public BigDecimal getTotaleImpegni() {
		return totaleImpegni;
	}

	/**
	 * @param totaleImpegni the totaleImpegni to set
	 */
	public void setTotaleImpegni(BigDecimal totaleImpegni) {
		this.totaleImpegni = totaleImpegni != null ? totaleImpegni : BigDecimal.ZERO;
	}
	
	/**
	 * Adds to totaleImpegni.
	 * 
	 * @param augend the augend
	 */
	public void addTotaleImpegni(BigDecimal augend) {
		if(augend == null) {
			return;
		}
		this.totaleImpegni = this.totaleImpegni.add(augend);
	}
	
	/**
	 * @return the totaleImportoDaDedurre
	 */
	public BigDecimal getTotaleImportoDaDedurre() {
		return totaleImportoDaDedurre;
	}

	/**
	 * @param totaleImportoDaDedurre the totaleImportoDaDedurre to set
	 */
	public void setTotaleImportoDaDedurre(BigDecimal totaleImportoDaDedurre) {
		this.totaleImportoDaDedurre = totaleImportoDaDedurre != null? totaleImportoDaDedurre : BigDecimal.ZERO;
	}
	
	/**
	 * Adds to totaleImportoDaDedurre
	 * 
	 * @param augend the augend
	 */
	public void addTotaleImportoDaDedurre(BigDecimal augend) {
		if(augend == null) {
			return;
		}
		this.totaleImportoDaDedurre = this.totaleImportoDaDedurre.add(augend);
	}

	/**
	 * @return the soggettoCessione
	 */
	public Soggetto getSoggettoCessione() {
		return soggettoCessione;
	}

	/**
	 * @param soggettoCessione the soggettoCessione to set
	 */
	public void setSoggettoCessione(Soggetto soggettoCessione) {
		this.soggettoCessione = soggettoCessione;
	}

	/**
	 * @return the listaStampaAllegatoAttoImpegno
	 */
	@XmlTransient
	public List<StampaAllegatoAttoImpegno> getListaStampaAllegatoAttoImpegno() {
		return listaStampaAllegatoAttoImpegno;
	}

	/**
	 * @param listaStampaAllegatoAttoImpegno the listaStampaAllegatoAttoImpegno to set
	 */
	public void setListaStampaAllegatoAttoImpegno(List<StampaAllegatoAttoImpegno> listaStampaAllegatoAttoImpegno) {
		this.listaStampaAllegatoAttoImpegno = listaStampaAllegatoAttoImpegno;
	}

	/**
	 * @return the listaStampaAllegatoAttoSubdocumento
	 */
	@XmlTransient
	public List<StampaAllegatoAttoSubdocumento> getListaStampaAllegatoAttoSubdocumento() {
		return listaStampaAllegatoAttoSubdocumento;
	}

	/**
	 * @param listaStampaAllegatoAttoSubdocumento the listaStampaAllegatoAttoSubdocumento to set
	 */
	public void setListaStampaAllegatoAttoSubdocumento(List<StampaAllegatoAttoSubdocumento> listaStampaAllegatoAttoSubdocumento) {
		this.listaStampaAllegatoAttoSubdocumento = listaStampaAllegatoAttoSubdocumento;
	}

	/**
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}
	
}
