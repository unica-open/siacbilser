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
 * The persistent class for the siac_d_causale_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_fam_tipo")
public class SiacDCausaleFamTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_fam_tipo_id")
	private Integer causFamTipoId;

	@Column(name="caus_fam_tipo_code")
	private String causFamTipoCode;

	@Column(name="caus_fam_tipo_desc")
	private String causFamTipoDesc;

	//bi-directional many-to-one association to SiacDCausaleTipoFin
	@OneToMany(mappedBy="siacDCausaleFamTipo")
	private List<SiacDCausaleTipoFin> siacDCausaleTipos;

	public SiacDCausaleFamTipoFin() {
	}

	public Integer getCausFamTipoId() {
		return this.causFamTipoId;
	}

	public void setCausFamTipoId(Integer causFamTipoId) {
		this.causFamTipoId = causFamTipoId;
	}

	public String getCausFamTipoCode() {
		return this.causFamTipoCode;
	}

	public void setCausFamTipoCode(String causFamTipoCode) {
		this.causFamTipoCode = causFamTipoCode;
	}

	public String getCausFamTipoDesc() {
		return this.causFamTipoDesc;
	}

	public void setCausFamTipoDesc(String causFamTipoDesc) {
		this.causFamTipoDesc = causFamTipoDesc;
	}

	public List<SiacDCausaleTipoFin> getSiacDCausaleTipos() {
		return this.siacDCausaleTipos;
	}

	public void setSiacDCausaleTipos(List<SiacDCausaleTipoFin> siacDCausaleTipos) {
		this.siacDCausaleTipos = siacDCausaleTipos;
	}

	public SiacDCausaleTipoFin addSiacDCausaleTipo(SiacDCausaleTipoFin siacDCausaleTipo) {
		getSiacDCausaleTipos().add(siacDCausaleTipo);
		siacDCausaleTipo.setSiacDCausaleFamTipo(this);

		return siacDCausaleTipo;
	}

	public SiacDCausaleTipoFin removeSiacDCausaleTipo(SiacDCausaleTipoFin siacDCausaleTipo) {
		getSiacDCausaleTipos().remove(siacDCausaleTipo);
		siacDCausaleTipo.setSiacDCausaleFamTipo(null);

		return siacDCausaleTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causFamTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causFamTipoId = uid;
	}

}