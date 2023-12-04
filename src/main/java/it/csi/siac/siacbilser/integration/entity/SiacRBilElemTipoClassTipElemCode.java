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
 * The persistent class for the siac_r_bil_elem_tipo_class_tip_elem_code database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_class_tip_elem_code")
@NamedQuery(name="SiacRBilElemTipoClassTipElemCode.findAll", query="SELECT s FROM SiacRBilElemTipoClassTipElemCode s")
public class SiacRBilElemTipoClassTipElemCode extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 133233326578323L;

	/** The elem tipo class tip elem code. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEM_CODE_ELEMTIPOCLASSTIPELEMCODE_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_tipo_class_ti_elem_tipo_class_tip_elem_code_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEM_CODE_ELEMTIPOCLASSTIPELEMCODE_GENERATOR")
	@Column(name="elem_tipo_class_tip_elem_code")
	private Integer elemTipoClassTipElemCode;
	
	//bi-directional many-to-one association to SiacDClassTipo
	/** The siac d class tipo. */
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	/** The elem code. */
	@Column(name="elem_code")
	private Integer elemCode;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	

	/**
	 * Instantiates a new siac r bil elem tipo class tip elem code.
	 */
	public SiacRBilElemTipoClassTipElemCode() {
	}

	/**
	 * Gets the elem tipo class tip elem code.
	 *
	 * @return the elem tipo class tip elem code
	 */
	public Integer getElemTipoClassTipElemCode() {
		return this.elemTipoClassTipElemCode;
	}

	/**
	 * Sets the elem tipo class tip elem code.
	 *
	 * @param elemTipoClassTipElemCode the new elem tipo class tip elem code
	 */
	public void setElemTipoClassTipElemCode(Integer elemTipoClassTipElemCode) {
		this.elemTipoClassTipElemCode = elemTipoClassTipElemCode;
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
	 * Gets the siac d class tipo.
	 *
	 * @return the siacDClassTipo
	 */
	public SiacDClassTipo getSiacDClassTipo() {
		return siacDClassTipo;
	}

	/**
	 * Sets the siac d class tipo.
	 *
	 * @param siacDClassTipo the siacDClassTipo to set
	 */
	public void setSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	/**
	 * Gets the siac d bil elem tipo.
	 *
	 * @return the siacDBilElemTipo
	 */
	public SiacDBilElemTipo getSiacDBilElemTipo() {
		return siacDBilElemTipo;
	}

	/**
	 * Sets the siac d bil elem tipo.
	 *
	 * @param siacDBilElemTipo the siacDBilElemTipo to set
	 */
	public void setSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemTipoClassTipElemCode;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemTipoClassTipElemCode = uid;
		
	}

}