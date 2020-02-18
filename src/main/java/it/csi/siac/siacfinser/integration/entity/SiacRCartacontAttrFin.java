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


/**
 * The persistent class for the siac_r_cartacont_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_attr")
public class SiacRCartacontAttrFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_ATTR_CARTACONT_ATTR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_attr_cartac_attr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_ATTR_CARTACONT_ATTR_ID_GENERATOR")
	@Column(name="cartac_attr_id")
	private Integer cartacAttrId;

	//bi-directional many-to-one association to SiacTCartacontFin
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacontFin siacTCartacont;

	public SiacRCartacontAttrFin() {
	}

	public Integer getCartacAttrId() {
		return this.cartacAttrId;
	}

	public void setCartacAttrId(Integer cartacAttrId) {
		this.cartacAttrId = cartacAttrId;
	}

	public SiacTCartacontFin getSiacTCartacont() {
		return this.siacTCartacont;
	}

	public void setSiacTCartacont(SiacTCartacontFin siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.cartacAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacAttrId = uid;
	}
}