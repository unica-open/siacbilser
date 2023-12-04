/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class PreDocumentiCartaReportModel {
	
	
	private List<PreDocumentoCartaReportModel> preDocumentoCarta;

	public List<PreDocumentoCartaReportModel> getPreDocumentoCarta() {
		return preDocumentoCarta;
	}

	public void setPreDocumentoCarta(
			List<PreDocumentoCartaReportModel> preDocumentoCarta) {
		this.preDocumentoCarta = preDocumentoCarta;
	}
	
	
}
