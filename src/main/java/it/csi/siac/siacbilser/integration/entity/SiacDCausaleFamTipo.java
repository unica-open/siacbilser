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
 * The persistent class for the siac_d_causale_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_fam_tipo")
@NamedQuery(name="SiacDCausaleFamTipo.findAll", query="SELECT s FROM SiacDCausaleFamTipo s")
public class SiacDCausaleFamTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus fam tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_FAM_TIPO_CAUSFAMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_FAM_TIPO_CAUS_FAM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_FAM_TIPO_CAUSFAMTIPOID_GENERATOR")
	@Column(name="caus_fam_tipo_id")
	private Integer causFamTipoId;

	/** The caus fam tipo code. */
	@Column(name="caus_fam_tipo_code")
	private String causFamTipoCode;

	/** The caus fam tipo desc. */
	@Column(name="caus_fam_tipo_desc")
	private String causFamTipoDesc;

	//bi-directional many-to-one association to SiacDCausaleTipo
	/** The siac d causale tipos. */
	@OneToMany(mappedBy="siacDCausaleFamTipo")
	private List<SiacDCausaleTipo> siacDCausaleTipos;

	/**
	 * Instantiates a new siac d causale fam tipo.
	 */
	public SiacDCausaleFamTipo() {
	}

	/**
	 * Gets the caus fam tipo id.
	 *
	 * @return the caus fam tipo id
	 */
	public Integer getCausFamTipoId() {
		return this.causFamTipoId;
	}

	/**
	 * Sets the caus fam tipo id.
	 *
	 * @param causFamTipoId the new caus fam tipo id
	 */
	public void setCausFamTipoId(Integer causFamTipoId) {
		this.causFamTipoId = causFamTipoId;
	}

	/**
	 * Gets the caus fam tipo code.
	 *
	 * @return the caus fam tipo code
	 */
	public String getCausFamTipoCode() {
		return this.causFamTipoCode;
	}

	/**
	 * Sets the caus fam tipo code.
	 *
	 * @param causFamTipoCode the new caus fam tipo code
	 */
	public void setCausFamTipoCode(String causFamTipoCode) {
		this.causFamTipoCode = causFamTipoCode;
	}

	/**
	 * Gets the caus fam tipo desc.
	 *
	 * @return the caus fam tipo desc
	 */
	public String getCausFamTipoDesc() {
		return this.causFamTipoDesc;
	}

	/**
	 * Sets the caus fam tipo desc.
	 *
	 * @param causFamTipoDesc the new caus fam tipo desc
	 */
	public void setCausFamTipoDesc(String causFamTipoDesc) {
		this.causFamTipoDesc = causFamTipoDesc;
	}

	/**
	 * Gets the siac d causale tipos.
	 *
	 * @return the siac d causale tipos
	 */
	public List<SiacDCausaleTipo> getSiacDCausaleTipos() {
		return this.siacDCausaleTipos;
	}

	/**
	 * Sets the siac d causale tipos.
	 *
	 * @param siacDCausaleTipos the new siac d causale tipos
	 */
	public void setSiacDCausaleTipos(List<SiacDCausaleTipo> siacDCausaleTipos) {
		this.siacDCausaleTipos = siacDCausaleTipos;
	}

	/**
	 * Adds the siac d causale tipo.
	 *
	 * @param siacDCausaleTipo the siac d causale tipo
	 * @return the siac d causale tipo
	 */
	public SiacDCausaleTipo addSiacDCausaleTipo(SiacDCausaleTipo siacDCausaleTipo) {
		getSiacDCausaleTipos().add(siacDCausaleTipo);
		siacDCausaleTipo.setSiacDCausaleFamTipo(this);

		return siacDCausaleTipo;
	}

	/**
	 * Removes the siac d causale tipo.
	 *
	 * @param siacDCausaleTipo the siac d causale tipo
	 * @return the siac d causale tipo
	 */
	public SiacDCausaleTipo removeSiacDCausaleTipo(SiacDCausaleTipo siacDCausaleTipo) {
		getSiacDCausaleTipos().remove(siacDCausaleTipo);
		siacDCausaleTipo.setSiacDCausaleFamTipo(null);

		return siacDCausaleTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causFamTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causFamTipoId = uid;
	}

}