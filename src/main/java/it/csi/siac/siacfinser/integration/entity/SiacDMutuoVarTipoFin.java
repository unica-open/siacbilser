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
 * The persistent class for the siac_d_mutuo_var_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_var_tipo")
public class SiacDMutuoVarTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_VAR_TIPO_MUTUO_VAR_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_mutuo_var_tipo_mut_var_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_VAR_TIPO_MUTUO_VAR_TIPO_ID_GENERATOR")
	@Column(name="mut_var_tipo_id")
	private Integer mutVarTipoId;

	@Column(name="mut_var_tipo_code")
	private String mutVarTipoCode;

	@Column(name="mut_var_tipo_desc")
	private String mutVarTipoDesc;

	//bi-directional many-to-one association to SiacTMutuoVoceVarFin
	@OneToMany(mappedBy="siacDMutuoVarTipo")
	private List<SiacTMutuoVoceVarFin> siacTMutuoVoceVars;

	public SiacDMutuoVarTipoFin() {
	}

	public Integer getMutVarTipoId() {
		return this.mutVarTipoId;
	}

	public void setMutVarTipoId(Integer mutVarTipoId) {
		this.mutVarTipoId = mutVarTipoId;
	}

	public String getMutVarTipoCode() {
		return this.mutVarTipoCode;
	}

	public void setMutVarTipoCode(String mutVarTipoCode) {
		this.mutVarTipoCode = mutVarTipoCode;
	}

	public String getMutVarTipoDesc() {
		return this.mutVarTipoDesc;
	}

	public void setMutVarTipoDesc(String mutVarTipoDesc) {
		this.mutVarTipoDesc = mutVarTipoDesc;
	}

	public List<SiacTMutuoVoceVarFin> getSiacTMutuoVoceVars() {
		return this.siacTMutuoVoceVars;
	}

	public void setSiacTMutuoVoceVars(List<SiacTMutuoVoceVarFin> siacTMutuoVoceVars) {
		this.siacTMutuoVoceVars = siacTMutuoVoceVars;
	}

	public SiacTMutuoVoceVarFin addSiacTMutuoVoceVar(SiacTMutuoVoceVarFin siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().add(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacDMutuoVarTipo(this);

		return siacTMutuoVoceVar;
	}

	public SiacTMutuoVoceVarFin removeSiacTMutuoVoceVar(SiacTMutuoVoceVarFin siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().remove(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacDMutuoVarTipo(null);

		return siacTMutuoVoceVar;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutVarTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutVarTipoId = uid;
	}
}