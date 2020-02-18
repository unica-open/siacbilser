/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_doc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_iva")
public class SiacRDocIvaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dociva_r_id")
	private Integer docivaRId;

	//bi-directional many-to-one association to SiacTDocFin
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDocFin siacTDoc;

	//bi-directional many-to-one association to SiacTDocIvaFin
	@ManyToOne
	@JoinColumn(name="dociva_id")
	private SiacTDocIvaFin siacTDocIva;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@OneToMany(mappedBy="siacRDocIva")
	private List<SiacTSubdocIvaFin> siacTSubdocIvas;

	public SiacRDocIvaFin() {
	}

	public Integer getDocivaRId() {
		return this.docivaRId;
	}

	public void setDocivaRId(Integer docivaRId) {
		this.docivaRId = docivaRId;
	}

	public SiacTDocFin getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDocFin siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	public SiacTDocIvaFin getSiacTDocIva() {
		return this.siacTDocIva;
	}

	public void setSiacTDocIva(SiacTDocIvaFin siacTDocIva) {
		this.siacTDocIva = siacTDocIva;
	}

	public List<SiacTSubdocIvaFin> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIvaFin> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIvaFin addSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacRDocIva(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIvaFin removeSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacRDocIva(null);

		return siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docivaRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docivaRId = uid;
	}

}