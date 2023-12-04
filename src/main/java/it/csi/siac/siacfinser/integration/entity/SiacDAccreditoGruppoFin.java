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
 * The persistent class for the siac_d_accredito_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_gruppo")
public class SiacDAccreditoGruppoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="accredito_gruppo_id")
	private Integer accreditoGruppoId;

	@Column(name="accredito_gruppo_code")
	private String accreditoGruppoCode;

	@Column(name="accredito_gruppo_desc")
	private String accreditoGruppoDesc;

	//bi-directional many-to-one association to SiacDAccreditoTipoFin
	@OneToMany(mappedBy="siacDAccreditoGruppo")
	private List<SiacDAccreditoTipoFin> siacDAccreditoTipos;

	public SiacDAccreditoGruppoFin() {
	}

	public Integer getAccreditoGruppoId() {
		return this.accreditoGruppoId;
	}

	public void setAccreditoGruppoId(Integer accreditoGruppoId) {
		this.accreditoGruppoId = accreditoGruppoId;
	}

	public String getAccreditoGruppoCode() {
		return this.accreditoGruppoCode;
	}

	public void setAccreditoGruppoCode(String accreditoGruppoCode) {
		this.accreditoGruppoCode = accreditoGruppoCode;
	}

	public String getAccreditoGruppoDesc() {
		return this.accreditoGruppoDesc;
	}

	public void setAccreditoGruppoDesc(String accreditoGruppoDesc) {
		this.accreditoGruppoDesc = accreditoGruppoDesc;
	}

	public List<SiacDAccreditoTipoFin> getSiacDAccreditoTipos() {
		return this.siacDAccreditoTipos;
	}

	public void setSiacDAccreditoTipos(List<SiacDAccreditoTipoFin> siacDAccreditoTipos) {
		this.siacDAccreditoTipos = siacDAccreditoTipos;
	}

	public SiacDAccreditoTipoFin addSiacDAccreditoTipo(SiacDAccreditoTipoFin siacDAccreditoTipo) {
		getSiacDAccreditoTipos().add(siacDAccreditoTipo);
		siacDAccreditoTipo.setSiacDAccreditoGruppo(this);

		return siacDAccreditoTipo;
	}

	public SiacDAccreditoTipoFin removeSiacDAccreditoTipo(SiacDAccreditoTipoFin siacDAccreditoTipo) {
		getSiacDAccreditoTipos().remove(siacDAccreditoTipo);
		siacDAccreditoTipo.setSiacDAccreditoGruppo(null);

		return siacDAccreditoTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.accreditoGruppoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.accreditoGruppoId = uid;
	}
}