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
 * The persistent class for the siac_d_accredito_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_tipo")
public class SiacDAccreditoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="accredito_tipo_id")
	private Integer accreditoTipoId;

	@Column(name="accredito_priorita")
	private BigDecimal accreditoPriorita;

	@Column(name="accredito_tipo_code")
	private String accreditoTipoCode;

	@Column(name="accredito_tipo_desc")
	private String accreditoTipoDesc;

	//bi-directional many-to-one association to SiacDAccreditoGruppoFin
	@ManyToOne
	@JoinColumn(name="accredito_gruppo_id")
	private SiacDAccreditoGruppoFin siacDAccreditoGruppo;

	//bi-directional many-to-one association to SiacTModpagFin
	@OneToMany(mappedBy="siacDAccreditoTipo")
	private List<SiacTModpagFin> siacTModpags;

	//bi-directional many-to-one association to SiacTModpagModFin
	@OneToMany(mappedBy="siacDAccreditoTipo")
	private List<SiacTModpagModFin> siacTModpagMods;

	public SiacDAccreditoTipoFin() {
	}

	public Integer getAccreditoTipoId() {
		return this.accreditoTipoId;
	}

	public void setAccreditoTipoId(Integer accreditoTipoId) {
		this.accreditoTipoId = accreditoTipoId;
	}

	public BigDecimal getAccreditoPriorita() {
		return this.accreditoPriorita;
	}

	public void setAccreditoPriorita(BigDecimal accreditoPriorita) {
		this.accreditoPriorita = accreditoPriorita;
	}

	public String getAccreditoTipoCode() {
		return this.accreditoTipoCode;
	}

	public void setAccreditoTipoCode(String accreditoTipoCode) {
		this.accreditoTipoCode = accreditoTipoCode;
	}

	public String getAccreditoTipoDesc() {
		return this.accreditoTipoDesc;
	}

	public void setAccreditoTipoDesc(String accreditoTipoDesc) {
		this.accreditoTipoDesc = accreditoTipoDesc;
	}

	public SiacDAccreditoGruppoFin getSiacDAccreditoGruppo() {
		return this.siacDAccreditoGruppo;
	}

	public void setSiacDAccreditoGruppo(SiacDAccreditoGruppoFin siacDAccreditoGruppo) {
		this.siacDAccreditoGruppo = siacDAccreditoGruppo;
	}

	public List<SiacTModpagFin> getSiacTModpags() {
		return this.siacTModpags;
	}

	public void setSiacTModpags(List<SiacTModpagFin> siacTModpags) {
		this.siacTModpags = siacTModpags;
	}

	public SiacTModpagFin addSiacTModpag(SiacTModpagFin siacTModpag) {
		getSiacTModpags().add(siacTModpag);
		siacTModpag.setSiacDAccreditoTipo(this);

		return siacTModpag;
	}

	public SiacTModpagFin removeSiacTModpag(SiacTModpagFin siacTModpag) {
		getSiacTModpags().remove(siacTModpag);
		siacTModpag.setSiacDAccreditoTipo(null);

		return siacTModpag;
	}

	public List<SiacTModpagModFin> getSiacTModpagMods() {
		return this.siacTModpagMods;
	}

	public void setSiacTModpagMods(List<SiacTModpagModFin> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	public SiacTModpagModFin addSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().add(siacTModpagMod);
		siacTModpagMod.setSiacDAccreditoTipo(this);

		return siacTModpagMod;
	}

	public SiacTModpagModFin removeSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().remove(siacTModpagMod);
		siacTModpagMod.setSiacDAccreditoTipo(null);

		return siacTModpagMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.accreditoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.accreditoTipoId = uid;
	}
}