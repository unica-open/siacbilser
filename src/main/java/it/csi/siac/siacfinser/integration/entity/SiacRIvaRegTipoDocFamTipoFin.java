/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_iva_reg_tipo_doc_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_reg_tipo_doc_fam_tipo")
public class SiacRIvaRegTipoDocFamTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dfr_tipo_id")
	private Integer dfrTipoId;

	//bi-directional many-to-one association to SiacDDocFamTipoFin
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipoFin siacDDocFamTipo;

	//bi-directional many-to-one association to SiacDIvaRegistrazioneTipoFin
	@ManyToOne
	@JoinColumn(name="reg_tipo_id")
	private SiacDIvaRegistrazioneTipoFin siacDIvaRegistrazioneTipo;

	public SiacRIvaRegTipoDocFamTipoFin() {
	}

	public Integer getDfrTipoId() {
		return this.dfrTipoId;
	}

	public void setDfrTipoId(Integer dfrTipoId) {
		this.dfrTipoId = dfrTipoId;
	}

	public SiacDDocFamTipoFin getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	public void setSiacDDocFamTipo(SiacDDocFamTipoFin siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

	public SiacDIvaRegistrazioneTipoFin getSiacDIvaRegistrazioneTipo() {
		return this.siacDIvaRegistrazioneTipo;
	}

	public void setSiacDIvaRegistrazioneTipo(SiacDIvaRegistrazioneTipoFin siacDIvaRegistrazioneTipo) {
		this.siacDIvaRegistrazioneTipo = siacDIvaRegistrazioneTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.dfrTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.dfrTipoId = uid;
	}


}