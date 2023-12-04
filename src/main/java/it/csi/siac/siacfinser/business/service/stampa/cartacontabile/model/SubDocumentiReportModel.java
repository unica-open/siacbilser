/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class SubDocumentiReportModel {
	
	
	private List<SubDocumentoReportModel> subDocumentoReportModel;

	public List<SubDocumentoReportModel> getSubDocumentoReportModel() {
		return subDocumentoReportModel;
	}

	public void setSubDocumentoReportModel(
			List<SubDocumentoReportModel> subDocumentoReportModel) {
		this.subDocumentoReportModel = subDocumentoReportModel;
	}
	
}
