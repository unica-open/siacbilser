/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_subdoc_iva_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_iva_attr")
@NamedQuery(name="SiacRSubdocIvaAttr.findAll", query="SELECT s FROM SiacRSubdocIvaAttr s")
public class SiacRSubdocIvaAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdociva attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_IVA_ATTR_SUBDOCIVAATTRID_GENERATOR", sequenceName="SIAC_R_SUBDOC_IVA_ATTR_SUBDOCIVA_ATTR_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_IVA_ATTR_SUBDOCIVAATTRID_GENERATOR")
	@Column(name="subdociva_attr_id")
	private Integer subdocivaAttrId;

	/** The boolean_. */
	@Column(name="boolean")
	private String boolean_;

	/** The numerico. */
	private BigDecimal numerico;

	/** The percentuale. */
	private BigDecimal percentuale;

	/** The tabella id. */
	@Column(name="tabella_id")
	private Integer tabellaId;

	/** The testo. */
	private String testo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva. */
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIva siacTSubdocIva;

	/**
	 * Instantiates a new siac r subdoc iva attr.
	 */
	public SiacRSubdocIvaAttr() {
	}

	/**
	 * Gets the subdociva attr id.
	 *
	 * @return the subdociva attr id
	 */
	public Integer getSubdocivaAttrId() {
		return this.subdocivaAttrId;
	}

	/**
	 * Sets the subdociva attr id.
	 *
	 * @param subdocivaAttrId the new subdociva attr id
	 */
	public void setSubdocivaAttrId(Integer subdocivaAttrId) {
		this.subdocivaAttrId = subdocivaAttrId;
	}

	/**
	 * Gets the boolean_.
	 *
	 * @return the boolean_
	 */
	public String getBoolean_() {
		return this.boolean_;
	}

	/**
	 * Sets the boolean_.
	 *
	 * @param boolean_ the new boolean_
	 */
	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}

	/**
	 * Gets the numerico.
	 *
	 * @return the numerico
	 */
	public BigDecimal getNumerico() {
		return this.numerico;
	}

	/**
	 * Sets the numerico.
	 *
	 * @param numerico the new numerico
	 */
	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	/**
	 * Gets the percentuale.
	 *
	 * @return the percentuale
	 */
	public BigDecimal getPercentuale() {
		return this.percentuale;
	}

	/**
	 * Sets the percentuale.
	 *
	 * @param percentuale the new percentuale
	 */
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	/**
	 * Gets the tabella id.
	 *
	 * @return the tabella id
	 */
	public Integer getTabellaId() {
		return this.tabellaId;
	}

	/**
	 * Sets the tabella id.
	 *
	 * @param tabellaId the new tabella id
	 */
	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	/**
	 * Gets the testo.
	 *
	 * @return the testo
	 */
	public String getTesto() {
		return this.testo;
	}

	/**
	 * Sets the testo.
	 *
	 * @param testo the new testo
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/**
	 * Gets the siac t attr.
	 *
	 * @return the siac t attr
	 */
	public SiacTAttr getSiacTAttr() {
		return this.siacTAttr;
	}

	/**
	 * Sets the siac t attr.
	 *
	 * @param siacTAttr the new siac t attr
	 */
	public void setSiacTAttr(SiacTAttr siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	/**
	 * Gets the siac t subdoc iva.
	 *
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	/**
	 * Sets the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the new siac t subdoc iva
	 */
	public void setSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocivaAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocivaAttrId = uid;
	}

}