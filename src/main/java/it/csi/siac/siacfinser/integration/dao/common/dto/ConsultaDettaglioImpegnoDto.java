/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;


import java.io.Serializable;
import java.math.BigDecimal;

public class ConsultaDettaglioImpegnoDto extends ConsultaDettaglioMovimentoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal totImpLiq;
	private int numeroLiq;
	private BigDecimal totImpSubdoc;
	private int numeroImpDoc;
	private BigDecimal totImpLiqSudoc;
	private int numeroDocLiq;
	private BigDecimal totDocNonLiq;
	private int numeroDocNonLiq;
	private BigDecimal totImpPredoc;
	private int numeroImpPredoc;
	private BigDecimal totImpCartac;
	private int numeroCartac;
	private BigDecimal totImpCartacSubdoc;
	private int numeroCartacSubdoc;
	private BigDecimal totCarteNonReg;
	private int numeroCarteNonReg; 
	private BigDecimal totModProv;
	private BigDecimal totImpCecNoGiust;
	private BigDecimal totImp2NoGiust;
	private BigDecimal totImp2GiustIntegrato;
	private BigDecimal totImp2GiustRestituito;
	private BigDecimal totImpCecFattura;
	private BigDecimal totImpCecPafFatt;
	private BigDecimal totCec;
	
	public BigDecimal getTotImpLiq() {
		return totImpLiq;
	}
	public void setTotImpLiq(BigDecimal totImpLiq) {
		this.totImpLiq = totImpLiq;
	}
	public int getNumeroLiq() {
		return numeroLiq;
	}
	public void setNumeroLiq(int numeroLiq) {
		this.numeroLiq = numeroLiq;
	}
	public BigDecimal getTotImpSubdoc() {
		return totImpSubdoc;
	}
	public void setTotImpSubdoc(BigDecimal totImpSubdoc) {
		this.totImpSubdoc = totImpSubdoc;
	}
	public int getNumeroImpDoc() {
		return numeroImpDoc;
	}
	public void setNumeroImpDoc(int numeroImpDoc) {
		this.numeroImpDoc = numeroImpDoc;
	}
	public BigDecimal getTotImpLiqSudoc() {
		return totImpLiqSudoc;
	}
	public void setTotImpLiqSudoc(BigDecimal totImpLiqSudoc) {
		this.totImpLiqSudoc = totImpLiqSudoc;
	}
	public int getNumeroDocLiq() {
		return numeroDocLiq;
	}
	public void setNumeroDocLiq(int numeroDocLiq) {
		this.numeroDocLiq = numeroDocLiq;
	}
	public BigDecimal getTotDocNonLiq() {
		return totDocNonLiq;
	}
	public void setTotDocNonLiq(BigDecimal totDocNonLiq) {
		this.totDocNonLiq = totDocNonLiq;
	}
	public int getNumeroDocNonLiq() {
		return numeroDocNonLiq;
	}
	public void setNumeroDocNonLiq(int numeroDocNonLiq) {
		this.numeroDocNonLiq = numeroDocNonLiq;
	}
	public BigDecimal getTotImpPredoc() {
		return totImpPredoc;
	}
	public void setTotImpPredoc(BigDecimal totImpPredoc) {
		this.totImpPredoc = totImpPredoc;
	}
	public int getNumeroImpPredoc() {
		return numeroImpPredoc;
	}
	public void setNumeroImpPredoc(int numeroImpPredoc) {
		this.numeroImpPredoc = numeroImpPredoc;
	}
	public BigDecimal getTotImpCartac() {
		return totImpCartac;
	}
	public void setTotImpCartac(BigDecimal totImpCartac) {
		this.totImpCartac = totImpCartac;
	}
	public int getNumeroCartac() {
		return numeroCartac;
	}
	public void setNumeroCartac(int numeroCartac) {
		this.numeroCartac = numeroCartac;
	}
	public BigDecimal getTotImpCartacSubdoc() {
		return totImpCartacSubdoc;
	}
	public void setTotImpCartacSubdoc(BigDecimal totImpCartacSubdoc) {
		this.totImpCartacSubdoc = totImpCartacSubdoc;
	}
	public int getNumeroCartacSubdoc() {
		return numeroCartacSubdoc;
	}
	public void setNumeroCartacSubdoc(int numeroCartacSubdoc) {
		this.numeroCartacSubdoc = numeroCartacSubdoc;
	}
	public BigDecimal getTotCarteNonReg() {
		return totCarteNonReg;
	}
	public void setTotCarteNonReg(BigDecimal totCarteNonReg) {
		this.totCarteNonReg = totCarteNonReg;
	}
	public int getNumeroCarteNonReg() {
		return numeroCarteNonReg;
	}
	public void setNumeroCarteNonReg(int numeroCarteNonReg) {
		this.numeroCarteNonReg = numeroCarteNonReg;
	}
	public BigDecimal getTotModProv() {
		return totModProv;
	}
	public void setTotModProv(BigDecimal totModProv) {
		this.totModProv = totModProv;
	}
	public BigDecimal getTotImpCecNoGiust() {
		return totImpCecNoGiust;
	}
	public void setTotImpCecNoGiust(BigDecimal totImpCecNoGiust) {
		this.totImpCecNoGiust = totImpCecNoGiust;
	}
	public BigDecimal getTotImp2NoGiust() {
		return totImp2NoGiust;
	}
	public void setTotImp2NoGiust(BigDecimal totImp2NoGiust) {
		this.totImp2NoGiust = totImp2NoGiust;
	}
	public BigDecimal getTotImp2GiustIntegrato() {
		return totImp2GiustIntegrato;
	}
	public void setTotImp2GiustIntegrato(BigDecimal totImp2GiustIntegrato) {
		this.totImp2GiustIntegrato = totImp2GiustIntegrato;
	}
	public BigDecimal getTotImp2GiustRestituito() {
		return totImp2GiustRestituito;
	}
	public void setTotImp2GiustRestituito(BigDecimal totImp2GiustRestituito) {
		this.totImp2GiustRestituito = totImp2GiustRestituito;
	}
	public BigDecimal getTotImpCecFattura() {
		return totImpCecFattura;
	}
	public void setTotImpCecFattura(BigDecimal totImpCecFattura) {
		this.totImpCecFattura = totImpCecFattura;
	}
	public BigDecimal getTotImpCecPafFatt() {
		return totImpCecPafFatt;
	}
	public void setTotImpCecPafFatt(BigDecimal totImpCecPafFatt) {
		this.totImpCecPafFatt = totImpCecPafFatt;
	}
	public BigDecimal getTotCec() {
		return totCec;
	}
	public void setTotCec(BigDecimal totCec) {
		this.totCec = totCec;
	}	 
}
