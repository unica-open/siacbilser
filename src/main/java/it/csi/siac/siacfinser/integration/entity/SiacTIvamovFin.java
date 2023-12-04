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
 * The persistent class for the siac_t_ivamov database table.
 * 
 */
@Entity
@Table(name="siac_t_ivamov")
public class SiacTIvamovFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivamov_id")
	private Integer ivamovId;

	@Column(name="ivamov_imponibile")
	private BigDecimal ivamovImponibile;

	@Column(name="ivamov_imposta")
	private BigDecimal ivamovImposta;

	@Column(name="ivamov_totale")
	private BigDecimal ivamovTotale;

	//bi-directional many-to-one association to SiacRIvamovFin
	@OneToMany(mappedBy="siacTIvamov")
	private List<SiacRIvamovFin> siacRIvamovs;

	//bi-directional many-to-one association to SiacTIvaAliquotaFin
	@ManyToOne
	@JoinColumn(name="ivaaliquota_id")
	private SiacTIvaAliquotaFin siacTIvaAliquota;

	public SiacTIvamovFin() {
	}

	public Integer getIvamovId() {
		return this.ivamovId;
	}

	public void setIvamovId(Integer ivamovId) {
		this.ivamovId = ivamovId;
	}

	public BigDecimal getIvamovImponibile() {
		return this.ivamovImponibile;
	}

	public void setIvamovImponibile(BigDecimal ivamovImponibile) {
		this.ivamovImponibile = ivamovImponibile;
	}

	public BigDecimal getIvamovImposta() {
		return this.ivamovImposta;
	}

	public void setIvamovImposta(BigDecimal ivamovImposta) {
		this.ivamovImposta = ivamovImposta;
	}

	public BigDecimal getIvamovTotale() {
		return this.ivamovTotale;
	}

	public void setIvamovTotale(BigDecimal ivamovTotale) {
		this.ivamovTotale = ivamovTotale;
	}

	public List<SiacRIvamovFin> getSiacRIvamovs() {
		return this.siacRIvamovs;
	}

	public void setSiacRIvamovs(List<SiacRIvamovFin> siacRIvamovs) {
		this.siacRIvamovs = siacRIvamovs;
	}

	public SiacRIvamovFin addSiacRIvamov(SiacRIvamovFin siacRIvamov) {
		getSiacRIvamovs().add(siacRIvamov);
		siacRIvamov.setSiacTIvamov(this);

		return siacRIvamov;
	}

	public SiacRIvamovFin removeSiacRIvamov(SiacRIvamovFin siacRIvamov) {
		getSiacRIvamovs().remove(siacRIvamov);
		siacRIvamov.setSiacTIvamov(null);

		return siacRIvamov;
	}

	public SiacTIvaAliquotaFin getSiacTIvaAliquota() {
		return this.siacTIvaAliquota;
	}

	public void setSiacTIvaAliquota(SiacTIvaAliquotaFin siacTIvaAliquota) {
		this.siacTIvaAliquota = siacTIvaAliquota;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivamovId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivamovId = uid;
	}

}