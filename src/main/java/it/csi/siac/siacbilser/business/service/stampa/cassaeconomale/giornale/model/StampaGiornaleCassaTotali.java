/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaGiornaleCassaTotali {
	BigDecimal totaleEntrate = BigDecimal.ZERO;
	BigDecimal totaleUscite = BigDecimal.ZERO;
	BigDecimal saldoTotale = BigDecimal.ZERO;
	
	BigDecimal ultTotaleCassaEntrate = BigDecimal.ZERO;
    BigDecimal ultTotaleCassaUscite = BigDecimal.ZERO;
    
	BigDecimal totaleRipEntrate = BigDecimal.ZERO;
	BigDecimal totaleRipUscite = BigDecimal.ZERO;
	
	BigDecimal saldoRiporto = BigDecimal.ZERO;

	/**
	 * @return the totaleEntrate
	 */
	public BigDecimal getTotaleEntrate() {
		return totaleEntrate;
	}

	/**
	 * @param totaleEntrate the totaleEntrate to set
	 */
	public void setTotaleEntrate(BigDecimal totaleEntrate) {
		this.totaleEntrate = totaleEntrate;
	}

	/**
	 * @return the totaleUscite
	 */
	public BigDecimal getTotaleUscite() {
		return totaleUscite;
	}

	/**
	 * @param totaleUscite the totaleUscite to set
	 */
	public void setTotaleUscite(BigDecimal totaleUscite) {
		this.totaleUscite = totaleUscite;
	}

	/**
	 * @return the saldoTotale
	 */
	public BigDecimal getSaldoTotale() {
		return saldoTotale;
	}

	/**
	 * @param saldoTotale the saldoTotale to set
	 */
	public void setSaldoTotale(BigDecimal saldoTotale) {
		this.saldoTotale = saldoTotale;
	}

	/**
	 * @return the ultTotaleCassaEntrate
	 */
	public BigDecimal getUltTotaleCassaEntrate() {
		return ultTotaleCassaEntrate;
	}

	/**
	 * @param ultTotaleCassaEntrate the ultTotaleCassaEntrate to set
	 */
	public void setUltTotaleCassaEntrate(BigDecimal ultTotaleCassaEntrate) {
		this.ultTotaleCassaEntrate = ultTotaleCassaEntrate;
	}

	/**
	 * @return the ultTotaleCassaUscite
	 */
	public BigDecimal getUltTotaleCassaUscite() {
		return ultTotaleCassaUscite;
	}

	/**
	 * @param ultTotaleCassaUscite the ultTotaleCassaUscite to set
	 */
	public void setUltTotaleCassaUscite(BigDecimal ultTotaleCassaUscite) {
		this.ultTotaleCassaUscite = ultTotaleCassaUscite;
	}

	/**
	 * @return the totaleRipEntrate
	 */
	public BigDecimal getTotaleRipEntrate() {
		return totaleRipEntrate;
	}

	/**
	 * @param totaleRipEntrate the totaleRipEntrate to set
	 */
	public void setTotaleRipEntrate(BigDecimal totaleRipEntrate) {
		this.totaleRipEntrate = totaleRipEntrate;
	}

	/**
	 * @return the totaleRipUscite
	 */
	public BigDecimal getTotaleRipUscite() {
		return totaleRipUscite;
	}

	/**
	 * @param totaleRipUscite the totaleRipUscite to set
	 */
	public void setTotaleRipUscite(BigDecimal totaleRipUscite) {
		this.totaleRipUscite = totaleRipUscite;
	}

	/**
	 * @return the saldoRiporto
	 */
	public BigDecimal getSaldoRiporto() {
		return saldoRiporto;
	}

	/**
	 * @param saldoRiporto the saldoRiporto to set
	 */
	public void setSaldoRiporto(BigDecimal saldoRiporto) {
		this.saldoRiporto = saldoRiporto;
	}

	
}
