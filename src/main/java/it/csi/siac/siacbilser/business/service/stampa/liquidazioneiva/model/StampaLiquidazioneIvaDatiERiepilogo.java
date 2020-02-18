/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaLiquidazioneIvaDatiERiepilogo {
	
	@XmlElements({
		@XmlElement(name="acquisto", type=StampaLiquidazioneIvaDatoIvaAcquisti.class),
		@XmlElement(name="vendita", type=StampaLiquidazioneIvaDatoIvaVendite.class)
	})
	
	@XmlElementWrapper(name = "datiIva")
	private List<StampaLiquidazioneIvaDatoIva> listaDatiIva = new ArrayList<StampaLiquidazioneIvaDatoIva>();
	
	private StampaLiquidazioneIvaRiepilogo riepilogo;

	/**
	 * @return the listaDatiIva
	 */
	@XmlTransient
	public List<StampaLiquidazioneIvaDatoIva> getListaDatiIva() {
		return listaDatiIva;
	}

	/**
	 * @param listaDatiIva the listaDatiIva to set
	 */
	public void setListaDatiIva(List<StampaLiquidazioneIvaDatoIva> listaRighe) {
		this.listaDatiIva = listaRighe;
	}
	
	/**
	 * @return the stampaLiquidazioneIvaRiepilogo
	 */
	public StampaLiquidazioneIvaRiepilogo getRiepilogo() {
		return riepilogo;
	}
	/**
	 * @param riepilogo the stampaLiquidazioneIvaRiepilogo to set
	 */
	public void setRiepilogo(
			StampaLiquidazioneIvaRiepilogo stampaLiquidazioneIvaSezione2) {
		this.riepilogo = stampaLiquidazioneIvaSezione2;
	}
	
}
