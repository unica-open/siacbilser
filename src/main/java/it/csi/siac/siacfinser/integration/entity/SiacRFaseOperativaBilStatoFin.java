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
 * The persistent class for the siac_r_fase_operativa_bil_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_fase_operativa_bil_stato")
public class SiacRFaseOperativaBilStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_FASE_OPERATIVA_BIL_STATO_FASE_OPERATIVA_BIL_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_fase_operativa_bil_stato_fase_operativa_bil_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FASE_OPERATIVA_BIL_STATO_FASE_OPERATIVA_BIL_STATO_ID_GENERATOR")
	@Column(name="fase_operativa_bil_stato_id")
	private Integer faseOperativaBilStatoId;

	//bi-directional many-to-one association to SiacDBilStatoOpFin
	@ManyToOne
	@JoinColumn(name="bil_stato_op_id")
	private SiacDBilStatoOpFin siacDBilStatoOp;

	//bi-directional many-to-one association to SiacDFaseOperativaFin
	@ManyToOne
	@JoinColumn(name="fase_operativa_id")
	private SiacDFaseOperativaFin siacDFaseOperativa;

	public SiacRFaseOperativaBilStatoFin() {
	}

	public Integer getFaseOperativaBilStatoId() {
		return this.faseOperativaBilStatoId;
	}

	public void setFaseOperativaBilStatoId(Integer faseOperativaBilStatoId) {
		this.faseOperativaBilStatoId = faseOperativaBilStatoId;
	}

	public SiacDBilStatoOpFin getSiacDBilStatoOp() {
		return this.siacDBilStatoOp;
	}

	public void setSiacDBilStatoOp(SiacDBilStatoOpFin siacDBilStatoOp) {
		this.siacDBilStatoOp = siacDBilStatoOp;
	}

	public SiacDFaseOperativaFin getSiacDFaseOperativa() {
		return this.siacDFaseOperativa;
	}

	public void setSiacDFaseOperativa(SiacDFaseOperativaFin siacDFaseOperativa) {
		this.siacDFaseOperativa = siacDFaseOperativa;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.faseOperativaBilStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.faseOperativaBilStatoId = uid;
	}

}