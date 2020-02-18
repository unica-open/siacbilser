/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.Movimento;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaGiornaleCassaMovimento extends Movimento {
	
	/** per serializzazione **/
	private static final long serialVersionUID = 5400736255724038087L;
	
	private String denominazioneSoggetto = "";
	private String modalitaPagamentoPerStampa;
	private String annoCapitoloPerStampa;
	private String numeroCapitoloPerStampa;
	private String articoloCapitoloPerStampa;

	private BigDecimal importoMovEntrataCC = BigDecimal.ZERO;
	private BigDecimal importoMovUscitaCC = BigDecimal.ZERO;
	private BigDecimal importoMovEntrataCO = BigDecimal.ZERO;
	private BigDecimal importoMovUscitaCO = BigDecimal.ZERO;

	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}
	/**
	 * @param denominazioneSoggetto the denominazioneSoggetto to set
	 */
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}
	/**
	 * @return the modalitaPagamentoPerStampa
	 */
	public String getModalitaPagamentoPerStampa() {
		return modalitaPagamentoPerStampa;
	}
	/**
	 * @param modalitaPagamentoPerStampa the modalitaPagamentoPerStampa to set
	 */
	public void setModalitaPagamentoPerStampa(String modalitaPagamentoPerStampa) {
		this.modalitaPagamentoPerStampa = modalitaPagamentoPerStampa;
	}
	
	/**
	 * @return the importoMovEntrataCC
	 */
	public BigDecimal getImportoMovEntrataCC() {
		return importoMovEntrataCC;
	}
	/**
	 * @param importoMovEntrataCC the importoMovEntrataCC to set
	 */
	public void setImportoMovEntrataCC(BigDecimal importoMovEntrataCC) {
		this.importoMovEntrataCC = importoMovEntrataCC;
	}
	/**
	 * @return the importoMovUscitaCC
	 */
	public BigDecimal getImportoMovUscitaCC() {
		return importoMovUscitaCC;
	}
	/**
	 * @param importoMovUscitaCC the importoMovUscitaCC to set
	 */
	public void setImportoMovUscitaCC(BigDecimal importoMovUscitaCC) {
		this.importoMovUscitaCC = importoMovUscitaCC;
	}
	/**
	 * @return the importoMovEntrataCO
	 */
	public BigDecimal getImportoMovEntrataCO() {
		return importoMovEntrataCO;
	}
	/**
	 * @param importoMovEntrataCO the importoMovEntrataCO to set
	 */
	public void setImportoMovEntrataCO(BigDecimal importoMovEntrataCO) {
		this.importoMovEntrataCO = importoMovEntrataCO;
	}
	/**
	 * @return the importoMovUscitaCO
	 */
	public BigDecimal getImportoMovUscitaCO() {
		return importoMovUscitaCO;
	}
	/**
	 * @param importoMovUscitaCO the importoMovUscitaCO to set
	 */
	public void setImportoMovUscitaCO(BigDecimal importoMovUscitaCO) {
		this.importoMovUscitaCO = importoMovUscitaCO;
	}
	/**
	 * @return the annoCapitoloPerStampa
	 */
	public String getAnnoCapitoloPerStampa() {
		return annoCapitoloPerStampa;
	}
	/**
	 * @param annoCapitoloPerStampa the annoCapitoloPerStampa to set
	 */
	public void setAnnoCapitoloPerStampa(String annoCapitoloPerStampa) {
		this.annoCapitoloPerStampa = annoCapitoloPerStampa;
	}
	/**
	 * @return the numeroCapitoloPerStampa
	 */
	public String getNumeroCapitoloPerStampa() {
		return numeroCapitoloPerStampa;
	}
	/**
	 * @param numeroCapitoloPerStampa the numeroCapitoloPerStampa to set
	 */
	public void setNumeroCapitoloPerStampa(String numeroCapitoloPerStampa) {
		this.numeroCapitoloPerStampa = numeroCapitoloPerStampa;
	}
	/**
	 * @return the articoloCapitoloPerStampa
	 */
	public String getArticoloCapitoloPerStampa() {
		return articoloCapitoloPerStampa;
	}
	/**
	 * @param articoloCapitoloPerStampa the articoloCapitoloPerStampa to set
	 */
	public void setArticoloCapitoloPerStampa(String articoloCapitoloPerStampa) {
		this.articoloCapitoloPerStampa = articoloCapitoloPerStampa;
	}
}
