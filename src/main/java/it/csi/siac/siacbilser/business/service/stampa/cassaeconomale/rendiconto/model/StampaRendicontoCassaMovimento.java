/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRendicontoCassaMovimento extends Movimento {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8762718060795787697L;
//	private Movimento movimento ;
	
	private BigDecimal importoRigaMovimento = BigDecimal.ZERO;
	private Boolean flagFattura = Boolean.FALSE;
	private Boolean flagAnticipoSpeseMissione = Boolean.FALSE;
	private Boolean flagIntegrato = Boolean.FALSE;
	private String numeroFattura = "";
	private String annoFattura = "";
	private Soggetto soggetto ;

	/**
	 * @return the numeroFattura
	 */
	public String getNumeroFattura() {
		return numeroFattura;
	}
	/**
	 * @param numeroFattura the numeroFattura to set
	 */
	public void setNumeroFattura(String numeroFattura) {
		this.numeroFattura = numeroFattura;
	}
	/**
	 * @return the flagFattura
	 */
	public Boolean getFlagFattura() {
		return flagFattura;
	}
	/**
	 * @param flagFattura the flagFattura to set
	 */
	public void setFlagFattura(Boolean flagFattura) {
		this.flagFattura = flagFattura;
	}
	/**
	 * @return the flagAnticipoSpeseMissione
	 */
	public final Boolean getFlagAnticipoSpeseMissione() {
		return flagAnticipoSpeseMissione;
	}
	/**
	 * @param flagAnticipoSpeseMissione the flagAnticipoSpeseMissione to set
	 */
	public final void setFlagAnticipoSpeseMissione(Boolean flagAnticipoSpeseMissione) {
		this.flagAnticipoSpeseMissione = flagAnticipoSpeseMissione;
	}
	/**
	 * @return the flagIntegrato
	 */
	public Boolean getFlagIntegrato() {
		return flagIntegrato;
	}
	/**
	 * @param flagIntegrato the flagIntegrato to set
	 */
	public void setFlagIntegrato(Boolean flagIntegrato) {
		this.flagIntegrato = flagIntegrato;
	}
	/**
	 * @return the importoRigaMovimento
	 */
	public BigDecimal getImportoRigaMovimento() {
		return importoRigaMovimento;
	}
	/**
	 * @param importoRigaMovimento the importoRigaMovimento to set
	 */
	public void setImportoRigaMovimento(BigDecimal importoRigaMovimento) {
		this.importoRigaMovimento = importoRigaMovimento;
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
	 * @return the annoFattura
	 */
	public String getAnnoFattura() {
		return annoFattura;
	}
	/**
	 * @param annoFattura the annoFattura to set
	 */
	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	
	
}
