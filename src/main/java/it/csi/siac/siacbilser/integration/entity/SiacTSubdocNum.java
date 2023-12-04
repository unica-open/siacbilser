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
import javax.persistence.Version;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc_num")
@NamedQuery(name="SiacTSubdocNum.findAll", query="SELECT s FROM SiacTSubdocNum s")
public class SiacTSubdocNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_SUBDOCNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SUBDOC_NUM_SUBDOC_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_SUBDOCNUMID_GENERATOR")
	@Column(name="subdoc_num_id")
	private Integer subdocNumId;

	/** The subdoc numero. */
	@Version
	@Column(name="subdoc_numero")
	private Integer subdocNumero;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	/**
	 * Instantiates a new siac t subdoc num.
	 */
	public SiacTSubdocNum() {
	}

	/**
	 * Gets the subdoc num id.
	 *
	 * @return the subdoc num id
	 */
	public Integer getSubdocNumId() {
		return this.subdocNumId;
	}

	/**
	 * Sets the subdoc num id.
	 *
	 * @param subdocId the new subdoc num id
	 */
	public void setSubdocNumId(Integer subdocId) {
		this.subdocNumId = subdocId;
	}

	/**
	 * Gets the subdoc numero.
	 *
	 * @return the subdoc numero
	 */
	public Integer getSubdocNumero() {
		return this.subdocNumero;
	}

	/**
	 * Sets the subdoc numero.
	 *
	 * @param subdocNumero the new subdoc numero
	 */
	public void setSubdocNumero(Integer subdocNumero) {
		this.subdocNumero = subdocNumero;
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
		return subdocNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocNumId = uid;		
	}


}