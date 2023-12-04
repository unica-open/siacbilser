/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_bil_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_tipo")
public class SiacDBilTipoFin  extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_TIPO_BIL_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_bil_tipo_bil_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_TIPO_BIL_TIPO_ID_GENERATOR")	
	@Column(name="bil_tipo_id")
	private Integer bilTipoId;

	@Column(name="bil_tipo_code")
	private String bilTipoCode;

	@Column(name="bil_tipo_desc")
	private String bilTipoDesc;

	//bi-directional many-to-one association to SiacRBilTipoStatoOpFin
	@OneToMany(mappedBy="siacDBilTipo")
	private List<SiacRBilTipoStatoOpFin> siacRBilTipoStatoOps;

	//bi-directional many-to-one association to SiacTBilFin
	@OneToMany(mappedBy="siacDBilTipo")
	private List<SiacTBilFin> siacTBils;

	public SiacDBilTipoFin() {
	}

	public Integer getBilTipoId() {
		return this.bilTipoId;
	}

	public void setBilTipoId(Integer bilTipoId) {
		this.bilTipoId = bilTipoId;
	}

	public String getBilTipoCode() {
		return this.bilTipoCode;
	}

	public void setBilTipoCode(String bilTipoCode) {
		this.bilTipoCode = bilTipoCode;
	}

	public String getBilTipoDesc() {
		return this.bilTipoDesc;
	}

	public void setBilTipoDesc(String bilTipoDesc) {
		this.bilTipoDesc = bilTipoDesc;
	}

	public List<SiacRBilTipoStatoOpFin> getSiacRBilTipoStatoOps() {
		return this.siacRBilTipoStatoOps;
	}

	public void setSiacRBilTipoStatoOps(List<SiacRBilTipoStatoOpFin> siacRBilTipoStatoOps) {
		this.siacRBilTipoStatoOps = siacRBilTipoStatoOps;
	}

	public SiacRBilTipoStatoOpFin addSiacRBilTipoStatoOp(SiacRBilTipoStatoOpFin siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().add(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilTipo(this);

		return siacRBilTipoStatoOp;
	}

	public SiacRBilTipoStatoOpFin removeSiacRBilTipoStatoOp(SiacRBilTipoStatoOpFin siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().remove(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilTipo(null);

		return siacRBilTipoStatoOp;
	}

	public List<SiacTBilFin> getSiacTBils() {
		return this.siacTBils;
	}

	public void setSiacTBils(List<SiacTBilFin> siacTBils) {
		this.siacTBils = siacTBils;
	}

	public SiacTBilFin addSiacTBil(SiacTBilFin siacTBil) {
		getSiacTBils().add(siacTBil);
		siacTBil.setSiacDBilTipo(this);

		return siacTBil;
	}

	public SiacTBilFin removeSiacTBil(SiacTBilFin siacTBil) {
		getSiacTBils().remove(siacTBil);
		siacTBil.setSiacDBilTipo(null);

		return siacTBil;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilTipoId = uid;
	}

}