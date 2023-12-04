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
 * The persistent class for the siac_r_cartacont_estera_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_estera_attr")
@NamedQuery(name="SiacRCartacontEsteraAttr.findAll", query="SELECT s FROM SiacRCartacontEsteraAttr s")
public class SiacRCartacontEsteraAttr extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_ESTERA_ATTR_CARTACESTATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_ESTERA_ATTR_CARTACEST_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_ESTERA_ATTR_CARTACESTATTRID_GENERATOR")
	@Column(name="cartacest_attr_id")
	private Integer cartacestAttrId;

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

	//bi-directional many-to-one association to SiacTCartacontEstera
	@ManyToOne
	@JoinColumn(name="cartacest_id")
	private SiacTCartacontEstera siacTCartacontEstera;

	public SiacRCartacontEsteraAttr() {
	}

	public Integer getCartacestAttrId() {
		return this.cartacestAttrId;
	}

	public void setCartacestAttrId(Integer cartacestAttrId) {
		this.cartacestAttrId = cartacestAttrId;
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

	public SiacTCartacontEstera getSiacTCartacontEstera() {
		return this.siacTCartacontEstera;
	}

	public void setSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		this.siacTCartacontEstera = siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		return cartacestAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacestAttrId = uid;
		
	}

}