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
 * The persistent class for the siac_r_bil_fase_operativa database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_fase_operativa")
public class SiacRBilFaseOperativaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_FASE_OPERATIVA_BIL_FASE_OPERATIVA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_fase_operativa_bil_fase_operativa_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_FASE_OPERATIVA_BIL_FASE_OPERATIVA_ID_GENERATOR")
	@Column(name="bil_fase_operativa_id")
	private Integer bilFaseOperativaId;

	//bi-directional many-to-one association to SiacDFaseOperativaFin
	@ManyToOne
	@JoinColumn(name="fase_operativa_id")
	private SiacDFaseOperativaFin siacDFaseOperativa;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	public SiacRBilFaseOperativaFin() {
	}

	public Integer getBilFaseOperativaId() {
		return this.bilFaseOperativaId;
	}

	public void setBilFaseOperativaId(Integer bilFaseOperativaId) {
		this.bilFaseOperativaId = bilFaseOperativaId;
	}

	public SiacDFaseOperativaFin getSiacDFaseOperativa() {
		return this.siacDFaseOperativa;
	}

	public void setSiacDFaseOperativa(SiacDFaseOperativaFin siacDFaseOperativa) {
		this.siacDFaseOperativa = siacDFaseOperativa;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilFaseOperativaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilFaseOperativaId = uid;
	}

}