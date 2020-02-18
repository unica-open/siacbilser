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
 * The persistent class for the siac_r_ordinativo_tipo_class_tip database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_tipo_class_tip")
public class SiacROrdinativoTipoClassTipFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ord_tipo_classif_tipo_id")
	private Integer ordTipoClassifTipoId;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipoFin siacDClassTipo;

	//bi-directional many-to-one association to SiacDOrdinativoTipoFin
	@ManyToOne
	@JoinColumn(name="ord_tipo_id")
	private SiacDOrdinativoTipoFin siacDOrdinativoTipo;

	public SiacROrdinativoTipoClassTipFin() {
	}

	public Integer getOrdTipoClassifTipoId() {
		return this.ordTipoClassifTipoId;
	}

	public void setOrdTipoClassifTipoId(Integer ordTipoClassifTipoId) {
		this.ordTipoClassifTipoId = ordTipoClassifTipoId;
	}

	public SiacDClassTipoFin getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	public void setSiacDClassTipo(SiacDClassTipoFin siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	public SiacDOrdinativoTipoFin getSiacDOrdinativoTipo() {
		return this.siacDOrdinativoTipo;
	}

	public void setSiacDOrdinativoTipo(SiacDOrdinativoTipoFin siacDOrdinativoTipo) {
		this.siacDOrdinativoTipo = siacDOrdinativoTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordTipoClassifTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordTipoClassifTipoId = uid;
	}
}