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
 * The persistent class for the siac_t_predoc database table.
 * 
 */
@Entity
@Table(name="siac_r_elenco_doc_predoc")
@NamedQuery(name="SiacRElencoDocPredoc.findAll", query="SELECT s FROM SiacRElencoDocPredoc s")
public class SiacRElencoDocPredoc extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The eldoc predoc rel id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ELENCO_DOC_PREDOC_ELDOCPREDOCRELID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ELENCO_DOC_PREDOC_ELDOC_PREDOC_REL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ELENCO_DOC_PREDOC_ELDOCPREDOCRELID_GENERATOR")
	@Column(name="eldoc_predoc_rel_id")
	private Integer eldocPredocRelId;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	//bi-directional many-to-one association to SiacTElencoDoc
	/** The siac t elenco doc. */
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDoc siacTElencoDoc;

	/**
	 * Instantiates a new siac t predoc.
	 */
	public SiacRElencoDocPredoc() {
	}

	/**
	 * Gets the eldoc predoc rel id.
	 *
	 * @return the eldoc predoc rel id
	 */
	public Integer getEldocPredocRelId() {
		return this.eldocPredocRelId;
	}

	/**
	 * Sets the eldoc predoc rel id.
	 *
	 * @param eldocPredocRelId the new eldoc predoc rel id
	 */
	public void setEldocPredocRelId(Integer eldocPredocRelId) {
		this.eldocPredocRelId = eldocPredocRelId;
	}

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	/**
	 * Gets the siac t elenco doc.
	 *
	 * @return the siac t elenco doc
	 */
	public SiacTElencoDoc getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	/**
	 * Sets the siac t elenco doc.
	 *
	 * @param siacTElencoDoc the new siac t elenco doc
	 */
	public void setSiacTElencoDoc(SiacTElencoDoc siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	@Override
	public Integer getUid() {
		return eldocPredocRelId;
	}

	@Override
	public void setUid(Integer uid) {
		this.eldocPredocRelId = uid;
	}

}