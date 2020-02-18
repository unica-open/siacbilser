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
 * The persistent class for the siac_d_mutuo_voce_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_voce_tipo")
@NamedQuery(name="SiacDMutuoVoceTipo.findAll", query="SELECT s FROM SiacDMutuoVoceTipo s")
public class SiacDMutuoVoceTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut voce tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_VOCE_TIPO_MUTVOCETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_VOCE_TIPO_MUT_VOCE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_VOCE_TIPO_MUTVOCETIPOID_GENERATOR")
	@Column(name="mut_voce_tipo_id")
	private Integer mutVoceTipoId;


	/** The mut voce tipo code. */
	@Column(name="mut_voce_tipo_code")
	private String mutVoceTipoCode;

	/** The mut voce tipo desc. */
	@Column(name="mut_voce_tipo_desc")
	private String mutVoceTipoDesc;


	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voces. */
	@OneToMany(mappedBy="siacDMutuoVoceTipo")
	private List<SiacTMutuoVoce> siacTMutuoVoces;

	/**
	 * Instantiates a new siac d mutuo voce tipo.
	 */
	public SiacDMutuoVoceTipo() {
	}

	/**
	 * Gets the mut voce tipo id.
	 *
	 * @return the mut voce tipo id
	 */
	public Integer getMutVoceTipoId() {
		return this.mutVoceTipoId;
	}

	/**
	 * Sets the mut voce tipo id.
	 *
	 * @param mutVoceTipoId the new mut voce tipo id
	 */
	public void setMutVoceTipoId(Integer mutVoceTipoId) {
		this.mutVoceTipoId = mutVoceTipoId;
	}

	

	

	/**
	 * Gets the mut voce tipo code.
	 *
	 * @return the mut voce tipo code
	 */
	public String getMutVoceTipoCode() {
		return this.mutVoceTipoCode;
	}

	/**
	 * Sets the mut voce tipo code.
	 *
	 * @param mutVoceTipoCode the new mut voce tipo code
	 */
	public void setMutVoceTipoCode(String mutVoceTipoCode) {
		this.mutVoceTipoCode = mutVoceTipoCode;
	}

	/**
	 * Gets the mut voce tipo desc.
	 *
	 * @return the mut voce tipo desc
	 */
	public String getMutVoceTipoDesc() {
		return this.mutVoceTipoDesc;
	}

	/**
	 * Sets the mut voce tipo desc.
	 *
	 * @param mutVoceTipoDesc the new mut voce tipo desc
	 */
	public void setMutVoceTipoDesc(String mutVoceTipoDesc) {
		this.mutVoceTipoDesc = mutVoceTipoDesc;
	}

	

	/**
	 * Gets the siac t mutuo voces.
	 *
	 * @return the siac t mutuo voces
	 */
	public List<SiacTMutuoVoce> getSiacTMutuoVoces() {
		return this.siacTMutuoVoces;
	}

	/**
	 * Sets the siac t mutuo voces.
	 *
	 * @param siacTMutuoVoces the new siac t mutuo voces
	 */
	public void setSiacTMutuoVoces(List<SiacTMutuoVoce> siacTMutuoVoces) {
		this.siacTMutuoVoces = siacTMutuoVoces;
	}

	/**
	 * Adds the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the siac t mutuo voce
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce addSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		getSiacTMutuoVoces().add(siacTMutuoVoce);
		siacTMutuoVoce.setSiacDMutuoVoceTipo(this);

		return siacTMutuoVoce;
	}

	/**
	 * Removes the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the siac t mutuo voce
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce removeSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		getSiacTMutuoVoces().remove(siacTMutuoVoce);
		siacTMutuoVoce.setSiacDMutuoVoceTipo(null);

		return siacTMutuoVoce;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutVoceTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutVoceTipoId = uid;
	}

}