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
 * The persistent class for the siac_r_bil_elem_tipo_class_tip database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_class_tip")
public class SiacRBilElemTipoClassTip extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem tipo classif tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEMTIPOCLASSIFTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEM_TIPO_CLASSIF_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEMTIPOCLASSIFTIPOID_GENERATOR")
	@Column(name="elem_tipo_classif_tipo_id")
	private Integer elemTipoClassifTipoId;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	//bi-directional many-to-one association to SiacDClassTipo
	/** The siac d class tipo. */
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	/**
	 * Instantiates a new siac r bil elem tipo class tip.
	 */
	public SiacRBilElemTipoClassTip() {
	}

	/**
	 * Gets the elem tipo classif tipo id.
	 *
	 * @return the elem tipo classif tipo id
	 */
	public Integer getElemTipoClassifTipoId() {
		return this.elemTipoClassifTipoId;
	}

	/**
	 * Sets the elem tipo classif tipo id.
	 *
	 * @param elemTipoClassifTipoId the new elem tipo classif tipo id
	 */
	public void setElemTipoClassifTipoId(Integer elemTipoClassifTipoId) {
		this.elemTipoClassifTipoId = elemTipoClassifTipoId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemTipoClassifTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemTipoClassifTipoId = uid;
	}

}