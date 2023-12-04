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
 * The persistent class for the siac_t_nazione database table.
 * 
 */
@Entity
@Table(name="siac_t_nazione")
@NamedQuery(name="SiacTNazione.findAll", query="SELECT s FROM SiacTNazione s")
public class SiacTNazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The nazione id. */
	@Id
	@SequenceGenerator(name="SIAC_T_NAZIONE_NAZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_NAZIONE_NAZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_NAZIONE_NAZIONEID_GENERATOR")
	@Column(name="nazione_id")
	private Integer nazioneId;

	/** The nazione code. */
	@Column(name="nazione_code")
	private String nazioneCode;

	/** The nazione desc. */
	@Column(name="nazione_desc")
	private String nazioneDesc;

	//bi-directional many-to-one association to SiacTComune
	/** The siac t comunes. */
	@OneToMany(mappedBy="siacTNazione")
	private List<SiacTComune> siacTComunes;

	/**
	 * Instantiates a new siac t nazione.
	 */
	public SiacTNazione() {
	}

	/**
	 * Gets the nazione id.
	 *
	 * @return the nazione id
	 */
	public Integer getNazioneId() {
		return this.nazioneId;
	}

	/**
	 * Sets the nazione id.
	 *
	 * @param nazioneId the new nazione id
	 */
	public void setNazioneId(Integer nazioneId) {
		this.nazioneId = nazioneId;
	}

	/**
	 * Gets the nazione code.
	 *
	 * @return the nazione code
	 */
	public String getNazioneCode() {
		return this.nazioneCode;
	}

	/**
	 * Sets the nazione code.
	 *
	 * @param nazioneCode the new nazione code
	 */
	public void setNazioneCode(String nazioneCode) {
		this.nazioneCode = nazioneCode;
	}

	/**
	 * Gets the nazione desc.
	 *
	 * @return the nazione desc
	 */
	public String getNazioneDesc() {
		return this.nazioneDesc;
	}

	/**
	 * Sets the nazione desc.
	 *
	 * @param nazioneDesc the new nazione desc
	 */
	public void setNazioneDesc(String nazioneDesc) {
		this.nazioneDesc = nazioneDesc;
	}

	/**
	 * Gets the siac t comunes.
	 *
	 * @return the siac t comunes
	 */
	public List<SiacTComune> getSiacTComunes() {
		return this.siacTComunes;
	}

	/**
	 * Sets the siac t comunes.
	 *
	 * @param siacTComunes the new siac t comunes
	 */
	public void setSiacTComunes(List<SiacTComune> siacTComunes) {
		this.siacTComunes = siacTComunes;
	}

	/**
	 * Adds the siac t comune.
	 *
	 * @param siacTComune the siac t comune
	 * @return the siac t comune
	 */
	public SiacTComune addSiacTComune(SiacTComune siacTComune) {
		getSiacTComunes().add(siacTComune);
		siacTComune.setSiacTNazione(this);

		return siacTComune;
	}

	/**
	 * Removes the siac t comune.
	 *
	 * @param siacTComune the siac t comune
	 * @return the siac t comune
	 */
	public SiacTComune removeSiacTComune(SiacTComune siacTComune) {
		getSiacTComunes().remove(siacTComune);
		siacTComune.setSiacTNazione(null);

		return siacTComune;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return nazioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.nazioneId = uid;
	}

}