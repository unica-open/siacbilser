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
 * The persistent class for the siac_r_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_iva")
public class SiacRSubdocIvaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_r_id")
	private Integer docRId;

	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipoFin siacDRelazTipo;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@ManyToOne
	@JoinColumn(name="subdociva_id_a")
	private SiacTSubdocIvaFin siacTSubdocIva1;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@ManyToOne
	@JoinColumn(name="subdociva_id_da")
	private SiacTSubdocIvaFin siacTSubdocIva2;

	public SiacRSubdocIvaFin() {
	}

	public Integer getDocRId() {
		return this.docRId;
	}

	public void setDocRId(Integer docRId) {
		this.docRId = docRId;
	}

	public SiacDRelazTipoFin getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	public void setSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	public SiacTSubdocIvaFin getSiacTSubdocIva1() {
		return this.siacTSubdocIva1;
	}

	public void setSiacTSubdocIva1(SiacTSubdocIvaFin siacTSubdocIva1) {
		this.siacTSubdocIva1 = siacTSubdocIva1;
	}

	public SiacTSubdocIvaFin getSiacTSubdocIva2() {
		return this.siacTSubdocIva2;
	}

	public void setSiacTSubdocIva2(SiacTSubdocIvaFin siacTSubdocIva2) {
		this.siacTSubdocIva2 = siacTSubdocIva2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docRId = uid;
	}

}