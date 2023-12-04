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
 * The persistent class for the siac_r_cartacont_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_attr")
@NamedQuery(name="SiacRCartacontAttr.findAll", query="SELECT s FROM SiacRCartacontAttr s")
public class SiacRCartacontAttr extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_ATTR_CARTACATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_ATTR_CARTAC_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_ATTR_CARTACATTRID_GENERATOR")
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

	//bi-directional many-to-one association to SiacTCartacont
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacont siacTCartacont;

	public SiacRCartacontAttr() {
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

	public SiacTCartacont getSiacTCartacont() {
		return this.siacTCartacont;
	}

	public void setSiacTCartacont(SiacTCartacont siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
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