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
 * The persistent class for the siac_r_cartacont_det_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_subdoc")
@NamedQuery(name="SiacRCartacontDetSubdoc.findAll", query="SELECT s FROM SiacRCartacontDetSubdoc s")
public class SiacRCartacontDetSubdoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_SUBDOC_CARTACDETSUBDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_DET_SUBDOC_CARTAC_DET_SUBDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_SUBDOC_CARTACDETSUBDOCID_GENERATOR")
	@Column(name="cartac_det_subdoc_id")
	private Integer cartacDetSubdocId;

	//bi-directional many-to-one association to SiacTCartacontDet
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDet siacTCartacontDet;

	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	public SiacRCartacontDetSubdoc() {
	}

	public Integer getCartacDetSubdocId() {
		return this.cartacDetSubdocId;
	}

	public void setCartacDetSubdocId(Integer cartacDetSubdocId) {
		this.cartacDetSubdocId = cartacDetSubdocId;
	}

	public SiacTCartacontDet getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return cartacDetSubdocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacDetSubdocId = uid;
	}

}