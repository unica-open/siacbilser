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
 * The persistent class for the siac_d_relaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_relaz_stato")
public class SiacDRelazStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="relaz_stato_id")
	private Integer relazStatoId;

	@Column(name="relaz_stato_code")
	private String relazStatoCode;

	@Column(name="relaz_stato_desc")
	private String relazStatoDesc;

	//bi-directional many-to-one association to SiacRSoggettoRelazStatoFin
	@OneToMany(mappedBy="siacDRelazStato")
	private List<SiacRSoggettoRelazStatoFin> siacRSoggettoRelazStatos;

	public SiacDRelazStatoFin() {
	}

	public Integer getRelazStatoId() {
		return this.relazStatoId;
	}

	public void setRelazStatoId(Integer relazStatoId) {
		this.relazStatoId = relazStatoId;
	}

	public String getRelazStatoCode() {
		return this.relazStatoCode;
	}

	public void setRelazStatoCode(String relazStatoCode) {
		this.relazStatoCode = relazStatoCode;
	}

	public String getRelazStatoDesc() {
		return this.relazStatoDesc;
	}

	public void setRelazStatoDesc(String relazStatoDesc) {
		this.relazStatoDesc = relazStatoDesc;
	}

	public List<SiacRSoggettoRelazStatoFin> getSiacRSoggettoRelazStatos() {
		return this.siacRSoggettoRelazStatos;
	}

	public void setSiacRSoggettoRelazStatos(List<SiacRSoggettoRelazStatoFin> siacRSoggettoRelazStatos) {
		this.siacRSoggettoRelazStatos = siacRSoggettoRelazStatos;
	}

	public SiacRSoggettoRelazStatoFin addSiacRSoggettoRelazStato(SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().add(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacDRelazStato(this);

		return siacRSoggettoRelazStato;
	}

	public SiacRSoggettoRelazStatoFin removeSiacRSoggettoRelazStato(SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().remove(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacDRelazStato(null);

		return siacRSoggettoRelazStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.relazStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.relazStatoId = uid;
	}
}