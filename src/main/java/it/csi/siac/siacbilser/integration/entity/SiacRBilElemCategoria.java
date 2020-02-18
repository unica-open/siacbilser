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
 * The persistent class for the siac_r_bil_elem_categoria database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_categoria")
public class SiacRBilElemCategoria extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil elem stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_STATO_BILELEMSTATOID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_categoria_bil_elem_r_cat_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_STATO_BILELEMSTATOID_GENERATOR")
	@Column(name="bil_elem_r_cat_id")
	private Integer bilElemStatoId;

	//bi-directional many-to-one association to SiacDBilElemCategoria
	/** The siac d bil elem stato. */
	@ManyToOne
	@JoinColumn(name="elem_cat_id")
	private SiacDBilElemCategoria siacDBilElemCategoria;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;


	/**
	 * Instantiates a new siac r bil elem stato.
	 */
	public SiacRBilElemCategoria() {
	}

	/**
	 * Gets the bil elem stato id.
	 *
	 * @return the bil elem stato id
	 */
	public Integer getBilElemStatoId() {
		return this.bilElemStatoId;
	}

	/**
	 * Sets the bil elem stato id.
	 *
	 * @param bilElemStatoId the new bil elem stato id
	 */
	public void setBilElemStatoId(Integer bilElemStatoId) {
		this.bilElemStatoId = bilElemStatoId;
	}

	/**
	 * Gets the siac d bil elem stato.
	 *
	 * @return the siac d bil elem stato
	 */
	public SiacDBilElemCategoria getSiacDBilElemCategoria() {
		return this.siacDBilElemCategoria;
	}

	/**
	 * Sets the siac d bil elem stato.
	 *
	 * @param siacDBilElemCategoria the new siac d bil elem stato
	 */
	public void setSiacDBilElemCategoria(SiacDBilElemCategoria siacDBilElemCategoria) {
		this.siacDBilElemCategoria = siacDBilElemCategoria;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilElemStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilElemStatoId = uid;
	}

}