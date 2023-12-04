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
 * The persistent class for the siac_r_iva_registro_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_registro_gruppo")
public class SiacRIvaRegistroGruppoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivaregru_id")
	private Integer ivaregruId;

	//bi-directional many-to-one association to SiacTIvaGruppoFin
	@ManyToOne
	@JoinColumn(name="ivagru_id")
	private SiacTIvaGruppoFin siacTIvaGruppo;

	//bi-directional many-to-one association to SiacTIvaRegistroFin
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistroFin siacTIvaRegistro;

	public SiacRIvaRegistroGruppoFin() {
	}

	public Integer getIvaregruId() {
		return this.ivaregruId;
	}

	public void setIvaregruId(Integer ivaregruId) {
		this.ivaregruId = ivaregruId;
	}

	public SiacTIvaGruppoFin getSiacTIvaGruppo() {
		return this.siacTIvaGruppo;
	}

	public void setSiacTIvaGruppo(SiacTIvaGruppoFin siacTIvaGruppo) {
		this.siacTIvaGruppo = siacTIvaGruppo;
	}

	public SiacTIvaRegistroFin getSiacTIvaRegistro() {
		return this.siacTIvaRegistro;
	}

	public void setSiacTIvaRegistro(SiacTIvaRegistroFin siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivaregruId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivaregruId = uid;
	}

}