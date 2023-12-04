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
 * The persistent class for the siac_d_elenco_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_elenco_doc_stato")
public class SiacDElencoDocStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="eldoc_stato_id")
	private Integer eldocStatoId;

	@Column(name="eldoc_stato_code")
	private String eldocStatoCode;

	@Column(name="eldoc_stato_desc")
	private String eldocStatoDesc;

	//bi-directional many-to-one association to SiacRElencoDocStatoFin
	@OneToMany(mappedBy="siacDElencoDocStato")
	private List<SiacRElencoDocStatoFin> siacRElencoDocStatos;

	public SiacDElencoDocStatoFin() {
	}

	public Integer getEldocStatoId() {
		return this.eldocStatoId;
	}

	public void setEldocStatoId(Integer eldocStatoId) {
		this.eldocStatoId = eldocStatoId;
	}

	public String getEldocStatoCode() {
		return this.eldocStatoCode;
	}

	public void setEldocStatoCode(String eldocStatoCode) {
		this.eldocStatoCode = eldocStatoCode;
	}

	public String getEldocStatoDesc() {
		return this.eldocStatoDesc;
	}

	public void setEldocStatoDesc(String eldocStatoDesc) {
		this.eldocStatoDesc = eldocStatoDesc;
	}

	public List<SiacRElencoDocStatoFin> getSiacRElencoDocStatos() {
		return this.siacRElencoDocStatos;
	}

	public void setSiacRElencoDocStatos(List<SiacRElencoDocStatoFin> siacRElencoDocStatos) {
		this.siacRElencoDocStatos = siacRElencoDocStatos;
	}

	public SiacRElencoDocStatoFin addSiacRElencoDocStato(SiacRElencoDocStatoFin siacRElencoDocStato) {
		getSiacRElencoDocStatos().add(siacRElencoDocStato);
		siacRElencoDocStato.setSiacDElencoDocStato(this);

		return siacRElencoDocStato;
	}

	public SiacRElencoDocStatoFin removeSiacRElencoDocStato(SiacRElencoDocStatoFin siacRElencoDocStato) {
		getSiacRElencoDocStatos().remove(siacRElencoDocStato);
		siacRElencoDocStato.setSiacDElencoDocStato(null);

		return siacRElencoDocStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.eldocStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.eldocStatoId = uid;
	}

}