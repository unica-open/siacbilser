/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_d_ruolo_op")
public class SiacDRuoloOpFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ruolo_op_id")
	private Integer ruoloOpId;

	@Column(name="ruolo_op_code")
	private String ruoloOpCode;

	@Column(name="ruolo_op_desc")
	private String ruoloOpDesc;

	//bi-directional many-to-one association to SiacRGruppoRuoloOpFin
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacRRuoloOpAzioneFin
	@OneToMany(mappedBy="siacDRuoloOp")
	private Set<SiacRRuoloOpAzioneFin> siacRRuoloOpAziones;

	//bi-directional many-to-one association to SiacRAccountRuoloOpFin
	@OneToMany(mappedBy="siacDRuoloOp")
	private List<SiacRAccountRuoloOpFin> siacRAccountRuoloOps;

	public SiacDRuoloOpFin() {
	}

	public Integer getRuoloOpId() {
		return this.ruoloOpId;
	}

	public void setRuoloOpId(Integer ruoloOpId) {
		this.ruoloOpId = ruoloOpId;
	}

	public String getRuoloOpCode() {
		return this.ruoloOpCode;
	}

	public void setRuoloOpCode(String ruoloOpCode) {
		this.ruoloOpCode = ruoloOpCode;
	}

	public String getRuoloOpDesc() {
		return this.ruoloOpDesc;
	}

	public void setRuoloOpDesc(String ruoloOpDesc) {
		this.ruoloOpDesc = ruoloOpDesc;
	}

	public List<SiacRGruppoRuoloOpFin> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	public SiacRGruppoRuoloOpFin addSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacDRuoloOp(this);

		return siacRGruppoRuoloOp;
	}

	public SiacRGruppoRuoloOpFin removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacDRuoloOp(null);

		return siacRGruppoRuoloOp;
	}

	public Set<SiacRRuoloOpAzioneFin> getSiacRRuoloOpAziones() {
		return this.siacRRuoloOpAziones;
	}

	public void setSiacRRuoloOpAziones(Set<SiacRRuoloOpAzioneFin> siacRRuoloOpAziones) {
		this.siacRRuoloOpAziones = siacRRuoloOpAziones;
	}

	public SiacRRuoloOpAzioneFin addSiacRRuoloOpAzione(SiacRRuoloOpAzioneFin siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().add(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacDRuoloOp(this);

		return siacRRuoloOpAzione;
	}

	public SiacRRuoloOpAzioneFin removeSiacRRuoloOpAzione(SiacRRuoloOpAzioneFin siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().remove(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacDRuoloOp(null);

		return siacRRuoloOpAzione;
	}

	public List<SiacRAccountRuoloOpFin> getSiacRAccountRuoloOps() {
		return this.siacRAccountRuoloOps;
	}

	public void setSiacRAccountRuoloOps(List<SiacRAccountRuoloOpFin> siacRAccountRuoloOps) {
		this.siacRAccountRuoloOps = siacRAccountRuoloOps;
	}

	public SiacRAccountRuoloOpFin addSiacRAccountRuoloOp(SiacRAccountRuoloOpFin siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().add(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacDRuoloOp(this);

		return siacRAccountRuoloOp;
	}

	public SiacRAccountRuoloOpFin removeSiacRAccountRuoloOp(SiacRAccountRuoloOpFin siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().remove(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacDRuoloOp(null);

		return siacRAccountRuoloOp;
	}

	@Override
	public Integer getUid() {
		return this.ruoloOpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ruoloOpId = uid;
	}
}