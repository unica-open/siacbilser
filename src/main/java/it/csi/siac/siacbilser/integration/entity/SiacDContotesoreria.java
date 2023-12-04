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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_contotesoreria database table.
 * 
 */
@Entity
@Table(name="siac_d_contotesoreria")
@NamedQuery(name="SiacDContotesoreria.findAll", query="SELECT s FROM SiacDContotesoreria s")
public class SiacDContotesoreria extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The contotes id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CONTOTESORERIA_CONTOTESID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CONTOTESORERIA_CONTOTES_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CONTOTESORERIA_CONTOTESID_GENERATOR")
	@Column(name="contotes_id")
	private Integer contotesId;

	/** The contotes code. */
	@Column(name="contotes_code")
	private String contotesCode;

	/** The contotes desc. */
	@Column(name="contotes_desc")
	private String contotesDesc;

	@Column(name="vincolato")
	private Boolean vincolato;

	@Column(name="per_ripianamento")
	private Boolean perRipianamento;

	//bi-directional many-to-one association to SiacTCartacontDet
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTCartacontDet> siacTCartacontDets;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidaziones. */
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTLiquidazione> siacTLiquidaziones;

	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTOrdinativo> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTPredoc
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTPredoc> siacTPredocs;
	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d contotesoreria.
	 */
	public SiacDContotesoreria() {
	}

	/**
	 * Gets the contotes id.
	 *
	 * @return the contotes id
	 */
	public Integer getContotesId() {
		return this.contotesId;
	}

	/**
	 * Sets the contotes id.
	 *
	 * @param contotesId the new contotes id
	 */
	public void setContotesId(Integer contotesId) {
		this.contotesId = contotesId;
	}

	/**
	 * Gets the contotes code.
	 *
	 * @return the contotes code
	 */
	public String getContotesCode() {
		return this.contotesCode;
	}

	/**
	 * Sets the contotes code.
	 *
	 * @param contotesCode the new contotes code
	 */
	public void setContotesCode(String contotesCode) {
		this.contotesCode = contotesCode;
	}

	/**
	 * Gets the contotes desc.
	 *
	 * @return the contotes desc
	 */
	public String getContotesDesc() {
		return this.contotesDesc;
	}

	/**
	 * Sets the contotes desc.
	 *
	 * @param contotesDesc the new contotes desc
	 */
	public void setContotesDesc(String contotesDesc) {
		this.contotesDesc = contotesDesc;
	}

	/**
	 * Gets the siac t liquidaziones.
	 *
	 * @return the siac t liquidaziones
	 */
	public List<SiacTCartacontDet> getSiacTCartacontDets() {
		return this.siacTCartacontDets;
	}

	public void setSiacTCartacontDets(List<SiacTCartacontDet> siacTCartacontDets) {
		this.siacTCartacontDets = siacTCartacontDets;
	}

	public SiacTCartacontDet addSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		getSiacTCartacontDets().add(siacTCartacontDet);
		siacTCartacontDet.setSiacDContotesoreria(this);

		return siacTCartacontDet;
	}

	public SiacTCartacontDet removeSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		getSiacTCartacontDets().remove(siacTCartacontDet);
		siacTCartacontDet.setSiacDContotesoreria(null);

		return siacTCartacontDet;
	}

	public List<SiacTLiquidazione> getSiacTLiquidaziones() {
		return this.siacTLiquidaziones;
	}

	/**
	 * Sets the siac t liquidaziones.
	 *
	 * @param siacTLiquidaziones the new siac t liquidaziones
	 */
	public void setSiacTLiquidaziones(List<SiacTLiquidazione> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}

	/**
	 * Adds the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione addSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacDContotesoreria(this);

		return siacTLiquidazione;
	}

	/**
	 * Removes the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione removeSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacDContotesoreria(null);

		return siacTLiquidazione;
	}

	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDContotesoreria(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDContotesoreria(null);

		return siacTOrdinativo;
	}

	public List<SiacTPredoc> getSiacTPredocs() {
		return this.siacTPredocs;
	}

	public void setSiacTPredocs(List<SiacTPredoc> siacTPredocs) {
		this.siacTPredocs = siacTPredocs;
	}

	public SiacTPredoc addSiacTPredoc(SiacTPredoc siacTPredoc) {
		getSiacTPredocs().add(siacTPredoc);
		siacTPredoc.setSiacDContotesoreria(this);

		return siacTPredoc;
	}

	public SiacTPredoc removeSiacTPredoc(SiacTPredoc siacTPredoc) {
		getSiacTPredocs().remove(siacTPredoc);
		siacTPredoc.setSiacDContotesoreria(null);

		return siacTPredoc;
	}

	/**
	 * Gets the siac t subdocs.
	 *
	 * @return the siac t subdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 *
	 * @param siacTSubdocs the new siac t subdocs
	 */
	public void setSiacTSubdocs(List<SiacTSubdoc> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	/**
	 * Adds the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc addSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDContotesoreria(this);

		return siacTSubdoc;
	}

	/**
	 * Removes the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc removeSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDContotesoreria(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return contotesId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.contotesId = uid;
		
	}

	public Boolean getVincolato() {
		return vincolato;
	}

	public void setVincolato(Boolean vincolato) {
		this.vincolato = vincolato;
	}

	public Boolean getPerRipianamento() {
		return perRipianamento;
	}

	public void setPerRipianamento(Boolean perRipianamento) {
		this.perRipianamento = perRipianamento;
	}

}