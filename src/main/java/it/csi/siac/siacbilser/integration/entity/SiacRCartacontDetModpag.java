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
 * The persistent class for the siac_r_cartacont_det_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_modpag")
@NamedQuery(name="SiacRCartacontDetModpag.findAll", query="SELECT s FROM SiacRCartacontDetModpag s")
public class SiacRCartacontDetModpag extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_MODPAG_ORDMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_DET_MODPAG_ORD_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_MODPAG_ORDMODPAGID_GENERATOR")
	@Column(name="ord_modpag_id")
	private Integer ordModpagId;

	//bi-directional many-to-one association to SiacTCartacontDet
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDet siacTCartacontDet;

	//bi-directional many-to-one association to SiacTModpag
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	public SiacRCartacontDetModpag() {
	}

	public Integer getOrdModpagId() {
		return this.ordModpagId;
	}

	public void setOrdModpagId(Integer ordModpagId) {
		this.ordModpagId = ordModpagId;
	}
	
	public SiacTCartacontDet getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	@Override
	public Integer getUid() {
		return ordModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ordModpagId = uid;
	}

}