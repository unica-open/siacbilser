/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow implements BaseExcelRow {

	private Integer versione;
	private String capitolo;
	private String articolo;
	private String ueb;
	private String titolo;
	private String tipologia;
	private String categoria;
	private String strutturaAmministrativoContabile;

	public Integer getVersione() {
		return versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getArticolo() {
		return articolo;
	}

	public void setArticolo(String articolo) {
		this.articolo = articolo;
	}

	public String getUeb() {
		return ueb;
	}

	public void setUeb(String ueb) {
		this.ueb = ueb;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	public void setStrutturaAmministrativoContabile(String strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

}
