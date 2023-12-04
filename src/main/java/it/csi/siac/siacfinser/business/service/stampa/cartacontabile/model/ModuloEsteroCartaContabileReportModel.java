/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class ModuloEsteroCartaContabileReportModel {
	
	private String tipologiaPagamento;
	private String istruzioni;
	private String diversoTitolare;
	
	private String denominazioneSoggetto;
	private String indirizzoSoggetto;
	private String ibanOConto;
	private String bic;
	
	
	private String causale;
	
	private String commissioniEstero;
	
	
	public String getTipologiaPagamento() {
		return tipologiaPagamento;
	}
	public void setTipologiaPagamento(String tipologiaPagamento) {
		this.tipologiaPagamento = tipologiaPagamento;
	}
	public String getIstruzioni() {
		return istruzioni;
	}
	public void setIstruzioni(String istruzioni) {
		this.istruzioni = istruzioni;
	}
	public String getDiversoTitolare() {
		return diversoTitolare;
	}
	public void setDiversoTitolare(String diversoTitolare) {
		this.diversoTitolare = diversoTitolare;
	}
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}
	public String getIndirizzoSoggetto() {
		return indirizzoSoggetto;
	}
	public void setIndirizzoSoggetto(String indirizzoSoggetto) {
		this.indirizzoSoggetto = indirizzoSoggetto;
	}
	public String getIbanOConto() {
		return ibanOConto;
	}
	public void setIbanOConto(String ibanOConto) {
		this.ibanOConto = ibanOConto;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getCommissioniEstero() {
		return commissioniEstero;
	}
	public void setCommissioniEstero(String commissioniEstero) {
		this.commissioniEstero = commissioniEstero;
	}
	
}
