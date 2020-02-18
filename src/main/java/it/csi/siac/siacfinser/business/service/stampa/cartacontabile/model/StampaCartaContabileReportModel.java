/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.Valuta;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaCartaContabileReportModel {
	
	private Ente ente;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private String listaDiDistribuzione;
	
	private String citta;
	private String data;
	
	private String indicazioneDiRegolamento;
	
	private CartaContabileReportModel cartaContabile;
	
	private Date dataStampa;
	private Valuta valutaDefault;

	
	private String nomeBanca;
	private String cittaBanca;

	/**
	 * @return the dataStampa
	 */
	public Date getDataStampa() {
		return dataStampa;
	}



	/**
	 * @param dataStampa the dataStampa to set
	 */
	public void setDataStampa(Date dataStampa) {
		this.dataStampa = dataStampa;
	}

	/**
	 * @return the valutaDefault
	 */
	public Valuta getValutaDefault() {
		return valutaDefault;
	}

	/**
	 * @param valutaDefault the valutaDefault to set
	 */
	public void setValutaDefault(Valuta valutaDefault) {
		this.valutaDefault = valutaDefault;
	}

	public Ente getEnte() {
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}


	public CartaContabileReportModel getCartaContabile() {
		return cartaContabile;
	}


	public void setCartaContabile(CartaContabileReportModel cartaContabile) {
		this.cartaContabile = cartaContabile;
	}



	public String getNomeBanca() {
		return nomeBanca;
	}



	public void setNomeBanca(String nomeBanca) {
		this.nomeBanca = nomeBanca;
	}



	public String getCittaBanca() {
		return cittaBanca;
	}



	public void setCittaBanca(String cittaBanca) {
		this.cittaBanca = cittaBanca;
	}



	public String getListaDiDistribuzione() {
		return listaDiDistribuzione;
	}



	public void setListaDiDistribuzione(String listaDiDistribuzione) {
		this.listaDiDistribuzione = listaDiDistribuzione;
	}



	public String getCitta() {
		return citta;
	}



	public void setCitta(String citta) {
		this.citta = citta;
	}



	public String getIndicazioneDiRegolamento() {
		return indicazioneDiRegolamento;
	}



	public void setIndicazioneDiRegolamento(String indicazioneDiRegolamento) {
		this.indicazioneDiRegolamento = indicazioneDiRegolamento;
	}



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}
	
	

}
