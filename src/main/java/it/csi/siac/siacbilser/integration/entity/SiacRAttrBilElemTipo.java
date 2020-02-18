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
 * The persistent class for the siac_r_attr_bil_elem_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_bil_elem_tipo")
public class SiacRAttrBilElemTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attr bil elem tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ATTR_BIL_ELEM_TIPO_ATTRBILELEMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTR_BIL_ELEM_TIPO_ATTR_BIL_ELEM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTR_BIL_ELEM_TIPO_ATTRBILELEMTIPOID_GENERATOR")
	@Column(name="attr_bil_elem_tipo_id")
	private Integer attrBilElemTipoId;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	/**
	 * Instantiates a new siac r attr bil elem tipo.
	 */
	public SiacRAttrBilElemTipo() {
	}

	/**
	 * Gets the attr bil elem tipo id.
	 *
	 * @return the attr bil elem tipo id
	 */
	public Integer getAttrBilElemTipoId() {
		return this.attrBilElemTipoId;
	}

	/**
	 * Sets the attr bil elem tipo id.
	 *
	 * @param attrBilElemTipoId the new attr bil elem tipo id
	 */
	public void setAttrBilElemTipoId(Integer attrBilElemTipoId) {
		this.attrBilElemTipoId = attrBilElemTipoId;
	}

	/**
	 * Gets the siac d bil elem tipo.
	 *
	 * @return the siac d bil elem tipo
	 */
	public SiacDBilElemTipo getSiacDBilElemTipo() {
		return this.siacDBilElemTipo;
	}

	/**
	 * Sets the siac d bil elem tipo.
	 *
	 * @param siacDBilElemTipo the new siac d bil elem tipo
	 */
	public void setSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
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
		return attrBilElemTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attrBilElemTipoId = uid;
	}

}