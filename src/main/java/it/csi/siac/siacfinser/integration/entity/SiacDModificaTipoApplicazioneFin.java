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
 * The persistent class for the siac_d_modifica_tipo_applicazione database table.
 * 
 */
@Entity
@Table(name="siac_d_modifica_tipo_applicazione")
public class SiacDModificaTipoApplicazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MODIFICA_TIPO_APPLICAZIONE_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_modifica_tipo_applicazione_mod_tipo_appl_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODIFICA_TIPO_APPLICAZIONE_ID_GENERATOR")
	@Column(name="mod_tipo_appl_id")
	private Integer modTipoId;

	@Column(name="mod_tipo_appl_code")
	private String modTipoApplCode;

	@Column(name="mod_tipo_appl_desc")
	private String modTipoApplDesc;

	@OneToMany(mappedBy="siacDModificaTipoApplicazione")
	private List<SiacRModificaTipoApplicazioneFin> siacRModificaTipoTipoApplicaziones;

	public SiacDModificaTipoApplicazioneFin() {
	}

	public Integer getModTipoId() {
		return this.modTipoId;
	}

	public void setModTipoId(Integer modTipoId) {
		this.modTipoId = modTipoId;
	}


	

	public String getModTipoApplCode() {
		return modTipoApplCode;
	}

	public void setModTipoApplCode(String modTipoApplCode) {
		this.modTipoApplCode = modTipoApplCode;
	}

	public String getModTipoApplDesc() {
		return modTipoApplDesc;
	}

	public void setModTipoApplDesc(String modTipoApplDesc) {
		this.modTipoApplDesc = modTipoApplDesc;
	}

	public List<SiacRModificaTipoApplicazioneFin> getSiacRModificaTipoTipoApplicaziones() {
		return siacRModificaTipoTipoApplicaziones;
	}

	public void setSiacRModificaTipoTipoApplicaziones(
			List<SiacRModificaTipoApplicazioneFin> siacRModificaTipoTipoApplicaziones) {
		this.siacRModificaTipoTipoApplicaziones = siacRModificaTipoTipoApplicaziones;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modTipoId = uid;
	}
}