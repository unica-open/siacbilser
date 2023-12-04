/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_bil_stato_op database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_stato_op")
public class SiacDBilStatoOpFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="bil_stato_op_id")
	private Integer bilStatoOpId;

	@Column(name="bil_stato_op_code")
	private String bilStatoOpCode;

	@Column(name="bil_stato_op_desc")
	private String bilStatoOpDesc;

	//bi-directional many-to-one association to SiacRBilStatoOpFin
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRBilStatoOpFin> siacRBilStatoOps;

	//bi-directional many-to-one association to SiacRBilTipoStatoOpFin
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRBilTipoStatoOpFin> siacRBilTipoStatoOps;

	//bi-directional many-to-one association to SiacRFaseOperativaBilStatoFin
	@OneToMany(mappedBy="siacDBilStatoOp")
	private List<SiacRFaseOperativaBilStatoFin> siacRFaseOperativaBilStatos;

	public SiacDBilStatoOpFin() {
	}

	public Integer getBilStatoOpId() {
		return this.bilStatoOpId;
	}

	public void setBilStatoOpId(Integer bilStatoOpId) {
		this.bilStatoOpId = bilStatoOpId;
	}

	public String getBilStatoOpCode() {
		return this.bilStatoOpCode;
	}

	public void setBilStatoOpCode(String bilStatoOpCode) {
		this.bilStatoOpCode = bilStatoOpCode;
	}

	public String getBilStatoOpDesc() {
		return this.bilStatoOpDesc;
	}

	public void setBilStatoOpDesc(String bilStatoOpDesc) {
		this.bilStatoOpDesc = bilStatoOpDesc;
	}

	public List<SiacRBilStatoOpFin> getSiacRBilStatoOps() {
		return this.siacRBilStatoOps;
	}

	public void setSiacRBilStatoOps(List<SiacRBilStatoOpFin> siacRBilStatoOps) {
		this.siacRBilStatoOps = siacRBilStatoOps;
	}

	public SiacRBilStatoOpFin addSiacRBilStatoOp(SiacRBilStatoOpFin siacRBilStatoOp) {
		getSiacRBilStatoOps().add(siacRBilStatoOp);
		siacRBilStatoOp.setSiacDBilStatoOp(this);

		return siacRBilStatoOp;
	}

	public SiacRBilStatoOpFin removeSiacRBilStatoOp(SiacRBilStatoOpFin siacRBilStatoOp) {
		getSiacRBilStatoOps().remove(siacRBilStatoOp);
		siacRBilStatoOp.setSiacDBilStatoOp(null);

		return siacRBilStatoOp;
	}

	public List<SiacRBilTipoStatoOpFin> getSiacRBilTipoStatoOps() {
		return this.siacRBilTipoStatoOps;
	}

	public void setSiacRBilTipoStatoOps(List<SiacRBilTipoStatoOpFin> siacRBilTipoStatoOps) {
		this.siacRBilTipoStatoOps = siacRBilTipoStatoOps;
	}

	public SiacRBilTipoStatoOpFin addSiacRBilTipoStatoOp(SiacRBilTipoStatoOpFin siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().add(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilStatoOp(this);

		return siacRBilTipoStatoOp;
	}

	public SiacRBilTipoStatoOpFin removeSiacRBilTipoStatoOp(SiacRBilTipoStatoOpFin siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().remove(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilStatoOp(null);

		return siacRBilTipoStatoOp;
	}

	public List<SiacRFaseOperativaBilStatoFin> getSiacRFaseOperativaBilStatos() {
		return this.siacRFaseOperativaBilStatos;
	}

	public void setSiacRFaseOperativaBilStatos(List<SiacRFaseOperativaBilStatoFin> siacRFaseOperativaBilStatos) {
		this.siacRFaseOperativaBilStatos = siacRFaseOperativaBilStatos;
	}

	public SiacRFaseOperativaBilStatoFin addSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStatoFin siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().add(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDBilStatoOp(this);

		return siacRFaseOperativaBilStato;
	}

	public SiacRFaseOperativaBilStatoFin removeSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStatoFin siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().remove(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDBilStatoOp(null);

		return siacRFaseOperativaBilStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilStatoOpId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilStatoOpId = uid;
	}

}