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
 * The persistent class for the siac_r_subdoc_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_subdoc_iva")
@NamedQuery(name="SiacRSubdocSubdocIva.findAll", query="SELECT s FROM SiacRSubdocSubdocIva s")
public class SiacRSubdocSubdocIva extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subsubdociva id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_SUBDOC_IVA_SUBSUBDOCIVAID_GENERATOR", sequenceName="SIAC_R_SUBDOC_SUBDOC_IVA_SUBSUBDOCIVA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_SUBDOC_IVA_SUBSUBDOCIVAID_GENERATOR")
	@Column(name="subsubdociva_id")
	private Integer subsubdocivaId;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva. */
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIva siacTSubdocIva;

	/**
	 * Instantiates a new siac r subdoc subdoc iva.
	 */
	public SiacRSubdocSubdocIva() {
	}

	/**
	 * Gets the subsubdociva id.
	 *
	 * @return the subsubdociva id
	 */
	public Integer getSubsubdocivaId() {
		return this.subsubdocivaId;
	}

	/**
	 * Sets the subsubdociva id.
	 *
	 * @param subsubdocivaId the new subsubdociva id
	 */
	public void setSubsubdocivaId(Integer subsubdocivaId) {
		this.subsubdocivaId = subsubdocivaId;
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

	/**
	 * Gets the siac t subdoc iva.
	 *
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	/**
	 * Sets the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the new siac t subdoc iva
	 */
	public void setSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subsubdocivaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subsubdocivaId = uid;
	}
}