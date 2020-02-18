/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_iva_aliquota database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_aliquota")
public class SiacTIvaAliquotaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivaaliquota_id")
	private Integer ivaaliquotaId;

	@Column(name="ivaaliquota_code")
	private String ivaaliquotaCode;

	@Column(name="ivaaliquota_desc")
	private String ivaaliquotaDesc;

	@Column(name="ivaaliquota_perc")
	private BigDecimal ivaaliquotaPerc;

	@Column(name="ivaaliquota_perc_indetr")
	private BigDecimal ivaaliquotaPercIndetr;

	//bi-directional many-to-one association to SiacDIvaOperazioneTipoFin
	@ManyToOne
	@JoinColumn(name="ivaop_tipo_id")
	private SiacDIvaOperazioneTipoFin siacDIvaOperazioneTipo;

	//bi-directional many-to-one association to SiacTIvamovFin
	@OneToMany(mappedBy="siacTIvaAliquota")
	private List<SiacTIvamovFin> siacTIvamovs;

	public SiacTIvaAliquotaFin() {
	}

	public Integer getIvaaliquotaId() {
		return this.ivaaliquotaId;
	}

	public void setIvaaliquotaId(Integer ivaaliquotaId) {
		this.ivaaliquotaId = ivaaliquotaId;
	}

	public String getIvaaliquotaCode() {
		return this.ivaaliquotaCode;
	}

	public void setIvaaliquotaCode(String ivaaliquotaCode) {
		this.ivaaliquotaCode = ivaaliquotaCode;
	}

	public String getIvaaliquotaDesc() {
		return this.ivaaliquotaDesc;
	}

	public void setIvaaliquotaDesc(String ivaaliquotaDesc) {
		this.ivaaliquotaDesc = ivaaliquotaDesc;
	}

	public BigDecimal getIvaaliquotaPerc() {
		return this.ivaaliquotaPerc;
	}

	public void setIvaaliquotaPerc(BigDecimal ivaaliquotaPerc) {
		this.ivaaliquotaPerc = ivaaliquotaPerc;
	}

	public BigDecimal getIvaaliquotaPercIndetr() {
		return this.ivaaliquotaPercIndetr;
	}

	public void setIvaaliquotaPercIndetr(BigDecimal ivaaliquotaPercIndetr) {
		this.ivaaliquotaPercIndetr = ivaaliquotaPercIndetr;
	}

	public SiacDIvaOperazioneTipoFin getSiacDIvaOperazioneTipo() {
		return this.siacDIvaOperazioneTipo;
	}

	public void setSiacDIvaOperazioneTipo(SiacDIvaOperazioneTipoFin siacDIvaOperazioneTipo) {
		this.siacDIvaOperazioneTipo = siacDIvaOperazioneTipo;
	}

	public List<SiacTIvamovFin> getSiacTIvamovs() {
		return this.siacTIvamovs;
	}

	public void setSiacTIvamovs(List<SiacTIvamovFin> siacTIvamovs) {
		this.siacTIvamovs = siacTIvamovs;
	}

	public SiacTIvamovFin addSiacTIvamov(SiacTIvamovFin siacTIvamov) {
		getSiacTIvamovs().add(siacTIvamov);
		siacTIvamov.setSiacTIvaAliquota(this);

		return siacTIvamov;
	}

	public SiacTIvamovFin removeSiacTIvamov(SiacTIvamovFin siacTIvamov) {
		getSiacTIvamovs().remove(siacTIvamov);
		siacTIvamov.setSiacTIvaAliquota(null);

		return siacTIvamov;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivaaliquotaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivaaliquotaId = uid;
	}

}