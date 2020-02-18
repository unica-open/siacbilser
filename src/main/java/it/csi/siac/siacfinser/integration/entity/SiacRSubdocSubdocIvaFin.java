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
 * The persistent class for the siac_r_subdoc_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_subdoc_iva")
public class SiacRSubdocSubdocIvaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subsubdociva_id")
	private Integer subsubdocivaId;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIvaFin siacTSubdocIva;

	public SiacRSubdocSubdocIvaFin() {
	}

	public Integer getSubsubdocivaId() {
		return this.subsubdocivaId;
	}

	public void setSubsubdocivaId(Integer subsubdocivaId) {
		this.subsubdocivaId = subsubdocivaId;
	}

	public SiacTSubdocFin getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	public SiacTSubdocIvaFin getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	public void setSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subsubdocivaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subsubdocivaId = uid;
	}

}