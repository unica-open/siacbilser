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
 * The persistent class for the siac_r_vincolo_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_vincolo_attr")
@NamedQuery(name="SiacRVincoloAttr.findAll", query="SELECT s FROM SiacRVincoloAttr s")
public class SiacRVincoloAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_VINCOLO_ATTR_VINCOLOATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_VINCOLO_ATTR_VINCOLO_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VINCOLO_ATTR_VINCOLOATTRID_GENERATOR")
	@Column(name="vincolo_attr_id")
	private Integer vincoloAttrId;

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

	//bi-directional many-to-one association to SiacTVincolo
	/** The siac t vincolo. */
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincolo siacTVincolo;

	/**
	 * Instantiates a new siac r vincolo attr.
	 */
	public SiacRVincoloAttr() {
	}

	/**
	 * Gets the vincolo attr id.
	 *
	 * @return the vincolo attr id
	 */
	public Integer getVincoloAttrId() {
		return this.vincoloAttrId;
	}

	/**
	 * Sets the vincolo attr id.
	 *
	 * @param vincoloAttrId the new vincolo attr id
	 */
	public void setVincoloAttrId(Integer vincoloAttrId) {
		this.vincoloAttrId = vincoloAttrId;
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
	 * Gets the siac t vincolo.
	 *
	 * @return the siac t vincolo
	 */
	public SiacTVincolo getSiacTVincolo() {
		return this.siacTVincolo;
	}

	/**
	 * Sets the siac t vincolo.
	 *
	 * @param siacTVincolo the new siac t vincolo
	 */
	public void setSiacTVincolo(SiacTVincolo siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloAttrId = uid;
		
	}

}