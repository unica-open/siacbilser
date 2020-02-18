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
 * The persistent class for the siac_r_bil_elem_tipo_attr_id_elem_code database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_attr_id_elem_code")
@NamedQuery(name="SiacRBilElemTipoAttrIdElemCode.findAll", query="SELECT s FROM SiacRBilElemTipoAttrIdElemCode s")
public class SiacRBilElemTipoAttrIdElemCode extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1332333265754321L;

	/** The elem tipo attr id elem code. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_ATTR_ID_ELEM_CODE_ELEMTIPOATTRIDELEMCODE_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_tipo_attr_id_el_elem_tipo_attr_id_elem_code_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_ATTR_ID_ELEM_CODE_ELEMTIPOATTRIDELEMCODE_GENERATOR")
	@Column(name="elem_tipo_attr_id_elem_code")
	private Integer elemTipoAttrIdElemCode;
	

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;
		
	
	/** The elem code. */
	@Column(name="elem_code")
	private Integer elemCode;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	

	/**
	 * Instantiates a new siac r bil elem tipo attr id elem code.
	 */
	public SiacRBilElemTipoAttrIdElemCode() {
	}

	/**
	 * Gets the elem tipo attr id elem code.
	 *
	 * @return the elem tipo attr id elem code
	 */
	public Integer getElemTipoAttrIdElemCode() {
		return elemTipoAttrIdElemCode;
	}

	/**
	 * Sets the elem tipo attr id elem code.
	 *
	 * @param elemTipoAttrIdElemCode the new elem tipo attr id elem code
	 */
	public void setElemTipoAttrIdElemCode(Integer elemTipoAttrIdElemCode) {
		this.elemTipoAttrIdElemCode = elemTipoAttrIdElemCode;
	}

	/**
	 * Gets the siac t attr.
	 *
	 * @return the siac t attr
	 */
	public SiacTAttr getSiacTAttr() {
		return siacTAttr;
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
		return elemTipoAttrIdElemCode;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemTipoAttrIdElemCode = uid;
		
	}

}