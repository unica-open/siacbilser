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
 * The persistent class for the siac_d_atto_allegato_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_allegato_stato")
@NamedQuery(name="SiacDAttoAllegatoStato.findAll", query="SELECT s FROM SiacDAttoAllegatoStato s")
public class SiacDAttoAllegatoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_ALLEGATO_STATO_ATTOALSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_ALLEGATO_STATO_ATTOAL_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_ALLEGATO_STATO_ATTOALSTATOID_GENERATOR")
	@Column(name="attoal_stato_id")
	private Integer attoalStatoId;

	@Column(name="attoal_stato_code")
	private String attoalStatoCode;

	@Column(name="attoal_stato_desc")
	private String attoalStatoDesc;

	//bi-directional many-to-one association to SiacRAttoAllegatoStato
	@OneToMany(mappedBy="siacDAttoAllegatoStato")
	private List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos;

	public SiacDAttoAllegatoStato() {
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

	public List<SiacRAttoAllegatoStato> getSiacRAttoAllegatoStatos() {
		return this.siacRAttoAllegatoStatos;
	}

	public void setSiacRAttoAllegatoStatos(List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos) {
		this.siacRAttoAllegatoStatos = siacRAttoAllegatoStatos;
	}

	public SiacRAttoAllegatoStato addSiacRAttoAllegatoStato(SiacRAttoAllegatoStato siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().add(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(this);

		return siacRAttoAllegatoStato;
	}

	public SiacRAttoAllegatoStato removeSiacRAttoAllegatoStato(SiacRAttoAllegatoStato siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().remove(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(null);

		return siacRAttoAllegatoStato;
	}

	@Override
	public Integer getUid() {
		return attoalStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalStatoId = uid;
	}

}