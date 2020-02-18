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
 * The persistent class for the siac_r_cartacont_estera_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_estera_attr")
public class SiacRCartacontEsteraAttrFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_ESTERA_ATTR_CARTACONT_ESTERA_ATTR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_estera_attr_cartacest_attr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_ESTERA_ATTR_CARTACONT_ESTERA_ATTR_ID_GENERATOR")
	@Column(name="cartacest_attr_id")
	private Integer cartacestAttrId;

	//bi-directional many-to-one association to SiacTCartacontEsteraFin
	@ManyToOne
	@JoinColumn(name="cartacest_id")
	private SiacTCartacontEsteraFin siacTCartacontEstera;

	public SiacRCartacontEsteraAttrFin() {
	}

	public Integer getCartacestAttrId() {
		return this.cartacestAttrId;
	}

	public void setCartacestAttrId(Integer cartacestAttrId) {
		this.cartacestAttrId = cartacestAttrId;
	}

	public SiacTCartacontEsteraFin getSiacTCartacontEstera() {
		return this.siacTCartacontEstera;
	}

	public void setSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		this.siacTCartacontEstera = siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.cartacestAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacestAttrId = uid;
	}
}