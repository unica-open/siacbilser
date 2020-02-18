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
 * The persistent class for the siac_r_elenco_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_elenco_doc_stato")
@NamedQuery(name="SiacRElencoDocStato.findAll", query="SELECT s FROM SiacRElencoDocStato s")
public class SiacRElencoDocStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ELENCO_DOC_STATO_ELDOCRSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ELENCO_DOC_STATO_ELDOC_R_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ELENCO_DOC_STATO_ELDOCRSTATOID_GENERATOR")
	@Column(name="eldoc_r_stato_id")
	private Integer eldocRStatoId;

	//bi-directional many-to-one association to SiacDElencoDocStato
	@ManyToOne
	@JoinColumn(name="eldoc_stato_id")
	private SiacDElencoDocStato siacDElencoDocStato;

	//bi-directional many-to-one association to SiacTElencoDoc
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDoc siacTElencoDoc;

	public SiacRElencoDocStato() {
	}

	public Integer getEldocRStatoId() {
		return this.eldocRStatoId;
	}

	public void setEldocRStatoId(Integer eldocRStatoId) {
		this.eldocRStatoId = eldocRStatoId;
	}

	public SiacDElencoDocStato getSiacDElencoDocStato() {
		return this.siacDElencoDocStato;
	}

	public void setSiacDElencoDocStato(SiacDElencoDocStato siacDElencoDocStato) {
		this.siacDElencoDocStato = siacDElencoDocStato;
	}

	public SiacTElencoDoc getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDoc siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	@Override
	public Integer getUid() {
		return eldocRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.eldocRStatoId = uid;
	}

}