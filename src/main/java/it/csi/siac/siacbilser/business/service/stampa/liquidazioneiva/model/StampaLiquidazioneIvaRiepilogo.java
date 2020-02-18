/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model;

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
public class StampaLiquidazioneIvaRiepilogo {
	
	@XmlElements({
		@XmlElement(name="riepilogoIva", type=StampaLiquidazioneIvaRiepilogoIva.class)
	})
	
	@XmlElementWrapper(name = "riepiloghiIva")
	private List<DatiIva> listaRiepilogoIva = new ArrayList<DatiIva>();
	
	private BigDecimal totaleImponibile;
	private BigDecimal totaleImposta;
	
	/**
	 * @return the listaRiepilogoIva
	 */
	@XmlTransient
	public List<DatiIva> getListaRiepilogoIva() {
		return listaRiepilogoIva;
	}
	/**
	 * @param listaRiepilogoIva the listaRiepilogoIva to set
	 */
	public void setListaRiepilogoIva(List<DatiIva> listaRighe) {
		this.listaRiepilogoIva = listaRighe;
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
}
