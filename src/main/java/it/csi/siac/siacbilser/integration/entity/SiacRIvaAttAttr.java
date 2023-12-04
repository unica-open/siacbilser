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
 * The persistent class for the siac_r_iva_att_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_att_attr")
@NamedQuery(name="SiacRIvaAttAttr.findAll", query="SELECT s FROM SiacRIvaAttAttr s")
public class SiacRIvaAttAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaatt attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVA_ATT_ATTR_IVAATTATTRID_GENERATOR", sequenceName="SIAC_R_IVA_ATT_ATTR_IVAATT_ATTR_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_ATT_ATTR_IVAATTATTRID_GENERATOR")
	@Column(name="ivaatt_attr_id")
	private Integer ivaattAttrId;

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

	//bi-directional many-to-one association to SiacTIvaAttivita
	/** The siac t iva attivita. */
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivita siacTIvaAttivita;

	/**
	 * Instantiates a new siac r iva att attr.
	 */
	public SiacRIvaAttAttr() {
	}

	/**
	 * Gets the ivaatt attr id.
	 *
	 * @return the ivaatt attr id
	 */
	public Integer getIvaattAttrId() {
		return this.ivaattAttrId;
	}

	/**
	 * Sets the ivaatt attr id.
	 *
	 * @param ivaattAttrId the new ivaatt attr id
	 */
	public void setIvaattAttrId(Integer ivaattAttrId) {
		this.ivaattAttrId = ivaattAttrId;
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
	 * Gets the siac t iva attivita.
	 *
	 * @return the siac t iva attivita
	 */
	public SiacTIvaAttivita getSiacTIvaAttivita() {
		return this.siacTIvaAttivita;
	}

	/**
	 * Sets the siac t iva attivita.
	 *
	 * @param siacTIvaAttivita the new siac t iva attivita
	 */
	public void setSiacTIvaAttivita(SiacTIvaAttivita siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaattAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaattAttrId = uid;
	}
}