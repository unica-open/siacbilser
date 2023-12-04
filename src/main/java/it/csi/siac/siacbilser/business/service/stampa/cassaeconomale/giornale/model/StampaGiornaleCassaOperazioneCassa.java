/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.OperazioneCassa;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaGiornaleCassaOperazioneCassa extends OperazioneCassa{

	/** per serializzazione */
	private static final long serialVersionUID = -855192847589586238L;
	
	private BigDecimal importoOpEntrataCC = BigDecimal.ZERO;
	private BigDecimal importoOpUscitaCC = BigDecimal.ZERO;
	private BigDecimal importoOpEntrataCO = BigDecimal.ZERO;
	private BigDecimal importoOpUscitaCO = BigDecimal.ZERO;
	
	private String descrizione;
	


	/**
	 * @return the importoOpEntrataCC
	 */
	public BigDecimal getImportoOpEntrataCC() {
		return importoOpEntrataCC;
	}

	/**
	 * @param importoOpEntrataCC the importoOpEntrataCC to set
	 */
	public void setImportoOpEntrataCC(BigDecimal importoOpEntrataCC) {
		this.importoOpEntrataCC = importoOpEntrataCC;
	}

	/**
	 * @return the importoOpUscitaCC
	 */
	public BigDecimal getImportoOpUscitaCC() {
		return importoOpUscitaCC;
	}

	/**
	 * @param importoOpUscitaCC the importoOpUscitaCC to set
	 */
	public void setImportoOpUscitaCC(BigDecimal importoOpUscitaCC) {
		this.importoOpUscitaCC = importoOpUscitaCC;
	}

	/**
	 * @return the importoOpEntrataCO
	 */
	public BigDecimal getImportoOpEntrataCO() {
		return importoOpEntrataCO;
	}

	/**
	 * @param importoOpEntrataCO the importoOpEntrataCO to set
	 */
	public void setImportoOpEntrataCO(BigDecimal importoOpEntrataCO) {
		this.importoOpEntrataCO = importoOpEntrataCO;
	}

	/**
	 * @return the importoOpUscitaCO
	 */
	public BigDecimal getImportoOpUscitaCO() {
		return importoOpUscitaCO;
	}

	/**
	 * @param importoOpUscitaCO the importoOpUscitaCO to set
	 */
	public void setImportoOpUscitaCO(BigDecimal importoOpUscitaCO) {
		this.importoOpUscitaCO = importoOpUscitaCO;
	}

	/**
	 * @return the descrizione
	 */
	public final String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public final void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


}
