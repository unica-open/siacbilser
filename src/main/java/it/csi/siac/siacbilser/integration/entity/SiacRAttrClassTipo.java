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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_attr_class_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_class_tipo")
public class SiacRAttrClassTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attr class tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ATTR_CLASS_TIPO_ATTRCLASSTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTR_CLASS_TIPO_ATTR_CLASS_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTR_CLASS_TIPO_ATTRCLASSTIPOID_GENERATOR")
	@Column(name="attr_class_tipo_id")
	private Integer attrClassTipoId;

	//bi-directional many-to-one association to SiacDClassTipo
	/** The siac d class tipo. */
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	/**
	 * Instantiates a new siac r attr class tipo.
	 */
	public SiacRAttrClassTipo() {
	}

	/**
	 * Gets the attr class tipo id.
	 *
	 * @return the attr class tipo id
	 */
	public Integer getAttrClassTipoId() {
		return this.attrClassTipoId;
	}

	/**
	 * Sets the attr class tipo id.
	 *
	 * @param attrClassTipoId the new attr class tipo id
	 */
	public void setAttrClassTipoId(Integer attrClassTipoId) {
		this.attrClassTipoId = attrClassTipoId;
	}

	/**
	 * Gets the siac d class tipo.
	 *
	 * @return the siac d class tipo
	 */
	public SiacDClassTipo getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	/**
	 * Sets the siac d class tipo.
	 *
	 * @param siacDClassTipo the new siac d class tipo
	 */
	public void setSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
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
		return attrClassTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attrClassTipoId = uid;
	}

}