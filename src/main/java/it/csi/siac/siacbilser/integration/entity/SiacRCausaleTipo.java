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
 * The persistent class for the siac_r_causale_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_tipo")
@NamedQuery(name="SiacRCausaleTipo.findAll", query="SELECT s FROM SiacRCausaleTipo s")
public class SiacRCausaleTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus tipo r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_TIPO_CAUSTIPORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_TIPO_CAUS_TIPO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_TIPO_CAUSTIPORID_GENERATOR")
	@Column(name="caus_tipo_r_id")
	private Integer causTipoRId;
	

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacDCausaleTipo
	/** The siac d causale tipo. */
	@ManyToOne
	@JoinColumn(name="caus_tipo_id")
	private SiacDCausaleTipo siacDCausaleTipo;



	/**
	 * Instantiates a new siac r causale tipo.
	 */
	public SiacRCausaleTipo() {
	}

	/**
	 * Gets the caus tipo r id.
	 *
	 * @return the caus tipo r id
	 */
	public Integer getCausTipoRId() {
		return this.causTipoRId;
	}

	/**
	 * Sets the caus tipo r id.
	 *
	 * @param causTipoRId the new caus tipo r id
	 */
	public void setCausTipoRId(Integer causTipoRId) {
		this.causTipoRId = causTipoRId;
	}

	

	

	/**
	 * Gets the siac d causale.
	 *
	 * @return the siac d causale
	 */
	public SiacDCausale getSiacDCausale() {
		return this.siacDCausale;
	}

	/**
	 * Sets the siac d causale.
	 *
	 * @param siacDCausale the new siac d causale
	 */
	public void setSiacDCausale(SiacDCausale siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	/**
	 * Gets the siac d causale tipo.
	 *
	 * @return the siac d causale tipo
	 */
	public SiacDCausaleTipo getSiacDCausaleTipo() {
		return this.siacDCausaleTipo;
	}

	/**
	 * Sets the siac d causale tipo.
	 *
	 * @param siacDCausaleTipo the new siac d causale tipo
	 */
	public void setSiacDCausaleTipo(SiacDCausaleTipo siacDCausaleTipo) {
		this.siacDCausaleTipo = siacDCausaleTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causTipoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causTipoRId = uid;
	}



}