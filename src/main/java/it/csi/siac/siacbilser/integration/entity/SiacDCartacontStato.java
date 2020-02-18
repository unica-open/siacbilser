/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cartacont_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_cartacont_stato")
@NamedQuery(name="SiacDCartacontStato.findAll", query="SELECT s FROM SiacDCartacontStato s")
public class SiacDCartacontStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CARTACONT_STATO_CARTACSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CARTACONT_STATO_CARTAC_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CARTACONT_STATO_CARTACSTATOID_GENERATOR")
	@Column(name="cartac_stato_id")
	private Integer cartacStatoId;

	@Column(name="cartac_stato_code")
	private String cartacStatoCode;

	@Column(name="cartac_stato_desc")
	private String cartacStatoDesc;

	//bi-directional many-to-one association to SiacRCartacontStato
	@OneToMany(mappedBy="siacDCartacontStato")
	private List<SiacRCartacontStato> siacRCartacontStatos;

	public SiacDCartacontStato() {
	}

	public Integer getCartacStatoId() {
		return this.cartacStatoId;
	}

	public void setCartacStatoId(Integer cartacStatoId) {
		this.cartacStatoId = cartacStatoId;
	}

	public String getCartacStatoCode() {
		return this.cartacStatoCode;
	}

	public void setCartacStatoCode(String cartacStatoCode) {
		this.cartacStatoCode = cartacStatoCode;
	}

	public String getCartacStatoDesc() {
		return this.cartacStatoDesc;
	}

	public void setCartacStatoDesc(String cartacStatoDesc) {
		this.cartacStatoDesc = cartacStatoDesc;
	}

	public List<SiacRCartacontStato> getSiacRCartacontStatos() {
		return this.siacRCartacontStatos;
	}

	public void setSiacRCartacontStatos(List<SiacRCartacontStato> siacRCartacontStatos) {
		this.siacRCartacontStatos = siacRCartacontStatos;
	}

	public SiacRCartacontStato addSiacRCartacontStato(SiacRCartacontStato siacRCartacontStato) {
		getSiacRCartacontStatos().add(siacRCartacontStato);
		siacRCartacontStato.setSiacDCartacontStato(this);

		return siacRCartacontStato;
	}

	public SiacRCartacontStato removeSiacRCartacontStato(SiacRCartacontStato siacRCartacontStato) {
		getSiacRCartacontStatos().remove(siacRCartacontStato);
		siacRCartacontStato.setSiacDCartacontStato(null);

		return siacRCartacontStato;
	}

	@Override
	public Integer getUid() {
		return cartacStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacStatoId = uid;
	}

}