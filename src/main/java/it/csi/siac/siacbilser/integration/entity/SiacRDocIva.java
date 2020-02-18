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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_doc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_iva")
@NamedQuery(name="SiacRDocIva.findAll", query="SELECT s FROM SiacRDocIva s")
public class SiacRDocIva extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The dociva r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_IVA_DOCIVARID_GENERATOR", sequenceName="SIAC_R_DOC_IVA_DOCIVA_R_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_IVA_DOCIVARID_GENERATOR")
	@Column(name="dociva_r_id")
	private Integer docivaRId;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	//bi-directional many-to-one association to SiacTDocIva
	/** The siac t doc iva. */
	@ManyToOne
	@JoinColumn(name="dociva_id")
	private SiacTDocIva siacTDocIva;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc ivas. */
	@OneToMany(mappedBy="siacRDocIva")
	private List<SiacTSubdocIva> siacTSubdocIvas;

	/**
	 * Instantiates a new siac r doc iva.
	 */
	public SiacRDocIva() {
	}

	/**
	 * Gets the dociva r id.
	 *
	 * @return the dociva r id
	 */
	public Integer getDocivaRId() {
		return this.docivaRId;
	}

	/**
	 * Sets the dociva r id.
	 *
	 * @param docivaRId the new dociva r id
	 */
	public void setDocivaRId(Integer docivaRId) {
		this.docivaRId = docivaRId;
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
	 * Gets the siac t doc iva.
	 *
	 * @return the siac t doc iva
	 */
	public SiacTDocIva getSiacTDocIva() {
		return this.siacTDocIva;
	}

	/**
	 * Sets the siac t doc iva.
	 *
	 * @param siacTDocIva the new siac t doc iva
	 */
	public void setSiacTDocIva(SiacTDocIva siacTDocIva) {
		this.siacTDocIva = siacTDocIva;
	}

	/**
	 * Gets the siac t subdoc ivas.
	 *
	 * @return the siac t subdoc ivas
	 */
	public List<SiacTSubdocIva> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	/**
	 * Sets the siac t subdoc ivas.
	 *
	 * @param siacTSubdocIvas the new siac t subdoc ivas
	 */
	public void setSiacTSubdocIvas(List<SiacTSubdocIva> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	/**
	 * Adds the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the siac t subdoc iva
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva addSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacRDocIva(this);

		return siacTSubdocIva;
	}

	/**
	 * Removes the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the siac t subdoc iva
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva removeSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacRDocIva(null);

		return siacTSubdocIva;
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docivaRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docivaRId = uid;
	}

}