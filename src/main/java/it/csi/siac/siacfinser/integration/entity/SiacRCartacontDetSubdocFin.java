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
 * The persistent class for the siac_r_cartacont_det_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_subdoc")
public class SiacRCartacontDetSubdocFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_SUBDOC_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_det_subdoc_cartac_det_subdoc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_SUBDOC_ID_GENERATOR")
	@Column(name="cartac_det_subdoc_id")
	private Integer cartacDetSubdocId;

	//bi-directional many-to-one association to SiacTCartacontDetFin
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDetFin siacTCartacontDet;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRCartacontDetSubdocFin() {
	}

	public Integer getCartacDetSubdocId() {
		return this.cartacDetSubdocId;
	}

	public void setCartacDetSubdocId(Integer cartacDetSubdocId) {
		this.cartacDetSubdocId = cartacDetSubdocId;
	}

	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTSubdocFin getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.cartacDetSubdocId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacDetSubdocId = uid;
	}

}