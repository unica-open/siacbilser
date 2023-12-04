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
 * The persistent class for the siac_d_soggetto_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_stato")
public class SiacDSoggettoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="soggetto_stato_id")
	private Integer soggettoStatoId;

	@Column(name="soggetto_stato_code")
	private String soggettoStatoCode;

	@Column(name="soggetto_stato_desc")
	private String soggettoStatoDesc;

	//bi-directional many-to-one association to SiacRSoggettoStatoFin
	@OneToMany(mappedBy="siacDSoggettoStato")
	private List<SiacRSoggettoStatoFin> siacRSoggettoStatos;

	public SiacDSoggettoStatoFin() {
	}

	public Integer getSoggettoStatoId() {
		return this.soggettoStatoId;
	}

	public void setSoggettoStatoId(Integer soggettoStatoId) {
		this.soggettoStatoId = soggettoStatoId;
	}

	public String getSoggettoStatoCode() {
		return this.soggettoStatoCode;
	}

	public void setSoggettoStatoCode(String soggettoStatoCode) {
		this.soggettoStatoCode = soggettoStatoCode;
	}

	public String getSoggettoStatoDesc() {
		return this.soggettoStatoDesc;
	}

	public void setSoggettoStatoDesc(String soggettoStatoDesc) {
		this.soggettoStatoDesc = soggettoStatoDesc;
	}

	public List<SiacRSoggettoStatoFin> getSiacRSoggettoStatos() {
		return this.siacRSoggettoStatos;
	}

	public void setSiacRSoggettoStatos(List<SiacRSoggettoStatoFin> siacRSoggettoStatos) {
		this.siacRSoggettoStatos = siacRSoggettoStatos;
	}

	public SiacRSoggettoStatoFin addSiacRSoggettoStato(SiacRSoggettoStatoFin siacRSoggettoStato) {
		getSiacRSoggettoStatos().add(siacRSoggettoStato);
		siacRSoggettoStato.setSiacDSoggettoStato(this);

		return siacRSoggettoStato;
	}

	public SiacRSoggettoStatoFin removeSiacRSoggettoStato(SiacRSoggettoStatoFin siacRSoggettoStato) {
		getSiacRSoggettoStatos().remove(siacRSoggettoStato);
		siacRSoggettoStato.setSiacDSoggettoStato(null);

		return siacRSoggettoStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoStatoId = uid;
	}
}