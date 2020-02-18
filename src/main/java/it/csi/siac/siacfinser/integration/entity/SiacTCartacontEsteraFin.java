/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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
 * The persistent class for the siac_t_cartacont_estera database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont_estera")
public class SiacTCartacontEsteraFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_ESTERA_CARTACONT_ESTERA_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_cartacont_estera_cartacest_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_ESTERA_CARTACONT_ESTERA_ID_GENERATOR")
	@Column(name="cartacest_id")
	private Integer cartacestId;

	@Column(name="cartacest_causalepagamento")
	private String cartacestCausalepagamento;

	@Column(name="cartacest_data_valuta")
	private Timestamp cartacestDataValuta;

	@Column(name="cartacest_diversotitolare")
	private String cartacestDiversotitolare;

	@Column(name="cartacest_istruzioni")
	private String cartacestIstruzioni;

	//bi-directional many-to-one association to SiacRCartacontEsteraAttrFin
	@OneToMany(mappedBy="siacTCartacontEstera")
	private List<SiacRCartacontEsteraAttrFin> siacRCartacontEsteraAttrs;

	//bi-directional many-to-one association to SiacDCommissioniesteroFin
	@ManyToOne
	@JoinColumn(name="commest_tipo_id")
	private SiacDCommissioniesteroFin siacDCommissioniestero;

	//bi-directional many-to-one association to SiacDValutaFin
	@ManyToOne
	@JoinColumn(name="valuta_id")
	private SiacDValutaFin siacDValuta;

	//bi-directional many-to-one association to SiacTCartacontFin
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacontFin siacTCartacont;

	public SiacTCartacontEsteraFin() {
	}

	public Integer getCartacestId() {
		return this.cartacestId;
	}

	public void setCartacestId(Integer cartacestId) {
		this.cartacestId = cartacestId;
	}

	public String getCartacestCausalepagamento() {
		return this.cartacestCausalepagamento;
	}

	public void setCartacestCausalepagamento(String cartacestCausalepagamento) {
		this.cartacestCausalepagamento = cartacestCausalepagamento;
	}

	public Timestamp getCartacestDataValuta() {
		return this.cartacestDataValuta;
	}

	public void setCartacestDataValuta(Timestamp cartacestDataValuta) {
		this.cartacestDataValuta = cartacestDataValuta;
	}

	public String getCartacestDiversotitolare() {
		return this.cartacestDiversotitolare;
	}

	public void setCartacestDiversotitolare(String cartacestDiversotitolare) {
		this.cartacestDiversotitolare = cartacestDiversotitolare;
	}

	public String getCartacestIstruzioni() {
		return this.cartacestIstruzioni;
	}

	public void setCartacestIstruzioni(String cartacestIstruzioni) {
		this.cartacestIstruzioni = cartacestIstruzioni;
	}

	public List<SiacRCartacontEsteraAttrFin> getSiacRCartacontEsteraAttrs() {
		return this.siacRCartacontEsteraAttrs;
	}

	public void setSiacRCartacontEsteraAttrs(List<SiacRCartacontEsteraAttrFin> siacRCartacontEsteraAttrs) {
		this.siacRCartacontEsteraAttrs = siacRCartacontEsteraAttrs;
	}

	public SiacRCartacontEsteraAttrFin addSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttrFin siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().add(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTCartacontEstera(this);

		return siacRCartacontEsteraAttr;
	}

	public SiacRCartacontEsteraAttrFin removeSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttrFin siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().remove(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTCartacontEstera(null);

		return siacRCartacontEsteraAttr;
	}

	public SiacDCommissioniesteroFin getSiacDCommissioniestero() {
		return this.siacDCommissioniestero;
	}

	public void setSiacDCommissioniestero(SiacDCommissioniesteroFin siacDCommissioniestero) {
		this.siacDCommissioniestero = siacDCommissioniestero;
	}

	public SiacDValutaFin getSiacDValuta() {
		return this.siacDValuta;
	}

	public void setSiacDValuta(SiacDValutaFin siacDValuta) {
		this.siacDValuta = siacDValuta;
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
		return this.cartacestId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacestId = uid;
	}

}