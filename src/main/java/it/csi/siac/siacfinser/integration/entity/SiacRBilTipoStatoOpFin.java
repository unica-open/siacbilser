/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_bil_tipo_stato_op database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_tipo_stato_op")
public class SiacRBilTipoStatoOpFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_TIPO_STATO_OP_BIL_TIPO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_tipo_stato_op_bil_tipo_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_TIPO_STATO_OP_BIL_TIPO_STATO_ID_GENERATOR")	
	@Column(name="bil_tipo_stato_id")
	private Integer bilTipoStatoId;

	//bi-directional many-to-one association to SiacDBilStatoOpFin
	@ManyToOne
	@JoinColumn(name="bil_stato_op_id")
	private SiacDBilStatoOpFin siacDBilStatoOp;

	//bi-directional many-to-one association to SiacDBilTipoFin
	@ManyToOne
	@JoinColumn(name="bil_tipo_id")
	private SiacDBilTipoFin siacDBilTipo;

	public SiacRBilTipoStatoOpFin() {
	}

	public Integer getBilTipoStatoId() {
		return this.bilTipoStatoId;
	}

	public void setBilTipoStatoId(Integer bilTipoStatoId) {
		this.bilTipoStatoId = bilTipoStatoId;
	}

	public SiacDBilStatoOpFin getSiacDBilStatoOp() {
		return this.siacDBilStatoOp;
	}

	public void setSiacDBilStatoOp(SiacDBilStatoOpFin siacDBilStatoOp) {
		this.siacDBilStatoOp = siacDBilStatoOp;
	}

	public SiacDBilTipoFin getSiacDBilTipo() {
		return this.siacDBilTipo;
	}

	public void setSiacDBilTipo(SiacDBilTipoFin siacDBilTipo) {
		this.siacDBilTipo = siacDBilTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilTipoStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilTipoStatoId = uid;
	}

}