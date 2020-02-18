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
 * The persistent class for the siac_r_doc_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_attr")
@NamedQuery(name="SiacRDocAttr.findAll", query="SELECT s FROM SiacRDocAttr s")
public class SiacRDocAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_ATTR_DOCATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_ATTR_DOC_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_ATTR_DOCATTRID_GENERATOR")
	@Column(name="doc_attr_id")
	private Integer docAttrId;

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

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	/**
	 * Instantiates a new siac r doc attr.
	 */
	public SiacRDocAttr() {
	}

	/**
	 * Gets the doc attr id.
	 *
	 * @return the doc attr id
	 */
	public Integer getDocAttrId() {
		return this.docAttrId;
	}

	/**
	 * Sets the doc attr id.
	 *
	 * @param docAttrId the new doc attr id
	 */
	public void setDocAttrId(Integer docAttrId) {
		this.docAttrId = docAttrId;
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
	 * Gets the siac t doc.
	 *
	 * @return the siac t doc
	 */
	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	/**
	 * Sets the siac t doc.
	 *
	 * @param siacTDoc the new siac t doc
	 */
	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docAttrId = uid;
		
	}

	

}