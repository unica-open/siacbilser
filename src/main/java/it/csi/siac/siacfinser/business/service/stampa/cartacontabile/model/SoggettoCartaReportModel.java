/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class SoggettoCartaReportModel {
	
	private IndirizzoSoggetto indirizzoPrincipale;
	
	private String codiceSoggetto;
	private String denominazione;
	private String codiceFiscale;
	private String partitaIva;

	public IndirizzoSoggetto getIndirizzoPrincipale() {
		return indirizzoPrincipale;
	}

	public void setIndirizzoPrincipale(IndirizzoSoggetto indirizzoPrincipale) {
		this.indirizzoPrincipale = indirizzoPrincipale;
	}

	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	
}
