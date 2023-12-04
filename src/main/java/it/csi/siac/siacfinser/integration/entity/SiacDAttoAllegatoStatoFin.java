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
 * The persistent class for the siac_d_atto_allegato_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_allegato_stato")
public class SiacDAttoAllegatoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoal_stato_id")
	private Integer attoalStatoId;

	@Column(name="attoal_stato_code")
	private String attoalStatoCode;

	@Column(name="attoal_stato_desc")
	private String attoalStatoDesc;

	//bi-directional many-to-one association to SiacRAttoAllegatoStatoFin
	@OneToMany(mappedBy="siacDAttoAllegatoStato")
	private List<SiacRAttoAllegatoStatoFin> siacRAttoAllegatoStatos;

	public SiacDAttoAllegatoStatoFin() {
	}

	public Integer getAttoalStatoId() {
		return this.attoalStatoId;
	}

	public void setAttoalStatoId(Integer attoalStatoId) {
		this.attoalStatoId = attoalStatoId;
	}

	public String getAttoalStatoCode() {
		return this.attoalStatoCode;
	}

	public void setAttoalStatoCode(String attoalStatoCode) {
		this.attoalStatoCode = attoalStatoCode;
	}

	public String getAttoalStatoDesc() {
		return this.attoalStatoDesc;
	}

	public void setAttoalStatoDesc(String attoalStatoDesc) {
		this.attoalStatoDesc = attoalStatoDesc;
	}

	public List<SiacRAttoAllegatoStatoFin> getSiacRAttoAllegatoStatos() {
		return this.siacRAttoAllegatoStatos;
	}

	public void setSiacRAttoAllegatoStatos(List<SiacRAttoAllegatoStatoFin> siacRAttoAllegatoStatos) {
		this.siacRAttoAllegatoStatos = siacRAttoAllegatoStatos;
	}

	public SiacRAttoAllegatoStatoFin addSiacRAttoAllegatoStato(SiacRAttoAllegatoStatoFin siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().add(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(this);

		return siacRAttoAllegatoStato;
	}

	public SiacRAttoAllegatoStatoFin removeSiacRAttoAllegatoStato(SiacRAttoAllegatoStatoFin siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().remove(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(null);

		return siacRAttoAllegatoStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoalStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoalStatoId = uid;
	}

}