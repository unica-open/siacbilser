/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_iva_reg_tipo_doc_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_reg_tipo_doc_fam_tipo")
@NamedQuery(name="SiacRIvaRegTipoDocFamTipo.findAll", query="SELECT s FROM SiacRIvaRegTipoDocFamTipo s")
public class SiacRIvaRegTipoDocFamTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_IVA_REG_TIPO_DOC_FAM_TIPO_DFRTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_IVA_REG_TIPO_DOC_FAM_TIPO_DFR_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_REG_TIPO_DOC_FAM_TIPO_DFRTIPOID_GENERATOR")
	@Column(name="dfr_tipo_id")
	private Integer dfrTipoId;

	//bi-directional many-to-one association to SiacDDocFamTipo
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipo siacDDocFamTipo;

	//bi-directional many-to-one association to SiacDIvaRegistrazioneTipo
	@ManyToOne
	@JoinColumn(name="reg_tipo_id")
	private SiacDIvaRegistrazioneTipo siacDIvaRegistrazioneTipo;

	public SiacRIvaRegTipoDocFamTipo() {
	}

	public Integer getDfrTipoId() {
		return this.dfrTipoId;
	}

	public void setDfrTipoId(Integer dfrTipoId) {
		this.dfrTipoId = dfrTipoId;
	}

	public SiacDDocFamTipo getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	public void setSiacDDocFamTipo(SiacDDocFamTipo siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

	public SiacDIvaRegistrazioneTipo getSiacDIvaRegistrazioneTipo() {
		return this.siacDIvaRegistrazioneTipo;
	}

	public void setSiacDIvaRegistrazioneTipo(SiacDIvaRegistrazioneTipo siacDIvaRegistrazioneTipo) {
		this.siacDIvaRegistrazioneTipo = siacDIvaRegistrazioneTipo;
	}

	@Override
	public Integer getUid() {
		return dfrTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.dfrTipoId = uid;
		
	}

}