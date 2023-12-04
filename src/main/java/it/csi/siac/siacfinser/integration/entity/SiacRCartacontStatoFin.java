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
 * The persistent class for the siac_r_cartacont_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_stato")
public class SiacRCartacontStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_STATO_CARTACONT_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_stato_cartac_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_STATO_CARTACONT_STATO_ID_GENERATOR")
	@Column(name="cartac_stato_r_id")
	private Integer cartacStatoRId;

	//bi-directional many-to-one association to SiacDCartacontStatoFin
	@ManyToOne
	@JoinColumn(name="cartac_stato_id")
	private SiacDCartacontStatoFin siacDCartacontStato;

	//bi-directional many-to-one association to SiacTCartacontFin
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacontFin siacTCartacont;

	public SiacRCartacontStatoFin() {
	}

	public Integer getCartacStatoRId() {
		return this.cartacStatoRId;
	}

	public void setCartacStatoRId(Integer cartacStatoRId) {
		this.cartacStatoRId = cartacStatoRId;
	}

	public SiacDCartacontStatoFin getSiacDCartacontStato() {
		return this.siacDCartacontStato;
	}

	public void setSiacDCartacontStato(SiacDCartacontStatoFin siacDCartacontStato) {
		this.siacDCartacontStato = siacDCartacontStato;
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
		return this.cartacStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacStatoRId = uid;
	}

}