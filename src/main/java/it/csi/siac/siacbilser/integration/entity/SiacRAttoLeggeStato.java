/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_atto_legge_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_legge_stato")
@NamedQuery(name="SiacRAttoLeggeStato.findAll", query="SELECT s FROM SiacRAttoLeggeStato s")
public class SiacRAttoLeggeStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_LEGGE_STATO_ATTATTOLEGGESTATOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_ATTO_LEGGE_STATO_ATT_ATTOLEGGE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_LEGGE_STATO_ATTATTOLEGGESTATOID_GENERATOR")
	@Column(name="att_attolegge_stato_id")
	private Integer attAttoleggeStatoId;

	//bi-directional many-to-one association to SiacDAttoLeggeStato
	@ManyToOne
	@JoinColumn(name="attolegge_stato_id")
	private SiacDAttoLeggeStato siacDAttoLeggeStato;

	//bi-directional many-to-one association to SiacTAttoLegge
	@ManyToOne
	@JoinColumn(name="attolegge_id")
	private SiacTAttoLegge siacTAttoLegge;

	public SiacRAttoLeggeStato() {
	}

	public Integer getAttAttoleggeStatoId() {
		return this.attAttoleggeStatoId;
	}

	public void setAttAttoleggeStatoId(Integer attAttoleggeStatoId) {
		this.attAttoleggeStatoId = attAttoleggeStatoId;
	}

	public SiacDAttoLeggeStato getSiacDAttoLeggeStato() {
		return this.siacDAttoLeggeStato;
	}

	public void setSiacDAttoLeggeStato(SiacDAttoLeggeStato siacDAttoLeggeStato) {
		this.siacDAttoLeggeStato = siacDAttoLeggeStato;
	}

	public SiacTAttoLegge getSiacTAttoLegge() {
		return this.siacTAttoLegge;
	}

	public void setSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		this.siacTAttoLegge = siacTAttoLegge;
	}

	@Override
	public Integer getUid() {
		return attAttoleggeStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		attAttoleggeStatoId = uid;
	}

}