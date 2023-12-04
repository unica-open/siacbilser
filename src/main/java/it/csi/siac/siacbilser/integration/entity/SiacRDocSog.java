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
 * The persistent class for the siac_r_doc_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_sog")
@NamedQuery(name="SiacRDocSog.findAll", query="SELECT s FROM SiacRDocSog s")
public class SiacRDocSog extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc sog id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_SOG_DOCSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_SOG_DOC_SOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_SOG_DOCSOGID_GENERATOR")
	@Column(name="doc_sog_id")
	private Integer docSogId;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo. */
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuolo siacDRuolo;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r doc sog.
	 */
	public SiacRDocSog() {
	}

	/**
	 * Gets the doc sog id.
	 *
	 * @return the doc sog id
	 */
	public Integer getDocSogId() {
		return this.docSogId;
	}

	/**
	 * Sets the doc sog id.
	 *
	 * @param docSogId the new doc sog id
	 */
	public void setDocSogId(Integer docSogId) {
		this.docSogId = docSogId;
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
	 * Gets the siac t doc.
	 *
	 * @return the siac t doc
	 */
	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	/**
	 * Sets the siac t doc.
	 *
	 * @param siacTDoc the new siac t doc
	 */
	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docSogId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docSogId = uid;
		
	}

}