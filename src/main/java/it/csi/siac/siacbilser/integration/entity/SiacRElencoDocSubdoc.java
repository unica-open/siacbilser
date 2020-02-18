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


/**
 * The persistent class for the siac_r_elenco_doc_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_elenco_doc_subdoc")
@NamedQuery(name="SiacRElencoDocSubdoc.findAll", query="SELECT s FROM SiacRElencoDocSubdoc s")
public class SiacRElencoDocSubdoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ELENCO_DOC_SUBDOC_SUBDOCATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ELENCO_DOC_SUBDOC_SUBDOC_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ELENCO_DOC_SUBDOC_SUBDOCATTOAMMID_GENERATOR")
	@Column(name="subdoc_atto_amm_id")
	private Integer subdocAttoAmmId;
	
	//bi-directional many-to-one association to SiacTElencoDoc
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDoc siacTElencoDoc;

	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	public SiacRElencoDocSubdoc() {
	}

	public Integer getSubdocAttoAmmId() {
		return this.subdocAttoAmmId;
	}

	public void setSubdocAttoAmmId(Integer subdocAttoAmmId) {
		this.subdocAttoAmmId = subdocAttoAmmId;
	}
	
	public SiacTElencoDoc getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDoc siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return subdocAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		this.subdocAttoAmmId = uid;
	}

}