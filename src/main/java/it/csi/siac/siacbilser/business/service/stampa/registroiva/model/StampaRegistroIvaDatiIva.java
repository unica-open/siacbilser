/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistroIvaDatiIva {
	
	@XmlElements({
		@XmlElement(name="acquisto", type=StampaRegistoIvaDatoIvaAcquisti.class),
		@XmlElement(name="vendita", type=StampaRegistoIvaDatoIvaVendite.class)
	})
	@XmlElementWrapper(name = "acquistiVendite")
	private List<DatiIva> listaDatiIva = new ArrayList<DatiIva>();
	
	private BigDecimal totaleImponibile;
	private BigDecimal totaleImposta;
	private BigDecimal totaleTotale;
	
	/**
	 * @return the listaDatiIva
	 */
	@XmlTransient
	public List<DatiIva> getListaDatiIva() {
		return listaDatiIva;
	}

	/**
	 * @param listaDatiIva the listaDatiIva to set
	 */
	public void setListaDatiIva(List<DatiIva> listaRighe) {
		this.listaDatiIva = listaRighe;
	}

	/**
	 * @return the totaleImponibile
	 */
	public BigDecimal getTotaleImponibile() {
		return totaleImponibile;
	}

	/**
	 * @param totaleImponibile the totaleImponibile to set
	 */
	public void setTotaleImponibile(BigDecimal totaleImponibile) {
		this.totaleImponibile = totaleImponibile;
	}

	/**
	 * @return the totaleImposta
	 */
	public BigDecimal getTotaleImposta() {
		return totaleImposta;
	}

	/**
	 * @param totaleImposta the totaleImposta to set
	 */
	public void setTotaleImposta(BigDecimal totaleImposta) {
		this.totaleImposta = totaleImposta;
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotaleTotale() {
		return totaleTotale;
	}

	/**
	 * @param totale the totale to set
	 */
	public void setTotaleTotale(BigDecimal totale) {
		this.totaleTotale = totale;
	}
	
}
