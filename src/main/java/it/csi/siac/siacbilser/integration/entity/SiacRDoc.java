/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_doc database table.
 * 
 */
@Entity
@Table(name="siac_r_doc")
@NamedQuery(name="SiacRDoc.findAll", query="SELECT s FROM SiacRDoc s")
public class SiacRDoc extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_DOCRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_DOC_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_DOCRID_GENERATOR")
	@Column(name="doc_r_id")
	private Integer docRId;
	
	@Column(name="doc_importo_da_dedurre")
	private BigDecimal docImportoDaDedurre;
	
	//bi-directional many-to-one association to SiacDRelazTipo
	/** The siac d relaz tipo. */
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipo siacDRelazTipo;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc padre. */
	@ManyToOne
	@JoinColumn(name="doc_id_da")
	private SiacTDoc siacTDocPadre;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc figlio. */
	@ManyToOne
	@JoinColumn(name="doc_id_a")
	private SiacTDoc siacTDocFiglio;

	/**
	 * Instantiates a new siac r doc.
	 */
	public SiacRDoc() {
	}

	/**
	 * Gets the doc r id.
	 *
	 * @return the doc r id
	 */
	public Integer getDocRId() {
		return this.docRId;
	}

	/**
	 * Sets the doc r id.
	 *
	 * @param docRId the new doc r id
	 */
	public void setDocRId(Integer docRId) {
		this.docRId = docRId;
	}
	
	/**
	 * @return the docImportoDaDedurre
	 */
	public BigDecimal getDocImportoDaDedurre() {
		return docImportoDaDedurre;
	}

	/**
	 * @param docImportoDaDedurre the docImportoDaDedurre to set
	 */
	public void setDocImportoDaDedurre(BigDecimal docImportoDaDedurre) {
		this.docImportoDaDedurre = docImportoDaDedurre;
	}

	/**
	 * Gets the siac d relaz tipo.
	 *
	 * @return the siac d relaz tipo
	 */
	public SiacDRelazTipo getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	/**
	 * Sets the siac d relaz tipo.
	 *
	 * @param siacDRelazTipo the new siac d relaz tipo
	 */
	public void setSiacDRelazTipo(SiacDRelazTipo siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	/**
	 * Gets the siac t doc padre.
	 *
	 * @return the siac t doc padre
	 */
	public SiacTDoc getSiacTDocPadre() {
		return this.siacTDocPadre;
	}

	/**
	 * Sets the siac t doc padre.
	 *
	 * @param siacTDoc1 the new siac t doc padre
	 */
	public void setSiacTDocPadre(SiacTDoc siacTDoc1) {
		this.siacTDocPadre = siacTDoc1;
	}

	/**
	 * Gets the siac t doc figlio.
	 *
	 * @return the siac t doc figlio
	 */
	public SiacTDoc getSiacTDocFiglio() {
		return this.siacTDocFiglio;
	}

	/**
	 * Sets the siac t doc figlio.
	 *
	 * @param siacTDoc2 the new siac t doc figlio
	 */
	public void setSiacTDocFiglio(SiacTDoc siacTDoc2) {
		this.siacTDocFiglio = siacTDoc2;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docRId = uid;
		
	}

	

}