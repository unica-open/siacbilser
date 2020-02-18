/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRegistroIvaTitolo {

	private GruppoAttivitaIva gruppoAttivitaIva;
	private RegistroIva registroIva;
	private Periodo periodo;
	private Integer annoContabile;
	
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
	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}
	/**
	 * @param registroIva the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}
	/**
	 * @return the periodo
	 */
	@XmlJavaTypeAdapter(Periodo.PeriodoAdapter.class)
	public Periodo getPeriodo() {
		return periodo;
	}
	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	/**
	 * @return the annoContabile
	 */
	public Integer getAnnoContabile() {
		return annoContabile;
	}
	/**
	 * @param annoContabile the annoContabile to set
	 */
	public void setAnnoContabile(Integer annoContabile) {
		this.annoContabile = annoContabile;
	}

}
