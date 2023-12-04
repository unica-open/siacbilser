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
 * The persistent class for the siac_t_cartacont_det database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont_det")
@NamedQuery(name="SiacTCartacontDet.findAll", query="SELECT s FROM SiacTCartacontDet s")
public class SiacTCartacontDet extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_DET_CARTACDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CARTACONT_DET_CARTAC_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_DET_CARTACDETID_GENERATOR")
	@Column(name="cartac_det_id")
	private Integer cartacDetId;

	@Column(name="cartac_det_data")
	private Date cartacDetData;

	@Column(name="cartac_det_desc")
	private String cartacDetDesc;

	@Column(name="cartac_det_importo")
	private BigDecimal cartacDetImporto;

	@Column(name="cartac_det_importo_valuta")
	private BigDecimal cartacDetImportoValuta;

	@Column(name="cartac_det_numero")
	private Integer cartacDetNumero;

	//bi-directional many-to-one association to SiacRCartacontDetAttr
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetAttr> siacRCartacontDetAttrs;

	//bi-directional many-to-one association to SiacRCartacontDetModpag
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetModpag> siacRCartacontDetModpags;

	//bi-directional many-to-one association to SiacRCartacontDetMovgestT
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetMovgestT> siacRCartacontDetMovgestTs;

	//bi-directional many-to-one association to SiacRCartacontDetSoggetto
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetSoggetto> siacRCartacontDetSoggettos;

	//bi-directional many-to-one association to SiacRCartacontDetSubdoc
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetSubdoc> siacRCartacontDetSubdocs;

	//bi-directional many-to-one association to SiacDContotesoreria
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreria siacDContotesoreria;

	//bi-directional many-to-one association to SiacTCartacont
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacont siacTCartacont;

	public SiacTCartacontDet() {
	}

	public Integer getCartacDetId() {
		return this.cartacDetId;
	}

	public void setCartacDetId(Integer cartacDetId) {
		this.cartacDetId = cartacDetId;
	}

	public Date getCartacDetData() {
		return this.cartacDetData;
	}

	public void setCartacDetData( Date cartacDetData) {
		this.cartacDetData = cartacDetData;
	}

	public String getCartacDetDesc() {
		return this.cartacDetDesc;
	}

	public void setCartacDetDesc(String cartacDetDesc) {
		this.cartacDetDesc = cartacDetDesc;
	}

	public BigDecimal getCartacDetImporto() {
		return this.cartacDetImporto;
	}

	public void setCartacDetImporto(BigDecimal cartacDetImporto) {
		this.cartacDetImporto = cartacDetImporto;
	}

	public BigDecimal getCartacDetImportoValuta() {
		return this.cartacDetImportoValuta;
	}

	public void setCartacDetImportoValuta(BigDecimal cartacDetImportoValuta) {
		this.cartacDetImportoValuta = cartacDetImportoValuta;
	}

	public Integer getCartacDetNumero() {
		return this.cartacDetNumero;
	}

	public void setCartacDetNumero(Integer cartacDetNumero) {
		this.cartacDetNumero = cartacDetNumero;
	}

	public List<SiacRCartacontDetAttr> getSiacRCartacontDetAttrs() {
		return this.siacRCartacontDetAttrs;
	}

	public void setSiacRCartacontDetAttrs(List<SiacRCartacontDetAttr> siacRCartacontDetAttrs) {
		this.siacRCartacontDetAttrs = siacRCartacontDetAttrs;
	}

	public SiacRCartacontDetAttr addSiacRCartacontDetAttr(SiacRCartacontDetAttr siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().add(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTCartacontDet(this);

		return siacRCartacontDetAttr;
	}

	public SiacRCartacontDetAttr removeSiacRCartacontDetAttr(SiacRCartacontDetAttr siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().remove(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTCartacontDet(null);

		return siacRCartacontDetAttr;
	}

	public List<SiacRCartacontDetModpag> getSiacRCartacontDetModpags() {
		return this.siacRCartacontDetModpags;
	}

	public void setSiacRCartacontDetModpags(List<SiacRCartacontDetModpag> siacRCartacontDetModpags) {
		this.siacRCartacontDetModpags = siacRCartacontDetModpags;
	}

	public SiacRCartacontDetModpag addSiacRCartacontDetModpag(SiacRCartacontDetModpag siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().add(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTCartacontDet(this);

		return siacRCartacontDetModpag;
	}

	public SiacRCartacontDetModpag removeSiacRCartacontDetModpag(SiacRCartacontDetModpag siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().remove(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTCartacontDet(null);

		return siacRCartacontDetModpag;
	}

	public List<SiacRCartacontDetMovgestT> getSiacRCartacontDetMovgestTs() {
		return this.siacRCartacontDetMovgestTs;
	}

	public void setSiacRCartacontDetMovgestTs(List<SiacRCartacontDetMovgestT> siacRCartacontDetMovgestTs) {
		this.siacRCartacontDetMovgestTs = siacRCartacontDetMovgestTs;
	}

	public SiacRCartacontDetMovgestT addSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestT siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().add(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTCartacontDet(this);

		return siacRCartacontDetMovgestT;
	}

	public SiacRCartacontDetMovgestT removeSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestT siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().remove(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTCartacontDet(null);

		return siacRCartacontDetMovgestT;
	}

	public List<SiacRCartacontDetSoggetto> getSiacRCartacontDetSoggettos() {
		return this.siacRCartacontDetSoggettos;
	}

	public void setSiacRCartacontDetSoggettos(List<SiacRCartacontDetSoggetto> siacRCartacontDetSoggettos) {
		this.siacRCartacontDetSoggettos = siacRCartacontDetSoggettos;
	}

	public SiacRCartacontDetSoggetto addSiacRCartacontDetSoggetto(SiacRCartacontDetSoggetto siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().add(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTCartacontDet(this);

		return siacRCartacontDetSoggetto;
	}

	public SiacRCartacontDetSoggetto removeSiacRCartacontDetSoggetto(SiacRCartacontDetSoggetto siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().remove(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTCartacontDet(null);

		return siacRCartacontDetSoggetto;
	}

	public List<SiacRCartacontDetSubdoc> getSiacRCartacontDetSubdocs() {
		return this.siacRCartacontDetSubdocs;
	}

	public void setSiacRCartacontDetSubdocs(List<SiacRCartacontDetSubdoc> siacRCartacontDetSubdocs) {
		this.siacRCartacontDetSubdocs = siacRCartacontDetSubdocs;
	}

	public SiacRCartacontDetSubdoc addSiacRCartacontDetSubdoc(SiacRCartacontDetSubdoc siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().add(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTCartacontDet(this);

		return siacRCartacontDetSubdoc;
	}

	public SiacRCartacontDetSubdoc removeSiacRCartacontDetSubdoc(SiacRCartacontDetSubdoc siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().remove(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTCartacontDet(null);

		return siacRCartacontDetSubdoc;
	}

	public SiacDContotesoreria getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	public void setSiacDContotesoreria(SiacDContotesoreria siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	public SiacTCartacont getSiacTCartacont() {
		return this.siacTCartacont;
	}

	public void setSiacTCartacont(SiacTCartacont siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
	}

	@Override
	public Integer getUid() {
		return cartacDetId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cartacDetId = uid;
		
	}

}