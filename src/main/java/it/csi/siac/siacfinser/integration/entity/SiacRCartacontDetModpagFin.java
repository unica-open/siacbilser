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
 * The persistent class for the siac_r_cartacont_det_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_modpag")
public class SiacRCartacontDetModpagFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_MODPAG_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_det_modpag_ord_modpag_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_MODPAG_ID_GENERATOR")
	@Column(name="ord_modpag_id")
	private Integer ordModpagId;

	//bi-directional many-to-one association to SiacTCartacontDetFin
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDetFin siacTCartacontDet;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	public SiacRCartacontDetModpagFin() {
	}

	public Integer getOrdModpagId() {
		return this.ordModpagId;
	}

	public void setOrdModpagId(Integer ordModpagId) {
		this.ordModpagId = ordModpagId;
	}

	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordModpagId = uid;
	}

}