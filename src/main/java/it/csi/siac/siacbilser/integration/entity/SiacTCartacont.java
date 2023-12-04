/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_cartacont database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont")
@NamedQuery(name="SiacTCartacont.findAll", query="SELECT s FROM SiacTCartacont s")
public class SiacTCartacont extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_CARTACID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CARTACONT_CARTAC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_CARTACID_GENERATOR")
	@Column(name="cartac_id")
	private Integer cartacId;

	@Column(name="cartac_causale")
	private String cartacCausale;

	@Column(name="cartac_data_pagamento")
	private Date cartacDataPagamento;

	@Column(name="cartac_data_scadenza")
	private Date cartacDataScadenza;

	@Column(name="cartac_importo")
	private BigDecimal cartacImporto;

	@Column(name="cartac_importo_valuta")
	private BigDecimal cartacImportoValuta;

	@Column(name="cartac_numero")
	private Integer cartacNumero;

	@Column(name="cartac_numero_reg")
	private String cartacNumeroReg;

	@Column(name="cartac_oggetto")
	private String cartacOggetto;

	//bi-directional many-to-one association to SiacRCartacontAttr
	@OneToMany(mappedBy="siacTCartacont")
	private List<SiacRCartacontAttr> siacRCartacontAttrs;

	//bi-directional many-to-one association to SiacRCartacontStato
	@OneToMany(mappedBy="siacTCartacont")
	private List<SiacRCartacontStato> siacRCartacontStatos;

	//bi-directional many-to-one association to SiacTAttoAmm
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCartacontDet
	@OneToMany(mappedBy="siacTCartacont")
	private List<SiacTCartacontDet> siacTCartacontDets;

	//bi-directional many-to-one association to SiacTCartacontEstera
	@OneToMany(mappedBy="siacTCartacont")
	private List<SiacTCartacontEstera> siacTCartacontEsteras;

	public SiacTCartacont() {
	}

	public Integer getCartacId() {
		return this.cartacId;
	}

	public void setCartacId(Integer cartacId) {
		this.cartacId = cartacId;
	}

	public String getCartacCausale() {
		return this.cartacCausale;
	}

	public void setCartacCausale(String cartacCausale) {
		this.cartacCausale = cartacCausale;
	}

	public Date getCartacDataPagamento() {
		return this.cartacDataPagamento;
	}

	public void setCartacDataPagamento( Date cartacDataPagamento) {
		this.cartacDataPagamento = cartacDataPagamento;
	}

	public Date getCartacDataScadenza() {
		return this.cartacDataScadenza;
	}

	public void setCartacDataScadenza( Date cartacDataScadenza) {
		this.cartacDataScadenza = cartacDataScadenza;
	}

	public BigDecimal getCartacImporto() {
		return this.cartacImporto;
	}

	public void setCartacImporto(BigDecimal cartacImporto) {
		this.cartacImporto = cartacImporto;
	}

	public BigDecimal getCartacImportoValuta() {
		return this.cartacImportoValuta;
	}

	public void setCartacImportoValuta(BigDecimal cartacImportoValuta) {
		this.cartacImportoValuta = cartacImportoValuta;
	}

	public Integer getCartacNumero() {
		return this.cartacNumero;
	}

	public void setCartacNumero(Integer cartacNumero) {
		this.cartacNumero = cartacNumero;
	}

	public String getCartacNumeroReg() {
		return this.cartacNumeroReg;
	}

	public void setCartacNumeroReg(String cartacNumeroReg) {
		this.cartacNumeroReg = cartacNumeroReg;
	}

	public String getCartacOggetto() {
		return this.cartacOggetto;
	}

	public void setCartacOggetto(String cartacOggetto) {
		this.cartacOggetto = cartacOggetto;
	}

	public List<SiacRCartacontAttr> getSiacRCartacontAttrs() {
		return this.siacRCartacontAttrs;
	}

	public void setSiacRCartacontAttrs(List<SiacRCartacontAttr> siacRCartacontAttrs) {
		this.siacRCartacontAttrs = siacRCartacontAttrs;
	}

	public SiacRCartacontAttr addSiacRCartacontAttr(SiacRCartacontAttr siacRCartacontAttr) {
		getSiacRCartacontAttrs().add(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTCartacont(this);

		return siacRCartacontAttr;
	}

	public SiacRCartacontAttr removeSiacRCartacontAttr(SiacRCartacontAttr siacRCartacontAttr) {
		getSiacRCartacontAttrs().remove(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTCartacont(null);

		return siacRCartacontAttr;
	}

	public List<SiacRCartacontStato> getSiacRCartacontStatos() {
		return this.siacRCartacontStatos;
	}

	public void setSiacRCartacontStatos(List<SiacRCartacontStato> siacRCartacontStatos) {
		this.siacRCartacontStatos = siacRCartacontStatos;
	}

	public SiacRCartacontStato addSiacRCartacontStato(SiacRCartacontStato siacRCartacontStato) {
		getSiacRCartacontStatos().add(siacRCartacontStato);
		siacRCartacontStato.setSiacTCartacont(this);

		return siacRCartacontStato;
	}

	public SiacRCartacontStato removeSiacRCartacontStato(SiacRCartacontStato siacRCartacontStato) {
		getSiacRCartacontStatos().remove(siacRCartacontStato);
		siacRCartacontStato.setSiacTCartacont(null);

		return siacRCartacontStato;
	}

	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public List<SiacTCartacontDet> getSiacTCartacontDets() {
		return this.siacTCartacontDets;
	}

	public void setSiacTCartacontDets(List<SiacTCartacontDet> siacTCartacontDets) {
		this.siacTCartacontDets = siacTCartacontDets;
	}

	public SiacTCartacontDet addSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		getSiacTCartacontDets().add(siacTCartacontDet);
		siacTCartacontDet.setSiacTCartacont(this);

		return siacTCartacontDet;
	}

	public SiacTCartacontDet removeSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		getSiacTCartacontDets().remove(siacTCartacontDet);
		siacTCartacontDet.setSiacTCartacont(null);

		return siacTCartacontDet;
	}

	public List<SiacTCartacontEstera> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEstera> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEstera addSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacTCartacont(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEstera removeSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacTCartacont(null);

		return siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		return cartacId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacId = uid;
	}

}