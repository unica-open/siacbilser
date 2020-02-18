/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class SubDocumentoReportModel {
	
	private DocumentoReportModel documento;
	
	private String numero;
	private String cig;
	private CodificaModel motivazioneAssenzaCig;
	private String cup;

	public DocumentoReportModel getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoReportModel documento) {
		this.documento = documento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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
	
}
