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
 * The persistent class for the siac_d_atto_amm_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_amm_tipo")
public class SiacDAttoAmmTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoamm_tipo_id")
	private Integer attoammTipoId;

	@Column(name="attoamm_tipo_code")
	private String attoammTipoCode;

	@Column(name="attoamm_tipo_desc")
	private String attoammTipoDesc;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@OneToMany(mappedBy="siacDAttoAmmTipo")
	private List<SiacTAttoAmmFin> siacTAttoAmms;

	public SiacDAttoAmmTipoFin() {
	}

	public Integer getAttoammTipoId() {
		return this.attoammTipoId;
	}

	public void setAttoammTipoId(Integer attoammTipoId) {
		this.attoammTipoId = attoammTipoId;
	}

	public String getAttoammTipoCode() {
		return this.attoammTipoCode;
	}

	public void setAttoammTipoCode(String attoammTipoCode) {
		this.attoammTipoCode = attoammTipoCode;
	}

	public String getAttoammTipoDesc() {
		return this.attoammTipoDesc;
	}

	public void setAttoammTipoDesc(String attoammTipoDesc) {
		this.attoammTipoDesc = attoammTipoDesc;
	}

	public List<SiacTAttoAmmFin> getSiacTAttoAmms() {
		return this.siacTAttoAmms;
	}

	public void setSiacTAttoAmms(List<SiacTAttoAmmFin> siacTAttoAmms) {
		this.siacTAttoAmms = siacTAttoAmms;
	}

	public SiacTAttoAmmFin addSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		getSiacTAttoAmms().add(siacTAttoAmm);
		siacTAttoAmm.setSiacDAttoAmmTipo(this);

		return siacTAttoAmm;
	}

	public SiacTAttoAmmFin removeSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		getSiacTAttoAmms().remove(siacTAttoAmm);
		siacTAttoAmm.setSiacDAttoAmmTipo(null);

		return siacTAttoAmm;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoammTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoammTipoId = uid;
	}
}