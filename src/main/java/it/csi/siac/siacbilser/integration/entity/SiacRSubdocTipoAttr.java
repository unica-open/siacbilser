/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_r_subdoc_tipo_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_tipo_attr")
@NamedQuery(name="SiacRSubdocTipoAttr.findAll", query="SELECT s FROM SiacRSubdocTipoAttr s")
public class SiacRSubdocTipoAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc tipo attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_TIPO_ATTR_SUBDOCTIPOATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_TIPO_ATTR_SUBDOC_TIPO_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_TIPO_ATTR_SUBDOCTIPOATTRID_GENERATOR")
	@Column(name="subdoc_tipo_attr_id")
	private Integer subdocTipoAttrId;

	/** The elem code. */
	@Column(name="elem_code")
	private Integer elemCode;

	//bi-directional many-to-one association to SiacDSubdocTipo
	/** The siac d subdoc tipo. */
	@ManyToOne
	@JoinColumn(name="subdoc_tipo_id")
	private SiacDSubdocTipo siacDSubdocTipo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;


	/**
	 * Instantiates a new siac r subdoc tipo attr.
	 */
	public SiacRSubdocTipoAttr() {
	}

	/**
	 * Gets the subdoc tipo attr id.
	 *
	 * @return the subdoc tipo attr id
	 */
	public Integer getSubdocTipoAttrId() {
		return this.subdocTipoAttrId;
	}

	/**
	 * Sets the subdoc tipo attr id.
	 *
	 * @param subdocTipoAttrId the new subdoc tipo attr id
	 */
	public void setSubdocTipoAttrId(Integer subdocTipoAttrId) {
		this.subdocTipoAttrId = subdocTipoAttrId;
	}

	/**
	 * Gets the elem code.
	 *
	 * @return the elem code
	 */
	public Integer getElemCode() {
		return this.elemCode;
	}

	/**
	 * Sets the elem code.
	 *
	 * @param elemCode the new elem code
	 */
	public void setElemCode(Integer elemCode) {
		this.elemCode = elemCode;
	}

	/**
	 * Gets the siac d subdoc tipo.
	 *
	 * @return the siac d subdoc tipo
	 */
	public SiacDSubdocTipo getSiacDSubdocTipo() {
		return this.siacDSubdocTipo;
	}

	/**
	 * Sets the siac d subdoc tipo.
	 *
	 * @param siacDSubdocTipo the new siac d subdoc tipo
	 */
	public void setSiacDSubdocTipo(SiacDSubdocTipo siacDSubdocTipo) {
		this.siacDSubdocTipo = siacDSubdocTipo;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocTipoAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocTipoAttrId = uid;
	}

}