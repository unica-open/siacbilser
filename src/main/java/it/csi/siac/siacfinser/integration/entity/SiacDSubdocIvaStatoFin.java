/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_subdoc_iva_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_subdoc_iva_stato")
public class SiacDSubdocIvaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdociva_stato_id")
	private Integer subdocivaStatoId;

	@Column(name="subdociva_stato_code")
	private String subdocivaStatoCode;

	@Column(name="subdociva_stato_desc")
	private String subdocivaStatoDesc;

	//bi-directional many-to-one association to SiacRSubdocIvaStatoFin
	@OneToMany(mappedBy="siacDSubdocIvaStato")
	private List<SiacRSubdocIvaStatoFin> siacRSubdocIvaStatos;

	public SiacDSubdocIvaStatoFin() {
	}

	public Integer getSubdocivaStatoId() {
		return this.subdocivaStatoId;
	}

	public void setSubdocivaStatoId(Integer subdocivaStatoId) {
		this.subdocivaStatoId = subdocivaStatoId;
	}

	public String getSubdocivaStatoCode() {
		return this.subdocivaStatoCode;
	}

	public void setSubdocivaStatoCode(String subdocivaStatoCode) {
		this.subdocivaStatoCode = subdocivaStatoCode;
	}

	public String getSubdocivaStatoDesc() {
		return this.subdocivaStatoDesc;
	}

	public void setSubdocivaStatoDesc(String subdocivaStatoDesc) {
		this.subdocivaStatoDesc = subdocivaStatoDesc;
	}

	public List<SiacRSubdocIvaStatoFin> getSiacRSubdocIvaStatos() {
		return this.siacRSubdocIvaStatos;
	}

	public void setSiacRSubdocIvaStatos(List<SiacRSubdocIvaStatoFin> siacRSubdocIvaStatos) {
		this.siacRSubdocIvaStatos = siacRSubdocIvaStatos;
	}

	public SiacRSubdocIvaStatoFin addSiacRSubdocIvaStato(SiacRSubdocIvaStatoFin siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().add(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(this);

		return siacRSubdocIvaStato;
	}

	public SiacRSubdocIvaStatoFin removeSiacRSubdocIvaStato(SiacRSubdocIvaStatoFin siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().remove(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(null);

		return siacRSubdocIvaStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocivaStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocivaStatoId = uid;
	}

}