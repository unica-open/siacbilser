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
 * The persistent class for the siac_d_subdoc_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_subdoc_tipo")
@NamedQuery(name="SiacDSubdocTipo.findAll", query="SELECT s FROM SiacDSubdocTipo s")
public class SiacDSubdocTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SUBDOC_TIPO_SUBDOCTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SUBDOC_TIPO_SUBDOC_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SUBDOC_TIPO_SUBDOCTIPOID_GENERATOR")
	@Column(name="subdoc_tipo_id")
	private Integer subdocTipoId;

	/** The subdoc tipo code. */
	@Column(name="subdoc_tipo_code")
	private String subdocTipoCode;

	/** The subdoc tipo desc. */
	@Column(name="subdoc_tipo_desc")
	private String subdocTipoDesc;


	//bi-directional many-to-one association to SiacRSubdocTipoAttr
	/** The siac r subdoc tipo attrs. */
	@OneToMany(mappedBy="siacDSubdocTipo")
	private List<SiacRSubdocTipoAttr> siacRSubdocTipoAttrs;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacDSubdocTipo")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d subdoc tipo.
	 */
	public SiacDSubdocTipo() {
	}

	/**
	 * Gets the subdoc tipo id.
	 *
	 * @return the subdoc tipo id
	 */
	public Integer getSubdocTipoId() {
		return this.subdocTipoId;
	}

	/**
	 * Sets the subdoc tipo id.
	 *
	 * @param subdocTipoId the new subdoc tipo id
	 */
	public void setSubdocTipoId(Integer subdocTipoId) {
		this.subdocTipoId = subdocTipoId;
	}

	/**
	 * Gets the subdoc tipo code.
	 *
	 * @return the subdoc tipo code
	 */
	public String getSubdocTipoCode() {
		return this.subdocTipoCode;
	}

	/**
	 * Sets the subdoc tipo code.
	 *
	 * @param subdocTipoCode the new subdoc tipo code
	 */
	public void setSubdocTipoCode(String subdocTipoCode) {
		this.subdocTipoCode = subdocTipoCode;
	}

	/**
	 * Gets the subdoc tipo desc.
	 *
	 * @return the subdoc tipo desc
	 */
	public String getSubdocTipoDesc() {
		return this.subdocTipoDesc;
	}

	/**
	 * Sets the subdoc tipo desc.
	 *
	 * @param subdocTipoDesc the new subdoc tipo desc
	 */
	public void setSubdocTipoDesc(String subdocTipoDesc) {
		this.subdocTipoDesc = subdocTipoDesc;
	}

	

	/**
	 * Gets the siac r subdoc tipo attrs.
	 *
	 * @return the siac r subdoc tipo attrs
	 */
	public List<SiacRSubdocTipoAttr> getSiacRSubdocTipoAttrs() {
		return this.siacRSubdocTipoAttrs;
	}

	/**
	 * Sets the siac r subdoc tipo attrs.
	 *
	 * @param siacRSubdocTipoAttrs the new siac r subdoc tipo attrs
	 */
	public void setSiacRSubdocTipoAttrs(List<SiacRSubdocTipoAttr> siacRSubdocTipoAttrs) {
		this.siacRSubdocTipoAttrs = siacRSubdocTipoAttrs;
	}

	/**
	 * Adds the siac r subdoc tipo attr.
	 *
	 * @param siacRSubdocTipoAttr the siac r subdoc tipo attr
	 * @return the siac r subdoc tipo attr
	 */
	public SiacRSubdocTipoAttr addSiacRSubdocTipoAttr(SiacRSubdocTipoAttr siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().add(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacDSubdocTipo(this);

		return siacRSubdocTipoAttr;
	}

	/**
	 * Removes the siac r subdoc tipo attr.
	 *
	 * @param siacRSubdocTipoAttr the siac r subdoc tipo attr
	 * @return the siac r subdoc tipo attr
	 */
	public SiacRSubdocTipoAttr removeSiacRSubdocTipoAttr(SiacRSubdocTipoAttr siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().remove(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacDSubdocTipo(null);

		return siacRSubdocTipoAttr;
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
		siacTSubdoc.setSiacDSubdocTipo(this);

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
		siacTSubdoc.setSiacDSubdocTipo(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocTipoId = uid;
		
	}

}