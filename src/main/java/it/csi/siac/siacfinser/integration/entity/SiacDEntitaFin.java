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
 * The persistent class for the siac_d_entita database table.
 * 
 */
@Entity
@Table(name="siac_d_entita")
public class SiacDEntitaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="entita_id")
	private Integer entitaId;

	@Column(name="entita_code")
	private String entitaCode;

	@Column(name="entita_desc")
	private String entitaDesc;

	//bi-directional many-to-one association to SiacRAttrEntitaFin
	@OneToMany(mappedBy="siacDEntita")
	private List<SiacRAttrEntitaFin> siacRAttrEntitas;

	public SiacDEntitaFin() {
	}

	public Integer getEntitaId() {
		return this.entitaId;
	}

	public void setEntitaId(Integer entitaId) {
		this.entitaId = entitaId;
	}

	public String getEntitaCode() {
		return this.entitaCode;
	}

	public void setEntitaCode(String entitaCode) {
		this.entitaCode = entitaCode;
	}

	public String getEntitaDesc() {
		return this.entitaDesc;
	}

	public void setEntitaDesc(String entitaDesc) {
		this.entitaDesc = entitaDesc;
	}

	public List<SiacRAttrEntitaFin> getSiacRAttrEntitas() {
		return this.siacRAttrEntitas;
	}

	public void setSiacRAttrEntitas(List<SiacRAttrEntitaFin> siacRAttrEntitas) {
		this.siacRAttrEntitas = siacRAttrEntitas;
	}

	public SiacRAttrEntitaFin addSiacRAttrEntita(SiacRAttrEntitaFin siacRAttrEntita) {
		getSiacRAttrEntitas().add(siacRAttrEntita);
		siacRAttrEntita.setSiacDEntita(this);

		return siacRAttrEntita;
	}

	public SiacRAttrEntitaFin removeSiacRAttrEntita(SiacRAttrEntitaFin siacRAttrEntita) {
		getSiacRAttrEntitas().remove(siacRAttrEntita);
		siacRAttrEntita.setSiacDEntita(null);

		return siacRAttrEntita;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.entitaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.entitaId = uid;
	}
}