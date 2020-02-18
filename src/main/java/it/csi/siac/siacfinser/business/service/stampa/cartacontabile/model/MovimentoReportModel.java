/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class MovimentoReportModel {
	
	private String numero;
	private String anno;
	private String numeroSub;
	private String cig;
	private CodificaModel motivazioneAssenzaCig;
	private String cup;
	
	private String codCofog;
	private String codPdc;
	private String codTransazioneEuropeaSpesa;
	private String codSiope;
	private String codRicorrenteSpesa;
	private String codCapitoloSanitarioSpesa;
	private String codPrgPolReg;
	
	private CapitoloReportModel capitolo;
	
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getNumeroSub() {
		return numeroSub;
	}
	public void setNumeroSub(String numeroSub) {
		this.numeroSub = numeroSub;
	}
	public CapitoloReportModel getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloReportModel capitolo) {
		this.capitolo = capitolo;
	}
	public String getCig() {
		return cig;
	}
	public void setCig(String cig) {
		this.cig = cig;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public CodificaModel getMotivazioneAssenzaCig() {
		return motivazioneAssenzaCig;
	}
	public void setMotivazioneAssenzaCig(CodificaModel motivazioneAssenzaCig) {
		this.motivazioneAssenzaCig = motivazioneAssenzaCig;
	}
	public String getCodPdc() {
		return codPdc;
	}
	public void setCodPdc(String codPdc) {
		this.codPdc = codPdc;
	}
	public String getCodCofog() {
		return codCofog;
	}
	public void setCodCofog(String codCofog) {
		this.codCofog = codCofog;
	}
	public String getCodTransazioneEuropeaSpesa() {
		return codTransazioneEuropeaSpesa;
	}
	public void setCodTransazioneEuropeaSpesa(String codTransazioneEuropeaSpesa) {
		this.codTransazioneEuropeaSpesa = codTransazioneEuropeaSpesa;
	}
	public String getCodSiope() {
		return codSiope;
	}
	public void setCodSiope(String codSiope) {
		this.codSiope = codSiope;
	}
	public String getCodRicorrenteSpesa() {
		return codRicorrenteSpesa;
	}
	public void setCodRicorrenteSpesa(String codRicorrenteSpesa) {
		this.codRicorrenteSpesa = codRicorrenteSpesa;
	}
	public String getCodCapitoloSanitarioSpesa() {
		return codCapitoloSanitarioSpesa;
	}
	public void setCodCapitoloSanitarioSpesa(String codCapitoloSanitarioSpesa) {
		this.codCapitoloSanitarioSpesa = codCapitoloSanitarioSpesa;
	}
	public String getCodPrgPolReg() {
		return codPrgPolReg;
	}
	public void setCodPrgPolReg(String codPrgPolReg) {
		this.codPrgPolReg = codPrgPolReg;
	}
	
}
