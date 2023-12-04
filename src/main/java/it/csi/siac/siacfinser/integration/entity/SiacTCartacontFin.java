/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_cartacont database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont")
public class SiacTCartacontFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_CARTACONT_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_cartacont_cartac_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_CARTACONT_ID_GENERATOR")
	@Column(name="cartac_id")
	private Integer cartacId;
	
	@Column(name="cartac_causale")
	private String cartacCausale;

	@Column(name="cartac_data_pagamento")
	private Timestamp cartacDataPagamento;

	@Column(name="cartac_data_scadenza")
	private Timestamp cartacDataScadenza;

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

	//bi-directional many-to-one association to SiacRCartacontAttrFin
	@OneToMany(mappedBy="siacTCartacont")
	// @OneToMany(mappedBy="siacTCartacont", fetch=FetchType.LAZY)
	private List<SiacRCartacontAttrFin> siacRCartacontAttrs;

	//bi-directional many-to-one association to SiacRCartacontStatoFin
	@OneToMany(mappedBy="siacTCartacont")
	// @OneToMany(mappedBy="siacTCartacont", fetch=FetchType.LAZY)
	private List<SiacRCartacontStatoFin> siacRCartacontStatos;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	//bi-directional many-to-one association to SiacTCartacontDetFin
	@OneToMany(mappedBy="siacTCartacont")
	// @OneToMany(mappedBy="siacTCartacont", fetch=FetchType.LAZY)
	private List<SiacTCartacontDetFin> siacTCartacontDets;

	//bi-directional many-to-one association to SiacTCartacontEsteraFin
	@OneToMany(mappedBy="siacTCartacont")
	// @OneToMany(mappedBy="siacTCartacont", fetch=FetchType.LAZY)
	private List<SiacTCartacontEsteraFin> siacTCartacontEsteras;

	public SiacTCartacontFin() {
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

	public Timestamp getCartacDataPagamento() {
		return this.cartacDataPagamento;
	}

	public void setCartacDataPagamento(Timestamp cartacDataPagamento) {
		this.cartacDataPagamento = cartacDataPagamento;
	}

	public Timestamp getCartacDataScadenza() {
		return this.cartacDataScadenza;
	}

	public void setCartacDataScadenza(Timestamp cartacDataScadenza) {
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

	public List<SiacRCartacontAttrFin> getSiacRCartacontAttrs() {
		return this.siacRCartacontAttrs;
	}

	public void setSiacRCartacontAttrs(List<SiacRCartacontAttrFin> siacRCartacontAttrs) {
		this.siacRCartacontAttrs = siacRCartacontAttrs;
	}

	public SiacRCartacontAttrFin addSiacRCartacontAttr(SiacRCartacontAttrFin siacRCartacontAttr) {
		getSiacRCartacontAttrs().add(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTCartacont(this);

		return siacRCartacontAttr;
	}

	public SiacRCartacontAttrFin removeSiacRCartacontAttr(SiacRCartacontAttrFin siacRCartacontAttr) {
		getSiacRCartacontAttrs().remove(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTCartacont(null);

		return siacRCartacontAttr;
	}

	public List<SiacRCartacontStatoFin> getSiacRCartacontStatos() {
		return this.siacRCartacontStatos;
	}

	public void setSiacRCartacontStatos(List<SiacRCartacontStatoFin> siacRCartacontStatos) {
		this.siacRCartacontStatos = siacRCartacontStatos;
	}

	public SiacRCartacontStatoFin addSiacRCartacontStato(SiacRCartacontStatoFin siacRCartacontStato) {
		getSiacRCartacontStatos().add(siacRCartacontStato);
		siacRCartacontStato.setSiacTCartacont(this);

		return siacRCartacontStato;
	}

	public SiacRCartacontStatoFin removeSiacRCartacontStato(SiacRCartacontStatoFin siacRCartacontStato) {
		getSiacRCartacontStatos().remove(siacRCartacontStato);
		siacRCartacontStato.setSiacTCartacont(null);

		return siacRCartacontStato;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public List<SiacTCartacontDetFin> getSiacTCartacontDets() {
		return this.siacTCartacontDets;
	}

	public void setSiacTCartacontDets(List<SiacTCartacontDetFin> siacTCartacontDets) {
		this.siacTCartacontDets = siacTCartacontDets;
	}

	public SiacTCartacontDetFin addSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		getSiacTCartacontDets().add(siacTCartacontDet);
		siacTCartacontDet.setSiacTCartacont(this);

		return siacTCartacontDet;
	}

	public SiacTCartacontDetFin removeSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		getSiacTCartacontDets().remove(siacTCartacontDet);
		siacTCartacontDet.setSiacTCartacont(null);

		return siacTCartacontDet;
	}

	public List<SiacTCartacontEsteraFin> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEsteraFin> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEsteraFin addSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacTCartacont(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEsteraFin removeSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacTCartacont(null);

		return siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.cartacId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacId = uid;
	}

}