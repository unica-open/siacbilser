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
 * The persistent class for the siac_r_subdoc_iva_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_iva_stato")
public class SiacRSubdocIvaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdociva_stato_r_id")
	private Integer subdocivaStatoRId;

	//bi-directional many-to-one association to SiacDSubdocIvaStatoFin
	@ManyToOne
	@JoinColumn(name="subdociva_stato_id")
	private SiacDSubdocIvaStatoFin siacDSubdocIvaStato;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIvaFin siacTSubdocIva;

	public SiacRSubdocIvaStatoFin() {
	}

	public Integer getSubdocivaStatoRId() {
		return this.subdocivaStatoRId;
	}

	public void setSubdocivaStatoRId(Integer subdocivaStatoRId) {
		this.subdocivaStatoRId = subdocivaStatoRId;
	}

	public SiacDSubdocIvaStatoFin getSiacDSubdocIvaStato() {
		return this.siacDSubdocIvaStato;
	}

	public void setSiacDSubdocIvaStato(SiacDSubdocIvaStatoFin siacDSubdocIvaStato) {
		this.siacDSubdocIvaStato = siacDSubdocIvaStato;
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
		return this.subdocivaStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocivaStatoRId = uid;
	}

}