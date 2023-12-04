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
 * The persistent class for the siac_r_doc_tipo_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_tipo_attr")
@NamedQuery(name="SiacRDocTipoAttr.findAll", query="SELECT s FROM SiacRDocTipoAttr s")
public class SiacRDocTipoAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc tipo attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_TIPO_ATTR_DOCTIPOATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_TIPO_ATTR_DOC_TIPO_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_TIPO_ATTR_DOCTIPOATTRID_GENERATOR")
	@Column(name="doc_tipo_attr_id")
	private Integer docTipoAttrId;

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

	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipo. */
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipo siacDDocTipo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;


	/**
	 * Instantiates a new siac r doc tipo attr.
	 */
	public SiacRDocTipoAttr() {
	}

	/**
	 * Gets the doc tipo attr id.
	 *
	 * @return the doc tipo attr id
	 */
	public Integer getDocTipoAttrId() {
		return this.docTipoAttrId;
	}

	/**
	 * Sets the doc tipo attr id.
	 *
	 * @param docTipoAttrId the new doc tipo attr id
	 */
	public void setDocTipoAttrId(Integer docTipoAttrId) {
		this.docTipoAttrId = docTipoAttrId;
	}

	/**
	 * Gets the siac d doc tipo.
	 *
	 * @return the siac d doc tipo
	 */
	public SiacDDocTipo getSiacDDocTipo() {
		return this.siacDDocTipo;
	}

	/**
	 * Sets the siac d doc tipo.
	 *
	 * @param siacDDocTipo the new siac d doc tipo
	 */
	public void setSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
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
	 * Gets the boolean_.
	 *
	 * @return the boolean_
	 */
	public String getBoolean_() {
		return boolean_;
	}

	/**
	 * Sets the boolean_.
	 *
	 * @param boolean_ the boolean_ to set
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
		return numerico;
	}

	/**
	 * Sets the numerico.
	 *
	 * @param numerico the numerico to set
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
		return percentuale;
	}

	/**
	 * Sets the percentuale.
	 *
	 * @param percentuale the percentuale to set
	 */
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	/**
	 * Gets the tabella id.
	 *
	 * @return the tabellaId
	 */
	public Integer getTabellaId() {
		return tabellaId;
	}

	/**
	 * Sets the tabella id.
	 *
	 * @param tabellaId the tabellaId to set
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
		return testo;
	}

	/**
	 * Sets the testo.
	 *
	 * @param testo the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docTipoAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docTipoAttrId = uid;		
	}

}