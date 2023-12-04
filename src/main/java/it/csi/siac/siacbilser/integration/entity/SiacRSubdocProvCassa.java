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
 * The persistent class for the siac_r_subdoc_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_prov_cassa")
@NamedQuery(name="SiacRSubdocProvCassa.findAll", query="SELECT s FROM SiacRSubdocProvCassa s")
public class SiacRSubdocProvCassa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc provc id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_PROV_CASSA_SUBDOCPROVCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_PROV_CASSA_SUBDOC_PROVC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_PROV_CASSA_SUBDOCPROVCID_GENERATOR")
	@Column(name="subdoc_provc_id")
	private Integer subdocProvcId;

	//bi-directional many-to-one association to SiacTProvCassa
	/** The siac t prov cassa. */
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassa siacTProvCassa;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc prov cassa.
	 */
	public SiacRSubdocProvCassa() {
	}

	/**
	 * Gets the subdoc provc id.
	 *
	 * @return the subdoc provc id
	 */
	public Integer getSubdocProvcId() {
		return this.subdocProvcId;
	}

	/**
	 * Sets the subdoc provc id.
	 *
	 * @param subdocProvcId the new subdoc provc id
	 */
	public void setSubdocProvcId(Integer subdocProvcId) {
		this.subdocProvcId = subdocProvcId;
	}

	/**
	 * Gets the siac t prov cassa.
	 *
	 * @return the siac t prov cassa
	 */
	public SiacTProvCassa getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	/**
	 * Sets the siac t prov cassa.
	 *
	 * @param siacTProvCassa the new siac t prov cassa
	 */
	public void setSiacTProvCassa(SiacTProvCassa siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
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
		return subdocProvcId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocProvcId = uid;
	}

}