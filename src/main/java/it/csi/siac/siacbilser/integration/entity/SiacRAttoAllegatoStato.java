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
 * The persistent class for the siac_r_atto_allegato_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_stato")
@NamedQuery(name="SiacRAttoAllegatoStato.findAll", query="SELECT s FROM SiacRAttoAllegatoStato s")
public class SiacRAttoAllegatoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_STATO_ATTOALRSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTO_ALLEGATO_STATO_ATTOAL_R_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_STATO_ATTOALRSTATOID_GENERATOR")
	@Column(name="attoal_r_stato_id")
	private Integer attoalRStatoId;

	//bi-directional many-to-one association to SiacDAttoAllegatoStato
	@ManyToOne
	@JoinColumn(name="attoal_stato_id")
	private SiacDAttoAllegatoStato siacDAttoAllegatoStato;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAttoAllegato siacTAttoAllegato;

	public SiacRAttoAllegatoStato() {
	}

	public Integer getAttoalRStatoId() {
		return this.attoalRStatoId;
	}

	public void setAttoalRStatoId(Integer attoalRStatoId) {
		this.attoalRStatoId = attoalRStatoId;
	}
	
	public SiacDAttoAllegatoStato getSiacDAttoAllegatoStato() {
		return this.siacDAttoAllegatoStato;
	}

	public void setSiacDAttoAllegatoStato(SiacDAttoAllegatoStato siacDAttoAllegatoStato) {
		this.siacDAttoAllegatoStato = siacDAttoAllegatoStato;
	}

	public SiacTAttoAllegato getSiacTAttoAllegato() {
		return this.siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAttoAllegato siacRAttoAllegato) {
		this.siacTAttoAllegato = siacRAttoAllegato;
	}

	@Override
	public Integer getUid() {
		return attoalRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalRStatoId = uid;
	}

}