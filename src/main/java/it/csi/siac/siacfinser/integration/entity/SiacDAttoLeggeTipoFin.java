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
 * The persistent class for the siac_d_atto_legge_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_legge_tipo")
public class SiacDAttoLeggeTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attolegge_tipo_id")
	private Integer attoleggeTipoId;

	@Column(name="attolegge_tipo_code")
	private String attoleggeTipoCode;

	@Column(name="attolegge_tipo_desc")
	private String attoleggeTipoDesc;

	//bi-directional many-to-one association to SiacTAttoLeggeFin
	@OneToMany(mappedBy="siacDAttoLeggeTipo")
	private List<SiacTAttoLeggeFin> siacTAttoLegges;

	public SiacDAttoLeggeTipoFin() {
	}

	public Integer getAttoleggeTipoId() {
		return this.attoleggeTipoId;
	}

	public void setAttoleggeTipoId(Integer attoleggeTipoId) {
		this.attoleggeTipoId = attoleggeTipoId;
	}

	public String getAttoleggeTipoCode() {
		return this.attoleggeTipoCode;
	}

	public void setAttoleggeTipoCode(String attoleggeTipoCode) {
		this.attoleggeTipoCode = attoleggeTipoCode;
	}

	public String getAttoleggeTipoDesc() {
		return this.attoleggeTipoDesc;
	}

	public void setAttoleggeTipoDesc(String attoleggeTipoDesc) {
		this.attoleggeTipoDesc = attoleggeTipoDesc;
	}

	public List<SiacTAttoLeggeFin> getSiacTAttoLegges() {
		return this.siacTAttoLegges;
	}

	public void setSiacTAttoLegges(List<SiacTAttoLeggeFin> siacTAttoLegges) {
		this.siacTAttoLegges = siacTAttoLegges;
	}

	public SiacTAttoLeggeFin addSiacTAttoLegge(SiacTAttoLeggeFin siacTAttoLegge) {
		getSiacTAttoLegges().add(siacTAttoLegge);
		siacTAttoLegge.setSiacDAttoLeggeTipo(this);

		return siacTAttoLegge;
	}

	public SiacTAttoLeggeFin removeSiacTAttoLegge(SiacTAttoLeggeFin siacTAttoLegge) {
		getSiacTAttoLegges().remove(siacTAttoLegge);
		siacTAttoLegge.setSiacDAttoLeggeTipo(null);

		return siacTAttoLegge;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoleggeTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoleggeTipoId = uid;
	}
}