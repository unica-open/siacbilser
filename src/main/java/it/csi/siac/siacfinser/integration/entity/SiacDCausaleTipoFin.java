/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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
 * The persistent class for the siac_d_causale_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_tipo")
public class SiacDCausaleTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_tipo_id")
	private Integer causTipoId;

	@Column(name="caus_tipo_code")
	private String causTipoCode;

	@Column(name="caus_tipo_desc")
	private String causTipoDesc;

	//bi-directional many-to-one association to SiacDCausaleFamTipoFin
	@ManyToOne
	@JoinColumn(name="caus_fam_tipo_id")
	private SiacDCausaleFamTipoFin siacDCausaleFamTipo;

	//bi-directional many-to-one association to SiacRCausaleTipoFin
	@OneToMany(mappedBy="siacDCausaleTipo")
	private List<SiacRCausaleTipoFin> siacRCausaleTipos;

	public SiacDCausaleTipoFin() {
	}

	public Integer getCausTipoId() {
		return this.causTipoId;
	}

	public void setCausTipoId(Integer causTipoId) {
		this.causTipoId = causTipoId;
	}

	public String getCausTipoCode() {
		return this.causTipoCode;
	}

	public void setCausTipoCode(String causTipoCode) {
		this.causTipoCode = causTipoCode;
	}

	public String getCausTipoDesc() {
		return this.causTipoDesc;
	}

	public void setCausTipoDesc(String causTipoDesc) {
		this.causTipoDesc = causTipoDesc;
	}

	public SiacDCausaleFamTipoFin getSiacDCausaleFamTipo() {
		return this.siacDCausaleFamTipo;
	}

	public void setSiacDCausaleFamTipo(SiacDCausaleFamTipoFin siacDCausaleFamTipo) {
		this.siacDCausaleFamTipo = siacDCausaleFamTipo;
	}

	public List<SiacRCausaleTipoFin> getSiacRCausaleTipos() {
		return this.siacRCausaleTipos;
	}

	public void setSiacRCausaleTipos(List<SiacRCausaleTipoFin> siacRCausaleTipos) {
		this.siacRCausaleTipos = siacRCausaleTipos;
	}

	public SiacRCausaleTipoFin addSiacRCausaleTipo(SiacRCausaleTipoFin siacRCausaleTipo) {
		getSiacRCausaleTipos().add(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausaleTipo(this);

		return siacRCausaleTipo;
	}

	public SiacRCausaleTipoFin removeSiacRCausaleTipo(SiacRCausaleTipoFin siacRCausaleTipo) {
		getSiacRCausaleTipos().remove(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausaleTipo(null);

		return siacRCausaleTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causTipoId = uid;
	}

}