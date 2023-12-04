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
 * The persistent class for the siac_r_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_iva")
@NamedQuery(name="SiacRSubdocIva.findAll", query="SELECT s FROM SiacRSubdocIva s")
public class SiacRSubdocIva extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_IVA_DOCRID_GENERATOR", sequenceName="SIAC_R_SUBDOC_IVA_DOC_R_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_IVA_DOCRID_GENERATOR")
	@Column(name="doc_r_id")
	private Integer docRId;

	//bi-directional many-to-one association to SiacDRelazTipo
	/** The siac d relaz tipo. */
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipo siacDRelazTipo;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva figlio. */
	@ManyToOne
	@JoinColumn(name="subdociva_id_a")
	private SiacTSubdocIva siacTSubdocIvaFiglio;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva padre. */
	@ManyToOne
	@JoinColumn(name="subdociva_id_da")
	private SiacTSubdocIva siacTSubdocIvaPadre;

	/**
	 * Instantiates a new siac r subdoc iva.
	 */
	public SiacRSubdocIva() {
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
	 * Gets the siac t subdoc iva figlio.
	 *
	 * @return the siac t subdoc iva figlio
	 */
	public SiacTSubdocIva getSiacTSubdocIvaFiglio() {
		return this.siacTSubdocIvaFiglio;
	}

	/**
	 * Sets the siac t subdoc iva figlio.
	 *
	 * @param siacTSubdocIva1 the new siac t subdoc iva figlio
	 */
	public void setSiacTSubdocIvaFiglio(SiacTSubdocIva siacTSubdocIva1) {
		this.siacTSubdocIvaFiglio = siacTSubdocIva1;
	}

	/**
	 * Gets the siac t subdoc iva padre.
	 *
	 * @return the siac t subdoc iva padre
	 */
	public SiacTSubdocIva getSiacTSubdocIvaPadre() {
		return this.siacTSubdocIvaPadre;
	}

	/**
	 * Sets the siac t subdoc iva padre.
	 *
	 * @param siacTSubdocIva2 the new siac t subdoc iva padre
	 */
	public void setSiacTSubdocIvaPadre(SiacTSubdocIva siacTSubdocIva2) {
		this.siacTSubdocIvaPadre = siacTSubdocIva2;
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