/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

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


/**
 * The persistent class for the siac_t_subdoc_sospensione database table.
 */
@Entity
@Table(name="siac_t_subdoc_sospensione")
@NamedQuery(name="SiacTSubdocSospensione.findAll", query="SELECT s FROM SiacTSubdocSospensione s")
public class SiacTSubdocSospensione extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_SOSPENSIONE_SUBDOCSOSPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SUBDOC_SOSPENSIONE_SUBDOC_SOSP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_SOSPENSIONE_SUBDOCSOSPID_GENERATOR")
	@Column(name="subdoc_sosp_id")
	private Integer subdocSospId;
	
	@Column(name="subdoc_sosp_data")
	private Date subdocSospData;
	
	@Column(name="subdoc_sosp_causale")
	private String subdocSospCausale;
	
	@Column(name="subdoc_sosp_data_riattivazione")
	private Date subdocSospDataRiattivazione;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;
	
	public SiacTSubdocSospensione() {
	}

	/**
	 * @return the subdocSospId
	 */
	public Integer getSubdocSospId() {
		return subdocSospId;
	}

	/**
	 * @param subdocSospId the subdocSospId to set
	 */
	public void setSubdocSospId(Integer subdocSospId) {
		this.subdocSospId = subdocSospId;
	}

	/**
	 * @return the subdocSospData
	 */
	public Date getSubdocSospData() {
		return subdocSospData;
	}

	/**
	 * @param subdocSospData the subdocSospData to set
	 */
	public void setSubdocSospData(Date subdocSospData) {
		this.subdocSospData = subdocSospData;
	}

	/**
	 * @return the subdocSospCausale
	 */
	public String getSubdocSospCausale() {
		return subdocSospCausale;
	}

	/**
	 * @param subdocSospCausale the subdocSospCausale to set
	 */
	public void setSubdocSospCausale(String subdocSospCausale) {
		this.subdocSospCausale = subdocSospCausale;
	}

	/**
	 * @return the subdocSospDataRiattivazione
	 */
	public Date getSubdocSospDataRiattivazione() {
		return subdocSospDataRiattivazione;
	}

	/**
	 * @param subdocSospDataRiattivazione the subdocSospDataRiattivazione to set
	 */
	public void setSubdocSospDataRiattivazione(Date subdocSospDataRiattivazione) {
		this.subdocSospDataRiattivazione = subdocSospDataRiattivazione;
	}

	/**
	 * @return the siacTSubdoc
	 */
	public SiacTSubdoc getSiacTSubdoc() {
		return siacTSubdoc;
	}

	/**
	 * @param siacTSubdoc the siacTSubdoc to set
	 */
	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return subdocSospId;
	}

	@Override
	public void setUid(Integer uid) {
		this.subdocSospId = uid;
	}
}