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
 * The persistent class for the siac_r_onere_attivita database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_attivita")
@NamedQuery(name="SiacROnereAttivita.findAll", query="SELECT s FROM SiacROnereAttivita s")
public class SiacROnereAttivita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The onere att r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ONERE_ATTIVITA_ONEREATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ONERE_ATTIVITA_ONERE_ATT_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ONERE_ATTIVITA_ONEREATTRID_GENERATOR")
	@Column(name="onere_att_r_id")
	private Integer onereAttRId;

	//bi-directional many-to-one association to SiacDOnere
	/** The siac d onere. */
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;

	//bi-directional many-to-one association to SiacDOnereAttivita
	/** The siac d onere attivita. */
	@ManyToOne
	@JoinColumn(name="onere_att_id")
	private SiacDOnereAttivita siacDOnereAttivita;



	/**
	 * Instantiates a new siac r onere attivita.
	 */
	public SiacROnereAttivita() {
	}

	/**
	 * Gets the onere att r id.
	 *
	 * @return the onere att r id
	 */
	public Integer getOnereAttRId() {
		return this.onereAttRId;
	}

	/**
	 * Sets the onere att r id.
	 *
	 * @param onereAttRId the new onere att r id
	 */
	public void setOnereAttRId(Integer onereAttRId) {
		this.onereAttRId = onereAttRId;
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

	/**
	 * Gets the siac d onere attivita.
	 *
	 * @return the siac d onere attivita
	 */
	public SiacDOnereAttivita getSiacDOnereAttivita() {
		return this.siacDOnereAttivita;
	}

	/**
	 * Sets the siac d onere attivita.
	 *
	 * @param siacDOnereAttivita the new siac d onere attivita
	 */
	public void setSiacDOnereAttivita(SiacDOnereAttivita siacDOnereAttivita) {
		this.siacDOnereAttivita = siacDOnereAttivita;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereAttRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereAttRId = uid;
	}

}