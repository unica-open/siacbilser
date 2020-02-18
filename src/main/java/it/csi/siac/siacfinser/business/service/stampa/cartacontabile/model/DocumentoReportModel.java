/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class DocumentoReportModel {
	
	private CodificaModel tipo;
	private String anno;
	private String numero;
	
	public CodificaModel getTipo() {
		return tipo;
	}
	public void setTipo(CodificaModel tipo) {
		this.tipo = tipo;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
}
