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
 * The persistent class for the siac_r_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_stato")
@NamedQuery(name="SiacRDocStato.findAll", query="SELECT s FROM SiacRDocStato s")
public class SiacRDocStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_STATO_DOCSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_STATO_DOC_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_STATO_DOCSTATORID_GENERATOR")
	@Column(name="doc_stato_r_id")
	private Integer docStatoRId;

	//bi-directional many-to-one association to SiacDDocStato
	/** The siac d doc stato. */
	@ManyToOne
	@JoinColumn(name="doc_stato_id")
	private SiacDDocStato siacDDocStato;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	

	/**
	 * Instantiates a new siac r doc stato.
	 */
	public SiacRDocStato() {
	}

	/**
	 * Gets the doc stato r id.
	 *
	 * @return the doc stato r id
	 */
	public Integer getDocStatoRId() {
		return this.docStatoRId;
	}

	/**
	 * Sets the doc stato r id.
	 *
	 * @param docStatoRId the new doc stato r id
	 */
	public void setDocStatoRId(Integer docStatoRId) {
		this.docStatoRId = docStatoRId;
	}

	

	/**
	 * Gets the siac d doc stato.
	 *
	 * @return the siac d doc stato
	 */
	public SiacDDocStato getSiacDDocStato() {
		return this.siacDDocStato;
	}

	/**
	 * Sets the siac d doc stato.
	 *
	 * @param siacDDocStato the new siac d doc stato
	 */
	public void setSiacDDocStato(SiacDDocStato siacDDocStato) {
		this.siacDDocStato = siacDDocStato;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docStatoRId = uid;
		
	}

	

}