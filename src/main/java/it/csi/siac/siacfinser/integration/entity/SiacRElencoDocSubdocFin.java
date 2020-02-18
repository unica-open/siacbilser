/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_elenco_doc_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_elenco_doc_subdoc")
public class SiacRElencoDocSubdocFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_atto_amm_id")
	private Integer subdocAttoAmmId;

	//bi-directional many-to-one association to SiacTElencoDocFin
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDocFin siacTElencoDoc;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRElencoDocSubdocFin() {
	}

	public Integer getSubdocAttoAmmId() {
		return this.subdocAttoAmmId;
	}

	public void setSubdocAttoAmmId(Integer subdocAttoAmmId) {
		this.subdocAttoAmmId = subdocAttoAmmId;
	}

	public SiacTElencoDocFin getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDocFin siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	public SiacTSubdocFin getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocAttoAmmId = uid;
	}

}