/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.progetto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
	
public class ProgettiAssociatiMutuoExcelRow extends BaseMutuoExcelRow {
	
	private String codice;
	private String ambito;
	private String provvedimento;
	private BigDecimal valoreIniziale;
	private BigDecimal valoreAttuale;
	
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getAmbito() {
		return ambito;
	}
	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	public String getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
	}
	public BigDecimal getValoreIniziale() {
		return valoreIniziale;
	}
	public void setValoreIniziale(BigDecimal valoreIniziale) {
		this.valoreIniziale = valoreIniziale;
	}
	public BigDecimal getValoreAttuale() {
		return valoreAttuale;
	}
	public void setValoreAttuale(BigDecimal valoreAttuale) {
		this.valoreAttuale = valoreAttuale;
	}
	


		
}
