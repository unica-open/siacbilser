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
 * The persistent class for the siac_r_ordinativo_tipo_class_tip database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_tipo_class_tip")
@NamedQuery(name="SiacROrdinativoTipoClassTip.findAll", query="SELECT s FROM SiacROrdinativoTipoClassTip s")
public class SiacROrdinativoTipoClassTip extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_TIPO_CLASS_TIP_ORDTIPOCLASSIFTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_ORDINATIVO_TIPO_CLASS_TIP_ORD_TIPO_CLASSIF_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_TIPO_CLASS_TIP_ORDTIPOCLASSIFTIPOID_GENERATOR")
	@Column(name="ord_tipo_classif_tipo_id")
	private Integer ordTipoClassifTipoId;

	//bi-directional many-to-one association to SiacDClassTipo
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	//bi-directional many-to-one association to SiacDOrdinativoTipo
	@ManyToOne
	@JoinColumn(name="ord_tipo_id")
	private SiacDOrdinativoTipo siacDOrdinativoTipo;

	public SiacROrdinativoTipoClassTip() {
	}

	public Integer getOrdTipoClassifTipoId() {
		return this.ordTipoClassifTipoId;
	}

	public void setOrdTipoClassifTipoId(Integer ordTipoClassifTipoId) {
		this.ordTipoClassifTipoId = ordTipoClassifTipoId;
	}

	public SiacDClassTipo getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	public void setSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	public SiacDOrdinativoTipo getSiacDOrdinativoTipo() {
		return this.siacDOrdinativoTipo;
	}

	public void setSiacDOrdinativoTipo(SiacDOrdinativoTipo siacDOrdinativoTipo) {
		this.siacDOrdinativoTipo = siacDOrdinativoTipo;
	}

	@Override
	public Integer getUid() {
		return ordTipoClassifTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		ordTipoClassifTipoId = uid;
	}

}