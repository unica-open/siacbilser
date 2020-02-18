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
 * The persistent class for the siac_r_subdoc_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_sog")
@NamedQuery(name="SiacRSubdocSog.findAll", query="SELECT s FROM SiacRSubdocSog s")
public class SiacRSubdocSog extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc sog id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_SOG_SUBDOCSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_SOG_SUBDOC_SOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_SOG_SUBDOCSOGID_GENERATOR")
	@Column(name="subdoc_sog_id")
	private Integer subdocSogId;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo. */
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuolo siacDRuolo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc sog.
	 */
	public SiacRSubdocSog() {
	}

	/**
	 * Gets the subdoc sog id.
	 *
	 * @return the subdoc sog id
	 */
	public Integer getSubdocSogId() {
		return this.subdocSogId;
	}

	/**
	 * Sets the subdoc sog id.
	 *
	 * @param subdocSogId the new subdoc sog id
	 */
	public void setSubdocSogId(Integer subdocSogId) {
		this.subdocSogId = subdocSogId;
	}

	/**
	 * Gets the siac d ruolo.
	 *
	 * @return the siac d ruolo
	 */
	public SiacDRuolo getSiacDRuolo() {
		return this.siacDRuolo;
	}

	/**
	 * Sets the siac d ruolo.
	 *
	 * @param siacDRuolo the new siac d ruolo
	 */
	public void setSiacDRuolo(SiacDRuolo siacDRuolo) {
		this.siacDRuolo = siacDRuolo;
	}

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/**
	 * Gets the siac t subdoc.
	 *
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	/**
	 * Sets the siac t subdoc.
	 *
	 * @param siacTSubdoc the new siac t subdoc
	 */
	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocSogId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocSogId = uid;
	}

}