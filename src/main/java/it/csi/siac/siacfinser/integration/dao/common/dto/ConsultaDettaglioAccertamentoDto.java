/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsultaDettaglioAccertamentoDto extends ConsultaDettaglioMovimentoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal totImpOrd;
	private int nOrd;
	
	private BigDecimal totImpSubdoc;
	private int nImpDoc;
	
	private BigDecimal totImpOrdSudoc;
	private int nDocOrd;
	
	private BigDecimal totDocNonInc;
	private int nDocNonInc;
	
	private BigDecimal totImpPredoc;
	private int nImpPredoc;
	
	private BigDecimal totModProv;

	public BigDecimal getTotImpOrd() {
		return totImpOrd;
	}

	public void setTotImpOrd(BigDecimal totImpOrd) {
		this.totImpOrd = totImpOrd;
	}

	public BigDecimal getTotImpSubdoc() {
		return totImpSubdoc;
	}

	public void setTotImpSubdoc(BigDecimal totImpSubdoc) {
		this.totImpSubdoc = totImpSubdoc;
	}

	public int getnImpDoc() {
		return nImpDoc;
	}

	public void setnImpDoc(int nImpDoc) {
		this.nImpDoc = nImpDoc;
	}

	public BigDecimal getTotImpOrdSudoc() {
		return totImpOrdSudoc;
	}

	public void setTotImpOrdSudoc(BigDecimal totImpOrdSudoc) {
		this.totImpOrdSudoc = totImpOrdSudoc;
	}

	public int getnDocOrd() {
		return nDocOrd;
	}

	public void setnDocOrd(int nDocOrd) {
		this.nDocOrd = nDocOrd;
	}

	public BigDecimal getTotDocNonInc() {
		return totDocNonInc;
	}

	public void setTotDocNonInc(BigDecimal totDocNonInc) {
		this.totDocNonInc = totDocNonInc;
	}

	public int getnDocNonInc() {
		return nDocNonInc;
	}

	public void setnDocNonInc(int nDocNonInc) {
		this.nDocNonInc = nDocNonInc;
	}

	public BigDecimal getTotImpPredoc() {
		return totImpPredoc;
	}

	public void setTotImpPredoc(BigDecimal totImpPredoc) {
		this.totImpPredoc = totImpPredoc;
	}

	public int getnImpPredoc() {
		return nImpPredoc;
	}

	public void setnImpPredoc(int nImpPredoc) {
		this.nImpPredoc = nImpPredoc;
	}

	public BigDecimal getTotModProv() {
		return totModProv;
	}

	public void setTotModProv(BigDecimal totModProv) {
		this.totModProv = totModProv;
	}

	public int getnOrd() {
		return nOrd;
	}

	public void setnOrd(int nOrd) {
		this.nOrd = nOrd;
	}
	
	
}
