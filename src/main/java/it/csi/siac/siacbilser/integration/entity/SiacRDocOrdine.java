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
 * The persistent class for the siac_r_doc_ordine database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_ordine")
@NamedQuery(name="SiacRDocOrdine.findAll", query="SELECT s FROM SiacRDocOrdine s")
public class SiacRDocOrdine extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_DOC_ORDINE_DOCORDINEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_ORDINE_DOC_ORDINE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_ORDINE_DOCORDINEID_GENERATOR")
	@Column(name="doc_ordine_id")
	private Integer docOrdineId;

	//bi-directional many-to-one association to SiacTOrdine
	@ManyToOne
	@JoinColumn(name="ordine_id")
	private SiacTOrdine siacTOrdine;

	//bi-directional many-to-one association to SiacTDoc
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;

	public SiacRDocOrdine() {
	}

	public Integer getDocOrdineId() {
		return this.docOrdineId;
	}

	public void setDocOrdineId(Integer docOrdineId) {
		this.docOrdineId = docOrdineId;
	}

	public SiacTOrdine getSiacTOrdine() {
		return this.siacTOrdine;
	}

	public void setSiacTOrdine(SiacTOrdine siacTOrdine) {
		this.siacTOrdine = siacTOrdine;
	}

	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	@Override
	public Integer getUid() {
		return this.docOrdineId;
	}

	@Override
	public void setUid(Integer uid) {
		this.docOrdineId = uid;
	}

}