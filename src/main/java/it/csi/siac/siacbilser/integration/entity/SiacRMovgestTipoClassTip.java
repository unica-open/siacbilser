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
 * The persistent class for the siac_r_movgest_tipo_class_tip database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_tipo_class_tip")
@NamedQuery(name="SiacRMovgestTipoClassTip.findAll", query="SELECT s FROM SiacRMovgestTipoClassTip s")
public class SiacRMovgestTipoClassTip extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TIPO_CLASS_TIP_MOVGESTTIPOCLASSIFTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MOVGEST_TIPO_CLASS_TIP_MOVGEST_TIPO_CLASSIF_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TIPO_CLASS_TIP_MOVGESTTIPOCLASSIFTIPOID_GENERATOR")
	@Column(name="movgest_tipo_classif_tipo_id")
	private Integer movgestTipoClassifTipoId;

	//bi-directional many-to-one association to SiacDClassTipo
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	//bi-directional many-to-one association to SiacDMovgestTipo
	@ManyToOne
	@JoinColumn(name="movgest_tipo_id")
	private SiacDMovgestTipo siacDMovgestTipo;

	public SiacRMovgestTipoClassTip() {
	}

	public Integer getMovgestTipoClassifTipoId() {
		return this.movgestTipoClassifTipoId;
	}

	public void setMovgestTipoClassifTipoId(Integer movgestTipoClassifTipoId) {
		this.movgestTipoClassifTipoId = movgestTipoClassifTipoId;
	}

	public SiacDClassTipo getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	public void setSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	public SiacDMovgestTipo getSiacDMovgestTipo() {
		return this.siacDMovgestTipo;
	}

	public void setSiacDMovgestTipo(SiacDMovgestTipo siacDMovgestTipo) {
		this.siacDMovgestTipo = siacDMovgestTipo;
	}

	@Override
	public Integer getUid() {
		return movgestTipoClassifTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestTipoClassifTipoId = uid;
	}

}