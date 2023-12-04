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

/**
 * The persistent class for the siac_r_bil_elem_tipo_categoria database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_categoria")
public class SiacRBilElemTipoCategoria extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem tipo cat id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_CATEGORIA_ELEMTIPOCATID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_TIPO_CATEGORIA_ELEM_TIPO_CAT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_CATEGORIA_ELEMTIPOCATID_GENERATOR")
	@Column(name="elem_tipo_cat_id")
	private Integer elemTipoCatId;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	//bi-directional many-to-one association to SiacDBilElemCategoria
	/** The siac d class tipo. */
	@ManyToOne
	@JoinColumn(name="elem_cat_id")
	private SiacDBilElemCategoria siacDBilElemCategoria;

	/**
	 * Instantiates a new siac r bil elem tipo categoria.
	 */
	public SiacRBilElemTipoCategoria() {
	}

	/**
	 * @return the elemTipoCatId
	 */
	public Integer getElemTipoCatId() {
		return elemTipoCatId;
	}

	/**
	 * @param elemTipoCatId the elemTipoCatId to set
	 */
	public void setElemTipoCatId(Integer elemTipoCatId) {
		this.elemTipoCatId = elemTipoCatId;
	}

	/**
	 * @return the siacDBilElemTipo
	 */
	public SiacDBilElemTipo getSiacDBilElemTipo() {
		return siacDBilElemTipo;
	}

	/**
	 * @param siacDBilElemTipo the siacDBilElemTipo to set
	 */
	public void setSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
	}

	/**
	 * @return the siacDBilElemCategoria
	 */
	public SiacDBilElemCategoria getSiacDBilElemCategoria() {
		return siacDBilElemCategoria;
	}

	/**
	 * @param siacDBilElemCategoria the siacDBilElemCategoria to set
	 */
	public void setSiacDBilElemCategoria(SiacDBilElemCategoria siacDBilElemCategoria) {
		this.siacDBilElemCategoria = siacDBilElemCategoria;
	}

	@Override
	public Integer getUid() {
		return elemTipoCatId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemTipoCatId = uid;
	}

}