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
 * The persistent class for the siac_r_soggetto_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_attr")
@NamedQuery(name="SiacRSoggettoAttr.findAll", query="SELECT s FROM SiacRSoggettoAttr s")
public class SiacRSoggettoAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ATTR_SOGGETTOATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_ATTR_SOGGETTO_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ATTR_SOGGETTOATTRID_GENERATOR")
	@Column(name="soggetto_attr_id")
	private Integer soggettoAttrId;

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

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r soggetto attr.
	 */
	public SiacRSoggettoAttr() {
	}

	/**
	 * Gets the soggetto attr id.
	 *
	 * @return the soggetto attr id
	 */
	public Integer getSoggettoAttrId() {
		return this.soggettoAttrId;
	}

	/**
	 * Sets the soggetto attr id.
	 *
	 * @param soggettoAttrId the new soggetto attr id
	 */
	public void setSoggettoAttrId(Integer soggettoAttrId) {
		this.soggettoAttrId = soggettoAttrId;
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
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoAttrId = uid;
		
	}

}