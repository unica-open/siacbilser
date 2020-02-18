/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistroIvaReportModel {
	
	private StampaRegistroIvaIntestazione intestazione;
	private StampaRegistroIvaTitolo titolo;
	private StampaRegistroIvaDatiIva datiIva;

	@XmlElementWrapper(name = "listaRiepilogo")
	@XmlElement(name = "riepilogo")
	private List<StampaRegistroIvaRiepilogo> listaRiepilogo = new ArrayList<StampaRegistroIvaRiepilogo>();

	/**
	 * @return the intestazione
	 */
	public StampaRegistroIvaIntestazione getIntestazione() {
		return intestazione;
	}
	/**
	 * @param intestazione the intestazione to set
	 */
	public void setIntestazione(StampaRegistroIvaIntestazione intestazione) {
		this.intestazione = intestazione;
	}
	/**
	 * @return the titolo
	 */
	public StampaRegistroIvaTitolo getTitolo() {
		return titolo;
	}
	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(StampaRegistroIvaTitolo titolo) {
		this.titolo = titolo;
	}
	/**
	 * @return the datiIva
	 */
	public StampaRegistroIvaDatiIva getDatiIva() {
		return datiIva;
	}
	/**
	 * @param datiIva the datiIva to set
	 */
	public void setDatiIva(StampaRegistroIvaDatiIva sezione1) {
		this.datiIva = sezione1;
	}

	/**
	 * @return the listaRiepilogo
	 */
	@XmlTransient
	public List<StampaRegistroIvaRiepilogo> getListaRiepilogo() {
		return listaRiepilogo;
	}
	/**
	 * @param listaRiepilogo the listaRiepilogo to set
	 */
	public void setListaRiepilogo(List<StampaRegistroIvaRiepilogo> listaRiepilogo) {
		this.listaRiepilogo = listaRiepilogo != null ? listaRiepilogo : new ArrayList<StampaRegistroIvaRiepilogo>();
	}
	
	

}
