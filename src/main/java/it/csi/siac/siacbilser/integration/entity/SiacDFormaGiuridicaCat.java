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
 * The persistent class for the siac_d_forma_giuridica_cat database table.
 * 
 */
@Entity
@Table(name="siac_d_forma_giuridica_cat")
@NamedQuery(name="SiacDFormaGiuridicaCat.findAll", query="SELECT s FROM SiacDFormaGiuridicaCat s")
public class SiacDFormaGiuridicaCat extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The forma giuridica cat id. */
	@Id
	@SequenceGenerator(name="SIAC_D_FORMA_GIURIDICA_CAT_FORMAGIURIDICACATID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_FORMA_GIURIDICA_CAT_FORMA_GIURIDICA_CAT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FORMA_GIURIDICA_CAT_FORMAGIURIDICACATID_GENERATOR")
	@Column(name="forma_giuridica_cat_id")
	private Integer formaGiuridicaCatId;	

	/** The forma giuridica cat code. */
	@Column(name="forma_giuridica_cat_code")
	private String formaGiuridicaCatCode;

	/** The forma giuridica cat desc. */
	@Column(name="forma_giuridica_cat_desc")
	private String formaGiuridicaCatDesc;

	//bi-directional many-to-one association to SiacTFormaGiuridica
	/** The siac t forma giuridicas. */
	@OneToMany(mappedBy="siacDFormaGiuridicaCat")
	private List<SiacTFormaGiuridica> siacTFormaGiuridicas;

	/**
	 * Instantiates a new siac d forma giuridica cat.
	 */
	public SiacDFormaGiuridicaCat() {
	}

	/**
	 * Gets the forma giuridica cat id.
	 *
	 * @return the forma giuridica cat id
	 */
	public Integer getFormaGiuridicaCatId() {
		return this.formaGiuridicaCatId;
	}

	/**
	 * Sets the forma giuridica cat id.
	 *
	 * @param formaGiuridicaCatId the new forma giuridica cat id
	 */
	public void setFormaGiuridicaCatId(Integer formaGiuridicaCatId) {
		this.formaGiuridicaCatId = formaGiuridicaCatId;
	}

	/**
	 * Gets the forma giuridica cat code.
	 *
	 * @return the forma giuridica cat code
	 */
	public String getFormaGiuridicaCatCode() {
		return this.formaGiuridicaCatCode;
	}

	/**
	 * Sets the forma giuridica cat code.
	 *
	 * @param formaGiuridicaCatCode the new forma giuridica cat code
	 */
	public void setFormaGiuridicaCatCode(String formaGiuridicaCatCode) {
		this.formaGiuridicaCatCode = formaGiuridicaCatCode;
	}

	/**
	 * Gets the forma giuridica cat desc.
	 *
	 * @return the forma giuridica cat desc
	 */
	public String getFormaGiuridicaCatDesc() {
		return this.formaGiuridicaCatDesc;
	}

	/**
	 * Sets the forma giuridica cat desc.
	 *
	 * @param formaGiuridicaCatDesc the new forma giuridica cat desc
	 */
	public void setFormaGiuridicaCatDesc(String formaGiuridicaCatDesc) {
		this.formaGiuridicaCatDesc = formaGiuridicaCatDesc;
	}

	
	/**
	 * Gets the siac t forma giuridicas.
	 *
	 * @return the siac t forma giuridicas
	 */
	public List<SiacTFormaGiuridica> getSiacTFormaGiuridicas() {
		return this.siacTFormaGiuridicas;
	}

	/**
	 * Sets the siac t forma giuridicas.
	 *
	 * @param siacTFormaGiuridicas the new siac t forma giuridicas
	 */
	public void setSiacTFormaGiuridicas(List<SiacTFormaGiuridica> siacTFormaGiuridicas) {
		this.siacTFormaGiuridicas = siacTFormaGiuridicas;
	}

	/**
	 * Adds the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the siac t forma giuridica
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica addSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().add(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaCat(this);

		return siacTFormaGiuridica;
	}

	/**
	 * Removes the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the siac t forma giuridica
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica removeSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().remove(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaCat(null);

		return siacTFormaGiuridica;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return formaGiuridicaCatId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.formaGiuridicaCatId = uid;
	}

}