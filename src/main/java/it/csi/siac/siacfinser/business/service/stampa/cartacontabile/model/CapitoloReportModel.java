/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class CapitoloReportModel {
	
	private String annoCapitolo;
	private String numeroCapitolo;
	private String numeroArticolo;
	
	private CodificaModel programma;
	private CodificaModel titolo;
	private CodificaModel missione;
	
	public String getNumeroCapitolo() {
		return numeroCapitolo;
	}
	public void setNumeroCapitolo(String numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}
	public String getNumeroArticolo() {
		return numeroArticolo;
	}
	public void setNumeroArticolo(String numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}
	public String getAnnoCapitolo() {
		return annoCapitolo;
	}
	public void setAnnoCapitolo(String annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}
	public CodificaModel getProgramma() {
		return programma;
	}
	public void setProgramma(CodificaModel programma) {
		this.programma = programma;
	}
	public CodificaModel getTitolo() {
		return titolo;
	}
	public void setTitolo(CodificaModel titolo) {
		this.titolo = titolo;
	}
	public CodificaModel getMissione() {
		return missione;
	}
	public void setMissione(CodificaModel missione) {
		this.missione = missione;
	}
	
}
