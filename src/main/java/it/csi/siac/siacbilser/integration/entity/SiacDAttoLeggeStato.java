/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_atto_legge_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_legge_stato")
@NamedQuery(name="SiacDAttoLeggeStato.findAll", query="SELECT s FROM SiacDAttoLeggeStato s")
public class SiacDAttoLeggeStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_LEGGE_STATO_ATTOLEGGESTATOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_ATTO_LEGGE_STATO_ATTOLEGGE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_LEGGE_STATO_ATTOLEGGESTATOID_GENERATOR")
	@Column(name="attolegge_stato_id")
	private Integer attoleggeStatoId;

	@Column(name="attolegge_stato_code")
	private String attoleggeStatoCode;

	@Column(name="attolegge_stato_desc")
	private String attoleggeStatoDesc;

	//bi-directional many-to-one association to SiacRAttoLeggeStato
	@OneToMany(mappedBy="siacDAttoLeggeStato")
	private List<SiacRAttoLeggeStato> siacRAttoLeggeStatos;

	public SiacDAttoLeggeStato() {
	}

	public Integer getAttoleggeStatoId() {
		return this.attoleggeStatoId;
	}

	public void setAttoleggeStatoId(Integer attoleggeStatoId) {
		this.attoleggeStatoId = attoleggeStatoId;
	}

	public String getAttoleggeStatoCode() {
		return this.attoleggeStatoCode;
	}

	public void setAttoleggeStatoCode(String attoleggeStatoCode) {
		this.attoleggeStatoCode = attoleggeStatoCode;
	}

	public String getAttoleggeStatoDesc() {
		return this.attoleggeStatoDesc;
	}

	public void setAttoleggeStatoDesc(String attoleggeStatoDesc) {
		this.attoleggeStatoDesc = attoleggeStatoDesc;
	}

	public List<SiacRAttoLeggeStato> getSiacRAttoLeggeStatos() {
		return this.siacRAttoLeggeStatos;
	}

	public void setSiacRAttoLeggeStatos(List<SiacRAttoLeggeStato> siacRAttoLeggeStatos) {
		this.siacRAttoLeggeStatos = siacRAttoLeggeStatos;
	}

	public SiacRAttoLeggeStato addSiacRAttoLeggeStato(SiacRAttoLeggeStato siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().add(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacDAttoLeggeStato(this);

		return siacRAttoLeggeStato;
	}

	public SiacRAttoLeggeStato removeSiacRAttoLeggeStato(SiacRAttoLeggeStato siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().remove(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacDAttoLeggeStato(null);

		return siacRAttoLeggeStato;
	}

	@Override
	public Integer getUid() {
		return attoleggeStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		attoleggeStatoId = uid;
	}

}