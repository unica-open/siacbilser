/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;


//import antlr.StringUtils;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaAllegatoAttoImpegno {
	
	private Impegno impegno;
	private SubImpegno subImpegno;
	private BigDecimal importo = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;	

	// SIAC-4865
	private String cig = "";
	private String cup = "";
	// SIAC-5272
	private boolean hasQuoteDaNonEsporre = false;
	// SIAC-6269
	private String siopeAssenzaMotivazioneDescr = "";
	
	/**
	 * @return the cig
	 */
	public String getCig() {
		return cig;
	}

	/**
	 * @param cig the cig to set
	 */
	public void setCig(String cig) {
		this.cig = cig;
	}

	/**
	 * @return the cup
	 */
	public String getCup() {
		return cup;
	}

	/**
	 * @param cup the cup to set
	 */
	public void setCup(String cup) {
		this.cup = cup;
	}
	
	/**
	 * @return the siopeAssenzaMotivazioneDescr
	 */
	public String getSiopeAssenzaMotivazioneDescr() {
		return siopeAssenzaMotivazioneDescr;
	}

	/**
	 * @param siopeAssenzaMotivazioneDescr the siopeAssenzaMotivazioneDescr to set
	 */
	public void setSiopeAssenzaMotivazioneDescr(String siopeAssenzaMotivazioneDescr) {
		this.siopeAssenzaMotivazioneDescr = siopeAssenzaMotivazioneDescr;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}
	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo != null ? importo : BigDecimal.ZERO;
	}
	
	/**
	 * Adds to importo.
	 * 
	 * @param augend the augend
	 */
	public void addImporto(BigDecimal augend) {
		if(augend == null) {
			return;
		}
		this.importo = this.importo.add(augend);
	}
	
	/**
	 * @return the totaleImportoDaDedurre
	 */
	public BigDecimal getTotaleImportoDaDedurre() {
		return totaleImportoDaDedurre;
	}

	/**
	 * @param totaleImportoDaDedurre the totaleImportoDaDedurre to set
	 */
	public void setTotaleImportoDaDedurre(BigDecimal totaleImportoDaDedurre) {
		this.totaleImportoDaDedurre = totaleImportoDaDedurre != null? totaleImportoDaDedurre : BigDecimal.ZERO;
	}
	
	/**
	 * @return the hasQuoteDaNonEsporre
	 */
	public boolean isHasQuoteDaNonEsporre() {
		return hasQuoteDaNonEsporre;
	}

	/**
	 * @param hasQuoteDaNonEsporre the hasQuoteDaNonEsporre to set
	 */
	public void setHasQuoteDaNonEsporre(boolean hasQuoteDaNonEsporre) {
		this.hasQuoteDaNonEsporre = hasQuoteDaNonEsporre;
	}

	/**
	 * Adds to totaleImportoDaDedurre
	 * 
	 * @param augend the augend
	 */
	public void addTotaleImportoDaDedurre(BigDecimal augend) {
		if(augend == null) {
			return;
		}
		this.totaleImportoDaDedurre = this.totaleImportoDaDedurre.add(augend);
	}
	
}
