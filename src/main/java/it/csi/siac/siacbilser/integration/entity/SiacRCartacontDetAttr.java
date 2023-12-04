/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_cartacont_det_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_attr")
@NamedQuery(name="SiacRCartacontDetAttr.findAll", query="SELECT s FROM SiacRCartacontDetAttr s")
public class SiacRCartacontDetAttr extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_ATTR_CARTACATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_DET_ATTR_CARTAC_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_ATTR_CARTACATTRID_GENERATOR")
	@Column(name="cartac_attr_id")
	private Integer cartacAttrId;

	@Column(name="boolean")
	private String boolean_;

	private BigDecimal numerico;

	private BigDecimal percentuale;

	@Column(name="tabella_id")
	private Integer tabellaId;

	private String testo;
	
	//bi-directional many-to-one association to SiacTAttr
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	//bi-directional many-to-one association to SiacTCartacontDet
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDet siacTCartacontDet;

	public SiacRCartacontDetAttr() {
	}

	public Integer getCartacAttrId() {
		return this.cartacAttrId;
	}

	public void setCartacAttrId(Integer cartacAttrId) {
		this.cartacAttrId = cartacAttrId;
	}

	public String getBoolean_() {
		return this.boolean_;
	}

	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}
	
	public BigDecimal getNumerico() {
		return this.numerico;
	}

	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	public BigDecimal getPercentuale() {
		return this.percentuale;
	}

	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	public Integer getTabellaId() {
		return this.tabellaId;
	}

	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public SiacTAttr getSiacTAttr() {
		return this.siacTAttr;
	}

	public void setSiacTAttr(SiacTAttr siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	public SiacTCartacontDet getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	@Override
	public Integer getUid() {
		return cartacAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacAttrId = uid;
	}

}