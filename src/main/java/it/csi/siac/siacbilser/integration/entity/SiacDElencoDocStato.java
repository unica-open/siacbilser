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
 * The persistent class for the siac_d_elenco_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_elenco_doc_stato")
@NamedQuery(name="SiacDElencoDocStato.findAll", query="SELECT s FROM SiacDElencoDocStato s")
public class SiacDElencoDocStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ELENCO_DOC_STATO_ELDOCSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ELENCO_DOC_STATO_ELDOC_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ELENCO_DOC_STATO_ELDOCSTATOID_GENERATOR")
	@Column(name="eldoc_stato_id")
	private Integer eldocStatoId;

	@Column(name="eldoc_stato_code")
	private String eldocStatoCode;

	@Column(name="eldoc_stato_desc")
	private String eldocStatoDesc;


	//bi-directional many-to-one association to SiacRElencoDocStato
	@OneToMany(mappedBy="siacDElencoDocStato")
	private List<SiacRElencoDocStato> siacRElencoDocStatos;

	public SiacDElencoDocStato() {
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



	public List<SiacRElencoDocStato> getSiacRElencoDocStatos() {
		return this.siacRElencoDocStatos;
	}

	public void setSiacRElencoDocStatos(List<SiacRElencoDocStato> siacRElencoDocStatos) {
		this.siacRElencoDocStatos = siacRElencoDocStatos;
	}

	public SiacRElencoDocStato addSiacRElencoDocStato(SiacRElencoDocStato siacRElencoDocStato) {
		getSiacRElencoDocStatos().add(siacRElencoDocStato);
		siacRElencoDocStato.setSiacDElencoDocStato(this);

		return siacRElencoDocStato;
	}

	public SiacRElencoDocStato removeSiacRElencoDocStato(SiacRElencoDocStato siacRElencoDocStato) {
		getSiacRElencoDocStatos().remove(siacRElencoDocStato);
		siacRElencoDocStato.setSiacDElencoDocStato(null);

		return siacRElencoDocStato;
	}

	@Override
	public Integer getUid() {
		return eldocStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.eldocStatoId = uid;
		
	}

}