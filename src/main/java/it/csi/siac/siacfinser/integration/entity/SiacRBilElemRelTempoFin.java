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
 * The persistent class for the siac_r_bil_elem_rel_tempo database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_rel_tempo")
public class SiacRBilElemRelTempoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_REL_TEMPO_ELEM_ELEM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_rel_tempo_elem_elem_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_REL_TEMPO_ELEM_ELEM_ID_GENERATOR")
	@Column(name="elem_elem_id")
	private Integer elemElemId;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id_old")
	// @JoinColumn(name="elem_id_old", insertable=false, updatable=false)
	private SiacTBilElemFin siacTBilElem1;

	// bi-directional many-to-one association to SiacTBilElemFin
	// @ManyToOne
	// @JoinColumn(name="elem_id_old")
	// @JoinColumn(name="elem_id_old", insertable=false, updatable=false)
	// private SiacTBilElemFin siacTBilElem2;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem3;

	public SiacRBilElemRelTempoFin() {
	}

	public Integer getElemElemId() {
		return this.elemElemId;
	}

	public void setElemElemId(Integer elemElemId) {
		this.elemElemId = elemElemId;
	}

	public SiacTBilElemFin getSiacTBilElem1() {
		return this.siacTBilElem1;
	}

	public void setSiacTBilElem1(SiacTBilElemFin siacTBilElem1) {
		this.siacTBilElem1 = siacTBilElem1;
	}

//	public SiacTBilElemFin getSiacTBilElem2() {
//		return this.siacTBilElem2;
//	}
//
//	public void setSiacTBilElem2(SiacTBilElemFin siacTBilElem2) {
//		this.siacTBilElem2 = siacTBilElem2;
//	}

	public SiacTBilElemFin getSiacTBilElem3() {
		return this.siacTBilElem3;
	}

	public void setSiacTBilElem3(SiacTBilElemFin siacTBilElem3) {
		this.siacTBilElem3 = siacTBilElem3;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemElemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemElemId = uid;
	}
}