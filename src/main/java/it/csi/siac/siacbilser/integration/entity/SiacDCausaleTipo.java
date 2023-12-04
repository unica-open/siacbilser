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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_causale_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_tipo")
@NamedQuery(name="SiacDCausaleTipo.findAll", query="SELECT s FROM SiacDCausaleTipo s")
public class SiacDCausaleTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_TIPO_CAUSTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_TIPO_CAUS_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_TIPO_CAUSTIPOID_GENERATOR")
	@Column(name="caus_tipo_id")
	private Integer causTipoId;

	/** The caus tipo code. */
	@Column(name="caus_tipo_code")
	private String causTipoCode;

	/** The caus tipo desc. */
	@Column(name="caus_tipo_desc")
	private String causTipoDesc;

	//bi-directional many-to-one association to SiacDCausaleFamTipo
	/** The siac d causale fam tipo. */
	@ManyToOne
	@JoinColumn(name="caus_fam_tipo_id")
	private SiacDCausaleFamTipo siacDCausaleFamTipo;


	//bi-directional many-to-one association to SiacRCausaleTipo
	/** The siac r causale tipos. */
	@OneToMany(mappedBy="siacDCausaleTipo")
	private List<SiacRCausaleTipo> siacRCausaleTipos;

	/**
	 * Instantiates a new siac d causale tipo.
	 */
	public SiacDCausaleTipo() {
	}

	/**
	 * Gets the caus tipo id.
	 *
	 * @return the caus tipo id
	 */
	public Integer getCausTipoId() {
		return this.causTipoId;
	}

	/**
	 * Sets the caus tipo id.
	 *
	 * @param causTipoId the new caus tipo id
	 */
	public void setCausTipoId(Integer causTipoId) {
		this.causTipoId = causTipoId;
	}

	/**
	 * Gets the caus tipo code.
	 *
	 * @return the caus tipo code
	 */
	public String getCausTipoCode() {
		return this.causTipoCode;
	}

	/**
	 * Sets the caus tipo code.
	 *
	 * @param causTipoCode the new caus tipo code
	 */
	public void setCausTipoCode(String causTipoCode) {
		this.causTipoCode = causTipoCode;
	}

	/**
	 * Gets the caus tipo desc.
	 *
	 * @return the caus tipo desc
	 */
	public String getCausTipoDesc() {
		return this.causTipoDesc;
	}

	/**
	 * Sets the caus tipo desc.
	 *
	 * @param causTipoDesc the new caus tipo desc
	 */
	public void setCausTipoDesc(String causTipoDesc) {
		this.causTipoDesc = causTipoDesc;
	}

	/**
	 * Gets the siac d causale fam tipo.
	 *
	 * @return the siac d causale fam tipo
	 */
	public SiacDCausaleFamTipo getSiacDCausaleFamTipo() {
		return this.siacDCausaleFamTipo;
	}

	/**
	 * Sets the siac d causale fam tipo.
	 *
	 * @param siacDCausaleFamTipo the new siac d causale fam tipo
	 */
	public void setSiacDCausaleFamTipo(SiacDCausaleFamTipo siacDCausaleFamTipo) {
		this.siacDCausaleFamTipo = siacDCausaleFamTipo;
	}

	/**
	 * Gets the siac r causale tipos.
	 *
	 * @return the siac r causale tipos
	 */
	public List<SiacRCausaleTipo> getSiacRCausaleTipos() {
		return this.siacRCausaleTipos;
	}

	/**
	 * Sets the siac r causale tipos.
	 *
	 * @param siacRCausaleTipos the new siac r causale tipos
	 */
	public void setSiacRCausaleTipos(List<SiacRCausaleTipo> siacRCausaleTipos) {
		this.siacRCausaleTipos = siacRCausaleTipos;
	}

	/**
	 * Adds the siac r causale tipo.
	 *
	 * @param siacRCausaleTipo the siac r causale tipo
	 * @return the siac r causale tipo
	 */
	public SiacRCausaleTipo addSiacRCausaleTipo(SiacRCausaleTipo siacRCausaleTipo) {
		getSiacRCausaleTipos().add(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausaleTipo(this);

		return siacRCausaleTipo;
	}

	/**
	 * Removes the siac r causale tipo.
	 *
	 * @param siacRCausaleTipo the siac r causale tipo
	 * @return the siac r causale tipo
	 */
	public SiacRCausaleTipo removeSiacRCausaleTipo(SiacRCausaleTipo siacRCausaleTipo) {
		getSiacRCausaleTipos().remove(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausaleTipo(null);

		return siacRCausaleTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causTipoId = uid;
	}

}