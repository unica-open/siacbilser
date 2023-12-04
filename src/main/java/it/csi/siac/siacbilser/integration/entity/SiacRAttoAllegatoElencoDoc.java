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
 * The persistent class for the siac_r_atto_allegato_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_elenco_doc")
@NamedQuery(name="SiacRAttoAllegatoElencoDoc.findAll", query="SELECT s FROM SiacRAttoAllegatoElencoDoc s")
public class SiacRAttoAllegatoElencoDoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_ELENCO_DOC_ATTOALEDOC_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_atto_allegato_elenco_doc_attoaleldoc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_ELENCO_DOC_ATTOALEDOC_ID_GENERATOR")
	@Column(name="attoaleldoc_id")
	private Integer attoaleldocId;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAttoAllegato siacTAttoAllegato;

	//bi-directional many-to-one association to SiacTElencoDoc
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDoc siacTElencoDoc;

	public SiacRAttoAllegatoElencoDoc() {
	}

	public Integer getAttoaleldocId() {
		return this.attoaleldocId;
	}

	public void setAttoaleldocId(Integer attoaleldocId) {
		this.attoaleldocId = attoaleldocId;
	}

	public SiacTAttoAllegato getSiacTAttoAllegato() {
		return this.siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAttoAllegato siacRAttoAllegato) {
		this.siacTAttoAllegato = siacRAttoAllegato;
	}

	public SiacTElencoDoc getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDoc siacTSoggetto) {
		this.siacTElencoDoc = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return attoaleldocId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoaleldocId = uid;
	}

}