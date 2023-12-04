/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_r_onere_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_causale")
@NamedQuery(name="SiacROnereCausale.findAll", query="SELECT s FROM SiacROnereCausale s")
public class SiacROnereCausale extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The onere caus id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ONERE_CAUSALE_ONERECAUSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ONERE_CAUSALE_ONERE_CAUS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ONERE_CAUSALE_ONERECAUSID_GENERATOR")
	@Column(name="onere_caus_id")
	private Integer onereCausId;

	

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacDOnere
	/** The siac d onere. */
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;



	/**
	 * Instantiates a new siac r onere causale.
	 */
	public SiacROnereCausale() {
	}

	/**
	 * Gets the onere caus id.
	 *
	 * @return the onere caus id
	 */
	public Integer getOnereCausId() {
		return this.onereCausId;
	}

	/**
	 * Sets the onere caus id.
	 *
	 * @param onereCausId the new onere caus id
	 */
	public void setOnereCausId(Integer onereCausId) {
		this.onereCausId = onereCausId;
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
	 * Gets the siac d onere.
	 *
	 * @return the siac d onere
	 */
	public SiacDOnere getSiacDOnere() {
		return this.siacDOnere;
	}

	/**
	 * Sets the siac d onere.
	 *
	 * @param siacDOnere the new siac d onere
	 */
	public void setSiacDOnere(SiacDOnere siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereCausId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereCausId = uid;
		
	}

}