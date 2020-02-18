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
 * The persistent class for the siac_r_doc_class database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_class")
@NamedQuery(name="SiacRDocClass.findAll", query="SELECT s FROM SiacRDocClass s")
public class SiacRDocClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_CLASS_DOCCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_CLASS_DOC_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_CLASS_DOCCLASSIFID_GENERATOR")
	@Column(name="doc_classif_id")
	private Integer docClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	

	/**
	 * Instantiates a new siac r doc class.
	 */
	public SiacRDocClass() {
	}

	/**
	 * Gets the doc classif id.
	 *
	 * @return the doc classif id
	 */
	public Integer getDocClassifId() {
		return this.docClassifId;
	}

	/**
	 * Sets the doc classif id.
	 *
	 * @param docClassifId the new doc classif id
	 */
	public void setDocClassifId(Integer docClassifId) {
		this.docClassifId = docClassifId;
	}

	

	/**
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
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
		return docClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docClassifId = uid;
		
	}

}