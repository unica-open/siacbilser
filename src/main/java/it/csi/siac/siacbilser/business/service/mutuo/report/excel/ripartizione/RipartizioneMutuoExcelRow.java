/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.ripartizione;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
	
public class RipartizioneMutuoExcelRow extends BaseMutuoExcelRow {
	
	private String ripartizioneTipo;
	private String capitolo;
	private BigDecimal importo;
	private BigDecimal percentuale;
	
	
	public String getRipartizioneTipo() {
		return ripartizioneTipo;
	}

	public void setRipartizioneTipo(String ripartizioneTipo) {
		this.ripartizioneTipo = ripartizioneTipo;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public BigDecimal getPercentuale() {
		return percentuale;
	}

	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}


	
		
}
