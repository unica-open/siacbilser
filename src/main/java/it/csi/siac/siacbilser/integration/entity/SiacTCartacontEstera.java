/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_t_cartacont_estera database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont_estera")
@NamedQuery(name="SiacTCartacontEstera.findAll", query="SELECT s FROM SiacTCartacontEstera s")
public class SiacTCartacontEstera extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_ESTERA_CARTACESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CARTACONT_ESTERA_CARTACEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_ESTERA_CARTACESTID_GENERATOR")
	@Column(name="cartacest_id")
	private Integer cartacestId;

	@Column(name="cartacest_causalepagamento")
	private String cartacestCausalepagamento;

	@Column(name="cartacest_data_valuta")
	private Date cartacestDataValuta;

	@Column(name="cartacest_diversotitolare")
	private String cartacestDiversotitolare;

	@Column(name="cartacest_istruzioni")
	private String cartacestIstruzioni;

	//bi-directional many-to-one association to SiacRCartacontEsteraAttr
	@OneToMany(mappedBy="siacTCartacontEstera")
	private List<SiacRCartacontEsteraAttr> siacRCartacontEsteraAttrs;

	//bi-directional many-to-one association to SiacDCommissioniestero
	@ManyToOne
	@JoinColumn(name="commest_tipo_id")
	private SiacDCommissioniestero siacDCommissioniestero;

	//bi-directional many-to-one association to SiacDValuta
	@ManyToOne
	@JoinColumn(name="valuta_id")
	private SiacDValuta siacDValuta;

	//bi-directional many-to-one association to SiacTCartacont
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacont siacTCartacont;

	public SiacTCartacontEstera() {
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

	public Date getCartacestDataValuta() {
		return this.cartacestDataValuta;
	}

	public void setCartacestDataValuta( Date cartacestDataValuta) {
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

	public List<SiacRCartacontEsteraAttr> getSiacRCartacontEsteraAttrs() {
		return this.siacRCartacontEsteraAttrs;
	}

	public void setSiacRCartacontEsteraAttrs(List<SiacRCartacontEsteraAttr> siacRCartacontEsteraAttrs) {
		this.siacRCartacontEsteraAttrs = siacRCartacontEsteraAttrs;
	}

	public SiacRCartacontEsteraAttr addSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttr siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().add(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTCartacontEstera(this);

		return siacRCartacontEsteraAttr;
	}

	public SiacRCartacontEsteraAttr removeSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttr siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().remove(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTCartacontEstera(null);

		return siacRCartacontEsteraAttr;
	}

	public SiacDCommissioniestero getSiacDCommissioniestero() {
		return this.siacDCommissioniestero;
	}

	public void setSiacDCommissioniestero(SiacDCommissioniestero siacDCommissioniestero) {
		this.siacDCommissioniestero = siacDCommissioniestero;
	}

	public SiacDValuta getSiacDValuta() {
		return this.siacDValuta;
	}

	public void setSiacDValuta(SiacDValuta siacDValuta) {
		this.siacDValuta = siacDValuta;
	}

	public SiacTCartacont getSiacTCartacont() {
		return this.siacTCartacont;
	}

	public void setSiacTCartacont(SiacTCartacont siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
	}

	@Override
	public Integer getUid() {
		return cartacestId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacestId = uid;
	}

}