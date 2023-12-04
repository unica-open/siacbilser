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
 * The persistent class for the siac_d_modifica_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_modifica_tipo")
public class SiacDModificaTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MODIFICA_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_modifica_tipo_mod_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODIFICA_TIPO_ID_GENERATOR")
	@Column(name="mod_tipo_id")
	private Integer modTipoId;

	@Column(name="mod_tipo_code")
	private String modTipoCode;

	@Column(name="mod_tipo_desc")
	private String modTipoDesc;

	//bi-directional many-to-one association to SiacTModificaFin
	@OneToMany(mappedBy="siacDModificaTipo")
	private List<SiacTModificaFin> siacTModificas;

	public SiacDModificaTipoFin() {
	}

	public Integer getModTipoId() {
		return this.modTipoId;
	}

	public void setModTipoId(Integer modTipoId) {
		this.modTipoId = modTipoId;
	}

	public String getModTipoCode() {
		return this.modTipoCode;
	}

	public void setModTipoCode(String modTipoCode) {
		this.modTipoCode = modTipoCode;
	}

	public String getModTipoDesc() {
		return this.modTipoDesc;
	}

	public void setModTipoDesc(String modTipoDesc) {
		this.modTipoDesc = modTipoDesc;
	}

	public List<SiacTModificaFin> getSiacTModificas() {
		return this.siacTModificas;
	}

	public void setSiacTModificas(List<SiacTModificaFin> siacTModificas) {
		this.siacTModificas = siacTModificas;
	}

	public SiacTModificaFin addSiacTModifica(SiacTModificaFin siacTModifica) {
		getSiacTModificas().add(siacTModifica);
		siacTModifica.setSiacDModificaTipo(this);

		return siacTModifica;
	}

	public SiacTModificaFin removeSiacTModifica(SiacTModificaFin siacTModifica) {
		getSiacTModificas().remove(siacTModifica);
		siacTModifica.setSiacDModificaTipo(null);

		return siacTModifica;
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