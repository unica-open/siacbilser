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


/**
 * The persistent class for the siac_d_bil_elem_categoria database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_categoria")
@NamedQuery(name="SiacDBilElemCategoria.findAll", query="SELECT s FROM SiacDBilElemCategoria s")
public class SiacDBilElemCategoria extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem categoria id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_CATEGORIA_ELEMCATID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_CATEGORIA_ELEM_CAT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_CATEGORIA_ELEMCATID_GENERATOR")
	@Column(name="elem_cat_id")
	private Integer elemCatId;

	/** The elem cat code. */
	@Column(name="elem_cat_code")
	private String elemCatCode;

	/** The elem cat desc. */
	@Column(name="elem_cat_desc")
	private String elemCatDesc;

	//bi-directional many-to-one association to SiacRBilElemStato
	/** The siac r bil elem statos. */
	@OneToMany(mappedBy="siacDBilElemCategoria")
	private List<SiacRBilElemCategoria> siacRBilElemCategorias;
	
	// bi-directional many-to-one association to SiacRBilElemTipoCategoria
	@OneToMany(mappedBy="siacDBilElemCategoria")
	private List<SiacRBilElemTipoCategoria> siacRBilElemTipoCategorias;

	/**
	 * Instantiates a new siac d bil elem categoria.
	 */
	public SiacDBilElemCategoria() {
	}

	/**
	 * Gets the elem stato id.
	 *
	 * @return the elem stato id
	 */
	public Integer getElemCatId() {
		return this.elemCatId;
	}

	/**
	 * Sets the elem stato id.
	 *
	 * @param elemStatoId the new elem stato id
	 */
	public void setElemCatId(Integer elemStatoId) {
		this.elemCatId = elemStatoId;
	}

	/**
	 * Gets the elem stato code.
	 *
	 * @return the elem stato code
	 */
	public String getElemCatCode() {
		return this.elemCatCode;
	}

	/**
	 * Sets the elem stato code.
	 *
	 * @param elemStatoCode the new elem stato code
	 */
	public void setElemCatCode(String elemStatoCode) {
		this.elemCatCode = elemStatoCode;
	}

	/**
	 * Gets the elem stato desc.
	 *
	 * @return the elem stato desc
	 */
	public String getElemCatDesc() {
		return this.elemCatDesc;
	}

	/**
	 * Sets the elem stato desc.
	 *
	 * @param elemStatoDesc the new elem stato desc
	 */
	public void setElemCatDesc(String elemStatoDesc) {
		this.elemCatDesc = elemStatoDesc;
	}

	/**
	 * Gets the siac r bil elem statos.
	 *
	 * @return the siac r bil elem statos
	 */
	public List<SiacRBilElemCategoria> getSiacRBilElemCategorias() {
		return this.siacRBilElemCategorias;
	}

	/**
	 * Sets the siac r bil elem statos.
	 *
	 * @param siacRBilElemCategorias the new siac r bil elem statos
	 */
	public void setSiacRBilElemCategorias(List<SiacRBilElemCategoria> siacRBilElemCategorias) {
		this.siacRBilElemCategorias = siacRBilElemCategorias;
	}

	/**
	 * Adds the siac r bil elem stato.
	 *
	 * @param siacRBilElemCategoia the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemCategoria addSiacRBilElemCategoria(SiacRBilElemCategoria siacRBilElemCategoria) {
		getSiacRBilElemCategorias().add(siacRBilElemCategoria);
		siacRBilElemCategoria.setSiacDBilElemCategoria(this);

		return siacRBilElemCategoria;
	}

	/**
	 * Removes the siac r bil elem stato.
	 *
	 * @param siacRBilElemCategoria the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemCategoria removeSiacRBilElemCategoria(SiacRBilElemCategoria siacRBilElemCategoria) {
		getSiacRBilElemCategorias().remove(siacRBilElemCategoria);
		siacRBilElemCategoria.setSiacDBilElemCategoria(null);

		return siacRBilElemCategoria;
	}
	
	public List<SiacRBilElemTipoCategoria> getSiacRBilElemTipoCategorias() {
		return this.siacRBilElemTipoCategorias;
	}

	public void setSiacRBilElemTipoCategorias(List<SiacRBilElemTipoCategoria> siacRBilElemTipoCategorias) {
		this.siacRBilElemTipoCategorias = siacRBilElemTipoCategorias;
	}

	public SiacRBilElemTipoCategoria addSiacRBilElemTipoCategoria(SiacRBilElemTipoCategoria siacRBilElemTipoCategoria) {
		getSiacRBilElemTipoCategorias().add(siacRBilElemTipoCategoria);
		siacRBilElemTipoCategoria.setSiacDBilElemCategoria(this);

		return siacRBilElemTipoCategoria;
	}

	public SiacRBilElemTipoCategoria removeSiacRBilElemTipoCategoria(SiacRBilElemTipoCategoria siacRBilElemTipoCategoria) {
		getSiacRBilElemTipoCategorias().remove(siacRBilElemTipoCategoria);
		siacRBilElemTipoCategoria.setSiacDBilElemCategoria(null);

		return siacRBilElemTipoCategoria;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemCatId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemCatId = uid;
	}

}