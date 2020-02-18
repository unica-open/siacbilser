/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_cartacont_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_cartacont_stato")
public class SiacDCartacontStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cartac_stato_id")
	private Integer cartacStatoId;

	@Column(name="cartac_stato_code")
	private String cartacStatoCode;

	@Column(name="cartac_stato_desc")
	private String cartacStatoDesc;

	//bi-directional many-to-one association to SiacRCartacontStatoFin
	@OneToMany(mappedBy="siacDCartacontStato")
	private List<SiacRCartacontStatoFin> siacRCartacontStatos;

	public SiacDCartacontStatoFin() {
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

	public List<SiacRCartacontStatoFin> getSiacRCartacontStatos() {
		return this.siacRCartacontStatos;
	}

	public void setSiacRCartacontStatos(List<SiacRCartacontStatoFin> siacRCartacontStatos) {
		this.siacRCartacontStatos = siacRCartacontStatos;
	}

	public SiacRCartacontStatoFin addSiacRCartacontStato(SiacRCartacontStatoFin siacRCartacontStato) {
		getSiacRCartacontStatos().add(siacRCartacontStato);
		siacRCartacontStato.setSiacDCartacontStato(this);

		return siacRCartacontStato;
	}

	public SiacRCartacontStatoFin removeSiacRCartacontStato(SiacRCartacontStatoFin siacRCartacontStato) {
		getSiacRCartacontStatos().remove(siacRCartacontStato);
		siacRCartacontStato.setSiacDCartacontStato(null);

		return siacRCartacontStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.cartacStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacStatoId = uid;
	}

}