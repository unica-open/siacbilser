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
 * The persistent class for the siac_d_onere_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_onere_tipo")
public class SiacDOnereTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="onere_tipo_id")
	private Integer onereTipoId;

	@Column(name="onere_tipo_code")
	private String onereTipoCode;

	@Column(name="onere_tipo_desc")
	private String onereTipoDesc;

	//bi-directional many-to-one association to SiacDOnereFin
	@OneToMany(mappedBy="siacDOnereTipo")
	private List<SiacDOnereFin> siacDOneres;

	public SiacDOnereTipoFin() {
	}

	public Integer getOnereTipoId() {
		return this.onereTipoId;
	}

	public void setOnereTipoId(Integer onereTipoId) {
		this.onereTipoId = onereTipoId;
	}

	public String getOnereTipoCode() {
		return this.onereTipoCode;
	}

	public void setOnereTipoCode(String onereTipoCode) {
		this.onereTipoCode = onereTipoCode;
	}

	public String getOnereTipoDesc() {
		return this.onereTipoDesc;
	}

	public void setOnereTipoDesc(String onereTipoDesc) {
		this.onereTipoDesc = onereTipoDesc;
	}

	public List<SiacDOnereFin> getSiacDOneres() {
		return this.siacDOneres;
	}

	public void setSiacDOneres(List<SiacDOnereFin> siacDOneres) {
		this.siacDOneres = siacDOneres;
	}

	public SiacDOnereFin addSiacDOnere(SiacDOnereFin siacDOnere) {
		getSiacDOneres().add(siacDOnere);
		siacDOnere.setSiacDOnereTipo(this);

		return siacDOnere;
	}

	public SiacDOnereFin removeSiacDOnere(SiacDOnereFin siacDOnere) {
		getSiacDOneres().remove(siacDOnere);
		siacDOnere.setSiacDOnereTipo(null);

		return siacDOnere;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.onereTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.onereTipoId = uid;
	}
}