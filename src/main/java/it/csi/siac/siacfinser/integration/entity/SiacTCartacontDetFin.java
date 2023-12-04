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
 * The persistent class for the siac_t_cartacont_det database table.
 * 
 */
@Entity
@Table(name="siac_t_cartacont_det")
public class SiacTCartacontDetFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CARTACONT_DET_CARTACONT_DET_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_cartacont_det_cartac_det_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CARTACONT_DET_CARTACONT_DET_ID_GENERATOR")
	@Column(name="cartac_det_id")
	private Integer cartacDetId;

	@Column(name="cartac_det_data")
	private Timestamp cartacDetData;

	@Column(name="cartac_det_desc")
	private String cartacDetDesc;

	@Column(name="cartac_det_importo")
	private BigDecimal cartacDetImporto;

	@Column(name="cartac_det_importo_valuta")
	private BigDecimal cartacDetImportoValuta;

	@Column(name="cartac_det_numero")
	private Integer cartacDetNumero;

	//bi-directional many-to-one association to SiacRCartacontDetAttrFin
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetAttrFin> siacRCartacontDetAttrs;

	//bi-directional many-to-one association to SiacRCartacontDetModpagFin
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetModpagFin> siacRCartacontDetModpags;

	//bi-directional many-to-one association to SiacRCartacontDetMovgestTFin
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetMovgestTFin> siacRCartacontDetMovgestTs;

	//bi-directional many-to-one association to SiacRCartacontDetSoggettoFin
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetSoggettoFin> siacRCartacontDetSoggettos;

	//bi-directional many-to-one association to SiacRCartacontDetSubdocFin
	@OneToMany(mappedBy="siacTCartacontDet")
	private List<SiacRCartacontDetSubdocFin> siacRCartacontDetSubdocs;

	//bi-directional many-to-one association to SiacDContotesoreriaFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContotesoreria;

	//bi-directional many-to-one association to SiacTCartacontFin
	@ManyToOne
	@JoinColumn(name="cartac_id")
	private SiacTCartacontFin siacTCartacont;

	public SiacTCartacontDetFin() {
	}

	public Integer getCartacDetId() {
		return this.cartacDetId;
	}

	public void setCartacDetId(Integer cartacDetId) {
		this.cartacDetId = cartacDetId;
	}

	public Timestamp getCartacDetData() {
		return this.cartacDetData;
	}

	public void setCartacDetData(Timestamp cartacDetData) {
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

	public List<SiacRCartacontDetAttrFin> getSiacRCartacontDetAttrs() {
		return this.siacRCartacontDetAttrs;
	}

	public void setSiacRCartacontDetAttrs(List<SiacRCartacontDetAttrFin> siacRCartacontDetAttrs) {
		this.siacRCartacontDetAttrs = siacRCartacontDetAttrs;
	}

	public SiacRCartacontDetAttrFin addSiacRCartacontDetAttr(SiacRCartacontDetAttrFin siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().add(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTCartacontDet(this);

		return siacRCartacontDetAttr;
	}

	public SiacRCartacontDetAttrFin removeSiacRCartacontDetAttr(SiacRCartacontDetAttrFin siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().remove(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTCartacontDet(null);

		return siacRCartacontDetAttr;
	}

	public List<SiacRCartacontDetModpagFin> getSiacRCartacontDetModpags() {
		return this.siacRCartacontDetModpags;
	}

	public void setSiacRCartacontDetModpags(List<SiacRCartacontDetModpagFin> siacRCartacontDetModpags) {
		this.siacRCartacontDetModpags = siacRCartacontDetModpags;
	}

	public SiacRCartacontDetModpagFin addSiacRCartacontDetModpag(SiacRCartacontDetModpagFin siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().add(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTCartacontDet(this);

		return siacRCartacontDetModpag;
	}

	public SiacRCartacontDetModpagFin removeSiacRCartacontDetModpag(SiacRCartacontDetModpagFin siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().remove(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTCartacontDet(null);

		return siacRCartacontDetModpag;
	}

	public List<SiacRCartacontDetMovgestTFin> getSiacRCartacontDetMovgestTs() {
		return this.siacRCartacontDetMovgestTs;
	}

	public void setSiacRCartacontDetMovgestTs(List<SiacRCartacontDetMovgestTFin> siacRCartacontDetMovgestTs) {
		this.siacRCartacontDetMovgestTs = siacRCartacontDetMovgestTs;
	}

	public SiacRCartacontDetMovgestTFin addSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().add(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTCartacontDet(this);

		return siacRCartacontDetMovgestT;
	}

	public SiacRCartacontDetMovgestTFin removeSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestTFin siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().remove(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTCartacontDet(null);

		return siacRCartacontDetMovgestT;
	}

	public List<SiacRCartacontDetSoggettoFin> getSiacRCartacontDetSoggettos() {
		return this.siacRCartacontDetSoggettos;
	}

	public void setSiacRCartacontDetSoggettos(List<SiacRCartacontDetSoggettoFin> siacRCartacontDetSoggettos) {
		this.siacRCartacontDetSoggettos = siacRCartacontDetSoggettos;
	}

	public SiacRCartacontDetSoggettoFin addSiacRCartacontDetSoggetto(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().add(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTCartacontDet(this);

		return siacRCartacontDetSoggetto;
	}

	public SiacRCartacontDetSoggettoFin removeSiacRCartacontDetSoggetto(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().remove(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTCartacontDet(null);

		return siacRCartacontDetSoggetto;
	}

	public List<SiacRCartacontDetSubdocFin> getSiacRCartacontDetSubdocs() {
		return this.siacRCartacontDetSubdocs;
	}

	public void setSiacRCartacontDetSubdocs(List<SiacRCartacontDetSubdocFin> siacRCartacontDetSubdocs) {
		this.siacRCartacontDetSubdocs = siacRCartacontDetSubdocs;
	}

	public SiacRCartacontDetSubdocFin addSiacRCartacontDetSubdoc(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().add(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTCartacontDet(this);

		return siacRCartacontDetSubdoc;
	}

	public SiacRCartacontDetSubdocFin removeSiacRCartacontDetSubdoc(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().remove(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTCartacontDet(null);

		return siacRCartacontDetSubdoc;
	}

	public SiacDContotesoreriaFin getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	public void setSiacDContotesoreria(SiacDContotesoreriaFin siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
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
		return this.cartacDetId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.cartacDetId = uid;
	}

}