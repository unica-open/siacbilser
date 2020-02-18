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
 * The persistent class for the siac_r_cartacont_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_stato")
@NamedQuery(name="SiacRCartacontStato.findAll", query="SELECT s FROM SiacRCartacontStato s")
public class SiacRCartacontStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_STATO_CARTACSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_STATO_CARTAC_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_STATO_CARTACSTATORID_GENERATOR")
	@Column(name="cartac_stato_r_id")
	private Integer cartacStatoRId;
	
	//bi-directional many-to-one association to SiacDCartacontStato
	@ManyToOne
	@JoinColumn(name="cartac_stato_id")
	private SiacDCartacontStato siacDCartacontStato;

	//bi-directional many-to-one association to SiacTCartacont
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacont siacTCartacont;

	public SiacRCartacontStato() {
	}

	public Integer getCartacStatoRId() {
		return this.cartacStatoRId;
	}

	public void setCartacStatoRId(Integer cartacStatoRId) {
		this.cartacStatoRId = cartacStatoRId;
	}

	public SiacDCartacontStato getSiacDCartacontStato() {
		return this.siacDCartacontStato;
	}

	public void setSiacDCartacontStato(SiacDCartacontStato siacDCartacontStato) {
		this.siacDCartacontStato = siacDCartacontStato;
	}

	public SiacTCartacont getSiacTCartacont() {
		return this.siacTCartacont;
	}

	public void setSiacTCartacont(SiacTCartacont siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
	}

	@Override
	public Integer getUid() {
		return cartacStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacStatoRId = uid;
	}

}